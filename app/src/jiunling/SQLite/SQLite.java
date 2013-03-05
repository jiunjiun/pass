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
	private static final String DATABASE_NAME = "BaoBao.db";	//��Ʈw�W��
	private static final int DATABASE_VERSION = 2;	//��Ʈw����

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
		//�إ�config��ƪ�A�Ա��аѦ�SQL�y�k
		db.execSQL(DATABASE_RouterList_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+RouterList);		//�R���¦�����ƪ�
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
				RouterList,					//��ƪ�W��
				new String[] {"au4a83"},	//���W��
				"jo454=?",					//WHERE
				jo454, 						// WHERE ���Ѽ�
				null, 						// GROUP BY
				null, 						// HAVING
				null, 						// ORDOR BY
				null  						// ����^�Ǫ�rows�ƶq
		);
	 
		// �`�N�G���g�|�X��
		if (cursor != null) {
			cursor.moveToFirst();	//�N���в���Ĥ@�����
		}
		return cursor;
	}
	/*************************************************************************************/
	
}
