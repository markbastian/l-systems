(defproject
  l-systems "0.1.0-SNAPSHOT"
  :description "Lindenmayer system code in Clojure"
  :url "https://github.com/markbastian/l-systems"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [quil "2.2.6"]]

  ;:main snake.launcher

  :jar-exclusions [#"\.swp|\.swo|\.DS_Store"]
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-cljsbuild "1.0.6"]
                             [org.clojure/clojurescript "0.0-3308"]]}
             :cljs {:plugins [[lein-cljsbuild "1.0.6"]] }}

  :source-paths ["src/clj" "src/cljc"]

  :clj {:builds [{ :source-paths ["src/clj" "src/cljc" "test"] }]}

  :cljsbuild {:builds [{ :source-paths ["src/cljs" "src/cljc"]
                        :compiler { :output-to "resources/public/js/lsystems.js"
                                   :optimizations :advanced
                                   :pretty-print true}}]})
