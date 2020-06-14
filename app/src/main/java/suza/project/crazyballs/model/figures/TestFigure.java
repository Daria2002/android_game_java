package suza.project.crazyballs.model.figures;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import suza.project.crazyballs.game.GamePanel;
import suza.project.crazyballs.R;
import suza.project.crazyballs.model.components.AbstractAnimation;
import suza.project.crazyballs.model.components.AbstractFigure;
import suza.project.crazyballs.model.properties.Collision;
import suza.project.crazyballs.model.properties.FigureState;
import suza.project.crazyballs.model.properties.FigureType;
import suza.project.crazyballs.model.properties.MySpeed;
import suza.project.crazyballs.util.Util;

/**
 * This class implements figure action movements. Figure can be dragged on screen
 * and can be given different velocity by dragging it and letting it go.
 * It collides with other figures of the same type.
 * Object ID is also drawn along side each object on screen.
 *
 * Created by lmark on 15/08/2017.
 */

public class TestFigure extends AbstractAnimation {

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
    private static final String TAG = TestFigure.class.getSimpleName();

    /**
     * Used to draw the figure.
     */
    private Paint paint = new Paint();

    public TestFigure(GamePanel gamePanel) {
        super(BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.face_animation),
                Util.randomInteger(0, gamePanel.getScreenWidth()), -10,
                10, 4);
        super.setSpeed(new MySpeed(
                Util.randomInteger(-5, 5),
                Util.randomInteger(2, 20)
        ));

        setState(FigureState.ALIVE);
        setType(FigureType.BALL);
    }

    /**
     * Action move implementation. If user is touching this figure while moving
     * finger, figure will move aswell.
     *
     * @param eventX X coordinate.
     * @param eventY Y coordinate.
     */
    @Override
    public synchronized void handleActionMove(int eventX, int eventY) {
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
    public synchronized void handleActionDown(int eventX, int eventY) {
        int radius = getRadius();

        double dx = eventX - getX();
        double dy = eventY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + 20) {
            setTouched(true);
            Log.d(TAG, "Touched ball " + getID());
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
    public synchronized void handleActionUp(int eventX, int eventY) {
        if (isTouched()) {
            setTouched(false);
            Log.d(TAG, "Action-UP: ID: " + getID());
            MySpeed newSpeed = new MySpeed(dx, dy, (double)dt/50);

            // Limit speed
            if (newSpeed.getX() > 30) {
                newSpeed.setX(30);
            }

            if (newSpeed.getX() < -30) {
                newSpeed.setX(-30);
            }

            if (newSpeed.getY() > 30) {
                newSpeed.setY(30);
            }

            if (newSpeed.getY() < -30) {
                newSpeed.setY(-30);
            }

            setSpeed(newSpeed);
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
        canvas.drawText(String.valueOf(getID()), getX() + getWidth(), getY(), paint);

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

    /**
     * Resolve collision between BALL shaped objects.
     *
     * @param screenWidth Screen width.
     * @param screenHeight Screen height.
     * @param otherFigures Other figures.
     */
    @Override
    public void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> otherFigures) {
        Collision.resolveWallCollision(screenWidth, screenHeight, this);

        // Add object resolveCollision
        for (int i = 0, count = otherFigures.size(); i < count; i++) {

            // Dont collid with self
            if (this.getID() == otherFigures.get(i).getID()) {
                continue;
            }

            Collision.resolveFigureCollision2(this, otherFigures.get(i));
        }
    }
}
