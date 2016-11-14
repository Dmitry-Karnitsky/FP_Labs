(ns dropbox.data.entities.folder)

(defrecord Folder [
    id
    folder-name
    private?
    root?
    create-date
    update-date])