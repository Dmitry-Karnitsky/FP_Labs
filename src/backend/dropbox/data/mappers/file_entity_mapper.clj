(ns dropbox.data.mappers.file-entity-mapper
    (:require
        [dropbox.data.mappers.protocols.db-entity-mapper-protocol :as m]
        [dropbox.data.entities.file :as f]
        [dropbox.infrastructure.converters :as c]))

(deftype FileMapper []
    m/DbEntityMapperProtocol

    (data->entity [this row]
        (f/->File
            (:fileid row)
            (:filename row)
            (:filebytes row)
            (:ownername row)
            (c/to-date-time (:createdate row))))

    (entity->data [this entity]
        (hash-map
            :filename (:filename entity)
            :filebytes (:content entity)
            :ownerid (:owner-name entity)
            :createdate (c/to-sql-time (:create-date entity))))
)