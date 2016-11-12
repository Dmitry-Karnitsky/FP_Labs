(defproject dropbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [selmer "0.8.2"]
                 [com.novemberain/monger "2.0.1"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [clojure.joda-time "0.6.0"]
                 [sqljdbc42/sqljdbc42 "4.2"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler dropbox.handler/app}
  :profiles
  {:dev
   {:dependencies
    [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
