(ns dropbox.config
  (:require [cprop.core :refer [load-config]]
            [cprop.source :as source]
            [mount.core :refer [args defstate]]))

(defstate env :start (load-config
                       :merge
                       [(args)
                        (source/from-system-props)
                        (source/from-env)]))

(def db-spec {:classname "com.microsoft.jdbc.sqlserver.SQLServerDriver"
               :subprotocol "sqlserver"
               :subname "//localhost:1433;database=Dropbox;integratedSecurity=true"})
