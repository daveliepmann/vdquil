;; Ben Fry's Visualizing Data, Chapter 4 (Time Series), figure 10:
;; Time series with user-selected highlight
;; Converted from Processing to Quil as an exercise by Dave Liepmann
;; I think the usability of the mouse interaction, as coded, is sub-optimal.

(ns vdquil.chapter4.figure10
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter4.ch4data)
  (require [clojure.set :refer [union]]))

(def current-column (atom 1))

(def WIDTH 720)
(def HEIGHT 405)

(def plotx1 120)
(def plotx2 (- WIDTH 80))
(def ploty1 60)
(def ploty2 (- HEIGHT 70))

(def year-min (apply min (map first (rest milk-tea-coffee-data)))) 
(def year-max (apply max (map first (rest milk-tea-coffee-data))))
(def data-min (apply min (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))))
(def year-interval 10)
(def volume-interval 5)
(def data-max (* volume-interval (ceil (/ (apply max (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))) volume-interval))))
(def data-first 0)

(defn setup [])

(defn draw-plot-area []
  (background 224)
  (smooth)
  ;; Show the plot area as a white box
  (fill 255)
  (no-stroke)
  (rect-mode :corners)
  (rect plotx1 ploty1 plotx2 ploty2))

(defn draw-title []
  (fill 0)
  (text-size 20)
  (text-align :left :baseline)
  (text-font (create-font "Sans-Serif" 20))
  (text (nth (first milk-tea-coffee-data) @current-column) plotx1 (- ploty1 10)))

(defn annotate-x-axis []
  ;; Draw year labels
  (text-size 10)
  (text-align :center :top)
  (stroke-weight 1)
  ;; Use thin, gray lines to draw the grid
  (stroke 224)
  (doseq [year (range year-min year-max year-interval)]
    (let [x (map-range year year-min year-max plotx1 plotx2)]
      (text (str year) x (+ 10 ploty2))
      (line x ploty1 x ploty2))))

(defn annotate-y-axis []
  ;; Draw volume labels
  ;; (Since we're not drawing the minor ticks, we would ideally
  ;; increase volume-interval to 10 and remove the modulo-10 check.
  ;; We keep it in to show how to produce figure 5.
  (text-align :right :center)
  (doseq [volume (range data-first (+ 1 data-max) volume-interval)]
    (let [y (map-range volume data-first data-max ploty2 ploty1)]
      ;; Commented out--the minor tick marks are too visually distracting
      ;; (stroke 128)
      ;; (line plotx1 y (- plotx1 2) y) ;; Draw minor tick
      (if (= 0 (mod volume 10)) ;; Draw major tick mark
        (do
          (stroke 0)
          (line plotx1 y (- plotx1 4) y)
          (text-align :right :center) ;; Center vertically
          (if (= volume data-first) (text-align :right :bottom)) ;; Align the "0" label by the bottom
          (text (str (ceil volume)) (- plotx1 10) y))))))

(defn draw-axis-labels []
  ;; Draw axis labels
  (text-size 13)
  (text-leading 15)
  (text-align :center :center)
  (text "Gallons\nconsumer\nper capita" 50 (/ (+ ploty1 ploty2) 2))
  (text (str (first (first milk-tea-coffee-data))) (/ (+ plotx1 plotx2) 2) (- (height) 25)))

(defn draw-data-point [row]
  (stroke (apply color (hex-to-rgb "#5679C1")))
  (let [[year milk tea coffee] row
        x (map-range year year-min year-max plotx1 plotx2)
        y (map-range (nth row @current-column) data-min data-max ploty2 ploty1)]
    (vertex x y)
    (if (< (dist (mouse-x) (mouse-y) x y) 3)
      (do (stroke-weight 10)
          (point x y)
          (fill 0)
          (text-size 10)
          (text-align :center)
          (text (str (format "%.2f" (double (nth row @current-column))) " (" (first row) ")")  x (- y 8))))
    ;; we must restore the line's stroke-weight and lack of fill due to a bug which occurs
    ;; when rolling over the last data point
    (stroke-weight 2)
    (no-fill)))

(defn draw []
  (draw-plot-area)
  (draw-title)
  (annotate-x-axis)
  (annotate-y-axis)
  (draw-axis-labels)
  (begin-shape)
  (loop [rows (rest milk-tea-coffee-data)]
    (if (seq rows)
      (do (draw-data-point (first rows))
          (recur (rest rows)))))
  (end-shape))

(defn switch-data-set []
  (let [max-modulo (count (first milk-tea-coffee-data))]
    (if (= (str (raw-key)) "[")
      (do (swap! current-column dec)
          (reset! current-column (mod @current-column max-modulo))
          (compare-and-set! current-column 0 (- max-modulo 1)))
      (if (= (str (raw-key)) "]")
        (do (swap! current-column inc)
            (reset! current-column (mod @current-column max-modulo))
            (compare-and-set! current-column 0 1))))))

(defsketch mtc
  :title "Milk, Tea, Coffee"
  :setup setup
  :draw draw
  :size [WIDTH HEIGHT]
  :key-pressed switch-data-set)
