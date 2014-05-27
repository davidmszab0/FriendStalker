package com.naegling.assassins;

import org.json.JSONArray;
import org.json.JSONException;

import com.naegling.assassins.lib.PlayerFunctions;
import com.naegling.assassins.lib.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * @author Mikaela Lidström and Henrik Edholm
 */
public class RankingActivity extends Activity {
	private ViewSwitcher switcher;
	private static final int REFRESH_SCREEN = 1;
	private PlayerFunctions playerFunctions;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		playerFunctions = new PlayerFunctions();
		JSONArray ranking = playerFunctions.getAllRanking();
		JSONArray friendRanking = playerFunctions.getFriendRanking(getApplicationContext());

		TextView textArray[] = new TextView[5];
		TextView numArray[] = new TextView[5];
		textArray[0] = (TextView)findViewById(R.id.allRankText1);
		textArray[1] = (TextView)findViewById(R.id.allRankText2);
		textArray[2] = (TextView)findViewById(R.id.allRankText3);
		textArray[3] = (TextView)findViewById(R.id.allRankText4);
		textArray[4] = (TextView)findViewById(R.id.allRankText5);
		numArray[0] = (TextView)findViewById(R.id.allRankNum1);
		numArray[1] = (TextView)findViewById(R.id.allRankNum2);
		numArray[2] = (TextView)findViewById(R.id.allRankNum3);
		numArray[3] = (TextView)findViewById(R.id.allRankNum4);
		numArray[4] = (TextView)findViewById(R.id.allRankNum5);
		

		try {
			for(int i = 0; i < ranking.length(); i++) {
				textArray[i].setText(ranking.getJSONObject(i).getString("name"));
				numArray[i].setText(Double.toString(ranking.getJSONObject(i).getDouble("killDeath")));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView textFriendArray[] = new TextView[3];
		TextView numFriendArray[] = new TextView[3];
		textFriendArray[0] = (TextView)findViewById(R.id.friendRankText1);
		textFriendArray[1] = (TextView)findViewById(R.id.friendRankText2);
		textFriendArray[2] = (TextView)findViewById(R.id.friendRankText3);
		numFriendArray[0] = (TextView)findViewById(R.id.friendRankNum1);
		numFriendArray[1] = (TextView)findViewById(R.id.friendRankNum2);
		numFriendArray[2] = (TextView)findViewById(R.id.friendRankNum3);
		
		try {
			for(int i = 0; i < friendRanking.length(); i++) {
				textFriendArray[i].setText(friendRanking.getJSONObject(i).getString("name"));
				numFriendArray[i].setText(Double.toString(friendRanking.getJSONObject(i).getDouble("killDeath")));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		switcher = (ViewSwitcher) findViewById(R.id.rankingSwitcher);



		Button friendRankButton = (Button) findViewById(R.id.friendRankButton);
		friendRankButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switcher.showNext();  // Switches to the next view

			}
		});

		Button allRankButton = (Button) findViewById(R.id.allRankButton);
		allRankButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switcher.showPrevious();  // Switches to the previous view

			}
		});
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

		if (id == R.id.action_profile) {
			Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
			profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(profile);
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
