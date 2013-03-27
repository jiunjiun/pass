package jiunling.pass.view.adapter;

import android.app.Activity;
import android.widget.ListView;

public class AdapterInterface {
	/***	Debugging	***/
//	private static final String TAG = "AdapterInterface";
//	private static final boolean D = true;
	
	private Activity mActivity;
	
	/**		layout	**/
	private ListView mListView;
	
	public static final int WifiListDialog_ListView  = 1;
	
	public AdapterInterface(Activity activity , ListView mListView){
		this.mActivity = activity;
		this.mListView = mListView;
	}
	
	public AdapterInterface(Activity activity , ListView mListView, int Kind){
		this.mActivity = activity;
		this.mListView = mListView;
		ListViewKey(Kind);
	}
	
	public void ListViewKey(int Kind) {
		switch(Kind) {
		case WifiListDialog_ListView:
			WifiListDialog();
			break;
		}
	}
	
	private void WifiListDialog() {
		WifiDialogListViewAdapter mWifiListDialogListViewAdapter = 
				new WifiDialogListViewAdapter(mActivity, mListView);
	}
}
