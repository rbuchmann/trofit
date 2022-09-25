(ns trofit.core)

;; TODO: Allow injecting other parts of the tree

(defmacro defsubtree [base & {:keys [events fx subs]}]
  (let [base-path (if (keyword? base)
                    [base]
                    base)]
    `(do
       ~@(for [[event-key f] events]
           `(re-frame.core/reg-event-db
             ~event-key
             (fn [db# [_# & args#]]
               (update-in db# ~base-path #(apply ~f % args#)))))
       ~@(for [[event-key f] fx]
           `(re-frame.core/reg-event-fx
             ~event-key
             (fn [cofx# [_# & args#]]
               (let [old-db# (:db cofx#)
                     {new-db# :db :as result#} (apply ~f (update cofx# :db get-in ~base-path) args#)]
                 (cond-> result#
                   new-db# (assoc :db (assoc-in old-db# ~base-path new-db#)))))))
       ~@(for [[sub-key f] subs]
           `(re-frame.core/reg-sub
             ~sub-key
             (fn [db# [_# & args#]]
               (apply ~f (get-in db# ~base-path) args#)))))))
