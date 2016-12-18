(ns dropbox.data.mappers.friendship-entity-mapper
    (:require
        [dropbox.data.mappers.protocols.db-entity-mapper-protocol :as m]
        [dropbox.data.entities.friendship :as f]
        [dropbox.infrastructure.converters :as c]))

(deftype FriendshipMapper []
    m/DbEntityMapperProtocol

    (data->entity [this row]
        (f/->Friendship
            (:frienshipid row)
            (:username row)
            (:friendname row)))

    (entity->data [this entity]
        (hash-map
            :username (:user-name entity)
            :friendname (:friend-name entity)))
)