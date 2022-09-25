(ns trofit.core
  (:require-macros trofit.core))

(defn map-to [k]
  (fn [db v]
    (assoc db k v)))

(defn sub-to [k]
  (fn [db]
    (get db k)))
