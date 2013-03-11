package jiunling.pull;

import static jiunling.config.config.GCM_KIND;
import static jiunling.config.config.GCM_MESSAGE;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class PullService {
	/***	Debugging	***/
	private static final String TAG = "PushService";
	private static final boolean D = true;
	
	private Context mContext;
	
	public static final int StoreWifi = 1;
	
	public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if(action.equals("PullService")) {
				int Kind 		= intent.getIntExtra(GCM_KIND, 0);
				String Message 	= intent.getStringExtra(GCM_MESSAGE);
				Pull(Kind, Message);
			}
		}
	};
	
	public PullService(Context mContext){
			this.mContext = mContext;
		
		EnableReceiver();
	}
	
	private void EnableReceiver(){		
		if(D) Log.e(TAG, "-- EnableReceiver --");
		
		/***	∫ ≈•		***/
    	IntentFilter mIntentFilter = new IntentFilter();
    	mIntentFilter.addAction("PullService");
    	
    	mContext.registerReceiver(mRequestServerReceiver, mIntentFilter); 
	}
	
	private void Pull(int Kind, String Message) {
		switch(Kind) {
		case StoreWifi:
			new StoreWifi(mContext).Save(Message);
			break;
		}
	}
}
