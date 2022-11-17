package controllers;

import engine.Car;
import engine.Game;
import engine.Vector;

/**
 *
 * @author santi
 */
public abstract class Controller {
    /*
        commands is an array with three components:
        - the desired "STEER" (-1 to +1)
        - the desired "THROTTLE" (0 to +1)
        - the deired "BRAKE" (0 to +1)
    */
    public static final int VARIABLE_STEERING = 0;
    public static final int VARIABLE_THROTTLE = 1;
    public static final int VARIABLE_BRAKE = 2;

    public static final int MAX_ACCELERATION = 8;

    public abstract void update(Car subject, Game game, double delta_t, double controlVariables[]);

    public void motorControl(Car subject, Vector target, double controlVariables[]) {
        Vector cur = new Vector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle()));

        Vector right = new Vector((cur.y * -1), cur.x);

        //Calculates the throttle
        if(cur.dot(target) > 0) {
            controlVariables[VARIABLE_THROTTLE] = 1;
        } else if(cur.dot(target) < -1) {
            controlVariables[VARIABLE_BRAKE] = 1;
        }

        //Calculate steering
        if(right.dot(target) > 0) {
            controlVariables[VARIABLE_STEERING] = right.dot(target)/2;
        } else if(right.dot(target) < 0) {
            controlVariables[VARIABLE_STEERING] = right.dot(target)/2;
        }
    }
}