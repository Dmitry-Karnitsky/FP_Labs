(ns dropbox.services.files-service
	(:require
		[dropbox.config :as conf]
		[dropbox.data.dsl :as dsl :refer :all]
		[dropbox.services.mappers.file-mapper :as mapper]
		[dropbox.data.generic-repository :as gen-repo]
		[dropbox.data.files-repository :as files-repo]))

(def files (files-repo/->FilesRepository conf/db-spec))
(def queryable (gen-repo/->GenericRepository conf/db-spec))

(defn get-files
	[user-name]
	(map
		mapper/raw->business
		(.execute-select queryable
			(select
			(fields {
				:FileId :f.FileId
				:FileName :f.FileName
				:FileBytes :f.FileBytes
				:CreateDate :f.CreateDate
				:OwnerName :f.OwnerName})
			(from :f :Files)
			(where ['= :f.OwnerName user-name])
			(order-by :f.FileName)))))

(defn get-file
	[user-name file-id]
	(println user-name file-id))

