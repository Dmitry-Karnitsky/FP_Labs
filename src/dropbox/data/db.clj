(ns dropbox.data.db
  (:require
    [clojure.java.jdbc :as jdbc],
    [dropbox.data.db.db-spec :refer :all],
    [dropbox.data.mappers.user-entity-mapper :refer :all],
   ; Время и дата
    [joda-time :as t])

  ; Импортируем методы из Java библиотек
  (:import org.bson.types.ObjectId 
           org.joda.time.DateTimeZone))

; Во избежание ошибок нужно указать часовой пояс
(DateTimeZone/setDefault DateTimeZone/UTC)

(def user-mapper (->UserMapper))

; Приватная функция создания штампа даты и времени
(defn- date-time
  "Текущие дата и время"
  [] 
  (t/date-time))

(defn get-notes
  "Получить все заметки"
  []

  (let [ent (first (jdbc/query db-spec
            ["SELECT UserId, Username, Password, RegisterDate FROM Users WHERE Username = ?", "user2"]
                {:row-fn (fn [map] (.data->entity user-mapper map))}))
        _ (println (.entity->data user-mapper ent))
        _ (try 
            (println (jdbc/query db-spec ["EXEC usp_CreateUser ?, ?" "newuser6" "newpassword"]))
            (catch Exception e (println e)))
       ]
  )
)


; ; Создадим переменную соединения с БД
; (defonce db
;   (let [uri "mongodb://127.0.0.1/notes_db"
;         {:keys [db]} (mg/connect-via-uri uri)]
;     db))



(defn remove-note
  "Удалить заметку по ее ObjectId"
  [id]
  (1))
  ; ; Переформатируем строку в ObjectId
  ; (let [id (ObjectId. id)]
  ;   (m/remove-by-id db "notes" id)))

(defn update-note
  "Обновить заметку по ее ObjectId"
  [id note]
  (1))
  ; ; Переформатируем строку в ObjectId
  ; (let [id (ObjectId. id)]

  ;   ; Здесь мы используем оператор $set
  ;   ; с его помощью если в документе имеются
  ;   ; другие поля они не будут удалены
  ;   ; обновятся только те которые есть
  ;   ; в нашем hash-map + если он включает
  ;   ; поля которых нет в документе они
  ;   ; они будут добавлены к нему.
  ;   ; Так-же обновлять документы можно
  ;   ; по их ObjectId с помощью
  ;   ; функции update-by-id,
  ;   ; для наглядности я оставил обновление
  ;   ; по любым параметрам
  ;   (m/update db "notes" {:_id id}

  ;             ; Обновим помимо документа
  ;             ; дату его создания
  ;             {$set (assoc note
  ;                     :created (date-time))})))

(defn get-note
  "Получить заметку по ее ObjectId"
  [id]

  ; ; Если искать документ по его :_id
  ; ; и в качестве значения передать
  ; ; ему строку а не ObjectId
  ; ; мы получим ошибку, поэтому
  ; ; переформатируем его в тип ObjectId
  ; (let [id (ObjectId. id)]

  ;   ; Эта функция вернет hash-map найденного документа
  ;   (m/find-map-by-id db "notes" id)))
  (1))

(defn create-note
  "Создать заметку в БД"

  ; Наша заметка принимается от котроллера
  ; и имеет тип hash-map c видом:
  ; {:title "Заголовок" :text "Содержание"}
  [note]
  (1)
  ; ; Monger может сам создать ObjectId
  ; ; но разработчиками настоятельно рекомендуется
  ; ; добавить это поле самостоятельно
  ; (let [object-id (ObjectId.)]

  ;   ; Нам остается просто передать hash-map
  ;   ; функции создания документа, только
  ;   ; добавим в него сгенерированный ObjectId
  ;   ; и штамп даты и времени создания
  ;   (m/insert db "notes" (assoc note
  ;                          :_id object-id
  ;                          :created (date-time))))
                           )

 
