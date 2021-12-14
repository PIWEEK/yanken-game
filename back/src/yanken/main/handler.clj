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
   [yanken.util.data :as d]
   [yanken.util.async :as aa]
   [yanken.util.exceptions :as ex]
   [yanken.util.json :as json]
   [yanken.util.logging :as l]
   [yanken.util.time :as dt]
   [yanken.util.uuid :as uuid])
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

(defn- resolve-room
  [state]
  (when-let [room (:current-room state)]
    (let [lookup   #(get-in state [:sessions %])
          sessions (sequence (comp (keep #(get-in state [:sessions %]))
                                   (map #(dissoc % :connection-id :room-id)))
                             (:players room))]
      (assoc room :sessions (d/index-by :id (cons yst/bot-session sessions))))))

(defmethod handler ["request" "hello"]
  [{:keys [local] :as ws} {:keys [session-id player-name]}]
  (aa/go-try
   (let [ state  (swap! yst/state yst/update-session (:id ws) session-id player-name)
         result  {:avatar-id (:current-avatar-id state)
                  :session-id (:current-session-id state)
                  :room (resolve-room state)}]

     (-> (d/without-nils result)
         (with-meta {:session-id (:session-id result)})))))

;; --- JOIN ROOM

(defn- resolve-player
  [state session-id]
  (when-let [session (get-in state [:sessions session-id])]
    (let [connection (get-in state [:connections (:connection-id session)])]
      (cond-> session
        (some? connection) (assoc :connection connection)))))

(defn- resolve-connection
  [state session-id]
  (when-let [connection-id (get-in state [:sessions session-id :connection-id])]
    (get-in state [:connections connection-id])))

(defn- notify-room-update
  [players room]
  (a/go-loop [players (seq players)]
    (when-let [player (first players)]

      (when-let [output-ch (some-> player :connection :output)]
        (l/debug :fn "notify-room-update" :room (:id room) :player (:id player))

        (a/>! output-ch {:type "notification"
                         :name "roomUpdate"
                         :room room}))
      (recur (rest players)))))

(defmethod handler ["request" "joinRoom"]
  [{:keys [session-id] :as ws} {:keys [room-id] :as message}]
  (aa/go-try

   ;; Session ID is mandatory for join-room
   (when-not session-id
     (ex/raise :type :validation
               :code :session-not-initialized
               :hint "missing hello event"))

   (let [state   (swap! yst/state yst/join-room session-id room-id)
         room    (resolve-room state)
         players (->> (:players room)
                      (filter #(not= % session-id))
                      (keep (partial resolve-player state)))]

     (a/<! (notify-room-update players room))
     {:room room})))

(def ^:const round-timeout 10000)
(def ^:const post-round-timeout 3000)

(defn- start-game!
  [room-id]
  (a/go-loop [round 0]
    (let [state   (swap! yst/state yst/prepare-round room-id round)
          room    (resolve-room state)
          opts    (:options room)
          players (->> (:players room)
                       (keep (partial resolve-player state)))]

      (l/debug :action "game" :round 0 :status (:status room))
      (if (not= "ended" (:status room))
        (do
          (a/<! (notify-room-update players room))
          (a/<! (a/timeout (:round-timeout opts)))
          (let [state (swap! yst/state yst/finish-round room-id)
                room  (resolve-room state)]
            (a/<! (notify-room-update players room))
            (a/<! (a/timeout (:post-round-timeout opts)))
            (recur (inc round))))
        (do
          (a/<! (notify-room-update players room)))))))

(defmethod handler ["request" "startGame"]
  [{:keys [session-id] :as ws} {:keys [options] :as message}]
  (aa/go-try
   (when-not session-id
     (ex/raise :type :validation
               :code :session-not-initialized
               :hint "missing hello event"))

   (let [state   (swap! yst/state yst/start-game session-id options)
         room-id (-> state :current-room :id)]
     (start-game! room-id)
     nil)))

(defmethod handler ["request" "sendTurn"]
  [{:keys [session-id] :as ws} {:keys [result] :as message}]
  (aa/go-try
   (swap! yst/state yst/update-round session-id result)
   nil))
