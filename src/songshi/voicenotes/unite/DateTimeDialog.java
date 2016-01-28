package songshi.voicenotes.unite;

import java.util.Calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.voicenotes.R;


public class DateTimeDialog {

	private Context mContext;
	private LayoutInflater mInflater;
	private View dateTimeView;
	
	private TextView textViewDate, textViewTime;
	private DatePicker datePicker;
	private TimePicker timePicker;
	
	private Calendar mCalendar;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	
	public DateTimeDialog(Context context){
		this.mContext=context;
		mInflater=LayoutInflater.from(mContext);
		dateTimeView=mInflater.inflate(R.layout.datetime_dialog, null);
		
		textViewDate=(TextView) dateTimeView.findViewById(R.id.reminderDateText);
		textViewTime=(TextView) dateTimeView.findViewById(R.id.reminderTimeText);
		datePicker=(DatePicker) dateTimeView.findViewById(R.id.reminderDatePicker);
		timePicker=(TimePicker) dateTimeView.findViewById(R.id.reminderTimePicker);
	}
	
	public void DateTimeDialogShow() {
		
		//CustomDialog是自定义对话框
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		//builder.setMessage("这个就是自定义的时间提示框");
		
		builder.setContentView(dateTimeView);
		builder.setTitle("备忘提醒");
		
		initDateTime();
		setDateTime();
		
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
				// 设置日期、时间的Text
				// 存入数据库
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 返回
						
					}
				});

		builder.create().show();
	}
	
	public void initDateTime(){
		mCalendar=Calendar.getInstance();
		
		year=mCalendar.get(Calendar.YEAR);
		month=mCalendar.get(Calendar.MONTH)+1;
		day=mCalendar.get(Calendar.DAY_OF_MONTH);
		hour=mCalendar.get(Calendar.HOUR_OF_DAY);
		minute=mCalendar.get(Calendar.MINUTE);
		
		textViewDate.setText(year+"年"+month+"月"+day+"日");
		textViewTime.setText(hour+"时"+minute+"分");
	}
	
	public void setDateTime(){
		
		datePicker.init(year, mCalendar.get(Calendar.MONTH), day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				textViewDate.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
			}
		});
		
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				//mCalendar.set(Calendar.MINUTE, minute);
				textViewTime.setText(hourOfDay+"时"+minute+"分");
			}
		});
	}
	
	
}
