;; Ben Fry's Visualizing Data, Chapter 6, figure 6-1
;; Geographic locations of postal zip codes
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter6.figure6-1
  (:use [quil.core]
        [vdquil.util])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def canvas-height 453)
(def canvas-width 720)

(defn setup [])

;; The first line is record count / minX / maxX / minY / maxY
;; I just manually pulled it in--let's not overcomplicate things.
;; # 41556,-0.3667764,0.35192886,0.4181981,0.87044954

;; From the first line of the original zips.tsv:
(def min-longitude -0.3667764)
(def min-latitude 0.4181981)
(def max-longitude 0.35192886)
(def max-latitude 0.87044954)

(defn draw []
  (background 255)
  (with-open [zips (io/reader "data/zips-modified.tsv")]
    (doseq [line (line-seq zips)]
      (let [l (string/split line #"\t")]
        (set-pixel (map-range (read-string (second l))
                              min-longitude max-longitude
                              30 (- canvas-width 30))
                   (map-range (read-string (nth l 2)) min-latitude max-latitude
                              (- canvas-height 20) 20)
                   (hex-to-color "#000000"))))))

(defsketch zips
  :title "Geographic locations of postal ZIP codes"
  :setup setup
  :draw draw
  :size [canvas-width canvas-height]
  :renderer :p2d)
