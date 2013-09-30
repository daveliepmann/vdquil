;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figures 1 and 2:
;; U.S. map and centers of states / Varying data by size
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure2
  (:use quil.core)
  (:use vdquil.chapter3.ch3data)
  (:use vdquil.util))

(defn setup []
  (background 255)
  (set-state!
   :img (load-image "map.png")))

(defn draw []
  (image (state :img) 0 0)  
  (doseq [location location-data]    
    (let [[abbrev [x y]] location
          random-value (random-data abbrev)
          ;; to produce figure 1, use "radius 9" instead of the following 3 lines
          radius (if (>= random-value 0)
                   (map-range random-value 0 (apply max (map second random-data)) 2 40)
                   (map-range random-value 0 (apply min (map second random-data)) 2 40))]
      (smooth)
      (no-stroke)
      (fill 192 0 0)  
      (ellipse x y radius radius))))

(defsketch ch3_map
  :title "Map"
  :setup setup
  :draw draw
  :size [640,400])
