(ns dropbox.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [dropbox.layout :refer [error-page]]
            [dropbox.routes.home :refer [home-routes]]
            [dropbox.routes.services :refer [service-routes restricted-service-routes]]
            [compojure.route :as route]
            [dropbox.middleware :as middleware]))

(def app-routes
  (routes
    #'service-routes
    (wrap-routes #'restricted-service-routes middleware/wrap-auth)
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
