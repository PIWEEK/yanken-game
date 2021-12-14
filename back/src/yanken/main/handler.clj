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

(defmulti handler (fn [context messate] [(:type messate) (:name messate)]))

(defmethod handler :default
  [context message]
  (l/warn :hint "unrecognized message" :message message)
  (a/go nil))

(defmethod handler ["connect" nil]
  [ws message]
  (aa/go-try
   (swap! yst/state yst/connect ws)
   nil))

(defmethod handler ["disconnect" nil]
  [ws message]
  (aa/go-try
   (swap! yst/state yst/disconnect ws)))

(defmethod handler ["request" "echo"]
  [{:keys [out-ch]} message]
  (a/go message))

(defmethod handler ["request" "hello"]
  [{:keys [local] :as ws} message]
  (aa/go-try
   (let [state  (swap! yst/state yst/create-or-update-session ws message)
         result {:avatar-id (:current-avatar-id state)
                 :session-id (:current-session-id state)}]
     (with-meta result
       {:session-id (:session-id result)}))))

;; --- JOIN ROOM

(defn- resolve-room
  [state]
  (let [room     (:current-room state)
        lookup   #(get-in state [:sessions %])
        sessions (->> (:players room)
                      (keep lookup))]
    (assoc room :sessions sessions)))

(defn- resolve-player
  [state session-id]
  (when-let [session (get-in state [:sessions session-id])]
    (when-let [connection (get-in state [:connections (:connection-id session)])]
      (assoc session :connection connection))))

(defn- resolve-connection
  [state session-id]
  (when-let [connection-id (get-in state [:sessions session-id :connection-id])]
    (get-in state [:connections connection-id])))

(defn- notify-room-update
  [{:keys [session-id] :as ws} state]
  (let [room (resolve-room state)]
    (a/go-loop [players (->> (:players room)
                             (filter #(not= % session-id))
                             (map (partial resolve-player state)))]

      (when-let [player (first players)]
        (when-let [connection (:connection player)]
          (let [output-ch (:yanken.websockets/output connection)]
            (prn "notify-room-update" player)
            (a/>! output-ch {:type "notification"
                             :name "roomUpdate"
                             :room room})))
        (recur (rest players))))))


(defmethod handler ["request" "joinRoom"]
  [ws {:keys [room-id] :as message}]
  (aa/go-try
   (let [state (swap! yst/state yst/join-room ws message)]
     (prn "KAKAKAK1" (:current-room state))
     (a/<! (notify-room-update ws state))
     (prn "KAKAKAK2" (:current-room state))

     (:current-room state))))

;; (defmethod handler "startGame"
;;   [ws message]
;;   (letfn [(notify-round [state]
;;             (let [room (:current-room state)]
;;               (a/go-loop [players (seq players)]
;;                 (when-let [id (first players)]
;;                   (when-let [connection (resolve-connection state id)]
;;                     (let [output-ch (:yanken.websockets/output connection)]
;;                       (a/>! output-ch {:type "notification"
;;                                        :name "roundStarted"
;;                                        :room room}


;;   (aa/go-try
;;    (let [state (swap! yst/state yst/start-game ws)]
;;      (loop [round 0]
;;        (let [state (swap! yst/state yst/prepare-round ws round)]
;;          (a/<! (notifiy-round state))

