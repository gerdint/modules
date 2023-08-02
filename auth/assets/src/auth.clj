(ns <<ns-name>>.web.middleware.auth
  (:require
   [buddy.auth.backends.session :as session]
   [buddy.auth :as auth]
   [buddy.auth.accessrules :as accessrules]
   [buddy.auth.middleware :as auth-middleware]))

(defn on-error [request _response]
  {:status 403
   :headers {"Content-Type" "text/plain"}
   :body (str "Access to " (:uri request) " is not authorized")})

(defn wrap-restricted [handler]
  (accessrules/restrict handler {:handler auth/authenticated?
                                 :on-error on-error}))

(defn wrap-auth [handler]
  (let [backend (session/session-backend)]
    (-> handler
        (auth-middleware/wrap-authentication backend)
        (auth-middleware/wrap-authorization backend))))
