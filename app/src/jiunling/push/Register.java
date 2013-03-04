package jiunling.push;

import static jiunling.config.config.Email;
import static jiunling.config.config.RegistrarId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jiunling.gps.AGPS;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class Register {
	
	/***	Debugging	***/
//	private static final String TAG = "Register";
//	private static final boolean D = true;
	
	private Map<String, String> Parameter;
	private JSONObject mJSONObjectParameter;
	
	private Context mContext;
	
	public Register(Context mContext) {
		this.mContext = mContext;
		getUserEmail();
		reg();
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
	
	private void reg() {
		Parameter = new HashMap<String, String>();
		Parameter.put("email", Email);
		Parameter.put("gps", getGPS());
		Parameter.put("gcm", RegistrarId);
	}
	
	private String getGPS() {
		Map<String, String> gps_params = new HashMap<String, String>();
		AGPS mAGPS = new AGPS(mContext);
		gps_params.put("lat", mAGPS.getLatitude());
		gps_params.put("long", mAGPS.getLongitude());
		JSONObject mJSONGPS = new JSONObject(gps_params);
		return mJSONGPS.toString();
	}
	
	public List<NameValuePair> getParams() {
		List<NameValuePair> mParams = new ArrayList<NameValuePair>();
		mJSONObjectParameter = new JSONObject(Parameter);
		mParams.add(new BasicNameValuePair("reg", mJSONObjectParameter.toString()));	
		return mParams;
	}
	
}
