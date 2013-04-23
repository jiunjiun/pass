package jiunling.pass.push;

import static jiunling.pass.service.BackgroundService.mGPS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class RegisterWifi {
	
	/***	Debugging	***/
//	private static final String TAG = "RegisterWifi";
//	private static final boolean D = true;
	
	private Map<String, String> Parameter;
	private JSONObject mJSONObjectParameter;
	
	String[] mParameter;
		
	public RegisterWifi(String[] mParameter) {
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
		mParams.add(new BasicNameValuePair("wifi", mJSONObjectParameter.toString()));	
		return mParams;
	}
}
