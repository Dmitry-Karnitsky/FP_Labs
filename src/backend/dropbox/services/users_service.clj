(ns dropbox.services.users-service
	(:require
		[dropbox.config :as conf]
		[dropbox.services.mappers.user-mapper :as mapper]
		[dropbox.data.users-repository :as users-repo]))

(def users (users-repo/->UsersRepository conf/db-spec))

(defn get-by-id
	[user-id]
	(mapper/data->business  (.get users user-id)))

(defn create-user
	[user]
	(.create users (mapper/business->data user)))
