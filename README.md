vdquil
======

Visualizing Data (in Quil!)
-

[Ben Fry](http://benfry.com/)'s [Visualizing Data](http://www.amazon.com/Visualizing-Data-Explaining-Processing-Environment/dp/0596514557) has been a lot of fun to work through. However, as an experienced programmer familiar with Java syntax and concepts, and having already played with Processing on my own before reading the book, I found that doing the exercises as-is wasn't challenging enough. I decided to have some fun by doing the Processing exercises in Clojure using the most excellent [Quil](https://github.com/quil/quil).

Similar work has been done with Matt Pearson's [Generative Art](https://github.com/quil/quil/blob/master/examples/gen_art/README.md).

My goals are to exactly match the Processing code's output and to write idiomatic Clojure. If you find an error (or even something non-idiomatic to Clojure) please contact me on github or via email (first name, period, last name, gmail).

###Running the Exercises yourself
To run these sketches yourself, first walk through the preprocessing, then data, then figures. For example, for Chapter 5:

 1. Evaluate the expressions in _ch5preprocessing.clj_ first. (The team images and figure 8 data are already included in the repository, so you can skip this step if you just want to see the output.) Change sample data (e.g. dates) as desired. Note that the second section (grabbing data across a date range) is not needed until figure 8.
 1. Evaluate the expressions in _ch5data.clj_.
 1. Evaluate the figures, e.g. _figure5-6.clj_. If you want to compare different versions of the sketch, comment out the marked code blocks.
