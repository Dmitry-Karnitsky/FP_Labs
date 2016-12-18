(ns dropbox.services.entities.file)

(defrecord File [
    id
    filename
    content
    create-date
	owner-id
	owner-name])