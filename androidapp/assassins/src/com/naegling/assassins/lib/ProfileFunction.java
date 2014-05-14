package com.naegling.assassins.lib;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ProfileFunction extends AsyncTask {
	private JSONParser jsonParser;
	
	// Tags for the json
	private static String profileURL = "http://www.davidmszabo.com/maffia/profile/";
	private static String getProfileKillTag = "get_user_kills";
	private static String getProfileDeathTag = "get_user_deaths";
	private static String getProfileWeaponTag = "get_user_weapon";
	private static String getProfileArmourTag = "get_user_armour";
	private static String getProfilePictureTag = "get_user_picture";
	private static String setProfilePictureTag = "set_user_picture";
	
	public ProfileFunction() {jsonParser = new JSONParser();}
	
	// get the user kills with the unique user id
	public JSONObject getUserKills(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileKillTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	// get the user deaths with the unique user id
	public JSONObject getUserDeaths(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileDeathTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	// get the user weapon with the unique user id
	public JSONObject getUserWeapon(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileWeaponTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	// get the user armour with the unique user id
	public JSONObject getUserArmour(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileArmourTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	// get the user profile picture with the unique user id
	public JSONObject getUserPicture(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfilePictureTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	// Set the path to the picture in the database
    public JSONObject setUserPicture(String userId) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setProfilePictureTag));
        params.add(new BasicNameValuePair("uuid", userId));

        return jsonParser.getJSONFromUrl(profileURL, params);
    }
	
	

	@Override
	protected Object doInBackground(Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}
}