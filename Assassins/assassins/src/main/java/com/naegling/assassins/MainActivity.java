package com.naegling.assassins;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naegling.assassins.lib.PlayerFunctions;
import com.naegling.assassins.lib.Target;
import com.naegling.assassins.lib.UserFunctions;


public class MainActivity extends ActionBarActivity {
	// Global Variables
	UserFunctions userFunctions;
    PlayerFunctions playerFunctions;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFunctions = new UserFunctions();
        playerFunctions = new PlayerFunctions();

        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            setContentView(R.layout.activity_main);

            assassinate = (Button) findViewById(R.id.assassinate_button);
            assassinate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean success = playerFunctions.assassinate(getApplicationContext());
                    targetClass = null;

                }
            });

            try {
                // Loading map
                initilizeMap();
                
                
                // this prints out the distance between you and target.
                distance = (TextView) findViewById(R.id.distance_text);
                
                // Acquire a reference to the system Location Manager - device's geographic location
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates / changes 
                locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        // Called when a new location is found by the network location provider.

                        playerFunctions.updatePlayerLocation(getApplicationContext(), location, "1");
                        //targetClass = null;
                        getTarget();


                        if(distanceInt >= 50){
                            assassinate.setClickable(false);
                            assassinate.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));

                        } else {
                            assassinate.setClickable(true);
                            assassinate.setBackgroundDrawable(getResources().getDrawable(R.drawable.assassinate_button));
                        }

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

        
        if (id == R.id.action_profile) {
        	Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
            profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profile);
            return true;
        }

        if (id == R.id.action_logout) {
        	locationManager.removeUpdates(locationListener);
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

}
