package jiunling.pass.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class PublicWifiDb extends SQLite {

	public PublicWifiDb(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/***	Wifi	***/
	public long Create(String SSID, String MAC, String GPS){
		int Count = SWhereCount(new String[]{MAC}).getCount();
		
		if(Count == 0) {
			ContentValues args = new ContentValues();
			args.put("SSID", SSID);
			args.put("MAC", MAC);
			args.put("GPS", GPS);
			
			return db.insert(PublicWifi, null, args);
		} 
		return 0;
	}
	
	public Cursor Select() throws SQLException {
		Cursor cursor = db.query(true,
				PublicWifi,								//資料表名稱
				new String[] {"SSID", "MAC", "GPS"},	//欄位名稱
				null,									//WHERE
				null, 									// WHERE 的參數
				null, 									// GROUP BY
				null, 									// HAVING
				null, 									// ORDOR BY
				null  									// 限制回傳的rows數量
		);
	 
		// 注意：不寫會出錯
		if (cursor != null) {
			cursor.moveToFirst();	//將指標移到第一筆資料
		}
		return cursor;
	}
	
	public Cursor SWhereCount(String[] MAC) throws SQLException {
		Cursor cursor = db.query(true,
				PublicWifi,								//資料表名稱
				null,	//欄位名稱
				"MAC=?",								//WHERE
				MAC, 									// WHERE 的參數
				null, 									// GROUP BY
				null, 									// HAVING
				null, 									// ORDOR BY
				null  									// 限制回傳的rows數量
		);
	 
		// 注意：不寫會出錯
		if (cursor != null) {
			cursor.moveToFirst();	//將指標移到第一筆資料
		}
		return cursor;
	}
}
