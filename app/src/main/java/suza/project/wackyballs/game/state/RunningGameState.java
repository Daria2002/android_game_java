package suza.project.wackyballs.game.state;

import android.graphics.Canvas;
import android.util.Log;import android.view.MotionEvent;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.FigureContainer;

/**
 * This class implements Game state interface. It defines a running game state.
 *
 * Created by lmark on 16/08/2017.
 */

public class RunningGameState implements IGameState{

    private static final String TAG = RunningGameState.class.getSimpleName();
    private GamePanel gamePanel;
    private FigureContainer figureContainer;

    public RunningGameState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        figureContainer = new FigureContainer(gamePanel, 10);
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
