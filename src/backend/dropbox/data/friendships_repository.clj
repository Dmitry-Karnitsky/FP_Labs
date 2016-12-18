(ns dropbox.data.friendships-repository
	(:require
		[dropbox.data.protocols.friendships-repository-protocol :as protocol]
		[dropbox.data.mappers.friendship-entity-mapper :as m]
		[clojure.java.jdbc :as jdbc])

	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(def friendship-mapper (m/->FriendshipMapper))

(deftype FriendshipsRepository [db-spec]
	protocol/FriendshipsRepositoryProtocol

	(get-all [this user-id]
		(jdbc/query db-spec
			["SELECT FriendshipId, UserName, FriendName FROM Friendship WHERE UserName = ?", user-id]
				:row-fn (fn [data] (.data->entity friendship-mapper data))))

	(create [this friendship]
		(jdbc/insert! db-spec :Friendships (.entity->data friendship-mapper friendship)))

	(delete [this friendship]
		(jdbc/delete! db-spec :Friendships ["FriendshipId = ?" (:id friendship)]))
)