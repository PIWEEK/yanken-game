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
  (swap! yst/state yst/disconnect ws)
  nil)

(yh/defmethod handler ["request" "echo"]
  [{:keys [out-ch]} message]
  message)

;; (defn- resolve-room
;;   [state]
;;   (when-let [room (:current-room state)]
;;     (let [lookup   #(get-in state [:sessions %])
;;           real-sessions (sequence (comp (keep #(get-in state [:sessions %]))
;;                                         (map #(dissoc % :connection-id :room-id)))
;;                                   (:players room))
;;           bots-sessions (sequence (comp (filter yst/is-bot?)
;;                                         (map make-bot))
;;                                   (:players room))]

;;       (assoc room :sessions (->> (concat real-sessions bots-sessions)
;;                                  (d/index-by :id))))))

(defn- resolve-room
  [state]
  (when-let [room (:current-room state)]
    (let [sessions-xf (comp (keep #(get-in state [:sessions %]))
                            (map #(dissoc % :connection-id :room-id)))
          sessions    (sequence sessions-xf (:players room))]
      (assoc room :sessions (d/index-by :id sessions)))))

(yh/defmethod handler ["request" "hello"]
  [{:keys [local] :as ws} {:keys [session-id player-name player-avatar]}]
  (let [state   (swap! yst/state yst/update-session (:id ws)
                       session-id player-name player-avatar)
        session (-> (:current-session state)
                    (dissoc :connection-id))

        result  {:session session
                 :session-created (:current-session-created state)
                 :room (resolve-room state)}]

    (-> (d/without-nils result)
        (with-meta {:session-id (:id session)}))))

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

(yh/defmethod handler ["request" "joinRoom"]
  [{:keys [session-id] :as ws} {:keys [room-id with-bots bot-join-timeout] :as message}]

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

    (when (and (integer? with-bots)
               (pos? with-bots))
      (let [bot-join-timeout (rand-int (or bot-join-timeout 1000))]
        (aa/go-try
         (loop [i with-bots]
           (when (> i 0)
             (let [state   (swap! yst/state yst/join-room-with-bot room-id i)
                   room    (resolve-room state)
                   players (keep (partial resolve-player state) (:players room))]
               (a/<! (notify-room-update players room))
               (a/<! (a/timeout bot-join-timeout))
               (recur (dec i))))))))

    {:room room}))

(defn- start-game-loop
  [{room-id :id players :players opts :options}]
  (let [players (keep (partial resolve-player @yst/state) players)]
    (a/go-loop [round 1]
      (let [state (swap! yst/state yst/prepare-round room-id round)
            room  (resolve-room state)]
        (l/debug :action "start-game-loop" :round round :status (:status room) :options opts)
        (if (not= "ended" (:status room))
          (do
            ;; Pairing stage
            (a/<! (notify-room-update players room))
            (a/<! (a/timeout (:pairing-screen-timeout opts)))

            ;; Game Stage
            (let [state (swap! yst/state yst/update-room-stage room-id :game)
                  room  (resolve-room state)]
              (a/<! (notify-room-update players room))
              (a/<! (a/timeout (:game-screen-timeout opts))))

            ;; Game End Timeout
            (let [state (swap! yst/state yst/finish-round room-id)
                  room  (resolve-room state)]
              (a/<! (notify-room-update players room))
              (a/<! (a/timeout (:game-end-screen-timeout opts))))

            ;; Result
            (let [state (swap! yst/state yst/update-room-stage room-id :result)
                  room  (resolve-room state)]
              (a/<! (notify-room-update players room))
              (a/<! (a/timeout (:result-screen-timeout opts))))

            (recur (inc round)))

          (a/<! (notify-room-update players room)))))))

(yh/defmethod handler ["request" "startGame"]
  [{:keys [session-id] :as ws} {:keys [options] :as message}]
  (when-not session-id
    (ex/raise :type :validation
              :code :session-not-initialized
              :hint "missing hello event"))

  (let [state (swap! yst/state yst/start-game session-id options)
        room  (:current-room state)]
    (start-game-loop room)
    nil))

(yh/defmethod handler ["request" "sendTurn"]
  [{:keys [session-id] :as ws} {:keys [result] :as message}]
  (swap! yst/state yst/update-round session-id result)
  nil)
