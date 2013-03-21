package jiunling.pass.wifi;

import jiunling.pass.SQLite.WiFiDb;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

public class Environment {
	
	/***	Debugging	***/
	private static final String TAG = "Environment";
	private static final boolean D = false;
	
	private Context mContext;
		
	/*** 	wifi	***/
	private WifiHelper mWifiHelper = null;
	
	/***	MESSAGE format	***/
	public static final int MESSAGE_REQUEST 		= 1;
	public static final int MESSAGE_ERROR_EQUEST 	= 2;
	
	
	public Environment( Context mContext ) {
		this.mContext = mContext;
		mWifiHelper = new WifiHelper(mContext);
	}
	
	private void UpdateWifiHelper() {
		mWifiHelper.startScan();
	}
		
	public synchronized void ScanHaveSpecifiedWifi() {
		try {
			UpdateWifiHelper();
			if(mWifiHelper.getWifiStatus()) {
				if(D) Log.e(TAG, "mWifiHelper.getWifiList()== null: " + (mWifiHelper.getWifiList() == null));
				if(mWifiHelper.getWifiList() != null) {
					for (ScanResult result : mWifiHelper.getWifiList()) {
						if(D) Log.e(TAG, "result.SSID: " + result.SSID);
						if(result != null) {
							if(result.level > -70) {
								WiFiDb dbHelper = new WiFiDb(mContext);
								Cursor cursor 	= dbHelper.SWhere(new String[]{result.BSSID});
						    	int rows_num 	= cursor.getCount();
						    	if(rows_num != 0) {
									cursor.moveToFirst();			//將指標移至第一筆資料
									for(int i=0; i<rows_num; i++) {
										if(D) Log.e(TAG, "psk " + cursor.getString(0));
									    mContext.sendBroadcast(new Intent("isDataWifi"));
										WifiConnect(result, cursor.getString(0));
								        cursor.moveToNext();
									}
								}
						    	cursor.close();		//關閉Cursor
								dbHelper.close();	//關閉資料庫，釋放記憶體
							}
						}
					}	
				}
			}
		} catch(Exception e) {
			if(D) Log.e(TAG, "err: "+e);
		}
	}
	
	private void WifiConnect(ScanResult result , String PASSWD) {
		WifiConfiguration wc = null;
		if(D) Log.e(TAG, "result.capabilities: "+result.capabilities);
		if(PASSWD.equals("")) {
			wc = mWifiHelper.ConnectWifi(result.SSID, PASSWD, WifiHelper.WifiCipher_NOPASSWD);
		} else if(result.capabilities.indexOf("WEP") != -1) {
			wc = mWifiHelper.ConnectWifi(result.SSID, PASSWD, WifiHelper.WifiCipher_WEP);
		} else if(result.capabilities.indexOf("WPA") != -1) {
			wc = mWifiHelper.ConnectWifi(result.SSID, PASSWD, WifiHelper.WifiCipher_WPA);
		} else {
			if(D) Log.e(TAG, "Null");
		}
		
		mWifiHelper.addNetWordLink(wc);
	}
}











