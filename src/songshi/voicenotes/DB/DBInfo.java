package songshi.voicenotes.DB;


public class DBInfo {

	public static class DB {
		/**
		 * 数据库名称
		 */
		public static final String DB_NAME = "voicenotes.db";

		/**
		 * 数据库版本
		 */
		public static final int DB_VERSION = 1;

	}

	public static class Table {
		/**
		 * 录音记录表名称
		 */
		public static final String VoiceNOTES_TABLE = "voicenotes_info";

		public static final String _ID = "_id"; // 主键
		public static final String _NOTES_DATETIME = "notes_datetime"; // 录音时间
		public static final String _NOTESFILE_PATH = "notesfile_path";
		public static final String _REMINDER_DATE = "reminder_date";
		public static final String _REMINDER_TIME = "reminder_time";
		public static final String _REMINDER_TEXT = "reminder_text";
		public static final String _REMINDER_LOCATION = "reminder_location";
		public static final String _REMINDER_PHOTO = "reminder_photo";

		public static final String CREATE_NOTES_TABLE = "create table if not exists "
				+ VoiceNOTES_TABLE
				+ "("
				+ _ID
				+ " integer primary key autoincrement, "
				+ _NOTES_DATETIME
				+ " text, "
				+ _NOTESFILE_PATH
				+ " text, "
				+ _REMINDER_DATE
				+ " text, "
				+ _REMINDER_TIME
				+ " text, "
				+ _REMINDER_TEXT
				+ " text, "
				+ _REMINDER_LOCATION
				+ " text, "
				+ _REMINDER_PHOTO
				+ " BLOB ) ;";

		//删除表空间
		public static final String DROP_NOTES_TABLE = "drop table " +VoiceNOTES_TABLE;
		
		
	}
}
