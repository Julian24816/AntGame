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
    Menu builderMenu;
    Menu[] allMenus = new Menu[2];

    public MenuManager(World world){
        this.world = world;

        //Nest Memu
        String[] offers = new String[]{"Buy Worker:Spawn 1 working ant","Buy Builder"};
        int[] costs = new int[]{10,50};
        nestMenu = new Menu("Nest",this,offers,costs);

        //Builder Menu
        offers = new String[]{"Evolution Chamber: Offers Upgrades"};
        costs = new int[]{50};
        builderMenu = new Menu("Builder",this,offers,costs);

        allMenus[0] = nestMenu;
        allMenus[1] = builderMenu;
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
            /*switch (id){
                case 0:
                    outText = buyWorker(id);
            }*/
            buyAnt(id);
        }

        gameView.writeTempText(outText);
    }

    public String buyAnt(int id){

        if(world.getNest().getFood()>=nestMenu.getCosts()[id]){

            world.getNest().addFoodAmount(-nestMenu.getCosts()[id]);

            switch (id){
                case 0:
                    buyWorker(id);
                case 1:
                    buyBuilder(id);
            }

            return "One Worker spawned";
        }else{
            return "You need "+(nestMenu.getCosts()[id]-world.getFood())+" more Food";
        }

    }

    public void buyWorker(int id){
        nestMenu.getCosts()[id] *= Worker.getExtCostIncrecment();
        world.addWorker();
    }

    public void buyBuilder(int id){
        nestMenu.getIdsOfShownOffersList().remove(id);
        world.addBuilder();
    }



    public Menu[] getAllMenus(){
        return allMenus;
    }

    public void allMenusInvisible(){
        for(Menu menu: allMenus)
            menu.setShow(false);
    }

    public Menu getNestMenu(){ return nestMenu; }

    public Menu getBuilderMenu(){ return builderMenu; }

}
