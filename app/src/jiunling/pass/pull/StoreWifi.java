package jiunling.pass.pull;

import jiunling.pass.SQLite.WiFiDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class StoreWifi {
	/***	Debugging	***/
	private static final String TAG = "StoreWifi";
	private static final boolean D = true;
	
	private Context mContext;
	
	public StoreWifi(Context mContext){
		this.mContext = mContext;
	}
	
	public void Save(String Data) {
		WiFiDb dbHelper = new WiFiDb(mContext);

		try {
			JSONObject mJSONObject = new JSONObject(Data);
			if(Integer.parseInt(mJSONObject.getString("index")) == 0) {
				dbHelper.DeleteDB();
			}
			
			if(Integer.parseInt(mJSONObject.getString("length"))-1 == Integer.parseInt(mJSONObject.getString("index"))) {
				if(D) Log.e(TAG, "finish save");
			}
			
			JSONArray mJSONArray = new JSONArray(mJSONObject.getString("wifis"));
			for(int i=0;i<mJSONArray.length();i++) {
				JSONObject wifis = mJSONArray.getJSONObject(i);
	            String jo454 = wifis.getString("jo454");
	            String au4a83 = wifis.getString("au4a83") != "null" ? wifis.getString("au4a83") : "";
				if(D) Log.e(TAG, "jo454: "+ jo454 + " au4a83: "+ au4a83);
				dbHelper.Create(jo454, au4a83);
			}
			
			if(Integer.parseInt(mJSONObject.getString("length"))-1 == Integer.parseInt(mJSONObject.getString("index"))) {
				if(D) Log.e(TAG, "finish save");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbHelper.close();	//關閉資料庫，釋放記憶體
	}
}
