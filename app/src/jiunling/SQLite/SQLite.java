package jiunling.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLite extends SQLiteOpenHelper {
	
	/***	Debugging	***/
	private static final String TAG = "SQLite";
	private static final boolean D = true;
	
	/***	SQLite initial	***/
	private static final String DATABASE_NAME = "BaoBao.db";	//資料庫名稱
	private static final int DATABASE_VERSION = 2;	//資料庫版本

	/***	SQLite Database	***/
	public SQLiteDatabase db;
	private static final String RouterList = "routerList";
	
	public SQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		if(D) Log.e(TAG, "-- onCreate --");
		String DATABASE_RouterList_TABLE =
			    "create table "+ RouterList +" (" +
			        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			        "jo454 	VARCHAR(255), " +
			        "au4a83 VARCHAR(255), " +
			        ");";
		//建立config資料表，詳情請參考SQL語法
		db.execSQL(DATABASE_RouterList_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+RouterList);		//刪除舊有的資料表
		onCreate(db);
	}
	
	
	/*************************************************************************************/
	/***	User	***/
	public long Create(String jo454, String au4a83){
		ContentValues args = new ContentValues();
		args.put("jo454", jo454);
		args.put("au4a83", au4a83);
		
		return db.insert(RouterList, null, args);
	}
	
	public Cursor SWhere(String jo454[]) throws SQLException {
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
	/*************************************************************************************/
	
}
