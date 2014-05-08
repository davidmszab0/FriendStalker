package com.naegling.assassins.lib;


/**
 * Created by Johan on 2014-04-23.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        	System.out.println("buffering json data");
            json = (String)bufferTask.get();
            Log.e("JSON", json);
            System.out.println("finished buffering");
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
        	System.out.println("parsing json");
            jObj = new JSONObject(json);
            System.out.println("finished parsing");
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONArray getJSONArrayFromUrl(String url, List<NameValuePair> params) {

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
