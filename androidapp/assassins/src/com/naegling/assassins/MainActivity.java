package com.naegling.assassins;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naegling.assassins.lib.PlayerFunctions;
import com.naegling.assassins.lib.Target;
import com.naegling.assassins.lib.UserFunctions;

public class MainActivity extends ActionBarActivity {
    UserFunctions userFunctions;
    PlayerFunctions playerFunctions;
    private GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Target randomTarget = new Target();
    Target targetClass;
    TextView test;
    Marker targetMarker = null;
    MarkerOptions[] markers;
    Marker friendM = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFunctions = new UserFunctions();
        playerFunctions = new PlayerFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            setContentView(R.layout.activity_main);

            try {
                // Loading map
                initilizeMap();
                
                // these are System outprints
                test = (TextView) findViewById(R.id.test);
                
                // Acquire a reference to the system Location Manager - device's geographic location
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates / changes 
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                    	playerFunctions.updatePlayerLocation(getApplicationContext(), location, "1");
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {
                    	Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
                    }

                    public void onProviderDisabled(String provider) {
                    	Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
                    }
                };

                // Register the listener with the Location Manager to receive location updates - 2seconds, 2 meters
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, locationListener);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing main activity screen
            finish();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
        playerFunctions.setOnlineStatus(getApplicationContext(), "0");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationManager != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, locationListener);
        playerFunctions.setOnlineStatus(getApplicationContext(), "1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        userFunctions = new UserFunctions();

        if (id == R.id.action_settings) {
            return true;
        }


        if (id == R.id.action_profile) {
            return true;
        }

        if (id == R.id.action_logout) {
            locationManager.removeUpdates(locationListener);
            playerFunctions.setOnlineStatus(getApplicationContext(), "0");
            userFunctions.logoutUser(getApplicationContext());
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing mainactivity screen
            finish();
            return true;
        }
        
        if (id == R.id.get_target) {
        	
        	// it there is one target, then remove it and then create another one
        	if (targetMarker != null){
        		targetMarker.remove();
        	}
        	
        	// gets the random target and adds it to the map
        	targetClass = randomTarget.getRandomTarget(getApplicationContext());
        	targetMarker = googleMap.addMarker(targetClass.marker);
        	
        	 
        	// to check if the marker already exists on the map
//        	if (randomTarget.isMarkerOnArray(markers, targetMarker)) {
//        		friendM.remove();
//        	}
        	
        	// just getting the id of the target
        	test.setText("" + targetMarker.getId());
        }
        
        if (id == R.id.show_friends) {
        	
        	// this section is for Johan: we need to remove the markers from the map
//        	if (markers.length != 0) {
//        		for (int i = 0; i < markers.length; i++){
//    	        	markers[i].visible(false);
//    	        }
//        	}
        	
        	markers = randomTarget.getMarkers();

	        for (int i = 0; i < markers.length; i++){
	        	friendM = googleMap.addMarker(markers[i]);
	        }
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            googleMap.setMyLocationEnabled(true);
        }
    }
}

