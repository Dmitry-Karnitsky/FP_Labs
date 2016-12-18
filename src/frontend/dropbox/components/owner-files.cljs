(ns dropbox.components.owner-files
	(:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [ajax.core :as ajax]))

(defonce files (atom []))

(defn list-files! [user-id]
  ajax/GET (str "/files/" user-id)
            {:handler
              #(reset!
                files
                (map
                  (fn [[fileid & rest]] (hash-map :id fileid :file-data rest))
                  %))})

(defn exclude-file-by-id! [fileid]
  (filter #(not= fileid (:fileid files))))

(defn remove-file! [fileid]
  ajax/DELETE (str "/files/" fileid)
              {:handler
                #(swap! files (exclude-file-by-id! fileid))})

(defn file-row [[fileid filename size creationdate]]
  [:tr {:id fileid}
          ^{:key (str filename size creationdate)}
          [:td filename]
          [:td size]
          [:td creationdate]
          [:td>a.btn {:on-click #(remove-file fileid)}]])

(defn owners-files [user-id]
	(fn [] (
		(list-files!)
		[:table.table-striped
			[:thead>tr]
			[:tbody
				(for [row files]
					^{:key row}
					[file-row row])])))

