package jay.antgame;

import android.view.MotionEvent;

import jay.antgame.data.Position;
import jay.antgame.data.World;
import jay.antgame.data.WorldClicks;
import jay.antgame.data.WorldObject;
import jay.antgame.data.menus.Menu;
import jay.antgame.data.menus.MenuManager;

/**
 * Created by Yannick.Pfeiffer on 06.07.2016.
 *
 * @author Yannick
 */

public class TouchHandling {

    private GameView gameView;
    private MenuManager menuManager;
    private World world;

    private WorldObject selectedObject = null;

    //Zeit zu der der Finger das display berührt
    private long timeDown = 0;
    //zeit ab der ein druck als langer druck gezählt wird in nano sekunden
    //                               M  k
    private final int longTouch = 500000000;
    private final int touchAccuracy = 30;
    private float downX,downY;
    private float oldX,oldY;

    public TouchHandling(World world, MenuManager menuManager){
        this.world = world;
        this.menuManager = menuManager;
    }

    public void setGameView(GameView gameView){ this.gameView = gameView; }

    /**
     *
     * @param event MotionEvent which gets
     */
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
            if(!Methods.samePosition(downX,downY,event.getX(),event.getY(),20)){
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

    /**
     * tests if the current touch is a long touch
     * is long touch if touch lasts longer than longTouch
     */
    public void checkLongTouch(){
        if(timeDown!=0){
            long deltaTime = System.nanoTime()-timeDown;

            if(deltaTime>longTouch){

                if(selectedObject!=null&&selectedObject instanceof WorldClicks)
                    ((WorldClicks) selectedObject).longClick(gameView.getWorldPosition(downX,downY));

                timeDown = 0;
            }
        }

    }

    public void click(Position p){

        if( !(selectedObject instanceof WorldClicks) ) {

            selectedObject = null;
            for (WorldObject object : world.getWorldObjects()) {

                if (Methods.samePosition(p, object.getPosition(), touchAccuracy)) {
                    selectedObject = object;
                }

            }

            if (selectedObject!=null) {
                menuManager.allMenusInvisible();
                selectedObject.click(p);
            }else{
                for (Menu menu : menuManager.getAllMenus()) {

                    if (menu.showList() && menu.insideBounds(p, gameView)) {
                        menu.click(p);
                        selectedObject = menu;
                    }

                }
            }
            if(selectedObject==null) {
                menuManager.allMenusInvisible();
            }

        }else{
            if(Methods.samePosition(p, selectedObject.getPosition(), touchAccuracy)){
                selectedObject = null;
            }else{
                selectedObject.click(p);
            }
        }
    }

    public WorldObject getSelectedObject(){ return selectedObject; }

}
