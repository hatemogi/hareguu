(defproject hareguu "0.1.0"
  :description "yet another static site generator"
  :url "https://github.com/hatemogi/hareguu"
  :license {:name "BSD License"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [misaeng "0.1.0"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.5.0"]
                 [hiccup "1.0.5"]

                 [org.clojure/clojurescript "1.7.228"]
                 [com.cemerick/piggieback "0.2.1"]
                 [figwheel-sidecar "0.5.0-6"]

                 [cljs-ajax "0.5.3"]
                 [reagent "0.6.0-alpha"]
                 [re-frame "0.7.0-alpha-3"]
                 [cljsjs/bootstrap "3.3.6-0"]
                 [cljsjs/marked "0.3.5-0"]
                 [cljsjs/highlight "8.4-0"]
                 [cljsjs/d3 "3.5.7-1"]
                 [cljsjs/google-analytics "2015.04.13-0"]]
  :plugins [[lein-figwheel "0.5.0-6"]
            [lein-cljsbuild "1.1.2" :exclusions [[org.clojure/clojure]]]]
  :repl-options {:init (set! *print-length* 50)
                 :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :figwheel {:css-dirs ["resources/public/css"]
             :open-file-command "emacsclient"}
  :cljsbuild {:builds {:dev {:source-paths ["src-cljs/"]
                             :figwheel true
                             :compiler {:main "hareguu.main"
                                        :asset-path "/js"
                                        :output-to "output/js/hareguu.js"
                                        :output-dir "output/js"}}
                       :prod {:source-paths ["src-cljs/"]
                              :compiler {:main "hareguu.main"
                                         :optimizations :advanced
                                         :asset-path "/js"
                                         :output-to "output/js/hareguu.js"
                                         :output-dir "output/js"}}}}
  :main ^:skip-aot hareguu.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
