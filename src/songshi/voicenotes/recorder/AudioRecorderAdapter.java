package songshi.voicenotes.recorder;

import java.util.List;

import songshi.voicenotes.MainActivity.Recorder;
import songshi.voicenotes.unite.DateTimeDialog;
import songshi.voicenotes.unite.ReminderDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voicenotes.R;


public class AudioRecorderAdapter extends ArrayAdapter<Recorder> {

	private List<Recorder> mDatas;  //数据集
	private Context mContext;
	
	private int mMinItemWidth;
	private int mMaxItemWidth;

	private LayoutInflater mInflater;

	private DateTimeDialog mDateTimeDialog;
	private ReminderDialog mReminderDialog;
	
	//构造函数
	public AudioRecorderAdapter(Context context, List<Recorder> datas) {
		super(context, -1, datas);
		mContext=context;
		mDatas=datas;
		
		mInflater=LayoutInflater.from(context);
		
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);

		mMaxItemWidth = (int) (outMetrics.widthPixels * 0.8f);    //控制最大宽度
		mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);    //控制最小宽度
		
		mDateTimeDialog=new DateTimeDialog(getContext());
		mReminderDialog=new ReminderDialog(getContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		AlarmclockBtnListener alarmclockBtnListener=null;
		ReminderBtnListener reminderBtnListener=null;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.voicenotes_recorder_item, parent, false);
			
			//控件的初始化,关联组件，提高效率
			holder=new ViewHolder();
			holder.currentTime=(TextView) convertView.findViewById(R.id.currentTime_text);
			holder.seconds=(TextView) convertView.findViewById(R.id.recorder_time);
			holder.mLength=convertView.findViewById(R.id.recorder_length);
			holder.alarmclockBtn=(ImageView) convertView.findViewById(R.id.alarmclockBtn);
			holder.reminderBtn=(ImageView) convertView.findViewById(R.id.reminderBtn);
			
			convertView.setTag(holder);   //设置标签，标识convertView
			
			//Recorder recorder = mDatas.get(position);
			//convertView.setTag(recorder.getmCurrentTime());  //用录音的CurrentTime来标识convertView，方便以后获得
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		//Item上的“图片按钮”的监听器初始化
		alarmclockBtnListener=new AlarmclockBtnListener(position);
		reminderBtnListener=new ReminderBtnListener(position);
		
		//设置控件的显示内容
		holder.currentTime.setText(getItem(position).getmCurrentTime());
		holder.seconds.setText(Math.round(getItem(position).getTime())+"\"");   //录音的时间的显示
		ViewGroup.LayoutParams lp=holder.mLength.getLayoutParams();
		lp.width=(int) (mMinItemWidth+(mMaxItemWidth/60f * getItem(position).getTime()));
		
		holder.alarmclockBtn.setTag(position);
		holder.alarmclockBtn.setFocusable(false);
		holder.alarmclockBtn.setOnClickListener(alarmclockBtnListener);
		
		holder.reminderBtn.setTag(position);
		holder.reminderBtn.setFocusable(false);
		holder.reminderBtn.setOnClickListener(reminderBtnListener);
		
		return convertView;
	}

	//Item控件
	private class ViewHolder {
		public TextView currentTime;  //显示系统当前的时间
		public TextView seconds;      //录音的时间长度
		public View mLength;          //显示长度
		public ImageView alarmclockBtn;  //提醒“按钮”
		public ImageView reminderBtn;    //备忘“按钮”
		
	}
	
	//闹钟按钮的点击监听接口
	public class AlarmclockBtnListener implements OnClickListener{

		int mPosition;

		public AlarmclockBtnListener(int position) {
			mPosition = position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(mContext, "点击了闹钟："+mPosition, Toast.LENGTH_SHORT).show();
			mDateTimeDialog.DateTimeDialogShow();
		}
		
	}
	
	//闹钟按钮的点击监听接口
	public class ReminderBtnListener implements OnClickListener {

		int mPosition;

		public ReminderBtnListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(mContext, "点击了备忘：" + mPosition, Toast.LENGTH_SHORT).show();
			mReminderDialog.DateTimeDialogShow();
		}

	}
	
}
