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
       (let [notes (.get-all repo 1)
            _ (assoc (first notes) :filename "newfile")
            _ (.update repo _)
            _ (.delete repo (second notes))]
         (v/index notes)))

  ; Ошибка 404
  (route/not-found "Empty"))
