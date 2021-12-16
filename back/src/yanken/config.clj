;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.config
  "A configuration management."
  (:refer-clojure :exclude [get])
  (:require
   [yanken.util.data :as d]
   [yanken.util.exceptions :as ex]
   [yanken.util.spec :as us]
   [yanken.util.time :as dt]
   [clojure.core :as c]
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [clojure.spec.alpha :as s]
   [cuerdas.core :as str]
   [environ.core :refer [env]]
   [integrant.core :as ig]))

(prefer-method print-method
               clojure.lang.IRecord
               clojure.lang.IDeref)

(prefer-method pprint/simple-dispatch
               clojure.lang.IPersistentMap
               clojure.lang.IDeref)

(defmethod ig/init-key :default
  [_ data]
  (d/without-nils data))

(defmethod ig/prep-key :default
  [_ data]
  (if (map? data)
    (d/without-nils data)
    data))

(def defaults
  {:http-server-port 11010
   :http-server-host "localhost"
   :asserts-enabled true})

(s/def ::http-server-port ::us/integer)
(s/def ::http-server-host ::us/string)
(s/def ::asserts-enabled ::us/boolean)

(s/def ::config
  (s/keys :opt-un [::http-server-port
                   ::http-server-host
                   ::asserts-enabled]))

(defn read-env
  [prefix]
  (let [prefix (str prefix "-")
        len    (count prefix)]
    (reduce-kv
     (fn [acc k v]
       (cond-> acc
         (str/starts-with? (name k) prefix)
         (assoc (keyword (subs (name k) len)) v)))
     {}
     env)))

(defn- read-config
  []
  (try
    (->> (read-env "yanken")
         (merge defaults)
         (us/conform ::config))
    (catch Throwable e
      (when (ex/ex-info? e)
        (println ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;")
        (println "Error on validating configuration:")
        (println (:explain (ex-data e))
        (println ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;")))
      (throw e))))

(def ^:dynamic config (read-config))

(defn get
  "A configuration getter. Helps code be more testable."
  ([key]
   (c/get config key))
  ([key default]
   (c/get config key default)))

;; Set value for all new threads bindings.
(alter-var-root #'*assert* (constantly (get :asserts-enabled)))


