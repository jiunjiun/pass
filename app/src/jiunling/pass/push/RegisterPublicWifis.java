package jiunling.pass.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jiunling.pass.SQLite.PublicWifiDb;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class RegisterPublicWifis {
	
	/***	Debugging	***/
	private static final String TAG = "RegisterPublicWifis";
	private static final boolean D = false;
	
	private Map<String, String> Parameter;
	private JSONObject mJSONObjectParameter;
	private JSONArray mJSONArray;
	
	private Context mContext;
		
	public RegisterPublicWifis(Context mContext) {
		this.mContext = mContext;
		save();
	}
	
	private void save() {
		mJSONArray = new JSONArray();

		PublicWifiDb dbHelper = new PublicWifiDb(mContext);
		Cursor cursor 	= dbHelper.Select();
    	int rows_num 	= cursor.getCount();
    	if(D) Log.e(TAG, "rows_num: " + rows_num);
    	if(rows_num != 0){
			cursor.moveToFirst();			//將指標移至第一筆資料
			for(int i=0; i<rows_num; i++) {
				if(D) Log.e(TAG, "SSID " + cursor.getString(0));
				if(D) Log.e(TAG, "MAC " + cursor.getString(1));
				if(D) Log.e(TAG, "GPS " + cursor.getString(2));
				Parameter = new HashMap<String, String>();
				Parameter.put("SSID", cursor.getString(0));
				Parameter.put("MAC", cursor.getString(1));
				Parameter.put("GPS", cursor.getString(2));
				mJSONArray.put(new JSONObject(Parameter));
		        cursor.moveToNext();
			}
		}
    	cursor.close();		//關閉Cursor
		dbHelper.close();	//關閉資料庫，釋放記憶體
	
		Parameter = new HashMap<String, String>();
		Parameter.put("publicWifi", mJSONArray.toString());
	}
		
	public List<NameValuePair> getParams() {
		List<NameValuePair> mParams = new ArrayList<NameValuePair>();
		mJSONObjectParameter = new JSONObject(Parameter);
		if(mJSONArray.length() > 0) 
			mParams.add(new BasicNameValuePair("publicWifi", mJSONObjectParameter.toString()));	
		return mParams;
	}
}
