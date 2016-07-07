package jay.antgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jay.antgame.data.Ant;
import jay.antgame.data.Builder;
import jay.antgame.data.FoodSource;
import jay.antgame.data.Nest;
import jay.antgame.data.Position;
import jay.antgame.data.ScentTrail;
import jay.antgame.data.Worker;
import jay.antgame.data.World;

/**
 * Created by Julian on 29.06.2016.
 *
 * @author Julian
 */
public class GameStorage extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "antgame";
    private static final int DATABASE_VERSION = 4;

    private ArrayList<Integer> availableSaveIds;

    // reused constants for database tables
    private static final String POS_X = "posX";
    private static final String POS_Y = "posY";
    private static final String FOOD_AMOUNT = "carriedFoodAmount";
    private static final String SAVE_ID = "saveId";
    private static final String TARGET_POS_X = "targetPosX";
    private static final String TARGET_POS_Y = "targetPosY";

    // constants for table ANTS
    private static final String TABLE_ANTS = "ants";
    private static final String ANT_TYPE = "type";
    private static final String ANT_TYPE_WORKER = "worker";
    private static final String ANT_TYPE_BUILDER = "builder";
    private static final String ANT_HAS_TARGET = "hasTarget";
    private static final int ANT_HAS_TARGET_TRUE = 1;
    private static final int ANT_HAS_TARGET_FALSE = 0;
    private static final String CREATE_TABLE_ANTS
            = "create table " + TABLE_ANTS + " (" + POS_X + " real, " + POS_Y + " real, "
            + TARGET_POS_X + " real, " + TARGET_POS_Y + " real, " + ANT_TYPE + " text, "
            + FOOD_AMOUNT + " integer, " + ANT_HAS_TARGET + " integer, " + SAVE_ID + " integer)";


    // constants for table NEST
    private static final String TABLE_NEST = "nests";
    private static final String CREATE_TABLE_NEST
            = "create table " + TABLE_NEST + " (" + POS_X + " real, " + POS_Y + " real, "
            + FOOD_AMOUNT + " integer, " + SAVE_ID + " integer)";


    // constants for table FOOD_SOURCES
    private static final String TABLE_FOOD_SOURCES = "foodSources";
    private static final String FOOD_SOURCE_MAX_FOOD_AMOUNT = "maxFoodAmount";
    private static final String CREATE_TABLE_FOOD_SOURCES
            = "create table " + TABLE_FOOD_SOURCES + " (" + POS_X + " real, " + POS_Y + " real, "
            + FOOD_AMOUNT + " integer, " + FOOD_SOURCE_MAX_FOOD_AMOUNT + " integer, "
            + SAVE_ID + " integer)";

    // constants for table SCENT_TRAILS
    private static final String TABLE_SCENT_TRAILS = "scentTrails";
    private static final String SCENT_TRAIL_REMAINING_LIFE_TIME = "liveTime";
    private static final String CREATE_TABLE_SCENTTRAILS
            = "create table " + TABLE_SCENT_TRAILS + " (" + POS_X + " real, " + POS_Y + " real, "
            + TARGET_POS_X + " real, " + TARGET_POS_Y + " real, "
            + SCENT_TRAIL_REMAINING_LIFE_TIME + " integer, " + SAVE_ID + " integer)";


    /**
     * calls the SuperConstructor and retrieves all available worldIds
     *
     * @param context the GameStorage's context
     */
    public GameStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        availableSaveIds = getAvailableSaveIds();
    }


    /**
     * creates the database tables.
     *
     * Overrides method of SQLiteOpenHelper;
     * this method gets called when the SQLiteOpenHelper constructor created the database aka on
     * the first run of the App
     *
     * @param db the created database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ANTS);
            db.execSQL(CREATE_TABLE_FOOD_SOURCES);
            db.execSQL(CREATE_TABLE_NEST);
            db.execSQL(CREATE_TABLE_SCENTTRAILS);
        } catch (Exception e) {
            Log.e("antgame", "GameStorage.onCreate sql threw exception",e);
        }
    }


    /**
     * alters the database to fit the new version
     *
     * Overrides method of SQLiteOpenHelper;
     * this method gets called upon the SQLiteOpenHelper constructor realizing that the database
     * version isn't equal to its parameter
     *
     * @param db the database
     * @param oldVersion the databases current version
     * @param newVersion the requested new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("reinstall App to fix database bug");
    }


    /**
     * retrieves the saveIds of all available Worlds
     *
     * @return Array of saveIds
     */
    public ArrayList<Integer> getAvailableSaveIds() {
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + SAVE_ID + " from " + TABLE_NEST,null);
        while (res.moveToNext()) {
            list.add(res.getInt(0));
        }
        res.close();
        db.close();
        return list;
    }


    /**
     * saves the World to the database by first calculating the next saveId and then calling the
     * itself with the additional parameter
     *
     * @param world the World, that will be saved
     */
    public void saveWorld(World world) {
        Log.d("antgame", "saving world...");
        // get next saveId
        int saveId = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select max("+ SAVE_ID + ") from " + TABLE_NEST, null);
        if (res.moveToNext())
            saveId = res.getInt(0) + 1;
//        if (saveId == 0) throw new RuntimeException("no previous saved world present");
        res.close();
        db.close();
        saveWorld(world, saveId);
        Log.d("antgame", "saving world... done");
    }


    /**
     * saves the World to the database
     *
     * @param world the World object to be saved
     * @param saveId the saveId to be used to identify all World-Components
     */
    public void saveWorld(World world, int saveId) {
        for (Ant ant: world.getAnts()) saveAnt(ant, saveId);
        for (FoodSource foodSource: world.getFoodSources()) saveFoodSource(foodSource, saveId);
        for (ScentTrail scentTrail: world.getScentTrails()) saveScentTrail(scentTrail, saveId);
        saveNest(world.getNest(), saveId);
    }


    /**
     * saves a Nest to the Database
     *
     * @param nest the nest object to be saved
     * @param saveId the saveId to be used
     */
    private void saveNest(Nest nest, int saveId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POS_X, nest.getPosition().getX());
        values.put(POS_Y, nest.getPosition().getY());
        values.put(FOOD_AMOUNT, nest.getFood());
        values.put(SAVE_ID, saveId);

        db.insert(TABLE_NEST, null, values);
        db.close();
    }



    /**
     * saves an Ant to the Database
     *
     * @param ant the ant object to be saved
     * @param saveId the saveId to be used
     */
    private void saveAnt(Ant ant, int saveId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POS_X, ant.getPosition().getX());
        values.put(POS_Y, ant.getPosition().getY());
        values.put(SAVE_ID, saveId);
        if (ant.hasTarget()) {
            values.put(ANT_HAS_TARGET, ANT_HAS_TARGET_TRUE);
            values.put(TARGET_POS_X, ant.getTargetPosition().getX());
            values.put(TARGET_POS_Y, ant.getTargetPosition().getY());
        } else {
            values.put(ANT_HAS_TARGET, ANT_HAS_TARGET_FALSE);
        }

        if (ant instanceof Worker) {
            values.put(FOOD_AMOUNT, ((Worker)ant).getFood());
            values.put(ANT_TYPE, ANT_TYPE_WORKER);
        } else if (ant instanceof Builder) {
            values.put(FOOD_AMOUNT, 0);
            values.put(ANT_TYPE, ANT_TYPE_BUILDER);
        }

        db.insert(TABLE_ANTS, null, values);
        db.close();
    }


    /**
     * saves a FoodSource to the Database
     *
     * @param foodSource the foodSource object to be saved
     * @param saveId the saveId to be used
     */
    private void saveFoodSource(FoodSource foodSource, int saveId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POS_X, foodSource.getPosition().getX());
        values.put(POS_Y, foodSource.getPosition().getY());
        values.put(FOOD_AMOUNT, foodSource.getFood());
        values.put(FOOD_SOURCE_MAX_FOOD_AMOUNT, foodSource.getMaxFood());
        values.put(SAVE_ID, saveId);

        db.insert(TABLE_FOOD_SOURCES, null, values);
        db.close();
    }


    /**
     * saves a ScentTrail to the Database
     *
     * @param scentTrail the scentTrail object to be saved
     * @param saveId the saveId to be used
     */
    private void saveScentTrail(ScentTrail scentTrail, int saveId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POS_X, scentTrail.getPosition().getX());
        values.put(POS_Y, scentTrail.getPosition().getY());
        values.put(TARGET_POS_X, scentTrail.getTarget().getX());
        values.put(TARGET_POS_Y, scentTrail.getTarget().getY());
        values.put(SCENT_TRAIL_REMAINING_LIFE_TIME, scentTrail.getRemainingLifetime());
        values.put(SAVE_ID, saveId);

        db.insert(TABLE_SCENT_TRAILS, null, values);
        db.close();
    }



    /**
     * loads a World from the Database
     *
     * @param saveId the saveId all world components are identified by
     * @return a new World object corresponding to the saveId
     */
    public World getWorld(int saveId) {
        Nest nest = getNest(saveId);
        List<Ant> ants = getAnts(saveId);
        List<FoodSource> sources = getFoodSources(saveId);
        List<ScentTrail> trails = getScentTrails(saveId);
        return new World(ants, sources, trails, nest);
    }


    /**
     * loads the scentTrails of a World
     *
     * @param saveId the saveId the scentTrails are identified by
     * @return a list of all the scentTrails
     */
    private List<ScentTrail> getScentTrails(int saveId) {
        List<ScentTrail> scentTrails = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + POS_X + ", " + POS_Y + ", " + TARGET_POS_X + ", "
                + TARGET_POS_Y + ", " + SCENT_TRAIL_REMAINING_LIFE_TIME + " from " + TABLE_SCENT_TRAILS
                + " where " + SAVE_ID + " = ?",new String[]{String.valueOf(saveId)});
        while (res.moveToNext()) {
            scentTrails.add(new ScentTrail(
                    new Position(res.getFloat(0), res.getFloat(1)), // pos
                    new Position(res.getFloat(2), res.getFloat(3)), // targetPos
                    res.getInt(4)));
        }
        res.close();
        db.close();
        return scentTrails;
    }


    /**
     * loads the foodSources of a World
     *
     * @param saveId the saveId the foodSources are identified by
     * @return a list of all the foodSources
     */
    private List<FoodSource> getFoodSources(int saveId) {
        List<FoodSource> foodSources = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + POS_X + ", " + POS_Y + ", " + FOOD_AMOUNT + ", "
                + FOOD_SOURCE_MAX_FOOD_AMOUNT + " from " + TABLE_FOOD_SOURCES
                + " where " + SAVE_ID + " = ?",new String[]{String.valueOf(saveId)});
        while (res.moveToNext()) {
            FoodSource fs = new FoodSource(new Position(res.getFloat(0), res.getFloat(1)),
                    res.getInt(3));
            fs.lowerFood(res.getInt(3) - res.getInt(2));
            foodSources.add(fs);
        }
        res.close();
        db.close();
        return foodSources;
    }


    /**
     * loads the ants of a World
     *
     * @param saveId the saveId the ants are identified by
     * @return a list of all the ants
     */
    private List<Ant> getAnts(int saveId) {
        List<Ant> ants = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + POS_X + ", " + POS_Y + ", " + TARGET_POS_X + ", "
                + TARGET_POS_Y + ", " + FOOD_AMOUNT + ", " + ANT_TYPE + ", " + ANT_HAS_TARGET
                + " from " + TABLE_ANTS
                + " where " + SAVE_ID + " = ?",new String[]{String.valueOf(saveId)});
        while (res.moveToNext()) {
            Ant ant;

            if (res.getString(5).equals(ANT_TYPE_WORKER)) {
                ant = new Worker(new Position(res.getFloat(0), res.getFloat(1)));
                ((Worker)ant).setFood(res.getInt(4));

            } else if (res.getString(5).equals(ANT_TYPE_BUILDER)) {
                ant = new Builder(new Position(res.getFloat(0), res.getFloat(1)));

            } else throw new RuntimeException("unknown ant type when loading world "+saveId);

            if (res.getInt(6) == ANT_HAS_TARGET_TRUE)
                ant.setTargetPosition(new Position(res.getFloat(2), res.getFloat(3)));

            ants.add(ant);
        }
        res.close();
        db.close();
        return ants;
    }


    /**
     * loads the Nest of a World
     *
     * @param saveId the saveId the nest is identified by
     * @return a Nest Object
     */
    private Nest getNest(int saveId) {
        Nest nest = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + POS_X + ", " + POS_Y + ", " + FOOD_AMOUNT + " from "
                + TABLE_NEST + " where " + SAVE_ID + " = ?",new String[]{String.valueOf(saveId)});
        if (res.moveToNext()) {
            nest = new Nest(new Position(res.getFloat(0), res.getFloat(1)));
            nest.addFoodAmount(res.getInt(2));
        }
        res.close();
        db.close();
        return nest;
    }


    /**
     * creates a default World
     *
     * @return the new World Object
     */
    public World getNewWorld() {
        return getNewWorld(20, 5, 50);
    }


    /**
     * creates a new World with specified object counts
     *
     * @param ants the number of ants in the World
     * @param foodSources the number of foodSources in the World
     * @param nestFood the amount of to start with
     * @return the new World object
     */
    private World getNewWorld(int ants, int foodSources, int nestFood) {

        //TODO use parameters to determine the worlds attributes

        Nest nest = new Nest(new Position(0,0));
        nest.addFoodAmount(nestFood);

        List<Ant> antList = new LinkedList<>();
        for (int i = 0; i < 10; i++)
            antList.add(new Worker(nest.getPosition().clone()));
        antList.add(new Worker(new Position(100,-100)));

        List<FoodSource> sources = new LinkedList<>();
        sources.add(new FoodSource(new Position(200,100), 50));
        sources.add(new FoodSource(new Position(-600,300), 50));
        sources.add(new FoodSource(new Position(-200,-100), 50));
        sources.add(new FoodSource(new Position(100,-100), 50));

        return new World(antList, sources , nest);
    }


    /**
     *
     * @return true if database contains saved Worlds
     */
    public boolean savedWorldsPresent() {
        return availableSaveIds.size() > 0;
    }


    /**
     * deletes all saved Worlds
     */
    public void deleteAllWorlds() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NEST, null, null);
        db.delete(TABLE_ANTS, null, null);
        db.delete(TABLE_SCENT_TRAILS, null, null);
        db.delete(TABLE_FOOD_SOURCES, null, null);
        db.close();
        availableSaveIds = new ArrayList<>();
    }


    /**
     * retrieves the latest World
     *
     * @return the World with the highest saveId or null
     */
    public World getLatestWorld() {
        if (availableSaveIds.size() > 0)
            return getWorld(availableSaveIds.get(availableSaveIds.size()-1));
        else
            return null;
    }
}