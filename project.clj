(defproject lab "0.1.0"
  :description "Oliver's Lab"
  :url "http://www.opowell.com/lab"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [devcards "0.2.0-8"]
                 [sablono "0.3.6"]
                 [org.omcljs/om "0.9.0"]
                 [reagent "0.5.1"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.1"]]

  :clean-targets ^{:protect false} ["resources/public/lab/js/compiled"
                                    "target"]
  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel { :devcards true }
                        :compiler { :main       "lab.core"
                                    :asset-path "js/compiled/lab_out"
                                    :output-to  "resources/public/lab/js/compiled/lab.js"
                                    :output-dir "resources/public/lab/js/compiled/lab_out"
                                    :source-map-timestamp true }}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:main       "lab.core"
                                   :devcards true
                                   :asset-path "js/compiled/lab_out"
                                   :output-to  "resources/public/lab/js/compiled/lab.js"
                                   :optimizations :advanced}}]}

  :figwheel { :css-dirs ["resources/public/assets/css"] })
