;; Ben Fry's Visualizing Data, Chapter 5, figure 5-7
;; Sample ranking that highlights lines
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; TODO use salary as a tiebreaker for rankings (p127)

(ns vdquil.chapter5.figure5-4
  (:use quil.core)
  (:use vdquil.chapter5.ch5data)
  (:require clojure.pprint))

(defn setup []
  (text-font (create-font "Georgia" 12)))

(def row-height 23)
(def y (atom 0))

(def teams-with-salary-y
  (let [teams-by-sals (reverse (sort-by second team-salaries))]
    (zipmap (map first teams-by-sals)
            (map #(* row-height (first %)) (map-indexed #(flatten (vector % %2)) teams-by-sals)))))

(defn team [team-abbrev]
  (let [name (mlb-teams team-abbrev)
        logo (str "resources/ch5/small/" team-abbrev "_small.gif")
        w-l (team-standings team-abbrev)
        w-l-ratio (/ (first w-l) (second w-l))
        salary (team-salaries team-abbrev)
        salary-y (teams-with-salary-y team-abbrev)]
    {:abbrev team-abbrev :name name :logo logo :w-l w-l :w-l-ratio w-l-ratio
     :salary salary :salary-y salary-y}))

(def teams-by-ranking
  (reverse (sort-by :w-l-ratio (map team (map first mlb-teams)))))

(defn draw-teams []
  (loop [teams teams-by-ranking]
    (if (map? (first teams))
      (do
        (let [h (* @y row-height)]
          (text (:name (first teams)) 28 h)
          (text (str (first (:w-l (first teams))) "\u2013" (second (:w-l (first teams)))) 115 h)
          (image (load-image (:logo (first teams))) 0 (- h 12.5) 25 25)
          (if (> (:salary-y (first teams)) h) 
            (stroke (color 33 85 156))
            (stroke  (color 206 0 82)))
          (stroke-weight (map-range (:salary (first teams))
                                    (apply min (map second team-salaries))
                                    (apply max (map second team-salaries))
                                    0.25 6))          
          (line 160 h 335 (:salary-y (first teams)))
          (text (str "$" (format "%,d" (:salary (first teams)))) 345 (:salary-y (first teams)))
          (swap! y inc)
          (recur (rest teams))))))
  (reset! y 0))

(defn draw []
  (background 255)
  (smooth)
  (translate 30 30)
  (fill 128)
  (text-align :left :center)
  (stroke 0)
  (draw-teams))

(defsketch mlb
  :title "MLB Rankings"
  :setup setup
  :draw draw
  :size [480 750])
