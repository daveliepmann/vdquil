(ns vdquil.chapter8.fig2.edge
  (:require [clojure.set :as st]
            [quil.core :as q]))


;; len never changes in the original code, nor here
(defrecord Edge [from to len])

(defn make-edge
  "from and to are indexes or keys that can be used to select nodes."
  [from to]
  (->Edge from to 50))


;; This doesn't alter edges, in this version, but I place it here to maintain
;; some rough parallelism with edge.pde.  It constructs updated nodes.
(defn relax-edge-endnodes
  "Given an edge, selects its endpoint nodes from the nodes passed in,
  and returns a set containing updated versions of those two nodes."
  [edge nodes-map]
  (let [from (nodes-map (:from edge))
        to (nodes-map (:to edge))
        len (:len edge)
        vx (- (:x to) (:x from))
        vy (- (:y to) (:y from))
        d (q/mag vx vy)]
    (if (<= d 0)
      nodes-map
      (let [f (/ (- len d) (* d 3))
            dx (* f vx)
            dy (* f vy)
               new-from (assoc from    ; BUG: This should be the same as what's in nodes, but it's not, now.
                            :dx (- (:dx from) dx)
                            :dy (- (:dy from) dy))
               new-to   (assoc to 
                            :dx (+ (:dx to) dx)
                            :dy (+ (:dy to) dy))]
        (assoc nodes-map 
               (:label new-from) new-from 
               (:label new-to) new-to)))))

(defn relax-all-endnodes
  "Applies relax-edge-endnotes to each edge, generating updated versions of
  its endpoint nodes, which then replace the original pair of nodes in the
  set of all nodes that this function constructs from the original set
  of nodes that is passed to this function.  Note:
  edges must be sequential?.  nodes must be a set."
  [edges nodes-map]
  (let [next-edges (next edges)
        new-nodes-map (relax-edge-endnodes (first edges) nodes-map)]
    (if next-edges
      (recur next-edges new-nodes-map)
      new-nodes-map)))


(defn draw
  [edge nodes-map]
  (q/stroke 0)
  (q/stroke-weight 0.35)
  (let [from (nodes-map (:from edge))
        to (nodes-map (:to edge))]
    (q/line (:x from)
            (:y from)
            (:x to)
            (:y to))))
