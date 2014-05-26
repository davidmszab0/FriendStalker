package com.naegling.assassins.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;


/**
 * @author Mikaela Lidström and Henrik Edholm
 */

public class ProfileFunction {
	private JSONParser jsonParser;

	// Tags for the json
	private static String profileURL = "http://www.davidmszabo.com/maffia/profile/";
	private static String getProfileKillTag = "get_user_kills";
	private static String getProfileDeathTag = "get_user_deaths";
	private static String getProfileWeaponTag = "get_user_weapon";
	private static String getProfileArmourTag = "get_user_armour";
	private static String getProfilePictureTag = "get_user_picture";
	private static String setProfilePictureTag = "set_user_picture";
	private static String getProfileTag = "get_profile";
	private static String collectItemTag = "collect_item";
	private static String getProfileKillstreakTag = "get_user_killstreak";
    private static String setProfileKillstreakTag = "set_killstreak";
    private static String isItemCollectableTag = "is_item_collectable";
    private static String setItemCollectableTag = "set_item_collectable";
    private static String setItemTag = "set_item";

	public ProfileFunction() {jsonParser = new JSONParser();}


    /** get the profile data for user
     * @author Mikaela Lidström
     * @param userId
     * @return
     */
	public JSONObject getProfile(String userId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** get the user kills with the unique user id
     * @author Mikaela Lidström
     * @param userId
     * @return
     */
	public JSONObject getUserKills(String userId) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileKillTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** get the user kills with the unique user id
     * @author Mikaela Lidström
     * @param context
     * @return
     */
	public JSONObject getUserDeaths(Context context) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		HashMap hm = dbh.getUserDetails();
		String uid = (String) hm.get("uid");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileDeathTag));
		params.add(new BasicNameValuePair("uuid", uid));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** get the user deaths with the unique user id
     * @author Henrik Edholm
     * @param userId
     * @return
     */
	public JSONObject getUserDeaths(String userId) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileDeathTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}


    /** get the user weapon with the unique user id
     * @author Henrik Edholm
     * @param userId
     * @return
     */
	public JSONObject getUserWeapon(String userId) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileWeaponTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** get the user armour with the unique user id
     * @author Henrik Edholm
     * @param userId
     * @return
     */
	public JSONObject getUserArmour(String userId) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfileArmourTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** get the user profile picture with the unique user id
     * @author Johan Nilsson
     * @param userId
     * @return
     */
	public JSONObject getUserPicture(String userId) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getProfilePictureTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** Set the path to the picture in the database
     * @author Johan Nilsson
     * @param userId
     * @return
     */
	public JSONObject setUserPicture(String userId) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", setProfilePictureTag));
		params.add(new BasicNameValuePair("uuid", userId));

		return jsonParser.getJSONFromUrl(profileURL, params);
	}

    /** Get the current user killstreak number
     * @author Johan Nilsson
     * @param userId
     * @return
     */
    public JSONObject getUserKillstreak(String userId) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getProfileKillstreakTag));
        params.add(new BasicNameValuePair("uuid", userId));

        return jsonParser.getJSONFromUrl(profileURL, params);
    }

    /**
     * @author Johan Nilsson
     * @param userId
     * @return
     */
	public JSONObject collectItem(String userId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", collectItemTag));
        params.add(new BasicNameValuePair("uuid", userId));

        return jsonParser.getJSONFromUrl(profileURL, params);
    }

    /** Checks if a player has an item to collect
     * @author Johan Nilsson
     * @param userId
     * @return
     */
    public JSONObject isItemCollectable(String userId) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", isItemCollectableTag));
        params.add(new BasicNameValuePair("uuid", userId));

        return jsonParser.getJSONFromUrl(profileURL, params);
    }

    /** Sets if player has an item is collectable or not
     * @author Johan Nilsson
     * @param userId
     * @param collectable
     * @return
     */
    public JSONObject setItemCollectable(String userId, boolean collectable) {
        String collectableString = collectable ? "1" : "0";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setItemCollectableTag));
        params.add(new BasicNameValuePair("uuid", userId));
        params.add(new BasicNameValuePair("item_to_collect", collectableString));

        return jsonParser.getJSONFromUrl(profileURL, params);

    }

    /** Sets the item for the player on the server
     * @author Johan Nilsson
     * @param userId
     * @param type
     * @param itemId
     * @return
     */
    public JSONObject setItem(String userId, String type, String itemId){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setItemTag));
        params.add(new BasicNameValuePair("uuid", userId));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("item_id", itemId));

        return jsonParser.getJSONFromUrl(profileURL, params);
    }

}