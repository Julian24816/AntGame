package jay.antgame;

import android.content.Context;
import android.os.Handler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Julian on 09.06.2016.
 */
public class GameEngine implements Runnable {

    private static final long MS_PER_FRAME = 50;
    private GameView gameView;
    private ScheduledExecutorService executorService;
    private Handler handler = new Handler();

    public GameEngine(GameView gameView) {
        this.gameView = gameView;
    }

    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        /*
         * do initialization stuff
         */
        executorService.scheduleAtFixedRate(this,
                MS_PER_FRAME, MS_PER_FRAME, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executorService.shutdown();
    }

    @Override
    public void run() {
        /*
         * do engine stuff that needs to be done while game is running
         */

        // push changes to gameView
        handler.post(new Runnable() {
            @Override
            public void run() {
                /*
                 * call the gameViews update methods
                 */
            }
        });

        // check for gameOver?
        // stop()
    }
}
