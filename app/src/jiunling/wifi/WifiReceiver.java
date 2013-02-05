package jiunling.wifi;

import static jiunling.config.config.SleepTime;
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
					
	/**		�b��swifi�AonReceive�|������A�]���n�����L�i�J�A�ҥH�]�wNetWordstatus	**/
	private boolean isWifi = true;
	private boolean isNotNetWord = false;
	private boolean NetWordstatus;
	
	private Environment mEnvironment;
		
	public BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				/***	��������ܮɷ|�i�J�o��		***/
				if(D) Log.e(TAG, "-- onReceive CONNECTIVITY_ACTION --");

				NetworkStatus();

			}else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				/***	��wifi�}�������ɷ|�i�J�o��		***/
				WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				int wifi_state = intent.getIntExtra("wifi_state", 0); 
		        int level = Math.abs(mWifiManager.getConnectionInfo().getRssi());
		        Log.i(TAG, "level:"+level); 
		        switch (wifi_state) { 
		        case WifiManager.WIFI_STATE_DISABLING: 
		        	if(D) Log.i(TAG, "WIFI_STATE_DISABLING");  
		        	NetWordstatus = isNotNetWord;
		        	break; 
		        case WifiManager.WIFI_STATE_DISABLED: 
		        	if(D) Log.i(TAG, "WIFI_STATE_DISABLED"); 
		        	break; 
		        case WifiManager.WIFI_STATE_ENABLING: 
		        	if(D) Log.i(TAG, "-- WIFI_STATE_ENABLING --"); 
		        	break; 
		        case WifiManager.WIFI_STATE_ENABLED: 
		        	if(D) Log.i(TAG, "-- WIFI_STATE_ENABLED --"); 
		        	NetWordstatus = isWifi;
		        	break; 
		        case WifiManager.WIFI_STATE_UNKNOWN: 
		        	if(D) Log.i(TAG, "WIFI_STATE_UNKNOWN"); 
		        	break; 
		        } 
			}
		} 
	};
    
	public WifiReceiver( Context mContext) {
		this.mContext = mContext;
		
		/***	Start Receiver	***/
		EnableReceiver();
		
		/***	��ťWifi����	***/
    	StartCheckWifi();
	}
			
	private void EnableReceiver(){		
		if(D) Log.e(TAG, "-- EnableReceiver --");
		
		/***	��ť		***/
    	IntentFilter mIntentFilter = new IntentFilter(); 
    	mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);		/*** 	wifi���A				***/
    	mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);	/*** 	(WIFI/3G) �s�u		***/
    	
    	mContext.registerReceiver(mWifiReceiver, mIntentFilter); 
        
	}

	public void DisableReceiver(){
		mContext.unregisterReceiver(mWifiReceiver);
	}
		
	/**		�ˬd�������A(WIFI or 3G)	**/
	private void NetworkStatus(){
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();  
	    if(info != null && info.isAvailable()) {
	        String name = info.getTypeName();
	        if(D) Log.e(TAG, "now Network "+name);
	        if(name.equals("WIFI")) {
	        	/**		UpdateEnvironment 	**/
	        	Intent mIntent = new Intent("UpdateEnvironment");
	        	mContext.sendBroadcast(mIntent);
	        	
	        	NetWordstatus = isWifi;
	        }
	    } else {
	    	if(D) Log.e(TAG, "no Network");
	    	NetWordstatus = isNotNetWord;
	    }
	}
		
	private void StartCheckWifi(){
		new Thread() {  
            @Override  
            public void run() {  
                super.run();  
                while(true){
                	try {  
                    	if(NetWordstatus){
                    		mEnvironment.CheckHaveSpecifiedWifi();
                    		Thread.sleep( SleepTime );
                    	}
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
            }  
        }.start();
	}
}