package jay.antgame;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jay.antgame.data.Ant;
import jay.antgame.data.Worker;
import jay.antgame.data.World;

/**
 * Created by Julian on 09.06.2016.
 */
public class GameEngine implements Runnable {

    private static final long MS_PER_FRAME = 50;
    private GameView gameView;
    private ScheduledExecutorService executorService;
    private Handler handler = new Handler();

    private final int expWorkerCostsImcrecment = 2;

    private int workerCost = 1;

    private World world = null;

    public GameEngine(GameView gameView) {
        this.gameView = gameView;
    }

    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();


        executorService.scheduleAtFixedRate(this,
                MS_PER_FRAME, MS_PER_FRAME, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executorService.shutdown();
    }

    @Override
    public void run() {

        if(world == null)
            return;

        tickAnts();



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

    public boolean buyWorker(){
        if(world.getFood()>=workerCost){
            world.addAnt(new Worker());
            world.addFood(-workerCost);
            workerCost *= expWorkerCostsImcrecment;
            return true;
        }else{
            return false;
        }
    }



    private void tickAnts(){
        for(Ant ant: world.getAnts()){
            ant.tick();
        }
    }

    public void setWorld(World world){
        this.world = world;
    }
}
