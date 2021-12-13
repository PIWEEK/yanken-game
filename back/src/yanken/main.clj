;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.main
  (:require
   [clojure.core.async :as a]
   [yanken.config :as cf]
   [yanken.util.async :as aa]
   [yanken.util.json :as json]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt])
  (:import
   java.util.concurrent.ForkJoinPool))

(defmulti handler (fn [context messate] (:type messate)))

(defmethod handler :default
  [context message]
  (l/warn :hint "unrecognized message" :message message)
  (a/go nil))

(defmethod handler :connect
  [context message]
  (a/go nil))

(defmethod handler :disconnect
  [context message]
  (a/go nil))

(defmethod handler "EchoRequest"
  [{:keys [out-ch]} message]
  (a/go
    (a/>! out-ch (assoc message :type "EchoResponse"))))

;; (defmethod handler "CreateGameRequest"
;;   [{:keys [out-ch]} message]
;;   (let [session-id (get message :sessionId)]))

