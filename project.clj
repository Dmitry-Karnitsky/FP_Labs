(defproject dropbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies  [[org.clojure/clojure "1.8.0"]
                 [clojure.joda-time "0.6.0"]
                 [selmer "1.0.2"]
                 [markdown-clj "0.9.86"]
                 [ring-middleware-format "0.7.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [bouncer "1.0.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.2"]
                 [org.webjars/font-awesome "4.5.0"]
                 [org.webjars.bower/tether "1.1.1"]
                 [org.webjars/jquery "2.2.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [compojure "1.5.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.2.0"]
                 [mount "0.1.10"]
                 [cprop "0.1.6"]
                 [org.clojure/tools.cli "0.3.3"]
                 [luminus-nrepl "0.1.4"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [luminus-immutant "0.1.9"]
                 [buddy "0.10.0"]
                 [luminus-migrations "0.1.0"]
                 [conman "0.4.5"]
                 [org.clojure/clojurescript "1.7.228" :scope "provided"]
                 [reagent "0.5.1"]
                 [reagent-forms "0.5.21"]
                 [reagent-utils "0.1.7"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.2.374"]
                 [cljs-ajax "0.5.4"]
                 [metosin/compojure-api "1.0.1"]
                 [luminus-log4j "0.1.3"]
                 [sqljdbc42/sqljdbc42 "4.2"]]
  :plugins [[lein-ring "0.9.7"]]
  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :ring {:handler dropbox.handler/app}
  :main dropbox.core
  :source-paths ["src/backend" "src/common"]
  :resource-paths ["resources" "target/cljsbuild"]
  :cljsbuild
    {:builds
    {:app
      {:source-paths ["src/common" "src/frontend"]
      :compiler
      {:output-to "target/cljsbuild/public/js/app.js"
        :output-dir "target/cljsbuild/public/js/out"
        :externs ["react/externs/react.js"]
        :pretty-print true}}}}
  :target-path "target/%s/"
  :clean-targets ^{:protect false} [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :profiles
  {:dev          [:project/dev :profiles/dev]
   :project/dev  {:dependencies [[prone "1.0.2"]
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.7.1"]
                                 [com.cemerick/piggieback "0.2.2-SNAPSHOT"]
                                 [lein-figwheel "0.5.0-6"]
                                 [mvxcvi/puget "1.0.0"]]
                  :plugins [[lein-figwheel "0.5.0-6"] [org.clojure/clojurescript "1.7.228"]]
                   :cljsbuild
                   {:builds
                    {:app
                     {:source-paths ["env/dev/frontend"]
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
                   :ring-handler dropbox.handler/app}
                  :doo {:build "test"}
                  :source-paths ["env/dev/backend"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :profiles/dev {}})
