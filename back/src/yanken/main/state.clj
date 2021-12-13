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


(defn get-current-session
  [state {:keys [id] :as ws}]
  (let [session-id (get-in state [:connections (:id ws) :session-id])]
    ;; Session should be initialized if not, this means no "hello"
    ;; message is received.
    (when-not session-id
      (ex/raise :type :validation
                :code :session-not-initialized))

    (get-in state [:sessions session-id])))

;; --- State transformations

(defn connect
  [state {:keys [id] :as ws}]
  (update state :connections assoc id ws))

(defn disconnect
  [state {:keys [id] :as ws}]
  (update state :connections dissoc id))

(defn create-or-update-session
  [state {:keys [id] :as ws} {:keys [session-id name]}]
  (let [session-id (or session-id (uuid/next))]
    (if-let [session (get-in state [:sessions session-id])]
      (let [session (-> session
                        (assoc :connection-id id)
                        (cond-> (string? name) (assoc :name name)))]
        (-> state
            (assoc :current-session-id session-id)
            (assoc :current-avatar-id (:avatar-id session))
            (assoc :session-created false)
            (update :sessions assoc session-id session)
            (update :connections update id assoc :session-id session-id)))

      (let [avatar-id 0
            session   {:id session-id
                       :avatar-id avatar-id
                       :name (or name (str (gensym "player")))
                       :connection-id id}]
        (-> state
            (assoc :current-avatar-id avatar-id)
            (assoc :current-session-id session-id)
            (assoc :session-created true)
            (update :sessions assoc session-id session)
            (update :connections update id assoc :session-id session-id))))))

(defn create-room
  [state session-id]
  (let [room-id (inc (:current-room-id state -1))
        room    {:created-at (dt/now)
                 :id room-id
                 :players #{}
                 :admin session-id}]
    (-> state
        (assoc :current-room-id room-id)
        (update :rooms assoc room-id room))))


;; TODO: validate params

(defn join-room
  [state {:keys [session-id] :as ws} {:keys [room-id]}]

  ;; Session ID is mandatory for join-room
  (when-not session-id
    (ex/raise :type :validation
              :code :session-not-initialized
              :hint "missing hello event"))

  (let [room-id (or room-id (uuid/next))]
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

      (let [room {:created-at (dt/now)
                  :id room-id
                  :status "waiting"
                  :players #{session-id}
                  :admin session-id}]
        (-> state
            (assoc :current-room room)
            (assoc :current-room-created true)
            (update :rooms assoc room-id room))))))

(defn start-game
  [state {:keys [id] :as ws} {:keys [room-id]}]
  (let [room    (get-in state [:rooms room-id])
        session (get-current-session state ws)]

    (when-not room
      (ex/raise :type :validation
                :code :room-does-not-exists))

    (when (not= (:admin room) (:id session))
      (ex/raise :type :validation
                :code :only-owners-can-start-the-game))

    (let [room (-> room
                   (assoc :status "playing")
                   (assoc :round 0))]
      (update state :rooms assoc room-id room))))
