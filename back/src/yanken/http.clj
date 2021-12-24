;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.http
  (:require
   [clojure.java.io :as io]
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [integrant.core :as ig]
   [yanken.http.middleware :as mw]
   [yanken.main :as main]
   [yanken.util.data :as d]
   [yanken.util.exceptions :as ex]
   [yanken.util.logging :as l]
   [yanken.util.spec :as us]
   [yanken.websocket :as ws]
   [yetti.adapter :as yt]
   [yetti.websocket :as yws]))

;; --- SERVER

(s/def ::handler fn?)
(s/def ::port ::us/integer)

(defmethod ig/pre-init-spec ::server [_]
  (s/keys :req-un [::port ::handler]))

(defmethod ig/prep-key ::server [_ cfg]
  (d/without-nils cfg))

(defmethod ig/init-key ::server
  [_ {:keys [handler router ws port host name metrics] :as opts}]
  (l/info :msg "starting http server" :port port :host host)
  (let [options {:http/port port
                 :http/host host
                 :http/handle-forwarded true
                 :join false}
        server  (-> handler
                    (yt/server options)
                    (yt/start! options))]
    (assoc opts :server server)))

(defmethod ig/halt-key! ::server
  [_ {:keys [server host port] :as opts}]
  (l/info :msg "stoping http server" :host host :port port)
  (yt/stop! server))

;; --- HANDLER

(defn serve-test-page
  [request]
  {:status 200
   :headers {"content-type" "text/html"}
   :body (io/input-stream (io/resource "index.html"))})

(defn handler
  [request]
  (if (yws/upgrade-request? request)
    (let [handler (ws/wrap main/handler)]
      (yws/upgrade request handler))
    (serve-test-page request)))

(defmethod ig/init-key ::handler
  [_ _]
  (mw/wrap-cors handler))
