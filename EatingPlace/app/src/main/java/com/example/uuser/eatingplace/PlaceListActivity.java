package com.example.uuser.eatingplace;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import application.utility.Constants;
import application.utility.VolleySingleton;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
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

                if(placeObject.getPlaceAddress().length() > 1) {
                    address.setText(placeObject.getPlaceAddress());
                } else {
                    address.setText(placeObject.getPlaceVicinity());
                }


            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "lat = " + Constants.nearPlaces.get(position).getPlaceGeoLat() + " lng = " + Constants.nearPlaces.get(position).getPlaceGeoLng());
            }
        });
    }
}