package me.borisbike.android.network;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * new
 */

public class HttpAsyncPost extends AsyncTask<String, Integer, String> {
    private OnAsyncTaskCompleted listener;

    public HttpAsyncPost(OnAsyncTaskCompleted listener) {
        this.listener = listener;
    }
    //required methods

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            HttpPost poster = new HttpPost(params[1]);
            result = poster.postData(params[0]);
            return result;

            //Toast.makeText(getApplicationContext(), "got a response", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "error happened ", Toast.LENGTH_LONG).show();

        }
        return result;
    }

    protected void onPostExecute(String result) {
        //pb.setVisibility(View.GONE);
        //Toast.makeText(context, "request sent", Toast.LENGTH_LONG).show();
        listener.onTaskCompleted(result);
    }





}