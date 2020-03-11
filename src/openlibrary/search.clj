(ns openlibrary.search
  (:require
   [clj-http.client :as http]))

(def ol-search "http://openlibrary.org/search.json")

(defn search [by token & [{:keys [debug?] :or {debug? false}}]]
  (-> 
   (http/get 
    ol-search
    {:debug? debug?
     :as :json
     :query-params
     {(keyword by) token}})
   :body :docs first))

(comment 
  (search :isbn "1935182641")
  (search :isbn "9780140043129")
  (apply (juxt :author_name :title) [(search :isbn "9780140043129")])
  )