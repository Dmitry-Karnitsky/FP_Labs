(ns dropbox.data.protocols.friendships-repository-protocol)

(defprotocol FriendshipsRepositoryProtocol
    (get-all [this username])
    (create [this friendship])
    (delete [this friendship])
)