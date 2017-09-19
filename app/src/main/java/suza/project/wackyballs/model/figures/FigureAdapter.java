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
 *
 * Created by lmark on 14/09/2017.
 */

public class FigureAdapter extends AbstractFigure {

    public FigureAdapter() {
        super(null, 0, 0);
        setSpeed(new MySpeed(0, 0));
        setType(FigureType.STATIC_BALL);
        setState(FigureState.ALIVE);
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
        //Paint p = new Paint();
        //p.setColor(Color.YELLOW);
        //canvas.drawCircle(getX(), getY(), getRadius(), p);
    }

    @Override
    public void update() {
        // Unimplemented
    }

}
