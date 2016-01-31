(ns hareguu.core
  (:use [compojure.core]
        [hareguu.layout])
  (:require [clojure.java.io :as io]
            [clojure.string :as s])
  (:import [java.io InputStream FileInputStream File]))

(defn post-generator [req]
  (-> (:body req)
      slurp
      cdata
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

    (seq? body)
    (spit outpath (apply str body))

    (instance? InputStream body)
    (with-open [in body
                out (io/output-stream outpath)]
      (io/copy in out))

    (instance? File body)
    (let [f body]
      (with-open [in (FileInputStream. f)
                  out (io/output-stream outpath)]
        (io/copy in out)))

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

(defn -main
  "build the site"
  [& args]
  (let [src-dir "content"]
    (doseq [f (filter
               (memfn isFile)
               (file-seq (io/file src-dir)))]
      (generate-file f))))
