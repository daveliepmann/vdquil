;; Ben Fry's Visualizing Data, Chapter 5, preprocessing
;; Analogous to page 109's short program to download logo images
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter5.preprocessing
  (:use vdquil.chapter5.ch5data)
  (:require [clj-time.core :as time]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Get logos from MLB site
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn copy-image [uri file]
  (with-open [in (clojure.java.io/input-stream uri)
              out (clojure.java.io/output-stream file)]
    (clojure.java.io/copy in out)))

(defn grab-logos
  [folder url-prefix file-suffix]
  (let [uris (map #(str url-prefix % file-suffix) (map first mlb-teams))
        files (map #(str folder % file-suffix) (map first mlb-teams))]
    (map copy-image uris files)))

(grab-logos "resources/ch5/small/" "http://mlb.mlb.com/mlb/images/team_logos/logo_" "_small.gif")
(grab-logos "resources/ch5/50x50/" "http://mlb.mlb.com/mlb/images/team_logos/50x50/" ".gif")
(grab-logos "resources/ch5/79x76/" "http://mlb.mlb.com/mlb/images/team_logos/logo_" "_79x76.jpg")
(grab-logos "resources/ch5/standings/" "http://mlb.mlb.com/mlb/images/team_logos/51x21/" "_standings_logo.gif")
(grab-logos "resources/ch5/100x100/" "http://mlb.mlb.com/mlb/images/team_logos/100x100/" ".gif")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Get standings across a date range for figure 5-8:
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-standings [startyear startmonth startday endyear endmonth endday]
  (let [start     (time/date-time startyear startmonth startday)
        end       (time/date-time endyear endmonth endday)
        num-days  (time/in-days (time/interval start end))]
    (for [day (map #(time/plus start (time/days %)) (range num-days))]
      (->> (map #(into [] %)
                (partition 4
                           (map #(nth % 2)
                                (filter #(#{"code" "w" "l" "league_sensitive_team_name"} (second %))
                                        (re-seq #"\s+([\w\d]+):\s'(.*)',?"
                                                (apply str
                                                       (pmap #(slurp (str "http://mlb.mlb.com/components/game/year_"
                                                                          (time/year day)
                                                                          "/month_"
                                                                          (format "%02d" (time/month day))
                                                                          "/day_"
                                                                          (format "%02d" (time/day day))
                                                                          "/standings_rs_" % ".js"))
                                                             ["alc" "ale" "alw" "nlc" "nle" "nlw"])))))))
           (interpose \newline)
           (apply str)
           (spit (str "data/ch5fig8-"
                      (format "%04d" (time/year day)) "-"
                      (format "%02d" (time/month day)) "-"
                      (format "%02d" (time/day day))
                      ".txt"))))))

;; Some examples:
(get-standings 2007 4 15 2007 4 20) ;; take a few seconds
(get-standings 2007 5 7 2007 5 12)
(get-standings 2007 4 7 2007 5 12) ;; takes on the order of 10 seconds
(get-standings 2007 5 7 2008 5 12) ;; takes several minutes
