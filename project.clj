(defproject hareguu "0.1.0"
  :description "yet another static site generator"
  :url "https://github.com/hatemogi/hareguu"
  :license {:name "BSD License"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [cljsjs/bootstrap "3.3.6-0"]
                 [cljsjs/marked "0.3.5-0"]
                 [cljsjs/highlight "8.4-0"]]
  :plugins [[lein-figwheel "0.5.0-4"]]
  :cljsbuild
  {:builds [{:id "example"
             :source-paths ["src/"]
             :figwheel true
             :compiler {:main "hareguu.main"
                        :asset-path "/js"
                        :output-to "output/js/hareguu.js"
                        :output-dir "output/js"}}]}
  :main ^:skip-aot hareguu.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
