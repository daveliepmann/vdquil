;; Ben Fry's Visualizing Data, Chapter 5, preprocessing
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter5.preprocessing
  (:use [vdquil.chapter5.ch5data])
  (:require [clj-time.core :as time]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Get logos from MLB site
;; Analogous to page 109's short program to download logo images
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
;;;; Get standings across a date range (for figure 5-8)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; ;; Parse mlb.com's JSON file for teams' wins, losses, and names (as of a certain date)
(defn get-standings-for-date [year month day desired-attributes?]
  (let [yyyy (format "%04d" year)
        mm   (format "%02d" month)
        dd   (format "%02d" day)]
    (->> (map #(slurp (str "http://mlb.mlb.com/components/game/year_"
                           yyyy "/month_" mm "/day_" dd "/standings_rs_" % ".js"))
              ["alc" "ale" "alw" "nlc" "nle" "nlw"])
         (apply str)
         (re-seq #"\s+([\w\d]+):\s'(.*)',?")
         (reduce (fn [acc [_ attr code]]
                   (if (desired-attributes? attr) (conj acc attr code) acc)) [])
         (partition 8)
         (map #(map (fn [i] (let [r-s (read-string i)] (if (number? r-s) r-s i))) %))
         (map #(apply hash-map %)))))

;; e.g.:
(get-standings-for-date 2007 04 15 #{"code" "w" "l" "league_sensitive_team_name"})
;; => ({"l" 3, "code" "cle", "league_sensitive_team_name" "Cleveland", "w" 6} {"l" 5, "code" "det", "league_sensitive_team_name" "Detroit", "w" 7} {"l" 5, "code" "min", "league_sensitive_team_name" "Minnesota", "w" 7} {"l" 6, "code" "cws", "league_sensitive_team_name" "Chi White Sox", "w" 5} {"l" 9, "code" "kc", "league_sensitive_team_name" "Kansas City", "w" 3} {"l" 4, "code" "bos", "league_sensitive_team_name" "Boston", "w" 6} {"l" 5, "code" "tor", "league_sensitive_team_name" "Toronto", "w" 7} {"l" 6, "code" "bal", "league_sensitive_team_name" "Baltimore", "w" 6} {"l" 6, "code" "nyy", "league_sensitive_team_name" "NY Yankees", "w" 5} {"l" 7, "code" "tb", "league_sensitive_team_name" "Tampa Bay", "w" 5} {"l" 3, "code" "sea", "league_sensitive_team_name" "Seattle", "w" 5} {"l" 6, "code" "ana", "league_sensitive_team_name" "LA Angels", "w" 6} {"l" 7, "code" "oak", "league_sensitive_team_name" "Oakland", "w" 6} {"l" 7, "code" "tex", "league_sensitive_team_name" "Texas", "w" 5} {"l" 5, "code" "cin", "league_sensitive_team_name" "Cincinnati", "w" 7} {"l" 5, "code" "mil", "league_sensitive_team_name" "Milwaukee", "w" 6} {"l" 5, "code" "stl", "league_sensitive_team_name" "St. Louis", "w" 6} {"l" 6, "code" "hou", "league_sensitive_team_name" "Houston", "w" 4} {"l" 6, "code" "pit", "league_sensitive_team_name" "Pittsburgh", "w" 4} {"l" 7, "code" "chc", "league_sensitive_team_name" "Chi Cubs", "w" 4} {"l" 3, "code" "atl", "league_sensitive_team_name" "Atlanta", "w" 8} {"l" 4, "code" "nym", "league_sensitive_team_name" "NY Mets", "w" 7} {"l" 5, "code" "fla", "league_sensitive_team_name" "Florida", "w" 6} {"l" 8, "code" "phi", "league_sensitive_team_name" "Philadelphia", "w" 3} {"l" 9, "code" "was", "league_sensitive_team_name" "Washington", "w" 3} {"l" 4, "code" "ari", "league_sensitive_team_name" "Arizona", "w" 9} {"l" 4, "code" "la", "league_sensitive_team_name" "LA Dodgers", "w" 8} {"l" 5, "code" "sd", "league_sensitive_team_name" "San Diego", "w" 7} {"l" 7, "code" "col", "league_sensitive_team_name" "Colorado", "w" 5} {"l" 7, "code" "sf", "league_sensitive_team_name" "San Francisco", "w" 3})

;; Wrap get-standings-for-date with the clojure-time library to get
;; team standings for a range of dates
(defn get-standings-for-date-range [startyear startmonth startday endyear endmonth endday]
  (let [start    (time/date-time startyear startmonth startday)
        end      (time/date-time endyear endmonth endday)
        attr?    #{"code" "w" "l" "league_sensitive_team_name"}]
    (apply conj (pmap #(hash-map (str (time/year %)
                                      "-"
                                      (time/month %)
                                      "-"
                                      (time/day %))
                                 (get-standings-for-date (time/year %) 
                                                         (time/month %)
                                                         (time/day %)
                                                         attr?))
                      (map #(time/plus start (time/days %)) (range (time/in-days (time/interval start end))))))))

;; e.g.:
(get-standings-for-date-range 2007 04 20 2007 04 23)
;; => {"2007-4-21" ({"l" 6, "code" "min", "league_sensitive_team_name" "Minnesota", "w" 11} {"l" 7, "code" "cws", "league_sensitive_team_name" "Chi White Sox", "w" 9} {"l" 8, "code" "det", "league_sensitive_team_name" "Detroit", "w" 9} {"l" 7, "code" "cle", "league_sensitive_team_name" "Cleveland", "w" 7} {"l" 12, "code" "kc", "league_sensitive_team_name" "Kansas City", "w" 5} {"l" 5, "code" "bos", "league_sensitive_team_name" "Boston", "w" 11} {"l" 7, "code" "bal", "league_sensitive_team_name" "Baltimore", "w" 10} {"l" 8, "code" "nyy", "league_sensitive_team_name" "NY Yankees", "w" 8} {"l" 9, "code" "tor", "league_sensitive_team_name" "Toronto", "w" 8} {"l" 10, "code" "tb", "league_sensitive_team_name" "Tampa Bay", "w" 7} {"l" 8, "code" "oak", "league_sensitive_team_name" "Oakland", "w" 9} {"l" 9, "code" "ana", "league_sensitive_team_name" "LA Angels", "w" 8} {"l" 10, "code" "tex", "league_sensitive_team_name" "Texas", "w" 7} {"l" 8, "code" "sea", "league_sensitive_team_name" "Seattle", "w" 5} {"l" 7, "code" "mil", "league_sensitive_team_name" "Milwaukee", "w" 10} {"l" 7, "code" "hou", "league_sensitive_team_name" "Houston", "w" 9} {"l" 9, "code" "cin", "league_sensitive_team_name" "Cincinnati", "w" 9} {"l" 10, "code" "chc", "league_sensitive_team_name" "Chi Cubs", "w" 7} {"l" 10, "code" "stl", "league_sensitive_team_name" "St. Louis", "w" 7} {"l" 10, "code" "pit", "league_sensitive_team_name" "Pittsburgh", "w" 6} {"l" 5, "code" "nym", "league_sensitive_team_name" "NY Mets", "w" 11} {"l" 6, "code" "atl", "league_sensitive_team_name" "Atlanta", "w" 11} {"l" 10, "code" "fla", "league_sensitive_team_name" "Florida", "w" 7} {"l" 12, "code" "was", "league_sensitive_team_name" "Washington", "w" 6} {"l" 11, "code" "phi", "league_sensitive_team_name" "Philadelphia", "w" 5} {"l" 5, "code" "la", "league_sensitive_team_name" "LA Dodgers", "w" 13} {"l" 7, "code" "sd", "league_sensitive_team_name" "San Diego", "w" 11} {"l" 9, "code" "ari", "league_sensitive_team_name" "Arizona", "w" 10} {"l" 8, "code" "sf", "league_sensitive_team_name" "San Francisco", "w" 8} {"l" 11, "code" "col", "league_sensitive_team_name" "Colorado", "w" 7}), "2007-4-22" ({"l" 7, "code" "min", "league_sensitive_team_name" "Minnesota", "w" 11} {"l" 8, "code" "det", "league_sensitive_team_name" "Detroit", "w" 10} {"l" 7, "code" "cle", "league_sensitive_team_name" "Cleveland", "w" 8} {"l" 8, "code" "cws", "league_sensitive_team_name" "Chi White Sox", "w" 9} {"l" 12, "code" "kc", "league_sensitive_team_name" "Kansas City", "w" 6} {"l" 5, "code" "bos", "league_sensitive_team_name" "Boston", "w" 12} {"l" 7, "code" "bal", "league_sensitive_team_name" "Baltimore", "w" 11} {"l" 9, "code" "nyy", "league_sensitive_team_name" "NY Yankees", "w" 8} {"l" 10, "code" "tor", "league_sensitive_team_name" "Toronto", "w" 8} {"l" 11, "code" "tb", "league_sensitive_team_name" "Tampa Bay", "w" 7} {"l" 9, "code" "ana", "league_sensitive_team_name" "LA Angels", "w" 9} {"l" 9, "code" "oak", "league_sensitive_team_name" "Oakland", "w" 9} {"l" 10, "code" "tex", "league_sensitive_team_name" "Texas", "w" 8} {"l" 9, "code" "sea", "league_sensitive_team_name" "Seattle", "w" 5} {"l" 7, "code" "mil", "league_sensitive_team_name" "Milwaukee", "w" 11} {"l" 8, "code" "hou", "league_sensitive_team_name" "Houston", "w" 9} {"l" 10, "code" "cin", "league_sensitive_team_name" "Cincinnati", "w" 9} {"l" 10, "code" "stl", "league_sensitive_team_name" "St. Louis", "w" 8} {"l" 10, "code" "pit", "league_sensitive_team_name" "Pittsburgh", "w" 7} {"l" 11, "code" "chc", "league_sensitive_team_name" "Chi Cubs", "w" 7} {"l" 6, "code" "atl", "league_sensitive_team_name" "Atlanta", "w" 12} {"l" 6, "code" "nym", "league_sensitive_team_name" "NY Mets", "w" 11} {"l" 10, "code" "fla", "league_sensitive_team_name" "Florida", "w" 8} {"l" 11, "code" "phi", "league_sensitive_team_name" "Philadelphia", "w" 6} {"l" 13, "code" "was", "league_sensitive_team_name" "Washington", "w" 6} {"l" 6, "code" "la", "league_sensitive_team_name" "LA Dodgers", "w" 13} {"l" 8, "code" "sd", "league_sensitive_team_name" "San Diego", "w" 11} {"l" 8, "code" "sf", "league_sensitive_team_name" "San Francisco", "w" 9} {"l" 10, "code" "ari", "league_sensitive_team_name" "Arizona", "w" 10} {"l" 11, "code" "col", "league_sensitive_team_name" "Colorado", "w" 8}), "2007-4-20" ({"l" 6, "code" "min", "league_sensitive_team_name" "Minnesota", "w" 10} {"l" 7, "code" "det", "league_sensitive_team_name" "Detroit", "w" 9} {"l" 6, "code" "cle", "league_sensitive_team_name" "Cleveland", "w" 7} {"l" 7, "code" "cws", "league_sensitive_team_name" "Chi White Sox", "w" 8} {"l" 11, "code" "kc", "league_sensitive_team_name" "Kansas City", "w" 5} {"l" 5, "code" "bos", "league_sensitive_team_name" "Boston", "w" 10} {"l" 7, "code" "bal", "league_sensitive_team_name" "Baltimore", "w" 9} {"l" 7, "code" "nyy", "league_sensitive_team_name" "NY Yankees", "w" 8} {"l" 8, "code" "tor", "league_sensitive_team_name" "Toronto", "w" 8} {"l" 10, "code" "tb", "league_sensitive_team_name" "Tampa Bay", "w" 6} {"l" 7, "code" "oak", "league_sensitive_team_name" "Oakland", "w" 9} {"l" 9, "code" "ana", "league_sensitive_team_name" "LA Angels", "w" 7} {"l" 7, "code" "sea", "league_sensitive_team_name" "Seattle", "w" 5} {"l" 10, "code" "tex", "league_sensitive_team_name" "Texas", "w" 6} {"l" 6, "code" "hou", "league_sensitive_team_name" "Houston", "w" 9} {"l" 7, "code" "mil", "league_sensitive_team_name" "Milwaukee", "w" 9} {"l" 8, "code" "cin", "league_sensitive_team_name" "Cincinnati", "w" 9} {"l" 9, "code" "stl", "league_sensitive_team_name" "St. Louis", "w" 7} {"l" 9, "code" "pit", "league_sensitive_team_name" "Pittsburgh", "w" 6} {"l" 10, "code" "chc", "league_sensitive_team_name" "Chi Cubs", "w" 6} {"l" 5, "code" "atl", "league_sensitive_team_name" "Atlanta", "w" 11} {"l" 5, "code" "nym", "league_sensitive_team_name" "NY Mets", "w" 10} {"l" 10, "code" "fla", "league_sensitive_team_name" "Florida", "w" 6} {"l" 11, "code" "was", "league_sensitive_team_name" "Washington", "w" 6} {"l" 11, "code" "phi", "league_sensitive_team_name" "Philadelphia", "w" 4} {"l" 5, "code" "la", "league_sensitive_team_name" "LA Dodgers", "w" 12} {"l" 7, "code" "sd", "league_sensitive_team_name" "San Diego", "w" 10} {"l" 8, "code" "ari", "league_sensitive_team_name" "Arizona", "w" 10} {"l" 8, "code" "sf", "league_sensitive_team_name" "San Francisco", "w" 7} {"l" 10, "code" "col", "league_sensitive_team_name" "Colorado", "w" 7})}

;; Save standings to a file by applying pr-str and spitting.
;; 
;; This step is not strictly necessary, since most data sets would be
;; small enough to save within a file (as we did with ch5data.clj for
;; previous exercises). Nevertheless, I used this approach to show how
;; to save unwieldy data sets to the filesystem and retrieve them
;; later.
;;
;; Replace the given date range with whatever date range you want to
;; play with.
(spit "data/ch5fig8.txt" (pr-str (get-standings-for-date-range 2007 04 20 2007 04 30)))
