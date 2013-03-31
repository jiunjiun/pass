package jiunling.pass.wifi;

import static jiunling.pass.service.BackgroundService.mGPS;

import java.util.HashMap;
import java.util.Map;

import jiunling.pass.SQLite.PublicWifiDb;

import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

public class ScanPublicWifi {
	
	/***	Debugging	***/
	private static final String TAG = "ScanPublicWifi";
	private static final boolean D = false;
	
	private Context mContext;

	private WifiHelper mWifiHelper = null;
	
	public ScanPublicWifi(Context mContext) {
		this.mContext = mContext;
		mWifiHelper = new WifiHelper(mContext);
	}
	
	private void UpdateWifiHelper() {
		mWifiHelper.startScan();
	}
	
	public synchronized void Scan() {
		try {
			UpdateWifiHelper();
			if(mWifiHelper.getWifiStatus()) {
				for(ScanResult result : mWifiHelper.getWifiList()) {
					if(result.capabilities.indexOf("[ESS]") == 0) {
						Save(result.SSID, result.BSSID);
					}
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	private void Save(String SSID, String MAC) {
		String GPS = getGPS();
		PublicWifiDb mPublicWifiDb = new PublicWifiDb(mContext);
		if(D) Log.e(TAG, "SSID: "+ SSID +" MAC "+ MAC +" GPS " + GPS);
		long l = mPublicWifiDb.Create(SSID, MAC, GPS);
		if(D) Log.e(TAG, "return: "+ l);
		mPublicWifiDb.close();
	}
	
	private String getGPS() {
		Map<String, String> gps_params = new HashMap<String, String>();
		if(mGPS != null) {
			gps_params.put("lat", mGPS.getLatitude());
			gps_params.put("lon", mGPS.getLongitude());
		}
		JSONObject mJSONGPS = new JSONObject(gps_params);
		return mJSONGPS.toString();
	}
}
