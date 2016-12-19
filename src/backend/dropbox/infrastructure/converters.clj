(ns dropbox.infrastructure.converters)

(require '[clj-time.format :as f])
(require '[clj-time.coerce :as c])


(defn to-date-time [param]
    (if (some? param)
        (f/parse
            (f/formatters :date-time)
            (c/to-string param))
    nil))


(defn to-sql-time [param]
    (c/to-sql-time param))