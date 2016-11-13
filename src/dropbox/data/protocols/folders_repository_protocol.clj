(ns dropbox.data.protocols.folders-repository-protocol
    (:refer-clojure :exclude [get update]))

(defprotocol FoldersRepositoryProtocol
    (get-all [this username])
    (get [this id])
    (create [this folder])
    (update [this folder])
    (delete [this folder])
)