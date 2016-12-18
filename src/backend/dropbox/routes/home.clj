(ns dropbox.routes.home
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [dropbox.infrastructure.converters :as conv]
   [dropbox.views :as v]
   [dropbox.config :as conf]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/"
        []
        (home-page)))
