package location.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.uuser.eatingplace.R;

import java.util.Date;
import java.util.List;

public class LocationSingleton extends Activity implements Runnable {

    private LocationManager locationManager;
    private Location locationCurrent;
    private Location locationGPS;
    private Location locationWIFI;

    private static final LocationSingleton locationSingleton = new LocationSingleton();

    // Private constructor prevents instantiation from other classes
    private LocationSingleton() {}

    public static LocationSingleton getInstance() {
        return locationSingleton;
    }

    public void setLatLng(Activity activity) {
		/*
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
		String providersCriteria = locationManager.getBestProvider(criteria, true);
		*/

        locationManager = (LocationManager) (activity.getSystemService(Context.LOCATION_SERVICE));
        List<String> providers = locationManager.getProviders(true);
        if(providers.contains("gps") || providers.contains("network"))
        {
            Log.e("=======", "yes there is a gps or network");

            if(providers.contains("gps"))
            {
                locationGPS = locationManager.getLastKnownLocation("gps");
                Log.e("=======", "GPS");
            }
            if(providers.contains("network"))
            {
                locationWIFI = locationManager.getLastKnownLocation("network");
                Log.e("=======", "Network");
            }

            if(locationGPS == null && locationWIFI != null)
            {
                locationCurrent = locationWIFI;
                Log.e("Location 1: ", locationCurrent.getLongitude() + " " + locationCurrent.getLatitude() );
                new Thread(this).start();
            }
            else if(locationGPS != null && locationWIFI == null)
            {
                locationCurrent = locationGPS;
                Log.e("Location 2: ", locationCurrent.getLongitude() + " " + locationCurrent.getLatitude() );
                new Thread(this).start();
            }
            else if(locationGPS != null && locationWIFI != null)
            {
                Date gpsDate = new Date(locationGPS.getTime());
                Date wifiDate = new Date(locationWIFI.getTime());

                if(wifiDate.after(gpsDate))
                {
                    locationCurrent = locationWIFI;
                    Log.e("Location 3 WIFI: ", locationCurrent.getLongitude() + " " + locationCurrent.getLatitude() + " " + new Date(locationCurrent.getTime()));
                }
                else
                {
                    locationCurrent = locationGPS;
                    Log.e("Location 3 GPS: ", locationCurrent.getLongitude() + " " + locationCurrent.getLatitude() + " " + new Date(locationCurrent.getTime()));
                }


                new Thread(this).start();
            }
            else
            {
                Log.e("=======", "location is null");
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("gps_error");
            builder.setMessage("gps_error");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    @Override
    public void run() {
        Util.getCityFromLocation(this, locationCurrent.getLatitude(), locationCurrent.getLongitude());
        //String city  = Util.getCityFromLocation(this, locationCurrent.getLatitude(), locationCurrent.getLongitude());
        //Log.e("===city name:===", city);
    }
}

