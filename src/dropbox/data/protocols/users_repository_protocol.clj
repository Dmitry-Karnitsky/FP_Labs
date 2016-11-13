(ns dropbox.data.protocols.users-repository-protocol
    (:refer-clojure :exclude [get update]))

(defprotocol UsersRepositoryProtocol
    (get [this login])
    (create [this user])
    (update [this user])
)