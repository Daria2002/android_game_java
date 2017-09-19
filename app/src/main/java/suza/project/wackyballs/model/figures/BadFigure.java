package suza.project.wackyballs.model.figures;

import android.graphics.BitmapFactory;

import java.util.List;

import suza.project.wackyballs.R;
import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.components.AbstractFigure;
import suza.project.wackyballs.model.containers.BasketBallContainer;
import suza.project.wackyballs.model.properties.Collision;
import suza.project.wackyballs.model.properties.FigureState;
import suza.project.wackyballs.model.properties.FigureType;
import suza.project.wackyballs.model.properties.MySpeed;
import suza.project.wackyballs.util.Util;

/**
 * Created by lmark on 16/09/2017.
 */

public class BadFigure extends AbstractFigure {

    BasketBallContainer figureContainer;
    GamePanel gamePanel;

    public BadFigure(GamePanel gamePanel, BasketBallContainer figureContainer) {
        super(BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.bad_ball),
                Util.randomInteger(0, gamePanel.getScreenWidth()), -50);
        super.setSpeed(new MySpeed(
                Util.randomInteger(-10, 10),
                Util.randomInteger(2, 5)
        ));

        this.figureContainer = figureContainer;
        this.gamePanel = gamePanel;
        super.getSpeed().setGravity(true);
        setState(FigureState.ALIVE);
        setType(FigureType.BAD_BALL);
    }

    @Override
    public void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> others) {
        Collision.resolveSideWallCollision(screenWidth, screenHeight, this);

        // Resolve collision
        for (int i = 0, count = others.size(); i < count; i++) {

            if (this.getID() == others.get(i).getID()) {
                continue;
            }

            if (others.get(i).getType() == FigureType.BASKET) {
                // Resolve basket collision
                Collision.resolveBasketBallCollision(others.get(i), this);
            } else {
                // Resolve collision with other balls
                boolean col = Collision.resolveFigureCollision2(this, others.get(i));

                // Re-enable gravity after the collision ( in case it was disabled )
                if (col) {
                    this.getSpeed().setGravity(true);
                    others.get(i).getSpeed().setGravity(true);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        getSpeed().update();

        // If out of the bottom of the screen set to DEAD
        if (getY() > gamePanel.getHeight() + 2 * getRadius()) {
            setState(FigureState.DEAD);
            figureContainer.decreaseLives(1);
        }
    }

    @Override
    public void handleActionDoubleDown(int eventX, int eventY) {

    }

    @Override
    public void handleActionMove(int eventX, int eventY) {

    }

    @Override
    public void handleActionDown(int eventX, int eventY) {

    }

    @Override
    public void handleActionUp(int eventX, int eventY) {
    }
}