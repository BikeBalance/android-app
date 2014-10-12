package me.borisbike.android.network;


import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mani on 29/09/14.
 */
public class HttpRequest {

    private final String BASE_URL = "http://borisbike.me:3000/";

    private String endpoint;

    public HttpRequest(String endpoint){
        this.endpoint = endpoint;
    }

    public String postData(String calls, String requestType) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(60000);
        conn.setRequestMethod(requestType); //POST or GET - should have enums #fuckitshipit
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/json");

        if(requestType.equals("POST")){
            conn.setDoOutput(true);
            DataOutputStream dataOut = new DataOutputStream(conn.getOutputStream());
            dataOut.writeBytes(calls.toString());
        } else {
            conn.setDoOutput(false);
        }

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
