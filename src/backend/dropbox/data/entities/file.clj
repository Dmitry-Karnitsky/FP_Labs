(ns dropbox.data.entities.file)

(defrecord File [
    id
    filename
    content
    owner-name
    create-date])