package jiunling.gcm;

import static jiunling.config.config.RegistrarId;
import static jiunling.config.config.SENDER_ID;
import static jiunling.push.PushService.Register;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class GCM {
	//Debugging
	private static final String TAG = "GCM";
	private static final boolean D = true;
	
	public static String regId = "";
	
	Context mContext = null;

	public GCM( Context mContext ){
		this.mContext = mContext;
		this.Registrar();
	}
	
	private void Registrar(){
		GCMRegistrar.checkDevice(mContext);
    	GCMRegistrar.checkManifest(mContext);
    	regId = GCMRegistrar.getRegistrationId(mContext);
    	if (regId.equals("")) {
    		if(D) Log.e(TAG, "+++ regId.equals +++");
    		GCMRegistrar.register(mContext, SENDER_ID);
    	} else {
    		Log.e(TAG, "regId: "+regId);
    		RegistrarId = regId;
    	}
    	
    	Intent mIntent = new Intent("PushServer");
	    mIntent.putExtra("Kind", Register);
	    mContext.sendBroadcast(mIntent);
	}
	
	public void Unregister(){
        GCMRegistrar.onDestroy(mContext);
	}

}
