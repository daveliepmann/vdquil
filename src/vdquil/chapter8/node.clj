(ns vdquil.chapter8.node
  (:require [quil.core :as q]
            [vdquil.chapter8.params :as p]))

(defrecord Node [label x y dx dy fixed cnt])

(defn make-node
  [label]
  (->Node label 
          (rand p/+width+) 
          (rand p/+height+) 
          0.0 0.0 true 0.0))

;; TODO I don't think this is necessary.  It just keeps a count of nodes.
;; That can be gotten directly from the node set.  (?)
(defn increment
  [node]
  (assoc node :cnt (inc (:cnt node))))

;; TODO
(defn relax
  [node nodes]
  (let [ddx 0
        ddy 0
        dlen (/ (q/mag ddx ddy) 2)
        f (fn [n]
            (if (= n node)
              (if (pos? dlen)
                (assoc n 
                       :dx (+ (:dx n) (/ ddx dlen))
                       :dy (+ (:dy n) (/ ddy dlen)))
                n)
              n ;; TODO add contents of big inner loop HERE
              ))]
    (map f nodes)))


(defn update
  [node]
  (let [{:keys [x y dx dy fixed]} node
        new-node (if fixed
                   node
                   (assoc node 
                          :x (q/constrain (+ x (q/constrain dx -5.0 5.0)) 
                                          0.0 p/+width+)
                          :y (q/constrain (+ y (q/constrain dy -5.0 5.0))
                                          0.0 p/+height+)))]
    (let [{:keys [dx dy]} new-node]
      (assoc new-node
             :dx (/ dx 2.0)
             :dy (/ dy 2.0)))))


