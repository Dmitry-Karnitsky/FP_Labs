(ns dropbox.data.protocols.userfiles-repository-protocol)

(defprotocol UserFilesRepositoryProtocol
    (create [this userfile])
    (delete [this userfile])
)