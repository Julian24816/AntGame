package jay.antgame;

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



    public GameStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
                    new Position(res.getDouble(0), res.getDouble(1)), // pos
                    new Position(res.getDouble(2), res.getDouble(3)), // targetPos
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
            FoodSource fs = new FoodSource(new Position(res.getDouble(0), res.getDouble(1)),
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
            Worker ant = new Worker(new Position(res.getDouble(0), res.getDouble(1)));
            ant.setPosition(new Position(res.getDouble(2), res.getDouble(3)));
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
            nest = new Nest(new Position(res.getDouble(0), res.getDouble(1)));
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

        List<FoodSource> sources = new LinkedList<>();
        sources.add(new FoodSource(new Position(20,10), 50));
        sources.add(new FoodSource(new Position(-10,10), 50));
        sources.add(new FoodSource(new Position(-20,-10), 50));
        sources.add(new FoodSource(new Position(10,-10), 50));


        List<ScentTrail> trails = new LinkedList<>();

        return new World(ants, sources, trails, nest);
    }
}
