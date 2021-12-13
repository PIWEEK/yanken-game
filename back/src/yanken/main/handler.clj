;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.main.handler
  (:require
   [clojure.core.async :as a]
   [yanken.config :as cf]
   [yanken.main.state :as yst]
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
   (swap! yst/state yst/connect ws)
   nil))

(defmethod handler "disconnect"
  [ws message]
  (aa/go-try
   (swap! yst/state yst/disconnect ws)))

(defmethod handler "echo"
  [{:keys [out-ch]} message]
  (a/go message))

(defmethod handler "hello"
  [{:keys [local] :as ws} message]
  (aa/go-try
   (let [state  (swap! yst/state yst/create-or-update-session ws message)
         result {:avatar-id (:current-avatar-id state)
                 :session-id (:current-session-id state)}]
     (with-meta result
       {:session-id (:session-id result)}))))

;; --- JOIN ROOM

(defn- resolve-connection
  [state session-id]
  (when-let [connection-id (get-in state [:sessions session-id :connection-id])]
    (get-in state [:connections connection-id])))

(defn- notify-room-update
  [{:keys [session-id] :as ws} state {:keys [players] :as room}]
  (a/go-loop [players (filter #(not= % session-id) (:players room))]
    (when-let [id (first players)]
      (when-let [connection (resolve-connection state id)]
        (let [output-ch (:yanken.websockets/output connection)]
          (a/>! output-ch {:type "notification"
                           :name "roomUpdate"
                           :room room})
          (recur (rest players)))))))

(defmethod handler "joinRoom"
  [ws {:keys [room-id] :as message}]
  (aa/go-try
   (let [state (swap! yst/state yst/join-room ws message)
         room  (:current-room state)]
     (a/<! (notify-room-update ws state room))
     room)))

;; (defmethod handler "startGame"
;;   [{:keys [out-ch] :as ws} {:keys [room-id] :as message}]
;;   (aa/go-try
;;    (let [state   (swap! yst/state yst/start-game ws message)
;;          room    (get-in state [:rooms room-id])
;;          players (
;;      (loop [participants (seq (:participants room))]
;;        (when-let [id (first participants)]
;;          (a/>! out-ch {:type "notification"
;;                        :name "gameStarted"
;;                        :

