;; Data for exercises in Chapter 5

(ns vdquil.chapter5.ch5data)

;; Data (locations, names) was extracted from the supplied TSV files
;; interactively in emacs using Jack Rusher's tsv-to-sexp lisp function:
;;
;;;; (defun tsv-to-sexp (tsv)
;;;;   "Parses the string `tsv` as a tab-separated-value file,
;;;; returning a sexp containing the values with strings converted to
;;;; numbers where appropriate."
;;;;   (-map (lambda (s) (-map 'reformat-field (s-split "\t" s))) (s-lines tsv)))

;; TODO extract data in emacs & clojure instead of taking Mr. Fry's TSVs

(def mlb-teams
  {"nyy" "NY Yankees"
   "bos" "Boston"
   "nym" "New York Mets"
   "ana" "LA Angels"
   "cws" "Chi White Sox"
   "la" "LA Dodgers"
   "sea" "Seattle"
   "chc" "Chi Cubs"
   "det" "Detroit"
   "bal" "Baltimore"
   "stl" "St. Louis"
   "sf" "San Francisco"
   "phi" "Philadelphia"
   "hou" "Houston"
   "atl" "Atlanta"
   "tor" "Toronto"
   "oak" "Oakland"
   "min" "Minnesota"
   "mil" "Milwaukee"
   "cin" "Cincinnati"
   "tex" "Texas"
   "kc" "Kansas City"
   "cle" "Cleveland"
   "sd" "San Diego"
   "col" "Colorado"
   "ari" "Arizona"
   "pit" "Pittsburgh"
   "was" "Washington"
   "fla" "Florida"
   "tb" "Tampa Bay"})

(def team-standings
  {"bos" [6 4]
   "tor" [7 5]
   "bal" [6 6]
   "nyy" [5 6]
   "tb" [5 7]
   "sea" [5 3]
   "ana" [6 6]
   "oak" [6 7]
   "tex" [5 7]
   "cle" [6 3]
   "det" [7 5]
   "min" [7 5]
   "cws" [5 6]
   "kc" [3 9]
   "atl" [8 3]
   "nym" [7 4]
   "fla" [6 5]
   "phi" [3 8]
   "was" [3 9]
   "ari" [9 4]
   "la" [8 4]
   "sd" [7 5]
   "col" [5 7]
   "sf" [3 7]
   "cin" [7 5]
   "mil" [6 5]
   "stl" [6 5]
   "hou" [4 6]
   "pit" [4 6]
   "chc" [4 7]})

;; I wanted to grab the salary data directly from USA Today (as
;; described on p109) so I could preprocess it manually in emacs, but
;; I got a DNS error across multiple attempts on distinct days.
;; http://usatoday30.usatoday.com/sports/baseball/salaries/totalpayroll.aspx?year=2007
(def team-salaries
  {"nyy" 189639045
   "bos" 143026214
   "nym" 115231663
   "ana" 109251333
   "cws" 108671833
   "la" 108454524
   "sea" 106460833
   "chc" 99670332
   "det" 95180369
   "bal" 93554808
   "stl" 90286823
   "sf" 90219056
   "phi" 89428213
   "hou" 87759000
   "atl" 87290833
   "tor" 81942800
   "oak" 79366940
   "min" 71439500
   "mil" 70986500
   "cin" 68904980
   "tex" 68318675
   "kc" 67116500
   "cle" 61673267
   "sd" 58110567
   "col" 54424000
   "ari" 52067546
   "pit" 38537833
   "was" 37347500
   "fla" 30507000
   "tb" 24123500})
