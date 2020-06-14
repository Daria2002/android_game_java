package suza.project.wackyballs.model.containers;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.components.AbstractFigure;

/**
 * A generic figure container used for storing, updating and drawing figures.
 *
 * Created by lmark on 15/09/2017.
 */

public class FigureContainer {

    private static final String TAG = FigureContainer.class.getSimpleName();
    private GamePanel gamePanel;

    /**
     * Figure array containing all the figure references.
     */
    private List<AbstractFigure> figureList;

    /**
     * Figure ID counter.
     */
    public static int figureID = 0;

    /**
     * Constructor for the figure container.
     *
     * @param panel Game panel.
     */
    public FigureContainer(GamePanel panel) {
        this.gamePanel = panel;
        figureList = new ArrayList<>();
    }

    /**
     * Add new figure to the container.
     * @param figure
     */
    public void addFigure(AbstractFigure figure) {
        if (figure.getID() == 0) {
            figure.setID(figureID);
            figureID++;
        }
        figureList.add(figure);
    }

    /**
     * Removes figure from the list.
     *
     * @param figure Figure reference.
     */
    public void removeFigure(AbstractFigure figure) {
        figureList.remove(figure);
    }

    /**
     * Handle action event - touch(down).
     *
     * @param eventX X coordinate of event.
     * @param eventY Y coordinate of event.
     */
    public void handleActionDown(int eventX, int eventY) {
        for (AbstractFigure figure : figureList) {
            figure.handleActionDown(eventX, eventY);
        }
    }

    /**
     * Handle action event - touch(move).
     *
     * @param eventX X coordinate of event.
     * @param eventY Y coordinate of event.
     */
    public void handleActionMove(int eventX, int eventY) {
        for (AbstractFigure figure : figureList) {
            figure.handleActionMove(eventX, eventY);
        }
    }

    /**
     * Handle action event - touch(up).
     *
     * @param eventX X coordinate of event.
     * @param eventY Y coordinate of event.
     */
    public void handleActionUp(int eventX, int eventY) {
        for (AbstractFigure figure : figureList) {
            figure.handleActionUp(eventX, eventY);
        }
    }

    /**
     * Handle action event - touch(up).
     *
     * @param eventX X coordinate of event.
     * @param eventY Y coordinate of event.
     */
    public void handleActionDoubleDown(int eventX, int eventY) {
        for (AbstractFigure figure: figureList) {
            figure.handleActionDoubleDown(eventX, eventY);
        }
    }

    /**
     * Draw all figures on the canvas.
     *
     * @param canvas Canvas.
     */
    public void draw(Canvas canvas) {
        for (AbstractFigure figure : figureList) {
            figure.draw(canvas);
        }
    }

    /**
     * Calls update method on all containing figures.
     * Calls resolveCollision method on all figure.
     */
    public void update() {
        // Update all figures
        for (AbstractFigure figure : figureList) {
            figure.update();
        }

        // Update figure collision
        for (AbstractFigure figure: figureList) {
            if (!figure.isAlive()) {
                continue;
            }
            figure.resolveCollision(gamePanel.getWidth(), gamePanel.getHeight(), figureList);
        }
    }

    /**
     * @return Number of figures.
     */
    public int getFigureCount() {
        return figureList.size();
    }

    public List<AbstractFigure> getFigures() {
        return figureList;
    }

    public AbstractFigure getFigure(int index) {
        return figureList.get(index);
    }

    public void removeFigure(int index) {
        figureList.get(index).removeAllListeners();
        figureList.remove(index);
    }
}
