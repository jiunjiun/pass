package jiunling.pass.view.adapter;

import static jiunling.pass.wifi.Environment.WifiConnectReceiver;
import java.util.ArrayList;
import java.util.HashMap;

import jiunling.pass.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;



public class WifiDialogListViewAdapter {
	
	/***	Debugging	***/
	private static final String TAG = "WifiDialogListViewAdapter";
	private static final boolean D = true;
	
	private Activity mActivity;
	private ListView mListView;
	private ArrayList<HashMap<String, String>> palnlist;
	
	public WifiDialogListViewAdapter(final Activity mActivity, ListView mListView) {
		this.mActivity 	= mActivity;
		this.mListView 	= mListView;
		palnlist 		= new ArrayList<HashMap<String, String>>();

	}
	
	public void Update(String params) {
		try {
			JSONObject mJSONObject = new JSONObject(params);
			JSONArray mJSONArray = new JSONArray(mJSONObject.getString("Wifi"));
			for (int i = 0; i < mJSONArray.length(); i++) {
				mJSONObject = mJSONArray.getJSONObject(i);
				
				HashMap<String, String> map = new HashMap<String, String>();  
				map.put("SSID", mJSONObject.getString("SSID"));
				map.put("BSSID", mJSONObject.getString("BSSID"));
				map.put("level", mJSONObject.getString("level"));
				map.put("capabilities", mJSONObject.getString("capabilities"));
				map.put("au4a83", mJSONObject.getString("au4a83"));
		        palnlist.add(map);
			}
			
			SimpleAdapter mSimpleAdapter = new SimpleAdapter(mActivity, palnlist);	
			mListView.setAdapter(mSimpleAdapter);
			mListView.setOnItemClickListener(mSimpleAdapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class ViewHolder {
		private TextView WifiName;
		private ImageButton WifiInfo;
	}
	
	class SimpleAdapter extends BaseAdapter implements OnItemClickListener {
		
		private Context mContext;
		private ArrayList<HashMap<String, String>> mData;  
	    private LayoutInflater inflater = null;  
	    
		public SimpleAdapter(Context mContext,  ArrayList<HashMap<String, String>> data) {
			this.mContext = mContext;
				 inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 	mData = data;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position); 
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;  
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder  mViewHolder;
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.lv_wifi_dialog, null);
				mViewHolder = new ViewHolder();
				mViewHolder.WifiName = ((TextView) convertView.findViewById(R.id.WifiName));
				mViewHolder.WifiInfo = ((ImageButton) convertView.findViewById(R.id.WifiInfo));
				
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			
			mViewHolder.WifiName.setText( mData.get(position).get("SSID") );
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
			if(D) Log.e(TAG, "mData.get(position): "+new JSONObject(mData.get(position)));
			Intent mIntent = new Intent(WifiConnectReceiver);
			mIntent.putExtra("Wifi", new JSONObject(mData.get(position)).toString());
			mContext.sendBroadcast(mIntent);
			mActivity.finish();
		}  
		
	}
}
