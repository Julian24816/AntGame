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

    private final int clickAcecptionRadius = 30;

    //Zeit zu der der Finger das display berührt
    private long timeDown = 0;
    //zeit ab der ein druck als langer druck gezählt wird in nano sekunden
    //                               M  k
    private final int longTouch = 500000000;
    private float downX,downY;
    private float oldX,oldY;

    public GameEngine(GameView gameView, World world) {
        this.world = world;
        this.gameView = gameView;
        System.out.println(mischen(new String[]{"Apfel","Birne","Banane","Erdbeere","Rosinen","Pflaume"},0,""));
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
        checkLongTouch();

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

    public void click(Position p){
        System.out.println(p.getX()+ " , "+p.getY());
        float nestX = world.getNest().getPosition().getX();
        float nestY = world.getNest().getPosition().getY();
        if(!world.showShop()&&samePosition(p,world.getNest().getPosition(),clickAcecptionRadius)){
            world.setShowShop(true);
        }else if(world.showShop()&&(p.getX()<nestX-gameView.getShopWidth()/2||p.getX()>nestX+gameView.getShopWidth()/2
                ||p.getY()<nestY-gameView.getShopHeight()/2||p.getY()>nestY+gameView.getShopHeight()/2)){
            world.setShowShop(false);
        }else if(world.showShop()){
            int line = (int)( ( nestY+gameView.getShopHeight()/2-p.getY() ) / gameView.getShopItemHeight() );
            //System.out.println(world.getShopList()[line]);
            String answer = world.buy(world.getShopList().get(line));
            gameView.writeTempText(answer);
        }
    }

    public String mischen(String[] zutaten, int aktZutat, String ergebnis){
        if(aktZutat<zutaten.length){
            ergebnis += zutaten[aktZutat];
            return mischen(zutaten, aktZutat+1, ergebnis);
        }else{
            return "Obstsalat";
        }
    }

    public static boolean samePosition(float x1, float y1, float x2, float y2, double nearTargetVariable){

        boolean nearX = -nearTargetVariable<x2-x1&&x2-x1<nearTargetVariable;
        boolean nearY = -nearTargetVariable<y2-y1&&y2-y1<nearTargetVariable;
        if(nearX&&nearY)
            return true;
        else
            return false;
    }

    public static boolean samePosition(Position position1, Position position2, double nearTargetVariable){

       return samePosition(position1.getX(),position1.getY(),position2.getX(),position2.getY(), nearTargetVariable);

    }

    public void checkLongTouch(){
        if(timeDown!=0){
            long deltaTime = System.nanoTime()-timeDown;
            //long Touch
            if(deltaTime>longTouch){
                timeDown = 0;
            }
        }

    }

    public void touchEvent(MotionEvent event){

        if(event.getAction()==MotionEvent.ACTION_UP) {
            long deltaTime = System.nanoTime()-timeDown;
            timeDown = 0;

            //Short Touch
            if(deltaTime<longTouch){
                click(gameView.getWorldPosition(event.getX(),event.getY()));
            }

        }else if(event.getAction()==MotionEvent.ACTION_DOWN) {
            timeDown = System.nanoTime();
            downX = oldX = event.getX();
            downY = oldY = event.getY();
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){

            //Bewegung
            if(!samePosition(downX,downY,event.getX(),event.getY(),20)){
                timeDown=0;

                float dx = event.getX() - oldX;
                float dy = event.getY() - oldY;

                gameView.addShifting(dx,dy);

                oldX = event.getX();
                oldY = event.getY();
            }
        }

                float x = event.getX();
                float y = event.getY();
                //if(event.getAction()==MotionEvent.ACTION_DOWN)
                //gameEngine.click(gameView.getWorldPosition(x,y));

    }

}
