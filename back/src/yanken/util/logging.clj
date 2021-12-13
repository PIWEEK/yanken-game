;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.util.logging
  (:import
   org.apache.logging.log4j.Level
   org.apache.logging.log4j.LogManager
   org.apache.logging.log4j.Logger
   org.apache.logging.log4j.message.MapMessage
   org.apache.logging.log4j.spi.LoggerContext))

(defn build-map-message
  [m]
  (let [message (MapMessage. (count m))]
    (reduce-kv #(.with ^MapMessage %1 (name %2) %3) message m)))

(def logger-context
  (LogManager/getContext false))

(def logging-agent
  (agent nil :error-mode :continue))

(defn get-logger
  [lname]
  (.getLogger ^LoggerContext logger-context ^String lname))

(defn get-level
  [level]
  (case level
    :trace Level/TRACE
    :debug Level/DEBUG
    :info  Level/INFO
    :warn  Level/WARN
    :error Level/ERROR
    :fatal Level/FATAL))

(defn write-log!
  [logger level exception message]
  (if exception
    (.log ^Logger    logger
          ^Level     level
          ^Object    message
          ^Throwable exception)
    (.log ^Logger logger
          ^Level  level
          ^Object message)))

(defn enabled?
  [logger level]
  (.isEnabled ^Logger logger ^Level level))

(defmacro log
  [& {:keys [level cause ::logger ::async ::raw] :as props}]
  (let [props      (dissoc props :level :cause ::logger ::async ::raw)
        logger     (or logger (str *ns*))
        logger-sym (gensym "log")
        level-sym  (gensym "log")]
    `(let [~logger-sym (get-logger ~logger)
           ~level-sym  (get-level ~level)]
       (if (enabled? ~logger-sym ~level-sym)
         ~(if async
            `(send-off logging-agent
                       (fn [_#]
                         (let [message# (or ~raw (build-map-message ~props))]
                           (write-log! ~logger-sym ~level-sym ~cause message#))))
            `(let [message# (or ~raw (build-map-message ~props))]
               (write-log! ~logger-sym ~level-sym ~cause message#)))))))

(defmacro info
  [& params]
  `(log :level :info ~@params))

(defmacro error
  [& params]
  `(log :level :error ~@params))

(defmacro warn
  [& params]
  `(log :level :warn ~@params))

(defmacro debug
  [& params]
  `(log :level :debug ~@params))

(defmacro trace
  [& params]
  `(log :level :trace ~@params))

(defmacro set-level!
  ([level]
   (when (:ns &env)
     `(set-level* ~(str *ns*) ~level)))
  ([n level]
   (when (:ns &env)
     `(set-level* ~n ~level))))
