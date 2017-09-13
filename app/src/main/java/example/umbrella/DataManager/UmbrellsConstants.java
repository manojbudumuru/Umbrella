package example.umbrella.DataManager;

import android.content.SharedPreferences;

/**
 * Created by manojbudumuru on 9/12/17.
 */

public class UmbrellsConstants {

    public static final String[] tomTimeArray = {"12:00 AM", "1:00 AM","2:00 AM","3:00 AM","4:00 AM","5:00 AM","6:00 AM"
            ,"7:00 AM","8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM",
            "1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM",
            "7:00 PM","8:00 PM","9:00 PM","10:00 PM","11:00 PM"};
    public static final String umbrella = "example.umbrella";
    public static final String appStatus = "UAppStatus";
    public static final String zipCodeStatus = "UmbrellaZipCodeStatus";
    public static final String zipCode = "UZipCode";
    public static final String tempType = "UType";
    public static final String ok = "OK";
    public static final String[] degrees = {"Fahrenheit","Celsius"};
    public static final String fDegree = "Fahrenheit";
    public static final String cDegree = "Celsius";

    //Json
    public static final String location = "location";
    public static final String state = "state";
    public static final String city = "city";
    public static final String hourly_forecast = "hourly_forecast";
    public static final String fctime = "FCTTIME";
    public static final String hour = "hour";
    public static final String temp = "temp";
    public static final String english = "english";
    public static final String metric = "metric";
    public static final String condition = "condition";

    // N/W  URLS
    public static final String callTypeState = "STATE";
    public static final String callTypeForecast = "ForeCast";
    public static final String json = ".json";
    public static final String locationNameUrl = "http://api.wunderground.com/api/e221d2ee663b37a7/geolookup/q/";
    public static final String forcastURL = "http://api.wunderground.com/api/e221d2ee663b37a7/hourly10day/q/";

}
