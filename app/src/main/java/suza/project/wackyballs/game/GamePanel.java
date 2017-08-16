package suza.project.wackyballs.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import suza.project.wackyballs.game.state.IGameState;
import suza.project.wackyballs.game.state.RunningGameState;
import suza.project.wackyballs.model.FigureContainer;
import suza.project.wackyballs.model.MyExplosion;


/**
 * This class represents a game panel used for updating the game state
 * and rendering objects on screen during gameplay. Main goal of this
 * class is to start and stop game loop thread. Rest of the methods
 * will be delegated to the current game state.
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

    /**
     * Current game state. Its methods will be delegated to when update / draw / on touch
     * is called.
     */
    private IGameState gameState;

    private Point screenDimension = new Point();

    /**
     * Game panel constructor. It starts the main game thread,
     * initializes a figure objects and sets its speed.
     *
     * @param context Context reference.
     */
    public GamePanel(Context context) {
        super(context);

        // Get screen dimensions
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(screenDimension);

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

        // initialize the game loop thread
        gameLoopThread = new MainThread(getHolder(), this);

        // Initialize current game state
        gameState = new RunningGameState(this);

        Log.d(TAG, "Screen dimensions: x = "
                + screenDimension.x + " y = " + screenDimension.y);
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
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // Unimplemented
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

        // Delegate method call to the current game state
        return gameState.onTouchEvent(event);
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

        // Delegate method call to the current game state
        gameState.draw(canvas);
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

        // Delegate method call to the current game state
        gameState.update();
    }

    /**
     * Set new game state.
     *
     * @param gameState New game state.
     */
    public void setGameState(IGameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @return Return screen width.
     */
    public int getScreenWidth() {
        return screenDimension.x;
    }

    public int getScreenHeight() {
        return screenDimension.y;
    }

}
