package suza.project.wackyballs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import suza.project.wackyballs.model.FigureContainer;
import suza.project.wackyballs.model.MyExplosion;
import suza.project.wackyballs.model.components.MySpeed;


/**
 * This class reprents a game panel used for updating the game state
 * and rendering objects on screen during gameplay.
 *
 * Created by lmark on 02/08/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Class tag used for logging.
     */
    private static final String TAG = GamePanel.class.getSimpleName();

    /**
     * Game loop thread.
     */
    private MainThread gameLoopThread;

    /**
     * Currently set average FPS.
     */
    private String avgFps;

    /**
     * Figure container - holds all figureContainer on screen.
     */
    private FigureContainer figureContainer;
    private MyExplosion[] explosions = new MyExplosion[10];
    private static final int EXPLOSION_COUNT = 50;

    /**
     * Game panel constructor. It starts the main game thread,
     * initializes a figure objects and sets its speed.
     *
     * @param context Context reference.
     */
    public GamePanel(Context context) {
        super(context);

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

        // initialize the game loop thread
        gameLoopThread = new MainThread(getHolder(), this);
    }

    /**
     * Creates the main game loop thread.
     *
     * @param surfaceHolder Surface holder reference.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        // Initialize figure container..
        if (figureContainer == null) {
            figureContainer = new FigureContainer(this, 10);
        }

        // Solution for freezing when app is paused (?)
        if (!gameLoopThread.isAlive()) {
            Log.d(TAG, "Surface created is called, thread is found dead.");
            gameLoopThread = new MainThread(surfaceHolder, this);
        }

        gameLoopThread.setRunning(true);
        gameLoopThread.start();

        Log.d(TAG, "MainThread successfully started");

        for (int i = 0; i < explosions.length; i++) {
            explosions[i] = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     * Destroys the main game loop thread.
     *
     * @param surfaceHolder Surface holder reference.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "SurfaceDestroyed was called.");
        boolean retry = true;
        gameLoopThread.setRunning(false);

        while (retry) {
            try {
                gameLoopThread.join();
                // If thread is successfully joined stop the loop
                retry = false;

            } catch (InterruptedException ignorable) {
                // Ignorable
            }
        }

        Log.d(TAG, "MainThread was successfully stopped.");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            figureContainer.handleActionDown((int)event.getX(), (int)event.getY());
            Log.d(TAG, "Coordinates: x=" + event.getX() + ",y=" + event.getY());

        } if (event.getAction() == MotionEvent.ACTION_MOVE) {
            figureContainer.handleActionMove((int)event.getX(), (int)event.getY());

        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
            figureContainer.handleActionUp((int)event.getX(), (int)event.getY());
        }
        return true;
    }

    /**
     * Set the average fps
     *
     * @param avgFps Current average fps
     */
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }


    /**
     * Render stuff on screen.
     *
     * @param canvas Canvas reference.
     */
    public void render(Canvas canvas) {

        // If the application is closing unexpectedly canvas
        // reference can be null, in that case do not
        // attempt to render anything.
        if (canvas == null) {
            return;
        }

        // display fps
        canvas.drawColor(Color.BLACK);
        this.draw(canvas);
        displayFps(canvas, avgFps);
        figureContainer.draw(canvas);

        for (MyExplosion explosion:explosions) {
            if (explosion != null) {
                explosion.draw(canvas);
            }
        }
    }

    /**
     * Display current average FPS text on canvas.
     *
     * @param canvas Canvas reference.
     * @param fps FPS text string.
     */
    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255, 255, 255, 255);
            paint.setTextSize(50);
            canvas.drawText(fps, this.getWidth() - 250, 50, paint);
        }
    }

    /**
     * Update game panel state. Currently handles collision detection
     * between a single MyFigure object and the wall.
     */
    public void update() {

        figureContainer.update();

        for (MyExplosion explosion:explosions) {
            if (explosion != null) {
                explosion.update();
            }
        }
    }

    /**
     * Generate a new explosion.
     * @param x X coordinate of the explosion.
     * @param y Y coordiante of the explosion.
     */
    private void generateExplosion(double x, double y) {
        // check if explosion is null or if it is still active
        int selectedExplosion = -1;
        MyExplosion explosion = null;

        for (int i = 0, eCount = explosions.length; i < eCount; i++) {
            explosion = explosions[i];
            if (explosion != null && explosion.isAlive()) {
                continue;
            }
            selectedExplosion = i;
            break;
        }

        if (selectedExplosion == -1) {
            Log.d(TAG, "Cannot create any more explosions.");
            return;
        }

        explosions[selectedExplosion] = new MyExplosion(EXPLOSION_COUNT, (float)x, (float)y);
    }
}
