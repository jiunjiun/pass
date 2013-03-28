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
	
	public static final int Second 						= 1000;
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

	
	/**		getPreferences Key		**/
	public final static int Kind_WIFI_AUTO_SCAN				= 1;
	public final static int Kind_WIFI_NOTIFICATION_USER		= 2;
	public final static int Kind_WIFI_UPDATE_INTERVAL		= 3;
	
	public String Key_WIFI_AUTO_SCAN					= "";
	public String Key_WIFI_NOTIFICATION_USER			= "";
	public String Key_WIFI_UPDATE_INTERVAL			= "";

	private Context mContext;
	
	public Option(Context mContext) {
		this.mContext = mContext;
		Key_WIFI_AUTO_SCAN			= mContext.getResources().getString(R.string.wifi_auto_scan_key);
		Key_WIFI_NOTIFICATION_USER	= mContext.getResources().getString(R.string.wifi_notification_user_key);
		Key_WIFI_UPDATE_INTERVAL	= mContext.getResources().getString(R.string.wifi_update_interval_key);
	}
	
	public Object getPreferences(int Kind) {
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mContext);
		switch(Kind) {
		case Kind_WIFI_AUTO_SCAN:
			return spref.getBoolean(Key_WIFI_AUTO_SCAN, WifiScan);
			
		case Kind_WIFI_NOTIFICATION_USER:
			return spref.getBoolean(Key_WIFI_NOTIFICATION_USER, NotificationUser);
			
		case Kind_WIFI_UPDATE_INTERVAL:
			return Integer.parseInt(spref.getString(Key_WIFI_UPDATE_INTERVAL, UpdateTime+""));
			
		default:
			return null;
		}
	}
}
