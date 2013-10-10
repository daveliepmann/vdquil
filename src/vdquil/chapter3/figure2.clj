;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figures 1 and 2:
;; U.S. map and centers of states / Varying data by size
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure2
  (:use quil.core)
  (:use vdquil.chapter3.ch3data)
  (:use vdquil.util))

(defn setup []
  (background 255)
  (smooth)
  (no-stroke)
  (set-state! :img (load-image "resources/ch3/map.png"))
  (fill 192 0 0))

(defn create-ellipse [location]
  (let [[abbrev [x y]] location
        random-value (random-data abbrev)
        ;; to produce figure 1, use "radius 9" instead of the following 3 lines
        radius (if (>= random-value 0)
                 (map-range random-value 0 (apply max (map second random-data)) 2 40)
                 (map-range random-value 0 (apply min (map second random-data)) 2 40))]
    (ellipse x y radius radius)))

(defn draw []
  (image (state :img) 0 0)
  (loop [rows location-data]
    (if (seq rows)
      (do (create-ellipse (first rows))
          (recur (rest rows))))))

(defsketch ch3-fig2
  :title "Map"
  :setup setup
  :draw draw
  :size [640 400])
