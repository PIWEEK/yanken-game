;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.util.spec
  "Data manipulation and query helper functions."
  (:refer-clojure :exclude [assert bytes?])
  (:require
   [clojure.spec.alpha :as s]
   [cuerdas.core :as str]
   [expound.alpha]
   [yanken.util.exceptions :as ex]
   [yanken.util.uuid :as uuid]))

(s/check-asserts true)

;; --- Default Specs

(s/def ::inst inst?)
(s/def ::string string?)
(s/def ::not-empty-string (s/and string? #(not (str/empty? %))))
(s/def ::url string?)

;; --- SPEC: uuid

(def uuid-rx
  #"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

(s/def ::uuid
  (s/conformer
   (fn [v]
     (if (uuid? v)
       v
       (if (string? v)
         (if (re-matches uuid-rx v)
           (uuid/uuid v)
           ::s/invalid)
         ::s/invalid)))
   str))

;; --- SPEC: boolean

(s/def ::boolean
  (s/conformer
   (fn [v]
     (if (boolean? v)
       v
       (if (string? v)
         (if (re-matches #"^(?:t|true|false|f|0|1)$" v)
           (contains? #{"t" "true" "1"} v)
           ::s/invalid)
         ::s/invalid)))
   (fn [v]
     (if v "true" "false"))))

;; --- SPEC: number

(s/def ::number
  (s/conformer
   (fn [v]
     (cond
       (number? v) v
       (str/numeric? v)
       (Double/parseDouble v)
       :else ::s/invalid))
   str))

;; --- SPEC: integer

(s/def ::integer
  (s/conformer
   (fn [v]
     (cond
       (integer? v) v
       (string? v)
       (if (re-matches #"^[-+]?\d+$" v)
         (Long/parseLong v)
         ::s/invalid)
       :else ::s/invalid))
   str))

;; --- SPEC: keyword

(s/def ::keyword
  (s/conformer
   (fn [v]
     (cond
       (keyword? v)
       v

       (string? v)
       (keyword v)

       :else
       ::s/invalid))
   name))

;; --- SPEC: set of Keywords

(s/def ::set-of-keywords
  (s/conformer
   (fn [s]
     (let [xform (comp
                  (map (fn [s]
                         (cond
                           (string? s) (keyword s)
                           (keyword? s) s
                           :else nil)))
                  (filter identity))]
       (cond
         (set? s)    (into #{} xform s)
         (string? s) (into #{} xform (str/words s))
         :else       ::s/invalid)))
   (fn [s]
     (str/join " " (map name s)))))

;; --- SPEC: set-of-str

(s/def ::set-of-str
  (s/conformer
   (fn [s]
     (let [xform (comp
                  (filter string?)
                  (remove str/empty?)
                  (remove str/blank?))]
       (cond
         (string? s) (->> (str/split s #"\s*,\s*")
                          (into #{} xform))
         (set? s)    (into #{} xform s)
         :else       ::s/invalid)))
   (fn [s]
     (str/join "," s))))

;; --- Macros

(defn spec-assert*
  [spec x message context]
  (if (s/valid? spec x)
    x
    (let [data    (s/explain-data spec x)
          explain (with-out-str (s/explain-out data))]
      (ex/raise :type :assertion
                :code :spec-validation
                :hint message
                :data data
                :explain explain
                :context context))))


(defmacro assert
  "Development only assertion macro."
  [spec x]
  (when *assert*
    (let [nsdata  (:ns &env)
          context (when nsdata
                    {:ns (str (:name nsdata))
                     :name (pr-str spec)
                     :line (:line &env)
                     :file (:file (:meta nsdata))})
          message (str "spec assert: '" (pr-str spec) "'")]
      `(spec-assert* ~spec ~x ~message ~context))))

(defmacro verify
  "Always active assertion macro (does not obey to :elide-asserts)"
  [spec x]
  (let [nsdata  (:ns &env)
        context (when nsdata
                  {:ns (str (:name nsdata))
                   :name (pr-str spec)
                   :line (:line &env)
                   :file (:file (:meta nsdata))})
        message (str "spec verify: '" (pr-str spec) "'")]
    `(spec-assert* ~spec ~x ~message ~context)))

;; --- Public Api

(defn conform
  [spec data]
  (let [result (s/conform spec data)]
    (when (= result ::s/invalid)
      (let [data    (s/explain-data spec data)
            explain (with-out-str
                      (s/explain-out data))]
        (throw (ex/error :type :validation
                         :code :spec-validation
                         :explain explain
                         :data data))))
    result))

(defmacro instrument!
  [& {:keys [sym spec]}]
  (when *assert*
    (let [message (str "Spec failed on: " sym)]
      `(let [origf# ~sym
             mdata# (meta (var ~sym))]
         (set! ~sym (fn [& params#]
                      (spec-assert* ~spec params# ~message mdata#)
                      (apply origf# params#)))))))

