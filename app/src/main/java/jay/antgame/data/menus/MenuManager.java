package jay.antgame.data.menus;

import jay.antgame.GameView;
import jay.antgame.data.Position;
import jay.antgame.data.Worker;
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

    public void setGameView(GameView gameView){
        this.gameView = gameView;
    }

    public void itemClicked(Menu menu, Position p){

    }

    public void itemClicked(Menu menu, int line){

        String outText = "";
        int id = menu.getIdsOfShownOffersList().get(line);

        if(menu==nestMenu){
            switch (id){
                case 0:
                    outText = buyWorker(id);
            }
        }

        gameView.writeTempText(outText);
    }

    public Menu getNestMenu(){ return nestMenu; }

    public String buyWorker(int id){

        if(world.getNest().getFood()>=nestMenu.getCosts()[id]){
            world.addWorker();
            world.getNest().addFoodAmount(-nestMenu.getCosts()[id]);
            nestMenu.getCosts()[id] *= Worker.getExtCostIncrecment();
            return "One Worker spawned";
        }else{
            return "You need "+(nestMenu.getCosts()[id]-world.getFood())+" more Food";
        }

    }

    public String buyAnt(int id){

        return "";

    }

    public Menu[] getAllMenus(){
        return allMenus;
    }

    public void allMenusInvisible(){
        for(Menu menu: allMenus)
            menu.setShow(false);
    }

}
