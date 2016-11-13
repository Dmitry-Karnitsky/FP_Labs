(ns dropbox.data.protocols.files-repository-protocol
    (:refer-clojure :exclude [get update]))

(defprotocol FilesRepositoryProtocol
    (get-all [this folder-id])
    (get [this id])
    (create [this file])
    (update [this file])
    (delete [this file])
)