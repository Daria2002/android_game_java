package suza.project.wackyballs.model.properties;

import suza.project.wackyballs.util.Vector;

/**
 * This class represents a simple 2-dimensional speed vector.
 * 
 * Created by lmark on 06/08/2017.
 */

public class MySpeed extends Vector {

    public static final String TAG = MySpeed.class.getSimpleName();

    public static final int DIRECTION_RIGHT	= 1;
    public static final int DIRECTION_LEFT	= -1;
    public static final int DIRECTION_UP	= -1;
    public static final int DIRECTION_DOWN	= 1;

    /**
     * Terminal velocity x direction.
     */
    public static final int TERMINAL_X = 15;

    /**
     * Terminal velocity y direction.
     */
    public static final int TERMINAL_Y = 15;

    /**
     * Gravity acceleration.
     */
    private static double g = 2;

    /**
     * Acceleration vector.
     */
    private Vector acceleration = new Vector(0, g);

    /**
     * Flag used for enabling gravity.
     */
    private boolean gravityEnabled = false;

    /**
     * Last time acceleration was updated
     */
    private long currentTime;

    /**
     * Default speed constructor. Initilizes speed values (1,1).
     */
    public MySpeed() {
        this((double)1,(double)1);
    }

    /**
     * Initializes speed with given component values.
     * @param xv X Component speed value.
     * @param yv Y Component speed value.
     */
    public MySpeed(double xv, double yv) {
        super(xv, yv);
        currentTime = System.currentTimeMillis();
    }

    /**
     * Initilizes speed object based on the current position and time delta.
     * @param dx X position component delta in pixels.
     * @param dy Y position component delta in pixels.
     * @param dt Time delta in seconds.
     */
    public MySpeed(double dx, double dy, double dt) {
        this(getAmplitude(dx/dt,dy/dt)*Math.cos(getPhase(dx/dt,dy/dt)),
                getAmplitude(dx/dt,dy/dt)* Math.sin(getPhase(dx/dt,dy/dt)));
    }

    public int getxDirection() {
        return getX() <= 0 ? DIRECTION_LEFT : DIRECTION_RIGHT;
    }

    public int getyDirection() {
       return getY() <= 0 ? DIRECTION_UP : DIRECTION_DOWN;
    }

    // changes the direction on the X axis
    public void toggleXDirection() {
        setX(-1 * getX());
    }

    public void setXDirection(int direction) {
        double x = getX();
        if (direction == DIRECTION_LEFT) {
            setX(x < 0 ? x : -x);
            return;
        }

        if (direction == DIRECTION_RIGHT) {
            setX(x > 0 ? x : -x);
            return;
        }
    }

    // changes the direction on the Y axis
    public void toggleYDirection() {
        setY(-1 * getY());
    }

    public void increaseSpeed(double xv, double yv) {
        setX(getX() + xv);
        setY(getY() + yv);
    }

    public void reduceSpeed(double xv, double yv) {
        setX(getX() - xv);
        setY(getY() - yv);
    }

    public void setGravity(boolean flag) {
        gravityEnabled = flag;
    }

    public void update() {
        if (!gravityEnabled) {
            return;
        }

        if (System.currentTimeMillis() - currentTime < 500) {
            return;
        }

        currentTime = System.currentTimeMillis();

        if (getX() < TERMINAL_X) {
            setX(getX() + acceleration.getX());
        }

        if (getY() < TERMINAL_Y) {
            setY(getY() + acceleration.getY());
        }
    }
}
