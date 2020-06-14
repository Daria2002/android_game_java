package suza.project.crazyballs.state;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import suza.project.crazyballs.R;
import suza.project.crazyballs.game.GamePanel;
import suza.project.crazyballs.model.containers.BasketBallContainer;
import suza.project.crazyballs.model.figures.BasketBallBadFigure;
import suza.project.crazyballs.model.figures.BasketBallGoodFigure;
import suza.project.crazyballs.model.figures.BasketFigure;
import suza.project.crazyballs.model.figures.HeartFigure;
import suza.project.crazyballs.model.properties.Collision;
import suza.project.crazyballs.util.IGameInfoListener;
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

    /**
     * Time last click was registered.
     */
    private long lastClickTime = 0;

    private int normalBallSpawnPeriod = 2000; //ms
    private int lifeBallSpawnPeriod = 10000; // ms
    private int badBallSpawnPeriod = 5000;  // ms

    private long lastNormalSpawn;
    private long lastLifeSpawn;
    private long lastBadSpawn;

    /**
     * Heart image used for counting lives.
     */
    Bitmap lifeHeart;

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

    public BasketGameState(GamePanel panel) {
        this.panel = panel;

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
    }

    @Override
    public void update(){
        figureContainer.update();

        // Spawn a new good ball figure
        if (System.currentTimeMillis() - lastNormalSpawn >= normalBallSpawnPeriod) {
            lastNormalSpawn = System.currentTimeMillis();
            normalBallSpawnPeriod = Util.randomInteger(3, 5) * 1000;

            BasketBallGoodFigure newFigure = new BasketBallGoodFigure(panel, figureContainer);
            newFigure.addGameInfoListener(defaultListener);
            figureContainer.addFigure(newFigure);
        }

        // Spawn a bad figure
        if (System.currentTimeMillis() - lastBadSpawn >= badBallSpawnPeriod) {
            lastBadSpawn = System.currentTimeMillis();
            badBallSpawnPeriod = Util.randomInteger(7, 9) * 1000;

            BasketBallBadFigure newFigure = new BasketBallBadFigure(panel, figureContainer);
            newFigure.addGameInfoListener(defaultListener);
            figureContainer.addFigure(newFigure);
        }

        // Spawn a new life figure
        if (System.currentTimeMillis() - lastLifeSpawn >= lifeBallSpawnPeriod) {
            lastLifeSpawn = System.currentTimeMillis();
            lifeBallSpawnPeriod = Util.randomInteger(7, 12) * 1000;

            HeartFigure newFigure = new HeartFigure(panel, figureContainer);
            newFigure.addGameInfoListener(defaultListener);
            figureContainer.addFigure(newFigure);
        }

        // If player lost all lives
        if (lives <= 0) {
            panel.finish(score);
        }
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

}
