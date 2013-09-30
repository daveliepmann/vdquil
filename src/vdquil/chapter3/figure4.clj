;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figures 3 and 4:
;; Varying data by color (with and without better color choices)
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure4
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
          radius 15
          percent (norm random-value
                        (apply min (map second random-data))
                        (apply max (map second random-data)))
          ;; ruddy green -- to produce figure 3 instead, use #FF4422
          low-color (hex-to-rgb "#296F34") 
          ;; light blue -- to produce figure 3 instead, use #4422CC
          high-color (hex-to-rgb "#61E2F0")]
      (smooth)
      (no-stroke)
      (fill (lerp-color (apply color low-color) (apply color high-color) percent))
      (ellipse x y radius radius))))

(defsketch ch3_map
  :title "Map"
  :setup setup
  :draw draw
  :size [640,400])
