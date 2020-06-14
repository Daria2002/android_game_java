package suza.project.wackyballs.model.containers;

import android.graphics.Canvas;
import android.util.Log;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.components.AbstractFigure;
import suza.project.wackyballs.model.properties.FigureState;
import suza.project.wackyballs.model.properties.FigureType;


/**
 * This class represents a container used for storing and managing Figures on screen,
 * and resolving collisions.
 *
 * Created by lmark on 15/08/2017.
 */

public class BasketBallContainer extends FigureContainer {

    private static final String TAG = BasketBallContainer.class.getSimpleName();
    private int score = 0;
    private int lives = 0;

    /**
     * Constructor for the figure container.
     *
     * @param panel Game panel.
     */
    public BasketBallContainer(GamePanel panel) {
        super(panel);
    }

    /**
     * Draw all figures on the canvas.
     *
     * @param canvas Canvas.
     */
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw basket last
        for (AbstractFigure figure: getFigures()) {
            if (figure.getType() == FigureType.BASKET) {
                figure.draw(canvas);
                return;
            }
        }
    }

    /**
     * Update current figure positions and states.
     */
    public void update() {
        super.update();

        // Remove dead figures
        int i = 0;
        while (i < getFigureCount()) {

            // Reset collision states
            if (getFigure(i).getState() == FigureState.COLLISION) {
                getFigures().get(i).setState(FigureState.ALIVE);
            }

            // If figure is alive continue
            if (getFigure(i).isAlive()) {
                i++;
                continue;
            }

            // If figure is not alive remove it
            removeFigure(i);
            Log.d(TAG, "Figure removed. " + getFigureCount() + " left.");
        }
    }
}
