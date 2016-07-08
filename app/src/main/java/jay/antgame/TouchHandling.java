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

    //World Object, falls eines durch antippen ausgewählt wurde
    private WorldObject selectedObject = null;

    //Zeit zu der der Finger das display berührt
    private long timeDown = 0;

    //zeit ab der ein druck als langer druck gezählt wird in nano sekunden
    //                               M  k
    private final int longTouch = 500000000;
    //Radius um ein Object in dem der Touch immer noch für das Object zählt
    private final int touchAccuracy = 30;
    //x und y von bildschirm berührung
    private float downX,downY;
    private float oldX,oldY;

    public TouchHandling(World world, MenuManager menuManager){
        this.world = world;
        this.menuManager = menuManager;
    }

    public void setGameView(GameView gameView){ this.gameView = gameView; }

    /**
     *
     * @param event MotionEvent welches von Main Activity übergeben wird
     * @see MainActivity
     */
    public boolean touchEvent(MotionEvent event){

        //Überpfüt welche art von MotionEvent es ist
        //Wenn Finger von display entfernt
        if(event.getAction()==MotionEvent.ACTION_UP) {
            long deltaTime = System.nanoTime()-timeDown;
            timeDown = 0;

            //Wenn die verstrichene zeit zwischen finger aus display und finger von display kürzer als longTouch ist es ein kurzer Touch und click wird
            //mit den World Koordinaten aufgerufen
            //Short Touch
            if(deltaTime<longTouch){
                click(gameView.getWorldPosition(event.getX(),event.getY()));
            }

            //Wenn finger aus display gelegt
        }else if(event.getAction()==MotionEvent.ACTION_DOWN) {
            timeDown = System.nanoTime();
            downX = oldX = event.getX();
            downY = oldY = event.getY();

            //wenn finger aus display bewegt
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){

            //Bewegung
            if(!Methods.samePosition(downX,downY,event.getX(),event.getY(),20)){
                //Es wird geschaut, in welche richtung die bewegung geht und danach in GameView die verschiebung des sichtbereiches verändert

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

        return true; // = success
    }

    /**
     * wird jeden tick von GameEngine aufgerufen um zu schauen ob eine lange Bildschirm berührung stattfand
     * tests if the current touch is a long touch
     * is long touch if touch lasts longer than longTouch
     */
    public void checkLongTouch(){

        //Wenn die zeit nicht null ist (0, wenn keine berührung stattgefunden)
        if(timeDown!=0){
            long deltaTime = System.nanoTime()-timeDown;

            //wenn die verstrichene zeit länger als long touch ist
            if(deltaTime>longTouch){

                //World Clicks ist ein Interface, das implementiert wird, wenn das object zwichen kuren und langen klicks unterscheiden soll
                //ist ein solches object in der umgebung der berühung wird die methode longClick dieses aufgerufen
                if(selectedObject!=null&&selectedObject instanceof WorldClicks)
                    ((WorldClicks) selectedObject).longClick(gameView.getWorldPosition(downX,downY));

                timeDown = 0;
            }
        }

    }

    /**
     * Managed alle kurzen klicks und gibt diese an das entsprechende object weiter
     * @param p World position, where click happend
     */
    public void click(Position p){

        //Wenn nicht ein Object mit Interface WorldClicks ausgewählt ist, welches auch kurze berührungen in der welt benutzt
        if( !(selectedObject instanceof WorldClicks) ) {

            selectedObject = null;
            //Es wird für alle world objects (Alle Object außer menüs) geschaut ob es sich in der nähe des clicks befindet
            //wenn ja wird dieses als selected object gesetzt
            for (WorldObject object : world.getWorldObjects()) {

                if (Methods.samePosition(p, object.getPosition(), touchAccuracy)) {
                    selectedObject = object;
                }

            }

            //Wurde ein object gefunden werden alle menüs unsichtbar gemacht
            if (selectedObject!=null) {
                menuManager.allMenusInvisible();
            }else{
                //ansonsten werden die menüs durchsucht ob vielleicht ein solches angeklickt wurde
                for (Menu menu : menuManager.getAllMenus()) {

                    if (menu.showList() && menu.insideBounds(p, gameView)) {
                        selectedObject = menu;
                    }

                }
            }
            //Wurde kein object gefunden werden alle menüs unsichtbar gemacht, wurde eines gefunden wird dessen click methode aufgerufen
            if(selectedObject==null) {
                menuManager.allMenusInvisible();
            }else
                selectedObject.click(p);

        }else{
            //Wenn ein Object mit dem Interface wordclick ausgewählt ist und dieses sich im bereich der berührung befindet,
            //wird es nicht mehr ausgewählt.
            //Ist die berührung nicht in seinem bereich wird die methode click des objects aufgerufen
            if(Methods.samePosition(p, selectedObject.getPosition(), touchAccuracy)){
                selectedObject = null;
            }else{
                selectedObject.click(p);
            }
        }
    }

    public WorldObject getSelectedObject(){ return selectedObject; }

}
