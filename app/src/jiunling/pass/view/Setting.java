package jiunling.pass.view;

import jiunling.pass.R;
import jiunling.pass.config.Option;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class Setting extends SherlockPreferenceActivity implements OnPreferenceChangeListener {
	
	/***	Debugging	***/
	private static final String TAG = "Setting";
	private static final boolean D = false;

	CheckBoxPreference wifi_auto_scan, wifi_notification_user;
	ListPreference wifi_update_intervalValues; 
	
	private Option mOption				= null;
	private boolean WifiScan			= Option.WifiScan;
	private boolean NotificationUser	= Option.NotificationUser;
	
	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		/***	Option config	***/
		OptionConfig();
		
		wifi_auto_scan 				= (CheckBoxPreference) findPreference(mOption.Key_WIFI_AUTO_SCAN);
		wifi_notification_user 		= (CheckBoxPreference) findPreference(mOption.Key_WIFI_NOTIFICATION_USER);
		wifi_update_intervalValues 	= (ListPreference) findPreference(mOption.Key_WIFI_UPDATE_INTERVAL);
		
		wifi_auto_scan.setOnPreferenceChangeListener(this);
		wifi_notification_user.setOnPreferenceChangeListener(this);
		wifi_update_intervalValues.setOnPreferenceChangeListener(this);
		
		
		if(WifiScan) wifi_auto_scan.setSummary(getResources().getString(R.string.enable));
		else wifi_auto_scan.setSummary(getResources().getString(R.string.disable));
		
		if(NotificationUser) wifi_notification_user.setSummary(getResources().getString(R.string.enable));
		else wifi_notification_user.setSummary(getResources().getString(R.string.disable));
		
	}
	
	private void OptionConfig() {
		if(mOption == null) mOption = new Option(this);
		WifiScan 			= (Boolean) mOption.getPreferences(Option.Kind_WIFI_AUTO_SCAN);
		NotificationUser 	= (Boolean) mOption.getPreferences(Option.Kind_WIFI_NOTIFICATION_USER);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case android.R.id.home:
		        finish();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		if(D) Log.e(TAG, "Change preference.key: "+preference.getKey().toString());
		if(D) Log.e(TAG, "Change preference.key: "+newValue.toString());
		
		if(preference.getKey().equals(mOption.Key_WIFI_AUTO_SCAN)) {
			WifiScan = (Boolean)newValue;
			if(WifiScan) 
				preference.setSummary(getResources().getString(R.string.enable));
			else 
				preference.setSummary(getResources().getString(R.string.disable));

		} else if(preference.getKey().equals(mOption.Key_WIFI_NOTIFICATION_USER)) {
			NotificationUser = (Boolean)newValue;
			if(NotificationUser)
				preference.setSummary(getResources().getString(R.string.enable));
			else
				preference.setSummary(getResources().getString(R.string.disable));
		}
		return true;
	}
}
