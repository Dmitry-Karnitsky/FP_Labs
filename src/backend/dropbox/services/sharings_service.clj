(ns dropbox.services.sharings-service
	(:require
		[dropbox.config :as conf]
		[dropbox.data.dsl :as dsl :refer :all]
		[dropbox.services.mappers.sharing-mapper :as mapper]
		[dropbox.services.mappers.file-mapper :as file-mapper]
		[dropbox.data.generic-repository :as gen-repo]
		[dropbox.data.fileshares-repository :as fileshares-repo]
		[dropbox.data.files-repository :as files-repo]
		[ring.util.http-response :refer :all]))

(def files (files-repo/->FilesRepository conf/db-spec))
(def fileshares (fileshares-repo/->FileSharesRepository conf/db-spec))
(def queryable (gen-repo/->GenericRepository conf/db-spec))

(defn get-shared-files
	[friend-name]
	(map
		file-mapper/raw->business
		(.execute-select queryable
			(select
				(fields {
					:FileId :f.FileId
					:FileName :f.FileName
					:FileSize (LEN :f.FileBytes)
					:CreateDate :f.CreateDate
					:OwnerName :f.OwnerName})
				(from :f :Files)
				(join :fs :FileShares (= :f.FileId :fs.FileId))
				(join :fr :Friendships (= :fs.FriendshipId :fr.FriendshipId))
				(where ['= :fr.FriendName friend-name])
				(order-by :f.FileName)))))

(defn get-users-for-file
	[user-name fileid]
	(map
		mapper/raw->usersharing
		(.execute-select queryable
			(select
				(fields {
					:FriendName :fr.FriendName
					:FileId :fs.FileId})
				(from :fs :FileShares)
				(join-right :fr :Friendships (= :fr.FriendshipId :fs.FriendshipId))
				(where ['and ['= :fr.UserName user-name] ['or ['= :fs.FileId fileid] ['is :fs.FileId nil]]])
				(order-by :fr.FriendName)))))

; (defn share-file
; 	[user-name fileid sharings]
; 	(let [data (map mapper/])
; 	(map
; 		mapper/raw->usersharing
; 		(.execute-select queryable
; 			(select
; 				(fields {
; 					:FriendName :fr.FriendName
; 					:FileId :fs.FileId})
; 				(from :fs :FileShares)
; 				(join-right :fr :Friendships (= :fr.FriendshipId :fs.FriendshipId))
; 				(where ['and ['= :fr.UserName user-name] ['or ['= :fs.FileId fileid] ['is :fs.FileId nil]]])
; 				(order-by :fr.FriendName)))))
