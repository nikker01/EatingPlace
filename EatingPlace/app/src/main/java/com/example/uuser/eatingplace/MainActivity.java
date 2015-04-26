package com.example.uuser.eatingplace;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import application.utility.Constants;
import application.utility.HttpMakecall;
import application.utility.PreferenceUtil;
import location.service.LocationListenerGPS;
import location.service.LocationListenerWIFI;
import location.service.LocationSingleton;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static String TAG = "MainActivity";

    public static LocationManager locationManager;
    public static LocationListenerGPS locationListenerGPS;
    public static LocationListenerWIFI locationListenerWIFI;

    private LatLngBounds mBounds;

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    double mBeginCoordinateX, mBeginCoordinateY, mEndCoordinateX, mEndCoordinateY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListenerGPS = new LocationListenerGPS(this);
        locationListenerWIFI = new LocationListenerWIFI(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        /*
        if(mGoogleApiClient != null) {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {

                        for(int i = 0; i < placeLikelihood.getPlace().getPlaceTypes().size(); i++) {
                            Log.i("PendingResult", "place type = "+placeLikelihood.getPlace().getPlaceTypes().get(i));
                        }

                        Log.i("PendingResult", String.format("Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                    likelyPlaces.release();
                }
            });
        }
        */

        FloatingActionButton searchNear = (FloatingActionButton) findViewById(R.id.searchNear);
        FloatingActionButton searchText = (FloatingActionButton) findViewById(R.id.searchText);

        searchNear.setOnClickListener(fabtnClick);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationSingleton.getInstance().setLatLng(this);

        locationManager.requestLocationUpdates("gps", 300 * 1000, 0,
                locationListenerGPS);
        locationManager.requestLocationUpdates("network", 30 * 1000, 0,
                locationListenerWIFI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListenerGPS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to
                // connect
                if (!mGoogleApiClient.isConnecting()
                        && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, 1001);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            Log.e("onConnectionFailed", "error" + result.getErrorCode());
            mResolvingError = true;
        }
    }

    private View.OnClickListener fabtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchNear: {
                    requestByMethodName(Constants.callMethods.getNearPlace);
                    break;
                }
                case R.id.searchText: {
                    break;
                }
            }
        }
    };

    private void requestByMethodName(final Constants.callMethods method) {
        new Thread() {
            @Override
            public void run() {
                if (method.equals(Constants.callMethods.getNearPlace)) {
                    String url = "";
                    try {
                        url = "https://maps.googleapis.com/maps/api/place/search/json?location=" + Constants.userLatitude + "," + Constants.userLongitude +
                                "&radius=100&types=" + URLEncoder.encode("food|bakery|cafe|restaurant|meal_delivery|meal_takeaway", "UTF-8") + "&key=" +
                                Constants.WEB_SERVICE_KEY;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String res = HttpMakecall.makeCall(url);
                    Log.i(TAG, "getNearPlace res = " + res);
                }
            }
        }.start();

    }


    // Location callback by locationListenerWIFI
    public void onLocationChangeCallBack(double latitude, double longitude) {

        Constants.userLatitude = latitude;
        Constants.userLongitude = longitude;
    }
}
