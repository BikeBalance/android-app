package me.borisbike.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import me.borisbike.android.helpers.ToastGenerator;
import me.borisbike.android.network.HttpAsyncRequest;
import me.borisbike.android.network.OnAsyncTaskCompleted;


public class StartingPoint extends Activity implements OnAsyncTaskCompleted {
    private TextView debugView;
    private ToastGenerator toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_point);

        debugView = (TextView)findViewById(R.id.debug_area);
        toaster = new ToastGenerator(StartingPoint.this);

        d("Loading...");

        new HttpAsyncRequest(StartingPoint.this).execute("", "cycle.json", "GET");

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
        d((String)result.toString());
    }
}
