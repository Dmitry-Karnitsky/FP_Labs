(ns dropbox.data.mappers.protocols.db-entity-mapper-protocol)

(defprotocol DbEntityMapperProtocol
    (entity->data [this row])
    (data->entity [this entity]))