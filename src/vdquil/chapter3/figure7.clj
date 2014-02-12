;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figure 7:
;; Magnitude and positive/negative using transparency
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure7
  (:use [quil.core]
        [vdquil.chapter3.ch3data]
        [vdquil.util]))

(def data-min (apply min (map second random-data)))

(def data-max (apply max (map second random-data)))

(defn setup []
  (background 255)
  (smooth)
  (no-stroke)
  (set-state! :img (load-image "resources/ch3/map.png"))
  (fill 192 0 0))

(defn create-ellipse [location]
  (let [[abbrev [x y]] location
        random-value (random-data abbrev)
        radius 15
        low-color (hex-to-color "#333366")
        high-color (hex-to-color "#EC5166")]
    (if (>= random-value 0)
      (fill low-color (map-range random-value 0 (abs data-min) 0 255))
      (fill high-color (map-range (abs random-value) 0 data-max 0 255)))
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
