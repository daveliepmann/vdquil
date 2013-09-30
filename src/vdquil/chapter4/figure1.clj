;; Ben Fry's Visualizing Data, Chapter 4 (Time Series), figure 1:
;; One set of points over time
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter4.figure1
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter4.ch4data)
  (require [clojure.set :refer [union]]))

(defn setup [])

(defn draw []
  (background 224)
  (smooth)
  
  ;; Show the plot area as a white box
  (fill 255)
  (no-stroke)
  (rect-mode :corners)
  (rect 50 60 (- (width) 50) (- (height) 60))

  ;; Draw data points
  (stroke-weight 5)
  (stroke (apply color (hex-to-rgb "#5679C1")))
  (doseq [row (rest milk-tea-coffee-data)]
    (let [[year milk tea coffee] row 
          year-min (apply min (map first (rest milk-tea-coffee-data))) 
          year-max (apply max (map first (rest milk-tea-coffee-data)))
          ;; data-min (apply min (union (map second (rest milk-tea-coffee-data)) (map #(nth % 2) (rest milk-tea-coffee-data)) (map #(nth % 3) (rest milk-tea-coffee-data))))
          data-min (apply min (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data)))))
          ;; data-max (apply max (union (map second (rest milk-tea-coffee-data)) (map #(nth % 2) (rest milk-tea-coffee-data)) (map #(nth % 3) (rest milk-tea-coffee-data))))
          data-max (apply max (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data)))))]
      (point (map-range year year-min year-max 50 (- (width) 50))
             (map-range milk data-min data-max (- (height) 60) 60)))))

(defsketch mtc
  :title "Milk, Tea, Coffee"
  :setup setup
  :draw draw
  :size [720, 400])
