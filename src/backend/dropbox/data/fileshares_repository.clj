(ns dropbox.data.fileshares-repository
	(:require
		[dropbox.data.protocols.fileshares-repository-protocol :as protocol]
		[dropbox.data.mappers.fileshare-entity-mapper :as m]
		[clojure.java.jdbc :as jdbc])

	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(def fileshare-mapper (m/->FileShareMapper))

(deftype FileShareRepository [db-spec]
	protocol/FileSharesRepositoryProtocol

	(create [this fileshare]
		(jdbc/insert! db-spec :FileShares (.entity->data fileshare-mapper fileshare)))

	(delete [this fileshare]
		(jdbc/delete! db-spec :FileShares ["FileShareId = ?" (:id fileshare)]))
)