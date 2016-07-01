package jay.antgame;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
    private ScheduledExecutorService executorService;
/* currently unused
    private Handler handler = new Handler();
    private run()
        // push changes to gameView
        handler.post(new Runnable() {
            @Override
            public void run() {
                // call the gameViews update methods
            }
        });
 */

    private GameView gameView;
    private World world;

    private final int expWorkerCostsImcrecment = 2;
    private final int expWorkerLevelCostIncrecment = 3;

    private int workerUpgradeCost = 10;
    private int workerCost = 1;

    public GameEngine(GameView gameView, World world) {
        this.world = world;
        this.gameView = gameView;
        world.setDimension(gameView.getWorldWidth(),gameView.getWorldHeight());
    }

    public void start() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this,
                MS_PER_FRAME, MS_PER_FRAME, TimeUnit.MILLISECONDS);
    }

    public void resume() {
        Log.d("antgame", "GameEngine.resume was called");
        this.start();
    }

    public void stop() {
        executorService.shutdown();
    }

    @Override
    public void run() {

        tickAnts();

        // check for gameOver?
        // stop()
    }

    public boolean buyWorker(){
        if(world.getFood()>=workerCost){
            world.addAnt(new Worker(world.getNest().getPosition()));
            world.addFood(-workerCost);
            workerCost *= expWorkerCostsImcrecment;
            return true;
        }else{
            return false;
        }
    }

    public boolean upgradeWorker(){
        if(world.getFood()>=workerUpgradeCost){
            world.increaseWorkerLevel();
            world.addFood(-workerUpgradeCost);
            workerUpgradeCost *= expWorkerLevelCostIncrecment;
            return true;
        }else{
            return false;
        }
    }

    public int getWorkerCost(){
        return workerCost;
    }

    private void tickAnts(){
        for(Ant ant: world.getAnts()){
            ant.tick(world);
        }
    }

}
