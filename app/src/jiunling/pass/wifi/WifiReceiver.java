package jiunling.pass.wifi;

import static jiunling.pass.config.Option.SleepTime;
import static jiunling.pass.config.Option.pubSleepTime;
import static jiunling.pass.config.Option.SendPubSleepTime;
import static jiunling.pass.config.Option.RemoveUpdateTime;
import static jiunling.pass.config.Option.WifiScan;
import static jiunling.pass.push.PushService.RegisterPublicWifis;
import static jiunling.pass.push.PushService.RegisterWifi;
import jiunling.pass.utile.Notifiy;
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
	
	private int wifi_state 			= -1;
	
	public static final String StartCheckWifi 	= "StartCheckWifi";
	public static final String WifiIsData 		= "WifiIsData";
		
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
		        	WifiStatus = NotWifi;
		        	StartCheckWifi();
		        	
		        	FindPublicWifi();
		        	break; 
		        case WifiManager.WIFI_STATE_UNKNOWN: 
		        	if(D) Log.e(TAG, "WIFI_STATE_UNKNOWN"); 
		        	break; 
		        } 
			} else if (action.equals(StartCheckWifi)) {
				if(D) Log.e(TAG, "StartCheckWifi Start"); 
				WifiStatus = NotWifi;
	        	StartCheckWifi();
			} else if (action.equals(WifiIsData)) {
				WifiDataStatus = InData;
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
    	mIntentFilter.addAction(WifiIsData);								/**			isDataWifi			***/
    	
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
	        	WifiStatus = isWifi;
	        	if(!WifiDataStatus) ConnectedWifiPasswd();
	        	MonitorWifi();
	        	Notifiy.NotifyAllCancel(mContext);
	        } else {
	        	WifiStatus = NotWifi;
	        	StartCheckWifi();
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
	                WifiDataStatus = NotData;
	                while(!WifiStatus) {
	                	if(D) Log.e(TAG, "Wifistatus: "+WifiStatus);
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
                while(NetWorkStatus) {
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