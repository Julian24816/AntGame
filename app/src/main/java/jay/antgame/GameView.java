package jay.antgame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Julian on 09.06.2016.
 */
public class GameView extends SurfaceView
        implements SurfaceHolder.Callback{

    private static final long FRAME_INTERVAL = 20;
    private ScheduledExecutorService executorService;

    private Runnable renderer = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = null;
            try {
                canvas = getHolder().lockCanvas();
                synchronized (getHolder()) {
                    doDraw(canvas);
                }
            } finally {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    };

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(renderer,
                FRAME_INTERVAL, FRAME_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        executorService.shutdown();
    }

    private void doDraw(Canvas canvas) {

    }
}
