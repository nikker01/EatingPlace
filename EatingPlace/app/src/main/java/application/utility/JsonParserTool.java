package application.utility;

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
            placeObj.setPlaceAddress("");
            placeObj.setPlaceIconUrl(jsonObject.getString("icon"));
            placeObj.setPlaceId(jsonObject.getString("place_id"));
            placeObj.setPlaceName(jsonObject.getString("name"));
            placeObj.setPlaceRating(jsonObject.getString("rating"));
            placeObj.setPlaceRef(jsonObject.getString("reference"));
            placeObj.setPlaceVicinity(jsonObject.getString("vicinity"));

            Constants.nearPlaces.add(placeObj);
        }
    }
}
