package jay.antgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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

    // reused constants
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


    public World getNewWorld() {
        Nest nest = new Nest(new Position(0,0));

        List<Ant> ants = new ArrayList<>();
        for (int i = 0; i < 10; i++) ants.add(new Worker(new Position(i*10 - 40,0)));

        List<FoodSource> sources = new ArrayList<>();
        sources.add(new FoodSource(new Position(20,10), 50));
        sources.add(new FoodSource(new Position(-10,10), 50));
        sources.add(new FoodSource(new Position(-20,-10), 50));
        sources.add(new FoodSource(new Position(10,-10), 50));


        List<ScentTrail> trails = new ArrayList<>();

        return new World(ants, sources, trails, nest);
    }
}
