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
        (:owner-name business)
        (:create-date business)))

(defn raw->business
    [raw]
    (f/->File
        (:fileid raw)
        (:filename raw)
        (alength (:filebytes raw))
        (c/to-date-time (:createdate raw))
        (:ownername raw)))

(defn business->map
    [business]
    (hash-map
        :id (:id business)
        :filename (:filename business)
        :content (:content business)
        :owner-name (:owner-name business))
        :create-date (:create-date business))
