;; Data for exercises in Chapter 3

(ns vdquil.chapter3.ch3data)

;; In the interests of hewing close to the original exercises,
;; I kept the data sources separate. If this were an actual project
;; I would prefer to do the preprocessing to combine them
;; into a single definition.

;; Location, name, and random data was:
;;  - copied from the source files into emacs,
;;  - stripped of tabs using replace,
;;  - and given quotation marks and brackets as necessary using multiple-cursors-mode.

(def location-data
  {"AL" [439 270]
   "AK" [94 325]
   "AZ" [148 241]
   "AR" [368 247]
   "CA" [56 176]
   "CO" [220 183]
   "CT" [576 120]
   "DE" [556 166]
   "FL" [510 331]
   "GA" [478 267]
   "HI" [232 380]
   "ID" [143 101]
   "IL" [405 168]
   "IN" [437 165]
   "IA" [357 147]
   "KS" [302 194]
   "KY" [453 203]
   "LA" [371 302]
   "ME" [595 59]
   "MD" [538 162]
   "MA" [581 108]
   "MI" [446 120]
   "MN" [339 86]
   "MS" [406 274]
   "MO" [365 197]
   "MT" [194 61]
   "NE" [286 151]
   "NV" [102 157]
   "NH" [580 89]
   "NJ" [561 143]
   "NM" [208 245]
   "NY" [541 107]
   "NC" [519 221]
   "ND" [283 65]
   "OH" [472 160]
   "OK" [309 239]
   "OR" [74 86]
   "PA" [523 144]
   "RI" [589 117]
   "SC" [506 251]
   "SD" [286 109]
   "TN" [441 229]
   "TX" [291 299]
   "UT" [154 171]
   "VT" [567 86]
   "VA" [529 189]
   "WA" [92 38]
   "WV" [496 178]
   "WI" [392 103]
   "WY" [207 125]})

(def name-data  
  {"AL" "Alabama"
   "AK" "Alaska"
   "AZ" "Arizona"
   "AR" "Arkansas"
   "CA" "California"
   "CO" "Colorado"
   "CT" "Connecticut"
   "DE" "Delaware"
   "FL" "Florida"
   "GA" "Georgia"
   "HI" "Hawaii"
   "ID" "Idaho"
   "IL" "Illinois"
   "IN" "Indiana"
   "IA" "Iowa"
   "KS" "Kansas"
   "KY" "Kentucky"
   "LA" "Louisiana"
   "ME" "Maine"
   "MD" "Maryland"
   "MA" "Massachusetts"
   "MI" "Michigan"
   "MN" "Minnesota"
   "MS" "Mississippi"
   "MO" "Missouri"
   "MT" "Montana"
   "NE" "Nebraska"
   "NV" "Nevada"
   "NH" "New Hampshire"
   "NJ" "New Jersey"
   "NM" "New Mexico"
   "NY" "New York"
   "NC" "North Carolina"
   "ND" "North Dakota"
   "OH" "Ohio"
   "OK" "Oklahoma"
   "OR" "Oregon"
   "PA" "Pennsylvania"
   "RI" "Rhode Island"
   "SC" "South Carolina"
   "SD" "South Dakota"
   "TN" "Tennessee"
   "TX" "Texas"
   "UT" "Utah"
   "VT" "Vermont"
   "VA" "Virginia"
   "WA" "Washington"
   "WV" "West Virginia"
   "WI" "Wisconsin"
   "WY" "Wyoming"})

(def random-data
  {"AL" 0.1
   "AK" -5.3
   "AZ" 3
   "AR" 7
   "CA" 11
   "CO" 1.5
   "CT" -6.7
   "DE" -4
   "FL" 9
   "GA" 2
   "HI" -3.3
   "ID" 6.6
   "IL" 7.2
   "IN" 7.1
   "IA" 6.9
   "KS" 6
   "KY" 1.8
   "LA" 7.5
   "ME" -4
   "MD" 0.1
   "MA" -6
   "MI" 1.7
   "MN" -2
   "MS" -4.4
   "MO" -2
   "MT" 1.0
   "NE" 1.2
   "NV" 1.6
   "NH" 0.5
   "NJ" 0.2
   "NM" 8.8
   "NY" 1.4
   "NC" 9.7
   "ND" 5.4
   "OH" 3.2
   "OK" 6
   "OR" -4
   "PA" -7
   "RI" -2
   "SC" 1
   "SD" 6
   "TN" 5
   "TX" -3.4
   "UT" 2.3
   "VT" 4.8
   "VA" 3
   "WA" 2.2
   "WV" 5.4
   "WI" 3.1
   "WY" -6})
