;; Ben Fry's Visualizing Data, Chapter 4 (Time Series), figure 1:
;; One set of points over time
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter4.figure1
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter4.ch4data)
  (require [clojure.set :refer [union]]))

(def year-min (apply min (map first (rest milk-tea-coffee-data)))) 
(def year-max (apply max (map first (rest milk-tea-coffee-data))))
(def data-min (apply min (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))))
(def data-max (apply max (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))))

(defn setup []
  (background 224)
  (smooth))

(defn draw-plot-area []
  ;; Show the plot area as a white box
  (fill 255)
  (no-stroke)
  (rect-mode :corners)
  (rect 50 60 (- (width) 50) (- (height) 60)))

(defn draw-data-point [row]
  (let [[year milk tea coffee] row]
    (point (map-range year year-min year-max 50 (- (width) 50))
           (map-range milk data-min data-max (- (height) 60) 60))))

(defn draw []
  (draw-plot-area)
  (stroke-weight 5)
  (stroke (apply color (hex-to-rgb "#5679C1")))
  (loop [rows (rest milk-tea-coffee-data)]
    (if (seq rows)
      (do (draw-data-point (first rows))
          (recur (rest rows))))))

(defsketch mtc
  :title "Milk, Tea, Coffee"
  :setup setup
  :draw draw
  :size [720 400])
