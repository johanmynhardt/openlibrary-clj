(ns openlibrary.core
  (:require
   [openlibrary.disk-cache :as cache]
   [openlibrary.util :as util]
   [openlibrary.search :as s]))

(def shutdown-hooks (atom {}))

(defn register-shutdown-hook [hook-key hook-fn]
  (swap! shutdown-hooks assoc hook-key hook-fn))

(defn setup-hooks []
  (.addShutdownHook
   (Runtime/getRuntime)
   (Thread.
    (fn []
      (doseq [[hook-key hook-fn] @shutdown-hooks]
        (util/log "Running shutdown hook:" hook-key)
        (hook-fn))
      (util/log "Exitting.")))))

(defn -main [& args]
  (util/log "Starting...")
  (register-shutdown-hook :cache-persist cache/shutdown-hook)
  (setup-hooks)
  
  (cache/initialize)
  
  (let [searches
        (->>
         args
         (map (fn [isbn] [isbn (s/search :isbn isbn)]))
         (into {}))]
    (println
     "Results" \newline
     "-----------" \newline 
     (with-out-str (clojure.pprint/pprint searches)))))