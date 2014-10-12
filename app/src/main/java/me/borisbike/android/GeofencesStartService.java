package me.borisbike.android;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.borisbike.android.geofences.GeofenceRequester;
import me.borisbike.android.geofences.SimpleGeofence;
import me.borisbike.android.geofences.SimpleGeofenceStore;
import me.borisbike.android.network.HttpAsyncRequest;
import me.borisbike.android.network.OnAsyncTaskCompleted;
import me.borisbike.android.stations.StationsMeta;
import me.borisbike.android.stations.StationsProvider;

/**
 * Created by mani on 12/10/14.
 */
public class GeofencesStartService extends Service implements OnAsyncTaskCompleted {
        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Toast.makeText(this, "Time to update fences", Toast.LENGTH_LONG).show();

            //this is for recognising activities
            //and storing them

            new HttpAsyncRequest(GeofencesStartService.this).execute("", "stations/top", "GET");

            return START_STICKY;
        }

        @Override
        public void onDestroy() {

            super.onDestroy();
            Toast.makeText(this, "Destroy called", Toast.LENGTH_LONG).show();


        }


        @Override
        public void onCreate() {
            super.onCreate();

        }

    @Override
    public void onTaskCompleted(Object result) {
        //RECREATE TABLE
        List<Geofence> fences = new ArrayList<Geofence>();
        SimpleGeofenceStore simpleGeofenceStore = new SimpleGeofenceStore(GeofencesStartService.this);
        try {
            JSONObject resultData = new JSONObject((String)result);

            JSONArray stations = resultData.getJSONArray("Station");


            for(int i=0; i < stations.length(); i++){
                JSONObject data = stations.getJSONObject(i);

                Boolean installed = data.getBoolean("Installed");
                String name = data.getString("Name");
                Double lon = data.getDouble("Long");
                Double lat = data.getDouble("Lat");
                Boolean locked = data.getBoolean("Locked");
               String terminalName = data.getString("TerminalName");

                if(installed == true && locked == false){
//                    ContentValues val = new ContentValues();
//                    val.put(StationsMeta.StationsTable.LAT, lat);
//                    val.put(StationsMeta.StationsTable.LON, lon);
//                    val.put(StationsMeta.StationsTable.NAME, name);
//                    val.put(StationsMeta.StationsTable.TERMINAL_NAME, terminalName);


                    SimpleGeofence simpleGeofence = new SimpleGeofence(terminalName, lat, lon, 15, (60000*15L), Geofence.GEOFENCE_TRANSITION_ENTER );

                    fences.add(simpleGeofence.toGeofence());
                    simpleGeofenceStore.setGeofence(terminalName, simpleGeofence);

                    Log.d("me.borisbike","Saved in database: \nNAME: "+name+"\nLAT: "+lat+" :: LON: "+lon+" \nTERMINAL NAME: "+terminalName);
                }

            }

            GeofenceRequester geofenceRequester = new GeofenceRequester(GeofencesStartService.this);
            geofenceRequester.addGeofences(fences);

        } catch (Exception e){
            Toast.makeText(this, "Failed to set geofences", Toast.LENGTH_LONG).show();
        }

    }


}
