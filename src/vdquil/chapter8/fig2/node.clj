(ns vdquil.chapter8.fig2.node
  (:require [quil.core :as q]
            [vdquil.chapter8.fig2.params :as p]))

(defrecord Node [x y dx dy fixed label])

(defn make-node
  [label]
  (->Node 
    (rand p/+width+) (rand p/+height+) 
    0.0 0.0 
    true
    label))

;; TODO
(defn relax
  [this-node nodes]
  (let [ddx 0
        ddy 0
        {x :x y :y} this-node
        f1 (fn [{nx :x ny :y
                 ddx :ddx ddy :ddy
                 :as node}]
             (if (= this-node node)
               node
               (let [vx (- x nx)
                     vy (- y ny)
                     lensq (+ (* vx vx) (vy vy))]
                 (if (== lensq 0)
                   (assoc node 
                          :ddx (+ ddx (rand 1))  ; pass this temp info 
                          :ddy (+ ddy (rand 1))) ;  via each node
                   (if (< lensq 10000)
                     (assoc node 
                            :ddx (+ ddx (/ vx lensq)) 
                            :ddy (+ ddy (/ vy lensq)))
                     ; TODO NOT RIGHT





              (if (pos? dlen)
                (assoc n 
                       :dx (+ (:dx n) (/ ddx dlen))
                       :dy (+ (:dy n) (/ ddy dlen)))
                n)
              n ;; TODO add contents of big inner loop HERE
              ))]
    (map f nodes)))

        dlen (/ (q/mag ddx ddy) 2)

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


