package suza.project.wackyballs.model.components;

import android.support.annotation.MainThread;
import android.util.Log;

/**
 * This class represents a simple 2-dimensional speed vector.
 * 
 * Created by lmark on 06/08/2017.
 */

public class MySpeed {

    public static final String TAG = MySpeed.class.getSimpleName();
    public static final int DIRECTION_RIGHT	= 1;
    public static final int DIRECTION_LEFT	= -1;
    public static final int DIRECTION_UP	= -1;
    public static final int DIRECTION_DOWN	= 1;

    /**
     * Absolute velocity on the X axis.
     */
    private double xv = 1;	// velocity value on the X axis

    /**
     * Absolute velocity on the Y axis.
     */
    private double yv = 1;	// velocity value on the Y axis

    private int xDirection = DIRECTION_RIGHT;
    private int yDirection = DIRECTION_DOWN;

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
        this.xv = xv;
        this.yv = yv;
        adjustSpeedDirection();
    }

    /**
     * Initilizes speed object based on the current position and time delta.
     * @param dx X position component delta in pixels.
     * @param dy Y position component delta in pixels.
     * @param dt Time delta in seconds.
     */
    public MySpeed(double dx, double dy, double dt) {
        this(getAmplitude(dx,dy,dt)*Math.cos(getPhase(dx,dy,dt)),
                getAmplitude(dx,dy,dt)* Math.sin(getPhase(dx,dy,dt)));
    }


    public double getXv() {
        return xv;
    }
    public void setXv(double xv) {
        this.xv = xv;
    }
    public double getYv() {
        return yv;
    }
    public void setYv(double yv) {
        this.yv = yv;
    }

    public int getxDirection() {
        return xDirection;
    }
    public void setxDirection(int xDirection) {
        this.xDirection = xDirection;
    }
    public int getyDirection() {
        return yDirection;
    }
    public void setyDirection(int yDirection) {
        this.yDirection = yDirection;
    }

    // changes the direction on the X axis
    public void toggleXDirection() {
        xDirection = xDirection * -1;
    }

    // changes the direction on the Y axis
    public void toggleYDirection() {
        yDirection = yDirection * -1;
    }

    public static double getAmplitude(double dx, double dy, double dt) {
        return  Math.sqrt(Math.pow(dx/dt, 2) + Math.pow(dy/dt,2));
    }

    public static double getPhase(double dx, double dy, double dt) {
        double phase = Math.atan2(dy/dt, dx/dt);
        return phase > 0 ? phase : phase + 2* Math.PI;
    }

    public void increaseSpeed(double xv, double yv) {
        this.xv = this.xv * xDirection + xv;
        this.yv = this.yv * yDirection + yv;

        adjustSpeedDirection();
    }

    public void reduceSpeed(double xv, double yv) {
        this.xv = this.xv * xDirection - xv;
        this.yv = this.yv * yDirection - yv;

        adjustSpeedDirection();
    }

    private void adjustSpeedDirection() {
        if (this.xv < 0) {
            xDirection = DIRECTION_LEFT;
            this.xv = - this.xv;
        } else {
            xDirection = DIRECTION_RIGHT;
        }

        if (this.yv < 0) {
            yDirection = DIRECTION_UP;
            this.yv = -this.yv;
        } else {
            yDirection = DIRECTION_DOWN;
        }
    }
}
