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
	[user-id]
	(map
		mapper/raw->business
		(.execute-select queryable
			(select
			(fields {
				:FileId :f.FileId
				:FileName :f.FileName
				:FileBytes :f.FileBytes
				:CreateDate :f.CreateDate
				:OwnerId :f.OwnerId
				:UserName :u.Username})
			(from :u :Users)
			(join :f :Files (= :u.UserId :f.OwnerId))
			(where ['= :f.OwnerId user-id])
			(order-by :f.FileName)))))

