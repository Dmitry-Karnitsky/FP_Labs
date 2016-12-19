(ns dropbox.components.friends
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [ajax.core :as ajax]
            [dropbox.components.common :as c]))

(def friends (atom []))

(defn friends-page []
	[:div
		])