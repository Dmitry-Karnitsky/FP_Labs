(ns dropbox.data.mappers.fileshare-entity-mapper
    (:require
        [dropbox.data.mappers.protocols.db-entity-mapper-protocol :as m]
        [dropbox.data.entities.fileshare :as f]
        [dropbox.infrastructure.converters :as c]))

(deftype FileShareMapper []
    m/DbEntityMapperProtocol

    (data->entity [this row]
        (f/->FileShare
            (:fileshareid row)
            (:fileid row)
            (:friendshipid row)))

    (entity->data [this entity]
        (hash-map
            :fileid (:file-id entity)
            :friendshipid (:friendship-id entity)))
)