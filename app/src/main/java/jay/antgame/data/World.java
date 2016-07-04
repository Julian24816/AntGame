package jay.antgame.data;

import java.util.ArrayList;
import java.util.List;

import jay.antgame.data.menus.MenuManager;

/**
 * Created by Julian on 24.06.2016.
 */
public class World {

    private List<WorldObject> objects = new ArrayList<>();
    private List<Ant> ants;
    private List<FoodSource> foodSources;
    private List<ScentTrail> scentTrails;
    private Nest nest;
    private int food;

    private int width = 2000;
    private int height = 1000;

    private WorldObject selectedObject = null;

    private int workerLevel = 1;
    private int workerFoodCapacity = 1;

    private double workerMovementSpeed = 3;
    private double workerNearFoodVariable = 20;

    private int expWorkerCostIncrecment = 2;

    private int workerLevelCost = 10;
    private final int workerExpLevelCostIncrecment = 3;
    private final int workerExpFoodCapacityIncrecmentPLevel = 2;

    private MenuManager menuManager;

    private boolean showShop = false;
    private String[] shopList = new String[]{"Buy Worker:Spawn 1 working ant","More Ants:Increase amount of ants"};
    private int[] shopCosts = new int[]{10,0};
    private ArrayList<String> shownShopList = new ArrayList<String>();

    public World(List<Ant> ants, List<FoodSource> sources, List<ScentTrail> trails, Nest nest) {
        menuManager = new MenuManager(this);
        this.ants = ants;
        foodSources = sources;
        scentTrails = trails;
        this.nest = nest;
        nest.setWorld(this);
        objects.addAll(sources);
        objects.add(nest);
        objects.addAll(ants);
        food = 0;
        for(String s: shopList)
            shownShopList.add(s);
    }

    /*
     * to do:
     * -----
     *
     * Constructor with DataManagerObject as parameter
     * -> fills the World object with the data from the Database using the DataManagers
     * getAntPositions(), etc. Methods
     *
     */

    public List<Ant> getAnts() {
        return ants;
    }

    public List<FoodSource> getFoodSources() {
        return foodSources;
    }

    public List<ScentTrail> getScentTrails() {
        return scentTrails;
    }

    public Nest getNest() {
        return nest;
    }

    public int getFood() {
        return food;
    }

    public void addFood(int f) {
        food = food + f;
    }

    public void addAnt(Ant ant){ ants.add(ant); }

    public int getWorkerLevel(){ return workerLevel; }

    public String buyWorker(){
        if(food>=shopCosts[0]){
            ants.add(new Worker(nest.getPosition().clone()));
            food -= shopCosts[0];
            shopCosts[0] *= expWorkerCostIncrecment;
            //aktShopList(shopList[0],"Buy Worker ("+workerCost+"):Spawn 1 working ant");
            return "One Worker spawned";
        }else{
            return "You need "+(shopCosts[0]-food)+" more Food";
        }
    }

    public void aktShopList(String old, String newS){
        for(int i=0;i<shopList.length;i++){
            if(i<shownShopList.size()){
                if(shownShopList.get(i).equals(old))
                    shownShopList.set(i,newS);
            }
            if(shopList[i].equals(old))
                shopList[i] = newS;
        }
    }

    public String buy(String order){
        int id = 0;
        for(int i=0;i<shopList.length;i++){
            if(shopList[i].equals(order)){
                id = i;
                break;
            }
        }
        switch (id) {
            case 0:
                return buyWorker();
        }
        return "";
    }

    public void increaseWorkerLevel(){ workerLevel+= 1; workerFoodCapacity*= workerExpFoodCapacityIncrecmentPLevel; }

    public int getWidth(){ return width; }

    public int getHeight(){ return  height; }

    public int getWorkerFoodCapacity(){ return workerFoodCapacity; }

    public double getWorkerMovmentSpeed(){ return workerMovementSpeed; }

    public double getWorkerNearFoodVariable(){ return workerNearFoodVariable; }

    public List<String> getShopList(){ return shownShopList; }

    public int[] getShopCosts(){ return shopCosts; }

    public boolean showShop(){ return showShop; }

    public void setShowShop(boolean show){ showShop= show; }

    public List<WorldObject> getWorldObjects(){ return objects; }

    public WorldObject getSelectedObject(){ return selectedObject; }

    public void setSelectedObject(WorldObject object){ selectedObject = object; }

    public MenuManager getMenuManager(){ return menuManager; }

    public void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }

}
