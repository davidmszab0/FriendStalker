package com.naegling.assassins.lib;

import com.naegling.assassins.MainActivity;
import com.naegling.assassins.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Mikaela Lidström and Henrik Edholm
 */

public class Notifications {
	int notId = 1;
	// Add app running notification  

	private void addNotification(Context context) {
		

		
		NotificationCompat.Builder builder =  
				new NotificationCompat.Builder(context)  
		.setSmallIcon(R.drawable.ic_launcher)  
		.setContentTitle("Notifications Example")  
		.setContentText("This is a test notification")
		.setWhen(System.currentTimeMillis()); 
		
		builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
		builder.setLights(Color.RED, 3000, 3000);
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		builder.setSound(uri);

		Intent notificationIntent = new Intent(context, MainActivity.class);  
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,   
				PendingIntent.FLAG_UPDATE_CURRENT);  
		builder.setContentIntent(contentIntent);  
		
		
		// Add as notification  
		NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);  
		manager.notify(notId, builder.build());  
	}  

	// Remove notification  
	private void removeNotification(Context context) {  
		NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);  
		manager.cancel(notId);  
	}


	public void youGotKilled(Context context, String name) {
		NotificationCompat.Builder builder =  
				new NotificationCompat.Builder(context)  
		.setSmallIcon(R.drawable.ic_launcher)  
		.setContentTitle("You got Killed!")  
		.setContentText("You got killed by " +name)
		.setAutoCancel(true)
		.setWhen(System.currentTimeMillis()); 
		
		builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
		builder.setLights(Color.RED, 3000, 3000);
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		builder.setSound(uri);

		Intent notificationIntent = new Intent(context, MainActivity.class);  
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,   
				PendingIntent.FLAG_UPDATE_CURRENT);  
		builder.setContentIntent(contentIntent);  
		
		
		// Add as notification  
		NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);  
		manager.notify(notId, builder.build());

	}

	public void itemToCollect(Context context) {
		NotificationCompat.Builder builder =  
				new NotificationCompat.Builder(context)  
		.setSmallIcon(R.drawable.ic_launcher)  
		.setContentTitle("You got an Item!")  
		.setContentText("You have an item to collect at the hub!")
		.setAutoCancel(true)
		.setWhen(System.currentTimeMillis()); 
		
		builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
		builder.setLights(Color.RED, 3000, 3000);
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		builder.setSound(uri);

		Intent notificationIntent = new Intent(context, MainActivity.class);  
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,   
				PendingIntent.FLAG_UPDATE_CURRENT);  
		builder.setContentIntent(contentIntent);  
		
		
		// Add as notification  
		NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);  
		manager.notify(notId, builder.build());
	}
}
