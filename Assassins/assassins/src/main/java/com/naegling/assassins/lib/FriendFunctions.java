package com.naegling.assassins.lib;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Simonas Stirbys and Elsa Wide
 */
public class FriendFunctions {
		
	private JSONParser jsonParser;
    private static String playerURL = "http://www.davidmszabo.com/maffia/friend/";
    private static String friendIdTag = "friend_id_tag";
    private static String friendConfirmedTag = "friend_confirmed_tag";
    private static String searchUserTag = "search_username";
	private static String sendFriendRequest = "send_request";
    private static String friendRequesterTag = "get_friend_requests";
    private static String acceptFriend = "accept_request";
    private static String denyFriend = "deny_request";
    private static String getId = "get_friend_id";
    private static String friendOnlineStatus = "is_friend_online";
    private static String deleteFriend = "remove_friend";
	
    
	public FriendFunctions(){
		jsonParser = new JSONParser();
	}
	
	
	public JSONArray getFriendList(String userId){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
        params.add(new BasicNameValuePair("tag", friendIdTag));
        params.add(new BasicNameValuePair("uuid", userId));
        
        return jsonParser.getJSONArrayFromUrl(playerURL, params);
	}
	
	
	public JSONObject searchForFriend(String friendName){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", searchUserTag));
		params.add(new BasicNameValuePair("uuid", friendName));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
	}
	
	public JSONObject sendFriendRequest(String userName, String friendName){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", sendFriendRequest));
		params.add(new BasicNameValuePair("uuid", userName));
		params.add(new BasicNameValuePair("friendName", friendName));
        
        JSONObject json = jsonParser.getJSONFromUrl(playerURL, params);
		Log.d("Create response", json.toString());
        return json;
	}
	
	public JSONArray getFriendRequestList(String userId){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", friendRequesterTag));
		params.add(new BasicNameValuePair("uuid", userId));
        
        return jsonParser.getJSONArrayFromUrl(playerURL, params);
	}
	
	
	public JSONObject acceptFriend(String uuid, String friendName){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", acceptFriend));
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("friendName", friendName));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
	}


	public JSONObject denyFriend(String uuid, String friendName){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", denyFriend));
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("friendName", friendName));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
	}
	
	
	public JSONObject translateToUniqueID(String name){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", getId));
		params.add(new BasicNameValuePair("friendName", name));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
	}
	

	public JSONObject getOnlineStatus(String name){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", friendOnlineStatus));
		params.add(new BasicNameValuePair("friendName", name));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
	}
	
	
	public JSONObject removeFriend(String uuid, String name){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("tag", deleteFriend));
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("friendName", name));
        
        return jsonParser.getJSONFromUrl(playerURL, params);
	}
	
	
	public ArrayList<String> translate(JSONArray jsonArray, ArrayList<String> arrayList){		
		String friendListString = "";
		int startInt = 0;
		try {
			while(!friendListString.equals(null)){
				friendListString = jsonArray.getString(startInt);
				friendListString = friendListString.replace("{\"friend\":\"", "");
				friendListString = friendListString.replace("\"}", "");
				arrayList.add(friendListString); 
				startInt++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
}
