(ns dropbox.data.entities.user)

(defrecord User [
    id
    username
    password
    register-date])