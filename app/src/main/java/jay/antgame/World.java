package jay.antgame;

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

    public int getFood(){ return food; }

    public void addFood(int f){}

}
