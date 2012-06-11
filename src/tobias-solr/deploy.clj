(ns tobias-solr.deploy
  (:use [pallet.core :only [converge group-spec]])
  (:use [pallet.configure :only [compute-service]])
  (:use [pallet.phase :only [phase-fn]])
  (:use [pallet.crate.automated-admin-user :only [automated-admin-user]])
  (:use [pallet.crate.java :only [java]])
  (:use [pallet.action.rsync :only [rsync-directory]])
  (:use [pallet.action exec-script]))

(def solr-server
  (group-spec "solr-server"
              :phases {:bootstrap (phase-fn (automated-admin-user))
                       :configure (phase-fn
                                    (java :openjdk)
                                    (rsync-directory "solr" "/home/solr"))
                       :start (phase-fn
                                (exec-script* "screen -d -m -S solr \"cd /home/solr; java -jar start.jar\""))}
              :node-spec {:image {:os-family :ubuntu}
                          :hardware {:smallest true}
                          :network {:inbound-ports [22 8983]}}))

(defn start-solr-nodes [count]
  (converge {solr-server count} :compute (compute-service) :phases [:start]))

(defn list-solr-nodes []
  ())

(defn list-solr-hosts []
  ())