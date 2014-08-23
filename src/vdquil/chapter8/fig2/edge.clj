(ns vdquil.chapter8.fig2.edge
  (:require [quil.core :as q]
            [vdquil.chapter8.fig2.params :as p]))


(defrecord Edge [from to len])

(defn make-edge
  "from and to are nodes."
  [from to]
  (->Edge from to 50))

(defn relax
  [edge]
  (let [from (:from edge)
        to (:to edge)
        len (:len edge)
        vx (- (:x to) (:x from))
        vy (- (:y to) (:y from))
        d (q/mag vx vy)]
    (if (<= d 0)
      edge
      (let [f (/ (- len d) (* d 3))
            dx (* f vx)
            dy (* f vy)]
        (assoc edge 
               :from (assoc from 
                            :dx (- (:dx from) dx)
                            :dy (- (:dy from) dy))
               :to   (assoc to 
                            :dx (+ (:dx to) dx)
                            :dy (+ (:dy to) dy)))))))

(defn draw
  [edge]
  (q/stroke 0)
  (q/stroke-weight 0.35)
  (let [from (:from edge)
        to (:to edge)]
    (q/line (:x from)
            (:y from)
            (:x to)
            (:y to))))
