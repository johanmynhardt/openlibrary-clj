(ns openlibrary.disk-cache
  (:require
   [clojure.edn :as edn]
   [openlibrary.util :as util]))

(def store (atom {}))

(defn initialize []
  (util/log "Loading cache...")
  (let [cache (edn/read-string (slurp "cache.edn"))]
    (reset! store (or cache {}))))

(defn persist []
  (util/log "Writing cache to disk...")
  (spit "cache.edn" @store))

(defn shutdown-hook []
  (util/log "Running cache shutdown hook.")
  (persist))