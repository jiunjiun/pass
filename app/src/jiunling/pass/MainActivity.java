package jiunling.pass;

import static jiunling.config.config.havaRoot;
import static jiunling.service.BackgroundService.haveBackgroundService;
import jiunling.service.BackgroundService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {
	
	/***	Debugging	***/
	private static final String TAG = "MainActivity";
	private static final boolean D = false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!havaRoot) havaRoot = jiunling.root.SuperUser.getRootAhth();
	}
	
	@Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "+++ ON Start +++");
        
        init();
    }
    
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+++ ON Resume +++");

    }
    
    @Override
    public synchronized void onPause() {
    	super.onPause();
        if(D) Log.e(TAG, "- ON Pause -"); 

    }
 
    @Override
    public void onStop() {
    	super.onStop();
        if(D) Log.e(TAG, "-- ON Stop --");
        
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
        if(D) Log.e(TAG, "--- ON Destroy ---");
    }
    
    private void init() {		
    	/**	 backgound Service	**/
    	if(haveBackgroundService){
    		Intent intent = new Intent(this, BackgroundService.class);
    	    startService(intent);
    	}
    }
}
