(defproject hareguu "0.1.0"
  :description "yet another static site generator"
  :url "https://github.com/hatemogi/hareguu"
  :license {:name "BSD License"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [lein-cljsbuild "1.1.2"]
                 [lein-figwheel "0.5.0-6"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [cljsjs/bootstrap "3.3.6-0"]]
  :main ^:skip-aot hareguu.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
