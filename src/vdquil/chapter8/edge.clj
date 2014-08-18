(ns vdquil.chapter8.edge)

(defrecord Edge [from to len cnt])

(defn make-edge
  [from-node to-node]
  (->Edge from-node to-node 50 0))



