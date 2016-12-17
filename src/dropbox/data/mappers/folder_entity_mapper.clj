(ns dropbox.data.mappers.folder-entity-mapper
    (:require
        [dropbox.data.mappers.protocols.db-entity-mapper-protocol :as m]
        [dropbox.data.entities.folder :as f]
        [dropbox.infrastructure.converters :as c]))

(deftype FolderMapper []
    m/DbEntityMapperProtocol

    (data->entity [this row]
        (f/->Folder
            (:folderid row)
            (:foldername row)
            (:isprivate row)
            (:isroot row)
            (:ownerid row)
            (c/to-date-time (:createdate row))
            (c/to-date-time (:updatedate row))))

    (entity->data [this entity]
        (hash-map
            :foldername (:folder-name entity)
            :isprivate (:private? entity)
            :isroot (:root? entity)
            :ownerid (:ownerid entity)
            :createdate (c/to-sql-time (:create-date entity))
            :updatedate (c/to-sql-time (:update-date entity))))
)