;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), figures 3, 4, and 5:
;; Varying data by color (with and without better color choices and a better color space)
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.chapter3.figure5
  (:use [quil.core]
        [vdquil.chapter3.ch3data]
        [vdquil.util]))

(defn setup []
  (background 255)
  (smooth)
  (no-stroke)
  (set-state! :img (load-image "resources/ch3/map.png"))
  (fill 192 0 0)
  ;; to produce figure 3 or 4, comment out the following line:
  (color-mode :hsb))

(defn create-ellipse [[abbrev [x y]]]
  (let [random-val (random-data abbrev)
        radius     15
        percent    (norm random-val
                         (apply min (map second random-data))
                         (apply max (map second random-data)))
        ;; ruddy green -- to produce figure 3, use #FF4422
        low-color  (hex-to-color "#296F34") 
        ;; light blue -- to produce figure 3, use #4422CC
        high-color (hex-to-color "#61E2F0")]
    (fill (lerp-color low-color high-color percent))
    (ellipse x y radius radius)))

(defn draw []
  (image (state :img) 0 0)
  (doseq [row location-data] 
    (create-ellipse row)))

(defsketch ch3_map
  :title "Map displaying data using size and playing with color choices"
  :setup setup
  :draw draw
  :size [640 400])
