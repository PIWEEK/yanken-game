;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.init
  "Main entry point."
  (:require
   [integrant.core :as ig]
   [yanken.config :as cf]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt])
  (:gen-class))

(def system-config
  {:yanken.http/server
   {:port (cf/get :http-server-port)
    :host (cf/get :http-server-host "localhost")
    :handler (ig/ref :yanken.http/handler)}

   :yanken.http/handler {}})

(def system nil)

(defn start
  []
  (ig/load-namespaces system-config)
  (alter-var-root #'system (fn [sys]
                             (when sys (ig/halt! sys))
                             (-> system-config
                                 (ig/prep)
                                 (ig/init))))

  (l/info :msg "welcome to yanken"))

(defn stop
  []
  (alter-var-root #'system (fn [sys]
                             (when sys (ig/halt! sys))
                             nil)))

(defn -main
  [& _args]
  (start))
