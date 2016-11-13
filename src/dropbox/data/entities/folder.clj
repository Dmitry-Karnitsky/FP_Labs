(ns dropbox.data.entities.folder)

(defrecord Folder [
    id
    foldername
    is-private
    is-root
    create-date
    update-date])