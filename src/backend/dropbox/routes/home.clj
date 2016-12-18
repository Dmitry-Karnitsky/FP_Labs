(ns dropbox.routes.home
  (:require [dropbox.layout :as layout]
            [dropbox.services.files-service :as fs]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" req (fs/get-file req)(home-page) ))
