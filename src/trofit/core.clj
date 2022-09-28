(ns trofit.core)

;; TODO: Allow injecting other parts of the tree

(defmacro defsubtree [base & {:keys [event-db event-fx sub]}]
  (let [base-path (if (keyword? base)
                    [base]
                    base)]
    `(do
       ~@(for [[event-key f] event-db]
           `(re-frame.core/reg-event-db
             ~event-key
             (fn [db# [_# & args#]]
               (update-in db# ~base-path #(apply ~f % args#)))))
       ~@(for [[event-key f] event-fx]
           `(re-frame.core/reg-event-fx
             ~event-key
             (fn [cofx# [_# & args#]]
               (let [old-db# (:db cofx#)
                     {new-db# :db :as result#} (apply ~f (update cofx# :db get-in ~base-path) args#)]
                 (cond-> result#
                   new-db# (assoc :db (assoc-in old-db# ~base-path new-db#)))))))
       ~@(for [[sub-key f] sub]
           `(re-frame.core/reg-sub
             ~sub-key
             (fn [db# [_# & args#]]
               (apply ~f (get-in db# ~base-path) args#)))))))


(defsubtree [:metadata]
  :event-db {::set-prop (fn [db prop value] (assoc db prop value))}
  :event-fx {::metadata-save-button-clicked (fn [{:keys [db]} id]
                                              {:db             (assoc db :dirty? false)
                                               :store-metadata (merge db
                                                                      {:id id})})}
  :sub {::metadata (fn [db] (select-keys db [:title :description :tags :id]))})



(re-frame.core/reg-event-db
 ::set-prop
 (fn [db [_ prop value]]
   (update-in db [:metadata] assoc prop value)))

(re-frame.core/reg-event-fx
 ::metadata-save-button-clicked
 (fn [{:keys [db]} [_ id]]
   {:db             (update-in db [:metadata] assoc :dirty? false)
    :store-metadata (merge (get-in db [:metadata])
                           {:id id})}))

(re-frame.core/reg-sub
 ::metadata
 (fn [db _]
   (select-keys (:metadata db) [:title :description :tags :id])))
