(ns vdquil.chapter8.fig2.network
  (:require [quil.core :as q]
            [vdquil.chapter8.fig2.node :as n]
            [vdquil.chapter8.fig2.edge :as e]))

(defn add-edge
  [from-label to-label nodes]
  (conj nodes [(n/make-node from-label) (n/make-node to-label)]))

(def edges 
  (->> #{}
       (add-edge "joe" "food")
       (add-edge "joe" "dog")
       (add-edge "joe" "tea")
       (add-edge "joe" "cat")
       (add-edge "joe" "table")
       (add-edge "table" "plate")
       (add-edge "plate" "food")
       (add-edge "food" "mouse")
       (add-edge "food" "dog")
       (add-edge "food" "dog")
       (add-edge "mouse" "cat")
       (add-edge "table" "cup")
       (add-edge "cup" "tea")
       (add-edge "dog" "cat")
       (add-edge "cup" "spoon")
       (add-edge "plate" "fork")
       (add-edge "dog" "flea1")
       (add-edge "dog" "flea2")
       (add-edge "flea1" "flea2")
       (add-edge "plate" "knife")))

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


