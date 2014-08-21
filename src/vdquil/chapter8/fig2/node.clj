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

;; RELAX
;; Description of algorithm for relax() in Node.pde:
;; Use ddx, ddy as accumulator vars starting = 0.
;; For all *other* nodes:
;;  Calc diff tween their x, y coords and mine
;;  Sum the squares of the two differences (Pyth thm without root)
;;  call this lensq.
;;  if the sum of squares = 0 (i.e. the other node is on top of me)
;;    add random positive increments to ddx and ddy
;;  or if it's > 0, but < 100^2
;;    then sum into ddx, ddy the diffs vx, vy, normalized by dividing by the sum of squares lensq
;;  and if it.s > 100^2, leave ddx, ddy alone.
;;  Then when you're done with the loop through other nodes
;;    calculate half the length of the difference vector <ddx,ddy> using mag().
;;    call this dlen.
;;    if this dlen is nonzero,
;;    then increment my dx by ddx/dlen, and dy by ddy/dlen
;;    node ddx and ddy could be negative.
;;    i.e. we push my dx and dy by ddx, ddy as proportion of the length of the vector they make
;; 
;;  Then the update() function adds dx and dy into x and y, constraining them to certain limits
;;
;; Overall strategy of this definition:
;;   - Calculate ddx and ddy, each of which is a slightly complex sum, by `reduce`ing over all
;;     other nodes using the internal function `sum-normalized-diffs` defined below.
;;   - Then use ddx and ddy to calculate new values for dx and dy for this node, and return
;;     a version of this node with the old dx and dy replaced with the new ones.
;;   Everything except the last step is done within the `let` bindings.
(defn relax
  "Returns a version of this-node with new values for dx and dy
  that reflect relaxation with respect to the other nodes."
  [all-nodes this-node]
  (let [other-nodes (remove #(= this-node %) all-nodes)

        {x :x y :y dx :dx dy :dy} this-node

        sum-normalized-diffs (fn [[ddx ddy]                ; quantities undergoing reducing
                                  {other-x :x other-y :y}] ; the next node to examine (other-x and other-y are n.x and n.y in node.pde)
                               (let [x-diff (- x other-x) ; vx in node.pde
                                     y-diff (- y other-y) ; vy in node.pde
                                     sum-sq-diffs (+ (* x-diff x-diff) (* y-diff y-diff))] ; lensq in node.pde
                                 (cond (== sum-sq-diffs 0) [(+ ddx (rand)) (+ ddy (rand))]      ; note we always return a 2-element seq
                                       (< sum-sq-diffs 10000) [(+ ddx (/ x-diff sum-sq-diffs))  ; since that's what the reducing fn
                                                               (+ ddy (/ y-diff sum-sq-diffs))] ; (i.e. this one) is looking for
                                       :else [ddx ddy]))) 

        [ddx ddy] (reduce sum-normalized-diffs [0 0] other-nodes)

        dlen (/ (q/mag ddx ddy) 2)

        new-dx (if (<= dlen 0) dx (+ dx (/ ddx dlen)))
        new-dy (if (<= dlen 0) dy (+ dy (/ ddy dlen)))]

    (assoc this-node :dx new-dx :dy new-dy)))


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

(defn draw
  [{x :x y :y label :label}] ; a node
  (q/fill p/+node-color+)
  (q/stroke 0)
  (q/stroke-weight 0.5)
  (q/ellipse x y 25 25)
  (q/text-align :center)
  (q/text label x y))
