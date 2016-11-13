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

	(get [this id]
		(jdbc/query db-spec
			["SELECT FileId, FileName, FileBytes, CreateDate FROM Files WHERE FileId = ?", id]
				{:row-fn (fn [data] (.data->entity file-mapper data))
				:result-set-fn first}))
)