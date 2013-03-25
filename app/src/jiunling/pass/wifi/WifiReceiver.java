package jiunling.pass.wifi;

import static jiunling.pass.config.Option.SleepTime;
import static jiunling.pass.config.Option.pubSleepTime;
import static jiunling.pass.config.Option.SendPubSleepTime;
import static jiunling.pass.config.Option.RemoveUpdateTime;
import static jiunling.pass.config.Option.WifiScan;
import static jiunling.pass.push.PushService.RegisterPublicWifis;
import static jiunling.pass.push.PushService.RegisterWifi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver {

	/***	Debugging	***/
	private static final String TAG = "WifiReceiver";
	private static final boolean D = true;
	
	private Context mContext;
	
	private Environment mEnvironment;	
	private ScanPublicWifi mScanPublicWifi;
	
	/**		在更新wifi，onReceive會捕捉到，因此要不讓他進入，所以設定Wifistatus	**/
	private boolean isWifi 			= true;
	private boolean NotWifi 		= false;
	private boolean Wifistatus 		= NotWifi;
	
	/**		在更新wifi，onReceive會捕捉到，因此要不讓他進入，所以設定NetWordstatus	**/
	private boolean hasNetWord 		= true;
	private boolean noNetWord 		= false;
	private boolean NetWorkstatus 	= noNetWord;
	
	/**		在更新wifi，onReceive會捕捉到，因此要不讓他進入，所以設定NetWordstatus	**/
	private boolean NotData 	= false;
	private boolean InData 		= true;
	private boolean WifiData 	= NotData;
	
	private int wifi_state 			= -1;
	
	public static final String StartCheckWifi 	= "StartCheckWifi";
	public static final String isDataWifi 		= "isDataWifi";
		
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
		        	break; 
		        case WifiManager.WIFI_STATE_DISABLED: 
		        	if(D) Log.e(TAG, "WIFI_STATE_DISABLED"); 
		        	break; 
		        case WifiManager.WIFI_STATE_ENABLING: 
		        	if(D) Log.e(TAG, "-- WIFI_STATE_ENABLING --"); 
		        	break; 
		        case WifiManager.WIFI_STATE_ENABLED: 
		        	if(D) Log.e(TAG, "-- WIFI_STATE_ENABLED --"); 
		        	Wifistatus = NotWifi;
		        	StartCheckWifi();
		        	
		        	FindPublicWifi();
		        	break; 
		        case WifiManager.WIFI_STATE_UNKNOWN: 
		        	if(D) Log.e(TAG, "WIFI_STATE_UNKNOWN"); 
		        	break; 
		        } 
			} else if (action.equals(StartCheckWifi)) {
				if(D) Log.e(TAG, "StartCheckWifi Start"); 
				Wifistatus = NotWifi;
	        	StartCheckWifi();
			} else if (action.equals(isDataWifi)) {
				WifiData = InData;
			}
			
		}
	};
    
	public WifiReceiver(Context mContext) {
		this.mContext = mContext;

		/***	Start Receiver	***/
		EnableReceiver();
		
		mEnvironment 	= new Environment(mContext);
		mScanPublicWifi = new ScanPublicWifi(mContext);
	}
			
	private void EnableReceiver() {		
		if(D) Log.e(TAG, "-- EnableReceiver --");
		
		/***	監聽		***/
    	IntentFilter mIntentFilter = new IntentFilter(); 
    	mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);		/*** 		WIFI Status			***/
    	mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);	/*** 	(WIFI/3G) connect		***/
    	mIntentFilter.addAction(StartCheckWifi);							/**		callStartCheckWifi		***/
    	mIntentFilter.addAction(isDataWifi);								/**			isDataWifi			***/
    	
    	mContext.registerReceiver(mWifiReceiver, mIntentFilter); 
	}

	public void DisableReceiver() {
		mContext.unregisterReceiver(mWifiReceiver);
	}
		
	/**		檢查網路狀態(WIFI or 3G)	**/
	private void NetworkStatus(){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();  
	    if(info != null && info.isAvailable()) {	    	
	        String name = info.getTypeName();
	        if(D) Log.e(TAG, "now Network "+name);
	        if(name.equals("WIFI")) {
	        	Wifistatus = isWifi;
	        	if(!WifiData) ConnectedWifiPasswd();
	        	MonitorWifi();
	        } else {
	        	Wifistatus = NotWifi;
	        	StartCheckWifi();
	        }
	        
	        NetWorkstatus = hasNetWord;
	    	SendPublicWifi();
	    } else {
	    	NetWorkstatus = noNetWord;
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
                while(Wifistatus) {
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
	                WifiData = NotData;
	                while(!Wifistatus) {
	                	if(D) Log.e(TAG, "Wifistatus: "+Wifistatus);
	                	try {  
	                		mEnvironment.ScanHaveSpecifiedWifi();
	                		Thread.sleep( SleepTime );
	    				} catch (InterruptedException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	                }
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
                		mScanPublicWifi.Scan();
                		Thread.sleep( pubSleepTime );
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
            }  
        }.start();
	}
	
	private synchronized void SendPublicWifi() {
		new Thread() {  
            @Override  
            public void run() {  
                super.run();  
                while(NetWorkstatus) {
                	try {  
                		Thread.sleep(SendPubSleepTime) ;
                		
                		Intent mIntent = new Intent("PushService");
                		mIntent.putExtra("Kind", RegisterPublicWifis);
                	    mContext.sendBroadcast(mIntent);
                	    
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
            }  
        }.start();
	}
}