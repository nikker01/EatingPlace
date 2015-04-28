package eatingplace.database;

import android.provider.BaseColumns;

/**
 * Created by Uuser on 2015/4/28.
 */
public class PlaceDatabaseVO implements BaseColumns{

    public static final String TABLE_NAME = "EATING_PLACE";

    public static final String ICON_URL = "_ICON_URL";
    public static final String NAME = "_NAME";
    public static final String PLACE_ID = "_PLACE_ID";
    public static final String RATING = "_RATING";
    public static final String REF = "_REF";
    public static final String VICINITY = "_VICINITY";
    public static final String ADDRESS = "_ADDRESS";
    public static final String PHOTO_URL = "_PHOTO_URL";
    public static final String LAT = "_LAT";
    public static final String LNG = "_LNG";

    public static final String DBName = "/sdcard/eating_place_db.db";

    public static String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ICON_URL + " TEXT, " +
            NAME + " TEXT, " +
            PLACE_ID + " TEXT, " +
            RATING + " TEXT, " +
            REF + " TEXT, " +
            VICINITY + " TEXT, " +
            ADDRESS + " TEXT, " +
            PHOTO_URL + " TEXT, " +
            LAT + " TEXT, " +
            LNG + " TEXT)";

    public static String DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
