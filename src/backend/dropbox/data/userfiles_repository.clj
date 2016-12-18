(ns dropbox.data.userfiles-repository
	(:require
		[dropbox.data.protocols.userfiles-repository-protocol :as protocol]
		[dropbox.data.mappers.userfile-entity-mapper :as m]
		[clojure.java.jdbc :as jdbc])

	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(def userfile-mapper (m/->UserFileMapper))

(deftype UserFilesRepository [db-spec]
	protocol/UserFilesRepositoryProtocol

	(create [this userfile]
		(jdbc/insert! db-spec :UserFiles (.entity->data userfile-mapper userfile)))

	(delete [this userfile]
		(jdbc/delete! db-spec :UserFiles ["UserFileId = ?" (:id userfile)]))
)