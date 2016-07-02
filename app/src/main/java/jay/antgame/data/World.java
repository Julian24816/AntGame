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

    private int workerLevelCost = 10;
    private final int workerExpLevelCostIncrecment = 3;
    private final int workerExpFoodCapacityIncrecmentPLevel = 2;

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

    public void increaseWorkerLevel(){ workerLevel+= 1; workerFoodCapacity*= workerExpFoodCapacityIncrecmentPLevel; }

    public int getWidth(){ return width; }

    public int getHeight(){ return  height; }

    public int getWorkerFoodCapacity(){ return workerFoodCapacity; }

    public void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }

}
