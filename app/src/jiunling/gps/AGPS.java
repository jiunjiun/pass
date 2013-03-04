package jiunling.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class AGPS {
	/***	Debugging	***/
	private static final String TAG = "AGPS";
	private static final boolean D = false;
	
	private Context mContext;
	private LocationManager mLocationManager;
	private Location mLocation = null;
	
	
	public AGPS(Context mContext) {
		this.mContext = mContext;
		LocationManager status = (LocationManager) (mContext.getSystemService(Context.LOCATION_SERVICE));
		try {
			if ( status.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || status.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				//如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
				locationServiceInitial();
			} 
		}catch (Exception e) {
			
		}
	}

	private void locationServiceInitial() {
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);		//取得系統定位服務
		mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);			//使用GPS定位座標
	}
	
	public String getLatitude() {
		String Result = "";
		if( mLocation != null) 	Result = mLocation.getLatitude() +"";
		return Result;
	}
	
	public String getLongitude() {
		String Result = "";
		if( mLocation != null) Result = mLocation.getLongitude() + "";
		return Result;
	}
	
	
	public String getLoation() {
		String Result = "";
		if( mLocation != null) {
			if(D) Log.e(TAG, "AGPS: "+mLocation.getLatitude() + "," +mLocation.getLongitude());
			Result = mLocation.getLatitude() + "," +mLocation.getLongitude();
		}
		return Result;
	}
}
