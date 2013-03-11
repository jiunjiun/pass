package jiunling.pull;

import jiunling.SQLite.WiFiDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class StoreWifi {
	/***	Debugging	***/
	private static final String TAG = "PushService";
	private static final boolean D = true;
	
	private Context mContext;
	
	public StoreWifi(Context mContext){
		this.mContext = mContext;
	}
	
	public void Save(String Data) {
		WiFiDb dbHelper = new WiFiDb(mContext);
		
		dbHelper.DeleteDB();
		try {
			JSONArray mJSONArray = new JSONArray(Data);
			for(int i=0;i<mJSONArray.length();i++) {
				JSONObject mJSONObject = mJSONArray.getJSONObject(i);
	            String jo454 = mJSONObject.getString("jo454");
	            String au4a83 = mJSONObject.getString("au4a83") != "null" ? mJSONObject.getString("au4a83") : "";
				if(D) Log.e(TAG, "jo454: "+ jo454 + " au4a83: "+ au4a83);
				dbHelper.Create(jo454, au4a83);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbHelper.close();	//關閉資料庫，釋放記憶體
	}
}
