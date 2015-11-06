(ns lab.core
  (:require
   [lab.todomvc]
   [devcards.core :as dc]
   [om.core :as om :include-macros true]
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [devcards.core :refer [defcard deftest]]))

(devcards.core/start-devcard-ui!)

(enable-console-print!)
