;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.util.json
  (:require
   [jsonista.core :as j]
   [camel-snake-kebab.core :as csk]))

(def mapper
  (j/object-mapper
    {:encode-key-fn csk/->camelCaseString
     :decode-key-fn csk/->kebab-case-keyword}))

(defn encode-str
  [v]
  (j/write-value-as-string v mapper))

(defn encode
  [v]
  (j/write-value-as-bytes v mapper))

(defn decode-str
  [v]
  (j/read-value v mapper))

(defn decode
  [v]
  (j/read-value v mapper))
