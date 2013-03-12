package jiunling.pass.view;

import jiunling.pass.R;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class option extends SherlockPreferenceActivity implements OnPreferenceChangeListener {
	
	/***	Debugging	***/
	private static final String TAG = "MainActivity";
	private static final boolean D = true;
	
	
	String wifi_auto_scan_key, wifi_notification_user_key, wifi_update_interval_key;
	CheckBoxPreference wifi_auto_scan, wifi_notification_user;
	ListPreference wifi_update_intervalValues; 
	
	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		wifi_auto_scan_key			= getResources().getString(R.string.wifi_auto_scan_key);
		wifi_notification_user_key	= getResources().getString(R.string.wifi_notification_user_key);
		wifi_update_interval_key	= getResources().getString(R.string.wifi_update_interval_key);
		
		wifi_auto_scan 				= (CheckBoxPreference) findPreference(wifi_auto_scan_key);
		wifi_notification_user 		= (CheckBoxPreference) findPreference(wifi_notification_user_key);
		wifi_update_intervalValues 	= (ListPreference) findPreference(wifi_update_interval_key);
		
		wifi_update_intervalValues.setValueIndex(3);
		
		wifi_auto_scan.setOnPreferenceChangeListener(this);
		wifi_notification_user.setOnPreferenceChangeListener(this);
		wifi_update_intervalValues.setOnPreferenceChangeListener(this);
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
		
		if(preference.getKey().equals(wifi_auto_scan_key)) {
			if((Boolean)newValue) {
				preference.setSummary(getResources().getString(R.string.wifi_auto_scan_enable));
			} else {
				preference.setSummary(getResources().getString(R.string.wifi_auto_scan_disable));
			}
		} else if(preference.getKey().equals(wifi_notification_user_key)) {
			if((Boolean)newValue) {
				preference.setSummary(getResources().getString(R.string.wifi_notification_user_enable));
			} else {
				preference.setSummary(getResources().getString(R.string.wifi_notification_user_disable));
			}
		} else if(preference.getKey().equals(wifi_update_interval_key)) {
			
		}
		return true;
	}
}
