# hypergraphs

Minimalist hypergraphs:

```clojure
(require '[clojure.math.combinatorics :as c] '[hypergraphs.core :as hg] '[hypergraphs.utils :as hg-utils])

;; This library uses sets extensively:
(def some-nodes #{1 2 3 4})

;; Here's a not-very-busy hypergraph. Each hyperedge is a set of 1 or
;; more nodes which are considered adjacent.
(def sparse-hg [some-nodes #{ #{1 2} #{2 3 4} }])

;; Here's a complete hypergraph; the set of hypernodes is the Powerset
;; of the nodeset (excluding the empty set)
(def complete-hg [some-nodes
                  (into #{} (->> (c/subsets some-nodes)
                                 (filter #(< 1 (count %)))
                                 (map set))])

(hg/nodes complete-hg)
;=> #{1 4 3 2}

(hg/edges complete-hg)
;=> #{#{4 3} #{1 4 3 2} #{4 3 2} #{1 4} #{4 2} #{1 3 2} #{1 3} #{1 2} #{1 4 3} #{3 2} #{1 4 2}}

(hg/neighbors complete-hg 1)
;=> #{1 4 3 2}

(hg/nodes sparse-hg)
;=> #{1 4 3 2}

(hg/edges sparse-hg)
;=> #{#{4 3} #{1 2}}

(hg/neighbors sparse-hg 1)
;=> #{1 2}

;; Let's find paths as a first exercise:

(hg/naive-paths sparse-hg 1 4)
;=> ((#{1 2} #{4 3 2}))
;; Yay!

(hg-utils/naive-paths complete-hg 1 4)
;=> ...
;; Go get some coffee. Time to figure out what good pathfinding algos
;; for hypergraphs look like...

## Usage

Not yet.

## TODO

Just about everything :)

## License

Copyright © 2014 Patrick Flor

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
