package jiunling.pass.push;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PushService {
	/***	Debugging	***/
	private static final String TAG = "PushService";
	private static final boolean D = true;
	
	private Context mContext;
	
	public static final int Register 			= 1;
	public static final int RegisterWifi 		= 2;
	public static final int Renew 				= 3;
	public static final int RegisterPublicWifis	= 4;
	
	public BroadcastReceiver mRequestServerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if(action.equals("PushService")) {
				int Kind = intent.getIntExtra("Kind", 0);
				String[] mParameter = intent.getStringArrayExtra("Parameter");
				Push(Kind, mParameter);
			}
		}
	};
	
	public PushService(Context mContext){
		this.mContext = mContext;
		
		EnableReceiver();
	}
	
	private void EnableReceiver(){		
		if(D) Log.e(TAG, "-- EnableReceiver --");
		
		/***	��ť		***/
    	IntentFilter mIntentFilter = new IntentFilter();
    	mIntentFilter.addAction("PushService");
    	
    	mContext.registerReceiver(mRequestServerReceiver, mIntentFilter); 
	}
	
	protected void DisableReceiver(){
		mContext.unregisterReceiver(mRequestServerReceiver);
	}
	
	private void Push(int Kind, String[] mParameter) {
		List<NameValuePair> mParams = new ArrayList<NameValuePair>();
		switch(Kind) {
		case Register:
			Register mRegister = new Register(mContext);
			mParams = mRegister.getParams();
			break;
			
		case RegisterWifi:
			RegisterWifi mRegisterWifi = new RegisterWifi(mParameter);
			mParams = mRegisterWifi.getParams();
			break;
			
		case Renew:
			RenewWifi mRenewWifi = new RenewWifi(mContext);
			mParams = mRenewWifi.getParams();
			break;
			
		case RegisterPublicWifis:
			RegisterPublicWifis mRegisterPublicWifis = new RegisterPublicWifis(mContext);
			mParams = mRegisterPublicWifis.getParams();
			break;
		}
		
		new PushServer(Kind, mParams, mHandler);
	}
	
	/**		get Data	**/
	@SuppressLint({ "HandlerLeak" })
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			int Error = (int)msg.arg1;
//			int Kind = msg.what;
//			int Type = (int)msg.arg2;
//			String json_data = (String)msg.obj;
			
		}
	};
}