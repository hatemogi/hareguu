(ns hareguu.layout
  (:use [hiccup.core]
        [hiccup.page]))

(defn footer []
  [:footer "Copyright (c) 2016 Daehyun Kim"])

(defn layout [content]
  (html5 {:lang "ko"}
         [:head
          (include-css "/css/hareguu.css")]
         [:body
          [:main content]
          (footer)
          (include-js "/js/main.js")]))

(defn markdown-div [content]
  [:div {:data-markdown true} content])

(defn cdata [content]
  (str "<![CDATA[\n" content "\n]]>"))
