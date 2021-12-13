;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.http
  (:require
   [yanken.util.data :as d]
   [yanken.util.exceptions :as ex]
   [yanken.util.logging :as l]
   [yanken.util.spec :as us]
   [yanken.http.middleware :as mw]
   [yanken.websocket :as ws]
   [yanken.main :as main]
   [clojure.spec.alpha :as s]
   [clojure.java.io :as io]
   [cuerdas.core :as str]
   [integrant.core :as ig]
   [ring.adapter.jetty9 :as jetty])
  (:import
   org.eclipse.jetty.server.Server
   org.eclipse.jetty.server.handler.ErrorHandler))

;; --- SERVER

(s/def ::handler fn?)
(s/def ::port ::us/integer)

(defmethod ig/pre-init-spec ::server [_]
  (s/keys :req-un [::port ::handler]))

(defmethod ig/prep-key ::server [_ cfg]
  (d/without-nils cfg))

(defmethod ig/init-key ::server
  [_ {:keys [handler router ws port name metrics] :as opts}]
  (l/info :msg "starting http server" :port port)
  (let [pre-start (fn [^Server server]
                    (let [handler (doto (ErrorHandler.)
                                    (.setShowStacks true)
                                    (.setServer server))]
                      (.setErrorHandler server ^ErrorHandler handler)))

        options   {:port port
                   :h2c? true
                   :join? false
                   :allow-null-path-info true
                   :configurator pre-start}

        server    (jetty/run-jetty handler options)]
    (assoc opts :server server)))

(defmethod ig/halt-key! ::server
  [_ {:keys [server port] :as opts}]
  (l/info :msg "stoping http server" :port port)
  (jetty/stop-server server))

;; --- HANDLER

(defn serve-test-page
  [request]
  {:status 200
   :headers {"content-type" "text/html"}
   :body (io/input-stream (io/resource "index.html"))})

(defn handler
  [request]
  (if (jetty/ws-upgrade-request? request)
    (let [ws-handler (ws/wrap main/handler)]
      (jetty/ws-upgrade-response ws-handler))
    (serve-test-page request)))

(defmethod ig/init-key ::handler
  [_ _]
  (mw/wrap-cors handler))
