package jiunling.pass.push;

import static jiunling.pass.config.config.Email;
import static jiunling.pass.service.BackgroundService.mGPS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class RenewWifi {
	
	/***	Debugging	***/
//	private static final String TAG = "Register";
//	private static final boolean D = true;
	
	private Map<String, String> Parameter;
	private JSONObject mJSONObjectParameter;
	
	private Context mContext;
	
	public RenewWifi(Context mContext) {
		this.mContext = mContext;
		getUserEmail();
		renew();
	}
	
	private void getUserEmail() {
		
		AccountManager am = AccountManager.get(mContext); // "this" references the current Context
		Account[] accounts = am.getAccountsByType("com.google");
		for (Account account : accounts) { 
			// TODO: Check possibleEmail against an email regex or treat 
			// account.name as an email address only for certain account.type values. 
			Email = account.name; 
			break;
		} 
	}
	
	private void renew() {
		Parameter = new HashMap<String, String>();
		Parameter.put("email", Email);
		Parameter.put("gps", getGPS());
	}
	
	private String getGPS() {
		Map<String, String> gps_params = new HashMap<String, String>();
		if(mGPS != null) {
			gps_params.put("lat", mGPS.getLatitude());
			gps_params.put("lon", mGPS.getLongitude());
		}
		JSONObject mJSONGPS = new JSONObject(gps_params);
		return mJSONGPS.toString();
	}
	
	public List<NameValuePair> getParams() {
		List<NameValuePair> mParams = new ArrayList<NameValuePair>();
		mJSONObjectParameter = new JSONObject(Parameter);
		mParams.add(new BasicNameValuePair("renew", mJSONObjectParameter.toString()));	
		return mParams;
	}
}
