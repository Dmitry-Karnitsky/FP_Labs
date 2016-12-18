(ns dropbox.data.files-repository
	(:require
		[dropbox.data.protocols.files-repository-protocol :as protocol]
		[dropbox.data.mappers.file-entity-mapper :as m]
		[clojure.java.jdbc :as jdbc])

	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(def file-mapper (m/->FileMapper))

(deftype FilesRepository [db-spec]
	protocol/FilesRepositoryProtocol

	(get-all [this owner-id]
		(jdbc/query db-spec
			["SELECT FileId, FileName, FileBytes, CreateDate, OwnerId FROM Files WHERE OwnerId = ?", owner-id]
				{:row-fn (fn [data] (.data->entity file-mapper data))}))

	(get [this id]
		(jdbc/query db-spec
			["SELECT FileId, FileName, FileBytes, CreateDate, OwnerId FROM Files WHERE FileId = ?", id]
				{:row-fn (fn [data] (.data->entity file-mapper data))
				:result-set-fn first}))

	(create [this file]
		(jdbc/insert! db-spec :Files (.entity->data file-mapper file)))

	(update [this file]
		(jdbc/update! db-spec :Files (.entity->data file-mapper file) ["FileId = ?" (:id file)]))

	(delete [this file]
		(jdbc/delete! db-spec :Files ["FileId = ?" (:id file)]))
)