;; Ben Fry's Visualizing Data, Chapter 6, figure 6-2
;; Selecting a region of zip codes
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter6.figure6-2
  (:use [quil.core]
        [vdquil.util])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def canvas-height 453)
(def canvas-width 720)

(defn setup []
  (text-font (load-font "ScalaSans-Regular-14.vlw"))
  (text-mode :screen))

;; First line of the data is record count / minX / maxX / minY / maxY
;; I just manually pulled it in--let's not overcomplicate things.
;; # 41556,-0.3667764,0.35192886,0.4181981,0.87044954

;; From the first line of the original zips.tsv:
(def min-longitude -0.3667764)
(def min-latitude 0.4181981)
(def max-longitude 0.35192886)
(def max-latitude 0.87044954)

(def typed-chars (atom ""))

;; TODO pull in performance improvements from zoomable version
(defn draw []
  (smooth)
  (background (hex-to-color "#333333"))
  (with-open [zips (io/reader "data/zips-modified.tsv")]
    (doseq [line (line-seq zips)]
      (let [l (string/split line #"\t")
            clr (if (= 0 (count @typed-chars))
                  "#999966" ;; dormant color, since we have selected no zips
                  (if (= (subs (first l) 0 (count @typed-chars)) @typed-chars)
                    "#CBCBCB" ;; highlight color, since this matches what the user entered
                    "#66664C"))] ;; unhighlight color, since this point is not selected 
        (set-pixel (map-range (read-string (second l))
                              min-longitude max-longitude
                              30 (- canvas-width 30))
                   (map-range (read-string (nth l 2))
                              min-latitude max-latitude (- canvas-height 20) 20)
                   (hex-to-color clr)))))
  ;; solicit user input
  (fill (hex-to-color "#CBCBCB"))
  (if (= @typed-chars "")
    (text "type the digits of a zip code" 40 (- (height) 40))
    (text @typed-chars 40 (- (height) 40))))

(def deletion-key? #{\backspace}) ;; TODO test this key on a Windows machine
(def number-key? (set (map char (range 48 58))))

(defn key-handler
  "Manage keyboard input of 0 to 5 digits"
  []
  (let [key (if (= processing.core.PConstants/CODED (int (raw-key)))
              (key-code) (raw-key))] 
    (cond (and (deletion-key? key) (> (count @typed-chars) 0))
          (swap! typed-chars #(apply str (butlast %)))
          (and (number-key? key) (< (count @typed-chars) 5))
          (swap! typed-chars #(str % key)))))

(defsketch zips
  :title "Figure 6-2: selecting a region of zip codes"
  :setup setup
  :draw draw
  :size [canvas-width canvas-height]
  :renderer :p2d
  :key-pressed key-handler)
