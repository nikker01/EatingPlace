package application.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Uuser on 2015/4/24.
 */
public class PreferenceUtil
{
    public static final String PrefrenceTag = "Preference";

    public static void cleanUserPreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Eating_Place", 0);
        settings.edit().clear().commit();
    }

    public static void SavePreferences(Activity activity, String key,
                                       String value) {
        SharedPreferences settings = activity.getSharedPreferences(
                PrefrenceTag, 0);
        settings.edit().putString(key, value).commit();
    }


    public static void setString(Activity activity, String key, String value) {
        SharedPreferences settings = activity.getSharedPreferences("Eating_Place", 0);
        settings.edit().putString(key, value).commit();
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences("Eating_Place", 0);
        settings.edit().putString(key, value).commit();
    }

    //added by Henry
    public static String getString(Context mContext, String key) {
        SharedPreferences settings = mContext.getSharedPreferences("Eating_Place", 0);
        return settings.getString(key, "");
    }

    public static String getString(Activity activity, String key) {
        SharedPreferences settings = activity.getSharedPreferences("Eating_Place", 0);
        return settings.getString(key, "");
    }

    public static void setBoolean(Activity activity, String key, boolean value) {
        SharedPreferences settings = activity.getSharedPreferences("Eating_Place", 0);
        settings.edit().putBoolean(key, value).commit();
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences("Eating_Place", 0);
        settings.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Activity activity, String key) {
        SharedPreferences settings = activity.getSharedPreferences("Eating_Place", 0);
        return settings.getBoolean(key, false);
    }

    ////added by Henry
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences("Eating_Place", 0);
        return settings.getBoolean(key, false);
    }
}
