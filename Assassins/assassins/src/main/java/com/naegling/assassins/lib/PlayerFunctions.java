package com.naegling.assassins.lib;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

/**
 * Created by Johan Nilsson, Henrik Edholm and Mikaela Lidström.
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
    private static String updateStatisticsTag = "update_statistics";
    private static String getKillerTag = "get_killer";
    private static String getAllRankingTag = "get_all_ranking";
    private static String getFriendRankingTag = "get_friend_ranking";


    public PlayerFunctions() {
    	jsonParser = new JSONParser(); 
    }

    /**
     * Updates players location and status on server
     * @author Johan Nilsson
     * @param context
     * @param location
     * @param status
     * @return
     */
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

    /**
     * Gets a JSON object representation of the targets location
     * @author Johan Nilsson
     * @param target
     * @return
     */
    public JSONObject getTargetLocation(String target){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTargetLocationTag));
        params.add(new BasicNameValuePair("uuid", target));

        return jsonParser.getJSONFromUrl(playerURL, params);
    }

    /**
     * Sets the online status of the player on server
     * @author Johan Nilsson
     * @param context
     * @param status
     * @return
     */
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

    /**
     * Gets a JSON array representation of all players online
     * @author Johan Nilsson
     * @return
     */
    public JSONArray getAllOnline(){
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getAllTag));

        return jsonParser.getJSONArrayFromUrl(playerURL, params);
    }

    /** Get the name of a player
     * @author Mikaela Lidström
     * @param uuid
     * @return
     */
    public JSONObject getName(String uuid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getNameTag));
        params.add(new BasicNameValuePair("uuid", uuid));

        return jsonParser.getJSONFromUrl(playerURL, params);
    }

    /**
     * Updates the target of a player
     * @author Mikaela Lidström
     * @param context
     * @param target
     * @return
     */
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

    /**
     * Gets all other players online except player that is passed as argument
     *
     * @author
     * @param uid
     * @return
     */
    public JSONArray getAllUid(String uid){
        
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getAlluidTag));
        params.add(new BasicNameValuePair("uuid", uid));
        
        return jsonParser.getJSONArrayFromUrl(playerURL, params);
    }

    /**
     * Gets a JSONarray representation of the five top ranked players
     * @author Mikeala Lidström
     * @return
     */
    public JSONArray getAllRanking(){
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", getAllRankingTag));
    	
    	return jsonParser.getJSONArrayFromUrl(playerURL, params);
    }

    /**
     * Gets a JSONarray representation of a players friends ranking
     * @param context
     * @return
     */
    public JSONArray getFriendRanking(Context context){
    	DatabaseHandler dbh = new DatabaseHandler(context);
    	HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", getFriendRankingTag));
    	params.add(new BasicNameValuePair("uuid", uid));
    	
    	return jsonParser.getJSONArrayFromUrl(playerURL, params);
    }

    /**
     * Updates a players statistics after assasination
     * @author Mikaela Lidström
     * @param context
     * @param target
     * @return
     */
    public JSONObject updateStatistics(Context context, String target) {
    	DatabaseHandler dbh = new DatabaseHandler(context);
    	HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", updateStatisticsTag));
        params.add(new BasicNameValuePair("uuid", uid));
        params.add(new BasicNameValuePair("target", target));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
    }

    /**
     * Gets a players killer
     * @author Henrik Edholm
     * @param context
     * @return
     */
    public JSONObject getKiller(Context context) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		HashMap hm = dbh.getUserDetails();
		String uid = (String) hm.get("uid");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getKillerTag));
		params.add(new BasicNameValuePair("uuid", uid));
		
		return jsonParser.getJSONFromUrl(playerURL, params);
	}

    /**
     * Calculates and checks if an assassination is successful or not
     * @author Henrik Edholm
     * @param context
     * @param target
     * @return
     */
    public boolean assassinate(Context context, String target) {
    	DatabaseHandler dbh = new DatabaseHandler(context);
    	HashMap hm = dbh.getUserDetails();
        String uid = (String) hm.get("uid");
        
        ProfileFunction profile = new ProfileFunction();
        
    	Random rand = new Random();
    	int roll = rand.nextInt(100);
    	
    	JSONObject uArm = profile.getUserArmour(uid);
    	JSONObject tArm = profile.getUserArmour(target);
    	JSONObject uWeap = profile.getUserWeapon(uid);
    	JSONObject tWeap = profile.getUserWeapon(target);
    	
    	int targetBonus = 0;
    	int userBonus = 0;
    	try {
    	targetBonus = tArm.getInt("bonusSurv") + tWeap.getInt("bonusSurv");
    	userBonus = uArm.getInt("bonusKill") + uWeap.getInt("bonusKill");
    	} catch(JSONException e) {
    		e.printStackTrace();
    	}
    	
    	System.out.println(roll);
    	roll += (userBonus - targetBonus);
    	System.out.println(roll);
    	
    	
    	if (roll > 39) {
    		
    		return true;
    	} else return false;
    	
    }
}

