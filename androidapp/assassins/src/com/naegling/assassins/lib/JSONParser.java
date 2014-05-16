
package com.naegling.assassins.lib;


import java.io.InputStream;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArr = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
    	 AsyncTask bufferTask = new HTTpTask().execute(url, params);
         try {
             json = (String)bufferTask.get();
             Log.e("JSON", json);
         } catch (Exception e) {
             Log.e("Buffer Error", "Error converting result " + e.toString());
         }

<<<<<<< HEAD
=======
    	 AsyncTask bufferTask = new HTTpTask().execute(url, params);
         try {
             json = (String)bufferTask.get();
             Log.e("JSON", json);
         } catch (Exception e) {
             Log.e("Buffer Error", "Error converting result " + e.toString());
         }

>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }
    
    public JSONArray getJSONArrayFromUrl(String url, List<NameValuePair> params) {
<<<<<<< HEAD
=======

>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
        AsyncTask bufferTask = new HTTpTask().execute(url, params);
        try {
            json = (String)bufferTask.get();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jArr = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jArr;
    }
}

