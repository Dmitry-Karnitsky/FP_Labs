(ns dropbox.data.users-repository
	(:require
		[dropbox.data.protocols.users-repository-protocol :as protocol]
		[dropbox.data.mappers.user-entity-mapper :as m]
		[clojure.java.jdbc :as jdbc])
	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(def user-mapper (m/->UserMapper))

(deftype UsersRepository [db-spec]
	protocol/UsersRepositoryProtocol

	(get [this user-id]
		(jdbc/query db-spec
			["SELECT UserId, Username, Password, RegisterDate FROM Users WHERE UserId = ?", user-id]
				{:row-fn (fn [data] (.data->entity user-mapper data))
				:result-set-fn first}))

	(create [this user]
		(jdbc/insert! db-spec :Users (.entity->data user-mapper user)))

	(update [this user]
		(jdbc/update! db-spec :Users (.entity->data user-mapper user) ["UserId = ?" (:id user)]))
)