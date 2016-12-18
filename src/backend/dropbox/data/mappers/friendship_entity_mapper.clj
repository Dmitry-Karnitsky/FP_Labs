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
            (:userid row)
            (:friendid row)))

    (entity->data [this entity]
        (hash-map
            :userid (:user-id entity)
            :friendid (:friend-id entity)))
)