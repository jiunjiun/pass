package jiunling.pass.gcm;

import static jiunling.pass.config.config.SENDER_ID;
import android.content.Context;
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
    	} 
//    	else {
//    		RegistrarId = regId;
//    	}
	}
	
	public void Unregister(){
        GCMRegistrar.onDestroy(mContext);
	}
}
