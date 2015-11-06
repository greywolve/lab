(ns lab.todomvc
  (:require
   [devcards.core :as dc]
   [om.core :as om :include-macros true]
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [devcards.core :refer [defcard defcard-doc deftest]]))

(defn table
  [rows]
  (sab/html
   [:table
    (for [row rows]
      [:tr
       (for [col row]
         [:td col])])]))

(defcard
  "# Todo MVC
  Let's break TodoMVC down into the smallest entities we can. Obviously they shouldn't be too small;
  it should make conceptual sense.")

(defcard
  "## todo-item")

(defcard
  " states"
  (sab/html [:ul.todo-list
             [:li.completed
              [:div.view
               [:input.toggle {:type "checkbox" :checked "checked"}]
               [:label "Become Batman"]
               [:button {:class "button destroy"}]]
              [:input.edit  {:value ""}]]
             [:li
              [:div.view
               [:input.toggle {:type "checkbox"}]
               [:label "Learn the one arm handstand"]
               [:button {:class "button destroy"}]]
              [:input.edit  {:value ""}]]]))

(defcard
  "entity"
  (table [["id" "number"]
          ["text" "string"]
          ["editing?" "boolean"]
          ["completed?" "boolean"]]))

(defcard
  "example entity instance"
  '{:todo/id 1
    :todo/text "Become Batman"
    :todo/completed? true})

(defcard
  "## todo-item-list
   What about the todo-item-list? That's the parent container for todo-items. It needs to be
   able to filter todo-items based on whether they have been completed or not, or simply show
   all todo-items.")

(defcard
  "entity"
  (table [["id" "number"]
          ["show" "keyword (all|completed|active)"]
          ["todos" "[todo-item ...]"]]))

(defcard
  "example entity instance"
  '{:todo-list/id 1
    :todo-list/show :all
    :todo-list/todos [{:todo/id 1
                       :todo/text "Become Batman"
                       :todo/completed? true}]})

(defcard
  "## new-todo")

(defcard
  "state"
  (sab/html [:input.new-todo {:placeholder "What needs to be done?" :autofocus true}]))

(defcard
  "entity"
  (table [["id" "number"]
          ["text" "string"]]))

(defcard
  "example entity instance"
  '{:new-todo/id 1
    :new-todo/text ""})

(defcard
  "## toggle-all-button")

(defcard
  "state"
  (sab/html [:div.todoapp
             [:div.main
              [:input.toggle-all {:type "checkbox"}]
              [:label {:for "toggle-all"} "Mark all complete"]]]))

(defcard
  "entity"
  (table [["id" "number"]
          ["all-todos-completed?" "boolean"]]))

(defcard
  "example entity instance"
  '{:toggle-all-button/id 1
    :toggle-all-button/all-todos-completed? false})

(defcard
  "## todo-filter-item")

(defcard
  "state"
  (sab/html [:div.footer
             [:ul.filters
              [:li [:a.selected {:href "#!/lab.todomvc"} "All"]]
              [:li [:a {:href "#!/lab.todomvc"} "Active"]]
              [:li [:a {:href "#!/lab.todomvc"} "Completed"]]]]))
(defcard
  "entity"
  (table [["id" "number"]
          ["name" "keyword"]
          ["selected?" "boolean"]]))
(defcard
  "example entity instance"
  '{:filter-item/id 1
    :filter-item/name :all
    :filter-item/selected? true})

(defcard
  "## todo-filter-item-list
  This doesn't actually need to hold any special state, since its merely a container for filter-items.")

(defcard
  "entity"
  (table [["id" "number"]
          ["filters" "[filter-item ...]"]]))

(defcard
  "example entity instance"
  '{:filter-item-list/id 1
    :filter-item-list/filters [{:filter-item/id 1
                                :filter-item/name :all
                                :filter-item/selected? true}
                               {:filter-item/id 2
                                :filter-item/name :active
                                :filter-item/selected? false}
                               {:filter-item/id 3
                                :filter-item/name :completed
                                :filter-item/selected? false}]})

(defcard
  "## items-left-counter
  Requires proper pluralisation.")

(defcard
  (sab/html [:section.footer
             [:span.todo-count [:strong 1] " item left"]]))
(defcard
  (sab/html [:section.footer
             [:span.todo-count [:strong 2] " items left"]]))
(defcard
  (sab/html [:section.footer
             [:span.todo-count "No items left"]]))
(defcard
  "entity"
  (table [["id" "number"]
          ["todos-left" "number"]]))
(defcard
  "example entity instance"
  '{:items-left-counter/id 1
    :items-left-counter/todos-left 2})

(defcard
  "## clear-completed-button ")

(defcard
  (sab/html [:section.footer
             [:button.button {:class "clear-completed"} "Clear completed"]]))

(defcard
  "entity"
  (table [["id" "number"]
          ["completed-todos-exist?" "boolean"]]))

(defcard
  "example entity instance"
  '{:clear-completed-button/id 1
    :clear-completed-button/completed-todos-exist? true})
