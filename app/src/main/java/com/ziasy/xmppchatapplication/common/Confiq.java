package com.ziasy.xmppchatapplication.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//import static com.ziasy.xmppchatapplication.TextChatApplication.TAG;

public class Confiq {
    static String address, city, state, country, postalCode, knownName;
    public static final String amazon_key = "JEOWPS5PEY4E5X6J42F4";
    public static final String amazon_secret = "U4Se+JKztU5tz8Vjz/SMpUNJwd3Xz+Z2VBlPe8ANrAg";
    public static final String DOCUMNMENT_URL_AWS="https://udtalks.sgp1.digitaloceanspaces.com/";
  // public static final String CHAT_SERVER_URL = "http://35.243.191.92:8000/";
   public static final String CHAT_SERVER_URL = "http://128.199.222.145:8001/";
    //  public static final String CHAT_SERVER_URL="http://13.59.184.203:8000/";
    // public static final String CHAT_SERVER_URL="http://35.243.191.92:8080/";
    //public static final String CHAT_SERVER_URL="http://111.118.249.198:2000/";
    public static final String KEY = "@ubuntu-s-1vcpu-1gb-sgp1-01";
    public static final String RETURN_IMAGE_URL = "https://udtalks.sgp1.digitaloceanspaces.com/";
    //public static final String RETURN_IMAGE_URL = "http://128.199.222.145/uploads/";
    public static final String GET_CHAT_HISTORY = "http://128.199.222.145/getchat.php";
    public static final String SEND_MULTIPLE_IMAGES = "http://128.199.222.145/uploadm.php";
    public static final String SEND_FILE = "http://128.199.222.145/upload.php";
    public static final String GET_CHAT_LIST = "http://128.199.222.145/getlist.php";
    public static final String LOGIN = "http://128.199.222.145/ofline/reg.php";


    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {


        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
/*

                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);
*/

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address + " " + city + " " + state;
    }
public static String getExtenstion(String data){
       String type = null;
    if (data.toLowerCase().endsWith(".xls") || data.toLowerCase().endsWith(".xlsb") || data.toLowerCase().endsWith(".xlsm") || data.toLowerCase().endsWith(".xlsx") || data.toLowerCase().endsWith(".xlt") || data.toLowerCase().endsWith(".xltx")) {
        type=".xlsx";

    } else if (data.toLowerCase().endsWith(".pdf")) {
     type=".pdf";

    } else {
        type=".docx";

    }
    return type;
}
}
