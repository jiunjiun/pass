package jiunling.pass;

import static jiunling.pass.config.config.GCM_KIND;
import static jiunling.pass.config.config.GCM_MESSAGE;
import static jiunling.pass.config.config.RegistrarId;
import static jiunling.pass.config.config.SENDER_ID;
import static jiunling.pass.push.PushService.Register;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {
	
	private static final boolean D = true;
    private static final String TAG = "GCMIntentService";
   
    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
    	if(D) Log.e(TAG, "Device registered: regId = " + registrationId);
        RegistrarId = registrationId;
        Intent mIntent = new Intent("PushService");
	    mIntent.putExtra("Kind", Register);
	    sendBroadcast(mIntent);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if(D) Log.i(TAG, "Device unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {

        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
        	if(D) Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
    	int Kind 		= Integer.parseInt(intent.getStringExtra(GCM_KIND));
        String Message 	= intent.getStringExtra(GCM_MESSAGE);
        
        Intent mIntent = new Intent("PullService");
        mIntent.putExtra(GCM_KIND, Kind);
        mIntent.putExtra(GCM_MESSAGE, Message);
	    sendBroadcast(mIntent);
        
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
    	if(D) Log.i(TAG, "Received deleted messages notification");
    }

    @Override
    public void onError(Context context, String errorId) {
    	if(D) Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
    	if(D) Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
}
