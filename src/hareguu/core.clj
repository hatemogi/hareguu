(ns hareguu.core
  (:use [미생.기본]
        [compojure.core]
        [hareguu.layout])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.edn :as edn]
            [hiccup.util :refer [escape-html]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]])
  (:import [java.io PushbackReader InputStream]))

(defn- skip-until-lf [^PushbackReader r]
  (if (= 10 (.read r))
    r
    (recur r)))

(defn parse-content [req]
  (if-not (instance? InputStream (:body req))
    req
    (let [buf (io/reader (:body req))
          req (assoc req :body buf)
          pushback (PushbackReader. buf)]
      (try
        (.mark buf 2048)
        (let [first-edn (edn/read pushback)]
          (println first-edn)
          (if (map? first-edn)
            (assoc req
                   :meta first-edn
                   :body (skip-until-lf pushback))
            (do
              (.reset buf)
              req)))
        (catch Exception e
          (.reset buf)
          req)))))

(defn debug [req]
  (println req)
  req)

(defn post-generator [req]
  (-> (parse-content req)
      debug
      :body
      slurp
      escape-html
      markdown-div
      layout))

(defroutes conversions
  (ANY "/post/:year/:title.md" req post-generator))

(defn replace-ext-to [ext s ]
  (let [last-dot (.lastIndexOf s ".")]
    (str (.substring s 0 last-dot) "." ext)))

(def replace-ext-to-html (partial replace-ext-to "html"))

(defn wrap-default-outpath [handler]
  (fn [req]
    (if-let [res (handler req)]
      (if (:outpath res)
        res
        (assoc res :outpath (replace-ext-to-html (:uri req)))))))

(def app (wrap-default-outpath conversions))

(defn render [outpath body]
  (cond
    (string? body)
    (spit outpath body)

    (nil? body)
    nil

    :else
    (throw (Exception. (str "Unrecognized body: " body)))))

(defn generate-file [f]
  (with-open [in (io/input-stream f)]
    (print "generating:" (.getPath f))
    (let [path (s/replace (.getPath f) #"content" "")
          req {:uri path :body in}
          res (app req)
          outpath (str "output" (:outpath res))]
      (when (and res outpath)
        (io/make-parents outpath)
        (render outpath (:body res))
        (println " ->" outpath)))))

(defn handler [req]
  nil)

(def serve
  (-> handler
      (wrap-file "output")
      (wrap-content-type)))

(let [server (atom nil)]
  (defn run []
    (let [s (run-jetty serve {:port 3000 :join? false})]
      (reset! server s)))
  (defn stop []
    (.stop @server)
    (reset! server nil)))

;; main.js 로딩을 위해 임베디드 서버가 있어야할듯
(defn -main
  "build the site"
  [& args]
  (let [src-dir "content"]
    (run! generate-file (filter (memfn isFile) (file-seq (io/file src-dir))))))
