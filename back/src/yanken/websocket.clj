;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>
(ns yanken.websocket
  "General purpose websockets interface."
  (:require
   [clojure.core.async :as a]
   [yanken.config :as cf]
   [yanken.util.async :as aa]
   [yanken.util.exceptions :as ex]
   [yanken.util.json :as json]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt]
   [yanken.util.uuid :as uuid]
   [yetti.websocket :as yws])
  (:import
   java.nio.ByteBuffer))

(defn- ws-send!
  "Fully asynchronous websocket send operation."
  [ws s]
  (let [ch (a/chan 1)]
    (yws/send! ws s (fn [e]
                      (when e (a/offer! ch e))
                      (a/close! ch)))
    ch))

(defn- ws-ping!
  ([ws] (ws-ping! ws (ByteBuffer/allocate 0)))
  ([ws s]
   (let [ch (a/chan 1)]
     (yws/ping! ws s (fn [e]
                       (when e (a/offer! ch e))
                       (a/close! ch)))
     ch)))

(defn- start-input-loop
  [ws handler]
  (let [{:keys [input output]} @ws]
    (a/go
      (a/<! (handler @ws {:type "connect"}))
      (a/<! (a/go-loop []
              (when-let [request (a/<! input)]
                (let [response (a/<! (handler @ws request))]
                  (cond
                    (ex/ex-info? response)
                    (a/>! output {:type "error"
                                  :request-id (:request-id request)
                                  :error (ex-data response)})

                    (ex/exception? response)
                    (a/>! output {:type "error"
                                  :request-id (:request-id request)
                                  :error {:message (ex-message response)}})

                    (map? response)
                    (let [mdata (meta response)]
                      (when (map? mdata) (swap! ws merge mdata))
                      (a/>! output (assoc response
                                          :type "response"
                                          :name (or (:name response) (:name request))
                                          :request-id (:request-id request))))

                    (nil? response)
                    (when-let [request-id (:request-id request)]
                      (a/>! output {:type "response"
                                    :name (:name request)
                                    :request-id request-id}))))

                (recur))))
      (a/<! (handler @ws {:type "disconnect"})))))

(defn- start-output-loop
  [{:keys [conn output input]}]
  (a/go-loop []
    (when-let [val (a/<! output)]
      (a/<! (ws-send! conn (json/encode-str val)))
      (recur))))


(defn- start-ping-pong-loop
  [{:keys [conn output input close on-close pong]}]
  (a/go-loop []
    (let [[val port] (a/alts! [close (a/timeout 2500)])]
      (when (and (yws/connected? conn) (not= port close))
        (a/<! (ws-ping! conn))
        (recur))))

  (a/go-loop []
    (let [[val port] (a/alts! [pong (a/timeout 10000)])]
      (cond
        (and (= port pong) (not (nil? val))) (recur)
        (and (= port pong) (nil? val))       nil
        :else                                (on-close conn -1 "pong-timeout")))))


(defn wrap
  ([handler] (wrap handler {}))
  ([handler {:keys [input-buff-size output-buff-size]
             :or {input-buff-size 32
                  output-buff-size 32}}]
   (fn [request]
     (let [input-ch  (a/chan input-buff-size)
           output-ch (a/chan output-buff-size)
           pong-ch   (a/chan (a/sliding-buffer 6))
           close-ch  (a/chan)

           on-error
           (fn [conn err]
             (l/info :hint "on-error" :err (str err))
             (a/close! close-ch)
             (a/close! pong-ch)
             (a/close! output-ch)
             (a/close! input-ch))

           on-close
           (fn [conn status reason]
             (l/info :hint "on-close" :status status :reason reason)
             (a/close! close-ch)
             (a/close! pong-ch)
             (a/close! output-ch)
             (a/close! input-ch))

           on-connect
           (fn [conn]
             (l/debug :hint "on-connect" :client (yws/remote-addr conn))
             (let [ws (atom {:output output-ch
                             :input input-ch
                             :conn conn
                             :id (uuid/next)})]

               ;; Properly handle keepalive
               (yws/idle-timeout! conn (dt/duration 10000))
               (-> @ws
                   (assoc :close close-ch)
                   (assoc :pong pong-ch)
                   (assoc :on-close on-close)
                   (start-ping-pong-loop))

               ;; Forward all messages from output-ch to the websocket
               ;; connection
               (start-output-loop @ws)

               ;; React on messages received from the client
               (start-input-loop ws handler)))

           on-message
           (fn [conn message]
             (let [message (json/decode-str message)]
               (l/debug :hint "on-message" :message message)
               (when-not (a/offer! input-ch message)
                 (l/warn :msg "drop messages"))))

           on-ping
           (fn [conn buffer]
             ;; (l/debug :hint "on-ping" :buffer buffer)
             )

           on-pong
           (fn [conn buffer]
             ;; (l/debug :hint "on-pong" :buffer buffer)
             (a/>!! pong-ch :pong))
           ]
       {:on-connect on-connect
        :on-error on-error
        :on-close on-close
        :on-text on-message
        :on-ping on-ping
        :on-pong on-pong
        :on-bytes (constantly nil)}))))
