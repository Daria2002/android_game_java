package suza.project.wackyballs.model;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.R;
import suza.project.wackyballs.model.components.MyAnimation;
import suza.project.wackyballs.model.components.MyFigure;
import suza.project.wackyballs.model.components.MySpeed;
import suza.project.wackyballs.util.Util;

/**
 * This class implements figure action movements. Figure can be dragged on screen
 * and can be given different velocity by dragging it and letting it go.
 * It collides with other figures of the same type.
 * Object ID is also drawn along side each object on screen.
 *
 * Created by lmark on 15/08/2017.
 */

public class DefaultFigure extends MyAnimation {

    /**
     * Old x coordinate of the figure ( from the previous frame update )
     */
    private int xOld = 0;

    /**
     * Old y coordinate of the figure ( from the previous frame update )
     */
    private int yOld = 0;

    /**
     * Old time in ms
     */
    private long tOld = 0;

    /**
     * Difference between previous and current Y coordinate.
     */
    private float dx = 0;

    /**
     * Difference between previous and current Y coordinate.
     */
    private float dy = 0;

    /**
     * Difference between previous and current time.
     */
    private long dt = 0;

    /**
     * Class tag used for logging.
     */
    private static final String TAG = DefaultFigure.class.getSimpleName();

    private GamePanel gamePanel;
    private Paint paint = new Paint();
    private int ID;
    private static final double spring = 0.05;

    public DefaultFigure(GamePanel gamePanel, int ID) {
        super(BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.face_animation),
                Util.randomInteger(0, gamePanel.getScreenWidth()), -10,
                10, 4);
        super.setSpeed(new MySpeed(
                Util.randomInteger(-20, 20),
                Util.randomInteger(1, 20)
        ));

        this.gamePanel = gamePanel;
        this.ID = ID;
    }

    /**
     * Action move implementation. If user is touching this figure while moving
     * finger, figure will move aswell.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    @Override
    public void handleActionMove(int eventX, int eventY) {
        if (isTouched()) {
            setX(eventX);
            setY(eventY);

            dx = eventX - xOld;
            dy = eventY - yOld;
            dt = System.currentTimeMillis() - tOld;
            tOld = System.currentTimeMillis();
            xOld = eventX;
            yOld = eventY;
        }
    }

    /**
     * Action down implementation. It calculates if user touched this figure.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    @Override
    public void handleActionDown(int eventX, int eventY) {
        int x = getX();
        int y = getY();
        int spriteWidth = getWidth();
        int spriteHeight = getHeight();

        if (eventX >= (x - spriteWidth / 2) && (eventX <= (x + spriteWidth/2))) {
            if (eventY >= (y - spriteHeight / 2) && (y <= (y + spriteHeight / 2))) {
                // droid touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }

        if (isTouched()) {
            xOld = eventX;
            yOld = eventY;
            tOld = System.currentTimeMillis();
        }
    }

    /**
     * Action up implementation. Check if user was touching this figure.
     * This figure gains new speed that user applied to it when moving it around.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    @Override
    public void handleActionUp(int eventX, int eventY) {
        if (isTouched()) {
            setTouched(false);
            Log.d(TAG, "Action-UP: ID: " + ID);
            setSpeed(new MySpeed(dx, dy, (double)dt/50));
        }
    }

    /**
     * Draws the bitmap along with the appropriate ID.
     *
     * @param canvas Reference to Canvas object, where the image will be drawn.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setARGB(255, 255, 255, 255);
        paint.setTextSize(50);
        canvas.drawText(String.valueOf(ID), getX() + getWidth(), getY(), paint);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        MySpeed speed = getSpeed();
        if (speed.getxDirection() == MySpeed.DIRECTION_LEFT) {
            canvas.drawLine(getX(), getY(), getX()-50, getY(), paint);
        } else {
            canvas.drawLine(getX(), getY(), getX()+50, getY(), paint);
        }

        if (speed.getyDirection() == MySpeed.DIRECTION_DOWN) {
            canvas.drawLine(getX(), getY(), getX(), getY()+50, paint);
        } else {
            canvas.drawLine(getX(), getY(), getX(), getY()-50, paint);
        }
    }

    @Override
    public void collision(int screenWidth, int screenHeight, List<MyFigure> others) {
        super.collision(screenWidth, screenHeight, others);

        // Add object collision
        for (int i = ID, count = others.size(); i < count; i++) {
            MyFigure otherFig = others.get(i);

            float dx = otherFig.getX() - getX();
            float dy = otherFig.getY() - getY();
            double distance = Math.sqrt(dx*dx + dy*dy);
            float minDist = others.get(i).getRadius() + getRadius();

            // Check if there is a collision
            if (distance >= minDist) {
                continue;
            }

            Log.d(TAG, "Collision detected between objects: "
                    + ID + " and " + ((DefaultFigure)otherFig).ID);
            double angle = Math.atan2(dy, dx);
            angle = angle > 0 ? angle : angle + 2 * Math.PI;
            double targetX = getX() + Math.cos(angle) * minDist;
            double targetY = getY() + Math.sin(angle) * minDist;

            //TODO Zasto ovako ?
            double ax = (targetX - otherFig.getX()) * spring;
            double ay = (targetY - otherFig.getY()) * spring;

            getSpeed().reduceSpeed(ax, ay);
            others.get(i).getSpeed().increaseSpeed(ax, ay);
        }
    }
}
