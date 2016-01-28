package songshi.voicenotes.DB;

import android.graphics.drawable.Drawable;

public class VoiceNotes {

	private Long id; // Ö÷¼ü
	private String notes_datetime; // Â¼ÒôÊ±¼ä
	private String notesfile_path;
	private String reminder_date;
	private String reminder_time;
	private String reminder_text;
	private String reminder_location;
	private Drawable reminder_photo;

	public VoiceNotes() {
	}

	public VoiceNotes(String notes_datetime, String notesfile_path) {
		this.notes_datetime = notes_datetime;
		this.notesfile_path = notesfile_path;
	}

	public VoiceNotes(String notes_datetime, String notesfile_path,
			String reminder_date, String reminder_time, String reminder_text,
			String reminder_location, Drawable reminder_photo) {

		this(notes_datetime, notesfile_path);
		this.reminder_date = reminder_date;
		this.reminder_time = reminder_time;
		this.reminder_text = reminder_text;
		this.reminder_location = reminder_location;
		this.reminder_photo = reminder_photo;
	}

	@Override
	public String toString() {
		return "VoiceNotes [id=" + id + ", notes_datetime=" + notes_datetime
				+ ", notesfile_path=" + notesfile_path + ", reminder_date="
				+ reminder_date + ", reminder_time=" + reminder_time
				+ ", reminder_text=" + reminder_text + ", reminder_location="
				+ reminder_location + ", reminder_photo=" + reminder_photo
				+ "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNotes_datetime() {
		return notes_datetime;
	}

	public void setNotes_datetime(String notes_datetime) {
		this.notes_datetime = notes_datetime;
	}

	public String getNotesfile_path() {
		return notesfile_path;
	}

	public void setNotesfile_path(String notesfile_path) {
		this.notesfile_path = notesfile_path;
	}

	public String getReminder_date() {
		return reminder_date;
	}

	public void setReminder_date(String reminder_date) {
		this.reminder_date = reminder_date;
	}

	public String getReminder_time() {
		return reminder_time;
	}

	public void setReminder_time(String reminder_time) {
		this.reminder_time = reminder_time;
	}

	public String getReminder_text() {
		return reminder_text;
	}

	public void setReminder_text(String reminder_text) {
		this.reminder_text = reminder_text;
	}

	public String getReminder_location() {
		return reminder_location;
	}

	public void setReminder_location(String reminder_location) {
		this.reminder_location = reminder_location;
	}

	public Drawable getReminder_photo() {
		return reminder_photo;
	}

	public void setReminder_photo(Drawable reminder_photo) {
		this.reminder_photo = reminder_photo;
	}

}
