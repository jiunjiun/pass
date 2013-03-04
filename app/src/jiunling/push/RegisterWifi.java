package jiunling.push;

import static jiunling.config.config.Email;
import static jiunling.config.config.RegistrarId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class RegisterWifi {
	
	/***	Debugging	***/
	private static final String TAG = "RegisterWifi";
	private static final boolean D = true;
	
	private Map<String, String> Parameter;
	private JSONObject mJSONObjectParameter;
	
	
	public RegisterWifi(List<NameValuePair> mParams, String[] mParameter) {
		reg();
	}
	
	private void reg() {
		Parameter = new HashMap<String, String>();
		Parameter.put("email", Email);
		Parameter.put("gcm", RegistrarId);
	}
	
	public List<NameValuePair> getParams() {
		List<NameValuePair> mParams = new ArrayList<NameValuePair>();
		mJSONObjectParameter = new JSONObject(Parameter);
		mParams.add(new BasicNameValuePair("reg", mJSONObjectParameter.toString()));	
		return mParams;
	}
}
