

;;TODO find an idiomatic way to implement this tab UI. As is, I'd just be cramming an imperative approach into a functional language. Maybe use Swing directly? Or Seesaw to clojurize Swing?

;; Ben Fry's Visualizing Data, Chapter 4 (Time Series), figure 15:
;; Clickable tabs
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter4.figure15
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter4.ch4data)
  (require [seesaw.core :as seesaw])
  (require [clojure.set :refer [union]]))

(def current-column (atom 1))

(def time-series-width 720)
(def time-series-height 405)

(def plotx1 120)
(def plotx2 (- time-series-width 80))
(def ploty1 60)
(def ploty2 (- time-series-height 70))

(def year-min (apply min (map first (rest milk-tea-coffee-data)))) 
(def year-max (apply max (map first (rest milk-tea-coffee-data))))
(def data-min (apply min (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))))
(def year-interval 10)
(def volume-interval 10)
(def data-max (* volume-interval (ceil (/ (apply max (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))) volume-interval))))
(def data-first 0)

(def tab-padding 10)

(defn tab-x-starts []
  (atom  (0 0 0)))
(defn tab-x-ends []
  (atom  (0 0 0)))
(def tab-y-start ploty1)
(def tab-y-end (- ploty1 40))

(defn setup []
  (seesaw/native!))

(defn draw-plot-area []
  (background 224)
  (smooth)
  ;; Show the plot area as a white box
  (fill 255)
  (no-stroke)
  (rect-mode :corners)
  (rect plotx1 ploty1 plotx2 ploty2))

(defn draw-title-tabs []
  (text-size 20)
  (text-align :left :baseline)
  (text-font (create-font "Sans-Serif" 20))
  (doseq [column (range 1 (count (first milk-tea-coffee-data)))]
    (let [column-title (nth (first milk-tea-coffee-data) column)
          tab-x-start (+ 1
                         plotx1
                         (* 2 (- column 1) tab-padding)
                         (reduce + 0 (map #(text-width (nth (first milk-tea-coffee-data) %)) (range 1 column))))
          tab-x-end (+ tab-x-start (* 2 tab-padding) (text-width column-title))]
      (reset! (nth tab-x-starts column) tab-x-start)
      (reset! (nth tab-x-ends column) tab-x-end)
      (if (= column @current-column)
        (fill 255)
        (fill 224))
      (rect tab-x-start tab-y-start tab-x-end tab-y-end)
      (if (= column @current-column)
        (fill 0)
        (fill 64))
      (text column-title (+ tab-x-start tab-padding) (- ploty1 10)))))

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
  (text-align :right :center)
  (doseq [volume (range data-first (+ 1 data-max) volume-interval)]
    (let [y (map-range volume data-first data-max ploty2 ploty1)]
      ;; Draw major tick mark
      (do
        (stroke 0)
        (line plotx1 y (- plotx1 4) y)
        (text-align :right :center) ;; Center vertically
        (if (= volume data-first) (text-align :right :bottom)) ;; Align the "0" label by the bottom
        (text (str (ceil volume)) (- plotx1 10) y)))))

(defn draw-axis-labels []
  ;; Draw axis labels
  (text-size 13)
  (text-leading 15)
  (text-align :center :center)
  (text "Gallons\nconsumer\nper capita" 50 (/ (+ ploty1 ploty2) 2))
  (text (str (first (first milk-tea-coffee-data))) (/ (+ plotx1 plotx2) 2) (- (height) 25)))

(defn draw-data-point-area [row]
  (stroke-weight 2)
  (no-stroke)
  (fill (apply color (hex-to-rgb "#5679C1")))
  (let [[year milk tea coffee] row
        x (map-range year year-min year-max plotx1 plotx2)
        y (map-range (nth row @current-column) data-min data-max ploty2 ploty1)]
    (vertex x y)))

(defn draw []
  (draw-plot-area)
  (draw-title-tabs)
  (annotate-x-axis)
  (annotate-y-axis)
  (draw-axis-labels)
  (begin-shape)
  (loop [rows (rest milk-tea-coffee-data)]
    (if (seq rows)
      (do (draw-data-point-area (first rows))
          (recur (rest rows)))))
  (vertex plotx2 ploty2)
  (vertex plotx1 ploty2)
  (end-shape))

(defn set-column []
  (for (range )
    (if (string? (first columns))
      (do (if (< (mouse-x) (nth tab-x-starts ))))
      (recur (rest columns)))))

(defsketch mtc
  :title "Milk, Tea, Coffee"
  :setup setup
  :draw draw
  :size [time-series-width time-series-height]
  :mouse-clicked set-column)

;(sketch-close mtc)

