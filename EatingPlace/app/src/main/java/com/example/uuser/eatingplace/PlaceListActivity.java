package com.example.uuser.eatingplace;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import application.utility.Constants;
import application.utility.IGenericDialogUtil;
import application.utility.Util;
import application.utility.VolleySingleton;
import eatingplace.database.PlaceDatabaseProxy;
import eatingplace.objects.PlaceObject;
import eatingplace.views.CommonAdapter;
import eatingplace.views.CommonViewHolder;

/**
 * Created by Uuser on 2015/4/27.
 */
public class PlaceListActivity extends ActionBarActivity {

    private static final String TAG = "PlaceListActivity";
    ListView listView;
    CommonAdapter<PlaceObject> adapter;
    ImageLoader mImageLoader;
    RequestQueue queue;
    PlaceDatabaseProxy prxoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        listView = (ListView)findViewById(R.id.listView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = VolleySingleton.getInstance().getRequestQueue();
        mImageLoader = VolleySingleton.getInstance().getImageLoader();



        initViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        prxoy = new PlaceDatabaseProxy(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        prxoy.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_add_favorite:
                goToMap();
                return true;

            default:
                return true;
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initViews() {

        adapter = new CommonAdapter(this,
                Constants.nearPlaces, R.layout.cell_place) {

            @Override
            public void setViewData(CommonViewHolder commonViewHolder, View currentView, Object item) {
                final PlaceObject placeObject = (PlaceObject)item;

                TextView name = (TextView) commonViewHolder.get(commonViewHolder, currentView, R.id.txt_place_name);
                TextView address = (TextView) commonViewHolder.get(commonViewHolder, currentView, R.id.txt_place_address);
                NetworkImageView placeView = (NetworkImageView) commonViewHolder.get(commonViewHolder, currentView, R.id.img_place_view);
                NetworkImageView icon = (NetworkImageView) commonViewHolder.get(commonViewHolder, currentView, R.id.img_icon_view);
                RatingBar rating = (RatingBar) commonViewHolder.get(commonViewHolder, currentView, R.id.ratingBar);

                name.setText(placeObject.getPlaceName());
                icon.setImageUrl(placeObject.getPlaceIconUrl(), mImageLoader);
                placeView.setImageUrl(placeObject.getPlacePhotoUrl(), mImageLoader);
                placeView.setDefaultImageResId(R.mipmap.nohead);
                float ratingValue = Float.parseFloat(placeObject.getPlaceRating());
                rating.setRating(ratingValue);

                /*
                if(placeObject.getPlaceAddress().length() > 1) {
                    address.setText(placeObject.getPlaceAddress());
                } else {
                    address.setText(placeObject.getPlaceVicinity());
                }
                */
                address.setText(placeObject.getPlaceVicinity());


            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(TAG, "lat = " + Constants.nearPlaces.get(position).getPlaceGeoLat() + " lng = " + Constants.nearPlaces.get(position).getPlaceGeoLng());
                showDialog(Constants.nearPlaces.get(position));
            }
        });
    }

    private void showDialog(final PlaceObject obj) {
        String title = obj.getPlaceName();
        String msg = getResources().getString(R.string.dialog_msg_add_favorite);
        String positiveBtn = getResources().getString(R.string.btn_ok);
        String negativeBtn = getResources().getString(R.string.btn_cancel);

        Util.pushGeneralDialog(this, title, msg, positiveBtn, negativeBtn, new IGenericDialogUtil.IMaterialBtnClick() {
            @Override
            public void PositiveMethod(View v) {
                long res = prxoy.insertData(obj);
                if(res != -1)
                    Toast.makeText(PlaceListActivity.this, getResources().getString(R.string.toast_msg_add_favorite_succes), Toast.LENGTH_LONG).show();
            }

            @Override
            public void NegativeMethod(View v) {

            }
        });
    }

    private void goToMap() {

        Uri gmmIntentUri = Uri.parse("geo:"+Constants.userLatitude+","+Constants.userLongitude+"?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
