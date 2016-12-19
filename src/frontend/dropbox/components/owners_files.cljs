(ns dropbox.components.owners-files
	(:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [ajax.core :as ajax]))

(defonce files (atom []))

(defn list-files! []
  (ajax/GET (str "/files")
    {:handler
      #(reset! files %)}))

(defn delete-file-by-id [fileid]
     (swap! files #(remove (fn [file] (= (:id file) fileid)) %)))

(defn remove-file! [fileid]
  (ajax/DELETE (str "/files/" fileid)
    {:handler
      #(delete-file-by-id fileid)}))

(defn file-row [row]
  (fn []
    [:tr
      ^{:key (str (:filename row) (:file-size row) (:create-date row))}
      [:td (:filename row)]
      [:td (:file-size row)]
      [:td (:create-date row)]
      [:td>a.btn {:on-click #(remove-file! (:id row))} "Delete" ]
      [:td>a.btn {:href (str "#/file-properties/" (:id row))} "Properties" ]]))

(defn owners-files []
	(list-files!)
  (fn [] (when-let [files @files]
     [:div
        [:table.table.table-striped
          [:thead>tr
            [:th "Fila name"]
            [:th "File size"]
            [:th "Create date"]
            [:th]
            [:th]]
          [:tbody
            (for [row files]
              ^{:key row}
              [file-row row])]]])))

