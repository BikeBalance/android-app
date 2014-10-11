package me.borisbike.android.stations;

import android.content.Context;
import org.json.JSONArray;


public class StationsManager {
    private Context ctx;
    private JSONArray activities;
    private String auth_token;
    private String device_id;

    public StationsManager(Context context, String auth_token, String id){
        this.auth_token = auth_token;
        this.ctx = context;
        this.device_id = id;
    }

    //get activities from server
    //save it in database using stations provider
    //will use a GeoFencesSetup service (background?) to set up geofences
}
