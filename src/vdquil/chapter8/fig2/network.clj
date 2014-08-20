(ns vdquil.chapter8.fig2.network
  (:require [quil.core :as q]
            [vdquil.chapter8.fig2.node :as n]
            [vdquil.chapter8.fig2.edge :as e]))

;(defn add-edge
;  [from-label to-label nodes]
;  (conj nodes [(n/make-node from-label) (n/make-node to-label)]))

(def edges 
  (into #{}
        [(e/make-edge "joe" "food")
         (e/make-edge "joe" "dog")
         (e/make-edge "joe" "tea")
         (e/make-edge "joe" "cat")
         (e/make-edge "joe" "table")
         (e/make-edge "table" "plate")
         (e/make-edge "plate" "food")
         (e/make-edge "food" "mouse")
         (e/make-edge "food" "dog")
         (e/make-edge "food" "dog")
         (e/make-edge "mouse" "cat")
         (e/make-edge "table" "cup")
         (e/make-edge "cup" "tea")
         (e/make-edge "dog" "cat")
         (e/make-edge "cup" "spoon")
         (e/make-edge "plate" "fork")
         (e/make-edge "dog" "flea1")
         (e/make-edge "dog" "flea2")
         (e/make-edge "flea1" "flea2")
         (e/make-edge "plate" "knife")]))

(defn setup []
  (q/text-font (q/create-font "SanSerif" 10))
  (q/smooth))

(defn draw []
  (q/background 255)
  ;; map relax over edges
  ;; map relax over nodes
  ;; map update over nodes
  ;; doseq draw over edges
  ;; doseq draw over nodes
  )

(q/defsketch ch8-fig2
  :title "Graph layout example"
  :setup setup
  :draw draw
  :size [600 600])


