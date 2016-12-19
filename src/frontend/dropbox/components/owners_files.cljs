(ns dropbox.components.owners-files
	(:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [ajax.core :as ajax]))

(defonce files (atom []))

(defn list-files! []
  (ajax/GET (str "/files")
            {:handler
              #(reset! files %)}))

(defn exclude-file-by-id! [fileid]
  (filter #(not= fileid (:id files))))

(defn remove-file! [fileid]
  (ajax/DELETE (str "/files/" fileid)
              {:handler
                #(swap! files (exclude-file-by-id! fileid))}))

(defn file-row [row]
  (fn []
    [:tr
         ^{:key (str (:filename row) (:file-size row) (:create-date row))}
          [:td (:filename row)]
          [:td (:file-size row)]
          [:td (:create-date row)]
          [:td>a.btn {:on-click #(remove-file! (:id row))} "Delete" ]]))

(defn owners-files [user-id]
  (.log js/console user-id)
	(list-files!)
  (fn [] (when-let [files @files]
		  [:table.table.table-striped
			[:thead>tr
        [:th "Fila name"]
        [:th "File size"]
        [:th "Create date"]
        [:th]]
			[:tbody
				(for [row files]
					^{:key row}
          [file-row row])]])))

