package suza.project.wackyballs.util;

/**
 * Provides an interface for managing game information through information listeners.
 *
 * Created by lmark on 19/09/2017.
 */

public interface IGameInfoProvider {

    /**
     * Adds listener to the internal list.
     *
     * @param listener Game info listener.
     */
    void addGameInfoListener(IGameInfoListener listener);

    /**
     * Removes game info listener from the listener list.
     *
     */
    void removeAllListeners();
}
