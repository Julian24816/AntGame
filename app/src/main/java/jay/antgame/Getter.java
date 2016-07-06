package jay.antgame;

import jay.antgame.data.menus.Menu;
import jay.antgame.data.menus.MenuManager;

/**
 * Created by Yannick on 06.07.2016.
 */
public class Getter {

    private static GameView gameView;
    private static GameEngine gameEngine;
    private static TouchHandling touchHandling;
    private static MenuManager menuManager;

    public static void set(GameView gv, GameEngine ge, TouchHandling th, MenuManager mm){
        gameView = gv;
        gameEngine = ge;
        touchHandling = th;
        menuManager = mm;
    }

    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    public static GameView getGameView() {
        return gameView;
    }

    public static MenuManager getMenuManager() {
        return menuManager;
    }

    public static TouchHandling getTouchHandling() {
        return touchHandling;
    }
}
