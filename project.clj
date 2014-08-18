(defproject vdquil "0.1.1-SNAPSHOT"
  :description "Ben Fry's *Visualizing Data* exercises (in Quil!)"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["treemap"] ;; for Chapter 7's java interop work
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [quil "2.2.1"]
                 [clj-time "0.8.0"]]
  :jvm-opts ["-Xmx4g"]) 
