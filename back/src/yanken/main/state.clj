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

(def ^:const pairing-screen-timeout 3000)
(def ^:const game-screen-timeout 5000)
(def ^:const game-end-screen-timeout 3000)
(def ^:const result-screen-timeout 3000)

(def default-room-options
  {:pairing-screen-timeout pairing-screen-timeout
   :game-screen-timeout game-screen-timeout
   :game-end-screen-timeout game-end-screen-timeout
   :result-screen-timeout result-screen-timeout})

;; --- Helpers

(defn- get-room
  [state room-id]
  (let [room (get-in state [:rooms room-id])]
    (when-not room
      (ex/raise :type :validation
                :code :room-does-not-exists))
    room))

(defn- get-room-for-session
  [state session-id]
  (let [room-id (get-in state [:sessions session-id :room-id])]
    (get-room state room-id)))

(defn- set-room
  [state {:keys [id] :as room}]
  (-> state
      (update :rooms assoc id room)
      (assoc :current-room room)))

(defn- set-session
  [state {:keys [id] :as session}]
  (-> state
      (update :sessions assoc id session)
      (assoc :current-session session)))

(defn- make-fight
  [pair]
  {:id (uuid/next)
   :players (into #{} pair)
   :winner nil
   :responses {}})

(defn make-session
  [name avatar]
  {:id (str (uuid/next))
   :avatar avatar
   :is-bot false
   :name (or name (str (gensym "player")))})

(defn make-bot
  [id]
  (let [id (if (uuid? id) id (uuid/custom 100 id))]
    {:id id
     :is-bot true
     :name "bot"
     :avatar "bot"}))

(defn make-bot-id
  [i]
  (uuid/custom 100 i))

(defn is-bot?
  [u]
  (when (uuid? u)
    (= (uuid/get-word-high u) 100)))

;; --- State transformations

(defn connect
  "Associates the connection to the state."
  [state {:keys [id] :as ws}]
  (update state :connections assoc id ws))

(defn disconnect
  "Dissociates the connection from state."
  [state {:keys [id] :as ws}]
  (update state :connections dissoc id))

(defn update-session
  "Creates or updates the current session associated with the specified
  connection."
  [state connection-id session-id player-name player-avatar]
  (if-let [session (get-in state [:sessions session-id])]
    (let [session (-> session
                      (assoc :connection-id connection-id)
                      (cond-> (string? player-name) (assoc :name player-name)))
          room    (get-in state [:rooms (:room-id session)])]
      (-> state
          (set-session session)
          (assoc :current-session session)
          (cond-> (some? room) (assoc :current-room room))
          (update :connections update connection-id assoc :session-id session-id)))

    (let [avatar-id  0
          session-id (str (uuid/next))
          session    (-> (make-session player-name player-avatar)
                         (assoc :connection-id connection-id))]
      (-> state
          (set-session session)
          (assoc :current-session-created true)
          (update :connections update connection-id assoc :session-id session-id)))))

(defn add-bot-session
  [state i]
  (set-session state (make-bot i)))

(defn join-room
  "Handles the association of session to a specific room. If room does
  not exists, creates it and associates the session with it."
  [state session-id room-id]
  (let [room-id (or room-id (uuid/next))
        state   (update-in state [:sessions session-id] assoc :room-id room-id)
        {:keys [status] :as room} (get-in state [:rooms room-id])]
    (cond
      (or (= "ended" status)
          (nil? room))
      (let [room {:id room-id
                  :status "waiting"
                  :options default-room-options
                  :players #{session-id}
                  :owner session-id}]
        (set-room state room))

      (= "playing" status)
      (ex/raise :type :validation
                :code :cant-join-ongoing-game)

      :else
      (let [room (update room :players conj session-id)]
        (set-room state room)))))

(defn join-room-with-bot
  [state room-id i]
  (let [{:keys [status] :as room} (get-in state [:rooms room-id])]
    (when (not= status "waiting")
      (ex/raise :type :validation
                :code :unable-join-bots))
    (let [session (-> (make-bot i)
                      (assoc :room-id room-id))
          room    (update room :players conj (:id session))]
      (-> state
          (set-session session)
          (set-room room)))))

(defn start-game
  "Marks the room object as `playing`. Only the owner of the room can do it."
  [state session-id options]
  (let [{:keys [players owner status] :as room} (get-room-for-session state session-id)]
    (when (not= owner session-id)
      (ex/raise :type :validation
                :code :only-owners-can-start-the-game))

    (when (= "playing" status)
      (ex/raise :type :validation
                :code :game-is-already-running))

    (let [bot       (make-bot -1)
          need-bot? (odd? (count players))
          players   (cond-> players need-bot? (conj (:id bot)))
          room      (-> room
                        (assoc :status "playing")
                        (assoc :players players)
                        (assoc :live-players players)
                        (assoc :dead-players #{})
                        (update :options merge options))]
      (-> state
          (set-room room)
          (cond-> need-bot? (update :sessions assoc (:id bot) bot))))))


(defn update-room-stage
  "Simply change the room stage to other value."
  [state room-id stage]
  (let [room (get-room state room-id)]
    (->> (assoc room :stage stage)
         (set-room state))))

(defn prepare-round
  "State transformation function that parepares the room (specified with
  room-id) for the (next) round. If no next round is posible it will
  return the state with room with ended status."
  [state room-id round]
  (let [room    (get-room state room-id)
        players (:live-players room)]

    (if (or (empty? players) (= 1 (count players)))
      (let [room (-> room
                     (assoc :status "ended")
                     (dissoc :stage))]
        (set-room state room))

      (let [fights (->> (shuffle (:live-players room))
                        (into [] (comp (partition-all 2)
                                       (map make-fight))))
            room   (-> room
                       (assoc :round round)
                       (assoc :stage "pairing")
                       (assoc :fights fights))]
        (set-room state room)))))

(defn finish-round
  "Function that handles the resolution of the ongoing turn."
  [state room-id]
  (let [{:keys [options] :as room} (get-room state room-id)]
    (letfn [(get-alive-players [{:keys [winner] :as fight}]
              (cond
                (= :nobody winner) []
                (= :both winner)   (:players fight)
                :else              [winner]))

            (get-bot-response [responses players]
              (cond
                (contains? options :bot-default-response)
                (:bot-default-response options)

                (:bot-always-winner options)
                (let [id  (->> players
                               (remove is-bot?)
                               (first))
                      res (get responses id)]
                  (cond
                    (nil? res) 1
                    (= res 1) 2
                    (= res 2) 3
                    (= res 3) 1))

                :else
                (inc (rand-int 3))))

            (resolve-bot-response [{:keys [players responses] :as fight}]
              (reduce (fn [fight player-id]
                        (cond-> fight
                          (is-bot? player-id)
                          (assoc-in [:responses player-id] (get-bot-response responses players))))
                      fight
                      players))

            (get-winner [responses]
              (cond
                (empty? responses)      :nobody
                (= 1 (count responses)) (ffirst responses)
                :else
                (let [[[p1 r1] [p2 r2]] (seq responses)]
                  (cond
                    (= r1 r2) :both
                    (= r1 1)  (if (= r2 3) p1 p2)
                    (= r1 2)  (if (= r2 1) p1 p2)
                    (= r1 3)  (if (= r2 2) p1 p2)))))

            (resolve-winner [{:keys [responses] :as fight}]
              (assoc fight :winner (get-winner responses)))]

      (let [fights-xf (comp (map resolve-bot-response)
                            (map resolve-winner)
                            (map #(assoc % :round (:round room))))

            fights    (into [] fights-xf (:fights room))
            alive     (into #{} (mapcat get-alive-players) fights)

            dead      (set/difference (:live-players room) alive)
            room      (-> (dissoc room :fights)
                          (assoc :live-players alive)
                          (assoc :stage "gameEnd")
                          (update :results conj fights)
                          (update :dead-players into dead))]
        (set-room state room)))))

(defn update-round
  [state session-id response]
  (letfn [(update-fight [{:keys [players] :as fights}]
            (cond-> fights
              (contains? players session-id)
              (update :responses assoc session-id response)))]

    (let [room (get-room-for-session state session-id)]
      (when (not= (:stage room) "game")
        (ex/raise :type :validation
                  :code :response-out-of-time))

      (let [fights (into [] (map update-fight) (:fights room))
            room   (assoc room :fights fights)]
        (set-room state room)))))
