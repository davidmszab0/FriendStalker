package com.naegling.assassins.lib;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

/** Class with functions for login and register
 * @author Johan Nilsson
 */

public class UserFunctions extends AsyncTask{

    private JSONParser jsonParser;

    private static String loginURL = "http://www.davidmszabo.com/maffia/login/";
    private static String registerURL = "http://www.davidmszabo.com/maffia/login/";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String store_user_in_target_tag = "storeUserInTarget";
    private static String changePasswordTag = "change_password";
    private static String newPasswordTag = "new_password";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
    
    public JSONObject userInTarget(String uuid) {
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", store_user_in_target_tag));
        params.add(new BasicNameValuePair("uuid", uuid));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    /**
     * Function to change password in database
     */
    public JSONObject changePassword(String uuid, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", changePasswordTag));
        params.add(new BasicNameValuePair("uuid", uuid));
        params.add(new BasicNameValuePair("password", password));

        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);

        return json;
    }

    /**
     * Function to reset a forgotten password
     */
    public JSONObject newPassword(String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", newPasswordTag));
        params.add(new BasicNameValuePair("email", email));

        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);

        return json;
    }
}