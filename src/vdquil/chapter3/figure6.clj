;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figure 6:
;; Magnitude and positive/negative
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure6
  (:use [quil.core]
        [vdquil.chapter3.ch3data]
        [vdquil.util]))

(defn setup []
  (background 255)
  (smooth)
  (no-stroke)
  (set-state! :img (load-image "resources/ch3/map.png"))
  (fill 192 0 0))

(defn create-ellipse [location]
  (let [[abbrev [x y]] location
        random-value (random-data abbrev)
        radius (if (>= random-value 0)
                 (map-range random-value 0 (apply max (map second random-data)) 3 30)
                 (map-range random-value 0 (apply min (map second random-data)) 3 30))
        low-color (hex-to-color "#333366")
        high-color (hex-to-color "#EC5166")]
    (if (>= random-value 0)
      (fill low-color)
      (fill high-color))
    (ellipse x y radius radius)))

(defn draw []
  (image (state :img) 0 0)
  (doseq [row location-data] 
    (create-ellipse row)))

(defsketch ch3_map
  :title "Map"
  :setup setup
  :draw draw
  :size [640 400])
