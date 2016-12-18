(ns dropbox.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [dropbox.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[dropbox started successfully using the development profile]=-"))
   :middleware wrap-dev})