package suza.project.crazyballs.model.properties;

/**
 * Figure states.
 *
 * Created by lmark on 13/09/2017.
 */

public enum FigureState {

    /**
     * Alive figures will be drawn on the panel.
     */
    ALIVE,

    /**
     * Recently collided figure.
     */
    COLLISION,

    /**
     * Dead figures will not be drawn, and will be removed
     * from the container eventually.
     */
    DEAD
}
