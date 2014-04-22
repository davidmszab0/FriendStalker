package com.example.maffia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity  {
	
	protected Account account;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); 
        
/*        Button viewMap = (Button) this.findViewById(R.id.button2);
        viewMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				String geoURIString = "geo:0,0?g=1600+Pennsylvania+Avenue+NW+Washington,+DC+20500";
//				Uri geoURI = Uri.parse(geoURIString);
				Intent intent = new Intent();
				setContentView(R.layout.activity_main);
				startActivity(intent);
			}
		});
    }*/
    }
	
	public void login(View view) {
		account = null;
		Intent intent = new Intent(this, ViewMap.class);
		startActivity(intent);
	}
}
