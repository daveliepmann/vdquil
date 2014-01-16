;; Ben Fry's Visualizing Data, Chapter 7, simple treemap example (p190)
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; project.clj's :java-source-paths gives us access to treemap
;; Treemap package grabbed from http://benfry.com/writing/treemap/library.zip
(ns vdquil.chapter7.simple-treemap-example
  (:use quil.core)
  (:import treemap.SimpleMapItem treemap.Treemap treemap.SimpleMapModel)
  (:require [clojure.string :as string]))

(def words (string/split-lines (slurp "data/equator.txt")))

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
                                          (map word-item (frequencies words)))))]
    (.draw (Treemap. map-data 0 0 (width) (height)))))

(defsketch simple-treemap-example
  :title "Figure 7-2. Treemap depicting word usage in Mark Twain's 'Following the Equator'"
  :draw draw
  :setup setup
  :size [1024 768])

