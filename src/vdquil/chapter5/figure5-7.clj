;; Ben Fry's Visualizing Data, Chapter 5, figure 5-7
;; Sample ranking that highlights lines
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; TODO use salary as a tiebreaker for rankings (page 127)

(ns vdquil.chapter5.figure5-7
  (:use quil.core)
  (:use vdquil.chapter5.ch5data))

(defn setup []
  (text-font (create-font "Georgia" 12)))

(def row-height 23)

(def teams-with-salary-y
  (zipmap (map first (reverse (sort-by second team-salaries)))
          (map #(* % row-height) (range))))

(defn team [team-abbrev]
  {:abbrev team-abbrev
   :name (mlb-teams team-abbrev)
   :logo (str "resources/ch5/small/" team-abbrev "_small.gif")
   :w-l (team-standings team-abbrev)
   :w-l-ratio (/ (first (team-standings team-abbrev)) (second (team-standings team-abbrev)))
   :salary (team-salaries team-abbrev)
   :salary-y (teams-with-salary-y team-abbrev)})

(def teams-by-ranking
  (map merge
       (reverse (sort-by :w-l-ratio (map team (map first mlb-teams))))
       (map #(assoc {} :y (* row-height %)) (range))))

(defn draw-teams []
  (doseq [team teams-by-ranking]
    (let [h (:y team)]
      (text (:name team) 28 h)
      (text (str (first (:w-l team)) "-" (second (:w-l team))) 115 h)
      (image (load-image (:logo team)) 0 (- h 12.5) 25 25)
      (if (> (:salary-y team) h) 
        (stroke (color 33 85 156))
        (stroke  (color 206 0 82)))
      (stroke-weight (map-range (:salary team)
                                (apply min (map second team-salaries))
                                (apply max (map second team-salaries))
                                0.25 6))
      
      (line 160 h 335 (:salary-y team))
      (text (str "$" (format "%,d" (:salary team))) 345 (:salary-y team)))))

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
