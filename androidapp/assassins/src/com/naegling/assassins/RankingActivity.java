package com.naegling.assassins;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class RankingActivity extends Activity {
	private ViewSwitcher switcher;
	private static final int REFRESH_SCREEN = 1;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);

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
