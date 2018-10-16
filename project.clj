(defproject waycrypt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [ [org.clojure/clojure "1.8.0"]
                  [org.clojure/tools.namespace "0.2.11"]
                  [byte-streams "0.2.4"]
                  [funcool/octet "1.1.1"]
                  [venantius/pyro "0.1.2"]
                  [byte-transforms "0.1.4"]]
  :main ^:skip-aot waycrypt.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
