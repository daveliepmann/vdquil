;; Ben Fry's Visualizing Data, Chapter 5, preprocessing
;; Analogous to page 109's short program to download logo images
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter5.preprocessing
  (:use vdquil.chapter5.ch5data))

;; Get logos from MLB site
(defn copy-image [uri file]
  (with-open [in (clojure.java.io/input-stream uri)
              out (clojure.java.io/output-stream file)]
    (clojure.java.io/copy in out)))

(defn grab-logos
  [folder url-prefix file-suffix]
  (let [uris (map #(str url-prefix % file-suffix) (map first teams))
        files (map #(str folder % file-suffix) (map first teams))]
    (map copy-image uris files)))

(grab-logos "resources/ch5/small/" "http://mlb.mlb.com/mlb/images/team_logos/logo_" "_small.gif")
(grab-logos "resources/ch5/50x50/" "http://mlb.mlb.com/mlb/images/team_logos/50x50/" ".gif")
(grab-logos "resources/ch5/79x76/" "http://mlb.mlb.com/mlb/images/team_logos/logo_" "_79x76.jpg")
(grab-logos "resources/ch5/standings/" "http://mlb.mlb.com/mlb/images/team_logos/51x21/" "_standings_logo.gif")
(grab-logos "resources/ch5/100x100/" "http://mlb.mlb.com/mlb/images/team_logos/100x100/" ".gif")
