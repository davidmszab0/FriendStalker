package com.naegling.assassins.lib;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.naegling.assassins.ProfileActivity;

/**
 * Created by Johan on 2014-04-28.
 */
public class PlayerFunctions {

    private JSONParser jsonParser;

    private static String playerURL = "http://www.davidmszabo.com/maffia/player/";
    private static String setLocationTag = "set_location";
    private static String getTargetLocationTag = "get_target_location";
    private static String setOnlineStatusTag = "set_online_status";
    private static String getAllTag = "get_all";
    private static String getAlluidTag = "get_all_uid";
    private static String updateTargetTag = "update_target";
    private static String getNameTag = "get_name";


    public PlayerFunctions() {
    	jsonParser = new JSONParser(); 
    }

    public JSONObject updatePlayerLocation(Context context, Location location, String status){
        DatabaseHandler dbh = new DatabaseHandler(context);
        HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", setLocationTag));
        params.add(new BasicNameValuePair("uuid", uid));
        params.add(new BasicNameValuePair("lat", location.getLatitude() + ""));
        params.add(new BasicNameValuePair("long", location.getLongitude() + ""));
        params.add(new BasicNameValuePair("status", status));

        return jsonParser.getJSONFromUrl(playerURL, params);
    }

    public JSONObject getTargetLocation(String target){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTargetLocationTag));
        params.add(new BasicNameValuePair("uuid", target));

        return jsonParser.getJSONFromUrl(playerURL, params);
    }

    public JSONObject setOnlineStatus(Context context, String status) {
        DatabaseHandler dbh = new DatabaseHandler(context);
        HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setOnlineStatusTag));
        params.add(new BasicNameValuePair("uuid", uid));
        params.add(new BasicNameValuePair("status", status));

        return jsonParser.getJSONFromUrl(playerURL, params);
    }

    public JSONArray getAllOnline(){
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getAllTag));

        return jsonParser.getJSONArrayFromUrl(playerURL, params);
    }
    
    public JSONObject updateTarget(Context context, String target) {
    	DatabaseHandler dbh = new DatabaseHandler(context);
    	HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", updateTargetTag));
    	params.add(new BasicNameValuePair("uuid", uid));
    	params.add(new BasicNameValuePair("targetUuid", target));
    	
    	return jsonParser.getJSONFromUrl(playerURL, params);
    	
    }
    
    public JSONArray getAllUid(String uid){
        
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getAlluidTag));
        params.add(new BasicNameValuePair("uuid", uid));
        
        return jsonParser.getJSONArrayFromUrl(playerURL, params);
    }
    
    public JSONObject getName(String uuid) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", getNameTag));
    	params.add(new BasicNameValuePair("uuid", uuid));
    	
    	return jsonParser.getJSONFromUrl(playerURL, params);
    }
    
    
    public boolean assassinate(Context context) {
    	DatabaseHandler dbh = new DatabaseHandler(context);
    	HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");
        
    	ProfileActivity profile = new ProfileActivity();
    	Random rand = new Random();
    	int roll = rand.nextInt(100);
    	System.out.println(roll);
    	
    	
    	System.out.println(roll);
    	
    	
    	if (roll > 41) {
    		
    		
    		return true;
    	} else return false;
    	
    }
}

