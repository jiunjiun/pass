package jiunling.pass.config;

import jiunling.pass.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Option {
	/***	root 	***/
    public static boolean havaRoot 						= false;
    
	/***	wifi ¥\²v ¶V¤p¶V®t	***/
	public static final int Wifi_Level 					= -70;
	
	/**		Wifi setting	**/
	public static boolean WifiScan						= true;
	public static boolean NotificationUser				= false;
	
	private static final int Second 					= 1000;
	public static int UpdateTime 						= 30;
	public static int SleepTime							= UpdateTime * Second;
	
	
	/**				hide			**/
	/**		Wifi public(hide get)	**/
	public static int pubUpdateTime 					= 60;							/** 	1 mins	**/
	public static int pubSleepTime						= pubUpdateTime * Second;
	

	public static int SendPubUpdateTime 				= 1440;							/** 	1 hour	**/
	public static int SendPubSleepTime					= SendPubUpdateTime * Second;
	
	
	public static int RemoveTime 						= 60;							/** 	1 mins	**/
	public static int RemoveUpdateTime					= RemoveTime * Second;
	public static int RssiLimit 						= -90;

	
	public Option(Context mContext) {
		String wifi_auto_scan_key, wifi_notification_user_key, wifi_update_interval_key;
		
		wifi_auto_scan_key			= mContext.getResources().getString(R.string.wifi_auto_scan_key);
		wifi_notification_user_key	= mContext.getResources().getString(R.string.wifi_notification_user_key);
		wifi_update_interval_key	= mContext.getResources().getString(R.string.wifi_update_interval_key);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		WifiScan 			= pref.getBoolean(wifi_auto_scan_key, true);
		NotificationUser 	= pref.getBoolean(wifi_notification_user_key, false);
		UpdateTime 			= Integer.parseInt(pref.getString(wifi_update_interval_key, "30"));
	}
}
