(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib       'yankengame/backend)
(def class-dir "target/classes")
(def basis     (b/create-basis {:project "deps.edn"}))
(def jar-file  (format "target/%s.jar" (name lib)))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file jar-file
           :basis basis
           :main 'yanken.init}))
