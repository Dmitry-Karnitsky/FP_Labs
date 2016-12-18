(ns dropbox.infrastructure.helpers
	(:require
		[clj-time.core :as c]))

(defn utc-now []
	(c/now))