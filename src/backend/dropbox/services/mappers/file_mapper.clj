(ns dropbox.services.mappers.file-mapper
    (:require
        [dropbox.data.entities.file :as df]
        [dropbox.services.entities.file :as f]
        [dropbox.infrastructure.helpers :as h]
        [dropbox.infrastructure.converters :as c])

    (:import
        [java.awt.image AffineTransformOp BufferedImage]
        [java.io ByteArrayOutputStream FileInputStream]
        java.awt.geom.AffineTransform
        javax.imageio.ImageIO
        java.net.URLEncoder))

(defn- file->byte-array [x]
(with-open [input ( FileInputStream. x)
            buffer (ByteArrayOutputStream.)]
    (clojure.java.io/copy input buffer)
    (.toByteArray buffer)))

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
        (:filesize raw)
        (c/to-date-time (:createdate raw))
        (:ownername raw)))

(defn business->map
    [business]
    (hash-map
        :id (:id business)
        :filename (:filename business)
        :content (:content business)
        :owner-name (:owner-name business)
        :create-date (:create-date business)))

(defn map->data
    [business]
    (df/->File
        (:filename map)
        (file->byte-array (:tempfile map))
        (:owner-name map)
        (c/to-date-time (:createdate map))))
