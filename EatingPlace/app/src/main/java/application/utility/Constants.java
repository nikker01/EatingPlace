package application.utility;

import java.util.ArrayList;

import eatingplace.objects.PlaceObject;

/**
 * Created by Henry on 2015/4/26.
 */
public class Constants {

    public static String WEB_SERVICE_KEY = "AIzaSyCPsdyYJNNkUuQjdHBTThVVGr82ZXD2PyU";

    public static String NEAR_SEARCH_LOCATION = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String NEAR_SEARCH_PARAMS = "&rankby=distance&type=food|bakery|cafe|restaurant|meal_delivery|meal_takeaway&key="+
            WEB_SERVICE_KEY ;

    public static double userLatitude;
    public static double userLongitude;

    public static enum callMethods{
        getNearPlace,
        getTextSearchPlace
    }

    public static ArrayList<PlaceObject> nearPlaces = new ArrayList<PlaceObject>();

}
