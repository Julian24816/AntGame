package jay.antgame.data;

import java.util.List;

/**
 * Created by Julian on 24.06.2016.
 */
public class World {

    private List<Ant> ants;
    private List<FoodSource> foodSources;
    private List<ScentTrail> scentTrails;
    private Nest nest;
    private int food;

    private int width;
    private int height;

    private int workerLevel = 1;
    private int workerFoodCapacity = 1;

    private double workerMovementSpeed = 3;
    private double workerNearFoodVariable = 20;

    private int workerCost = 10;
    private int expWorkerCostIncrecment = 2;

    private int workerLevelCost = 10;
    private final int workerExpLevelCostIncrecment = 3;
    private final int workerExpFoodCapacityIncrecmentPLevel = 2;

    private boolean showShop = false;
    private final String[] shopList = new String[]{"Buy Worker:Spawn 1 working ant","More Ants:Increase amount of ants"};
    private String[] shownShopList = shopList;

    public World(List<Ant> ants, List<FoodSource> sources, List<ScentTrail> trails, Nest nest) {
        this.ants = ants;
        foodSources = sources;
        scentTrails = trails;
        this.nest = nest;
        food = 0;
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
        if(food>=workerCost){
            ants.add(new Worker(nest.getPosition().clone()));
            food -= workerCost;
            workerCost *= expWorkerCostIncrecment;
            return "One Worker spawned";
        }else{
            return "You need "+(workerCost-food)+" more Food";
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

    public String[] getShopList(){ return shownShopList; }

    public boolean showShop(){ return showShop; }

    public void setShowShop(boolean show){ showShop= show; }

    public void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }

}
