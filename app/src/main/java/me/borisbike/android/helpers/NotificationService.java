package me.borisbike.android.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import me.borisbike.android.R;
import me.borisbike.android.ShowSuggestionsActivity;

/**
 * Created by mani on 11/10/14.
 */

public class NotificationService extends Service {
    final int NOTIFICATION_ID = 1234599;
    private NotificationManager nm;
    private void createNotification(String title, String message){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("BorisBike.me")
                        .setContentText(message).setOngoing(true);

        Intent targetIntent = new Intent(this, ShowSuggestionsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
//        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, builder.build());

    }

    private void cancelNotification() {
        nm.cancel(NOTIFICATION_ID);
        Log.d("me.borisbike", "Should have destroyed notification");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("me.borisbike", "On destroy called");
        cancelNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        createNotification(title, message);

        return super.onStartCommand(intent, flags, startId);
    }

}

