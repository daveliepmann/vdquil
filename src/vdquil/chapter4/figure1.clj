;; Ben Fry's Visualizing Data, Chapter 4 (Time Series), figure 1:
;; One set of points over time
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter4.figure1
  (:use [quil.core]
        [vdquil.util]
        [vdquil.chapter4.ch4data]))

(def year-min (apply min (map first (rest milk-tea-coffee-data)))) 

(def year-max (apply max (map first (rest milk-tea-coffee-data))))

(def data-min (apply min (mapcat rest (rest milk-tea-coffee-data))))

(def data-max (apply max (mapcat rest (rest milk-tea-coffee-data))))

(defn setup []
  (smooth))

(defn draw-plot-area
  "Render the plot area as a white box"
  []
  (background 224)
  (fill 255)
  (no-stroke)
  (rect-mode :corners)
  (rect 50 60 (- (width) 50) (- (height) 60)))

(defn draw-data-point [[year milk tea coffee]]
  (point (map-range year year-min year-max 50 (- (width) 50))
         (map-range milk data-min data-max (- (height) 60) 60)))

(defn draw []
  (draw-plot-area)
  (stroke-weight 5)
  (stroke (hex-to-color "#5679C1"))
  (doseq [row (rest milk-tea-coffee-data)]
    (draw-data-point row)))

(defsketch mtc
  :title "Milk, Tea, Coffee"
  :setup setup
  :draw draw
  :size [720 400])
