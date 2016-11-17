package com.example.heem.togetherfit;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/***
 * Elham Jaffar : To use DirectionJSONParser to draw the path between 2 locations.
 * Saint Joseph University as a default source
 * User choosing distention
 */
public class DirectionsJSONParser {

    // constructor
    public DirectionsJSONParser() {

    }
    public String downloadUrl(String strUrl) {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("downloadURL", e.toString());
        }finally{
            try {
                if(iStream != null)
                    iStream.close();
            }catch (IOException e) {

            }
            urlConnection.disconnect();
        }
        return data;
    }
}//End the file
