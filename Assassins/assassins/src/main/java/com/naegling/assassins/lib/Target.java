package com.naegling.assassins.lib;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/** Class to handle data about a target
 * @author David M Szabo
 */

public class Target {
	
	public String uid;
	public String name;
	double lat;
	double lon;
	public MarkerOptions marker;
	PlayerFunctions playerFunctions = new PlayerFunctions();

    /** Constructor for target class */
	public Target (String uid, String name){
		this.uid = uid;
		this.name = name;
		lon = 0.0;
        lat = 0.0;

        ProfileFunction profileFunction = new ProfileFunction();

        try {
            String url = profileFunction.getUserPicture(uid).getString("picture");
            String markerUrl = url.substring(0, url.length() -4) + "_marker.png";
            AsyncTask bmTask = new HttpBitMap().execute(new URL(markerUrl));
            Bitmap[] bitMap = (Bitmap[])bmTask.get();
            this.marker = new MarkerOptions().position(getLocation())
                    .title(name)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitMap[0]))
                    .anchor(0.5F, 0.5F);
        } catch (Exception e) {
            e.printStackTrace();
            this.marker = new MarkerOptions().position(getLocation())
                    .title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

    }
	
	public Target () {
	}
	
	// gets the position of a uuid in the form of LatLng, so it can be used for markers
	public LatLng getLocation() {
        JSONObject json = playerFunctions.getTargetLocation(uid);

        try {
            if (json.getString("success") != null){
                lat = Double.parseDouble(json.getString("lat"));
                lon = Double.parseDouble(json.getString("long"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LatLng(lat, lon);
    }
	
	// gets all the uuid-s from the database in an array, and then randomly selects one from the array
    public Target getRandomTarget(Context context) {
    	DatabaseHandler dbh = new DatabaseHandler(context);
    	HashMap hm = dbh.getUserDetails();
        String uuid = (String) hm.get("uid");
        
    	JSONArray json = playerFunctions.getAllUid(uuid);

    	Target target = null;
    	Random rand = new Random();
    	int randomInt = rand.nextInt(json.length());

        try {


            uid = json.getJSONObject(randomInt).getString("uuid");
            if(!uid.equals("0")) {
                JSONObject jsonName = playerFunctions.getName(uid);
                name = jsonName.getString("name");
                target = new Target(uid, name);
                JSONObject jTarget = playerFunctions.updateTarget(context, uid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return target;
    }

    // puts the online player's location into an array of markeroptions
    public MarkerOptions [] getMarkers() {

        JSONArray array = playerFunctions.getAllOnline();

        try {
            MarkerOptions[] markers = new MarkerOptions[array.length()]; 
	            for(int i = 0; i < array.length(); i++) {
	                JSONObject element = array.getJSONObject(i);
	                markers[i] = new MarkerOptions()
	                		.position(new LatLng(Double.parseDouble((String)element.get("lat")), Double.parseDouble((String)element.get("long"))))
	                		.title((String)element.get("name"));
	             }
	            return markers;
        	} catch (JSONException e) {
            e.printStackTrace();
            return null;
        	}
    }
    
    public ArrayList<Marker> friendMarkers () {
        JSONArray array = playerFunctions.getAllOnline();
        
		return null;
    }
    
}
