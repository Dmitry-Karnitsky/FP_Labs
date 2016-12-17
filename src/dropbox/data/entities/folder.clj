(ns dropbox.data.entities.folder)

(defrecord Folder [
    id
    folder-name
    private?
    root?
    ownerid
    create-date
    update-date])