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

    private int width = 2000;
    private int height = 1000;

    private WorldObject selectedObject = null;

    public World(List<Ant> ants, List<FoodSource> sources, List<ScentTrail> trails, Nest nest) {
        this.ants = ants;
        foodSources = sources;
        scentTrails = trails;
        this.nest = nest;
        objects.addAll(sources);
        objects.add(nest);
        objects.addAll(ants);
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
        return nest.getFood();
    }

    public void addFood(int f) {
        nest.addFoodAmount(f);
    }

    public void addAnt(Ant ant){ ants.add(ant); }

    public int getWidth(){ return width; }

    public int getHeight(){ return  height; }

    public List<WorldObject> getWorldObjects(){ return objects; }

    public WorldObject getSelectedObject(){ return selectedObject; }

    public void setSelectedObject(WorldObject object){ selectedObject = object; }

    public void addWorker(Position p){ ants.add(new Worker(p)); }

    public void addWorker(){ addWorker(nest.getPosition().clone()); }

    public void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }

}
