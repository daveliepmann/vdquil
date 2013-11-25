(ns vdquil.util
  (:use quil.core))

;; Color conversion function, courtesy of Jack Rusher
(defn hex-to-rgb [hex]
  (map (comp #(Integer/parseInt % 16) (partial apply str))
       (partition 2 (.replace hex "#" ""))))

(defn hex-to-color [hex]
  (apply color (hex-to-rgb hex)))
