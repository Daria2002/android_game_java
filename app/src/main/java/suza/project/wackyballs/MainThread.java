package suza.project.wackyballs;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.text.DecimalFormat;

/**
 * This thread is used for running the GameLoop. Game loop will update the game state and
 * render new objects on screen via GamePanel methods.
 *
 * Created by lmark on 02/08/2017.
 */

public class MainThread extends Thread {

    /**
     * Desired application FPS.
     */
    private final static int 	MAX_FPS = 50;
    /**
     *  Maximum number of frames to be skipped
     */
    private final static int	MAX_FRAME_SKIPS = 5;
    /**
     * The frame period
     */
    private final static int	FRAME_PERIOD = 1000 / MAX_FPS;

    /**
     * FPS Decimal font string formatter.
     */
    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    /**
     * Interval the stats are being read.
     */
    private final static int 	STAT_INTERVAL = 1000; //ms
    /**
     * Number of last FPS counts. Used for calculating average FPS.
     */
    private final static int	FPS_HISTORY_NR = 10;
    /**
     * Last time status was stored.
     */
    private long lastStatusStore = 0;
    /**
     * Status time counter.
     */
    private long statusIntervalTimer	= 0L;
    /**
     * Total number of frames skipped.
     */
    private long totalFramesSkipped			= 0L;
    /**
     * Number of frames skipped in a store cycle.
     */
    private long framesSkippedPerStatCycle 	= 0L;

    /**
     * Rendered frames per interval.
     */
    private int frameCountPerStatCycle = 0;
    /**
     * Total number of frames.
     */
    private long totalFrameCount = 0L;
    /**
     * Last N fps values.
     */
    private double 	fpsStore[];
    /**
     * Number of times stat has been read.
     */
    private long 	statsCount = 0;
    /**
     * Average FPS since the game started.
     */
    private double 	averageFps = 0.0;

    /**
     * Surface holder which has access to the game surface.
     */
    private SurfaceHolder surfaceHolder;
    /**
     * Handles inputs and renders stuff on screen.
     */
    private GamePanel gamePanel;

    private boolean running;
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Constructor for the Main thread, initializes class variables.
     *
     * @param surfaceHolder Surface holder reference.
     * @param gamePanel Game panel reference.
     */
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    /**
     * Signals the MainThread that it should start / stop running.
     *
     * @param running True / false for running / stopping.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Run the thread.
     */
    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        // initialise timing elements for stat gathering
        initTimingElements();

        long beginTime;		// the time when the cycle begun
        long timeDiff;		// the time it took for the cycle to execute
        int sleepTime;		// ms to sleep (<0 if we're behind)
        int framesSkipped;	// number of frames being skipped

        sleepTime = 0;

        while (running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing
            // in the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;	// resetting the frames skipped
                    // update game state
                    this.gamePanel.update();

                    // render state to the screen
                    // draws the canvas on the panel
                    this.gamePanel.render(canvas);
                    // calculate how long did the cycle take
                    timeDiff = System.currentTimeMillis() - beginTime;
                    // calculate sleep time
                    sleepTime = (int)(FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        // if sleepTime > 0 we're OK
                        try {
                            // send the thread to sleep for a short period
                            // very useful for battery saving
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {}
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        // we need to catch up
                        this.gamePanel.update(); // update without rendering
                        sleepTime += FRAME_PERIOD;	// add frame period to check if in next frame
                        framesSkipped++;
                    }

                    if (framesSkipped > 0) {
                        Log.d(TAG, "Skipped:" + framesSkipped);
                    }
                    // for statistics
                    framesSkippedPerStatCycle += framesSkipped;
                    // calling the routine to store the gathered statistics
                    storeStats();
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }	// end finally
        }
    }

    /**
     * The statistics - it is called every cycle, it checks if time since last
     * store is greater than the statistics gathering period (1 sec) and if so
     * it calculates the FPS for the last period and stores it.
     *
     *  It tracks the number of frames per period. The number of frames since
     *  the start of the period are summed up and the calculation takes part
     *  only if the next period and the frame count is reset to 0.
     */
    private void storeStats() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        // check the actual time
        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
            // calculate the actual frames pers status check interval
            double actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));

            //stores the latest fps in the array
            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

            // increase the number of times statistics was calculated
            statsCount++;

            double totalFps = 0.0;
            // sum up the stored fps values
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            // obtain the average
            if (statsCount < FPS_HISTORY_NR) {
                // in case of the first 10 triggers
                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }
            // saving the number of total frames skipped
            totalFramesSkipped += framesSkippedPerStatCycle;
            // resetting the counters after a status record (1 sec)
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;

            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
//			Log.d(TAG, "Average FPS:" + df.format(averageFps));
            gamePanel.setAvgFps("FPS: " + df.format(averageFps));
        }
    }

    /**
     * Initialized the FPS array.
     */
    private void initTimingElements() {
        // initialise timing elements
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
        Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialised");
    }
}
