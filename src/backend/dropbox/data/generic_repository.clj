(ns dropbox.data.generic-repository
	(:require
		[dropbox.data.protocols.generic-repository-protocol :as protocol]
		[clojure.java.jdbc :as jdbc]
		[dropbox.data.dsl :as dsl :refer :all])

	(:import org.joda.time.DateTimeZone))

(DateTimeZone/setDefault DateTimeZone/UTC)

(defn- to-sql-params
	[query]
	(let [{s :sql p :args} (as-sql query)]
		(vec (cons s p))))

(deftype GenericRepository [db-spec]
	protocol/GenericRepositoryProtocol

	(execute-select [this query]
		(jdbc/query db-spec
			(to-sql-params query)
				{:result-set-fn vec}))
)