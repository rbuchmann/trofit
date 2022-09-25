(defproject org.clojars.rbuchmann/trofit "0.1.0"
  :description "A simple macro to make defining the re-frame boilerplate more convenient"
  :url "https://github.com/rbuchmann/trofit"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]]
  :repl-options {:init-ns trofit.core}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]])
