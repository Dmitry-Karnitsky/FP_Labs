(ns dropbox.routes.services
  (:require [dropbox.services.auth-service :as auth]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [compojure.api.upload :refer :all]
            [schema.core :as s]))

(s/defschema UserRegistration
  {:id               String
   :password         String
   :password-confirm String})

(s/defschema Result
  {:result                   s/Keyword
   (s/optional-key :message) String})

(defapi service-routes
  (POST "/register" req
        :body [user UserRegistration]
        :summary "register a new user"
        :return Result
        (auth/register! req user))
  (POST "/login" req
        :header-params [authorization :- String]
        :summary "login the user and create a session"
        :return Result
        (auth/login! req authorization))
  (POST "/logout" []
        :summary "remove user session"
        :return Result
        (auth/logout!))
  )

(defapi restricted-service-routes

  )