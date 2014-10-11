package me.borisbike.android.stations;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

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
    public Double[][] getStationLocations(){
        Cursor managedCursor = ctx.getContentResolver().query(StationsMeta.CONTENT_URI, null, null, null, null);
        Double[][] locations = new Double[managedCursor.getCount()][2];
        int i = 0;
        while(managedCursor.moveToNext()){
            Double[] d = new Double[2];
            d[0] = managedCursor.getDouble(managedCursor.getColumnIndex("latitude"));
            d[1] = managedCursor.getDouble(managedCursor.getColumnIndex("longitude"));
            locations[i] = d;
            i++;
        }

        return locations;
    }
}
