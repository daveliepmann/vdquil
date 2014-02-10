vdquil - Visualizing Data (in Quil!)
======

[Ben Fry](http://benfry.com/)'s
[Visualizing Data](http://www.amazon.com/Visualizing-Data-Explaining-Processing-Environment/dp/0596514557)
has been a lot of fun to work through. However, as an experienced
programmer familiar with Java syntax and concepts, and having already
played with Processing on my own before reading the book, I found that
doing the exercises as-is wasn't challenging enough. I decided to have
some fun by doing the Processing exercises in Clojure using the most
excellent [Quil](https://github.com/quil/quil) library.

Similar work has been done with Matt Pearson's [Generative Art](https://github.com/quil/quil/blob/master/examples/gen_art/README.md).

My goals are to exactly match the Processing code's output and to write idiomatic Clojure. If you find an error (or just something non-idiomatic to Clojure) please contact me via github or via email (first name, period, last name, gmail).

### Running the exercises
To see these sketches in action, first walk through the preprocessing, then data, then figures. For example, for Chapter 5:

 1. Evaluate the expressions in _ch5preprocessing.clj_ first. (The
    team images and figure 8 data are already included in the git
    repository, so you can skip this step if you cloned the
    `/resources folder of this repo.) Change sample data (e.g. dates)
    as desired. Note that the second section (grabbing data across a
    date range) is not needed until figure 8.
 1. Evaluate the expressions in _ch5data.clj_.
 1. Evaluate the expressions in _figure5-6.clj_. If you want to
    compare different versions of the sketch, comment out the marked
    code blocks.

Compare my code output to that of the original by downloading
[the Processing source](http://benfry.com/writing/archives/3) and
running the examples on your machine. If you have any questions drop
me a line.

### Screenshots!
That's all well and good, but what if you just want to see what this
stuff looks like without the hassle of running the code yourself?
Well, that's too bad, because you're missing out on a lot of cool
interactivity. But here are some screenshots to hold you over.

**Chapter 3** - Plotting (arbitrary) data spatially:

![Screenshot of Chapter 3, figure 7](https://github.com/daveliepmann/vdquil/blob/master/src/vdquil/chapter3/ch3fig7.png?raw=true "Chapter 3 figure 7 screenshot")

**Chapter 4** - Graphing milk, tea, and coffee prices as a time series:

![Screenshot of Chapter 4, figure 14](https://github.com/daveliepmann/vdquil/blob/master/src/vdquil/chapter4/ch4fig14.png?raw=true "Chapter 4 figure 14 screenshot")

**Chapter 5** - Correlating separate win/loss and salary datasets for Major League Baseball teams:

![Screenshot of Chapter 5, figure 8](https://github.com/daveliepmann/vdquil/blob/master/src/vdquil/chapter5/ch5fig8.png?raw=true "Chapter 5 figure 8 screenshot")

**Chapter 6:** Mapping zipcodes as a scatterplot:
![Screenshot of Chapter 6, whole map](https://github.com/daveliepmann/vdquil/blob/master/src/vdquil/chapter6/ch6-USA.png?raw=true "Chapter 6 whole map screenshot")

![Screenshot of Chapter 6, zoomed in](https://github.com/daveliepmann/vdquil/blob/master/src/vdquil/chapter6/ch6-michigan.png?raw=true "Chapter 6 zoomed in screenshot")

**Chapter 7:** Treemapping word usage in Mark Twain's *Following the Equator*:
![Screenshot of Chapter 7, figure 2](https://github.com/daveliepmann/vdquil/blob/master/src/vdquil/chapter7/twain-word-treemap.png?raw=true
"Chapter 7, word usage in Twain's Following the Equator, displayed as
a treemap")

### Attribution
_This project is based on code and examples in Visualizing Data, First Edition by Ben Fry. Copyright 2008 Ben Fry, 9780596514556_
