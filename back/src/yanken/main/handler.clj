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
   [yanken.main.helpers :as yh]
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

;; --- HELPERS

(defn- resolve-room
  [state]
  (when-let [room (:current-room state)]
    (let [sessions-xf (comp (keep #(get-in state [:sessions %]))
                            (map #(dissoc % :connection-id :room-id)))
          sessions    (sequence sessions-xf (:players room))]
      (assoc room :sessions (d/index-by :id sessions)))))

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
        (a/>! output-ch {:type "notification"
                         :name "roomUpdate"
                         :room room}))
      (recur (rest players)))))

;; --- EVENTS IMPL

(defmulti handler (fn [context messate] [(:type messate) (:name messate)]))

(yh/defmethod handler :default
  [context message]
  (l/warn :hint "unrecognized message" :message message)
  nil)

(yh/defmethod handler ["connect" nil]
  [ws message]
  (swap! yst/state yst/connect ws)
  nil)

(yh/defmethod handler ["disconnect" nil]
  [ws message]
  (let [state (swap! yst/state yst/disconnect ws)]
    (when-let [room (resolve-room state)]
      (let [players (->> (:players room) (keep (partial resolve-player state)))]
        (a/<! (notify-room-update players room))))
    nil))

(yh/defmethod handler ["request" "echo"]
  [{:keys [out-ch]} message]
  message)

(yh/defmethod handler ["request" "hello"]
  [{:keys [local] :as ws} {:keys [session-id player-name player-avatar]}]
  (let [state   (swap! yst/state yst/authenticate (:id ws)
                       session-id player-name player-avatar)
        session (-> (:current-session state)
                    (dissoc :connection-id))

        result  {:session session
                 :session-created (:current-session-created state)
                 :room (resolve-room state)}]

    (-> (d/without-nils result)
        (with-meta {:session-id (:id session)}))))

;; --- JOIN ROOM

(yh/defmethod handler ["notification" "joinBots"]
  [{:keys [session-id] :as ws} {:keys [room-id bot-num bot-join-timeout] :as message}]

  ;; Session ID is mandatory for join-room
  (when-not session-id
    (ex/raise :type :validation
              :code :session-not-initialized
              :hint "missing hello event"))

  (let [total   (if (and (integer? bot-num)
                         (pos? bot-num))
                   bot-num
                   3)
        timeout (or bot-join-timeout 1000)]

    (dotimes [_ total]
      (let [state   (swap! yst/state yst/add-bot-session (rand-int 10000))
            session (:current-session state)
            state   (swap! yst/state yst/join-room (:id session) room-id)
            room    (resolve-room state)
            players (->> (:players room)
                         (keep (partial resolve-player state)))]

        (a/<! (notify-room-update players room))
        (a/<! (a/timeout timeout))))))

(yh/defmethod handler ["request" "joinRoom"]
  [{:keys [session-id] :as ws} {:keys [room-id] :as message}]

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
    {:room room}))

(defn- start-game-loop
  [{room-id :id players :players opts :options}]
  (let [players (keep (partial resolve-player @yst/state) players)]
    (a/go-loop [round 1]
      (let [state (swap! yst/state yst/prepare-round room-id round)
            room  (resolve-room state)]

        ;; Pairing stage
        (a/<! (notify-room-update players room))
        (a/<! (a/timeout (:pairing-screen-timeout opts)))

        ;; Game Stage
        (let [state (swap! yst/state yst/update-room-stage room-id "game")
              room  (resolve-room state)]
          (a/<! (notify-room-update players room))
          (a/<! (a/timeout (:game-screen-timeout opts))))

        ;; Game End Stage
        (let [state (swap! yst/state yst/finish-current-round room-id)
              room  (resolve-room state)]
          (a/<! (notify-room-update players room))
          (a/<! (a/timeout (:game-end-screen-timeout opts)))

          (if (yst/is-last-round? room)
            (let [state (swap! yst/state yst/end-room room-id)
                  room  (resolve-room state)]
              (a/<! (notify-room-update players room))
              round)
            (let [state (swap! yst/state yst/update-room-stage room-id "result")
                  room  (resolve-room state)]
              (a/<! (notify-room-update players room))
              (a/<! (a/timeout (:result-screen-timeout opts)))
              (recur (inc round)))))))))

(yh/defmethod handler ["request" "startGame"]
  [{:keys [session-id] :as ws} {:keys [options] :as message}]
  (when-not session-id
    (ex/raise :type :validation
              :code :session-not-initialized
              :hint "missing hello event"))

  (let [state (swap! yst/state yst/start-game session-id options)
        room  (:current-room state)]

    (a/go
      (l/debug :action "game-start" :room (:id room) :players (count (:players room)))
      (l/trace :action "game-start" :room (:id room) :options (:options room))
      (let [rounds (a/<! (start-game-loop room))]
        (l/debug :action "game-end" :room (:id room) :rounds rounds)))

    nil))

(yh/defmethod handler ["request" "sendTurn"]
  [{:keys [session-id] :as ws} {:keys [result] :as message}]
  (swap! yst/state yst/update-round session-id result)
  nil)
