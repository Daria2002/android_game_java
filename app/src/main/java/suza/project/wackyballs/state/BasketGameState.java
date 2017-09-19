package suza.project.wackyballs.state;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import suza.project.wackyballs.R;
import suza.project.wackyballs.ScoreActivity;
import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.containers.BasketBallContainer;
import suza.project.wackyballs.model.figures.BadFigure;
import suza.project.wackyballs.model.figures.BasketBallFigure;
import suza.project.wackyballs.model.figures.BasketFigure;
import suza.project.wackyballs.model.figures.HeartFigure;
import suza.project.wackyballs.model.properties.Collision;
import suza.project.wackyballs.util.Util;

/**
 * Catch balls in basket. Double click basket to store balls.
 *
 * Created by lmark on 13/09/2017.
 */

public class BasketGameState implements IGameState {

    private static final String TAG = BasketGameState.class.getSimpleName();
    private static final long DOUBLE_CLICK_DELTA = 250;

    /**
     * Time last click was registered.
     */
    private long lastClickTime = 0;

    /**
     * Period when new figures spawn.
     */
    private int normalBallSpawnPeriod = 5000; //ms
    private int lifeBallSpawnPeriod = 10000; // ms
    private int badBallSpawnPeriod = 7000;

    /**
     * Time when last figure spawned.
     */
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

    public BasketGameState(GamePanel panel) {
        this.panel = panel;

        // Make a figure container
        figureContainer = new BasketBallContainer(panel);

        // Add a basket figure
        BasketFigure basketFigure = new BasketFigure(panel, figureContainer);
        figureContainer.addFigure(basketFigure);
        figureContainer.addFigure(basketFigure.getLeftEdge());
        figureContainer.addFigure(basketFigure.getRightEdge());
        figureContainer.increaseLives(5);

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

        figureContainer.draw(canvas);

        // Draw life hearts
        for (int i = 0; i < figureContainer.getLives(); i++) {
            canvas.drawBitmap(lifeHeart, i * lifeHeart.getWidth(), 0, p);
        }

        // Draw score on screen
        p.setColor(Color.BLACK);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String score = String.format("SCORE: %d", figureContainer.getScore());
        p.setTextSize(100);
        canvas.drawText(score, 5, lifeHeart.getHeight()*2, p);
    }

    @Override
    public void update() {
        figureContainer.update();

        // Spawn a new good ball figure
        if (System.currentTimeMillis() - lastNormalSpawn >= normalBallSpawnPeriod) {
            lastNormalSpawn = System.currentTimeMillis();
            normalBallSpawnPeriod = Util.randomInteger(2, 6) * 1000;
            figureContainer.addFigure(new BasketBallFigure(panel, figureContainer));
        }

        if (System.currentTimeMillis() - lastBadSpawn >= badBallSpawnPeriod) {
            lastBadSpawn = System.currentTimeMillis();
            badBallSpawnPeriod = Util.randomInteger(4, 7) * 1000;
            figureContainer.addFigure(new BadFigure(panel, figureContainer));
        }

        // Spawn a new life figure
        if (System.currentTimeMillis() - lastLifeSpawn >= lifeBallSpawnPeriod) {
            lastLifeSpawn = System.currentTimeMillis();
            lifeBallSpawnPeriod = Util.randomInteger(6, 10) * 1000;
            figureContainer.addFigure(new HeartFigure(panel, figureContainer));
        }

        if (figureContainer.getLives() == 0) {
            /*
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(panel.getContext(),
                        android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(panel.getContext());
            }
            final EditText input = new EditText(panel.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setTitle("Game Over")
                    .setMessage("Do you want to save your score?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Activity activity = (Activity)panel.getContext();
                            activity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
                    */

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

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                figureContainer.handleActionMove((int) event.getX(), (int) event.getY());

            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // touch was released
                figureContainer.handleActionUp((int) event.getX(), (int) event.getY());
            }
        }

        return true;
    }

}
