package com.naegling.assassins;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naegling.assassins.lib.FriendMarker;
import com.naegling.assassins.lib.PlayerFunctions;
import com.naegling.assassins.lib.UserFunctions;


public class MainActivity extends ActionBarActivity {
    UserFunctions userFunctions;
    PlayerFunctions playerFunctions;
    private GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener;
    FriendMarker friendMarker = new FriendMarker();

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
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
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
            // Closing dashboard screen
            finish();
            return true;
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

