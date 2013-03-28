package jiunling.pass.view;

import static jiunling.pass.view.adapter.AdapterInterface.WifiListDialog_ListView;
import jiunling.pass.R;
import jiunling.pass.view.adapter.AdapterInterface;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;


public class WifiDialog extends SherlockFragmentActivity {
	/***	Debugging	***/
//	private static final String TAG = "WifiListDialog";
//	private static final boolean D = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);
		init();
	}
	
	private void init() {
		String WifiList = getIntent().getStringExtra("WifiList");
		
		ListView mListView = (ListView) findViewById(R.id.ListView);
		AdapterInterface mAdapterInterface = new AdapterInterface(this, mListView, WifiListDialog_ListView);
		mAdapterInterface.Update(WifiList);
    }
}
