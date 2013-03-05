package jiunling.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jiunling.gps.AGPS;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class RegisterWifi {
	
	/***	Debugging	***/
//	private static final String TAG = "RegisterWifi";
//	private static final boolean D = true;
	
	private Map<String, String> Parameter;
	private JSONObject mJSONObjectParameter;
	
	String[] mParameter;
	
	private Context mContext;
	
	public RegisterWifi(Context mContext, String[] mParameter) {
		this.mContext 	= mContext;
		this.mParameter = mParameter;
		save();
	}
	
	private void save() {
		Parameter = new HashMap<String, String>();
		String SSID, MAC, passwd;
		SSID 	= mParameter[0];
		MAC 	= mParameter[1];
		passwd 	= mParameter[2];
		
		Parameter.put("SSID", SSID);
		Parameter.put("MAC", MAC);
		Parameter.put("PSK", passwd);
		Parameter.put("gps", getGPS());
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
		mParams.add(new BasicNameValuePair("wifi", mJSONObjectParameter.toString()));	
		return mParams;
	}
}
