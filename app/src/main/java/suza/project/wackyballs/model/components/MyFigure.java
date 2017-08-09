package suza.project.wackyballs.model.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * This class represents a non-animated figure with some basic properties(speed, position,
 * image - represented as a Bitmap object, etc.)
 *
 * Created by lmark on 03/08/2017.
 */

public class MyFigure {

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
    private MySpeed mySpeed;
    private static final String TAG = MyFigure.class.getSimpleName();

    public MyFigure(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

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
     * This method checks if the figure is currently being touched
     * by the user.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
            if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
                // droid touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }
    }

    /**
     * @return Returns reference to the currently set speed of the MyFigure instance.
     */
    public MySpeed getMySpeed() {
        return mySpeed;
    }

    public void setMySpeed(MySpeed mySpeed) {
        if (this.mySpeed == null) {
            this.mySpeed = mySpeed;
        } else {
            this.mySpeed.setXv(mySpeed.getXv());
            this.mySpeed.setYv(mySpeed.getYv());
            this.mySpeed.setxDirection(mySpeed.getxDirection());
            this.mySpeed.setyDirection(mySpeed.getyDirection());
        }
    }

    /**
     * Update figure coordinates according to the given speed.
     */
    public void update() {

        if (mySpeed == null) {
            return;
        }

        if (!touched) {
            x += (mySpeed.getXv() * mySpeed.getxDirection());
            y += (mySpeed.getYv() * mySpeed.getyDirection());
        }
    }

    /**
     * Reduce speed dividing it by given factor.
     *
     * @param redFactor Speed reduction factor.
     */
    public void reduceSpeed(double redFactor) {
        mySpeed.setXv(mySpeed.getXv()/redFactor);
        mySpeed.setYv(mySpeed.getYv()/redFactor);
    }
}
