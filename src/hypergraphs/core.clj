(ns hypergraphs.core
  (:require [clojure.set :as s]))

(defprotocol Hypergraph
  "Some functions we should be able to call over a Hypergraph"
  (nodes [hg] "Return the values connected by the hypergraph")
  (edges [hg] "Return the subsets of directly adjacent nodes, i.e. hyperedges")
  (neighbors [hg n] "Return the neighbors of n in the hypergraph")
  (has-node? [hg n] "Is the value a node in the hypergraph?")
  (out-edges [hg n] "Returns the edges incident upon n")
  (adjacent? [hg ns] "Are the values included in one hyperedge?"))

;; Let's implement Hypergraph it with a record
(defrecord NativeHypergraph [my-nodes my-edges]
  Hypergraph
  ; Primitive operations
  (nodes [hg] my-nodes)
  (edges [hg] my-edges)
  (has-node? [hg n] (contains? my-nodes n))
  (out-edges [hg n] (into #{} (filter #(contains? % n) my-edges)))
  (adjacent? [hg ns] (some (partial s/subset? ns) my-edges))
  ; Some derived functions
  (neighbors [hg n] (apply s/union (out-edges hg n))))

;; Need to read
;; https://pragprog.com/book/cjclojure/mastering-clojure-macros; until
;; then, this will have to do
(defmacro if-hg
  ([v body]
     `(if-hg ~v ~body (throw (Exception. "That ain't no hypergraph"))))
  ([v yes no]
     `(if (and (= 2 (count ~v))
                    (set? (first ~v))
                    (set? (second ~v))
                    (every? set? (second ~v)))
        ~yes
        ~no)))

;; Here's a literal syntax too
(extend clojure.lang.PersistentVector
  Hypergraph
  {:nodes (fn [v] (if-hg v (first v)))
   :edges (fn [v] (if-hg v (second v)))
   :out-edges (fn [v n] (into #{} (filter #(contains? % n) (second v))))
   :has-node? (fn [v n] (if-hg v (contains? (first v) n)))
   :adjacent? (fn [v ns] (if-hg v (some (partial s/subset? ns) (second v))))
   :neighbors (fn [v n] (if-hg v (apply s/union (out-edges v n))))})
