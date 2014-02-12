;; Ben Fry's Visualizing Data, Chapter 6, figure 4, with zoom
;; Converted from Processing to Clojure as an exercise by Dave Liepmann

(ns vdquil.chapter6.figure6-zoom
  (:use [quil.core]
        [vdquil.util])
  (:require [clojure.string :as string]))

(def canvas-height 453)
(def canvas-width 720)
(def aspect-ratio (/ canvas-width canvas-height))

;; First line of the data is record count / minX / maxX / minY / maxY
;; I just manually pulled it in--let's not overcomplicate things.
;; # 41556,-0.3667764,0.35192886,0.4181981,0.87044954

(def typed-chars (atom ""))

(def zoom-enabled (atom false))

(def palette (atom {}))

(defn create-zip
  "Given a line of zipcode data, create a zipcode data structure"
  [data]
  (let [[zip longitude latitude desc] (string/split data #"\t")]
    {:zip zip :longitude (read-string longitude)
     :latitude (read-string latitude) :desc desc}))

(def zipcodes
  (map create-zip (string/split-lines (slurp "data/zips-modified.tsv"))))

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
          (- x (+ 8 (text-width zip))) (+ x 8))
        (- y 4)))

(defn calc-map-bounds
  "Calculate the map bounds of a given set of matched zips.
   If a chosen-zip is supplied then center it."
  [matched-zips chosen-zip]
  (let [[min-lat max-lat min-long max-long]
        (if (and @zoom-enabled (seq matched-zips))
          (let [latitudes  (map :latitude matched-zips)
                longitudes (map :longitude matched-zips)]
            [(apply min latitudes) (apply max latitudes)
             (apply min longitudes) (apply max longitudes)])
          ;; Constants from 1st line of zips.tsv:
          [0.4181981 0.87044954 -0.3667764 0.35192886]) 

        [mid-x mid-y] (if (and @zoom-enabled (seq chosen-zip))
                        [(chosen-zip :longitude) (chosen-zip :latitude)]
                        [(/ (+ max-long min-long) 2)
                         (/ (+ max-lat min-lat) 2)])

        ;; Preserve aspect ratio
        span-aspect
        (if (or (and @zoom-enabled (seq chosen-zip))
                (= 0.0 (- max-long min-long) (- max-lat min-lat)))
          aspect-ratio 0)

        [span-x span-y]
        (let [w (* (/ (- max-lat min-lat) canvas-height) canvas-width)
              h (* (/ (- max-long min-long) canvas-width) canvas-height)]
          (if (and @zoom-enabled (seq chosen-zip)) [w h]
              [(if (<= span-aspect aspect-ratio) w nil)
               (if (> span-aspect aspect-ratio)  h nil)]))]

    {:span-x span-x :span-y span-y
     :min-x (if (nil? span-x) min-long (- mid-x (/ span-x 2)))
     :max-x (if (nil? span-x) max-long (+ mid-x (/ span-x 2)))
     :min-y (if (nil? span-y) min-lat (- mid-y (/ span-y 2)))
     :max-y (if (nil? span-y) max-lat (+ mid-y (/ span-y 2)))
     :x     [30 (- canvas-width 30)]
     :y     [(- canvas-height 20) 20]}))

(defn map-bounds
  "Recursively find the appropriate bounds for the set of zips
  corresponding to a given inputted-zip"
  [inputted-zip matched-zips chosen-zip]
  (let [zips (if (empty? inputted-zip) zipcodes
                 (let [zip-regex (re-pattern (str "^" inputted-zip))]
                   (map #(assoc % :match (if (re-find zip-regex (% :zip))
                                           :highlight :unhighlight))
                        zipcodes)))
        matched-zips (filter #(= :highlight (% :match)) zips)
        b (calc-map-bounds matched-zips chosen-zip)]
    (if (or (and @zoom-enabled (seq inputted-zip) (empty? matched-zips))
            (= (b :min-x) (b :max-x))
            (= (b :min-y) (b :max-y)))
      (map-bounds (subs inputted-zip 0 (if (= 0 (count inputted-zip))
                                         0 (- (count inputted-zip) 1)))
                  matched-zips chosen-zip)
      b)))

(defn draw-zips
  "Given 0-5 digits, draw a map of the matching zipcodes, highlighting matches"
  [inputted-zip]
  (let [zips (if (empty? inputted-zip) zipcodes
                 (let [zip-regex (re-pattern (str "^" inputted-zip))]
                   (map #(assoc % :match (if (re-find zip-regex (% :zip))
                                           :highlight :unhighlight)) zipcodes)))
        matched-zips (filter #(= :highlight (% :match)) zips)
        exact-match  (when (empty? (rest matched-zips)) (first matched-zips))
        bounds (map-bounds inputted-zip matched-zips exact-match)]
    
    (doseq [zip zips] 
      (let [x (map-range (zip :longitude) (bounds :min-x) (bounds :max-x)
                         (first (bounds :x)) (second (bounds :x)))
            y (map-range (zip :latitude) (bounds :min-y) (bounds :max-y)
                         (first (bounds :y)) (second (bounds :y)))]
        (if (and (< (- (first (bounds :x)) 30) x (+ 30 (second (bounds :x))))
                 (> (+ (first (bounds :y)) 20) y (- (second (bounds :y)) 20)))
          (if (seq exact-match)
            (set-pixel x y (@palette :unhighlight))
            (set-pixel x y (@palette (get zip :match :dormant)))))))

    (when exact-match
      (draw-zip-detail (exact-match :zip)
                       (exact-match :desc)
                       (map-range (exact-match :longitude)
                                  (bounds :min-x) (bounds :max-x)
                                  (first (bounds :x)) (second (bounds :x)))
                       (map-range (exact-match :latitude)
                                  (bounds :min-y) (bounds :max-y)
                                  (first (bounds :y)) (second (bounds :y)))))
    
    (draw-instructions (if (and (empty? matched-zips)
                                (seq inputted-zip))
                         (@palette :bad) (@palette :highlight)))))

(defn draw []
  (background (hex-to-color "#333333"))
  (draw-zips @typed-chars))

(def deletion-key? #{\backspace}) ;; TODO test this key on a Windows machine
(def number-key? (set (map char (range 48 58))))

(defn key-handler
  "Manage keyboard input of up to 5 digits"
  []
  (let [key (if (= processing.core.PConstants/CODED (int (raw-key)))
              (key-code) (raw-key))] 
    (cond (and (deletion-key? key) (> (count @typed-chars) 0))
          (swap! typed-chars #(subs % 0 (- (count @typed-chars) 1)))
          (and (number-key? key) (< (count @typed-chars) 5))
          (swap! typed-chars #(str % key)))))

(defn toggle-zoom []
  (when (and (< (mouse-x) (- canvas-width 35))
             (> (mouse-x) (- canvas-width 45 (text-width "zoom")))
             (> (- canvas-height 30) (mouse-y) (- canvas-height 55)))
    (reset! zoom-enabled (not @zoom-enabled))))

(defsketch zips
  :title "Zip codes with zooming (e.g. figure 6-4)"
  :setup setup
  :draw draw
  :size [canvas-width canvas-height]
  :renderer :p2d
  :key-pressed key-handler
  :mouse-released toggle-zoom)
