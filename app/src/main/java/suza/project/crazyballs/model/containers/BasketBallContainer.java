package suza.project.crazyballs.model.containers;

import android.graphics.Canvas;
import android.util.Log;

import suza.project.crazyballs.game.GamePanel;
import suza.project.crazyballs.model.components.AbstractFigure;
import suza.project.crazyballs.model.properties.FigureState;
import suza.project.crazyballs.model.properties.FigureType;


/**
 * This class represents a container used for storing and managing Figures on screen,
 * and resolving collisions.
 *
 * Created by lmark on 15/08/2017.
 */

public class BasketBallContainer extends FigureContainer {

    private static final String TAG = BasketBallContainer.class.getSimpleName();

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
        }
    }
}
