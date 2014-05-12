package com.naegling.assassins;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.naegling.assassins.lib.JSONParser;

public class ProfileFunction extends AsyncTask {
	private JSONParser jsonParser;
	
	private static String profileURL = "http://www.davidmszabo.com/maffia/profile/";
	private static String getProfileKillTag = "get_user_kills";
	private static String getProfileDeathTag = "get_user_deaths";
	private static String getProfileWeaponTag = "get_user_weapon";
	private static String getProfileArmourTag = "get_user_armour";
	
	public ProfileFunction() {jsonParser = new JSONParser();}
	
	public JSONObject getUserKills(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileKillTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	public JSONObject getUserDeaths(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileDeathTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	public JSONObject getUserWeapon(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileWeaponTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	
	public JSONObject getUserArmour(String userId) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileArmourTag));
		params.add(new BasicNameValuePair("uuid", userId));
		
		return jsonParser.getJSONFromUrl(profileURL, params);
	}
	

	@Override
	protected Object doInBackground(Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}
}