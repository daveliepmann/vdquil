;; Ben Fry's Visualizing Data, Chapter 4 (Time Series), figure 9:
;; Combined dots and continuous line
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter4.figure9
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter4.ch4data)
  (require [clojure.set :refer [union]]))

(def current-column (atom 1))

(defn setup [])

(defn draw []
  (background 224)
  (smooth)
  
  ;; Show the plot area as a white box
  (fill 255)
  (no-stroke)
  (rect-mode :corners)
  ;; ... define corners of the plotted time series
  (let [plotx1 120
        plotx2 (- (width) 80)
        ploty1 60
        ploty2 (- (height) 70)
        year-min (apply min (map first (rest milk-tea-coffee-data))) 
        year-max (apply max (map first (rest milk-tea-coffee-data)))
        year-interval 10
        volume-interval 5
        data-min (apply min (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data)))))
        data-max (* volume-interval (ceil (/ (apply max (apply union (for [x (range 1 4)] (map #(nth % x) (rest milk-tea-coffee-data))))) volume-interval)))
        data-first 0]
    (rect plotx1 ploty1 plotx2 ploty2)

    ;; Draw title
    (fill 0)
    (text-size 20)
    (text-align :left :baseline)
    (text-font (create-font "Sans-Serif" 20))
    (text (nth (first milk-tea-coffee-data) @current-column) plotx1 (- ploty1 10))

    ;; Draw year labels
    (text-size 10)
    (text-align :center :top)
    (stroke-weight 1)
    ;; ...use thin, gray lines to draw the grid
    (stroke 224)
    (doseq [year (range year-min year-max year-interval)]
      (let [x (map-range year year-min year-max plotx1 plotx2)]
        (text (str year) x (+ 10 ploty2))
        (line x ploty1 x ploty2)))

    ;; Draw volume labels
    ;; (Since we're not drawing the minor ticks, we would ideally
    ;; increase volume-interval to 10 and remove the modulo-10 check.
    (text-align :right :center)
    (doseq [volume (range data-first data-max volume-interval)]
      (let [y (map-range volume data-first data-max ploty2 ploty1)]
        ;; Commented out; the minor tick marks are too visually distracting
        ;; (stroke 128)
        ;; (line plotx1 y (- plotx1 2) y) ;; Draw minor tick
        (if (= 0 (mod volume 10)) ;; Draw major tick mark
          (do
            (stroke 0)
            (line plotx1 y (- plotx1 4) y)
            (text-align :right :center) ;; Center vertically
            (if (= volume data-first) (text-align :right :bottom)) ;; Align the "0" label by the bottom
            (text (str (ceil volume)) (- plotx1 10) y)))))
    ;; Clojure's range function is exclusive on the upper bound,
    ;; so we have to manually append to the above loop
    (text-align :right :top) ;; Align the "50" by the top
    (stroke 0)
    (line plotx1 ploty1 (- plotx1 4) ploty1)
    (text (str data-max) (- plotx1 10) ploty1)

    ;; Draw axis labels
    (text-size 13)
    (text-leading 15)
    (text-align :center :center)
    (text "Gallons\nconsumer\nper capita" 50 (/ (+ ploty1 ploty2) 2))
    (text (str (first (first milk-tea-coffee-data))) (/ (+ plotx1 plotx2) 2) (- (height) 25))
    
    ;; Draw data points
    (stroke-weight 5)
    (stroke (apply color (hex-to-rgb "#5679C1")))
    (doseq [row (rest milk-tea-coffee-data)]
      (let [[year milk tea coffee] row]
        (point (map-range year year-min year-max plotx1 plotx2)
               (map-range (nth row @current-column) data-min data-max ploty2 ploty1))))

    ;; Draw data lines
    (stroke-weight 5)
    (stroke (apply color (hex-to-rgb "#5679C1")))
    (no-fill)
    (stroke-weight 0.5)
    (begin-shape)
    (doseq [row (rest milk-tea-coffee-data)]
      (let [[year milk tea coffee] row]
        (vertex (map-range year year-min year-max plotx1 plotx2)
                (map-range (nth row @current-column) data-min data-max ploty2 ploty1))))
    (end-shape)))

(defn switch-data-set []
  (if (= (str (raw-key)) "[")
    (do (swap! current-column inc)
        (if (>= @current-column (count (first (rest milk-tea-coffee-data))))
          (reset! current-column 1)))
    (if (= (str (raw-key)) "]")
      (do (swap! current-column dec)
          (if (= @current-column 0)
            (reset! current-column (- (count (first (rest milk-tea-coffee-data))) 1)))))))

(defsketch mtc
  :title "Milk, Tea, Coffee"
  :setup setup
  :draw draw
  :size [720, 405]
  :key-pressed switch-data-set)
