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
	
//	public static final int Option_ListView  = 1;
	
	public AdapterInterface(Activity activity , ListView mListView){
		this.mActivity = activity;
		this.mListView = mListView;
	}
	
	public void ListViewKey(int Kind) {
//		switch(Kind) {
//		case Option_ListView:
//			OptionListView();
//			break;
//		}
	}
	
	private void OptionListView() {
//		OptionListViewAdapter mOptionListViewAdapter = 
//				new OptionListViewAdapter(mActivity, mListView);
	}
}
