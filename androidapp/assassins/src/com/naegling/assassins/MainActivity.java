
package com.naegling.assassins;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
<<<<<<< HEAD
import android.os.AsyncTask;
=======
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
=======
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
<<<<<<< HEAD
import com.naegling.assassins.lib.Notifications;
import com.naegling.assassins.lib.PlayerFunctions;
import com.naegling.assassins.lib.ProfileFunction;
import com.naegling.assassins.lib.Target;
=======
import com.naegling.assassins.lib.FriendMarker;
import com.naegling.assassins.lib.PlayerFunctions;
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
import com.naegling.assassins.lib.UserFunctions;


public class MainActivity extends ActionBarActivity {
<<<<<<< HEAD
	// Global Variables
	UserFunctions userFunctions;
    PlayerFunctions playerFunctions;
    ProfileFunction profileFunc = new ProfileFunction();
    Notifications notifications = new Notifications();
    private GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Target randomTarget = new Target();
    Target targetClass = null;
    TextView distance;
    Marker targetMarker = null;
    MarkerOptions[] markers;
    Marker friendM = null;
    Location currLocation;
    Button assassinate;
    int distanceInt = 0;
    int oldDeaths = 0;
=======
    UserFunctions userFunctions;
    PlayerFunctions playerFunctions;
    private GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener;
    FriendMarker friendMarker = new FriendMarker();
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFunctions = new UserFunctions();
        playerFunctions = new PlayerFunctions();
<<<<<<< HEAD
        

=======
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            setContentView(R.layout.activity_main);
            
            assassinate = (Button) findViewById(R.id.assassinate_button);
            assassinate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean success = playerFunctions.assassinate(getApplicationContext(), targetClass.uid);
                    if(success) {
                    	playerFunctions.updateStatistics(getApplicationContext(), targetClass.uid);
                    	Toast.makeText( getApplicationContext(), "You slaughtered " + targetClass.name, Toast.LENGTH_LONG).show();
                    } else {
                    	Toast.makeText( getApplicationContext(), targetClass.name + " escaped!", Toast.LENGTH_LONG).show();
                    }
                    targetClass = null;
                    getTarget();

                }
            });

            try {
                // Loading map
                initilizeMap();
<<<<<<< HEAD
                getOldDeaths();
                
                // this prints out the distance between you and target.
                distance = (TextView) findViewById(R.id.distance_text);
                
                // Acquire a reference to the system Location Manager - device's geographic location
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates / changes 
                locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        // Called when a new location is found by the network location provider.

                    	
                        playerFunctions.updatePlayerLocation(getApplicationContext(), location, "1");
                        getTarget();
                        checkIfKilled();
                        
                        if(distanceInt > 15000) {
                        	targetClass = null;
                        	Toast.makeText( getApplicationContext(), "This target is too far away", Toast.LENGTH_SHORT).show();
                        }
                        
                        if(distanceInt >= 50){
                            assassinate.setClickable(false);
                            assassinate.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));

                        } else {
                            assassinate.setClickable(true);
                            assassinate.setBackgroundDrawable(getResources().getDrawable(R.drawable.assassinate_button));
                        }

=======

                final TextView textLat = (TextView) findViewById(R.id.textLat);
                final TextView textLong = (TextView) findViewById(R.id.textLong);

                MarkerOptions[] markers = friendMarker.getMarkers();

                for (int i = 0; i < markers.length; i++){
                    googleMap.addMarker(markers[i]);
                }

                // Acquire a reference to the system Location Manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        textLat.setText("" + location.getLatitude());
                        textLong.setText("" + location.getLongitude());
                        playerFunctions.updatePlayerLocation(getApplicationContext(), location, "1");
                        //marker.position(getTarget(target));
                        //playerFunctions.setOnlineStatus(getApplicationContext(), "1");
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

<<<<<<< HEAD
                    public void onProviderEnabled(String provider) {
                    	Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
                    }

                    public void onProviderDisabled(String provider) {
                    	Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
                    }
                };

                // Register the listener with the Location Manager to receive location updates - 2seconds, 2 meters
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, locationListener);


=======
                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed

            } catch (Exception e) {
                e.printStackTrace();
            }
<<<<<<< HEAD
            

            
=======
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed

        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing main activity screen
            finish();
        }

<<<<<<< HEAD
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


	/**
	 * function to load map. If map is not created it will create it for you
	 * */
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
	    }
	    	    
	  //my location button
        googleMap.setMyLocationEnabled(true);
    }
	
	
	
=======
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        playerFunctions.setOnlineStatus(getApplicationContext(), "1");
    }
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed

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

<<<<<<< HEAD
        
        if (id == R.id.action_profile) {
        	Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
            profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profile);
=======
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == R.id.action_profile) {
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
            return true;
        }

        if (id == R.id.action_logout) {
<<<<<<< HEAD
        	locationManager.removeUpdates(locationListener);
=======
            locationManager.removeUpdates(locationListener);
>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
            playerFunctions.setOnlineStatus(getApplicationContext(), "0");
            userFunctions.logoutUser(getApplicationContext());
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing main activity screen
            finish();
            return true;
        }
        
        if (id == R.id.get_target) {        	
        	getTarget();
        	
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void getTarget() {
    	// gets the random target and adds it to the map
    	if(targetClass == null) {
        	targetClass = randomTarget.getRandomTarget(getApplicationContext());
    	}
    	// if there is a marker already, remove it.
    	if (targetMarker != null){
    		targetMarker.remove();
    	}
    	// get the target and place a marker on the map
    	targetMarker = googleMap.addMarker(targetClass.marker);
    	assassinate.setText("Assassinate " + targetClass.name);
    	
    	//get distance from user location to target location
    	Location location = convertLocation(targetMarker.getPosition());
    	currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	distanceInt = (int)currLocation.distanceTo(location);
    	distance.setText(Integer.toString(distanceInt) + " m to target");
    }
    
    public Location convertLocation(LatLng latLong) {
    	
    	Location location = new Location("Test");
    	location.setLatitude(latLong.latitude);
    	location.setLongitude(latLong.longitude);
    	
    	return location;
	
    }
    private class LocationTask extends AsyncTask<Location, Object, Object> {

        @Override
        protected Object doInBackground(Location... params) {

            playerFunctions.updatePlayerLocation(getApplicationContext(), params[0], "1");
            //targetClass = null;
            getTarget();


            if(distanceInt >= 50){
                assassinate.setClickable(false);
                assassinate.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));

<<<<<<< HEAD
            } else {
                assassinate.setClickable(true);
                assassinate.setBackgroundDrawable(getResources().getDrawable(R.drawable.assassinate_button));
            }
            return null;
        }
    }
    
    public void checkIfKilled() {
    	JSONObject Deaths = profileFunc.getUserDeaths(getApplicationContext());
    	int newDeaths = 0;
    	try {
			newDeaths = Deaths.getInt("deaths");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(oldDeaths < newDeaths) {
    		String killer = "";
    		JSONObject json = playerFunctions.getKiller(getApplicationContext());
    		oldDeaths = newDeaths;
    		try {
				killer = json.getString("killer");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		notifications.youGotKilled(getApplicationContext(), killer);
    	}       
    }
    
    public void getOldDeaths() {
    	System.out.println("I'm in deaths");
    	JSONObject Deaths = profileFunc.getUserDeaths(getApplicationContext());
        System.out.println("getting deaths");
    	try {
			oldDeaths = Deaths.getInt("deaths");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("these are the old deaths " + oldDeaths);
    }
}
>>>>>>> Mikaela
=======
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

    private LatLng getTarget(String target) {
        JSONObject json = playerFunctions.getTargetLocation(target);
        double lon = 0.0;
        double lat = 0.0;

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
}

>>>>>>> ce3ee1488983100b77648af93e7dabf4a10f17ed
