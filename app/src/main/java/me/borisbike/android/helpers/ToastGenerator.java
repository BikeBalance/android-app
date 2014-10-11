package me.borisbike.android.helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by mani on 11/10/14.
 */
public class ToastGenerator {
    private Activity act = null;
    private int length = Toast.LENGTH_SHORT;
    private Context context = null;

    public ToastGenerator(Activity activity){
        this.act = activity;
    }
    public ToastGenerator(Context context){
        this.context = context;
    }
    public void show(String str){
        if(this.act != null) {
            Toast.makeText(act, str, length).show();
            return;
        }

        if (this.context != null) {
            Toast.makeText(context, str, length).show();
            return;
        }
    }
}
