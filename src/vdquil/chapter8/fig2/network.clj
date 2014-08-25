(ns vdquil.chapter8.fig2.network
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [vdquil.chapter8.fig2.node :as n]
            [vdquil.chapter8.fig2.edge :as e]
            [vdquil.chapter8.fig2.params :as p]))

;; NOTE: This uses Quil's "functional mode", in which a state variable is
;; passed between the top-level procedures setup, update, and draw.

(defn add-nodes-and-edge
  "Given two strings from-label and two-label, and a pair of
  sets representing nodes and edges, return a new pair of nodes
  and edges with zero or more new nodes and zero or one new edges."
  [from-label to-label [nodes edges]]
  (let [from-node (n/make-node from-label)
        to-node   (n/make-node to-label)]
    [(-> nodes (conj from-node) (conj to-node))      ; add nodes if new
     (conj edges (e/make-edge from-node to-node))])) ; add edge if new

;; first collect the complete sets of nodes and edges, and then
;; use them to def top-level symbols.
(defn init-graph []
  (let [[nodes edges] (->> [#{} #{}]
                      (add-nodes-and-edge "joe" "food")
                      (add-nodes-and-edge "joe" "dog")
                      (add-nodes-and-edge "joe" "tea")
                      (add-nodes-and-edge "joe" "cat")
                      (add-nodes-and-edge "joe" "table")
                      (add-nodes-and-edge "table" "plate")
                      (add-nodes-and-edge "plate" "food")
                      (add-nodes-and-edge "food" "mouse")
                      (add-nodes-and-edge "food" "dog")
                      (add-nodes-and-edge "food" "dog")
                      (add-nodes-and-edge "mouse" "cat")
                      (add-nodes-and-edge "table" "cup")
                      (add-nodes-and-edge "cup" "tea")
                      (add-nodes-and-edge "dog" "cat")
                      (add-nodes-and-edge "cup" "spoon")
                      (add-nodes-and-edge "plate" "fork")
                      (add-nodes-and-edge "dog" "flea1")
                      (add-nodes-and-edge "dog" "flea2")
                      (add-nodes-and-edge "flea1" "flea2")
                      (add-nodes-and-edge "plate" "knife"))]
    {:nodes nodes :edges edges}))

(defn setup []
  (q/text-font (q/create-font "SansSerif" 10))
  (q/smooth)
  (init-graph)) ; passed to update as state

(defn update [state]
  (let [nodes (:nodes state)
        edges (:edges state)]
    {:nodes (map n/update (map (partial n/relax nodes) nodes))
     :edges (map e/relax edges)}))

(defn draw [state]
  (q/background 255) ; white
  (let [nodes (:nodes state)
        edges (:edges state)]
  (doseq [edge edges] (e/draw edge))
  (doseq [node nodes] (n/draw node)))


(q/defsketch ch8-fig2
  :title "Graph layout example"
  :size [p/+width+ p/+height+]
  :setup setup
  :draw draw
  :update update
  :middleware [m/fun-mode])
