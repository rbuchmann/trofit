
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.rbuchmann/trofit.svg)](https://clojars.org/org.clojars.rbuchmann/trofit)

# trofit

A simple macro to make defining the re-frame boilerplate easier, while not adding too much magic. `event-db` and `sub` should be pretty straightforward, `event-fx` apply the function to the coeffects with the db replaced with just the subtree, and substitute the result back at the right place of the full tree if there is a `:db` key in the returned map.

## Usage

``` clojure
(require '[trofit.core :refer [defsubtree]])


(defsubtree [:metadata]
  :event-db {::set-prop (fn [db prop value] (assoc db prop value))}
  :event-fx {::metadata-save-button-clicked (fn [{:keys [db]} id]
                                              {:db             (assoc db :dirty? false)
                                               :store-metadata (merge db
                                                                      {:id id})})}
  :sub {::metadata (fn [db] (select-keys db [:title :description :tags :id]))})

;; This translates to the following re-frame code:

(re-frame.core/reg-event-db
 ::set-prop
 (fn [db [_ prop value]]
   (assoc-in db [:metadata prop] value)))

(re-frame.core/reg-event-fx
 ::metadata-save-button-clicked
 (fn [{:keys [db]} [_ id]]
   {:db             (assoc-in db [:metadata :dirty?] false)
    :store-metadata (merge (:metadata db)
                           {:id id})}))

(re-frame.core/reg-sub
 ::metadata
 (fn [db _]
   (select-keys (:metadata db) [:title :description :tags :id])))
```

The difference doesn't seem like much, but it adds up, and it also really helps with keeping track which of your code modifies which part of the tree, as well as making refactoring and moving state to different subtrees easier.

## License

Copyright © 2022 Rasmus Buchmann

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
