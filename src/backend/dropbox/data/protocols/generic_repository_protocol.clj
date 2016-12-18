(ns dropbox.data.protocols.generic-repository-protocol)

(defprotocol GenericRepositoryProtocol
    (execute-select [this query])
)