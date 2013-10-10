;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), mouse rollover
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.ch3.mouse
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter3.ch3data))

(def data-min (apply min (map second random-data)))
(def data-max (apply max (map second random-data)))

(def closest-distance (atom (. Integer MAX_VALUE)))
(def closest-text (atom ""))
(def closest-x (atom 0))
(def closest-y (atom 0))

(defn setup []
  (background 255)
  (text-align :center)
  (smooth)
  (no-stroke)
  (set-state! :img (load-image "resources/ch3/map.png")))

(defn create-ellipse [location]
  (let [[abbrev [x y]] location
        random-value (random-data abbrev)
        radius (if (>= random-value 0)
                 (map-range random-value 0 (apply max (map second random-data)) 1.5 15)
                 (map-range random-value 0 (apply min (map second random-data)) 1.5 15))
        ellipse-color (if (>= random-value 0)
                        (hex-to-rgb "#4422cc") ;; blue
                        (hex-to-rgb "#ff4422")) ;; red -- use emacs with rainbow-mode and you won't need these 2 comments!
        d (dist x y (mouse-x) (mouse-y))]
    (apply fill ellipse-color)
    (ellipse x y radius radius)    
    ;; Because the following check is done each time a new circle is drawn,
    ;; we end up with the values of the circle closest to the mouse.
    (if (< d (+ 2 radius))
      (if (< d @closest-distance)
        (do (reset! closest-distance d)
            (reset! closest-text (str (name-data abbrev) " " random-value))
            (reset! closest-x x)
            (reset! closest-y (- y (+ radius 4))))))))

(defn draw []
  (image (state :img) 0 0)
  (loop [rows location-data]
    (if (seq rows)
      (do (create-ellipse (first rows))
          (recur (rest rows)))))
  ;; Use the values set inside create-ellipse to draw text related to the closest circle:
  (if (< @closest-distance (. Integer MAX_VALUE))
    (do (fill 0)
        (text @closest-text
              @closest-x
              @closest-y)))
  (reset! closest-distance (. Integer MAX_VALUE)))

(defsketch ch3_map
  :title "Map"
  :setup setup
  :draw draw
  :size [640 400])
