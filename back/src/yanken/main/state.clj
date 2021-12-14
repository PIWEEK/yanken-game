;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.main.state
  "State management functions implementation."
  (:require
   [yanken.util.exceptions :as ex]
   [yanken.util.time :as dt]
   [yanken.util.uuid :as uuid]))

(defonce state (atom {}))

;; --- Helpers

;; --- State transformations

(defn connect
  [state {:keys [id] :as ws}]
  (update state :connections assoc id ws))

(defn disconnect
  [state {:keys [id] :as ws}]
  (update state :connections dissoc id))

(defn create-or-update-session
  [state {:keys [id] :as ws} {:keys [session-id player-name]}]
  (let [session-id (or session-id (uuid/next))]
    (if-let [session (get-in state [:sessions session-id])]
      (let [session (-> session
                        (assoc :connection-id id)
                        (cond-> (string? player-name) (assoc :name player-name)))]
        (-> state
            (assoc :current-session-id session-id)
            (assoc :current-avatar-id (:avatar-id session))
            (assoc :session-created false)
            (update :sessions assoc session-id session)
            (update :connections update id assoc :session-id session-id)))

      (let [avatar-id 0
            session   {:id session-id
                       :avatar-id avatar-id
                       :name (or player-name (str (gensym "player")))
                       :connection-id id}]
        (-> state
            (assoc :current-avatar-id avatar-id)
            (assoc :current-session-id session-id)
            (assoc :session-created true)
            (update :sessions assoc session-id session)
            (update :connections update id assoc :session-id session-id))))))

;; TODO: validate params

(defn join-room
  [state {:keys [session-id] :as ws} {:keys [room-id]}]

  ;; Session ID is mandatory for join-room
  (when-not session-id
    (ex/raise :type :validation
              :code :session-not-initialized
              :hint "missing hello event"))

  (let [room-id (or room-id (uuid/next))
        state   (update-in state [:sessions session-id] assoc :room-id room-id)]
    ;; if room is found in state this means the user tries to rejoin
    ;; the existing game.
    (if-let [room (get-in state [:rooms room-id])]
      (do
        ;; We dont allow join game if is already started or ongoing.
        (when (not= (:status room) "waiting")
          (ex/raise :type :validation
                    :code :cant-join-ongoing-or-finished-game))

        (-> state
            (assoc :current-room room)
            (assoc :current-room-created false)
            (update-in [:rooms room-id :players] conj session-id)))

      (let [room {:id room-id
                  :status "waiting"
                  :players #{session-id}
                  :owner session-id}]
        (-> state
            (assoc :current-room room)
            (assoc :current-room-created true)
            (update :rooms assoc room-id room))))))

(defn resolve-room
  [state session-id]
  (let [room-id (get-in state [:sessions session-id :room-id])
        room    (get-in state [:rooms room-id])]

    (when-not room
      (ex/raise :type :validation
                :code :room-does-not-exists))

    room))

(defn start-game
  [state {:keys [session-id] :as ws}]
  (let [room (resolve-room state session-id)]
    (when (not= (:owner room) session-id)
      (ex/raise :type :validation
                :code :only-owners-can-start-the-game))

    (let [room (-> room
                   (assoc :status "playing")
                   (assoc :results [])
                   (assoc :live-players (into #{} (:players room)))
                   (assoc :dead-players #{}))]
      (-> state
          (update :rooms assoc (:id room) room)
          (assoc :current-room room)))))

(defn prepare-round
  [state {:keys [session-id] :as ws} round]
  (let [room    (resolve-room state session-id)
        fights  (->> (:live-players room)
                     (shuffle)
                     (partition-all 2)
                     (map (fn [pair]
                            (cond-> (vec pair)
                              (< (count pair) 2)
                              (conj uuid/zero)))))

        room    (-> room
                    (assoc :round round)
                    (assoc :fights fights))]

    (-> state
        (update :rooms assoc (:id room) room)
        (assoc :current-room room))))
