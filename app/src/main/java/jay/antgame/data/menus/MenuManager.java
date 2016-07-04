package jay.antgame.data.menus;

import jay.antgame.GameView;
import jay.antgame.data.World;

/**
 * Created by Yannick on 04.07.2016.
 */
public class MenuManager {

    GameView gameView;
    World world;
    Menu nestMenu;
    Menu[] allMenus = new Menu[1];

    public MenuManager(World world){
        this.world = world;
        String[] nestOffers = new String[]{"Buy Worker:Spawn 1 working ant","More Ants:Increase amount of ants"};
        int[] nestCosts = new int[]{10,0};
        nestMenu = new Menu("Nest",this,nestOffers,nestCosts);
        allMenus[0] = nestMenu;
    }

    public void itemClicked(Menu menu, int line){
        String outText = "";
        String order = menu.getList().get(line);
        if(menu==nestMenu){
            int id = 0;
            for(int i=0;i<menu.getFullList().length;i++){
                if(menu.getFullList()[i].equals(order)){
                    id = i;
                    break;
                }
            }
            switch (id) {
                case 0:
                    outText = world.buyWorker();
            }
            gameView.writeTempText(outText);
        }
    }

    public void setGameView(GameView gv){
        this.gameView = gv;
    }

    public Menu[] getAllMenus(){
        return allMenus;
    }

    public void allMenusInvisible(){
        for(Menu menu: allMenus)
            menu.setShow(false);
    }

    public Menu getNestMenu(){ return nestMenu; }

}
