(ns lab.todomvc
  (:require
   [clojure.string :as string]
   [devcards.core :as dc]
   [om.core :as om :include-macros true]
   [sablono.core :as sab :include-macros true]
   [rum.core :as rum])
  (:require-macros
   [devcards.core :refer [defcard defcard-doc deftest]]))

(defn pass-in-atom-value [component args]
  (fn [data owner]
    (apply component (concat [@data] args))))

(defn table
  [rows]
  (sab/html
   (into [:table]
          (for [row rows]
            (into [:tr {:key row}]
                   (for [col row]
                     [:td {:key col} col]))))))

(defcard
  "# Todo MVC
  Let's break TodoMVC down into the smallest entities we can. Obviously they shouldn't be too small;
  it should make conceptual sense.")

(defcard
  "## todo-item")

(defcard
  (table [["id" "number"]
          ["text" "string"]
          ["state" "keyword (active|editing|completed)"]]))

(rum/defc todo-item < rum/static
  [{:keys [todo-item/id todo-item/text todo-item/state]}]
  [:li {:key id
        :class (if (= state :active)
                   ""
                   (name state))}
   [:div.view
    [:input.toggle (cond-> {:type "checkbox"}
                     (= state :completed) (assoc :checked "checked"))]
    [:label text]
    [:button {:class "button destroy"}]]
   [:input.edit  {:value text}]])

(rum/defc todo-item-wrapper < rum/static [todo]
  [:section.todapp
   [:section.main
    [:ul.todo-list
     (todo-item todo)]]
   [:footer.footer]])

(defcard todo-item-completed
  (pass-in-atom-value todo-item-wrapper [])
  {:todo-item/id 1
   :todo-item/text "Become Batman."
   :todo-item/state :completed}
  {:inspect-data true})

(defcard todo-item-editing
  (pass-in-atom-value todo-item-wrapper [])
  {:todo-item/id 1
   :todo-item/text "Learn the one arm handstand."
   :todo-item/state :editing}
  {:inspect-data true})

(defcard todo-item-active
  (pass-in-atom-value todo-item-wrapper [])
  {:todo-item/id 1
   :todo-item/text "Do a backflip!"
   :todo-item/state :active}
  {:inspect-data true})

(defn keep-todo? [filter-type {:keys [todo-item/state]}]
  (case filter-type
    :completed
    (= state :completed)
    :active
    (= state :active)
    true))

(rum/defc todo-list < rum/static [{:keys [todo-list/id todo-list/show todo-list/todos]}]
  [:ul.todo-list
   (for [todo (filter (partial keep-todo? show) todos)]
     (todo-item todo))])

(rum/defc todo-list-wrapper < rum/static [data]
  [:section.todapp
   [:section.main
    (todo-list data)]
   [:footer.footer]])

(defcard
  "## todo-item-list
   What about the todo-item-list? That's the parent container for todo-items. It needs to be
   able to filter todo-items based on whether they have been completed or not, or simply show
   all todo-items.")

(defcard
  (table [["id" "number"]
          ["show" "keyword (all|completed|active)"]
          ["todos" "[todo-item ...]"]]))


(def sentences
  ["A theory wets the carriage with a burned consumer."
   "The grateful tool thanks the genetics after the oldest northern."
   "Its substance stirs against the witch!"
   "Why does the leather hum?"
   "After the offensive culprit chooses a brief variance."
   "The shock venture calculates the jargon before the null buffer."
   "Become Batman."
   "Learn the one arm handstand."
   "Do a backflip!"])

(defn random-todo-item [id]
  {:todo-item/id id
   :todo-item/text (rand-nth sentences)
   :todo-item/state (rand-nth [:completed :active])})

(defn random-todo-items [n]
  (let [r (range n)
        todos (->> r
                   (map random-todo-item)
                   (into []))]
    (assoc-in todos [(rand-nth r) :todo-item/state] :editing)))

(defn no-todos-with-state [state todos]
  (->> todos
       (filter #(= (:todo-item/state %) state))
       count))

(defn todo-counts [todos]
  {:active (no-todos-with-state :active todos)
   :completed (no-todos-with-state :completed todos)
   :editing (no-todos-with-state :editing todos)})

(def todo-list-data
  (let [todos (random-todo-items 10)]
    (-> {:todo-list/id 1
         :todo-list/show :all
         :todo-list/todos todos}
        (assoc :todo-counts (todo-counts todos)))))

(defcard
  "shared data"
  todo-list-data)

(defcard todo-list-active
  (pass-in-atom-value todo-list-wrapper [])
  (assoc todo-list-data :todo-list/show :active))

(defcard todo-list-completed
  (pass-in-atom-value todo-list-wrapper [])
  (assoc todo-list-data :todo-list/show :completed))

(defcard todo-list-all
  (pass-in-atom-value todo-list-wrapper [])
  (assoc todo-list-data :todo-list/show :all))

(rum/defc new-todo < rum/static [{:keys [new-todo/text]}]
  [:input.new-todo {:placeholder "What needs to be done?"
                    :autofocus true
                    :value text}])

(defcard
  "## new-todo")

(defcard
  (table [["id" "number"]
          ["text" "string"]]))

(defcard new-todo-blank
  (pass-in-atom-value new-todo [])
  {:new-todo/id 1
   :new-todo/text ""}
  {:inspect-data true})

(defcard new-todo-really-long
  (pass-in-atom-value new-todo [])
  {:new-todo/id 1
   :new-todo/text
   "This is going to be a really long todo entry, it's going to span quite a few words."}
  {:inspect-data true})

(rum/defc toggle-all-button < rum/static [{:keys [toggle-all-button/all-todos-completed?]}]
  [:div
   [:input.toggle-all (cond-> {:type "checkbox"}
                        all-todos-completed? (assoc :checked "checked"))]
   [:label {:for "toggle-all"} "Mark all complete"]])

(rum/defc toggle-all-button-wrapper < rum/static [data]
  [:section.todoapp
   [:section.main
    (toggle-all-button data)]])

(defcard
  "## toggle-all-button")

(defcard
  (table [["id" "number"]
          ["all-todos-completed?" "boolean"]]))

(defcard toggle-all-button-off
  (pass-in-atom-value toggle-all-button-wrapper [])
  {:toggle-all-button/id 1
   :toggle-all-button/all-todos-completed? false}
  {:inspect-data true})

(defcard toggle-all-button-on
  (pass-in-atom-value toggle-all-button-wrapper [])
  {:toggle-all-button/id 1
   :toggle-all-button/all-todos-completed? true}
  {:inspect-data true})


(rum/defc filter-item < rum/static [{:keys [filter-item/label filter-item/url filter-item/selected?]}]
  [:li [:a {:href url
            :class (when selected? "selected")} (-> label name string/capitalize)]])

(rum/defc filter-item-wrapper < rum/static [data]
  [:section.todoapp
   [:section.footer
    [:ul.filters
     (filter-item data)]]])

(defcard
  "## filter-item")

(defcard
  (table [["id" "number"]
          ["url" "string"]
          ["label" "keyword"]
          ["selected?" "boolean"]]))

(defcard filter-item-all
  (pass-in-atom-value filter-item-wrapper [])
  {:filter-item/id 1
   :filter-item/url "#!/all"
   :filter-item/label :all
   :filter-item/selected? true}
  {:inspect-data true})

(defcard filter-item-all-not-selected
  (pass-in-atom-value filter-item-wrapper [])
  {:filter-item/id 1
   :filter-item/url "#!/all"
   :filter-item/label :all
   :filter-item/selected? false}
  {:inspect-data true})

(defcard filter-item-active
  (pass-in-atom-value filter-item-wrapper [])
  {:filter-item/id 1
   :filter-item/url "#!/active"
   :filter-item/label :active
   :filter-item/selected? true}
  {:inspect-data true})

(defcard filter-item-completed
  (pass-in-atom-value filter-item-wrapper [])
  {:filter-item/id 1
   :filter-item/url "#!/completed"
   :filter-item/label :completed
   :filter-item/selected? true}
  {:inspect-data true})

(rum/defc filter-item-list < rum/static [{:keys [filter-item-list/filters]}]
  [:ul.filters
   (for [filter filters]
     (filter-item filter))])

(rum/defc filter-item-list-wrapper < rum/static [data]
  [:section.todapp
   [:section.footer
    (filter-item-list data)]])

(defcard
  "## todo-filter-item-list
  This doesn't actually need to hold any special state, since its merely a container for filter-items.")

(defcard
  (table [["id" "number"]
          ["filters" "[filter-item ...]"]]))

(def filter-item-list-data
  {:filter-item-list/id 1
   :filter-item-list/filters [{:filter-item/id 1
                               :filter-item/label :all
                               :filter-item/selected? true}
                              {:filter-item/id 2
                               :filter-item/label :active
                               :filter-item/selected? false}
                              {:filter-item/id 3
                               :filter-item/label :completed
                               :filter-item/selected? false}]})

;; styles seem to get messed up for filter-item-lists on mobile with inspect-data true
;; hence the extra cards to show the state
(defcard filter-item-list-all-selected
  (pass-in-atom-value filter-item-list-wrapper [])
  filter-item-list-data)

(defcard
  ""
  filter-item-list-data)

(defcard filter-item-list-active-selected
  (pass-in-atom-value filter-item-list-wrapper [])
  (-> filter-item-list-data
      (assoc-in [:filter-item-list/filters 0 :filter-item/selected?] false)
      (assoc-in [:filter-item-list/filters 1 :filter-item/selected?] true)))

(defcard
  ""
  (-> filter-item-list-data
      (assoc-in [:filter-item-list/filters 0 :filter-item/selected?] false)
      (assoc-in [:filter-item-list/filters 1 :filter-item/selected?] true)))

(defcard filter-item-list-completed-selected
  (pass-in-atom-value filter-item-list-wrapper [])
  (-> filter-item-list-data
      (assoc-in [:filter-item-list/filters 0 :filter-item/selected?] false)
      (assoc-in [:filter-item-list/filters 2 :filter-item/selected?] true)))

(defcard
  ""
  (-> filter-item-list-data
      (assoc-in [:filter-item-list/filters 0 :filter-item/selected?] false)
      (assoc-in [:filter-item-list/filters 2 :filter-item/selected?] true)))

(defn pluralize-items-left [n]
  (case n
    0 "No items left"
    1 " item left"
    " items left"))

(rum/defc items-left-counter < rum/static [{:keys [items-left-counter/todos-left]}]
  [:span.todo-count
   (when (> todos-left 0) [:strong todos-left]) (pluralize-items-left todos-left)])

(rum/defc items-left-counter-wrapper < rum/static [data]
  [:section.todapp
   [:section.footer
    (items-left-counter data)]])

(defcard
  "## items-left-counter
  Requires proper pluralisation.")

(defcard
  (table [["id" "number"]
          ["todos-left" "number"]]))

(defcard items-left-counter-for-2-items
  (pass-in-atom-value items-left-counter-wrapper [])
  {:items-left-counter/id 1
   :items-left-counter/todos-left 2}
  {:inspect-data true})

(defcard items-left-counter-for-1-item
  (pass-in-atom-value items-left-counter-wrapper [])
  {:items-left-counter/id 1
   :items-left-counter/todos-left 1}
  {:inspect-data true})

(defcard items-left-counter-for-no-items
  (pass-in-atom-value items-left-counter-wrapper [])
  {:items-left-counter/id 1
   :items-left-counter/todos-left 0}
  {:inspect-data true})

(rum/defc clear-completed-button < rum/static
  [{:keys [clear-completed-button/completed-todos-exist?]}]
  (when completed-todos-exist?
    [:button.button {:class "clear-completed"} "Clear completed"]))

(rum/defc clear-completed-button-wrapper < rum/static [data]
  [:section.todapp
   [:section.footer
    (clear-completed-button data)]])

(defcard
  "## clear-completed-button ")

(defcard
  (table [["id" "number"]
          ["completed-todos-exist?" "boolean"]]))

(defcard clear-completed-button-showing
  (pass-in-atom-value clear-completed-button-wrapper [])
  {:clear-completed-button/id 1
   :clear-completed-button/completed-todos-exist? true}
  {:inspect-data true})

(defcard clear-completed-button-hidden
  (pass-in-atom-value clear-completed-button-wrapper [])
  {:clear-completed-button/id 1
   :clear-completed-button/completed-todos-exist? false}
  {:inspect-data true})
