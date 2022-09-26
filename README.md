
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.rbuchmann/trofit.svg)](https://clojars.org/org.clojars.rbuchmann/trofit)

# trofit

A simple macro to make defining the re-frame boilerplate easier, while not adding too much magic. Events and subs should be pretty straightforward, fx will work by substituting the db for the subtree you selected, and put the modified result back at the right place of the full tree, if there is a `:db` key.

## Usage

``` clojure
(require '[trofit.core :refer [defsubtree]])

(defsubtree [:metadata]
  :events {::set-prop (fn [db prop value] (assoc db prop value))}
  :subs {::metadata (fn [db] (select-keys db [:title :description :tags :id]))}
  :fx {::metadata-save-button-clicked (fn [{:keys [db]} id]
                                       {:store-metadata db})})
```

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
