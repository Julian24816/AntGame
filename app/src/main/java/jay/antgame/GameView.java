package jay.antgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jay.antgame.data.Ant;
import jay.antgame.data.FoodSource;
import jay.antgame.data.Position;
import jay.antgame.data.ScentTrail;
import jay.antgame.data.ScreenPosition;
import jay.antgame.data.World;

/**
 * Created by Julian on 09.06.2016.
 */
public class GameView extends SurfaceView
        implements SurfaceHolder.Callback{

    private static final long FRAME_INTERVAL = 20;

    private static final int NEST_SIZE = 50;
    private static final int FOOD_SOURCE_SIZE = 30;
    private static final int SCENT_TRAIL_SIZE = 10;
    private static final int ANT_SIZE = 5;

    private final float density;
    private final int height, width;

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

    private World gameWorld;
    private Paint paintNest = new Paint();
    private Paint paintFoodSource = new Paint();
    private Paint paintScentTrail = new Paint();
    private Paint paintAnt = new Paint();


    public GameView(Context context, World gameWorld) {
        super(context);

        this.gameWorld = gameWorld;

        // get metrics
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        density = displayMetrics.density;
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        // initialise Paint-Objects
        paintNest.setAntiAlias(true);
        paintNest.setColor(Color.DKGRAY);
        paintNest.setStyle(Paint.Style.FILL);

        paintFoodSource.setAntiAlias(true);
        paintFoodSource.setColor(Color.YELLOW);
        paintFoodSource.setStyle(Paint.Style.FILL);

        paintScentTrail.setAntiAlias(true);
        paintScentTrail.setColor(Color.GREEN);
        paintScentTrail.setStyle(Paint.Style.FILL);

        paintAnt.setAntiAlias(true);
        paintAnt.setColor(Color.BLACK);
        paintAnt.setStyle(Paint.Style.FILL);

        getHolder().addCallback(this);
    }

    private ScreenPosition getScreenCoordinates(Position pos) {

        float x = width/2 + pos.getX()*density;
        float y = height/2 - pos.getY()*density;

        return new ScreenPosition(x,y);
    }


    private void doDraw(Canvas canvas) {

        // draw Background
        canvas.drawColor(Color.WHITE);

        // draw Nest
        Position nestPos = getScreenCoordinates(gameWorld.getNest().getPosition());
        canvas.drawCircle(nestPos.getX(), nestPos.getY(), NEST_SIZE*density/2, paintNest);

        // draw FoodSources
        for (FoodSource source: gameWorld.getFoodSources()) {
            Position sourcePos = getScreenCoordinates(source.getPosition());
            canvas.drawCircle(sourcePos.getX(), sourcePos.getY(), FOOD_SOURCE_SIZE*density/2,
                    paintFoodSource);
        }

        // draw ScentTrails
        for (ScentTrail trail: gameWorld.getScentTrails()) {
            Position sourcePos = getScreenCoordinates(trail.getPosition());
            canvas.drawCircle(sourcePos.getX(), sourcePos.getY(), SCENT_TRAIL_SIZE*density/2,
                    paintScentTrail);
        }

        // draw FoodSources
        for (Ant ant: gameWorld.getAnts()) {
            Position sourcePos = getScreenCoordinates(ant.getPosition());
            canvas.drawCircle(sourcePos.getX(), sourcePos.getY(), ANT_SIZE*density/2,
                    paintAnt);
        }


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
}
