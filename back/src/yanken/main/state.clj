;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.main.state
  "State management functions implementation."
  (:require
   [clojure.set :as set]
   [yanken.util.exceptions :as ex]
   [yanken.util.time :as dt]
   [yanken.util.uuid :as uuid]))

(defonce state (atom {}))

(def bot-session
  {:id "yanken$$bot"
   :is-bot true
   :name "Bot"
   :avatar-id 0})

;; --- Helpers

(defn get-room
  [state room-id]
  (let [room (get-in state [:rooms room-id])]
    (when-not room
      (ex/raise :type :validation
                :code :room-does-not-exists))
    room))

(defn get-room-for-session
  [state session-id]
  (let [room-id (get-in state [:sessions session-id :room-id])]
    (get-room state room-id)))

(defn set-room
  [state {:keys [id] :as room}]
  (-> state
      (update :rooms assoc id room)
      (assoc :current-room room)))

;; --- State transformations

(defn connect
  [state {:keys [id] :as ws}]
  (update state :connections assoc id ws))

(defn disconnect
  [state {:keys [id] :as ws}]
  (update state :connections dissoc id))

(defn update-session
  [state connection-id session-id player-name]
  (let [session-id (or session-id (uuid/next))]
    (if-let [session (get-in state [:sessions session-id])]
      (let [session (-> session
                        (assoc :connection-id connection-id)
                        (cond-> (string? player-name) (assoc :name player-name)))
            room    (get-room-for-session state session-id)]
        (-> state
            (assoc :current-session-id session-id)
            (assoc :current-avatar-id (:avatar-id session))
            (cond-> (some? room) (assoc :current-room room))
            (update :sessions assoc session-id session)
            (update :connections update connection-id assoc :session-id session-id)))

      (let [avatar-id 0
            session   {:id session-id
                       :avatar-id avatar-id
                       :is-bot false
                       :name (or player-name (str (gensym "player")))
                       :connection-id connection-id}]
        (-> state
            (assoc :current-avatar-id avatar-id)
            (assoc :current-session-id session-id)
            (update :sessions assoc session-id session)
            (update :connections update connection-id assoc :session-id session-id))))))

(defn join-room
  [state session-id room-id]
  (let [room-id (or room-id (uuid/next))
        state   (update-in state [:sessions session-id] assoc :room-id room-id)
        {:keys [status] :as room} (get-in state [:rooms room-id])]
    (cond
      (or (= "ended" status)
          (nil? room))
      (let [room {:id room-id
                  :status "waiting"
                  :players #{session-id}
                  :owner session-id}]
        (set-room state room))

      (= "playing" status)
      (ex/raise :type :validation
                :code :cant-join-ongoing-game)

      :else
      (-> state
          (assoc :current-room room)
          (assoc :current-room-created false)
          (update-in [:rooms room-id :players] conj session-id)))))

(defn start-game
  [state session-id]
  (let [{:keys [players owner status] :as room} (get-room-for-session state session-id)]
    (when (not= owner session-id)
      (ex/raise :type :validation
                :code :only-owners-can-start-the-game))

    (when (= "playing" status)
      (ex/raise :type :validation
                :code :game-is-already-running))

    (let [players (cond-> players
                    (odd? (count players)) (conj (:id bot-session)))
          room    (-> room
                      (assoc :status "playing")
                      (assoc :players players)
                      (assoc :live-players players)
                      (assoc :dead-players #{}))]
      (set-room state room))))

(defn prepare-round
  [state room-id round]
  (letfn [(make-fight-object [pair]
            {:id (uuid/next)
             :players (into #{} pair)
             :winner nil
             :responses {}})]

    (let [room    (get-room state room-id)
          players (:live-players room)]

      (if (= (count players) 1)
        (let [room (-> room
                       (assoc :status "ended")
                       (dissoc :stage))]
          (set-room state room))


        ;; if we still have players, lets continue to the next round.
        (let [fights (->> (:live-players room)
                          (shuffle)
                          (partition-all 2)
                          (map make-fight-object))

              room   (-> room
                         (assoc :round round)
                         (assoc :stage "waitResponses")
                         (assoc :fights (vec fights)))]

          (set-room state room))))))



(defn finish-round
  [state room-id]
  (letfn [(get-alive-players [{:keys [winner] :as fight}]
            (cond
              (= :nobody winner) []
              (= :both winner)   (:players fight)
              :else [winner]))

          (calculate-winner [responses]
            (cond
              (empty? responses)
              :nobody

              (= 1 (count responses))
              (ffirst responses)

              :else
              (let [[[p1 r1] [p2 r2]] responses]
                (cond
                  (= r1 r2) :both
                  (= r1 1)  (if (= r2 3) r1 r2)
                  (= r1 2)  (if (= r2 1) r1 r2)
                  (= r1 3)  (if (= r2 2) r1 r2)))))

          (resolve-winner [{:keys [responses players] :as fight}]
            (let [responses (cond-> responses
                              (contains? players (:id bot-session))
                              (assoc (:id bot-session) (inc (rand-int 3))))]
              (assoc fight :winner (calculate-winner responses))))]

    (let [room   (get-room state room-id)
          fights (into [] (map resolve-winner) (:fights room))
          alive  (into #{} (mapcat get-alive-players) fights)

          dead   (set/difference (:live-players room) alive)

          room   (-> (dissoc room :fights)
                     (assoc :live-players alive)
                     (assoc :stage "turnEnded")
                     (assoc :last-results fights)
                     (update :results conj fights)
                     (update :dead-players into dead))]

      (set-room state room))))

(defn update-round
  [state session-id response]
  (letfn [(update-fight [{:keys [players] :as fights}]
            (cond-> fights
              (contains? players session-id)
              (update :responses assoc session-id response)))]

    (let [room   (get-room-for-session state session-id)
          fights (into [] (map update-fight) (:fights room))
          room   (assoc room :fights fights)]

      (set-room state room))))
