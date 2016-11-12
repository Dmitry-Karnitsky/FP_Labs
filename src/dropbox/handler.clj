(ns dropbox.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [dropbox.routes :refer [dropbox-routes]]))

(def app
  (wrap-defaults dropbox-routes site-defaults))
