(ns openlibrary.core
  (:require
   [openlibrary.disk-cache :as cache]
   [openlibrary.util :as util]
   [openlibrary.search :as s]))

(defn -main [& args]
  (->
   (Runtime/getRuntime)
   (.addShutdownHook
    (Thread. 
     (fn [& _]
       (util/log "Writing cache to disk...")
       (spit "cache.edn" @cache/store)
       (util/log "Exitting.")))))
  
  (util/log "Starting.")
  
  (let [cache (clojure.edn/read-string (slurp "cache.edn"))]
    (reset! cache/store (or cache {})))
  
  (println (s/search :isbn (first args)))
)