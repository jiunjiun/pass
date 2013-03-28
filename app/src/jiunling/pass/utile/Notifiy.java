package jiunling.pass.utile;


import jiunling.pass.R;
import jiunling.pass.view.WifiDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class Notifiy {
	
	/***	Debugging	***/
//	private static final String TAG = "Notificatio";
//	private static final boolean D = true;
	
	public static final int	NotifyUser	= 1;
		
	public static void NotifyMsg(Context mContext, int Kind, String Message) {
		switch(Kind) {
		case NotifyUser:
			NotifyUser(mContext, Kind, Message);
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void NotifyUser(Context mContext, int Kind, String WifiList) {
		int Count = 0;
		try {
			Count = new JSONArray(new JSONObject(WifiList).getString("Wifi").toString()).length();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String tickerText = mContext.getResources().getString(R.string.wifi_Dialog_title)+ ": "+ Count;
		String Title = mContext.getResources().getString(R.string.app_name);
		String context = tickerText;
		
		NotificationManager mNotificationManager = 
				(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notifyIntent = new Intent(mContext, WifiDialog.class);
		notifyIntent.putExtra("WifiList", WifiList);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent appIntent 	= PendingIntent.getActivity(mContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification mNotification 	= new Notification();
		mNotification.icon 			= R.drawable.ic_launcher;
		mNotification.tickerText 	= tickerText;
		mNotification.defaults		= Notification.DEFAULT_ALL;
		mNotification.flags 	   |= Notification.FLAG_AUTO_CANCEL;
		mNotification.setLatestEventInfo(mContext, Title, context, appIntent);
		mNotificationManager.notify(Kind, mNotification);
	}
	
	public static void NotifyAllCancel(Context mContext) {
		NotificationManager mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NotifyUser);
	}
	
	public static void NotifyCancel(Context mContext, int Kind) {
		NotificationManager mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(Kind);
	}
}
