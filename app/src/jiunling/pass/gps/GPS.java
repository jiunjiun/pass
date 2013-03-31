package jiunling.pass.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPS implements LocationListener {
	/***	Debugging	***/
	private static final String TAG = "GPS";
	private static final boolean D = true;
	
	private LocationManager mLocationManager;
	private Location mLocation = null;
	private String LOCATION_SERVICE = LocationManager.GPS_PROVIDER;
//	private String LOCATION_SERVICE = LocationManager.NETWORK_PROVIDER;
	
	private final int AgainTime = 1000 * 60 * 10;
	
	@Override
	public void onLocationChanged(Location mLocation) {
		// TODO Auto-generated method stub
		this.mLocation = mLocation;
		if(D) Log.e(TAG, "AGPS: "+mLocation.getLatitude() + "," +mLocation.getLongitude());
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}
	
	public GPS(Context mContext) {
		if(D)Log.e(TAG, "GPS onCreate() ");
		LocationManager status = (LocationManager) (mContext.getSystemService(Context.LOCATION_SERVICE));
		try {
			if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);			//取得系統定位服務
				mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);				//使用GPS定位座標
				mLocationManager.requestLocationUpdates(LOCATION_SERVICE, AgainTime, 5, this);
			} 
		} catch (Exception e) {
		}
	}
	
	public String getLatitude() {
		return  mLocation != null ?	mLocation.getLatitude() +"" :  "";
	}
	
	public String getLongitude() {
		return mLocation != null ? mLocation.getLongitude() + "" : "";
	}
	
	
	public String getLoation() {
		if( mLocation != null) {
//			if(D) Log.e(TAG, "AGPS: "+mLocation.getLatitude() + "," +mLocation.getLongitude());
			return mLocation.getLatitude() + "," +mLocation.getLongitude();
		} 
		return "";
	}
}
