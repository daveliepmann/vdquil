vdquil
======

Visualizing Data (in Quil!)
-

[Ben Fry](http://benfry.com/)'s [Visualizing Data](http://www.amazon.com/Visualizing-Data-Explaining-Processing-Environment/dp/0596514557) has been a lot of fun to work through. However, as a professional programmer familiar with Java syntax and concepts, I found that doing the exercises in Processing wasn't challenging. I decided to have some fun by doing the Processing exercises in Clojure using the most excellent [Quil](https://github.com/quil/quil).

Similar work has been done with Matt Pearson's [Generative Art](https://github.com/quil/quil/blob/master/examples/gen_art/README.md).

My goal is to match Processing code's output exactly and write idiomatic Clojure. If you find an error (or even something non-idiomatic to Clojure) please contact me on github or via email (first name, period, last name, gmail).

###Running the Exercises yourself
Walk through preprocessing steps first, then data, then figures. For example, for Chapter 5:

 1. Evaluate the expressions in _ch5preprocessing.clj_ first. Note that the team images and figure 8 data are already included in the repository, so you don't technically have to do any of this. Change sample data (e.g. dates) as desired. Note that the second section (grabbing data across a date range) is not needed to run the first few figures.
 1. Evaluate the expressions in _ch5data.clj_ next.
 1. Evaluate the figures, e.g. _figure5-6.clj_. If you want to compare different versions of the sketch, comment out the marked code blocks.
