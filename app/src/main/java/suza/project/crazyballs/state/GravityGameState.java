package suza.project.wackyballs.state;

import android.graphics.Canvas;
import android.view.MotionEvent;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.containers.GeneralFigureContainer;
import suza.project.wackyballs.model.figures.GravityBallFigure;
import suza.project.wackyballs.model.properties.Collision;

/**
 * This game state periodically spawns balls. Balls are affected by gravity.
 *
 * Created by lmark on 13/09/2017.
 */

public class GravityGameState implements IGameState {

    private static final String TAG = GravityGameState.class.getSimpleName();

    /**
     * Period when new figures spawn.
     */
    private int spawnPeriod = 2000; //ms

    /**
     * Time when last figure spawned.
     */
    private long currentTime;

    private GamePanel panel;
    private GeneralFigureContainer container;

    public GravityGameState(GamePanel panel) {
        this.panel = panel;
        container = new GeneralFigureContainer(panel);
        container.addFigure(new GravityBallFigure(panel));

        Collision.BOTTOM_FACTOR = 3;
        Collision.LEFT_FACTOR = 1;
        Collision.RIGHT_FACTOR = 1;

        currentTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        container.draw(canvas);
    }

    @Override
    public void update() {
        container.update();

        if (System.currentTimeMillis() - currentTime < spawnPeriod) {
            return;
        }

        // Time to spawn new figure
        currentTime = System.currentTimeMillis();
        container.addFigure(new GravityBallFigure(panel));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Unimplemented
        return false;
    }
}
