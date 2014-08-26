(ns vdquil.chapter8.fig2.graph
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
  [from-label to-label [nodes-map edges]]
  (let [from-node (n/make-node from-label)
        to-node   (n/make-node to-label)]
    [(assoc nodes-map from-label from-node to-label to-node) ; this might replace a node with a functionally identical node (Feed the gc!)
     (conj edges (e/make-edge from-label to-label))]))   ; add edge, which is assumed new

;; first collect the complete sets of nodes and edges, and then
;; use them to def top-level symbols.
(defn init-graph []
  (let [[nodes-map edges] (->> [{} []]
                      (add-nodes-and-edge "joe" "food")
                      (add-nodes-and-edge "joe" "dog")
                      (add-nodes-and-edge "joe" "tea")
                      (add-nodes-and-edge "joe" "cat")
                      (add-nodes-and-edge "joe" "table")
                      (add-nodes-and-edge "table" "plate")
                      (add-nodes-and-edge "plate" "food")
                      (add-nodes-and-edge "food" "mouse")
                      (add-nodes-and-edge "food" "dog") ; there is a duplicate edge in the Java source, but not here
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
    {:nodes-map nodes-map :edges edges}))

(defn setup []
  (q/text-font (q/create-font "SansSerif" 14))
  (q/smooth)
  (init-graph)) ; passed to update as state


(defn update [state]
  (let [nodes-map (:nodes-map state)
        edges (:edges state)] ; edges never changes, but we pass it around anyway
    (assoc state 
           :nodes-map (apply merge 
                             (map n/update 
                                  (map (partial n/relax nodes-map) 
                                       (vals 
                                         (e/relax-all-endnodes edges nodes-map))))))))

(defn draw [state]
  (q/background 200)
  (let [nodes-map (:nodes-map state)
        edges (:edges state)]
    (doseq [edge edges] (e/draw edge nodes-map))
    (doseq [node (vals nodes-map)] 
      (n/draw node))))


(q/defsketch ch8-fig2
  :title "Graph layout example"
  :size [p/+width+ p/+height+]
  :setup setup
  :draw draw
  :update update
  :middleware [m/fun-mode])
