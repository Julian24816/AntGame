package jay.antgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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


    // constants for table FOODSOURCES
    private static final String TABLE_FOODSOURCES = "foodSources";
    private static final String FOODSOOURCE_MAX_FOOD_AMOUNT = "maxFoodAmount";
    private static final String CREATE_TABLE_FOODSOURCES
            = "create table " + TABLE_FOODSOURCES + " (" + POS_X + " real, " + POS_Y + " real, "
            + FOOD_AMOUNT + " integer, " + FOODSOOURCE_MAX_FOOD_AMOUNT + " integer, "
            + SAVE_ID + " integer)";

    // constants for table SCENTTRAILS
    private static final String TABLE_SCENTTRAILS = "scentTrails";
    private static final String SCENTTRAIL_REMAINING_LIFE_TIME = "liveTime";
    private static final String CREATE_TABLE_SCENTTRAILS
            = "create table " + TABLE_SCENTTRAILS + " (" + POS_X + " real, " + POS_Y + " real, "
            + SCENTTRAIL_REMAINING_LIFE_TIME + " integer, " + SAVE_ID + " integer)";



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


}
