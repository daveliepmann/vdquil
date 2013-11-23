;; Ben Fry's Visualizing Data, Chapter 5, figure 5-8
;; Sample ranking with date selector bar
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

;; TODO use salary as a tiebreaker for rankings (p127)
;; TODO need a clojure/quil version of Interpolator to smoothly animate transitions

(ns vdquil.chapter5.figure5-8
  (:use quil.core)
  (:use vdquil.chapter5.ch5data))

(defn setup []
  (text-font (create-font "Georgia" 12)))

(def row-height 23)

(def standings-over-time
  (read-string (slurp "data/ch5fig8.txt")))

(def selected-date (atom (first (first standings-over-time))))

(def teams-with-salary-y
  (zipmap (map first (reverse (sort-by second team-salaries)))
          (map #(* % row-height) (range))))

(defn team [team-abbrev]
  (let [team-standings-for-date (first (filter #(= (% "code") team-abbrev) (standings-over-time @selected-date)))]
    {:abbrev team-abbrev
     :name (mlb-teams team-abbrev)
     :logo (str "resources/ch5/small/" team-abbrev "_small.gif")
     :w-l [(team-standings-for-date "w")
           (team-standings-for-date "l")]
     :w-l-ratio (/ (team-standings-for-date "w")
                   (team-standings-for-date "l"))
     :salary (team-salaries team-abbrev)
     :salary-y (teams-with-salary-y team-abbrev)}))

(defn teams-by-ranking []
  (map merge
       (reverse (sort-by :w-l-ratio (map team (map first mlb-teams))))
       (map #(assoc {} :y (* row-height %)) (range))))

(defn draw-teams [teams]
  (doseq [team teams]
    (let [h (:y team)]
      (text (:name team) 28 h)
      (text (str (first (:w-l team)) "-" (second (:w-l team))) 115 h)
      (image (load-image (:logo team)) 0 (- h 12.5) 25 25)
      (if (> (:salary-y team) h) 
        (stroke (color 33 85 156))
        (stroke  (color 206 0 82)))
      ;; TODO It would make more sense to me if the stroke-weight
      ;; was proportional to the difference between salary and
      ;; ranking, rather than acting merely as another display of
      ;; salary ranking.
      (stroke-weight (map-range (:salary team)
                                (apply min (map second team-salaries))
                                (apply max (map second team-salaries))
                                0.25 6))
      (line 160 h 335 (:salary-y team))
      (text (str "$" (format "%,d" (:salary team))) 345 (:salary-y team)))))

(def dates-by-x
  (zipmap
   (range 0 (* 2 (count standings-over-time)) 2)
   (map first standings-over-time)))

(defn draw-date-selector []
  (stroke-weight 1)
  (doseq [date date-range]
    (let [date-x (/ (- (width) (* 2 (count standings-over-time))) 2)
          x (+ date-x (date :x))]
      (if (= @selected-date (date :date))
        (do (stroke 0)
            (line x 10 x 23)
            (text-align :center :top)
            (text @selected-date x 25))
        (do (stroke 128)
            (line x 10 x 17))))))

(defn draw []
  (background 255)
  (smooth)
  (draw-date-selector)
  (translate 30 55)
  (fill 128)
  (text-align :left :center)
  (stroke 0)
  (draw-teams (teams-by-ranking)))

(defn mouse-handler []
  (let [x-for-date (- (mouse-x) (/ (- (width) (* 2 (count standings-over-time))) 2))
        mouse-date (if (odd? x-for-date) (inc x-for-date) x-for-date)]
    (if (and (< (mouse-y) 30) (not (nil? (dates-by-x mouse-date))))
      (reset! selected-date
              (dates-by-x mouse-date)))))

(defn key-handler []
  (println (str (raw-key)))
  (let [old-date-x ((into {} (map (fn [[a b]] [b a]) dates-by-x)) @selected-date)
        key (if (= processing.core.PConstants/CODED (int (raw-key)))
              (key-code)
              (raw-key))]
    (if (= key 37) ;; left arrow
      (if (not (nil? (dates-by-x (- old-date-x 2)))) 
        (reset! selected-date (dates-by-x (- old-date-x 2))))
      (if (= key 39) ;; right arrow
        (if (not (nil? (dates-by-x (+ 2 old-date-x))))
          (reset! selected-date (dates-by-x (+ 2 old-date-x))))))))

(defsketch mlb
  :title "MLB Rankings"
  :setup setup
  :draw draw
  :size [480 750]
  :mouse-pressed mouse-handler
  :mouse-dragged mouse-handler
  :key-pressed key-handler)
