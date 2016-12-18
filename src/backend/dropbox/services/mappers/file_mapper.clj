(ns dropbox.services.mappers.file-mapper
    (:require
        [dropbox.data.entities.file :as df]
        [dropbox.services.entities.file :as f]
		[dropbox.infrastructure.helpers :as h]
        [dropbox.infrastructure.converters :as c]))

(defn business->data
    [business]
    (df/->File
        (:id business)
        (:filename business)
        (:content business)
        (:owner-id business)
        (:create-date business)))

(defn raw->business
    [raw]
    (f/->File
        (:fileid raw)
        (:filename raw)
        (:filebytes raw)
        (c/to-date-time (:createdate raw))
        (:ownerid raw)
        (:username raw)))

(defn business->map
    [business]
    (hash-map
        :id (:id business)
        :filename (:filename business)
        :content (:content business)
        :create-date (:create-date business)
        :owner-id (:owner-id business)
        :owner-name (:owner-name business)))
