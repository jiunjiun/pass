package jiunling.pass;

import static jiunling.pass.config.Option.havaRoot;
import static jiunling.pass.push.PushService.Renew;
import static jiunling.pass.service.BackgroundService.haveBackgroundService;
import jiunling.pass.config.Option;
import jiunling.pass.service.BackgroundService;
import jiunling.pass.view.Setting;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {
	
	/***	Debugging	***/
	private static final String TAG = "MainActivity";
	private static final boolean D = true;
	
	private TextView tvWifiScan, tvNotify, tvUpdatetime;
		
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
    	switch(item.getItemId()) {
    	case R.id.menu_update:
    		Intent mIntent = new Intent("PushService");
    		mIntent.putExtra("Kind", Renew);
    	    sendBroadcast(mIntent);
    		break;
    	case R.id.menu_settings:
    		startActivity(new Intent().setClass(this , Setting.class));
    		break;
    	}
        return false;
    }
        
    private void init() {	
    	/**	 backgound Service	**/
    	if(haveBackgroundService){
    		Intent intent = new Intent(this, BackgroundService.class);
    	    startService(intent);
    	}
    	
    	findViewById();
    }
    
    private void findViewById() {
    	tvWifiScan = (TextView) findViewById(R.id.WifiScan);
    	tvNotify = (TextView) findViewById(R.id.NotificationUser);
    	tvUpdatetime = (TextView) findViewById(R.id.UpdateTime);
    	
    	Option mOption = new Option(this);
    	boolean NotificationUser 	= (Boolean) mOption.getPreferences(Option.Kind_WIFI_NOTIFICATION_USER);
    	boolean WifiScan 			= (Boolean) mOption.getPreferences(Option.Kind_WIFI_AUTO_SCAN);
		int 	SleepTime			= (Integer) mOption.getPreferences(Option.Kind_WIFI_UPDATE_INTERVAL);
		
		tvWifiScan.setText("WifiScan: "+WifiScan);
		tvNotify.setText("NotificationUser: "+ NotificationUser);
		tvUpdatetime.setText("SleepTime: "+ SleepTime);
    }
}
