package org.antennae.android.notifyapp;

import org.antennea.android.notifyapp.model.Alert;
import org.antennea.android.notifyapp.model.AlertSeverityEnum;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import org.antennae.android.notifyapp.R;

public class NotificationsReceiver extends BroadcastReceiver {

	private static final String TAG = NotificationsReceiver.class.getSimpleName();
	public static final String ACTION_NOTIFICATION_VALUE = "org.antennae.notify.notifications.PATCH_NOTIFICATION";

	public NotificationsReceiver() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			Log.d(TAG, "Receiver intent null");
		} else {
			String action = intent.getAction();
			Log.d(TAG, "got action " + action);
			
			try {
				String parseData = intent.getExtras().getString("com.parse.Data");
				
				JSONObject data = new JSONObject(parseData);
				Log.d(TAG, "Received: " + data.toString());
				
				Alert alert = Alert.fromJson(data.getString("notification"));
//			if (action.equals(ACTION_NOTIFICATION_VALUE)) {
					showNotification(context, alert);
//			}
			} catch (JSONException e) {
				Toast.makeText(context, "Could not read data from push", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	public static void showNotification(Context context, Alert alert) {
		final Intent notificationIntent = new Intent(context, MainActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		Builder builder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.setContentTitle(alert.getTitle())
				.setContentText(alert.getMessage())
				.setContentIntent(pendingIntent);
		
		if(AlertSeverityEnum.shouldCall(alert)) {
			builder = builder.addAction(android.R.drawable.ic_menu_call, "Call", pendingIntent);
		}
			
		
		final Notification notification = builder
//		        .addAction(R.drawable.ic_launcher, "More", pendingIntent)
				.build();

		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, notification);
		
		updateMyActivity(context, alert);
	}
	
	static void updateMyActivity(Context context, Alert alert) {
	    Intent intent = new Intent(MainActivity.ALERT_NOTIFICATION_INTENT_FILTER);

	    //put whatever data you want to send, if any
	    intent.putExtra(MainActivity.ALERT_PARAM, alert);

	    //send broadcast
	    context.sendBroadcast(intent);
	}
}
