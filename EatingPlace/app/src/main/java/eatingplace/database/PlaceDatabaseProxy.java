package eatingplace.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import eatingplace.objects.PlaceObject;

/**
 * Created by Uuser on 2015/4/28.
 */
public class PlaceDatabaseProxy {

    private static final String TAG = "PlaceDatabaseProxy";

    Context mContext;
    PlaceDatabaseHelper dbHelper;
    SQLiteDatabase db;
    String[] columns = {"_ICON_URL", "_NAME", "_PLACE_ID", "_RATING", "_REF", "_VICINITY", "_ADDRESS", "_PHOTO_URL", "_LAT", "_LNG"};

    public PlaceDatabaseProxy(Context context) {
        this.mContext = context;
        initDB();
    }

    private void initDB() {
        dbHelper = new PlaceDatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();
    }

    public void closeDB() {
        //dbHelper.close();
        //db.close();
    }

    public long insertData(PlaceObject object) {
        Log.i(TAG, "insertData BEGIN");
        long res = -1;
        boolean isExist = false;


        try {
            Cursor c = null;
            c = db.query(PlaceDatabaseVO.TABLE_NAME, columns, null, null, null, null, null);
            if (c.getCount() > 0) {
                Rawloop: for(int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String name = c.getString(c.getColumnIndex("_NAME"));
                    if(name.equals(object.getPlaceName())) {
                        isExist = true;
                        break Rawloop;
                    }
                }
            }
        } catch (Exception e) {
            isExist = false;
            e.printStackTrace();
        }

        if (isExist) {
            return -1;
        } else {
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put("_ICON_URL", object.getPlaceIconUrl());
                values.put("_NAME", object.getPlaceName());
                values.put("_PLACE_ID", object.getPlaceId());
                values.put("_RATING", object.getPlaceRating());
                values.put("_REF", object.getPlaceRef());
                values.put("_VICINITY", object.getPlaceVicinity());
                values.put("_ADDRESS", object.getPlaceAddress());
                values.put("_PHOTO_URL", object.getPlacePhotoUrl());
                values.put("_LAT", object.getPlaceGeoLat());
                values.put("_LNG", object.getPlaceGeoLng());

                res = db.insert(PlaceDatabaseVO.TABLE_NAME, null, values);

                db.setTransactionSuccessful();
            } catch (Exception e) {
                res = -1;
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }


        return res;
    }
}
