(ns dropbox.services.files-service
	(:require
		[dropbox.config :as conf]
		[dropbox.data.dsl :as dsl :refer :all]
		[dropbox.services.mappers.file-mapper :as mapper]
		[dropbox.data.generic-repository :as gen-repo]
		[dropbox.data.fileshares-repository :as fileshares-repo]
		[dropbox.data.files-repository :as files-repo]
		[ring.util.http-response :refer :all]))

(def files (files-repo/->FilesRepository conf/db-spec))
(def fileshares (fileshares-repo/->FileSharesRepository conf/db-spec))
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
				:FileSize (LEN :f.FileBytes)
				;;:CreateDate :f.CreateDate
				:OwnerName :f.OwnerName})
			(from :f :Files)
			(where ['= :f.OwnerName user-name])
			(order-by :f.FileName)))))

(defn get-file
	[user-name file-id]
	(first
		(map
			mapper/raw->business
			(.execute-select queryable
				(select
					(fields {
						:FileId :f.FileId
						:FileName :f.FileName
						:FileSize (LEN :f.FileBytes)
						:CreateDate :f.CreateDate
						:OwnerName :f.OwnerName})
					(from :f :Files)
					(where ['and ['= :f.OwnerName user-name] ['= :f.FileId file-id] ])
					(order-by :f.FileName))))))

(defn save-file [username {:keys [tempfile filename]}]
	(try
		(.create files  (mapper/map->data
						{:owner-name username
						:filename filename
						:tempfile tempfile}))
		(ok {:result :ok})
	(catch Exception e
		(internal-server-error "error"))))

(defn delete-file [owner file-id]
	(let [ids (.execute-select queryable
				(select
					(fields [:FileShareId])
					(from :FileShares)
					(where ['= :FileId file-id])))
		ids (map #(:fileshareid %) ids)
		file (get-file owner file-id)]
	(println file owner file-id)
	(if (some? file)
		(do
			(doseq [id ids] (.delete fileshares {:id id}))
			(.delete files (mapper/business->data file)))))
	(ok {:result :ok}))

