;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.main
  (:require
   [clojure.core.async :as a]
   [yanken.config :as cf]
   [yanken.main.impl :as impl]
   [yanken.util.async :as aa]
   [yanken.util.json :as json]
   [yanken.util.uuid :as uuid]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt])
  (:import
   java.util.concurrent.ForkJoinPool))

(defonce state (atom {}))

(defmulti handler (fn [context messate] (:type messate)))

(defmethod handler :default
  [context message]
  (l/warn :hint "unrecognized message" :message message)
  (a/go nil))

(defmethod handler "connect"
  [ws message]
  (aa/go-try
   (swap! state impl/connect ws)
   nil))

(defmethod handler "disconnect"
  [ws message]
  (aa/go-try
   (swap! state impl/disconnect ws)))

(defmethod handler "echo"
  [{:keys [out-ch]} message]
  (a/go message))

(defmethod handler "hello"
  [ws params]
  (aa/go-try
   (let [state (swap! state impl/create-or-update-session ws params)]
     (if (:session-created state)
       {:avatar-id (:current-avatar-id state)
        :session-id (:current-session-id state)}
       (do :rejoin-game)))))

(defmethod handler "joinRoom"
  [{:keys [out-ch] :as ws} params]
  (aa/go-try
   (let [state (swap! state impl/join-room ws params)]
     (:current-room state))))

(defmethod handler "startGame"
  [{:keys [out-ch] :as ws} params]
  (aa/go-try
   (let [state (swap! state impl/start-game ws params)])))
