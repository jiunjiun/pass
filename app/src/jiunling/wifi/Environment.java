package jiunling.wifi;

import android.content.Context;

public class Environment {
	
	/***	Debugging	***/
//	private static final String TAG = "Environment";
//	private static final boolean D = true;
	
	private Context mContext;
		
	/*** 	wifi	***/
	private WifiHelper mWifiHelper = null;
	
	/***	MESSAGE format	***/
	public static final int MESSAGE_REQUEST = 1;
	public static final int MESSAGE_ERROR_EQUEST = 2;
	
	
	public Environment( Context mContext ) {
		this.mContext = mContext;
	}
	
	private void UpdateWifiHelper() {
		mWifiHelper = new WifiHelper(mContext);
	}
		
	public void CheckHaveSpecifiedWifi() {
		
		UpdateWifiHelper();
		
	}
	
}
