package songshi.voicenotes.DB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class VoiceNotesDao {

	private DB dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues contentValues;

	public VoiceNotesDao(Context context) {
		dbHelper = new DB(context);
	}

	// 插入（新增）
	public long insertVoiceNotes(VoiceNotes voiceNotes) {

		db = dbHelper.getWritableDatabase(); // 获得SQLiteDatabase

		contentValues = new ContentValues();
		contentValues.put(DBInfo.Table._NOTES_DATETIME, voiceNotes.getId());
		contentValues.put(DBInfo.Table._NOTESFILE_PATH,
				voiceNotes.getNotesfile_path());
		contentValues.put(DBInfo.Table._REMINDER_DATE,
				voiceNotes.getReminder_date());
		contentValues.put(DBInfo.Table._REMINDER_TIME,
				voiceNotes.getReminder_time());
		contentValues.put(DBInfo.Table._REMINDER_TEXT,
				voiceNotes.getReminder_text());
		contentValues.put(DBInfo.Table._REMINDER_LOCATION,
				voiceNotes.getReminder_location());

		// 将图片类型进行一定的转化处理,然后才能存储到BLOB类型中
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) voiceNotes
				.getReminder_photo();
		bitmapDrawable.getBitmap().compress(CompressFormat.PNG, 100,
				outputStream);// 将数据压缩成PNG编码数据
		contentValues.put(DBInfo.Table._REMINDER_PHOTO,
				outputStream.toByteArray());// 存储图片类型数据

		long rowID = db.insert(DBInfo.Table.VoiceNOTES_TABLE,
				DBInfo.Table._NOTES_DATETIME, contentValues);// 插入数据，返回一个行号

		db.close();

		return rowID;
	}

	// 更新
	public int updateVoiceNotes(VoiceNotes voiceNotes) {
		return 1;
	}

	// 删除
	public long deleteVoiceNotes(String notes_datetime) {
		return 1;
	}

	// 查找某一条语音备忘记录
	public VoiceNotes findVoiceNoteByNotesDateTime(String notes_datetime) {
		return null;
	}

	// 查找所有的语音备忘记录
	String[] columns = { DBInfo.Table._ID, DBInfo.Table._NOTES_DATETIME,
			DBInfo.Table._NOTESFILE_PATH, DBInfo.Table._REMINDER_DATE,
			DBInfo.Table._REMINDER_TIME, DBInfo.Table._REMINDER_TEXT,
			DBInfo.Table._REMINDER_LOCATION, DBInfo.Table._REMINDER_PHOTO }; // VoiceNotes字段表

	public List<VoiceNotes> findAllVoiceNotes() {
		db = dbHelper.getReadableDatabase();
		List<VoiceNotes> voiceNotesList = null;
		VoiceNotes voiceNotes = null;

		Cursor cursor = db.query(DBInfo.Table.VoiceNOTES_TABLE, columns, null,
				null, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			voiceNotesList = new ArrayList<VoiceNotes>(cursor.getCount());
			while (cursor.moveToNext()) {
				voiceNotes = new VoiceNotes();

				voiceNotes.setId(cursor.getLong(cursor
						.getColumnIndex(DBInfo.Table._ID)));
				voiceNotes.setNotes_datetime(cursor.getString(cursor
						.getColumnIndex(DBInfo.Table._NOTES_DATETIME)));
				voiceNotes.setNotesfile_path(cursor.getString(cursor
						.getColumnIndex(DBInfo.Table._NOTESFILE_PATH)));
				voiceNotes.setReminder_date(cursor.getString(cursor
						.getColumnIndex(DBInfo.Table._REMINDER_DATE)));
				voiceNotes.setReminder_time(cursor.getString(cursor
						.getColumnIndex(DBInfo.Table._REMINDER_TIME)));
				voiceNotes.setReminder_text(cursor.getString(cursor
						.getColumnIndex(DBInfo.Table._REMINDER_TEXT)));
				voiceNotes.setReminder_location(cursor.getString(cursor
						.getColumnIndex(DBInfo.Table._REMINDER_LOCATION)));
				
				//图片
				byte[] byteReminderPhoto = cursor.getBlob(cursor
						.getColumnIndex(DBInfo.Table._REMINDER_PHOTO));
				ByteArrayInputStream arrayInputStream=new ByteArrayInputStream(byteReminderPhoto);
				Drawable drawable = Drawable.createFromStream(arrayInputStream, "reminder_photo");
				voiceNotes.setReminder_photo(drawable);

				voiceNotesList.add(voiceNotes);
			}
		}
		cursor.close();
		db.close();
		return voiceNotesList;
	}
}
