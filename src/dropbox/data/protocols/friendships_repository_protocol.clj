(ns dropbox.data.protocols.friendships-repository-protocol
    (:refer-clojure :exclude [get update]))

(defprotocol FriendshipsRepositoryProtocol
    (get-all [this username])
    (create [this friendship])
    (delete [this friendship])
)