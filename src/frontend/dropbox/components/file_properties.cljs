(ns dropbox.components.file-properties
	(:require [reagent.core :refer [atom]]
			  [reagent.session :as session]
			  [ajax.core :as ajax]))

(def file (atom []))

(defn fetch-file-properties! [file-id]
	(ajax/GET (str "/files/" file-id)
		{:handler
			#(reset! file (js->clj (.parse js/JSON %) {:keywordize-keys true}))}))
;
(defn file-properties []
	(fetch-file-properties! (session/get :file-id))
	(fn [] (when-let [file @file]
		[:div
			[:div {:class "base"}]
			[:table.table.table-striped
				[:thead>tr
					[:th "Property"]
					[:th "Value"]]
				[:tbody
					(for [selected-prop (map (fn [[key value]] (vector key value)) file)]
					^{:key selected-prop}
						[:tr
							[:td (first selected-prop)]
							[:td (second selected-prop)]
					])]
		]])))

