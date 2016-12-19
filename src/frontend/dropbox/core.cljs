(ns dropbox.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [cljsjs.bootstrap :as bs]
            [goog.history.EventType :as EventType]
            [markdown.core :refer [md->html]]
            [dropbox.ajax :refer [load-interceptors!]]
            [ajax.core :as ajax]
            [dropbox.components.registration :as reg]
            [dropbox.components.login :as l]
            [dropbox.components.owners-files :as of]
            [dropbox.components.file-properties :as fp])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn account-actions [id]
  [:a.btn
    {:on-click
      #(ajax/POST
        "/logout"
        {:handler (fn [] (session/remove! :identity))})}
          "sign out"])

(defn user-menu []
  (if-let [id (session/get :identity)]
    [:ul.nav.navbar-nav.pull-xs-right
      [:li.nav-item
       [account-actions id]]]
    [:ul.nav.navbar-nav.pull-xs-right
      [:li.nav-item [l/login-button]]
      [:li.nav-item [reg/registration-button]]]))

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "Dropbox"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/Shared" "Shared" :about collapsed?]]]
       [user-menu]])))

(defn about-page []
  [:div "Dropbox about"])

(defn home-page []
  (fn []
    [:div.container
     [:div.row
      [:div.col-md-12
        (if-let [id (session/get :identity)]
          [of/owners-files]
          [:h2 "Welcom to Dropbox. Please Login or Register"])]]]))

(defn file-properties-page []
  (fn []
    [:div.container
     [:div.row
      [:div.col-md-12
        [fp/file-properties]]]]
  ))

(def pages
  {:home               #'home-page
   :file-properties    #'file-properties-page
   :about              #'about-page})

(defn modal []
  (when-let [session-modal (session/get :modal)]
    [session-modal]))

(defn page []
  [:div
   [modal]
   [(pages (session/get :page))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
                    (session/put! :page :home))
(secretary/defroute "/file-properties/:file-id" [file-id]
                    (session/put! :file-id file-id)
                    (session/put! :page :file-properties))
(secretary/defroute "/about" []
                    (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (session/put! :identity js/identity)
  (mount-components))
