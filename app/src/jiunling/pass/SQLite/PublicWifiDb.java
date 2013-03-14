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
				PublicWifi,								//��ƪ�W��
				new String[] {"SSID", "MAC", "GPS"},	//���W��
				null,									//WHERE
				null, 									// WHERE ���Ѽ�
				null, 									// GROUP BY
				null, 									// HAVING
				null, 									// ORDOR BY
				null  									// ����^�Ǫ�rows�ƶq
		);
	 
		// �`�N�G���g�|�X��
		if (cursor != null) {
			cursor.moveToFirst();	//�N���в���Ĥ@�����
		}
		return cursor;
	}
	
	public Cursor SWhereCount(String[] MAC) throws SQLException {
		Cursor cursor = db.query(true,
				PublicWifi,								//��ƪ�W��
				null,	//���W��
				"MAC=?",								//WHERE
				MAC, 									// WHERE ���Ѽ�
				null, 									// GROUP BY
				null, 									// HAVING
				null, 									// ORDOR BY
				null  									// ����^�Ǫ�rows�ƶq
		);
	 
		// �`�N�G���g�|�X��
		if (cursor != null) {
			cursor.moveToFirst();	//�N���в���Ĥ@�����
		}
		return cursor;
	}
}
