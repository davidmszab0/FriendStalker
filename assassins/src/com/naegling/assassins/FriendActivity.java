package com.naegling.assassins;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.FriendFunctions;
import com.naegling.assassins.lib.ProfileFunction;
import com.naegling.assassins.lib.HttpBitMap;

public class FriendActivity extends ActionBarActivity {

	 
	protected static final int INVISIBLE = 0;
	protected static final int VISIBLE = 0;
	protected static final int GONE = 0;
	private static String KEY_SUCCESS1 = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_FRIEND = "friend";
    private static String KEY_FRIEND_ID = "friendUID";
    private static String KEY_FRIEND_KILLS = "kills";
    private static String KEY_FRIEND_DEATHS = "deaths";
    private static String KEY_ONLINE_STATUS = "onlineStatus";
    private static String KEY_PROFILE_PIC = "picture";
    private ListView listDisplay;
    static String selectedFriendName, selectedFriendUniqueId;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2;
	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list_display);
		
		final Context context = this;
		
        listDisplay = (ListView) findViewById(R.id.list_layout);
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        final HashMap<String, String> user = dbh.getUserDetails();
		final FriendFunctions friendFunc = new FriendFunctions();
		final ProfileFunction profileFunc = new ProfileFunction();
		
		
		//Here we create empty array lists and fetch all of user's friends and friend requests
		//in jsonArray form. The translate method "translates" json array data and fills the empty 
		//array lists. Array adapters use the array lists to display data in ListView 
		final ArrayList<String> friendList = new ArrayList();
		final JSONArray jsonFriendList = friendFunc.getFriendList(user.get(KEY_UID));
		friendFunc.translate(jsonFriendList, friendList);
		arrayAdapter = new ArrayAdapter<String>(
                context, 
                android.R.layout.simple_list_item_1,
                friendList);
		
		final ArrayList<String> requestList = new ArrayList();
		JSONArray jsonRequestList = friendFunc.getFriendRequestList(user.get(KEY_UID));
		friendFunc.translate(jsonRequestList, requestList);
		arrayAdapter2 = new ArrayAdapter<String>(
                context, 
                android.R.layout.simple_list_item_1,
                requestList );
		
		
		
		//we initialize tabs
		final ActionBar actionBar = getSupportActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            
	        	//the following if statement handles everything that 
	        	//happens in the friend list part of the Friends menu. 
	        	if(tab.getText().equals("Friend List")){   		
	        		listDisplay.setAdapter(arrayAdapter);
	        		//arrayAdapter.setDropDownViewResource(R.id.list_layout);
	        		setContentView(listDisplay);
	        		
	        		listDisplay.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							
							//Here we initialize all the text views that we will use
							//to display user's friend information
							selectedFriendName = arrayAdapter.getItem(position);
							setContentView(R.layout.layout_friend_profile);
							ImageView profileImage = (ImageView) findViewById(R.id.profile_picture);
							TextView friendName = (TextView) findViewById(R.id.friend_name);
							TextView onlineStatus = (TextView) findViewById(R.id.friend_online_status);							
							TextView friendKills = (TextView) findViewById(R.id.friend_kills);
							TextView friendDeaths = (TextView) findViewById(R.id.friend_deaths);
							friendName.setText(selectedFriendName);
							
							
							selectedFriendUniqueId = user.get(KEY_UID);														
							JSONObject getId = friendFunc.translateToUniqueID(selectedFriendName);
							try {
								
								//Here we get appropriate information using friend functions and 
								//adjust text views to show the obtained values
								String friendId = getId.getString(KEY_FRIEND_ID);
								JSONObject getPicture = profileFunc.getUserPicture(friendId);
								URL[] url = {new URL(getPicture.getString(KEY_PROFILE_PIC))};
								AsyncTask bmTask = new HttpBitMap().execute(url[0]);
								Bitmap[] bitmap = (Bitmap[])bmTask.get();
								profileImage.setImageBitmap(bitmap[0]);
								
								JSONObject getFriendKills = profileFunc.getUserKills(friendId);
								friendKills.setText("Number of kills: "+getFriendKills.getString(KEY_FRIEND_KILLS));
								
								JSONObject getFriendDeaths = profileFunc.getUserDeaths(friendId);
								friendDeaths.setText("Number of deaths: "+getFriendDeaths.getString(KEY_FRIEND_DEATHS));
								
								JSONObject getOnlineStatus = friendFunc.getOnlineStatus(friendId);
								onlineStatus.setText(getOnlineStatus.getString(KEY_ONLINE_STATUS));
								
							} catch (JSONException | MalformedURLException | InterruptedException | ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
	        			
	        		});
	        		
	        		
        		//the following if statement handles sending friend requests	
	        	}else if(tab.getText().equals("Add Friends")){
	        		setContentView(R.layout.layout_add_friend);
	        		final EditText friendName = (EditText) findViewById(R.id.add_friend_text);
	        		final TextView friendResult = (TextView) findViewById(R.id.add_friend_result);
	        		friendName.setHint("Enter a Name");
    		    	friendResult.setText("");

	        		Button send = (Button) findViewById(R.id.button_send_friend);
	        		send.setOnClickListener(new View.OnClickListener() {
	        		    @Override
	        		    public void onClick(View v) {	        		    	
	        		    	String name = friendName.getText().toString();
	        		    	
	        		    	friendName.setText("");
	        		    	FriendFunctions friendFunc = new FriendFunctions();
	        		    	JSONObject searchUsers = friendFunc.searchForFriend(name);
	        		    	try {
								if(searchUsers.getString(KEY_SUCCESS1)!=null){
									String success = searchUsers.getString(KEY_SUCCESS1);
									System.out.println("AND THIS "+ success);
									if(Integer.parseInt(success)==1){
										friendFunc.sendFriendRequest(user.get(KEY_UID),name);
										friendResult.setText("Request sent!");
									}else{
										friendResult.setText("No such user!");										
									}
								}
							} catch (NumberFormatException | JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        		    }
	        		});   		
	        		
	        	
	        	//The following if statement handles Friend Requests tab
	        	}else if(tab.getText().equals("Friend Requests")){  		
	        		listDisplay.setAdapter(arrayAdapter2);
	        		listDisplay.setOnItemClickListener(new OnItemClickListener() {
	        			@Override
	        			public void onItemClick(AdapterView<?> parent, View view,
	        					int position, long id) {
	        				final String requesterName = requestList.get(position);
	        				
	        				//this is where alert dialog starts
	        				AlertDialog friendAccept = new AlertDialog.Builder(context)
	        			    .setTitle("Accept Friend?")
	        			    .setMessage("Do you want to accept "+requesterName+"'s friend request?")
	        			    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
	        			        public void onClick(DialogInterface dialog, int which) { 
	        			        	friendFunc.acceptFriend(user.get(KEY_UID), requesterName);
	    	    	        		arrayAdapter2.remove(requesterName);
	    	    	        		listDisplay.setAdapter(arrayAdapter2);
	        			        }
	        			     })
	        			    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
	        			        public void onClick(DialogInterface dialog, int which) { 
	        			            friendFunc.denyFriend(user.get(KEY_UID), requesterName);
	    	    	        		arrayAdapter2.remove(requesterName);
	    	    	        		listDisplay.setAdapter(arrayAdapter2);
	        			        }
	        			     })
	        			     .setNeutralButton("Do Nothing", new DialogInterface.OnClickListener() {
	        			        public void onClick(DialogInterface dialog, int which) { 
	        			        }
	        			     })
	        			    .setIcon(android.R.drawable.ic_dialog_alert)
	        			     .show();	
	        				//Allert dialog code ends here
	        				
	        			}
	                	});  
	        		setContentView(listDisplay);
	        	}
	        }
	        
	        
	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        }
	    };

	    
	    //Here we just add tabs to the actionbar so they are displayed 
	    //in the Friend menu section
	    actionBar.addTab(actionBar.newTab().setText("Friend List").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Add Friends").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Friend Requests").setTabListener(tabListener));

	}

	
	//Automatically implemented code
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}

	
	//Automatically implemented code
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	//this method was created automatically by eclipse when the class was built.
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu,
					container, false);
			return rootView;
		}
	}
	
	
	//This method just changes the layout back to friend list
	//when return button is clicked on the friend's profile
	public void returnButton(View view) {
		setContentView(listDisplay);
	}
	
	
	// This method is activated when you delete a friend in friend profile.
	// Basically it prompts a dialog, and depending on the user's reply,
	// either does nothing or deletes a friend and sets layout to friend list
	public void deleteButton(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete "+selectedFriendName+" from your friend list?")
		       .setCancelable(false)
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int which) {
		    		   
		    	   }
		       })
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   FriendFunctions remove = new FriendFunctions();
		        	   remove.removeFriend(selectedFriendUniqueId, selectedFriendName);
		        	   arrayAdapter.remove(selectedFriendName);
		        	   listDisplay.setAdapter(arrayAdapter);
		        	   setContentView(listDisplay);
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();	
	
	}
}
