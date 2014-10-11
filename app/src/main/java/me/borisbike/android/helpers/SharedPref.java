package me.borisbike.android.helpers;

/**
 * Created by mani on 11/10/14.
 */

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private Context ctx;
    private final Long ONE_MONTH = 2592000000L;

    public SharedPref(Context context) {
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("me.borisbike.android", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public Long getLastStationsDownload(){
        return sharedPreferences.getLong("last_stations_download", 0);
    }

    public void setLastStationsDownload(){
        //set time to now, keep life simple
        sharedPreferencesEditor.putLong("last_stations_download", System.currentTimeMillis() );
        sharedPreferencesEditor.commit();
    }

    public boolean toSync(){
        //older than a month - then sync
        return getLastStationsDownload() + ONE_MONTH < System.currentTimeMillis();
    }

}