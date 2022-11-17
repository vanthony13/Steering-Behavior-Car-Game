package controllers;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.Vector;

public class SeekController extends Controller {
    public static final int key_accelerate = KeyEvent.VK_UP;
    public static final int key_brake = KeyEvent.VK_DOWN;
    public static final int key_left = KeyEvent.VK_LEFT;
    public static final int key_right = KeyEvent.VK_RIGHT;

    // store which keys are currently pressed:
    boolean keyboardState[] = new boolean[KeyEvent.KEY_LAST];

    private GameObject target;

    public SeekController(GameObject _target) {
        target = _target;
    }

    public Vector seek(Car subject) {
        Vector c = new Vector(subject.getX(), subject.getY());

        Vector car = new Vector(subject.getX(), subject.getY());
        Vector targetCar = new Vector(target.getX(), target.getY());
        c = targetCar.minus(car);
        c = c.normalize();
        c = c.times(MAX_ACCELERATION);

        //System.out.println(c.toString());
        return c;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;

        motorControl(subject, seek(subject), controlVariables);
    }
}