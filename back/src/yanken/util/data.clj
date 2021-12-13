;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.util.data
  "Data manipulation and query helper functions."
  (:refer-clojure :exclude [read-string parse-double])
  (:require
   [cuerdas.core :as str]
   [clojure.edn :as r]))

(defn deep-merge
  ([a b]
   (if (map? a)
     (merge-with deep-merge a b)
     b))
  ([a b & rest]
   (reduce deep-merge a (cons b rest))))

(defn- transient-concat
  [c1 colls]
  (loop [result (transient c1)
         colls  colls]
    (if colls
      (recur (reduce conj! result (first colls))
             (next colls))
      (persistent! result))))

(defn concat-set
  ([] #{})
  ([c1]
   (if (set? c1) c1 (into #{} c1)))
  ([c1 & more]
   (if (set? c1)
     (transient-concat c1 more)
     (transient-concat #{} (cons c1 more)))))

(defn concat-vec
  ([] [])
  ([c1]
   (if (vector? c1) c1 (into [] c1)))
  ([c1 & more]
   (if (vector? c1)
     (transient-concat c1 more)
     (transient-concat [] (cons c1 more)))))

(defn enumerate
  ([items] (enumerate items 0))
  ([items start]
   (loop [idx   start
          items items
          res   (transient [])]
     (if (empty? items)
       (persistent! res)
       (recur (inc idx)
              (rest items)
              (conj! res [idx (first items)]))))))

(defn seek
  ([pred coll]
   (seek pred coll nil))
  ([pred coll not-found]
   (reduce (fn [_ x]
             (if (pred x)
               (reduced x)
               not-found))
           not-found coll)))

(defn index-by
  "Return a indexed map of the collection keyed by the result of
  executing the getter over each element of the collection."
  [getter coll]
  (persistent!
   (reduce #(assoc! %1 (getter %2) %2) (transient {}) coll)))

(defn index-of-pred
  [coll pred]
  (loop [c    (first coll)
         coll (rest coll)
         index 0]
    (if (nil? c)
      nil
      (if (pred c)
        index
        (recur (first coll)
               (rest coll)
               (inc index))))))

(defn index-of
  [coll v]
  (index-of-pred coll #(= % v)))

(defn replace-by-id
  ([value]
   (map (fn [item]
          (if (= (:id item) (:id value))
            value
            item))))
  ([coll value]
   (sequence (replace-by-id value) coll)))

(defn without-nils
  "Given a map, return a map removing key-value
  pairs when value is `nil`."
  [data]
  (into {} (remove (comp nil? second) data)))

(defn without-keys
  "Return a map without the keys provided
  in the `keys` parameter."
  [data keys]
  (when (map? data)
    (persistent!
     (reduce #(dissoc! %1 %2) (transient data) keys))))


(defn map-perm
  "Maps a function to each pair of values that can be combined inside the
  function without repetition.

  Optional parameters:
  `pred?`   A predicate that if not satisfied won't process the pair
  `target?` A collection that will be used as seed to be stored

  Example:
  (map-perm vector [1 2 3 4]) => [[1 2] [1 3] [1 4] [2 3] [2 4] [3 4]]"
  ([mfn coll]
   (map-perm mfn (constantly true) [] coll))
  ([mfn pred? coll]
   (map-perm mfn pred? [] coll))
  ([mfn pred? target coll]
   (loop [result (transient target)
          current (first coll)
          coll (rest coll)]
     (if (not current)
       (persistent! result)
       (let [result
             (loop [result result
                    other (first coll)
                    coll (rest coll)]
               (if (not other)
                 result
                 (recur (cond-> result
                          (pred? current other)
                          (conj! (mfn current other)))
                        (first coll)
                        (rest coll))))]
         (recur result
                (first coll)
                (rest coll)))))))

(def sentinel (Object.))

(defn getf
  "Returns a function to access a map"
  [coll]
  (partial get coll))

(defn update-in-when
  [m key-seq f & args]
  (let [found (get-in m key-seq sentinel)]
    (if-not (identical? sentinel found)
      (assoc-in m key-seq (apply f found args))
      m)))

(defn update-when
  [m key f & args]
  (let [found (get m key sentinel)]
    (if-not (identical? sentinel found)
      (assoc m key (apply f found args))
      m)))

(defn assoc-in-when
  [m key-seq v]
  (let [found (get-in m key-seq sentinel)]
    (if-not (identical? sentinel found)
      (assoc-in m key-seq v)
      m)))

(defn assoc-when
  [m key v]
  (let [found (get m key sentinel)]
    (if-not (identical? sentinel found)
      (assoc m key v)
      m)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Data Parsing / Conversion
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn nan?
  [v]
  (not= v v))

(defn- impl-parse-integer
  [v]
  (try
    (Integer/parseInt v)
    (catch Throwable _
      nil)))

(defn- impl-parse-double
  [v]
  (try
    (Double/parseDouble v)
    (catch Throwable _
      nil)))

(defn parse-integer
  ([v]
   (parse-integer v nil))
  ([v default]
   (let [v (impl-parse-integer v)]
     (if (or (nil? v) (nan? v))
       default
       v))))

(defn parse-double
  ([v]
   (parse-double v nil))
  ([v default]
   (let [v (impl-parse-double v)]
     (if (or (nil? v) (nan? v))
       default
       v))))

(defn num-string?
  [v]
  (not= (parse-double v :nan) :nan))

(defn read-string
  [v]
  (r/read-string v))

(defn coalesce-str
  [val default]
  (if (or (nil? val) (nan? val))
    default
    (str val)))

(defn coalesce
  [val default]
  (or val default))

(defmacro export
  "A helper macro that allows reexport a var in a current namespace."
  [v]
  (let [vr (resolve v)
        m  (meta vr)
        n  (:name m)
        n  (with-meta n
             (cond-> {}
               (:dynamic m) (assoc :dynamic true)
               (:protocol m) (assoc :protocol (:protocol m))))]
    `(let [m# (meta ~vr)]
       (def ~n (deref ~vr))
       (alter-meta! (var ~n) merge (dissoc m# :name))
       ;; (when (:macro m#)
       ;;   (.setMacro (var ~n)))
       ~vr)))


(defn deep-mapm
  "Applies a map function to an associative map and recurses over its children
  when it's a vector or a map"
  [mfn m]
  (let [do-map
        (fn [entry]
          (let [[k v] (mfn entry)]
            (cond
              (or (vector? v) (map? v))
              [k (deep-mapm mfn v)]

              :else
              (mfn [k v]))))]
    (cond
      (map? m)
      (into {} (map do-map) m)

      (vector? m)
      (into [] (map (partial deep-mapm mfn)) m)

      :else
      m)))

(defn not-empty?
  [coll]
  (boolean (seq coll)))
