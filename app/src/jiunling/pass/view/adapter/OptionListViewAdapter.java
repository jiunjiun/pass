package jiunling.pass.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class OptionListViewAdapter {
	
	/***	Debugging	***/
//	private static final String TAG = "CalendarListViewAdapter";
//	private static final boolean D = true;
	
	private Activity mActivity;
	private ListView mListView;
	
	public OptionListViewAdapter(Activity mActivity, ListView mListView) {
		this.mActivity = mActivity;
		this.mListView = mListView;
	}
	
	public static class ViewHolder {
		
	}
	
	class SimpleAdapter extends BaseAdapter implements OnItemClickListener {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}  
		
	}
}
