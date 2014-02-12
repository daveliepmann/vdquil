;; Ben Fry's Visualizing Data, Chapter 5, figure 5-4, 5-5, 5-6
;; Sample team ranking, with color and line widths to show results.
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; Quil-ifying this exercise involved a somewhat more dramatic
;; restructuring compared to previous exercises. Where the Processing
;; original made heavy use of the RankedList class, it made more sense
;; in Clojure to amalgamate the various source data into a *single*
;; data structure with all the information I needed, in the format I
;; preferred.

(ns vdquil.chapter5.figure5-6
  (:use [quil.core]
        [vdquil.chapter5.ch5data]))

(defn setup []
  (text-font (create-font "Sans-Serif" 11)))

(def row-height 23)

(def teams-with-salary-y
  (zipmap (map first (reverse (sort-by second team-salaries)))
          (map #(* % row-height) (range))))

(defn team [team-abbrev]
  {:abbrev team-abbrev
   :name (mlb-teams team-abbrev)
   :logo (str "resources/ch5/small/" team-abbrev "_small.gif")
   :w-l (team-standings team-abbrev)
   :w-l-ratio (/ (first (team-standings team-abbrev))
                 (second (team-standings team-abbrev)))
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

      ;; remove this block to produce fig. 5-4:
      (if (> (:salary-y team) h) 
        (stroke (color 33 85 156))
        (stroke  (color 206 0 82)))

      ;; remove this block to produce fig. 5-5:
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
  (text-align :left :center)
  (fill 0)
  (stroke 0)
  (draw-teams))

(defsketch mlb
  :title "MLB Rankings"
  :setup setup
  :draw draw
  :size [480 750])
