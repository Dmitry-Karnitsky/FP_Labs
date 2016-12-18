(ns dropbox.data.protocols.fileshares-repository-protocol)

(defprotocol FileSharesRepositoryProtocol
    (create [this fileshare])
    (delete [this fileshare])
)