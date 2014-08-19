(ns vdquil.chapter8.fig2.edge)

(defrecord Edge [from to len])

(defn make-edge
  [from-node to-node]
  (->Edge from-node to-node 50))



