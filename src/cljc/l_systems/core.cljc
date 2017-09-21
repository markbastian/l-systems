(ns l-systems.core
  (:require [quil.core :as q #?@(:cljs [:include-macros true])]
            [quil.middleware :as m]))

;http://www.kevs3d.co.uk/dev/lsystems/#
(def koch-curve
  { :rules { \F (seq "F+F-F-F+F") } :state [\F] :angle 60 :d 5})

(def sierpinski-triangle
  { :rules { \A (seq "+B-A-B+") \B (seq "-A+B+A-") } :state [\A] :angle 60 :d 5})

(def dragon-curve
  { :rules { \X (seq "X+YF+") \Y (seq "-FX−Y") } :state [\F \X] :angle 90 :d 5})

(def leaf
  { :rules { \X (seq "F−[[X]+X]+F[+FX]−X") \F (seq "FF")} :state [\X] :angle 25 :d 5})

(def thing
  { :rules { \F (seq "C0FF[C1-F++F][C2+F--F]C3++F--F")} :state [\F] :angle 27 :d 5})

(def thing1
  { :rules { \F (seq "C0FF-[C1-F+F+F]+[C2+F-F-F]")} :state [\F] :angle 22 :d 5})

(def thing2
  { :rules { \X (seq "X+YF+") \Y (seq "-FX-Y")} :state [\F \X] :angle 90 :d 12})

(defn process [c d a]
  (case c
    (\A \B \F) (do (q/line [0 0] [0 (- d)]) (q/translate 0 (- d)))
    \- (q/rotate (q/radians (- a)))
    \+ (q/rotate (q/radians a))
    \[ (q/push-matrix)
    \] (q/pop-matrix)
    nil))

(defn step [{:keys [state rules] :as s }]
  (-> s
      (assoc :state (flatten (map #(rules % %) state)))
      (update :d * 1.0)))

(defn setup []
  (assoc (nth (iterate step leaf) 5)
    :scale 1.0 :dx 0 :dy 0))

(defn zoom [state x]
  (update state :scale (fn [scale] ((if (pos? x) * /) scale 0.9))))

(defn pan [state {:keys [p-x x p-y y]}]
  (-> state
      (update :dx + (- x p-x))
      (update :dy + (- y p-y))))

(defn draw [{:keys [state d angle scale dx dy]}]
  (do
    (q/smooth)
    (q/stroke-cap :round)
    (q/stroke-join :round)
    (q/background 0 0 0)
    (q/fill 0 255 0)
    (q/stroke 0 255 0)
    (q/push-matrix)
    (q/translate dx dy)
    (q/scale scale)
    (doseq [c state] (process c d angle))
    (q/pop-matrix)))

(defn launch-sketch [{:keys[width height host]}]
  (q/sketch
    :title "L-Systems"
    :setup setup
    :settings #(do (q/smooth) (q/frame-rate 30))
    :draw draw
    :mouse-wheel zoom
    :mouse-dragged pan
    :middleware [m/fun-mode]
    :size [width height]
    #?@(:cljs [:host host])))

#?(:clj (launch-sketch { :width 400 :height 400 }))