package jiunling.pass;

import static jiunling.pass.config.Option.havaRoot;
import static jiunling.pass.push.PushService.RegisterPublicWifis;
import static jiunling.pass.push.PushService.Renew;
import static jiunling.pass.service.BackgroundService.haveBackgroundService;
import jiunling.pass.config.Option;
import jiunling.pass.service.BackgroundService;
import jiunling.pass.view.option;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {
	
	/***	Debugging	***/
	private static final String TAG = "MainActivity";
	private static final boolean D = true;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!havaRoot) havaRoot = jiunling.pass.root.SuperUser.getRootAhth();
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
		
        Intent mIntent = new Intent("PushService");
		mIntent.putExtra("Kind", RegisterPublicWifis);
	    sendBroadcast(mIntent);
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(D) Log.e(TAG, "item.getItemId() " + item.getItemId());
    	switch(item.getItemId()) {
    	case R.id.menu_update:
    		Intent mIntent = new Intent("PushService");
    		mIntent.putExtra("Kind", Renew);
    	    sendBroadcast(mIntent);
    		break;
    	case R.id.menu_settings:
    		startActivity(new Intent().setClass(this , option.class));
    		break;
    	}
        return false;
    }
        
    private void init() {	
    	/**		init option		**/
    	new Option(this);
    	
    	/**	 backgound Service	**/
    	if(haveBackgroundService){
    		Intent intent = new Intent(this, BackgroundService.class);
    	    startService(intent);
    	}
    }
}
