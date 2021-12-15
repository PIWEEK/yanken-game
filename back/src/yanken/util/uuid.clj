;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.util.uuid
  (:refer-clojure :exclude [next uuid zero?])
  (:require
   [clj-uuid :as impl])
  (:import java.util.UUID))

(def zero #uuid "00000000-0000-0000-0000-000000000000")

(defn zero?
  [v]
  (= zero v))

(defn next
  []
  (impl/v1))

(defn random
  "Alias for clj-uuid/v4."
  []
  (impl/v4))

(defn uuid
  "Parse string uuid representation into proper UUID instance."
  [s]
  (UUID/fromString s))

(defn custom
  ([a] (UUID. 0 a))
  ([b a] (UUID. b a)))

(defn get-word-high
  [u]
  (impl/get-word-high u))

(defn get-data
  [u]
  [(impl/get-word-high u)
   (impl/get-word-low u)])
