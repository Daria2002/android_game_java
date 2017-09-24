package suza.project.wackyballs.model.figures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import suza.project.wackyballs.model.components.AbstractFigure;
import suza.project.wackyballs.model.properties.FigureState;
import suza.project.wackyballs.model.properties.FigureType;
import suza.project.wackyballs.model.properties.MySpeed;

/**
 * Figure adapter for AbstractFigure class. Leaves all abstract methods of inherited
 * class unimplemented.
 *
 * Created by lmark on 14/09/2017.
 */

public class StaticFigure extends AbstractFigure {

    private MySpeed stateicSpeed = new MySpeed(0, 0) {
        @Override
        public void increaseSpeed(double xv, double yv) {
            // Unimplemented
        }

        @Override
        public void reduceSpeed(double xv, double yv) {
            // unimplemented
        }

        @Override
        public void setX(double x) {
            // Unimplemented
        }

        @Override
        public void setY(double y) {
            // Unimplemented
        }

    };

    public StaticFigure() {
        super(null, 0, 0);
        setSpeed(new MySpeed(0, 0));

        // in order to avoid overriding methods and leaving them unimplemented
        setType(FigureType.STATIC_BALL);
        setState(FigureState.ALIVE);
    }

    @Override
    public MySpeed getSpeed() {

        // Always return zero speed
        return stateicSpeed;
    }

    @Override
    public void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> others) {
        // unimplemented
    }

    @Override
    public void handleActionMove(int eventX, int eventY) {
        // unimplemented
    }

    @Override
    public void handleActionDown(int eventX, int eventY) {
        // unimplemented
    }

    @Override
    public void handleActionDoubleDown(int eventX, int eventY) {

    }

    @Override
    public void handleActionUp(int eventX, int eventY) {
        // unimplemented
    }

    @Override
    public void draw(Canvas canvas) {
        // Unimplemented
    }

    @Override
    public void update() {
        // Unimplemented
    }

}
