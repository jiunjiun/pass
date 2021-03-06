package jiunling.pass.wifi;

import static jiunling.pass.config.Option.RemoveUpdateTime;
import static jiunling.pass.config.Option.Second;
import static jiunling.pass.config.Option.SendPubSleepTime;
import static jiunling.pass.config.Option.pubSleepTime;
import static jiunling.pass.push.PushService.RegisterPublicWifis;
import static jiunling.pass.push.PushService.RegisterWifi;
import jiunling.pass.config.Option;
import jiunling.pass.utile.Notifiy;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class WifiReceiver {

	/***	Debugging	***/
	private static final String TAG = "WifiReceiver";
	private static final boolean D = true;
	
	private Context mContext;
	
	private Environment mEnvironment;	
	private ScanPublicWifi mScanPublicWifi;
	
	/**		在更新wifi，onReceive會捕捉到，判斷是不是wifi	**/
	private boolean isWifi 			= true;
	private boolean NotWifi 		= false;
	private boolean WifiStatus 		= NotWifi;
	
	/**		在更新wifi，onReceive會捕捉到，判斷是否有網路連線能力	**/
	private boolean hasNetWord 		= true;
	private boolean noNetWord 		= false;
	private boolean NetWorkStatus 	= noNetWord;
	
	/**		在更新wifi，onReceive會捕捉到，判斷是否為本機資料庫的網路	**/
	private boolean NotData 		= false;
	private boolean InData 			= true;
	private boolean WifiDataStatus 	= NotData;
	
	private static boolean StartCheckWifiStatusStop		= false;
	private static boolean StartCheckWifiStatusRunning	= true;
	private static boolean StartCheckWifiStatus			= StartCheckWifiStatusStop;
	
	private int wifi_state 			= -1;
	
	public static final String WifiIsData 		= "WifiIsData";
	
	private Option 	mOption			= null;
	private boolean WifiScan		= Option.WifiScan;
	private int SleepTime			= Option.SleepTime;
	
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	
	public BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				/***	當網路改變時會進入這裡		***/
				if(D) Log.e(TAG, "-- onReceive CONNECTIVITY_ACTION --");
				NetworkStatus();
			} else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				/***	當wifi開啟關閉時會進入這裡		***/
				wifi_state = intent.getIntExtra("wifi_state", 0); 
		        switch (wifi_state) { 
		        case WifiManager.WIFI_STATE_DISABLING: 
		        	if(D) Log.e(TAG, "WIFI_STATE_DISABLING");  
		        	Notifiy.NotifyAllCancel(context);
		        	break; 
		        case WifiManager.WIFI_STATE_DISABLED: 
		        	if(D) Log.e(TAG, "WIFI_STATE_DISABLED"); 
		        	break; 
		        case WifiManager.WIFI_STATE_ENABLING: 
		        	if(D) Log.e(TAG, "-- WIFI_STATE_ENABLING --"); 
		        	break; 
		        case WifiManager.WIFI_STATE_ENABLED: 
		        	if(D) Log.e(TAG, "-- WIFI_STATE_ENABLED --"); 
//		        	WifiStatus = NotWifi;
		        	if(!StartCheckWifiStatus) StartCheckWifi();
		        	
		        	FindPublicWifi();
		        	break; 
		        case WifiManager.WIFI_STATE_UNKNOWN: 
		        	if(D) Log.e(TAG, "WIFI_STATE_UNKNOWN"); 
		        	break; 
		        } 
			} else if (action.equals(WifiIsData)) {
				WifiDataStatus = InData;
			}
			
		}
	};
    
	public WifiReceiver(Context mContext) {
		this.mContext = mContext;
		
		/***	Option config	***/
		OptionConfig();

		/***	Start Receiver	***/
		EnableReceiver();
		
		/***	Start PreferenceChangeListener	***/
		EnableSharedPreferences();
		
		mEnvironment 	= new Environment(mContext);
		mScanPublicWifi = new ScanPublicWifi(mContext);
	}
			
	private void EnableReceiver() {		
		if(D) Log.e(TAG, "-- EnableReceiver --");
		
		/***	監聽		***/
    	IntentFilter mIntentFilter = new IntentFilter(); 
    	mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);		/*** 		WIFI Status			***/
    	mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);	/*** 	(WIFI/3G) connect		***/
    	mIntentFilter.addAction(WifiIsData);								/**			isDataWifi			***/
    	
    	mContext.registerReceiver(mWifiReceiver, mIntentFilter); 
	}

	public void DisableReceiver() {
		mContext.unregisterReceiver(mWifiReceiver);
	}
	
	private void OptionConfig() {
		if(mOption == null) mOption = new Option(mContext);
		WifiScan 	= (Boolean) mOption.getPreferences(Option.Kind_WIFI_AUTO_SCAN);
		SleepTime	= (Integer) mOption.getPreferences(Option.Kind_WIFI_UPDATE_INTERVAL) * Second;
	}
	
	private void EnableSharedPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences mSharedPreferences, String key) {
				if(key.equals(mOption.Key_WIFI_AUTO_SCAN)) {
					WifiScan = mSharedPreferences.getBoolean(key, false);
					if(!StartCheckWifiStatus) StartCheckWifi();
					if(!WifiScan) Notifiy.NotifyAllCancel(mContext);
				} else if(key.equals(mOption.Key_WIFI_UPDATE_INTERVAL)) {
					SleepTime = Integer.parseInt(mSharedPreferences.getString(key, "")) * Second;
				}
				if(D) Log.e(TAG, "WifiScan: "+ WifiScan+ " SleepTime: "+SleepTime);
			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}
		
	/**		檢查網路狀態(WIFI or 3G)	**/
	private void NetworkStatus() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();  
	    if(info != null && info.isAvailable()) {	    	
	        String name = info.getTypeName();
	        if(D) Log.e(TAG, "now Network "+name);
	        if(name.equals("WIFI")) {
	        	WifiStatus = isWifi;
	        	if(!WifiDataStatus) ConnectedWifiPasswd();
	        	MonitorWifi();
	        	Notifiy.NotifyAllCancel(mContext);
	        } else {
	        	WifiStatus = NotWifi;
	        	if(!StartCheckWifiStatus) StartCheckWifi();
	        }
	        
	        NetWorkStatus = hasNetWord;
	    	SendPublicWifi();
	    } else {
	    	NetWorkStatus = noNetWord;
	    	if(D) Log.e(TAG, "no Network");
	    }
	}
	
	private void ConnectedWifiPasswd() {
		WifiHelper mWifiHelper = new WifiHelper(mContext);
		RegexNetwork mRegexNetwork = new RegexNetwork();
		mRegexNetwork.getNetwork(mWifiHelper.getSSID());
		
		if(mRegexNetwork.verify()) 	Push(mWifiHelper.getSSID(), mWifiHelper.getBSSID(), mRegexNetwork.getPSK());
	}
	
	private void Push(String SSID, String MAC, String psk) {
		String []Parameter = new String[3];
		
		Parameter[0] = SSID;
		Parameter[1] = MAC;
		Parameter[2] = psk;

		Intent mIntent = new Intent("PushService");
	    mIntent.putExtra("Kind", RegisterWifi);
	    mIntent.putExtra("Parameter", Parameter);
	    mContext.sendBroadcast(mIntent);
		
	}
	
	private synchronized void MonitorWifi() {
		new Thread() {  
            @Override  
            public void run() {  
                super.run();  
                while(WifiStatus) {
                	try {  
                		mEnvironment.MonitorWifi();
                		Thread.sleep( RemoveUpdateTime );
                	} catch (InterruptedException e) {
                		e.printStackTrace();
                	}
                }
            }
		}.start();
	}
	
	private synchronized void StartCheckWifi() {
		if(wifi_state == WifiManager.WIFI_STATE_ENABLED && WifiScan) {
			new Thread() {  
	            @Override  
	            public void run() {  
	                super.run();  
	                StartCheckWifiStatus = StartCheckWifiStatusRunning;
	                WifiDataStatus = NotData;
	                while(!WifiStatus && WifiScan) {
	                	if(D) Log.d(TAG, "StartCheckWifi Worker");
	                	try {  
	                		mEnvironment.ScanHaveSpecifiedWifi();
	                		Thread.sleep( SleepTime );
	    				} catch (InterruptedException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	                }
	                StartCheckWifiStatus = StartCheckWifiStatusStop;
	                if(D) Log.d(TAG, "StartCheckWifi Stop Worker");
	            }  
	        }.start();
		}
		
	}
	
	private synchronized void FindPublicWifi() {
		new Thread() {  
            @Override  
            public void run() {  
                super.run();  
                while(wifi_state == WifiManager.WIFI_STATE_ENABLED) {
                	try {  
                		if(D) Log.i(TAG, "FindPublicWifi Worker");
                		mScanPublicWifi.Scan();
                		Thread.sleep( pubSleepTime );
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
                if(D) Log.i(TAG, "FindPublicWifi Stop Worker");
            }  
        }.start();
	}
	
	private synchronized void SendPublicWifi() {
		new Thread() {  
            @Override  
            public void run() {  
                super.run();  
                while(NetWorkStatus) {
                	try {  
                		if(D) Log.w(TAG, "SendPublicWifi Worker");
                		Thread.sleep(SendPubSleepTime) ;
                		
                		Intent mIntent = new Intent("PushService");
                		mIntent.putExtra("Kind", RegisterPublicWifis);
                	    mContext.sendBroadcast(mIntent);
                	    
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
                if(D) Log.w(TAG, "SendPublicWifi Stop Worker");
            }  
        }.start();
	}
}