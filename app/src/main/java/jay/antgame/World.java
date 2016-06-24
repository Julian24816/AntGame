package jay.antgame;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Julian on 24.06.2016.
 */
public class World {

    private List<Ant> ants;
    private List<FoodSource> foodSources;
    private List<ScentTrail> scentTrails;
    private Nest nest;

    public World(List<Ant> ants, List<FoodSource> sources, List<ScentTrail> trails, Nest nest) {
        this.ants = ants;
        foodSources = sources;
        scentTrails = trails;
        this.nest = nest;
    }


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

}
