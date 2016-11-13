(ns dropbox.routes
  (:require

   ; Работа с маршрутами
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [dropbox.infrastructure.converters :as conv]
   [dropbox.views :as v]
   [dropbox.config :as conf]
   [dropbox.data.files-repository :as r]))

(def repo (r/->FilesRepository conf/db-spec))

; Объявляем маршруты
(defroutes dropbox-routes

  (GET "/"
       []
       (let [files (.get-all repo 1)]
         (v/index files)))

  (GET "/files/:id"
      [id]
      (let [files (.get-all repo id)]
          (v/index files)))

  (route/not-found "Empty"))
