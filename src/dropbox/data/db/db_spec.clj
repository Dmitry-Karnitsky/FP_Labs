(ns dropbox.data.db.db-spec)

(def db-spec {:classname "com.microsoft.jdbc.sqlserver.SQLServerDriver"
               :subprotocol "sqlserver"
               :subname "//localhost:1433;database=Dropbox;integratedSecurity=true"})