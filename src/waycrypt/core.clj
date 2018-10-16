(ns waycrypt.core
  (:require [byte-transforms :as bt]
            [byte-streams :as bs]    
            [pyro.printer :as printer]
            [octet.core :as buf])      
  (:gen-class))

;; TODO
; 1. password -> hash -> seed = long hash * len?
; 2. reproduce in cljs
; 3. appify

(printer/swap-stacktrace-engine!)
(defn rand-gen-with-seed [seed]
  (java.util.Random. seed))
(def rng (atom (rand-gen-with-seed 1))) ; some default
(defn set-rng [seed] (reset! rng (rand-gen-with-seed seed)))
(defn rnd-int [n] 
  (.nextInt @rng n))

(defn random-descending-seq [length]
  (reset! rng (rand-gen-with-seed 1))
  (mapv (fn [i] 
          (rnd-int i))
    (range length 0 -1)))

(defn random-set [seed length]
    (set-rng seed)
    { :shuffle (random-descending-seq length)
      :hash (mapv   (fn [i] 
                      (rnd-int 65535))
                    (range length))})

(defn hash-string [s rand-set]
  (let [  chars (into [] s)
          hash-rand-arr (:hash rand-set)
          hashed  (mapv (fn [i]
                          (char (bit-xor  (int (nth chars i)) 
                                          (nth hash-rand-arr i))))
                    (range (count s)))]
      (apply str hashed)))

(defn vec-extract
  "remove elem in coll"
  [coll pos]
  [(nth coll pos) (vec (concat (subvec coll 0 pos) (subvec coll (inc pos))))])

(defn vec-insert
  "insert elem into coll"
  [coll pos item]
  (vec (concat (subvec coll 0 pos) [item] (subvec coll pos))))

(defn mix-string [s seed]
  (let [rs (random-set seed (count s))
        hashed (hash-string s rs)]
    (loop [arr (into [] hashed) 
           result []
           the-seq (:shuffle rs)]
      (if (empty? arr)
        (apply str result)
        (let 
          [ [c remainder] (vec-extract arr (first the-seq))] 
          (recur remainder (conj result c) (rest the-seq)))))))

(defn unmix-string [s seed]
  (let [rs (random-set seed (count s))]
    (loop [arr (vec (reverse (into [] s))) 
           result []
           the-seq (reverse (:shuffle rs))]
      (if (empty? arr)
        (hash-string (apply str result) rs)  ; unhash the result
        (let [next-result (vec-insert result (first the-seq) (first arr))]
          (recur (rest arr) next-result (rest the-seq)))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
