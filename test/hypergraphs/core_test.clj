(ns hypergraphs.core-test
  (:require [clojure.test :refer :all]
            [hypergraphs.core :refer :all]
            [clojure.math.combinatorics :as comb]))

(deftest NativeHypergraph-works
  (let [node-set #{1 2 3 4}
        disjoint-hg     (->NativeHypergraph
                         node-set
                         #{#{1 2}
                           #{3 4}})
        complete-hg     (->NativeHypergraph
                         node-set
                         (->> (comb/subsets node-set)
                              (map set)
                              (remove empty?)))
        pi              0.5
        random-edge-set (->> (comb/subsets node-set)
                             (filter #(do % (< (rand) pi)))
                             (map set)
                             (remove empty?))
        random-hg       (->NativeHypergraph
                         node-set
                         random-edge-set)]
    (testing "nodes works on a NativeHypergraph"
      (is (= node-set
             (nodes disjoint-hg)
             (nodes complete-hg)
             (nodes random-hg))))
    (testing "edges works on a NativeHypergraph"
      (are [expected actual] (= expected actual)
           random-edge-set   (edges random-hg)
           #{#{1 2} #{3 4}}  (edges disjoint-hg)))))
