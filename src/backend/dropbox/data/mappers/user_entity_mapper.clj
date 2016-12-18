(ns dropbox.data.mappers.user-entity-mapper
    (:require
        [dropbox.data.mappers.protocols.db-entity-mapper-protocol :as m]
        [dropbox.data.entities.user :as u]
        [dropbox.infrastructure.converters :as c]))

(deftype UserMapper []
    m/DbEntityMapperProtocol

    (data->entity [this row]
        (u/->User
            (:userid row)
            (:username row)
            (:password row)
            (c/to-date-time (:registerdate row))))

    (entity->data [this entity]
        (hash-map
            :username (:username entity)
            :password (:password entity)
            :registerdate (c/to-sql-time (:register-date entity))))
)