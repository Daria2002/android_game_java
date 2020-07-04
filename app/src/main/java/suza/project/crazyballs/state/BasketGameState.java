package suza.project.crazyballs.state;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import suza.project.crazyballs.GameActivity;
import suza.project.crazyballs.R;
import suza.project.crazyballs.game.GamePanel;
import suza.project.crazyballs.model.containers.BasketBallContainer;
import suza.project.crazyballs.model.figures.BasketBallBadFigure;
import suza.project.crazyballs.model.figures.BasketBallGoodFigure;
import suza.project.crazyballs.model.figures.BasketFigure;
import suza.project.crazyballs.model.figures.HeartFigure;
import suza.project.crazyballs.model.properties.Collision;
import suza.project.crazyballs.util.IGameFinishedListener;
import suza.project.crazyballs.util.IGameInfoListener;
import suza.project.crazyballs.util.IGamePausedListener;
import suza.project.crazyballs.util.Util;

/**
 * Catch balls in basket. Double click basket to store balls.
 *
 * Created by lmark on 13/09/2017.
 */

public class BasketGameState implements IGameState {

    private static final String TAG = BasketGameState.class.getSimpleName();
    private static final long DOUBLE_CLICK_DELTA = 250;
    public static int INITIAL_LIFE_COUNT = 3;
    private final IGameInfoListener defaultListener = new IGameInfoListener() {
        @Override
        public void onLivesChanged(int amount) {
            lives += amount;
        }

        @Override
        public void onScoreChanged(int amount) {
            score += amount;
        }
    };
    private final LevelConfig levelConfig;

    /**
     * Time last click was registered.
     */
    private long lastClickTime = 0;

    private long lastNormalSpawn;
    private long lastLifeSpawn;
    private long lastBadSpawn;

    /**
     * Pause and finish listener
     */
    IGamePausedListener pauseListener;
    IGameFinishedListener finishedListener;

    /**
     * Heart image used for counting lives.
     */
    Bitmap lifeHeart;
    /**
     * Pause image used for pause button
     */
    Bitmap pause;

    private GamePanel panel;
    private BasketBallContainer figureContainer;
    private Paint p = new Paint();

    /**
     * Track lives.
     */
    private int lives = INITIAL_LIFE_COUNT;

    /**
     * Track score.
     */
    private int score = 0;

    public BasketGameState(GamePanel panel, LevelConfig levelConfig) {
        this.panel = panel;
        this.levelConfig = levelConfig;
        // Make a figure container
        figureContainer = new BasketBallContainer(panel);

        // Add a basket figure
        BasketFigure basketFigure = new BasketFigure(panel, figureContainer);
        basketFigure.addGameInfoListener(defaultListener);

        figureContainer.addFigure(basketFigure);
        figureContainer.addFigure(basketFigure.getLeftEdge());
        figureContainer.addFigure(basketFigure.getRightEdge());

        // Set wall collision factor
        Collision.LEFT_FACTOR = 1.2;
        Collision.RIGHT_FACTOR = 1.2;

        lastNormalSpawn = System.currentTimeMillis();
        lastLifeSpawn = System.currentTimeMillis();
        lastBadSpawn = System.currentTimeMillis();
        lifeHeart = BitmapFactory.decodeResource(panel.getResources(), R.drawable.heart);
        pause = BitmapFactory.decodeResource(panel.getResources(), R.drawable.pause);
    }

    @Override
    public void draw(Canvas canvas) {
        // Draw background
        p.setColor(Color.CYAN);
        canvas.drawRect(0, 0, panel.getWidth(), panel.getHeight(), p);

        // Draw container objects
        figureContainer.draw(canvas);

        // Draw life hearts
        for (int i = 0; i < lives; i++) {
            canvas.drawBitmap(lifeHeart, i * lifeHeart.getWidth(), 0, p);
        }

        // Draw score on screen
        p.setColor(Color.BLACK);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String stringScore = String.format("SCORE: %d", score);
        p.setTextSize(100);
        canvas.drawText(stringScore, 5, lifeHeart.getHeight()*2, p);

        // Draw pause symbol
        canvas.drawBitmap(pause, panel.getWidth() - pause.getWidth(), 0, p);
    }

    @Override
    public void update(){
        if(panel.isPaused()) {
            // update lastSpawnTime, otherwise all objects will be spawned after pause
            lastNormalSpawn = System.currentTimeMillis();
            lastBadSpawn = System.currentTimeMillis();
            lastLifeSpawn = System.currentTimeMillis();
            return;
        }
        figureContainer.update();

        // Spawn a new good ball figure
        if (System.currentTimeMillis() - lastNormalSpawn >= levelConfig.getNormalBallSpawnPeriod()) {
            lastNormalSpawn = System.currentTimeMillis();
            levelConfig.randomizeNormalPeriod();

            BasketBallGoodFigure newFigure = new BasketBallGoodFigure(panel, figureContainer);
            newFigure.addGameInfoListener(defaultListener);
            figureContainer.addFigure(newFigure);
        }

        // Spawn a bad figure
        if (System.currentTimeMillis() - lastBadSpawn >= levelConfig.getBadBallSpawnPeriod()) {
            lastBadSpawn = System.currentTimeMillis();
            levelConfig.randomizeBadPeriod();

            BasketBallBadFigure newFigure = new BasketBallBadFigure(panel, figureContainer);
            newFigure.addGameInfoListener(defaultListener);
            figureContainer.addFigure(newFigure);
        }

        // Spawn a new life figure
        if (System.currentTimeMillis() - lastLifeSpawn >= levelConfig.getLifeBallSpawnPeriod()) {
            lastLifeSpawn = System.currentTimeMillis();
            levelConfig.randomizeLifePeriod();

            HeartFigure newFigure = new HeartFigure(panel, figureContainer);
            newFigure.addGameInfoListener(defaultListener);
            figureContainer.addFigure(newFigure);
        }

        // If player lost all lives
        if (lives <= 0) {
            finishedListener.gameFinished(score);
        }
    }

    private  boolean pauseClicked(float x, float y) {
        if(x >= panel.getWidth() - pause.getWidth() && x <= panel.getWidth() &&
           y >= 0 && y <= pause.getHeight()) {
            Log.d(TAG, "Pause clicked.");
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (panel.getHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                long clickTime = System.currentTimeMillis();

                if (clickTime - lastClickTime < DOUBLE_CLICK_DELTA) {
                    // Double click detected
                    Log.d(TAG, "Double click detected");
                    figureContainer.handleActionDoubleDown((int) event.getX(), (int) event.getY());

                } else {
                    // Single click detected
                    Log.d(TAG, "Single click detected");
                    figureContainer.handleActionDown((int) event.getX(), (int) event.getY());
                    if(pauseClicked(event.getX(), event.getY())) {
                        pauseListener.gamePaused();
                    }
                }
                lastClickTime = clickTime;
            }

            // Move detection
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                figureContainer.handleActionMove((int) event.getX(), (int) event.getY());
            }

            // Release detection
            if (event.getAction() == MotionEvent.ACTION_UP) {
                figureContainer.handleActionUp((int) event.getX(), (int) event.getY());
            }
        }
        return true;
    }

    public void setGamePausedListener(IGamePausedListener pl) {
        pauseListener = pl;
    }

    public void setGameFinishedListener(IGameFinishedListener fl) {
        finishedListener = fl;
    }
}
