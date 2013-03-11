package jiunling.pass.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class WiFiDb extends SQLite {
	
	/***	Debugging	***/
//	private static final String TAG = "WiFiDb";
//	private static final boolean D = true;
	
	public WiFiDb(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/***	Wifi	***/
	public long Create(String jo454, String au4a83){
		ContentValues args = new ContentValues();
		args.put("jo454", jo454);
		args.put("au4a83", au4a83);

		return db.insert(RouterList, null, args);
	}
	
	public Cursor SWhere(String[] jo454) throws SQLException {
		Cursor cursor = db.query(true,
				RouterList,					//資料表名稱
				new String[] {"au4a83"},	//欄位名稱
				"jo454=?",					//WHERE
				jo454, 						// WHERE 的參數
				null, 						// GROUP BY
				null, 						// HAVING
				null, 						// ORDOR BY
				null  						// 限制回傳的rows數量
		);
	 
		// 注意：不寫會出錯
		if (cursor != null) {
			cursor.moveToFirst();	//將指標移到第一筆資料
		}
		return cursor;
	}
	
	public long DeleteDB(){
		return db.delete(RouterList, null, null);
	}
}
