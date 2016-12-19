(ns dropbox.routes.services
    (:require
        [dropbox.services.auth-service :as auth]
        [dropbox.services.files-service :as files]
        [dropbox.services.sharings-service :as sharings]
        [ring.util.http-response :refer :all]
        [compojure.api.sweet :refer :all]
        [compojure.api.upload :refer :all]
        [schema.core :as s]
        [cheshire.core :as cheshire]))

(s/defschema UserRegistration
{:id               String
:password         String
:password-confirm String})

(s/defschema Result
{:result                   s/Keyword
(s/optional-key :message) String})

(defapi service-routes
    {:swagger {:ui "/swagger-ui"
            :spec "/swagger.json"
            :data {:info {:version "1.0.0"
                        :title "Dropbox API"
                        :description "Public Services"}}}}
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
    {:swagger {:ui "/swagger-ui-private"
            :spec "/swagger-private.json"
            :data {:info {:version "1.0.0"
                            :title "Dropbox API"
                            :description "Private Services"}}}}

    ;; Files
    (GET "/files" {:keys [identity]}
        :summary "retrieves files for the current user"
        (files/get-files identity))

    (GET "/files/:fileid" {:keys [identity]}
        :path-params [fileid :- Long]
        :summary "retrieves files for the current user with the specified id"
        (cheshire/generate-string (files/get-file identity fileid)))

    (POST "/files" {:keys [identity]}
        :multipart-params [file :- TempFileUpload]
        :middleware [wrap-multipart-params]
        :summary "uploades a new file for the specified user"
        :return Result
        (files/save-file identity file))

    (DELETE "/files/:fileid" {:keys [identity]}
        :path-params [fileid :- Long]
        :summary "deletes the specified file"
        :return Result
        (files/delete-file identity fileid))

    ;; Sharings
    (GET "/sharings/files" {:keys [identity]}
        :summary "retrieves shared files for the current user"
        (sharings/get-shared-files identity))

    (GET "/sharings/users/:fileid" {:keys [identity]}
        :path-params [fileid :- Long]
        :summary "retrieves shared files for the current user"
        (sharings/get-users-for-file identity fileid))

    ; (PUT "/sharings/users/:fileid" {:keys [identity]}
    ;     :body-params [sharings :- Long]
    ;     :summary "update shared files for the current user"
    ;     (sharings/get-users-for-file identity fileid sharings))
)