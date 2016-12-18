(ns dropbox.services.mappers.user-mapper
    (:require
        [dropbox.data.entities.user :as du]
        [dropbox.services.entities.user :as u]
		[dropbox.infrastructure.helpers :as h]
		[buddy.hashers :as hash]))

(defn reguser->business
    [reguser]
    (u/->User
        (:id reguser) ;;username
        (:password (update reguser :password hash/encrypt))
        (h/utc-now)))

(defn business->data
    [business]
    (du/->User
        (:user-name business)
        (:password business)
        (:register-date business)))

(defn data->business
    [data]
    (u/->User
        (:user-name data)
        (:password data)
        (:register-date data)))
