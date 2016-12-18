(ns dropbox.services.entities.file)

(defrecord File [
    id
    filename
    file-size
    create-date
	owner-name])