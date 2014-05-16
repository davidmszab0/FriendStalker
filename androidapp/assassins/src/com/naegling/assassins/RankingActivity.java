package com.naegling.assassins;

import org.json.JSONArray;
import org.json.JSONException;

import com.naegling.assassins.lib.PlayerFunctions;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
		
		TextView textArray[] = new TextView[5];
		textArray[0] = (TextView)findViewById(R.id.allRankText1);
		textArray[1] = (TextView)findViewById(R.id.allRankText2);
		textArray[2] = (TextView)findViewById(R.id.allRankText3);
		textArray[3] = (TextView)findViewById(R.id.allRankText4);
		textArray[4] = (TextView)findViewById(R.id.allRankText5);
		
		try {
			for(int i = 0; i < ranking.length(); i++) {
			textArray[i].setText(ranking.getJSONObject(i).getString("name") + "\t\t" + ranking.getJSONObject(i).getDouble("killDeath"));
			
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
	
	
}
