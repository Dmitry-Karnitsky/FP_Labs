(ns dropbox.services.auth-service
  (:require
			[dropbox.config :as conf]
			[dropbox.data.dsl :as dsl :refer :all]
			[dropbox.services.users-service :as us]
      [dropbox.validation :refer [registration-errors]]
      [ring.util.http-response :as response]
      [buddy.hashers :as hashers]
      [clojure.tools.logging :as log]
      [dropbox.services.mappers.user-mapper :as mapper]))

(defn decode-auth [encoded]
  (let [auth (second (.split encoded " "))]
    (-> (.decode (java.util.Base64/getDecoder) auth)
        (String. (java.nio.charset.Charset/forName "UTF-8"))
        (.split ":"))))

(defn authenticate [[id password]]
  (when-let [user (us/get id)]
    (when (hashers/check password (:password user))
      id)))

(defn login! [{:keys [session]} auth]
  (if-let [id (authenticate (decode-auth auth))]
    (-> {:result :ok}
        (response/ok)
        (assoc :session (assoc session :identity id)))
    (response/unauthorized {:result  :unauthorized
                            :message "Authentication failed."})))

(defn logout! []
  (-> {:result :ok}
      (response/ok)
      (assoc :session nil)))

(defn handle-registration-error [e]
  (if (and
        (instance? java.sql.SQLException e)
        (-> e (.getNextException)
            (.getMessage)
            (.startsWith "ERROR: duplicate key value")))
    (response/precondition-failed
      {:result  :error
       :message "user with the selected ID already exists"})
    (do
      (log/error e)
      (response/internal-server-error
        {:result  :error
         :message "server error occurred while adding the user"}))))

(defn register! [{:keys [session]} user]
  (if (registration-errors user)
    (response/precondition-failed {:result :error})
    (try
      (us/create-user (mapper/reguser->business user))
      (-> {:result :ok}
          (response/ok)
          (assoc :session (assoc session :identity (:id user))))
      (catch Exception e
        (handle-registration-error e)))))
