package jay.antgame;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jay.antgame.data.Ant;
import jay.antgame.data.Position;
import jay.antgame.data.Worker;
import jay.antgame.data.World;
import jay.antgame.data.WorldObject;
import jay.antgame.data.menus.Menu;

/**
 * Created by Julian on 09.06.2016.
 *
 * @author Yannick
 */
public class GameEngine implements Runnable {

    private static final long MS_PER_FRAME = 40;
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
    private TouchHandling touchHandling;
    private World world;

    public GameEngine(GameView gameView, World world, TouchHandling touchHandling) {
        this.world = world;
        this.gameView = gameView;
        this.touchHandling = touchHandling;
        Log.d("antgame",mischen(
                new String[]{"Apfel","Birne","Banane","Erdbeere","Rosinen","Pflaume"},0,""));
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


    public World getWorld() {
        return world;
    }


    @Override
    public void run() {

        tickAnts();
        touchHandling.checkLongTouch();

        // check for gameOver?
        // stop()
    }

    private void tickAnts(){
        for(Ant ant: world.getAnts()){
            ant.tick(world);
        }
    }

    /**
     * creates a mix of all ingredients
     * @param ingredients List of ingredients being mixed
     * @param aktZutat position of the string which gets thowen in the mixer next
     * @param ergebnis current state of the mix
     * @return returns a mix of all ingredients
     */
    public String mischen(String[] ingredients, int aktZutat, String ergebnis){
        if(aktZutat<ingredients.length){
            ergebnis += " "+ingredients[aktZutat];
            return mischen(ingredients, aktZutat+1, ergebnis);
        }else{
            return "Obstsalat =" + ergebnis;
        }
    }

}