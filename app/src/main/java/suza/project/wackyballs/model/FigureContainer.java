package suza.project.wackyballs.model;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.components.MyFigure;


/**
 * This class represents a container used for storing and managing Figures on screen,
 * and resolving collisions.
 *
 * Created by lmark on 15/08/2017.
 */

public class FigureContainer {

    private GamePanel gamePanel;
    private int figureNumber;
    private static final String TAG = FigureContainer.class.getSimpleName();

    /**
     * Figure array conaining all the figure references.
     */
    private List<MyFigure> figureList;

    /**
     * Constructor for the figure container. Initializes balls on screen.
     *
     * @param panel Game panel.
     * @param figureNumber Number of figures on screen.
     */
    public FigureContainer(GamePanel panel, int figureNumber) {
        this.gamePanel = panel;
        this.figureNumber = figureNumber;
        figureList = new ArrayList<>();

        for (int i = 0; i < figureNumber; i++) {
            figureList.add(new DefaultFigure(panel, i+1));
        }
    }


    /**
     * Handle action event - touch(down).
     *
     * @param eventX X coordinate of event.
     * @param eventY Y coordinate of event.
     */
    public void handleActionDown(int eventX, int eventY) {
        for (MyFigure figure : figureList) {
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
        for (MyFigure figure : figureList) {
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
        for (MyFigure figure : figureList) {
            figure.handleActionUp(eventX, eventY);
        }
    }

    /**
     * Draw all figures on the canvas.
     *
     * @param canvas Canvas.
     */
    public void draw(Canvas canvas) {
        for (MyFigure figure : figureList) {
            figure.draw(canvas);
        }
    }

    /**
     * Update current figure positions and states.
     */
    public void update() {
        for (MyFigure figure : figureList) {
            figure.collision(gamePanel.getWidth(), gamePanel.getHeight(), figureList);
            figure.update();
        }
    }
}
