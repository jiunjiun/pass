package jiunling.pass;

import static jiunling.pass.push.PushService.Renew;
import static jiunling.pass.service.BackgroundService.haveBackgroundService;
import jiunling.pass.config.Option;
import jiunling.pass.service.BackgroundService;
import jiunling.pass.view.WifiDialog;
import jiunling.pass.view.option;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
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
		
//		if(!havaRoot) havaRoot = jiunling.pass.root.SuperUser.getRootAhth();
	}
	
	@Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "+++ ON Start +++");
        
//        init();
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
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_update:
    		Intent mIntent = new Intent("PushService");
    		mIntent.putExtra("Kind", Renew);
    	    sendBroadcast(mIntent);
    		break;
    	case R.id.menu_settings:
    		startActivity(new Intent().setClass(this , option.class));
    		break;
    	case R.id.menu_dialog:
//    		startActivity(new Intent().setClass(this , WifiDialog.class));
    		
    		//取得Notification服務
    		NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    		//設定當按下這個通知之後要執行的activity
    		Intent notifyIntent = new Intent(this, WifiDialog.class);
    		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		PendingIntent appIntent=PendingIntent.getActivity(this,0,notifyIntent,0);
    		Notification notification = new Notification();
    		notification.icon=R.drawable.ic_launcher;
    		//顯示在狀態列的文字
    		notification.tickerText="notification on status bar.";
    		//會有通知預設的鈴聲、振動、light
    		notification.defaults=Notification.DEFAULT_ALL;
    		notification.flags |= Notification.FLAG_AUTO_CANCEL;
    		//設定通知的標題、內容
    		notification.setLatestEventInfo(this,"Title","content",appIntent);
    		//送出Notification
    		notificationManager.notify(1,notification);
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
