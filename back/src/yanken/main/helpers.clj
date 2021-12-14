;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.main.helpers
  (:refer-clojure :exclude [defmethod])
  (:require
   [clojure.core :as c]
   [clojure.core.async :as a]
   [yanken.util.async :as aa]))

(defmacro defmethod
  [sym key args & body]
  `(c/defmethod ~sym ~key ~args
     (aa/go-try ~@body)))
