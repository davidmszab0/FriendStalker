package com.naegling.assassins.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Target {
	
	public String uid;
	public String name;
	double lat;
	double lon;
	public MarkerOptions marker;
	PlayerFunctions playerFunctions = new PlayerFunctions();
 	
	public Target (String uid, String name){
		this.uid = uid;
		this.name = name;
		lon = 0.0;
        lat = 0.0;
        
		// create marker
   	 	this.marker = new MarkerOptions().position(getTarget(uid))
   			 .title("Assassinate")
   			 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	}
	
	public Target () {
	}
	
	// gets the position of a uuid in the form of LatLng, so it can be used for markers
	private LatLng getTarget(String target) {
        JSONObject json = playerFunctions.getTargetLocation(target);

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
		    	JSONObject jsonName = playerFunctions.getName(uid);
		    	name = jsonName.getString("name"); 
				target = new Target(uid, name);
				JSONObject jTarget = playerFunctions.updateTarget(context, uid); 
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
