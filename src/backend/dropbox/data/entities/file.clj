(ns dropbox.data.entities.file)

(defrecord File [
    id
    filename
    content
    private?
    folder-id
    create-date])