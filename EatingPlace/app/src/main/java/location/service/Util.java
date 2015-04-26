package location.service;

/**
 * Created by Uuser on 2015/4/24.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static ProgressDialog progressDialog;
    private static AlertDialog.Builder OKAlertDialog;



    public static void showProgressDialog(Activity activity, String title,
                                          String message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showOKAlertDialog(Activity activity, String title,
                                         String message) {
        OKAlertDialog = new AlertDialog.Builder(activity);
        OKAlertDialog.setTitle(title);
        OKAlertDialog.setMessage(message);

        DialogInterface.OnClickListener OKClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        OKAlertDialog.setNeutralButton("OK", OKClick);

        OKAlertDialog.show();
    }

    public static String getCityFromLocation(Activity activity, double lat,
                                             double lng) {
        String city = "";
        String county = "";
        String state = "";
        String cityName = "";

        HttpGet httpGet = new HttpGet(
                "http://maps.google.com/maps/api/geocode/json?latlng=" + lat
                        + "," + lng + "&sensor=true&language=zh-TW");
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;
        String httpResult = "";

        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            httpResult = EntityUtils.toString(entity, HTTP.UTF_8);

            JSONObject addressJSON = new JSONObject(httpResult);

            String Status = addressJSON.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray addressResults = addressJSON.getJSONArray("results");
                JSONObject addressResult = addressResults.getJSONObject(0);
                JSONArray address_components = addressResult
                        .getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject addressInfo = address_components
                            .getJSONObject(i);
                    String long_name = addressInfo.getString("long_name");
                    JSONArray types = addressInfo.getJSONArray("types");
                    String typeValue = types.getString(0);
                    if (typeValue.equalsIgnoreCase("locality")) {
                        city = long_name;
                    } else if (typeValue
                            .equalsIgnoreCase("administrative_area_level_2")) {
                        county = long_name;
                    } else if (typeValue
                            .equalsIgnoreCase("administrative_area_level_1")) {
                        state = long_name;
                    }
                }

                if (city.length() > 0) {
                    cityName = city;
                    Log.e("===city===", city);
                } else if (county.length() > 0) {
                    cityName = county;
                    Log.e("===county===", county);
                } else if (state.length() > 0) {
                    cityName = state;
                    Log.e("===state===", state);
                } else
                    Log.e("=======", "There is no city");
            }
        } catch (Exception e) {
        }

        return cityName;
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static String getEditText(EditText editText) {
        String str = editText.getText().toString().trim();

        if (str.length() == 0)
            return "";
        else
            return str;
    }

    public static byte[] convertIStoByte(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()).format(new Date(System
                .currentTimeMillis()));
    }

    public static String getTimeYYYYMMDD() {
        return new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
                .format(new Date(System.currentTimeMillis()));
    }

    public static int computeDateInterval(String time1, String time2) {
        SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault());
        try {

            Date start = smdf.parse(time1);
            Date end = smdf.parse(time2);
            long t = (end.getTime() - start.getTime()) / 1000;
            return (int) t / (3600 * 24);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}