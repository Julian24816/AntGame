package jay.antgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jay.antgame.data.Ant;
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
    private static final int DATABASE_VERSION = 1;

    // reused constants for database tables
    private static final String POS_X = "posX";
    private static final String POS_Y = "posY";
    private static final String FOOD_AMOUNT = "carriedFoodAmount";
    private static final String SAVE_ID = "saveId";
    private static final String TARGET_POS_X = "targetPosX";
    private static final String TARGET_POS_Y = "targetPosY";

    // constants for table ANTS
    private static final String TABLE_ANTS = "ants";
    private static final String CREATE_TABLE_ANTS
            = "create table " + TABLE_ANTS + " (" + POS_X + " real, " + POS_Y + " real, "
            + TARGET_POS_X + " real, " + TARGET_POS_Y + " real, "
            + FOOD_AMOUNT + " integer, " + SAVE_ID + " integer)";


    // constants for table NEST
    private static final String TABLE_NEST = "nests";
    private static final String CREATE_TABLE_NEST
            = "create table " + TABLE_NEST + " (" + POS_X + " real, " + POS_Y + " real, "
            + FOOD_AMOUNT + " integer, " + SAVE_ID + " integer)";


    // constants for table FOOD_SOURCES
    private static final String TABLE_FOOD_SOURCES = "foodSources";
    private static final String FOOD_SOURCE_MAX_FOOD_AMOUNT = "maxFoodAmount";
    private static final String CREATE_TABLE_FOODSOURCES
            = "create table " + TABLE_FOOD_SOURCES + " (" + POS_X + " real, " + POS_Y + " real, "
            + FOOD_AMOUNT + " integer, " + FOOD_SOURCE_MAX_FOOD_AMOUNT + " integer, "
            + SAVE_ID + " integer)";

    // constants for table SCENT_TRAILS
    private static final String TABLE_SCENT_TRAILS = "scentTrails";
    private static final String SCENT_TRAIL_REMAINING_LIFE_TIME = "liveTime";
    private static final String CREATE_TABLE_SCENTTRAILS
            = "create table " + TABLE_SCENT_TRAILS + " (" + POS_X + " real, " + POS_Y + " real, "
            + SCENT_TRAIL_REMAINING_LIFE_TIME + " integer, " + SAVE_ID + " integer)";


    /**
     * calls the SuperConstructor
     *
     * @param context the GameStorage's context
     */
    public GameStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
            db.execSQL(CREATE_TABLE_FOODSOURCES);
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

    }

    public Integer[] getAvailableWorlds() {
        List<Integer> list = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + SAVE_ID + " from " + TABLE_NEST,null);
        while (res.moveToNext()) {
            list.add(res.getInt(0));
        }
        res.close();
        db.close();
        return (Integer[]) list.toArray();
    }

    /*    public void saveWorld(World world) {
            // get next saveId
            int saveId = 0;
            SQLiteDatabase db = getReadableDatabase();
            Cursor res = db.rawQuery("select max("+ SAVE_ID + ") from " + TABLE_NEST, null);
            if (res.moveToNext()) {
                saveId = res.getInt(0);
            }
            if (saveId == 0) throw new RuntimeException("no saved world present");
            saveWorld(world, saveId);
        }

        public void saveWorld(World world, int saveId) {
            for (Ant ant: world.getAnts()) saveAnt(ant, saveId);
            for (FoodSource foodSource: world.getFoodSources()) saveFoodSource(foodSource, saveId);
            for (ScentTrail scentTrail: world.getScentTrails()) saveScentTrail(scentTrail, saveId);
            saveNest(world.getNest(), saveId);
        }
    */

    private void saveAnt(Ant ant, int saveId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POS_X, ant.getPosition().getX());
        values.put(POS_Y, ant.getPosition().getY());
        values.put(FOOD_AMOUNT, ((Worker)ant).getFood()); //todo different ant types
        values.put(SAVE_ID, saveId);
        if (ant.hasTarget()) {
            values.put(TARGET_POS_X, ant.getTargetPosition().getX());
            values.put(TARGET_POS_Y, ant.getTargetPosition().getY());
        }

        db.insert(TABLE_ANTS, null, values);
        db.close();
    }

    public World getWorld(int saveId) {
        //TODO assert that a save with saveId exists
        Nest nest = getNest(saveId);
        List<Ant> ants = getAnts(saveId);
        List<FoodSource> sources = getFoodSources(saveId);
        List<ScentTrail> trails = getScentTrails(saveId);
        return new World(ants, sources, trails, nest);
    }

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

    private List<Ant> getAnts(int saveId) {
        List<Ant> ants = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + POS_X + ", " + POS_Y + ", " + TARGET_POS_X + ", "
                + TARGET_POS_Y + ", " + FOOD_AMOUNT + " from " + TABLE_ANTS
                + " where " + SAVE_ID + " = ?",new String[]{String.valueOf(saveId)});
        while (res.moveToNext()) {
            Worker ant = new Worker(new Position(res.getFloat(0), res.getFloat(1)));
            ant.setPosition(new Position(res.getFloat(2), res.getFloat(3)));
            ant.setFood(res.getInt(4));
            ants.add(ant);
        }
        res.close();
        db.close();
        return ants;
    }

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


    public World getNewWorld() {

        Nest nest = new Nest(new Position(0,0));

        List<Ant> ants = new LinkedList<>();
        for (int i = 0; i < 10; i++) ants.add(new Worker(new Position(i*10 - 40,0)));
        ants.add(new Worker(new Position(100,-100)));

        List<FoodSource> sources = new LinkedList<>();
        sources.add(new FoodSource(new Position(200,100), 50));
        sources.add(new FoodSource(new Position(-600,300), 50));
        sources.add(new FoodSource(new Position(-200,-100), 50));
        sources.add(new FoodSource(new Position(100,-100), 50));

        nest.addFoodAmount(20);

        List<ScentTrail> trails = new LinkedList<>();

        return new World(ants, sources, trails, nest);
    }
}
