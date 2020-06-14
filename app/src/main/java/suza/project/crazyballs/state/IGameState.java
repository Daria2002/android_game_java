package suza.project.crazyballs.state;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * This interface defines basic methods used by the game panel.
 *
 * Created by lmark on 16/08/2017.
 */

public interface IGameState {

    /**
     * Draw current objects on screen.
     *
     * @param canvas Canvas reference.
     */
    void draw(Canvas canvas);

    /**
     * Update game logic and object positions.
     */
    void update();

    /**
     * Determine what happens when user touches the screen.
     *
     * @param event Motion event reference.
     */
    boolean onTouchEvent(MotionEvent event);
}
