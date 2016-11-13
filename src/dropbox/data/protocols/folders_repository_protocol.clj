(ns dropbox.data.protocols.folders-repository-protocol)

(defprotocol FoldersRepositoryProtocol
    (get-all [this login])
    (get [this id])
    (create [this folder])
    (update [this folder])
    (delete [this id])
)