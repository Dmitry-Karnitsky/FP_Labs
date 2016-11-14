(ns dropbox.data.folders-repository
	(:require
		[dropbox.data.protocols.folders-repository-protocol :as protocol]
		[dropbox.data.mappers.folder-entity-mapper :as m]
		[clojure.java.jdbc :as jdbc])

	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(def folder-mapper (m/->FolderMapper))

(deftype FoldersRepository [db-spec]
	protocol/FoldersRepositoryProtocol

	(get-all [this owner-id]
		(jdbc/query db-spec
			["SELECT FolderId, FolderName, IsPrivate, IsRoot, OwnerId, CreateDate, UpdateDate FROM Folders WHERE OwnerId = ?", owner-id]
				{:row-fn (fn [data] (.data->entity folder-mapper data))}))

	(get [this id]
		(jdbc/query db-spec
			["SELECT FolderId, FolderName, IsPrivate, IsRoot, OwnerId, CreateDate, UpdateDate FROM Folders WHERE FolderId = ?", id]
				{:row-fn (fn [data] (.data->entity folder-mapper data))
				:result-set-fn first}))

	(create [this folder]
		(jdbc/insert! db-spec :Folders (.entity->data folder-mapper folder)))

	(update [this folder]
		(jdbc/update! db-spec :Folders (.entity->data folder-mapper folder) ["FolderId = ?" (:id folder)]))

	(delete [this folder]
		(jdbc/delete! db-spec :Folders ["FolderId = ?" (:id folder)]))
)