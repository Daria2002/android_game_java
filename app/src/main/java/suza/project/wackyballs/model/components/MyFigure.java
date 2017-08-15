package suza.project.wackyballs.model.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

/**
 * This class represents a non-animated figure with some basic properties(speed, position,
 * image - represented as a Bitmap object, etc.) It can collide with walls.
 *
 * Created by lmark on 03/08/2017.
 */

public abstract class MyFigure {

    /**
     * the actual bitmap
     */
    private Bitmap bitmap;
    /**
     * Current X coordinate of the figure.
     */
    private int x;
    /**
     * Current Y coordinate of the figure.
     */
    private int y;
    /**
     * True if Figure is currently being touched, otherwise false.
     */
    private boolean touched;
    /**
     * Currently set spped of the figure.
     */
    private MySpeed speed;

    /**
     * Ball radius.
     */
    private int radius;

    private static final String TAG = MyFigure.class.getSimpleName();

    public MyFigure(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        radius = bitmap.getHeight()/2; // Assume bitmap is equal - sided
        Log.d(TAG, "Creating a new figure at: x= " + x +" y= " + y);
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    /**
     * This method draws current Bitmap object on the position specified
     * by x,y coordinate values.
     *
     * @param canvas Reference to Canvas object, where the image will be drawn.
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }


    /**
     * @return Returns reference to the currently set speed of the MyFigure instance.
     */
    public MySpeed getSpeed() {
        return speed;
    }

    public void setSpeed(MySpeed mySpeed) {
        if (this.speed == null) {
            this.speed = mySpeed;
        } else {
            this.speed.setXv(mySpeed.getXv());
            this.speed.setYv(mySpeed.getYv());
            this.speed.setxDirection(mySpeed.getxDirection());
            this.speed.setyDirection(mySpeed.getyDirection());
        }
    }

    /**
     * Update figure coordinates according to the given speed.
     */
    public void update() {

        if (speed == null) {
            return;
        }

        if (!touched) {
            x += (speed.getXv() * speed.getxDirection());
            y += (speed.getYv() * speed.getyDirection());
        }
    }

    /**
     * Resolve figure collision.
     */
    public void collision(int screenWidth, int screenHeight, List<MyFigure> others) {
        resolveWallCollision(screenWidth, screenHeight);
    }


    /**
     * Reduce speed dividing it by given factor.
     *
     * @param redFactor Speed reduction factor.
     */
    private void reduceSpeed(double redFactor) {
        speed.setXv(speed.getXv()/redFactor);
        speed.setYv(speed.getYv()/redFactor);
    }

    private void resolveWallCollision(int screenWidth, int screenHeight) {
        // check collision with right wall if heading right
        if (speed.getxDirection() == MySpeed.DIRECTION_RIGHT
                && x + radius >= screenWidth) {
            speed.toggleXDirection();
            reduceSpeed(1.5);
        }
        // check collision with left wall if heading left
        if (speed.getxDirection() == MySpeed.DIRECTION_LEFT
                && x - radius  <= 0) {
            speed.toggleXDirection();
            reduceSpeed(1.5);
        }
        // check collision with bottom wall if heading down
        if (speed.getyDirection() == MySpeed.DIRECTION_DOWN
                && y + radius >= screenHeight) {
            speed.toggleYDirection();
            reduceSpeed(1.5);
        }
        // check collision with top wall if heading up
        if (speed.getyDirection() == MySpeed.DIRECTION_UP
                && y - radius <= 0) {
            speed.toggleYDirection();
            reduceSpeed(1.5);
        }
    }

    /**
     * Callback for move action.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public abstract void handleActionMove(int eventX, int eventY);

    /**
     * Callback for down click action.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public abstract void handleActionDown(int eventX, int eventY);

    /**
     * Callback for up click action.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public abstract void handleActionUp(int eventX, int eventY);

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() { return bitmap.getWidth(); }
    public int getHeight() { return bitmap.getHeight(); }
    public int getRadius() { return radius; }
}
