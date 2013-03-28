package jiunling.pass.wifi;

import static jiunling.pass.config.Option.RssiLimit;
import static jiunling.pass.wifi.WifiReceiver.WifiIsData;

import java.util.HashMap;

import jiunling.pass.SQLite.WiFiDb;
import jiunling.pass.config.Option;
import jiunling.pass.utile.Notifiy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.preference.PreferenceManager;
import android.util.Log;

public class Environment {
	
	/***	Debugging	***/
	private static final String TAG = "Environment";
	private static final boolean D 	= true;
	
	private Context mContext;
		
	/*** 	wifi	***/
	private WifiHelper mWifiHelper = null;	
	
	private HashMap<String, String> mWifListHashMap;
	private JSONArray mJSONArray;
	
	public final static String WifiConnectReceiver 	= "WifiConnectReceiver";	
	
	private Option 	mOption							= null;
	private boolean NotificationUser				= Option.NotificationUser;
	
	public BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent mIntent) {
			String action = mIntent.getAction();
			if (action.equals(WifiConnectReceiver)) {
				try {
					WifiConnect(new JSONObject(mIntent.getStringExtra("Wifi")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	public Environment( Context mContext ) {
		this.mContext 	= mContext;
		mWifiHelper 	= new WifiHelper(mContext);
		mWifListHashMap = new HashMap<String, String>();
		
		/***	Option config	***/
		OptionConfig();
		
		/***	Start Receiver	***/
		EnableReceiver();
		
		/***	Start PreferenceChangeListener	***/
		EnableSharedPreferences();
		
	}
	
	private void EnableReceiver() {
		/***	監聽		***/
    	IntentFilter mIntentFilter = new IntentFilter(); 
    	mIntentFilter.addAction(WifiConnectReceiver);		/*** 		WIFI Status			***/
    	mContext.registerReceiver(mWifiReceiver, mIntentFilter); 
	}
	
	public void DisableReceiver() {
		mContext.unregisterReceiver(mWifiReceiver);
	}
	
	private void OptionConfig() {
		if(mOption == null) mOption = new Option(mContext);
		NotificationUser = (Boolean) mOption.getPreferences(Option.Kind_WIFI_NOTIFICATION_USER);
		
	}
	
	private void EnableSharedPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
				@Override
				public void onSharedPreferenceChanged(SharedPreferences mSharedPreferences, String key) {
		            if(key.equals(mOption.Key_WIFI_NOTIFICATION_USER)) {
		            	NotificationUser = mSharedPreferences.getBoolean(key, false);
		    		}
				}
			});
	}
		
	public synchronized void ScanHaveSpecifiedWifi() {
		try {
			mWifiHelper.startScan();
			if(mWifiHelper.getWifiStatus()) {
				if(mWifiHelper.getWifiList() != null) {
					mJSONArray = new JSONArray();
					for (ScanResult result : mWifiHelper.getWifiList()) {
//						if(D) Log.e(TAG, "result.SSID: " + result.SSID);
//						if(D) Log.e(TAG, "result.BSSID: " + result.BSSID);
						if(result != null) {
							if(result.level >= RssiLimit) {
								matchWifiDB(result);
							}
						}
					}
					FilterWifi();
				}
			}
		} catch(Exception e) {
			if(D) Log.e(TAG, "err: "+e);
		}
	}
	
	private void matchWifiDB(ScanResult result) {		
		WiFiDb dbHelper = new WiFiDb(mContext);
		Cursor cursor 	= dbHelper.SWhere(new String[]{result.BSSID});
    	int rows_num 	= cursor.getCount();
    	if(rows_num != 0) {
			cursor.moveToFirst();			//將指標移至第一筆資料
			for(int i=0; i<rows_num; i++) {
//				if(D) Log.e(TAG, "psk " + cursor.getString(0));

				HashMap<String, String> mWiFiHashMap = new HashMap<String, String>();
				mWiFiHashMap.put("SSID", result.SSID);
				mWiFiHashMap.put("BSSID", result.BSSID);
				mWiFiHashMap.put("level", result.level+"");
				mWiFiHashMap.put("capabilities", result.capabilities);
				mWiFiHashMap.put("au4a83", cursor.getString(0));
				
				mJSONArray.put(new JSONObject(mWiFiHashMap));

		        cursor.moveToNext();
			}
		}
    	cursor.close();		//關閉Cursor
		dbHelper.close();	//關閉資料庫，釋放記憶體
	}
	
	private void FilterWifi() {
		SortWifi_SelectionSort();
		if(NotificationUser) {
			if(mJSONArray.length() == 0) {
				Notifiy.NotifyCancel(mContext, Notifiy.NotifyUser);
			} else {
				mWifListHashMap.put("Wifi", mJSONArray.toString());
				JSONObject mJSONObjectParameter = new JSONObject(mWifListHashMap);
				Notifiy.NotifyMsg(mContext, Notifiy.NotifyUser, mJSONObjectParameter.toString());
			}
		} else {
			try {
				if(mJSONArray.length() != 0) {
					JSONObject mJSONObject = mJSONArray.getJSONObject(0);
					WifiConnect(mJSONObject);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void SortWifi_SelectionSort() {
		try {
			JSONArray tempJSONArray = mJSONArray;
			for(int i=0;i<tempJSONArray.length();i++) {
				
				int k = i;
				int min = Integer.parseInt(tempJSONArray.getJSONObject(i).getString("level"));
				for (int j = i + 1; j < tempJSONArray.length(); j++) {
					int tempint = Integer.parseInt(tempJSONArray.getJSONObject(j).getString("level"));
					if (tempint < min) {
						k = j;
						min = tempint;
					}
					if (k != i) {
						int tempLevel = Integer.parseInt(tempJSONArray.getJSONObject(i).getString("level"));
						tempJSONArray.getJSONObject(i).put("level", tempJSONArray.getJSONObject(k).getString("level"));
						tempJSONArray.getJSONObject(k).put("level", tempLevel);
						
						String tempSSID = tempJSONArray.getJSONObject(i).getString("SSID");
						tempJSONArray.getJSONObject(i).put("SSID", tempJSONArray.getJSONObject(k).getString("SSID"));
						tempJSONArray.getJSONObject(k).put("SSID", tempSSID);
						
						String tempBSSID = tempJSONArray.getJSONObject(i).getString("BSSID");
						tempJSONArray.getJSONObject(i).put("BSSID", tempJSONArray.getJSONObject(k).getString("BSSID"));
						tempJSONArray.getJSONObject(k).put("BSSID", tempBSSID);
						
						String tempCapabilities = tempJSONArray.getJSONObject(i).getString("capabilities");
						tempJSONArray.getJSONObject(i).put("capabilities", tempJSONArray.getJSONObject(k).getString("capabilities"));
						tempJSONArray.getJSONObject(k).put("capabilities", tempCapabilities);
						
						String tempau4a83 = tempJSONArray.getJSONObject(i).getString("au4a83");
						tempJSONArray.getJSONObject(i).put("au4a83", tempJSONArray.getJSONObject(k).getString("au4a83"));
						tempJSONArray.getJSONObject(k).put("au4a83", tempau4a83);
					}
				}
				
			}
			mJSONArray = tempJSONArray;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void WifiConnect(JSONObject mJSONObject) {
		try {
			String SSID 		= mJSONObject.getString("SSID");
			String PASSWD 		= mJSONObject.getString("au4a83");
			String capabilities = mJSONObject.getString("capabilities");
			
			mContext.sendBroadcast(new Intent(WifiIsData));
			WifiConfiguration wc = null;
	//		if(D) Log.e(TAG, "result.capabilities: "+result.capabilities);
			if(PASSWD.equals("")) {
				wc = mWifiHelper.ConnectWifi(SSID, PASSWD, WifiHelper.WifiCipher_NOPASSWD);
			} else if(capabilities.indexOf("WEP") != -1) {
				wc = mWifiHelper.ConnectWifi(SSID, PASSWD, WifiHelper.WifiCipher_WEP);
			} else if(capabilities.indexOf("WPA") != -1) {
				wc = mWifiHelper.ConnectWifi(SSID, PASSWD, WifiHelper.WifiCipher_WPA);
			} else {
//				if(D) Log.e(TAG, "Null");
			}
			mWifiHelper.addNetWordLink(wc);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(D) e.printStackTrace();
		}
	}
	
	public void MonitorWifi() {
		try {
			mWifiHelper = new WifiHelper(mContext);
			if(mWifiHelper.getWifiStatus()) {
				int Rssi = mWifiHelper.getRssi();
				if(Rssi < RssiLimit) {
					int NetId = mWifiHelper.getCurrentNetId();
					mWifiHelper.removeNetworkLink(NetId);
				}
			}
		} catch(Exception e) {
			if(D) Log.e(TAG, "err: "+e);
		}
	}	
}











