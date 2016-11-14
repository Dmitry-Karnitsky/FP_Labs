(ns dropbox.routes
    (:require
        [compojure.core :refer [defroutes GET POST]]
        [compojure.route :as route]
        [dropbox.infrastructure.converters :as conv]
        [dropbox.views :as v]
        [dropbox.config :as conf]
        [dropbox.data.users-repository :as users-repo]
        [dropbox.data.files-repository :as files-repo]
        [dropbox.data.friendships-repository :as friendships-repo]
        [dropbox.data.folders-repository :as folders-repo]))

(def users (users-repo/->UsersRepository conf/db-spec))
(def files (files-repo/->FilesRepository conf/db-spec))
(def folders (folders-repo/->FoldersRepository conf/db-spec))
(def friendships (friendships-repo/->FriendshipsRepository conf/db-spec))

; Объявляем маршруты
(defroutes dropbox-routes

    (GET "/"
        []
        (let [u (.get users "user1")]
            (v/index u)))

    (GET "/repo/:repo/:method/:id"
        [repo, method, id]
        (let [r nil
              res nil
              r (if (= repo "folders") folders r)
              r (if (= repo "files") files r)
              r (if (= repo "users") users r)
              r (if (= repo "friendships") friendships r)
              res (if (= method "get-all") (.get-all r id) res)
              res (if (= method "get") (.get r id) res)
              res (if (= method "delete") (.delete r {:id id}) res)]
        (println res, repo, method, id)))

(route/not-found "Empty"))
