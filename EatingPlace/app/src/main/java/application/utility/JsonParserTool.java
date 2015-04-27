package application.utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eatingplace.objects.PlaceObject;

/**
 * Created by Henry on 2015/4/26.
 */
public class JsonParserTool {

    public static void parseNearPlace(String resultString) throws JSONException {

        Constants.nearPlaces.clear();

        JSONObject obj = new JSONObject(resultString);
        JSONArray jsonArray = new JSONArray(obj.getString("results"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PlaceObject placeObj = new PlaceObject();

            JSONObject locObject = jsonObject.getJSONObject("geometry").getJSONObject("location");
            placeObj.setPlaceGeoLat(locObject.getString("lat"));
            placeObj.setPlaceGeoLng(locObject.getString("lng"));

            placeObj.setPlaceAddress("");
            if(jsonObject.has("icon"))
                placeObj.setPlaceIconUrl(jsonObject.getString("icon"));
            if(jsonObject.has("place_id"))
                placeObj.setPlaceId(jsonObject.getString("place_id"));
            if(jsonObject.has("name"))
                placeObj.setPlaceName(jsonObject.getString("name"));
            if(jsonObject.has("rating"))
                placeObj.setPlaceRating(jsonObject.getString("rating"));
            if(jsonObject.has("reference"))
                placeObj.setPlaceRef(jsonObject.getString("reference"));
            if(jsonObject.has("vicinity"))
                placeObj.setPlaceVicinity(jsonObject.getString("vicinity"));

            if(jsonObject.has("photos")) {
                JSONArray photoArray = new JSONArray(jsonObject.getString("photos"));

                JSONObject photo = photoArray.getJSONObject(0);
                String url = "https://maps.googleapis.com/maps/api/place/photo?";
                placeObj.setPlacePhotoUrl(url + "maxwidth="+photo.getString("width") + "&photoreference=" + photo.getString("photo_reference") +
                    "&" + "key=" + Constants.WEB_SERVICE_KEY);

            }

            Constants.nearPlaces.add(placeObj);
        }
    }
}
