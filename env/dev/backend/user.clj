(ns user
  (:require [mount.core :as mount]
            dropbox.core))

(defn start []
  (mount/start-without #'dropbox.core/repl-server))

(defn stop []
  (mount/stop-except #'dropbox.core/repl-server))

(defn restart []
  (stop)
  (start))