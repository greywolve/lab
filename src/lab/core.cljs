(ns lab.core
  (:require
   [devcards.core :as dc]
   [om.core :as om :include-macros true]
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [devcards.core :refer [defcard deftest]]))

(devcards.core/start-devcard-ui!)

(enable-console-print!)

(defcard first-card
  (sab/html [:div
             [:h1 "This is your first devcard!!"]]))

