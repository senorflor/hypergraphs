(ns hypergraphs.utils
  (:require [clojure.set :as s]
            [clojure.math.combinatorics :as c]
            [hypergraphs.core :as hgc]))

(defn- is-path?
  "An edgelist is a path if there is some element in common between
  each pair of adjacent hyperedges, n1 is only in the first path, and
  n2 is only in the last. We assume all incoming edgelists are length
  two or greater."
  [hg n1 n2 edgelist]
  (and (= [(first edgelist)]
          (filter #(contains? % n1) edgelist))
       (= [(last edgelist)]
          (filter #(contains? % n2) edgelist))
       (every? not-empty (map (partial apply s/intersection)
                              (partition 2 1 edgelist)))))

(defn naive-paths
  "Find the paths between two nodes in a hypergraph, by increasing length"
  [hg n1 n2]
  (let [starts (hgc/out-edges hg n1)
        ends (hgc/out-edges hg n2)]
    (->> (c/subsets (hgc/edges hg))
         (rest) ;; to deal with empty subset
         (mapcat c/permutations)
         (filter #(and (contains? starts (first %))
                       (contains? ends (last %))))
         (filter (partial is-path? hg n1 n2)))))
