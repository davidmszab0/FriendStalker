package com.naegling.assassins.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Johan on 2014-04-30.
 */
public class FriendMarker {

    PlayerFunctions playerFunctions;

    public MarkerOptions[] getMarkers() {

        playerFunctions = new PlayerFunctions();
        JSONArray array = playerFunctions.getAllOnline();

        try {
            MarkerOptions[] markers = new MarkerOptions[array.length()];
            for(int i = 0; i < array.length(); i++) {
                JSONObject element = array.getJSONObject(i);
                markers[i] = new MarkerOptions().position(new LatLng(Double.parseDouble((String)element.get("lat")), Double.parseDouble((String)element.get("long")))).title((String)element.get("name"));
            }
            return markers;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
