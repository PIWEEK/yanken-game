;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.util.time
  (:require
   [clojure.spec.alpha :as s]
   [cuerdas.core :as str])
  (:import
   java.time.Duration
   java.time.Instant
   java.time.OffsetDateTime
   java.time.ZoneId
   java.time.ZonedDateTime
   java.time.format.DateTimeFormatter
   java.time.temporal.TemporalAmount
   java.util.Date))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Instant & Duration
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; --- DURATION

(defn- obj->duration
  [{:keys [days minutes seconds hours nanos millis]}]
  (cond-> (Duration/ofMillis (if (int? millis) ^long millis 0))
    (int? days)    (.plusDays ^long days)
    (int? hours)   (.plusHours ^long hours)
    (int? minutes) (.plusMinutes ^long minutes)
    (int? seconds) (.plusSeconds ^long seconds)
    (int? nanos)   (.plusNanos ^long nanos)))

(defn duration?
  [v]
  (instance? Duration v))

(defn duration
  [ms-or-obj]
  (cond
    (string? ms-or-obj)
    (Duration/parse (str "PT" ms-or-obj))

    (duration? ms-or-obj)
    ms-or-obj

    (integer? ms-or-obj)
    (Duration/ofMillis ms-or-obj)

    :else
    (obj->duration ms-or-obj)))

(defn duration-between
  {:deprecated true}
  [t1 t2]
  (Duration/between t1 t2))

(defn diff
  [t1 t2]
  (Duration/between t1 t2))

(s/def ::duration
  (s/conformer
   (fn [v]
     (cond
       (duration? v) v

       (string? v)
       (try
         (duration v)
         (catch java.time.format.DateTimeParseException _e
           ::s/invalid))

       :else
       ::s/invalid))
   (fn [v]
     (subs (str v) 2))))

(extend-protocol clojure.core/Inst
  java.time.Duration
  (inst-ms* [v] (.toMillis ^Duration v))

  OffsetDateTime
  (inst-ms* [v] (.toEpochMilli (.toInstant ^OffsetDateTime v))))

(defmethod print-method Duration
  [mv ^java.io.Writer writer]
  (.write writer (str "#yanken/duration \"" (str/lower (subs (str mv) 2)) "\"")))

(defmethod print-dup Duration [o w]
  (print-method o w))

;; --- INSTANT

(defn instant
  ([s]
   (if (int? s)
     (Instant/ofEpochMilli s)
     (Instant/parse s)))
  ([s fmt]
   (case fmt
     :rfc1123 (Instant/from (.parse DateTimeFormatter/RFC_1123_DATE_TIME ^String s))
     :iso     (Instant/from (.parse DateTimeFormatter/ISO_INSTANT ^String s))
     :iso8601 (Instant/from (.parse DateTimeFormatter/ISO_INSTANT ^String s)))))

(defn instant?
  [v]
  (instance? Instant v))

(defn is-after?
  [da db]
  (.isAfter ^Instant da ^Instant db))

(defn is-before?
  [da db]
  (.isBefore ^Instant da ^Instant db))

(defn plus
  [d ta]
  (.plus d ^TemporalAmount (duration ta)))

(defn minus
  [d ta]
  (.minus d ^TemporalAmount (duration ta)))

(defn now
  []
  (Instant/now))

(defn in-future
  [v]
  (plus (now) (duration v)))

(defn in-past
  [v]
  (minus (now) (duration v)))

(defn instant->zoned-date-time
  [v]
  (ZonedDateTime/ofInstant v (ZoneId/of "UTC")))

(defn format-instant
  ([v] (.format DateTimeFormatter/ISO_INSTANT ^Instant v))
  ([v fmt]
   (case fmt
     :iso (.format DateTimeFormatter/ISO_INSTANT ^Instant v)
     :rfc1123 (.format DateTimeFormatter/RFC_1123_DATE_TIME
                       ^ZonedDateTime (instant->zoned-date-time v)))))

(defmethod print-method Instant
  [mv ^java.io.Writer writer]
  (.write writer (str "#yanken/instant \"" (format-instant mv) "\"")))

(defmethod print-dup Instant [o w]
  (print-method o w))
