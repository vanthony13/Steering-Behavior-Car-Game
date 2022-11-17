package controllers;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.RotatedRectangle;
import engine.Vector;

public class SeekAvoidController extends Controller {
    public static final int key_accelerate = KeyEvent.VK_UP;
    public static final int key_brake = KeyEvent.VK_DOWN;
    public static final int key_left = KeyEvent.VK_LEFT;
    public static final int key_right = KeyEvent.VK_RIGHT;

    // store which keys are currently pressed:
    boolean keyboardState[] = new boolean[KeyEvent.KEY_LAST];

    private GameObject target;

    public SeekAvoidController(GameObject _target) {
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

    public Vector seekTurn(Car subject, double x, double y) {
        Vector c = new Vector(subject.getX(), subject.getY());

        Vector car = new Vector(subject.getX(), subject.getY());
        Vector targetCar = new Vector(x, y);
        c = targetCar.minus(car);
        c = c.normalize();
        c = c.times(MAX_ACCELERATION);

        //System.out.println(c.toString());
        return c;
    }

    public boolean collideBox(Car subject, Game game, double angle, double distance) {
        double newX = subject.getX() + Math.cos(angle) * distance;
        double newY = subject.getY() + Math.sin(angle) * distance;

        RotatedRectangle r = new RotatedRectangle(newX, newY, subject.getImg().getWidth() * 1.0, subject.getImg().getHeight() * 1.0, angle);

        for(GameObject obj:game.getObjects()) {
            if(obj.isObstacle() && RotatedRectangle.RotRectsCollision(r, obj.getCollisionBox())) {
                return true;
            }
        }

        return false;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double castLength = 25;
        double moveLength = castLength;

        boolean left = false;
        boolean front = false;
        boolean right = false;

        for(int i = 1; i < castLength; i ++) {
            left = collideBox(subject, game, subject.getAngle() - Math.PI/4, i);
            if(left) {
                moveLength = i;
                break;
            }
        }

        for(int i = 1; i < castLength; i ++) {
            front = collideBox(subject, game, subject.getAngle(), i);
            if(front) {
                moveLength = i;
                break;
            }
        }

        for(int i = 1; i < castLength; i ++) {
            right = collideBox(subject, game, subject.getAngle() + Math.PI/4, i);
            if(right) {
                moveLength = i;
                break;
            }
        }

        if(left) {
            System.out.println("Going Right");
            double newX = subject.getX() + Math.cos(subject.getAngle() + Math.PI/4) * moveLength;
            double newY = subject.getY() + Math.sin(subject.getAngle() + Math.PI/4) * moveLength;
            motorControl(subject, seekTurn(subject, newX, newY), controlVariables);
        } else if(front) {
            System.out.println("Going Back");
            double newX = subject.getX() + Math.cos(subject.getAngle()) * -moveLength;
            double newY = subject.getY() + Math.sin(subject.getAngle()) * -moveLength;
            motorControl(subject, seekTurn(subject, newX, newY), controlVariables);
        } else if(right) {
            System.out.println("Going Left");
            double newX = subject.getX() + Math.cos(subject.getAngle() - Math.PI/4) * moveLength;
            double newY = subject.getY() + Math.sin(subject.getAngle() - Math.PI/4) * moveLength;
            motorControl(subject, seekTurn(subject, newX, newY), controlVariables);
        } else {
            motorControl(subject, seek(subject), controlVariables);
        }
    }
}