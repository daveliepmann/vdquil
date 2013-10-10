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

(def teams
  '(("nyy" "NY Yankees")
    ("bos" "Boston")
    ("nym" "New York Mets")
    ("ana" "LA Angels")
    ("cws" "Chi White Sox")
    ("la" "LA Dodgers")
    ("sea" "Seattle")
    ("chc" "Chi Cubs")
    ("det" "Detroit")
    ("bal" "Baltimore")
    ("stl" "St. Louis")
    ("sf" "San Francisco")
    ("phi" "Philadelphia")
    ("hou" "Houston")
    ("atl" "Atlanta")
    ("tor" "Toronto")
    ("oak" "Oakland")
    ("min" "Minnesota")
    ("mil" "Milwaukee")
    ("cin" "Cincinnati")
    ("tex" "Texas")
    ("kc" "Kansas City")
    ("cle" "Cleveland")
    ("sd" "San Diego")
    ("col" "Colorado")
    ("ari" "Arizona")
    ("pit" "Pittsburgh")
    ("was" "Washington")
    ("fla" "Florida")
    ("tb" "Tampa Bay")))

(def standings
  '(("bos" 6 4)
    ("tor" 7 5)
    ("bal" 6 6)
    ("nyy" 5 6)
    ("tb" 5 7)
    ("sea" 5 3)
    ("ana" 6 6)
    ("oak" 6 7)
    ("tex" 5 7)
    ("cle" 6 3)
    ("det" 7 5)
    ("min" 7 5)
    ("cws" 5 6)
    ("kc" 3 9)
    ("atl" 8 3)
    ("nym" 7 4)
    ("fla" 6 5)
    ("phi" 3 8)
    ("was" 3 9)
    ("ari" 9 4)
    ("la" 8 4)
    ("sd" 7 5)
    ("col" 5 7)
    ("sf" 3 7)
    ("cin" 7 5)
    ("mil" 6 5)
    ("stl" 6 5)
    ("hou" 4 6)
    ("pit" 4 6)
    ("chc" 4 7)))
