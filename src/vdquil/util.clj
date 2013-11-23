(ns vdquil.util)

;; Color conversion function, courtesy of Jack Rusher
(defn hex-to-rgb [hex]
  (map (comp #(Integer/parseInt % 16) (partial apply str))
       (partition 2 (.replace hex "#" ""))))

;; Substitute for contains? for lists
(defn in? 
  "true if seq contains elm"
  [seq elm]  
  (some #(= elm %) seq))
