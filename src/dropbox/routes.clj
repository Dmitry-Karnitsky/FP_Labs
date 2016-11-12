(ns dropbox.routes
  (:require

   ; Работа с маршрутами
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]

   ; Контроллеры запросов
   [dropbox.controllers :as c]

   ; Отображение страниц
   [dropbox.views :as v]

   ; Функции для взаимодействия с БД
   [dropbox.db :as db]))

; Объявляем маршруты
(defroutes dropbox-routes

  ; Страница просмотра заметки
  (GET "/note/:id"
       [id]

       ; Получим нашу заметку по ее ObjectId
       ; и передадим данные в отображение
       (let [note (db/get-note id)]
         (v/note note)))

  ; Контроллер удаления заметки по ее ObjectId
  (GET "/delete/:id"
       [id]
       (c/delete id))

  ; Обработчик редактирования заметки
  (POST "/edit/:id"
        request
        (-> c/edit))

  ; Страница редактирования заметки
  ; на деле, полагаю использовать
  ; ObjectId документа в запросах
  ; плохая идея, но в качестве
  ; примера сойдет.
  (GET "/edit/:id"
       [id]

       ; Получим нашу заметку по ее ObjectId
       ; и передадим данные в отображение
       (let [note (db/get-note id)]
         (v/edit note)))

  ; Обработчик добавления заметки
  (POST "/create"

        ; Можно получить необходимые нам значения
        ; в виде [title text], но мы возьмем
        ; request полностью и положим
        ; эту работу на наш обработчик
        request

        ; Этот синтаксический сахар аналогичен
        ; выражению: (create-controller request)
        (-> c/create))

  ; Страница добавления заметки
  (GET "/create"
       []
       (v/create))


  ; Главная страница приложения
  (GET "/"
       []
       (let [notes (db/get-notes)]
         (v/index notes)))

  ; Ошибка 404
  (route/not-found "Ничего не найдено"))
