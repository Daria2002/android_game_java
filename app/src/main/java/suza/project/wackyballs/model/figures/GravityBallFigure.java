package suza.project.wackyballs.model.figures;

import android.graphics.BitmapFactory;

import java.util.List;

import suza.project.wackyballs.R;
import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.components.AbstractAnimation;
import suza.project.wackyballs.model.components.AbstractFigure;
import suza.project.wackyballs.model.properties.Collision;
import suza.project.wackyballs.model.properties.FigureState;
import suza.project.wackyballs.model.properties.FigureType;
import suza.project.wackyballs.model.properties.MySpeed;
import suza.project.wackyballs.util.Util;

/**
 * Ball affected by gravity.
 *
 * Created by lmark on 13/09/2017.
 */

public class GravityBallFigure extends AbstractAnimation {

    public static final String TAG = GravityBallFigure.class.getSimpleName();

    public GravityBallFigure(GamePanel gamePanel) {
        super(BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.face_animation),
                Util.randomInteger(0, gamePanel.getScreenWidth()), -50,
                10, 4);
        super.setSpeed(new MySpeed(
                Util.randomInteger(-7, 7),
                Util.randomInteger(2, 5)
        ));

        super.getSpeed().setGravity(true);
        setState(FigureState.ALIVE);
        setType(FigureType.BALL);
    }

    @Override
    public void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> others) {
        int collision = Collision.resolveWallCollision(screenWidth, screenHeight, this);

        // When bottom collision occurs disable gravity and speed
        // if the total speed is low enough.
        if (collision == Collision.BOTTOM_COLLISION && getSpeed().getAmplitude() < 1) {
            getSpeed().setGravity(false);
            getSpeed().setX(0);
            getSpeed().setY(0);
        }

        // Resolve collision
        for (int i = 0, count = others.size(); i < count; i++) {

            if (this.getID() == others.get(i).getID()) {
                continue;
            }

            boolean colHappened = Collision.resolveFigureCollision2(this, others.get(i));

            // If collision happened re-enable gravity in case it was disabled
            if (colHappened) {
                getSpeed().setGravity(true);
                others.get(i).getSpeed().setGravity(true);
            }
        }
    }

    @Override
    public void handleActionMove(int eventX, int eventY) {
        // Unimplemented
    }

    @Override
    public void handleActionDown(int eventX, int eventY) {
        // Unimplemented
    }

    @Override
    public void handleActionDoubleDown(int eventX, int eventY) {

    }

    @Override
    public void handleActionUp(int eventX, int eventY) {
        // Unimplemented
    }

    @Override
    public void update() {
        super.update();
        getSpeed().update();
    }
}
