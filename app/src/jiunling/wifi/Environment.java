package jiunling.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

public class Environment {
	
	/***	Debugging	***/
	private static final String TAG = "Environment";
	private static final boolean D = true;
	
	private Context mContext;
		
	/*** 	wifi	***/
	private WifiHelper mWifiHelper = null;
	
	/***	MESSAGE format	***/
	public static final int MESSAGE_REQUEST = 1;
	public static final int MESSAGE_ERROR_EQUEST = 2;
	
	
	public Environment( Context mContext ) {
		this.mContext = mContext;
		mWifiHelper = new WifiHelper(mContext);
	}
	
	private void UpdateWifiHelper() {
		mWifiHelper.startScan();
	}
		
	public void ScanHaveSpecifiedWifi() {
		if(D) Log.e(TAG, "scan wifi");
		UpdateWifiHelper();
		for (ScanResult result : mWifiHelper.getWifiList()) {
			if(D) Log.e(TAG, result.SSID + " " + result.level + " " + result.BSSID);
		}
	}
	
}
