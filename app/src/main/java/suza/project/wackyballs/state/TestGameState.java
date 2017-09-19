package suza.project.wackyballs.state;

import android.graphics.Canvas;
import android.util.Log;import android.view.MotionEvent;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.figures.TestFigure;
import suza.project.wackyballs.model.containers.GeneralFigureContainer;
import suza.project.wackyballs.model.properties.Collision;

/**
 * This class implements Game state interface. It defines a running game state.
 *
 * Created by lmark on 16/08/2017.
 */

public class TestGameState implements IGameState{

    private static final String TAG = TestGameState.class.getSimpleName();
    private GamePanel gamePanel;
    private GeneralFigureContainer figureContainer;

    public TestGameState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        figureContainer = new GeneralFigureContainer(gamePanel);
        figureContainer.figureID = 0;

        for (int i = 0; i < 10; i++) {
            figureContainer.addFigure(new TestFigure(gamePanel));
        }

        // Reset collision factors
        Collision.TOP_FACTOR = 1.5;
        Collision.BOTTOM_FACTOR = 1.5;
        Collision.LEFT_FACTOR = 1.5;
        Collision.RIGHT_FACTOR = 1.5;
    }

    @Override
    public void draw(Canvas canvas) {
        figureContainer.draw(canvas);
    }

    @Override
    public void update() {
        figureContainer.update();
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
}
