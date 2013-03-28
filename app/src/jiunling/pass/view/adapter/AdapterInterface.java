package jiunling.pass.view.adapter;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

public class AdapterInterface {
	/***	Debugging	***/
	private static final String TAG = "AdapterInterface";
	private static final boolean D = true;
	
	private Activity mActivity;
	
	/**		layout	**/
	private ListView mListView;
	
	private int Kind;
	public static final int WifiListDialog_ListView  = 1;
	
	private WifiDialogListViewAdapter mWifiListDialogListViewAdapter = null;
	
	public AdapterInterface(Activity activity , ListView mListView){
		this.mActivity = activity;
		this.mListView = mListView;
	}
	
	public AdapterInterface(Activity activity , ListView mListView, int Kind){
		this.mActivity 	= activity;
		this.mListView 	= mListView;
		this.Kind		= Kind;
		ListViewKey(Kind);
	}
	
	public void ListViewKey(int Kind) {
		switch(Kind) {
		case WifiListDialog_ListView:
			WifiListDialog();
			break;
		}
	}
	
	public void Update(String Params) {
		switch(Kind) {
		case WifiListDialog_ListView:
			if(D) Log.e(TAG, "WifiListDialog_ListView");
			if(mWifiListDialogListViewAdapter != null) mWifiListDialogListViewAdapter.Update(Params);
			break;
		}
	}
	
	private void WifiListDialog() {
		mWifiListDialogListViewAdapter = new WifiDialogListViewAdapter(mActivity, mListView);
	}
}
