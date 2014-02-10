;; Ben Fry's Visualizing Data, Chapter 7, improved treemap example
;; Improve upon the book's "simple treemap example" by adding a stop list
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; project.clj's :java-source-paths gives us access to treemap
;; Treemap package grabbed from http://benfry.com/writing/treemap/library.zip
(ns vdquil.chapter7.improved-treemap-example
  (:use quil.core)
  (:import treemap.SimpleMapItem treemap.Treemap treemap.SimpleMapModel)
  (:require [clojure.string :as string]))

(def words (string/split-lines (slurp "data/equator.txt")))

;; Stop list, so we can ignore commonly-used words
(def ignore
  (set (string/split-lines
        (slurp "http://jmlr.org/papers/volume5/lewis04a/a11-smart-stop-list/english.stop"))))

(defn word-item [[word freq]]
  (doto (proxy [SimpleMapItem] []
          (draw []
            (fill 255)
            (rect (.x this) (.y this) (.w this) (.h this))
            (when (and (> (.w this) (+ 6 (text-width word)))
                       (> (.w this) (+ 6 (text-ascent))))
              (text-align :center :center)
              (fill 0)
              (text word
                    (+ (.x this) (/ (.w this) 2))
                    (+ (.y this) (/ (.h this) 2))))))
    (.setSize freq)))

(defn setup []
  (smooth)
  (stroke-weight 0.1)
  (text-font (create-font "Serif" 13))
  (no-loop))

(defn draw []
  (background 255)
  (let [map-data (doto (SimpleMapModel.)
                   (.setItems (into-array treemap.SimpleMapItem
                                          (map word-item (frequencies (remove ignore words))))))]
    (.draw (Treemap. map-data 0 0 (width) (height)))))

;; Also, consider playing with (frequencies (remove ignore words))
;; further with something like:
;; (second (second (group-by (comp (partial < 15) second)
;;                           (frequencies (remove ignore words)))))

(defsketch simple-treemap-example
  :title "Figure 7-2. Treemap depicting word usage in Mark Twain's 'Following the Equator'"
  :draw draw
  :setup setup
  :size [1024 768])
