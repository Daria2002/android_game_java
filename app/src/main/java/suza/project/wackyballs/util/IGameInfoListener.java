package suza.project.wackyballs.util;

/**
 * Listener used for notifications when various game information changes.
 *
 * Created by lmark on 19/09/2017.
 */

public interface IGameInfoListener {

    /**
     * Lives changed action.
     *
     * @param amount Amount life changed.
     */
    void onLivesChanged(int amount);

    /**
     * Score changed action.
     *
     * @param amount Amount score changed.
     */
    void onScoreChanged(int amount);
}
