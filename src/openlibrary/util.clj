(ns openlibrary.util
  (:require
   [clojure.string :as str]))

(defn log [msg & more]
  (spit "log.dat" 
        (str (java.time.Instant/now) " - "
             (if more (apply str/join [" " (cons msg more)]) msg) \newline) :append true))