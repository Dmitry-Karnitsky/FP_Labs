(ns dropbox.services.entities.user)

(defrecord User [
    id
    username
    password
    register-date])