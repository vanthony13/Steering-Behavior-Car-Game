package controllers;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.Vector;

public class ArriveController extends Controller {
    public static final int key_accelerate = KeyEvent.VK_UP;
    public static final int key_brake = KeyEvent.VK_DOWN;
    public static final int key_left = KeyEvent.VK_LEFT;
    public static final int key_right = KeyEvent.VK_RIGHT;

    // store which keys are currently pressed:
    boolean keyboardState[] = new boolean[KeyEvent.KEY_LAST];

    private GameObject target;
    private double slowRadius;
    private double targetRadius;

    public ArriveController(GameObject _target, double _slowRadius, double _targetRadius) {
        target = _target;
        slowRadius = _slowRadius;
        targetRadius = _targetRadius;
    }

    public Vector arrive(Car subject, double delta_t) {
        Vector targetVelocity = new Vector(subject.getX(), subject.getY());
        Vector d = new Vector(subject.getX(), subject.getY());
        Vector a = new Vector(subject.getX(), subject.getY());

        Vector character = new Vector(subject.getX(),  subject.getY());
        Vector e = new Vector(target.getX(), target.getY());

        //D = E - character.position
        d = e.minus(character);

        //Length = |D|
        double length = d.magnitude();
        double targetSpeed;

        //If Length<targetRadius Return (0,0,0)
        if(length < targetRadius) {
            return new Vector(0, 0);
        } else if(length > slowRadius) {
            // If Length>slowRadius then targetSpeed = maxSpeed
            targetSpeed = subject.getMaxSpeed();
        } else {
            //else targetSpeed = maxSpeed * Length/slowRadius
            targetSpeed = subject.getMaxSpeed() * (length/slowRadius);
        }

        //targetVelocity = (D/|D|)*targetSpeed
        targetVelocity = d.normalize();
        targetVelocity = targetVelocity.times(targetSpeed);

        Vector characterVelocity = new Vector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle()));
        characterVelocity = characterVelocity.normalize();
        characterVelocity = characterVelocity.times(subject.getSpeed());

        //A = (targetVelociy – character.velocity)/time
        a = targetVelocity.minus(characterVelocity).times((1/delta_t));

        //If |A|>maxAcceleration then A = (A/|A|)*maxAcceleration
        if(a.magnitude() > MAX_ACCELERATION) {
            a = a.normalize();
            a = a.times(MAX_ACCELERATION);
        }

        return a;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;

        motorControl(subject, arrive(subject, delta_t), controlVariables);
    }
}