package suza.project.crazyballs.model.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import suza.project.crazyballs.model.properties.FigureState;
import suza.project.crazyballs.model.properties.FigureType;
import suza.project.crazyballs.model.properties.MySpeed;
import suza.project.crazyballs.util.IGameInfoListener;
import suza.project.crazyballs.util.IGameInfoProvider;

/**
 * This class represents a non-animated figure with some basic properties(speed, position,
 * image - represented as a Bitmap object, etc.) It can collide with walls.
 *
 * Implements game information provider interface used for signaling game information changes.
 *
 * Created by lmark on 03/08/2017.
 */

public abstract class AbstractFigure implements IGameInfoProvider{

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

    /**
     * Figure type;
     */
    private FigureType type = FigureType.GENERIC;

    /**
     * Figure state.
     */
    private FigureState state = FigureState.DEAD;

    /**
     * Figure ID.
     */
    private int ID = 0;

    /**
     * Figure "mass"
     */
    private int mass = 5;

    private static final String TAG = AbstractFigure.class.getSimpleName();

    /**
     * Listener list.
     */
    private List<IGameInfoListener> listenerList;

    public AbstractFigure(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
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
     * @return Returns reference to the currently set speed of the AbstractFigure instance.
     */
    public MySpeed getSpeed() {
        return speed;
    }

    public void setSpeed(MySpeed mySpeed) {
        if (this.speed == null) {
            this.speed = mySpeed;
        } else {
            this.speed.setX(mySpeed.getX());
            this.speed.setY(mySpeed.getY());
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
            x += speed.getX();
            y += speed.getY();
        }
    }

    public boolean isAlive() {
        return state != FigureState.DEAD;
    }

    /**
     * Resolve figure resolveCollision.
     */
    public abstract void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> others);

    /**
     * Callback for double click.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public void handleActionDoubleDown(int eventX, int eventY) {
        //Unimplemented
    }

    /**
     * Callback for move action.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public void handleActionMove(int eventX, int eventY) {
        //Unimplemented
    }

    /**
     * Callback for down click action.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public void handleActionDown(int eventX, int eventY) {
        // Unimplemented
    }

    /**
     * Callback for up click action.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    public void handleActionUp(int eventX, int eventY) {
        // Unimplemented
    }

    @Override
    public void addGameInfoListener(IGameInfoListener listener) {
        if (listenerList == null) {
            listenerList = new ArrayList<>();
        }

        listenerList.add(listener);
    }

    @Override
    public void removeAllListeners() {
        if (listenerList == null) {
            Log.d(TAG, "Cannot remove listeners");
            return;
        }

        int i = 0;
        while (i > listenerList.size()) {
            listenerList.remove(i);
        }
    }

    /**
     * Signals that the score changed.
     * @param amount Amount score changed.
     */
    public void scoreChanged(int amount) {
        for (IGameInfoListener listener: listenerList) {
            listener.onScoreChanged(amount);
        }
    }

    /**
     * Signals that the lives changed.
     * @param amount Amount lives changed.
     */
    public void livesChanged(int amount) {
        for (IGameInfoListener listener: listenerList) {
            listener.onLivesChanged(amount);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
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
    public int getRadius() {

        if (bitmap != null) {
            return bitmap.getHeight()/2;
        } else {
            return radius;
        }
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    public FigureType getType() {
        return type;
    }
    public void setType(FigureType type) {
        this.type = type;
    }
    public FigureState getState() {
        return state;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setState(FigureState state) {
        this.state = state;
    }
    public int getMass() {
        return mass;
    }
    public void setMass(int mass) {
        this.mass = mass;
    }
    public boolean equals(AbstractFigure figure) {
        return getID() == figure.getID();
    }
}
