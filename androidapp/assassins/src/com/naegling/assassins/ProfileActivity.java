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
	
	String uid;
	
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
		TextView weaponName = (TextView)findViewById(R.id.profileWeaponText);
		TextView armourName = (TextView)findViewById(R.id.profileArmourText);
		TextView wK = (TextView)findViewById(R.id.wBonusKill);
		TextView wS = (TextView)findViewById(R.id.wBonusSurv);
		TextView aK = (TextView)findViewById(R.id.aBonusKill);
		TextView aS = (TextView)findViewById(R.id.aBonusSurv);

		// Initializes variables
		String jKills = "";
		String jDeaths = "";
		

		// Setting the user name and email to the interface
		name.setText(user.get(KEY_NAME));
		email.setText(user.get(KEY_EMAIL));
		uid = user.get(KEY_UID);
		
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
		
		// set the text to the textviews
		kills.setText(jKills);       
		deaths.setText(jDeaths); 
		
		// calculate the k/d ratio and set it in the textview
		double kDRatio = Double.parseDouble(jKills) / Double.parseDouble(jDeaths);
		kD.setText(Double.toString(kDRatio));
		
		int jBonusKill = 0;
		int jBonusSurv = 0;
		int jABonusKill = 0;
		int jABonusSurv = 0;
		
		String wName = "";
		String aName = "";

		// Get the weapon/armour from the database
		JSONObject jsonWeapon = pFunc.getUserWeapon(user.get(KEY_UID));
		JSONObject jsonArmour = pFunc.getUserArmour(user.get(KEY_UID));
		
		

		// Get the specific bonuses from JSON data
		try {
			jBonusKill = jsonWeapon.getInt(KEY_KILL);
			jABonusKill = jsonArmour.getInt(KEY_KILL);
			jABonusSurv = jsonArmour.getInt(KEY_SURV);
			jBonusSurv = jsonWeapon.getInt(KEY_SURV); 
			wName = jsonWeapon.getString(KEY_WNAME);
			aName = jsonArmour.getString(KEY_ANAME);
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		
		weaponName.setText(wName);
		armourName.setText(aName);
		// Adding the bonuses from the items to the interface
		bonusKill.setText("+" + Integer.toString(jBonusKill + jABonusKill) + "%");
		bonusSurv.setText("+" + Integer.toString(jBonusSurv + jABonusSurv) + "%");
		
		wK.setText("Kill Bonus: " + Integer.toString(jBonusKill));
		aK.setText("Kill Bonus: " + Integer.toString(jABonusKill));
		wS.setText("Defence Bonus: " + Integer.toString(jBonusSurv));
		aS.setText("Defence Bonus: " + Integer.toString(jABonusSurv));
		
		// Get the items pictures and set them to the imageviews
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
	
	// when pressing the back button go back to the main activity
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
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        UserFunctions userFunctions = new UserFunctions();

     // Take a picture with the pi
        if (id == R.id.take_picture) {
            Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
            intent.putExtra("MODE", "pic");
            intent.putExtra("UID", "" + uid);
            startActivity(intent);

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
