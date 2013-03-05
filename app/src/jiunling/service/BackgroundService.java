package jiunling.service;

import jiunling.gcm.GCM;
import jiunling.gps.GPS;
import jiunling.push.PushService;
import jiunling.wifi.WifiReceiver;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {
	
	/***	Debugging	***/
	private static final String TAG = "BackgroundService";
	private static final boolean D = true;
	
	
	private GCM mGCM = null;
	private WifiReceiver mWifiReceiver = null;
	private PushService mPushService = null;
	public static GPS mGPS = null;
	
	
	/***	Background config State		***/
	public static boolean haveBackgroundService = true;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		if(D) Log.e(TAG, "onStart");
		
		haveBackgroundService = false;
		
		/***	Receiver	***/
		PushService_Receiver();
		GCM_Receiver();
		WiFi_Receiver();
		
		if(mGPS == null) mGPS = new GPS(this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(D) Log.e(TAG, "onDestroy");
		DisableReceiver();
	}
	
	private void PushService_Receiver() {
		if(mPushService == null) mPushService = new PushService(this);
	}
		
	private void GCM_Receiver() {
		if( mGCM == null) mGCM = new GCM(this);
	}
	
	private void WiFi_Receiver() {
		if(mWifiReceiver == null) {
			mWifiReceiver = new WifiReceiver(this);
			
			/***	Background config State		***/
	        haveBackgroundService = true;
		}
	}
	
	private void DisableReceiver() {
		if( mGCM != null) mGCM.Unregister();
		
		if(mWifiReceiver != null) {
			mWifiReceiver.DisableReceiver();
			
			/***	Background config State		***/
	        haveBackgroundService = false;
		}
	}
}
