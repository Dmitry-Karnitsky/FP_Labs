(ns dropbox.data.mappers.userfile-entity-mapper
    (:require
        [dropbox.data.mappers.protocols.db-entity-mapper-protocol :as m]
        [dropbox.data.entities.userfile :as f]
        [dropbox.infrastructure.converters :as c]))

(deftype UserFileMapper []
    m/DbEntityMapperProtocol

    (data->entity [this row]
        (f/->UserFile
            (:userfileid row)
            (:fileid row)
            (:friendshipid row)))

    (entity->data [this entity]
        (hash-map
            :fileid (:file-id entity)
            :friendshipid (:friendship-id entity)))
)