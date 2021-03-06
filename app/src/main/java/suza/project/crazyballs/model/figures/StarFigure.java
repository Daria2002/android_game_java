package suza.project.crazyballs.model.figures;

import android.graphics.BitmapFactory;

import java.util.List;

import suza.project.crazyballs.R;
import suza.project.crazyballs.game.GamePanel;
import suza.project.crazyballs.model.components.AbstractAnimation;
import suza.project.crazyballs.model.components.AbstractFigure;
import suza.project.crazyballs.model.containers.BasketBallContainer;
import suza.project.crazyballs.model.properties.Collision;
import suza.project.crazyballs.model.properties.FigureState;
import suza.project.crazyballs.model.properties.FigureType;
import suza.project.crazyballs.model.properties.MySpeed;
import suza.project.crazyballs.util.Util;

/**
 * Figure provides user 1 extra heart when deposited in the basket.
 * Used in BasketBall game.
 *
 * Created by lmark on 16/09/2017.
 */

public class StarFigure extends AbstractAnimation {

    BasketBallContainer figureContainer;
    GamePanel gamePanel;

    public StarFigure(GamePanel gamePanel, BasketBallContainer figureContainer) {
        super(BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.star_animation),
                Util.randomInteger(0, gamePanel.getScreenWidth()), -50,
                10, 4);
        super.setSpeed(new MySpeed(
                Util.randomInteger(-10, 10),
                Util.randomInteger(2, 5)
        ));

        this.figureContainer = figureContainer;
        this.gamePanel = gamePanel;
        super.getSpeed().setGravity(true);
        setState(FigureState.ALIVE);
        setType(FigureType.STAR);
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
        if (getY() > gamePanel.getHeight() + 2*getRadius()) {
            setState(FigureState.DEAD);
        }
    }
}
