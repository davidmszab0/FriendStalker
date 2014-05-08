package com.naegling.assassins;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.HttpBitMap;
import com.naegling.assassins.lib.ProfileFunction;
import com.naegling.assassins.lib.UserFunctions;

public class ProfileActivity extends Activity {

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_KILLS = "kills";
	private static String KEY_DEATHS = "deaths";
	private static String KEY_KILL = "bonusKill";
	private static String KEY_SURV = "bonusSurv";
	private static String KEY_PICT = "picture";
	private static String KEY_WNAME = "weaponName";
	private static String KEY_ANAME = "armourName";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		HashMap<String, String> user = db.getUserDetails();

		// TextViews being initialized
		TextView name = (TextView)findViewById(R.id.profileUsername);
		TextView email = (TextView)findViewById(R.id.profileEmail);
		TextView kills = (TextView)findViewById(R.id.profileNumberKills);
		TextView deaths = (TextView)findViewById(R.id.profileNumberDeath);
		TextView kD = (TextView)findViewById(R.id.profileNumberKD);
		TextView bonusKill = (TextView)findViewById(R.id.profileNumberBonusKill);
		TextView bonusSurv = (TextView)findViewById(R.id.profileNumberBonusSurvive);
		ImageView weaponPic = (ImageView)findViewById(R.id.profileEquipmentWeapon);
		ImageView armourPic = (ImageView)findViewById(R.id.profileEquipmentArmour);
		ImageView profilePic = (ImageView)findViewById(R.id.profilePicture);

		// Initializes variables
		String jKills = "";
		String jDeaths = "";
		int jBonusKill = 0;
		int jBonusSurv = 0;

		// Setting the user name and email to the interface
		name.setText(user.get(KEY_NAME));
		email.setText(user.get(KEY_EMAIL));

		// Making new profile function
		ProfileFunction pFunc = new ProfileFunction();
		
		// Get profile picture
		JSONObject jsonPicture = pFunc.getUserPicture(user.get(KEY_UID));
		
		try {
			AsyncTask bmPict = new HttpBitMap().execute(jsonPicture.getString(KEY_PICT));
			try {
				Bitmap bitPict = (Bitmap) bmPict.get();

				profilePic.setImageBitmap(bitPict);
			}catch(InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		

		// Get the users kills and deaths from the database
		JSONObject jsonKills = pFunc.getUserKills(user.get(KEY_UID));
		JSONObject jsonDeaths = pFunc.getUserDeaths(user.get(KEY_UID));

		try {
			jKills = jsonKills.getString(KEY_KILLS);
			jDeaths = jsonDeaths.getString(KEY_DEATHS);
		}
		catch(JSONException e) {
			e.printStackTrace();
		}

		kills.setText(jKills);       
		deaths.setText(jDeaths); 

		double kDRatio = Double.parseDouble(jKills) / Double.parseDouble(jDeaths);
		kD.setText(Double.toString(kDRatio));

		// Get the weapon/armour from the database
		JSONObject jsonWeapon = pFunc.getUserWeapon(user.get(KEY_UID));
		JSONObject jsonArmour = pFunc.getUserArmour(user.get(KEY_UID));       

		// Get the specific bonuses from JSON data
		try {
			jBonusKill = jsonWeapon.getInt(KEY_KILL) + jsonArmour.getInt(KEY_KILL);
			jBonusSurv = jsonWeapon.getInt(KEY_SURV) + jsonArmour.getInt(KEY_SURV);
		}
		catch(JSONException e) {
			e.printStackTrace();
		}

		// Adding the bonuses to the interface
		bonusKill.setText("+" + Integer.toString(jBonusKill) + "%");
		bonusSurv.setText("+" + Integer.toString(jBonusSurv) + "%");

		try {
			AsyncTask bmWeap = new HttpBitMap().execute(jsonWeapon.getString(KEY_PICT));
			AsyncTask bmArm = new HttpBitMap().execute(jsonArmour.getString(KEY_PICT));
			try {
				Bitmap bitWeap = (Bitmap) bmWeap.get();
				Bitmap bitArm = (Bitmap) bmArm.get();

				weaponPic.setImageBitmap(bitWeap);
				armourPic.setImageBitmap(bitArm);
			}catch(InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

		// Close all views before launching Dashboard
		mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mainActivity);
		finish();
		return;
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
        UserFunctions userFunctions = new UserFunctions();

        if (id == R.id.action_settings) {
            return true;
        }
        
        if (id == R.id.action_profile) {
        	Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
            profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profile);
            finish();
            return true;
        }

        if (id == R.id.action_logout) {
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
}
