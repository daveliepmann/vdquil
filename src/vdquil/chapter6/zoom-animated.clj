;; Ben Fry's Visualizing Data, Chapter 6 zipcode scatterplot, with animated zoom
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter6.zoom-animated
  (:use [quil.core]
        [vdquil.util])
  (:require [clojure.string :as string]))

;; Test cases: 48529, 91110, 12561,
;; 98350 (northernmost and westernmost, but not uppermost), 33040 (southernmost)
;; 04652 (easternmost), 95456 (leftmost), 98281 (uppermost)
;; 59544 and 33242 (both marked as problematic in Processing source)

(def canvas-height 453)
(def canvas-width 720)
(def aspect-ratio (/ canvas-width canvas-height))

(def x-min 30)
(def x-max (- canvas-width x-min))
(def y-max 20)
(def y-min (- canvas-height y-max))

(def typed-chars (atom ""))
(def zoom-enabled (atom false))
(def palette (atom {}))
(def matched-zips (atom ()))

;; constants pulled from 1st line of data:
(def bounds-queue
  (atom [[0.4181981 0.87044954 -0.3668288878807947 0.3519813478807947]]))

(defn create-zip
  "Given a line of zipcode data, create a zipcode data structure"
  [data]
  (let [[zip longitude latitude desc] (string/split data #"\t")]
    {:zip zip :longitude (read-string longitude)
     :latitude (read-string latitude) :desc desc}))

(def zip-trie (reduce (fn [acc line]
                        (let [zip (create-zip line)]
                          (assoc-in acc (vec (:zip zip)) zip)))
                      {}
                      (string/split-lines (slurp "data/zips-modified.tsv"))))

(defn zipcodes-from-trie 
  "Return a vector of all child zipcodes in `trie`."
  [trie]
  (into [] (filter :zip (tree-seq (comp char? first keys) vals trie))))

(defn setup []
  (frame-rate 20)
  (text-font (load-font "ScalaSans-Regular-14.vlw"))
  (text-mode :screen)
  (no-stroke)
  (text-align :left)
  (reset! palette {:dormant     (hex-to-color "#999966")
                   :highlight   (hex-to-color "#CBCBCB")
                   :unhighlight (hex-to-color "#66664C")
                   :bad         (hex-to-color "#FFFF66")}))

(defn draw-instructions
  "Tell the user how to use the interface"
  [clr]
  (text-align :left)
  (fill clr)
  (if (= @typed-chars "")
    (text "type the digits of a zip code" 40 (- canvas-height 40))
    (text @typed-chars 40 (- canvas-height 40)))
  
  (text-align :right)
  (fill (if @zoom-enabled
          (@palette :highlight)
          (@palette :unhighlight)))
  (text "zoom" (- canvas-width 40) (- canvas-height 40)))

(defn draw-zip-detail
  "Highlight a single zip code by showing its information"
  [zip zip-desc x y]
  (no-stroke)
  (fill (@palette :highlight))
  (let [size (if @zoom-enabled 6 4)] (rect x y size size))
  (text-align :center)
  (text (str zip-desc ", " zip) 
        (if (> x (/ canvas-width 3))
          (- x (+ 8 (text-width zip)))
          (+ x (text-width zip) 8))
        (- y 4)))

(defn calc-bounds
  "Calculate bounding box of `zips`, a sequence of lat/long-containing maps."
  [zips]
  (reduce (fn [[min-lat max-lat min-long max-long] {:keys [longitude latitude]}]
            (vector (min min-lat  latitude)  (max max-lat  latitude)
                    (min min-long longitude) (max max-long longitude)))
          [Float/MAX_VALUE (- Float/MAX_VALUE) Float/MAX_VALUE (- Float/MAX_VALUE)]
          zips))

;; NB: though one would think that we could check only
;; (empty? (zipcodes-from-trie (get-in trie partial-zip)))
;; ...this is not the case. To see why, consider:
;; (pprint (zipcodes-from-trie (get-in zip-trie (vec "901"))))
;; Sets of urban zips often have *identical* coordinates.
(defn find-bounds
  "Find the appropriate bounding box for the zipcodes matching `partial-zip`."
  [partial-zip trie]
  (let [b (calc-bounds (zipcodes-from-trie (get-in trie partial-zip)))]
    (if (and (seq partial-zip)
             (or (= (first b) (second b))
                 (= (nth b 2) (last b))
                 (= (first b) (nth b 2)) 
                 (= (second b) (last b))))
      (find-bounds (butlast partial-zip) trie)
      b)))

(defn preserve-aspect-ratio
  "Adjusts one dimension of `bounding-box` to preserve
  `aspect-ratio`. Returns a map describing the outer bounds."
  [[min-lat max-lat min-long max-long] aspect-ratio]
  (let [bounds-aspect (/ (- max-long min-long) (- max-lat min-lat))]
    (cond (> bounds-aspect aspect-ratio)
          (let [mid-lat  (/ (+ max-lat min-lat) 2)
                span-lat (* (/ (- max-long min-long)
                               canvas-width) canvas-height)]
            [(- mid-lat (/ span-lat 2)) (+ mid-lat (/ span-lat 2))
             min-long max-long])
          (< bounds-aspect aspect-ratio)
          (let [mid-long  (/ (+ max-long min-long) 2)
                span-long (* (/ (- max-lat min-lat)
                                canvas-height) canvas-width)]
            [min-lat max-lat
             (- mid-long (/ span-long 2)) (+ mid-long (/ span-long 2))])
          :else [min-lat max-lat min-long max-long])))

(defn lat-long-to-point
  "Returns a vector containing the on-screen x/y position of this
  point's latitude/longitude based on the supplied bounds."
  [{:keys [longitude latitude]} [min-lat max-lat min-long max-long]]
  (vector (map-range longitude min-long max-long 30 (- canvas-width 30))
          (map-range latitude  min-lat max-lat (- canvas-height 20) 20)))

(defn render-zips 
  "Render `zips` within `bounds` using `color`."
  [zips bounds color]
  (doseq [zip zips]
    (let [[x y] (lat-long-to-point zip bounds)]
      (set-pixel x y color))))

;; Grabbed from clojure.core.incubator
;; https://github.com/clojure/core.incubator/blob/master/src/main/clojure/clojure/core/incubator.clj#L56
(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defn center-on-zip
  "Adjusts all dimensions of `bounding-box` to center on `chosen-zip`.
  Returns a map describing the outer bounds."
  [chosen-zip [min-lat max-lat min-long max-long]]
  (let [span-lat (- max-lat min-lat)
        span-long (- max-long min-long)]
    [(- (chosen-zip :latitude) (/ span-lat 2))
     (+ (chosen-zip :latitude) (/ span-lat 2))
     (- (chosen-zip :longitude) (/ span-long 2))
     (+ (chosen-zip :longitude) (/ span-long 2))]))

(defn draw-zips
  "Given 0-5 digits, draw a map of matching zipcodes, highlighting matches"
  [inputted-zip bounds]
  (let [partial-zip (vec inputted-zip)
        unmatched-zips (zipcodes-from-trie (dissoc-in zip-trie partial-zip))]
    (if (empty? inputted-zip)
      (render-zips unmatched-zips bounds (@palette :dormant))
      (do (render-zips unmatched-zips bounds (@palette :unhighlight))
          (render-zips @matched-zips bounds (@palette :highlight))))

    (when (and (= 5 (count partial-zip)) (= 1 (count @matched-zips)))
      (let [exact-match (first @matched-zips)
            [x y] (lat-long-to-point exact-match bounds)]
        (draw-zip-detail (exact-match :zip) (exact-match :desc) x y)))
    
    (draw-instructions (if (and (empty? @matched-zips) (seq inputted-zip))
                         (@palette :bad) (@palette :highlight)))))

(defn draw []
  (background (hex-to-color "#333333"))
  (draw-zips @typed-chars (first @bounds-queue))
  (if (> (count @bounds-queue) 1)
    (swap! bounds-queue #(vec (rest %)))))

(defn load-bounds-queue
  "Populate `@bounds-queue` with midpoint bounds on the way to
   calculated destination `bounds`"
  []
  (let [partial-zip (vec @typed-chars)
        zip-matches (if (empty? partial-zip) []
                        (zipcodes-from-trie (get-in zip-trie partial-zip)))
        bounds (preserve-aspect-ratio
                (cond (not @zoom-enabled)
                      (calc-bounds (zipcodes-from-trie zip-trie))
                      (empty? zip-matches)
                      (find-bounds (butlast partial-zip) zip-trie)
                      (= 1 (count zip-matches))
                      (center-on-zip (first zip-matches)
                                     (find-bounds partial-zip zip-trie))
                      :else (find-bounds partial-zip zip-trie))
                aspect-ratio)        
        start (last @bounds-queue)
        stop  bounds

        ;; TODO choose better easing method -- linear is jarring.
        midpoints (mapv (fn [step] (map #(lerp %1 %2 step) start stop))
                        (range 1/10 11/10 1/10))]

    (reset! matched-zips zip-matches) 
    (when (not= start stop)
      (reset! bounds-queue midpoints))))

(def deletion-key? #{\backspace}) ;; TODO test this key on a Windows machine
(def number-key? (set (map char (range 48 58))))

(defn key-handler
  "Manage keyboard input of 0 to 5 digits"
  []
  (let [key (if (= processing.core.PConstants/CODED (int (raw-key)))
              (key-code) (raw-key))] 
    (cond (and (deletion-key? key) (> (count @typed-chars) 0))
          (swap! typed-chars #(apply str (butlast %)))
          (and (number-key? key) (< (count @typed-chars) 5))
          (swap! typed-chars #(str % key))))
  (load-bounds-queue))

(defn toggle-zoom []
  (when (and (< (mouse-x) (- canvas-width 35))
             (> (mouse-x) (- canvas-width 45 (text-width "zoom")))
             (> (- canvas-height 30) (mouse-y) (- canvas-height 55)))
    (reset! zoom-enabled (not @zoom-enabled))
    (load-bounds-queue)))

(defsketch zips
  :title "Zip codes with animated zooming"
  :setup setup
  :draw draw
  :size [canvas-width canvas-height]
  :renderer :p2d
  :key-pressed key-handler
  :mouse-released toggle-zoom)
