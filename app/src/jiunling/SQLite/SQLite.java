package jiunling.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {
	
	/***	Debugging	***/
//	private static final String TAG = "SQLite";
//	private static final boolean D = true;
	
	/***	SQLite initial	***/
	private static final String DATABASE_NAME = "WifiPass.db";	//��Ʈw�W��
	private static final int DATABASE_VERSION = 1;	//��Ʈw����

	/***	SQLite Database	***/
	public SQLiteDatabase db;
	public final String RouterList = "routerList";
	
	public SQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		if(D) Log.e(TAG, "SQLite -- onCreate --");
		String DATABASE_RouterList_TABLE =
			    "create table "+ RouterList +" (" +
			        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			        "jo454 	TEXT, " +
			        "au4a83 TEXT " +
			        ");";
		//�إ�config��ƪ�A�Ա��аѦ�SQL�y�k
		db.execSQL(DATABASE_RouterList_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+RouterList);		//�R���¦�����ƪ�
		onCreate(db);
	}
}
