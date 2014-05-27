package com.naegling.assassins;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.FriendFunctions;
import com.naegling.assassins.lib.HttpBitMap;
import com.naegling.assassins.lib.ProfileFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Simonas Stirbys
 */

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
    private ListView friendListDisplay;
    static String selectedFriendName, selectedFriendUniqueId;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2;
	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list_layout);
		
		final Context context = this;
		
        friendListDisplay = (ListView) findViewById(R.id.friend_list_layout);
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        final HashMap<String, String> user = dbh.getUserDetails();
		final FriendFunctions friendFunc = new FriendFunctions();
		final ProfileFunction profileFunc = new ProfileFunction();
		
		
		final ActionBar actionBar = getSupportActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(Tab tab, FragmentTransaction ft) {
	            // show the given tab
	        	if(tab.getText().equals("Friend List")){
	        		
	        		final ArrayList<String> jFriendList = new ArrayList();
	        		final JSONArray jsonFriend = friendFunc.getFriendList(user.get(KEY_UID));
	        		friendFunc.translate(jsonFriend, jFriendList);
	        		arrayAdapter = new ArrayAdapter<String>(
	                        context, 
	                        android.R.layout.simple_spinner_dropdown_item,//.simple_list_item_1,
	                        jFriendList);
	        	
	        		
	        		friendListDisplay.setAdapter(arrayAdapter);
	        		arrayAdapter.setDropDownViewResource(R.id.friend_list_layout);
	        		setContentView(friendListDisplay);
	        		
	        		friendListDisplay.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							selectedFriendName = arrayAdapter.getItem(position);
							setContentView(R.layout.friend_request_layout);
							ImageView profileImage = (ImageView) findViewById(R.id.profile_picture);
							TextView friendName = (TextView) findViewById(R.id.friend_name);
							TextView onlineStatus = (TextView) findViewById(R.id.friend_online_status);							
							TextView friendKills = (TextView) findViewById(R.id.friend_kills);
							TextView friendDeaths = (TextView) findViewById(R.id.friend_deaths);
							Button back = (Button) findViewById(R.id.friend_return);
							
							selectedFriendUniqueId = user.get(KEY_UID);
							friendName.setText(selectedFriendName);
														
							JSONObject getId = friendFunc.translateToUniqueID(selectedFriendName);
							try {
								
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
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
	        			
	        		});
	        		
	        		
	        	}else if(tab.getText().equals("Add Friends")){
	        		setContentView(R.layout.add_friend_layout);
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
										String error = searchUsers.getString("error_msg");
                                        friendResult.setText(error);
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        		    }
	        		});   		
	        		
	        		
	        		
	        	
	        	}else if(tab.getText().equals("Friend Requests")){    
	        		
	        		JSONArray requestList = friendFunc.getFriendRequestList(user.get(KEY_UID));
	        		final ArrayList<String> friendRequestList = new ArrayList();
	        		friendFunc.translate(requestList, friendRequestList);

	        		arrayAdapter2 = new ArrayAdapter<String>(
	                        context, 
	                        android.R.layout.simple_list_item_1,
	                        friendRequestList );
	        		
	        		
	        		friendListDisplay.setAdapter(arrayAdapter2);
	        		friendListDisplay.setOnItemClickListener(new OnItemClickListener() {
	        			@Override
	        			public void onItemClick(AdapterView<?> parent, View view,
	        					int position, long id) {
	        				final String requesterName = friendRequestList.get(position);
	        				AlertDialog friendAccept = new AlertDialog.Builder(context)
	        			    .setTitle("Accept Friend?")
	        			    .setMessage("Do you want to accept "+requesterName+"'s friend request?")
	        			    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
	        			        public void onClick(DialogInterface dialog, int which) { 
	        			        	friendFunc.acceptFriend(user.get(KEY_UID), requesterName);
	    	    	        		arrayAdapter2.remove(requesterName);
	        			        }
	        			     })
	        			    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
	        			        public void onClick(DialogInterface dialog, int which) { 
	        			            friendFunc.denyFriend(user.get(KEY_UID), requesterName);
	    	    	        		arrayAdapter2.remove(requesterName);
	        			        }
	        			     })
	        			     .setNeutralButton("Do Nothing", new DialogInterface.OnClickListener() {
	        			        public void onClick(DialogInterface dialog, int which) { 
	        			        }
	        			     })
	        			    .setIcon(android.R.drawable.ic_dialog_alert)
	        			     .show();	
	        			}
	                	});  
	        		setContentView(friendListDisplay);
	        	}
	        }

	        public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	        }

	        public void onTabReselected(Tab tab, FragmentTransaction ft) {

	        }
	    };

	    
	    actionBar.addTab(actionBar.newTab().setText("Friend List").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Add Friends").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Friend Requests").setTabListener(tabListener));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}

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
	
	
	public void returnButton(View view) {
		setContentView(friendListDisplay);
	}
	
	
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
		        	   setContentView(friendListDisplay);
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();	
	
	}
}
