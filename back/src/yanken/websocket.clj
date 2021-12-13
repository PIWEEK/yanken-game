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
   [yanken.util.json :as json]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt])
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
  [{:keys [rcv-ch out-ch handler] :as context}]
  (a/go-loop []
    (let [val (a/<! rcv-ch)]
      (when (some? val)
        (a/<! (handler context val))
        (recur)))))

(defn- start-output-loop
  [{:keys [conn executor out-ch]}]
  (a/go-loop []
    (let [val (a/<! out-ch)]
      (when (some? val)
        (when (a/<! (aa/thread-call executor #(ws-send! conn (json/encode-str val))))
          (recur))))))

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
             (let [context {:out-ch out-ch
                            :rcv-ch rcv-ch
                            :executor executor
                            :handler handler
                            :conn conn}]

               ;; Forward all messages from out-ch to the websocket
               ;; connection
               (start-output-loop context)

               ;; React on messages received from the client
               (start-input-loop context)))

           on-error
           (fn [conn err]
             (l/info :hint "on-error" :err (str err)))

           on-close
           (fn [conn status reason]
             (l/info :hint "on-close" :status status :reason reason))

           on-message
           (fn [conn message]
             (l/info :hint "on-message" :message message)
             (let [message (json/decode-str message)]
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
