;; Ben Fry's Visualizing Data, Chapter 3 (Data on a Map), mouse rollover
;; Converted from Processing to Quil as an exercise by Dave Liepmann

(ns vdquil.ch3.mouse
  (:use [quil.core]
        [vdquil.chapter3.ch3data]
        [vdquil.util]))

(def data-min (apply min (map second random-data)))
(def data-max (apply max (map second random-data)))

(defn setup []
  (background 255)
  (text-align :center)
  (smooth)
  (no-stroke)
  (set-state! :img (load-image "resources/ch3/map.png")))

(defn ell [location]
  (let [[abbr [x y]] location
        random-value (random-data abbr)
        radius       (if (>= random-value 0)
                       (map-range random-value 0
                                  (apply max (map second random-data)) 1.5 15)
                       (map-range random-value 0
                                  (apply min (map second random-data)) 1.5 15))
        ;; blue for positive, red for negative.
        ;; (use emacs with rainbow-mode and you won't need that comment!)
        ellipse-clr  (if (>= random-value 0) "#4422cc" "#ff4422")]
    {:abbrev abbr :x x :y y :val random-value :r radius :clr ellipse-clr}))

(defn draw []
  (image (state :img) 0 0)
  (let [ells     (map ell location-data)
        ellipses (map #(assoc % :d (dist (:x %) (:y %) (mouse-x) (mouse-y)))
                      ells)
        closest  (apply (partial min-key :d) ellipses)]
    (doseq [e ellipses]
      (fill (hex-to-color (:clr e)))
      (ellipse (:x e) (:y e) (:r e) (:r e)))
    ;; When rolling over an ellipse with the mouse, label it:
    (when (< (:d closest) (+ 2 (:r closest)))
      (fill (hex-to-color "#2d2d2d"))
      (text (str (name-data (:abbrev closest)) " " (:val closest))
            (:x closest) (- (:y closest) (+ 4 (:r closest)))))))

(defsketch ch3_map
  :title "Map"
  :setup setup
  :draw draw
  :size [640 400])
