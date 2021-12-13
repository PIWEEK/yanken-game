;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) Andrey Antukh <niwi@niwi.nz>

(ns yanken.http.middleware)

(defn- add-cors-headers
  [response request]
  (update response
          :headers
          (fn [headers]
            (-> headers
                (assoc "access-control-allow-origin" (get-in request [:headers "origin"]))
                (assoc "access-control-allow-methods" "GET,POST,DELETE,OPTIONS,PUT,HEAD,PATCH")
                (assoc "access-control-allow-credentials" "true")
                (assoc "access-control-expose-headers" "x-requested-with, content-type, cookie")
                (assoc "access-control-allow-headers" "x-frontend-version, content-type, accept, x-requested-width")))))

(defn wrap-cors
  [handler]
  (fn [request]
    (if (= (:request-method request) :options)
      (-> {:status 200 :body ""}
          (add-cors-headers request))
      (let [response (handler request)]
        (add-cors-headers response request)))))
