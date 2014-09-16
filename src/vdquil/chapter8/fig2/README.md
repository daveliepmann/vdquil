Figure 2 in Chapter 8 of Fry's *Visualizing Data*
====

An attempt to translate Fry's Processing code into Quil by Marshall
Abrams.  

"Rewritten" would be a better term than "translated", since
rendering Fry's force layout algorithm in purely functional style
required quite a bit of reorganization.  I also made some trivial changes--e.g.
to background color.

The network visualization method used by Fry requires data to be passed
from `setup` to `draw`.  One way to do this is to store the data in a
Clojure atom.  That can work the standard `draw`/`setup` scheme.
Instead, I used Quil's "functional mode".  In this scheme, `setup`
returns a state object that Quil then passes to `update`.  Quil then
causes an updated state object to passed to `draw`, to cause the graphic
to be displayed or modified.  It also passes the state object back to
`update`, and a new cycle begins.

Usage: `lein repl`, and then:

`(use 'vdquil.chapter8.fig2.graph)`
