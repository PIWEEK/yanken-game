;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.websocket
  "General purpose websockets interface."
  (:require
   [clojure.core.async :as a]
   [ring.adapter.jetty9 :as jetty]
   [yanken.config :as cf]
   [yanken.util.async :as aa]
   [yanken.util.exceptions :as ex]
   [yanken.util.json :as json]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt]
   [yanken.util.uuid :as uuid])
  (:import
   java.util.concurrent.ForkJoinPool))

(defn- ws-send!
  [conn data]
  (try
    (when (jetty/connected? conn)
      (jetty/send! conn data)
      true)
    (catch java.lang.NullPointerException _e
      false)))

(defn- start-input-loop
  [ws]
  (let [{:keys [handler input output]} @ws]
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
  [{:keys [conn executor output]}]
  (a/go-loop []
    (let [val (a/<! output)]
      (when (some? val)
        (when (a/<! (aa/thread-call executor #(ws-send! conn (json/encode-str val))))
          (recur))))))


;; TODO: PING/PONG control

(defn wrap
  ([handler] (wrap handler {}))
  ([handler {:keys [rcv-buff-size snd-buff-size] :or {snd-buff-size 32 rcv-buff-size 32}}]
   (fn [request]
     (let [rcv-ch   (a/chan rcv-buff-size)
           out-ch   (a/chan snd-buff-size)
           executor (ForkJoinPool/commonPool)

           on-connect
           (fn [conn]
             (l/info :hint "on-connect" :client (jetty/remote-addr conn))
             (let [ws (atom {:output out-ch
                             :input rcv-ch
                             :executor executor
                             :handler handler
                             :conn conn
                             :id (uuid/next)})]

               ;; Forward all messages from out-ch to the websocket
               ;; connection
               (start-output-loop @ws)

               ;; React on messages received from the client
               (start-input-loop ws)))

           on-error
           (fn [conn err]
             (l/info :hint "on-error" :err (str err))
             (a/close! out-ch)
             (a/close! rcv-ch))

           on-close
           (fn [conn status reason]
             (l/info :hint "on-close" :status status :reason reason)
             (a/close! out-ch)
             (a/close! rcv-ch))

           on-message
           (fn [conn message]
             (let [message (json/decode-str message)]
               (l/info :hint "on-message" :message message)
               (when-not (a/offer! rcv-ch message)
                 (l/warn :msg "drop messages"))))

           on-ping
           (fn [conn buffer]
             (l/info :hint "on-ping" :buffer buffer))

           on-pong
           (fn [conn buffer]
             (l/info :hint "on-ping" :buffer buffer))
           ]
       {:on-connect on-connect
        :on-error on-error
        :on-close on-close
        :on-text on-message
        :on-ping on-ping
        :on-pong on-pong
        :on-bytes (constantly nil)}))))
