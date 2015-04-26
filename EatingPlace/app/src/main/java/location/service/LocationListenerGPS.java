package location.service;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.uuser.eatingplace.MainActivity;

/**
 * Created by Uuser on 2015/4/24.
 */
public class LocationListenerGPS extends Activity implements LocationListener {
    MainActivity m_pOwnerMainActivity;

    public LocationListenerGPS(MainActivity mainActivity) {
        this.m_pOwnerMainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.m_pOwnerMainActivity.onLocationChangeCallBack(location.getLatitude(), location.getLongitude());

        Log.e("GPS Current Location",
                location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String arg0) {
        Log.e("GPS=======", "onProviderDisable");
    }

    @Override
    public void onProviderEnabled(String arg0) {
        Log.e("GPS=======", "onProviderEnable");
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        Log.e("GPS=======", "onStatusChanged");
    }
}