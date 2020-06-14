package suza.project.wackyballs.util;

/**
 * Listener signaling if game is finished.
 *
 * Created by lmark on 19/09/2017.
 */

public interface IGameFinishedListener {

    /**
     * Method will be called when game is finished.
     *
     * @param score Current game score.
     */
    void gameFinished(int score);
}
