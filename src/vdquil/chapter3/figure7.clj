;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figure 7:
;; Magnitude and positive/negative
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure7
  (:use quil.core)
  (:use vdquil.util)
  (:use vdquil.chapter3.ch3data))

(defn setup []
  (background 255)
  (set-state!
   :img (load-image "map.png")))

(defn draw []
  (image (state :img) 0 0)  
  (doseq [location location-data]    
    (let [[abbrev [x y]] location
          random-value (random-data abbrev)
          radius (if (>= random-value 0)
                   (map-range random-value 0 (apply max (map second random-data)) 3 30)
                   (map-range random-value 0 (apply min (map second random-data)) 3 30))
          low-color (hex-to-rgb "#333366")
          high-color (hex-to-rgb "#EC5166")]
      (smooth)
      (no-stroke)
      (if (>= random-value 0)
        (fill (apply color low-color))
        (fill (apply color high-color)))
      (ellipse x y radius radius))))

(defsketch ch3_map
  :title "Map"
  :setup setup
  :draw draw
  :size [640,400])
