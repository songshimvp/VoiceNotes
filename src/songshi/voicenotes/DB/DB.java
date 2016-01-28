package songshi.voicenotes.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	/**
	 * @param context
	 * @param name
	 *            :数据库名称
	 * @param factory
	 *            ：游标工厂
	 * @param version
	 *            ：数据库版本
	 */
	public DB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DB(Context context) {
		this(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DBInfo.Table.CREATE_NOTES_TABLE);
	}

	/**
	 * 更新数据库
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DBInfo.Table.DROP_NOTES_TABLE); // 删除表（应该先备份）

		onCreate(db); // 重新创建
	}

}
