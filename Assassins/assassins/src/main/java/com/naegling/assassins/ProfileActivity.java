package com.naegling.assassins;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.HttpBitMap;
import com.naegling.assassins.lib.ProfileFunction;
import com.naegling.assassins.lib.UserFunctions;

/**
 * @author Mikaela Lidström and Henrik Edholm
 */

public class ProfileActivity extends Activity {

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_KILLS = "noKills";
	private static String KEY_DEATHS = "noDeaths";
    private static String KEY_PICT = "picture";
	private static String KEY_WKILL = "weaponBonusKill";
	private static String KEY_WSURV = "weaponBonusSurv";
	private static String KEY_WPICT = "weaponPicture";
	private static String KEY_WNAME = "weaponName";
	private static String KEY_ANAME = "armourName";
    private static String KEY_AKILL = "armourBonusKill";
    private static String KEY_ASURV = "armourBonusSurv";
    private static String KEY_APICT = "armourPic";
	
	String uid;
    String email;

    TextView nameTextView;
    TextView emailTextView;
    TextView killsTextView;
    TextView deathsTextView;
    TextView kDTextView;
    TextView bonusKillTextView;
    TextView bonusSurvTextView;
    ImageView weaponPic;
    ImageView armourPic;
    ImageView profilePic;
    TextView weaponNameTextView;
    TextView armourNameTextView;
    TextView wKTextView;
    TextView wSTextView;
    TextView aKTextView;
    TextView aSTextView;
    HashMap<String, String> user;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		user = db.getUserDetails();

		// TextViews being initialized
		nameTextView = (TextView)findViewById(R.id.profileUsername);
		emailTextView = (TextView)findViewById(R.id.profileEmail);
		killsTextView = (TextView)findViewById(R.id.profileNumberKills);
		deathsTextView = (TextView)findViewById(R.id.profileNumberDeath);
		kDTextView = (TextView)findViewById(R.id.profileNumberKD);
		bonusKillTextView = (TextView)findViewById(R.id.profileNumberBonusKill);
		bonusSurvTextView = (TextView)findViewById(R.id.profileNumberBonusSurvive);
		weaponPic = (ImageView)findViewById(R.id.profileEquipmentWeapon);
		armourPic = (ImageView)findViewById(R.id.profileEquipmentArmour);
		profilePic = (ImageView)findViewById(R.id.profilePicture);
		weaponNameTextView = (TextView)findViewById(R.id.profileWeaponText);
		armourNameTextView = (TextView)findViewById(R.id.profileArmourText);
		wKTextView = (TextView)findViewById(R.id.wBonusKill);
		wSTextView = (TextView)findViewById(R.id.wBonusSurv);
		aKTextView = (TextView)findViewById(R.id.aBonusKill);
		aSTextView = (TextView)findViewById(R.id.aBonusSurv);


	}

    @Override
    public void onResume() {
        super.onResume();
        // Making new profile function
        ProfileFunction pFunc = new ProfileFunction();


        // Initializes variables
        int kills = 0;
        int deaths = 0;
        Double kdRatio;
        int wBonusKill = 0;
        int wBonusSurv = 0;
        int aBonusKill = 0;
        int aBonusSurv = 0;
        String wName = "";
        String aName = "";

        // Get data from the server through API
        JSONObject jsonProfile = pFunc.getProfile(user.get(KEY_UID));

        // Setting the user nameTextView and email to the interface
        nameTextView.setText(user.get(KEY_NAME));
        emailTextView.setText(user.get(KEY_EMAIL));
        uid = user.get(KEY_UID);
        email = user.get(KEY_EMAIL);

        // Get picture for profile, weapon and armour and set ImageViews

        try {
            URL[] urls = {new URL(jsonProfile.getString(KEY_PICT)),new URL(jsonProfile.getString(KEY_WPICT)),new URL(jsonProfile.getString(KEY_APICT))};
            AsyncTask bmTask = new HttpBitMap().execute(urls[0], urls[1], urls[2]);
            //AsyncTask bmTaskWPic = new HttpBitMap().execute();
            //AsyncTask bmTaskAPic = new HttpBitMap().execute();
            try {
                Bitmap[] bitMap = (Bitmap[])bmTask.get();
                //Bitmap bitMapWPic = (Bitmap)bmTaskWPic.get();
                //Bitmap bitMapAPic = (Bitmap)bmTaskAPic.get();
                profilePic.setImageBitmap(bitMap[0]);
                weaponPic.setImageBitmap(bitMap[1]);
                armourPic.setImageBitmap(bitMap[2]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // Get kills, deaths from server
            kills = jsonProfile.getInt(KEY_KILLS);
            deaths = jsonProfile.getInt(KEY_DEATHS);
            wName = jsonProfile.getString(KEY_WNAME);
            wBonusKill = jsonProfile.getInt(KEY_WKILL);
            wBonusSurv = jsonProfile.getInt(KEY_WSURV);
            aName = jsonProfile.getString(KEY_ANAME);
            aBonusKill = jsonProfile.getInt(KEY_AKILL);
            aBonusSurv = jsonProfile.getInt(KEY_ASURV);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // set the text to the textviews
        killsTextView.setText("" + kills);
        deathsTextView.setText("" + deaths);
        weaponNameTextView.setText(wName);
        armourNameTextView.setText(aName);

        // calculate the k/d ratio and set it in the textview
        if (deaths > 0)
            kdRatio = 0.0 + kills / deaths;
        else
            kdRatio = 0.0;
        kDTextView.setText(Double.toString(kdRatio));



        // Adding the bonuses from the items to the interface
        bonusKillTextView.setText("+" + Integer.toString(wBonusKill + aBonusKill) + "%");
        bonusSurvTextView.setText("+" + Integer.toString(wBonusSurv + aBonusSurv) + "%");

        wKTextView.setText("Kill Bonus: " + Integer.toString(wBonusKill));
        aKTextView.setText("Kill Bonus: " + Integer.toString(aBonusKill));
        wSTextView.setText("Defence Bonus: " + Integer.toString(wBonusSurv));
        aSTextView.setText("Defence Bonus: " + Integer.toString(aBonusSurv));
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
            Log.e("Strings: ", uid);
            Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
            intent.putExtra("MODE", "pic");
            intent.putExtra("UID", "" + uid);
            startActivity(intent);

            return true;
        }
        
        if (id == R.id.action_ranking) {
            Intent ranking = new Intent(getApplicationContext(), RankingActivity.class);
            ranking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ranking);
            finish();
            return true;
        }

        if (id == R.id.change_password) {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            intent.putExtra("UID", uid);
            intent.putExtra("EMAIL", email);
            startActivity(intent);
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
