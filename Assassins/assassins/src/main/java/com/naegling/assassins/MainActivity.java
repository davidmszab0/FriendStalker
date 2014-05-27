package com.naegling.assassins;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.ItemFunctions;
import com.naegling.assassins.lib.Notifications;
import com.naegling.assassins.lib.PlayerFunctions;
import com.naegling.assassins.lib.ProfileFunction;
import com.naegling.assassins.lib.Target;
import com.naegling.assassins.lib.UserFunctions;

import java.util.HashMap;

/**
 * @author Johan Nilsson, Henrik Edholm, Mikaela Edström and David M Szabo
 */
public class MainActivity extends ActionBarActivity {
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
    Location currLocation;
    Button assassinate;
    Button buttonCollectItem;
    int distanceInt = 0;
    int oldDeaths = 0;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFunctions = new UserFunctions();
        playerFunctions = new PlayerFunctions();
        distanceInt = 100; // set distance more than 100;

        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        HashMap hm = dbh.getUserDetails();
        uid = (String) hm.get("uid");
        

        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            setContentView(R.layout.activity_main);

            buttonCollectItem = (Button) findViewById(R.id.buttonCollectItem);
            buttonCollectItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
                    intent.putExtra("MODE", "item");
                    intent.putExtra("UID", "" + uid);
                    startActivity(intent);
                }
            });

            assassinate = (Button) findViewById(R.id.assassinate_button);
            assassinate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (locationManager != null)
                        locationManager.removeUpdates(locationListener);
                    String message = "";
                    boolean success = playerFunctions.assassinate(getApplicationContext(), targetClass.uid);
                    if(success) {
                        playerFunctions.updateStatistics(getApplicationContext(), targetClass.uid);
                        message = "You slaughtered " + targetClass.name + "!";
                        //Toast.makeText(getApplicationContext(), "You slaughtered " + targetClass.name, Toast.LENGTH_LONG).show();
                        if (ItemFunctions.loot(uid)) {
                            buttonCollectItem.setVisibility(View.VISIBLE);
                            profileFunc.setItemCollectable(uid, true);
                        }

                    } else {
                        //Toast.makeText(getApplicationContext(), targetClass.name + " escaped!", Toast.LENGTH_LONG).show();
                        message = targetClass.name + " escaped!";
                    }



                    targetClass = null;
                    getTarget();
                    Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                    if (locationManager != null)
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, locationListener);
                }
            });

            try {
                // Loading map
                initilizeMap();
                getOldDeaths();
                if (ItemFunctions.isItemCollectable(uid)) {
                    buttonCollectItem.setVisibility(View.VISIBLE);
                } else {
                    buttonCollectItem.setVisibility(View.GONE);
                }
                
                // this prints out the distance between you and target.
                distance = (TextView) findViewById(R.id.distance_text);
                
                // Acquire a reference to the system Location Manager - device's geographic location
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates / changes 
                locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        // Called when a new location is found by the network location provider.

                    	//AsyncTask locationTask = new LocationTask().execute(location);
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
        assassinate.setClickable(false);
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
        //playerFunctions.setOnlineStatus(getApplicationContext(), "0");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationManager != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, locationListener);
        playerFunctions.setOnlineStatus(getApplicationContext(), "1");
        if (ItemFunctions.isItemCollectable(uid)) {
            buttonCollectItem.setVisibility(View.VISIBLE);
        } else {
            buttonCollectItem.setVisibility(View.GONE);
        }
    }


	/**
     * @autho Johan Nilsson
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

        if (id == R.id.action_friends) {
            Intent intent = new Intent(this, FriendActivity.class);
            startActivity(intent);
            return true;
        }
        
        if (id == R.id.get_target) {        	
        	getTarget();
        	
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @author Mikaela Lidström
     */
    public void getTarget() {
    	// gets the random target and adds it to the map
    	if(targetClass == null) {
        	targetClass = randomTarget.getRandomTarget(getApplicationContext());
            if (targetClass == null)
                Toast.makeText(getApplicationContext(), "No other users online", Toast.LENGTH_SHORT).show();
    	}
        if (targetClass != null) {
            // if there is a marker already, remove it.
            if (targetMarker != null) {
                targetMarker.remove();
            }
            // get the target and place a marker on the map
            targetClass.marker.position(targetClass.getLocation());
            targetMarker = googleMap.addMarker(targetClass.marker);
            assassinate.setText("Assassinate " + targetClass.name);

            //get distance from user location to target location
            Location location = convertLocation(targetMarker.getPosition());
            currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(currLocation != null) {
                distanceInt = (int) currLocation.distanceTo(location);
                distance.setText(Integer.toString(distanceInt) + " m to target");
            } else {
                distanceInt = 100;
                distance.setText("");
            }
        }
    }

    /**
     * @author Mikaela Lidström and Henrik Edholm
     * @param latLong
     * @return
     */
    public Location convertLocation(LatLng latLong) {
    	
    	Location location = new Location("Test");
    	location.setLatitude(latLong.latitude);
    	location.setLongitude(latLong.longitude);
    	
    	return location;
	
    }


    /**
     * @author Mikaela Lidström and Henrik Edholm
     */
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

    /**
     * @author Mikaela Lidström and Henrik Edholm
     */
    public void getOldDeaths() {
    	JSONObject Deaths = profileFunc.getUserDeaths(getApplicationContext());
    	try {
			oldDeaths = Deaths.getInt("deaths");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
