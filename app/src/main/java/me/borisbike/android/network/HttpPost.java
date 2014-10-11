package me.borisbike.android.network;


import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mani on 29/09/14.
 */
public class HttpPost {

    private final String BASE_URL = "http://borisbike.me:8000/";

    private String endpoint;

    public HttpPost(String endpoint){
        this.endpoint = endpoint;
    }

    public String postData(String calls) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(60000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        DataOutputStream dataOut = new DataOutputStream(conn.getOutputStream());
        dataOut.writeBytes(calls.toString());

        conn.connect();
        InputStream is = conn.getInputStream();

        int ch;
        StringBuffer sb = new StringBuffer();
        while ((ch = is.read()) != -1) {
            sb.append((char) ch);
        }
        return sb.toString();

    }
}
