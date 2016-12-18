(ns dropbox.data.dsl
  (:require [clojure.string :as s]))

(defrecord Sql [sql args])

(defprotocol SqlLike
  (as-sql [this]))

; вспомогательная функция
(defn quote-name
  [s]
  (let [x (name s)]
    (if (= "*" x)
      x
      (str "[" (s/replace x "." "].[") "]"))))

(extend-protocol SqlLike

  ; для любого `x` (= (as-sql (as-sql x)) (as-sql x))
  Sql
  (as-sql [this] this)

  ; по умолчанию считаем все объекты параметрами для запросов
  Object
  (as-sql [this] (Sql. "?" [this]))

  ; экранируем имена таблиц и столбцов
  clojure.lang.Keyword
  (as-sql [this] (Sql. (quote-name this) nil))

  ; символами обозначаем ключевые слова SQL
  clojure.lang.Symbol
  (as-sql [this] (Sql. (name this) nil))

  ; для nil специальное ключевое слово
  nil
  (as-sql [this] (Sql. "NULL" nil)))


; вспомогательная функция, объединяет 2 sql-объекта в один
(defn- join-sqls
  ([] (Sql. "" nil))
  ([^Sql s1 ^Sql s2]
    (Sql. (str (.sql s1) " " (.sql s2)) (concat (.args s1) (.args s2)))))

(extend-protocol SqlLike
  clojure.lang.Sequential
  (as-sql [this]
    (reduce join-sqls (map as-sql this))))

(defn- to-sql-params
  [relation]
  (let [{s :sql p :args} (as-sql relation)]
    (vec (cons s p))))

(defn top
  [relation v]
  (assoc relation :top v))

(defn fields
  [query fd]
  (assoc query :fields fd))

(defn where
  [query wh]
  (assoc query :where wh))

(defn join*
  [{:keys [tables joins] :as q} type alias table on]
  (let [a (or alias table)]
    (assoc
      q
      :tables (assoc tables a table)
      :joins (conj (or joins []) [a type on]))))

(defn from
  ([q table] (join* q nil table table nil))
  ([q table alias] (join* q nil table alias nil)))

(def empty-select {})

(defmacro select
  [& body]
   `(-> empty-select ~@body))

; пустой SQL, поскольку nil преобразуется в "NULL"
(def NONE (Sql. "" nil))

; большинство фукнций реализуется тривиально
(defn render-top [s]
  (if-let [l (:top s)]
    ['TOP l]
    NONE))

(defn render-fields [s] '*)  ; пока будем возвращать все столбцы

; эти функции реализуем чуть позже
(defn render-where [s] NONE)
(defn render-order [s] NONE)
(defn render-expression [s] NONE)

; вспомогательная функция
(defn render-table
  [[alias table]]
  (if (= alias table)
    ; если алиас и таблица совпадают, то не выводим 'AS'
    table
    [table 'AS alias]))

(defn render-join-type
  [jt]
  (get
    {nil (symbol ",")
     :cross '[CROSS JOIN],
     :left '[LEFT OUTER JOIN],
     :right '[RIGHT OUTER JOIN],
     :inner '[INNER JOIN],
     :full '[FULL JOIN],
     } jt jt))

; некоторые функции довольно сложные
(defn render-from
  [{:keys [tables joins]}]
  ; секции FROM может и не быть!
  (if (not (empty? joins))
    ['FROM
     ; первый джоин
     (let [[a jn] (first joins)
           t (tables a)]
       ; первый джоин должен делаться при помощи `(from ..)`
       (assert (nil? jn))
       (render-table [a t]))
     ; перебираем оставшиеся джоины
     (for [[a jn c] (rest joins)
           :let [t (tables a)]]
       [(render-join-type jn) ; связка JOIN XX или запятая
        (render-table [a t])  ; имя таблицы и алиас
        (if c ['ON (render-expression c)] NONE) ; секция 'ON'
        ])]
    NONE))

(defn render-select
  [select]
  ['SELECT
   (mapv
     #(% select)
     [render-top
      render-fields
      render-from
      render-where
      render-order])])


(declare render-select)

; все поля объявлять не обязательно
; record поддерживает установку ключей, не перечисленных при объявлении типа
(defrecord Select [fields where order joins tables top]
  SqlLike
  (as-sql [this] (as-sql (render-select this))))

(def empty-select (map->Select {}))

; склеиваем 2 выражения вместе при помощи AND
(defn- conj-expression
  [e1 e2]
  (cond
    (not (seq e1)) e2
    (= 'and (first e1)) (conj (vec e1) e2)
    :else (vector 'and e1 e2)))

(defn where*
  [query expr]
    (assoc query :where (conj-expression (:where query) expr)))


; взаимнорекурсивные функции
(declare render-operator)
(declare render-expression)

; функция или оператор?
(defn- function-symbol? [s]
  (re-matches #"\w+" (name s)))

; форматируем вызов функции или оператора
(defn render-operator
  [op & args]
  (let [ra (map render-expression args)
        lb (symbol "(")
        rb (symbol ")")]
    (if (and (function-symbol? op) (not= (op "like")))
      ; функция (count, max, ...)
      [(s/upper-case op) lb (interpose (symbol ",") ra) rb]
      ; оператор (+, *, ...)
      [lb (interpose op (map render-expression args)) rb])))

(defn render-expression
  [etree]
  (if (and (sequential? etree) (or (symbol? (first etree)) (= (first etree) "like")))
    (apply render-operator etree)
    etree))

(defn render-where
  [{:keys [where]}]
  (if where
    ['WHERE (render-expression where)]
    NONE))

(declare prepare-expression)

(defmacro where
  [q body]
  `(where* ~q ~(prepare-expression body)))


(defn- canonize-operator-symbol
  [op]
  (get '{not= <>, == =} op op))

; перепишем функцию
(defn prepare-expression
  [e]
  (if (seq? e)
    `(vector
       (quote ~(canonize-operator-symbol (first e)))
       ~@(map prepare-expression (rest e)))
    e))


; импортируем очень полезный макрос `do-template`
(use 'clojure.template)

; этот код разворачивается в 5 объявлений макросов
(do-template
  [join-name join-key]  ; параметры для шаблона

  ; сам шаблон
  (defmacro join-name
   ([relation alias table cond]
     `(join* ~relation ~join-key ~alias ~table ~(prepare-expression cond)))
   ([relation table cond]
     `(let [table# ~table]
        (join* ~relation ~join-key nil table# ~(prepare-expression cond)))))

  ; значения для параметров
  join-inner :inner,
  join :inner,
  join-right :right,
  join-left :left,
  join-full :full)

(defn order*
  ([relation column] (order* relation column nil))
  ([{order :order :as relation} column dir]
    (assoc
      relation
      :order (cons [column dir] order))))

(defmacro order-by
  ([relation column]
     `(order* ~relation
              ~(prepare-expression column)))
  ([relation column dir]
     `(order* ~relation
              ~(prepare-expression column) ~dir)))

(defn render-order
  [{order :order}]
  (let [f (fn [[c d]]
            [(render-expression c)
             (get {nil [] :asc 'ASC :desc 'DESC} d d)])]
    (if order
      ['[ORDER BY] (interpose (symbol ",") (map f order))]
      [])))

(defn- map-vals
  [f m]
  (into (if (map? m) (empty m) {}) (for [[k v] m] [k (f v)])))

; счетчик для генерации уникальных идентификаторов
(def surrogate-alias-counter (atom 0))

; генерируем идентификаторы вида :__00001234
(defn generate-surrogate-alias
  []
  (let [k (swap! surrogate-alias-counter #(-> % inc (mod 1000000)))]
    (keyword (format "__%08d" k))))


; применяем `f` к значениям `m` (не ключам)
(defn- map-vals
  [f m]
  (into (if (map? m) (empty m) {}) (for [[k v] m] [k (f v)])))

; счетчик для генерации уникальных идентификаторов
(def surrogate-alias-counter (atom 0))

; генерируем идентификаторы вида :__00001234
(defn generate-surrogate-alias
  []
  (let [k (swap! surrogate-alias-counter #(-> % inc (mod 1000000)))]
    (keyword (format "__%08d" k))))

; преобразуем произвольное выражение в "алиас"
(defn as-alias
  [n]
  (cond
    (keyword? n) n               ; имя столбца/таблицы оставляем как есть
    (string? n) (keyword n)      ; аналогично для строк
    :else (generate-surrogate-alias)))  ; для выражений генерируем суррогатный алиас

; список столбцов для запроса -- словарь "алиас - выражение" или вектор столбцов
(defn- prepare-fields
  [fs]
  (if (map? fs)
    (map-vals prepare-expression fs)
    (into {} (map (juxt as-alias prepare-expression) fs))))

(defn fields*
  [query fd]
  (assoc query :fields fd))

(defmacro fields
  [query fd]
  `(fields* ~query ~(prepare-fields fd)))

(defn render-field
  [[alias nm]]
  (if (= alias nm)
    nm  ; просто имя столбца
    [(render-expression nm) 'AS alias]))

(defn render-fields
  [{:keys [fields]}]
  (if (or (nil? fields) (= fields :*))
    '*
    (interpose (symbol ",") (map render-field fields))))


;;(def res
;;(select
;;  (limit :3)
;;  (fields [:name :age])
;;  (from :Users)
;;          (order-by :id :asc)
;;      (order-by :username :desc)
;;  (where ['= :id "user"])

;;  (as-sql)))

;;(println res)
;;(println "")

;;(def res (select
;;  (fields {:cnt (COUNT :*), :max-age (MAX :age)})
;;  (from :users)
;; (as-sql)))

;;(println res)

;;(println "")

;; (def res
;;   (select
;;   (fields {:UserId :u.UserId :FolderId :f.FolderId})
;;   (from :u :Users)
;;   (join :f :Folders (= :u.UserId :f.OwnerId))
;;   (where ['like :u.Username "%user%"])
;;   (order-by :u.UserId :desc)
;;   (as-sql)))

;; (println res)
;; (println "")

