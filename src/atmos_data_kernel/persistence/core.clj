(ns atmos-data-kernel.persistence.core
  (:require [atmos-data-kernel.core :refer [update-map]]
            [korma.db :as sql]))

(def ^:private init-persistences {:mysql sql/defdb})

(def ^:private persistence-types {:mysql [sql/mysql
                                          "com.mysql.cj.jdbc.Driver"]})

(defn- persistence-dialect
  "Return korma persistence dialect"
  [persistence-type]
  (first (persistence-type persistence-types)))

(defn- db-class-name
  "Return persistence relational db class name"
  [persistence-type]
  (second (persistence-type persistence-types)))

(defn defpersistence
  "Define persistence device map"
  [persistence-type persistence-data]
  (let [persistence-fn (persistence-dialect persistence-type)
        classname (db-class-name persistence-type)
        persistence-data (case persistence-type
                           :mysql (update-map persistence-data
                                              :classname
                                              #(str classname %)))]
    (persistence-fn persistence-data)))

(defn init-persistence
  "Initialize the persistence device"
  [persistence-type persistence-definition]
  (let [init-fn (persistence-type init-persistences)]
    (init-fn persistence-definition)))

