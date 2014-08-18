(ns vdquil.chapter8.node
  (use [quil.core]
       [vdquil.chapter8.params]))

(defrecord Node [label x y dx dy fixed cnt])

(defn new-node
  [label]
  (->Node label 
          (rand WIDTH) 
          (rand HEIGHT) 
          0.0 0.0 true 0.0))

(defn increment
  [node]
  (assoc node :cnt (inc (:cnt node))))

;; TODO
(defn relax
  [node nodes]
  (let [ddx 0
        ddy 0
        dlen (/ (mag ddx ddy) 2)
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
                          :x (constrain (+ x (constrain dx -5.0 5.0)) 0.0 WIDTH)
                          :y (constrain (+ y (constrain dy -5.0 5.0)) 0.0 HEIGHT)))]
    (let [{:keys [dx dy]} new-node]
      (assoc new-node
             :dx (/ dx 2.0)
             :dy (/ dy 2.0)))))

