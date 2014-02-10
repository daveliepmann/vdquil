;; Ben Fry's Visualizing Data, Chapter 7, figure 7-3
;; Visualize a filesystem as a treemap
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; project.clj's :java-source-paths gives us access to treemap
;; Treemap package grabbed from http://benfry.com/writing/treemap/library.zip
(ns vdquil.chapter7.fig3-subfolder-treemap
  (:use quil.core)
  (:import treemap.SimpleMapItem treemap.Treemap treemap.SimpleMapModel)
  (:require [clojure.string :as string]))

(def root (clojure.java.io/file "/Users/daveliepmann/src/vdquil"))

;; I found it handy to preview what I'm going to map:
;; (doseq [l (.listFiles root)]
;;   (println [(.getName l) (.length l)]))

;; ...this gets used in place of (.listFiles root) when mapping file-item to that sexp.
;; (def dummy [["_CodeSignature" 102]
;;             ["Info.plist" 3710]
;;             ["MacOS" 102]
;;             ["PkgInfo" 8]
;;             ["Resources" 170]])

;; I repurposed word-item from simple-treemap-example instead of
;; building an entirely new file-item and folder-item.
(defn file-item
  "Returns a `file-item` object extending `SimpleMapItem`."
  [file]
  (doto (proxy [SimpleMapItem] []
          (draw []
            (fill 255)
            (rect (.x this) (.y this) (.w this) (.h this))
            (text-align :center :center)
            (fill 0)
            (let [name (.getName file)]
              (when (and (> (.w this) (+ 6 (text-width name)))
                         (> (.w this) (+ 6 (text-ascent))))
                (text-align :center :center)
                (fill 0)
                (text name
                      (+ (.x this) (/ (.w this) 2))
                      (+ (.y this) (/ (.h this) 2)))))))
    (.setSize (.length file))))

(defn setup []
  (smooth)
  (stroke-weight 0.1)
  (text-font (create-font "Sans-Serif" 13))
  (no-loop))

(defn draw []
  (background 255)
  (let [folder-map (doto (SimpleMapModel.)
                     (.setItems (into-array treemap.SimpleMapItem
                                            (map file-item (.listFiles root)))))]
    (.draw (Treemap. folder-map 0 0 (width) (height)))))

(defsketch fig7-3
  :title "Figure 7-3. Treemap depicting relative sizes of subfolders"
  :draw draw
  :setup setup
  :size [1024 768])
