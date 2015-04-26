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
public class LocationListenerWIFI extends Activity implements LocationListener {

    MainActivity m_pOwnerMainActivity;
    public LocationListenerWIFI(MainActivity mainActivity) {
        this.m_pOwnerMainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
        //Log.e("WIFI Current Location", location.getLatitude() + " " + location.getLongitude());
        this.m_pOwnerMainActivity.onLocationChangeCallBack(location.getLatitude(), location.getLongitude());

        //PreferenceProxy proxy = new PreferenceProxy(this);
        //PreferenceUtil.setString(this, Constant.USER_LNG, Double.toString(location.getLongitude()) +"");
        //PreferenceUtil.setString(this, Constant.USER_LAT, location.getLatitude() +"");
        //Log.e("WIFI Current Location", location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String arg0) {
        Log.e("WIFI=======", "onProviderDisable");
    }

    @Override
    public void onProviderEnabled(String arg0) {
        Log.e("WIFI=======", "onProviderEnable");
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        Log.e("WIFI=======", "onStatusChanged");
    }
}


