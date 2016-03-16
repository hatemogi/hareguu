(ns hareguu.layout
  (:use [hiccup.core]
        [hiccup.page]))

(defn footer []
  [:footer "Copyright (c) 2016 Daehyun Kim"])

(defn layout [content & opts]
  (html5 {:lang "ko"}
         [:head
          [:meta {:charset "utf-8"}]
          [:meta {:http-equiv "X-UA-Compatible", :content "IE=edge"}]
          [:meta {:name "viewport", :content "width=device-width, initial-scale=1"}]
          [:title (get opts :title "Hare and Guu site generator")]
          (map include-css
               (list* "//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
                      "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"
                      (:css opts)))]
         [:body
          [:nav "navigation"]
          [:main content]
          (footer)
          (include-js "/js/hareguu.js")]))

(defn markdown-div [content]
  [:div {:data-markdown true} content])
