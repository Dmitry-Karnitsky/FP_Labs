(defproject dropbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [selmer "0.8.2"]
                 [markdown-clj "0.9.86"]
                 [ring-middleware-format "0.7.0"]
                 [com.novemberain/monger "2.0.1"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [clojure.joda-time "0.6.0"]
                 [sqljdbc42/sqljdbc42 "4.2"]
                 [bouncer "1.0.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.2"]
                 [org.webjars/font-awesome "4.5.0"]
                 [org.webjars.bower/tether "1.1.1"]
                 [org.webjars/jquery "2.2.1"]
                 [org.clojure/clojurescript "1.7.228" :scope "provided"]
                 [reagent "0.5.1"]
                 [reagent-forms "0.5.21"]
                 [reagent-utils "0.1.7"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.2.374"]
                 [metosin/compojure-api "1.0.1"]
                 [cljs-ajax "0.5.4"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-cljsbuild "1.1.1"]]
  :ring {:handler dropbox.handler/app}
  :source-paths ["src/backend" "src/common"]
  :resource-paths ["resources" "target/cljsbuild"]
  :cljsbuild
    {:builds
      {:app
        {:source-paths ["src/frontend" "src/common"]
         :compiler
           {:output-to "target/cljsbuild/public/js/app.js"
            :output-dir "target/cljsbuild/public/js/out"
            :externs ["react/externs/react.js"]
            :pretty-print true}}}}
  :target-path "target/%s/"
  :profiles
    {:dev
      {:dependencies [[javax.servlet/servlet-api "2.5"]
                      [ring/ring-mock "0.3.0"]
                      [lein-figwheel "0.5.0-6"]]
        :plugins [[lein-figwheel "0.5.0-6"]
                  [org.clojure/clojurescript "1.7.228"]]
        :cljsbuild
          {:builds
            {:app
              {:source-paths ["env/dev/cljs"]
              :compiler
                {:main "dropbox.app"
                :asset-path "/js/out"
                :optimizations :none
                :source-map true}}}}
        :figwheel
          {:http-server-root "public"
          :nrepl-port 7002
          :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
          :css-dirs ["resources/public/css"]
          :ring-handler dropbox.handler/app
        :source-paths ["env/dev/clj"]
        :resource-paths ["env/dev/resources"]
        :repl-options {:init-ns user}}}})
