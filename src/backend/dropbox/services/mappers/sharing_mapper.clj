(ns dropbox.services.mappers.sharing-mapper
	(:require
		[dropbox.services.entities.usersharing :as us]
		[dropbox.infrastructure.helpers :as h]
		[dropbox.infrastructure.converters :as c]))

(defn raw->usersharing
	[raw]
	(us/->UserSharing
		(:friendname raw)
		(some? (:fileid raw))))
