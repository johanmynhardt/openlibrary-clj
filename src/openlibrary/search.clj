(ns openlibrary.search
  (:require
   [clj-http.client :as http]
   [openlibrary.disk-cache :as cache]
   [openlibrary.util :as util]))

(def ol-search "http://openlibrary.org/search.json")

(defn search [by token & [{:keys [debug? use-cache?] :or {debug? false use-cache? true}}]]
  (util/log "Searching by" by "for" token)
  (let [cache-hit (when use-cache? (get @cache/store token))
        _ (println "cache-hit? " (boolean cache-hit))
        result
        (or
         cache-hit 
         (->
          (http/get
           ol-search
           {:debug? debug?
            :as :json
            :query-params
            {(keyword by) token}})
          :body :docs first)
          
         {:not-found :title-not-found})]
    (when-not cache-hit (swap! cache/store assoc token result))
    result))

(comment 
  (def x (atom {}))
  (swap! x assoc "2" :x)
  (search :isbn "1935182641" {:use-cache? false})
  (get @cache/store "9781847940667")
  (System/exit 0)
  (search :q "9781847940667")
  (apply (juxt :author_name :title) [(search :isbn "9780140043129")])
  )