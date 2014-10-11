package me.borisbike.android;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import me.borisbike.android.helpers.SharedPref;
import me.borisbike.android.helpers.ToastGenerator;
import me.borisbike.android.network.HttpAsyncRequest;
import me.borisbike.android.network.OnAsyncTaskCompleted;
import me.borisbike.android.stations.StationsDbHelper;
import me.borisbike.android.stations.StationsMeta;
import me.borisbike.android.stations.StationsProvider;


public class StartingPoint extends Activity implements OnAsyncTaskCompleted {
    private TextView debugView;
    private ToastGenerator toaster;
    private SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_point);

        debugView = (TextView)findViewById(R.id.debug_area);
        toaster = new ToastGenerator(StartingPoint.this);
        sharedPref = new SharedPref(StartingPoint.this);
        debugView.setText("");

        d("Loading...");

        //get the stations data
        if(sharedPref.toSync()){
            d("Getting latest stations info...");
            //get the stations data
           new HttpAsyncRequest(StartingPoint.this).execute("", "cycle.json", "GET");

        } else {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.starting_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void d(String str){
        debugView.append(str+"\n");
    }

    @Override
    public void onTaskCompleted(Object result) {
        //RECREATE TABLE
        StationsProvider stationsProvider = new StationsProvider();
        stationsProvider.createHelper(StartingPoint.this);
        d("Got a response");
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
                Integer terminalName = Integer.parseInt(data.getString("TerminalName"));

                if(installed == true && locked == false){
                    ContentValues val = new ContentValues();
                    val.put(StationsMeta.StationsTable.LAT, lat);
                    val.put(StationsMeta.StationsTable.LON, lon);
                    val.put(StationsMeta.StationsTable.NAME, name);
                    val.put(StationsMeta.StationsTable.TERMINAL_NAME, terminalName);
                    stationsProvider.insert(StationsMeta.CONTENT_URI, val);

                    Log.d("me.borisbike","Saved in database: \nNAME: "+name+"\nLAT: "+lat+" :: LON: "+lon+" \nTERMINAL NAME: "+terminalName);
                }

            }

            d("Got latest station info");
            //update last sync time
            sharedPref.setLastStationsDownload();

            setupGeofencing();
        } catch (Exception e){
            toaster.show("Failed to get latest stations info");
        }

    }


    public void setupGeofencing(){
        //TODO START A GEOSYNC BACKGROUND SERVICE WITH AN INTENT
    }


}
