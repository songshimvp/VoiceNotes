package com.example.voicenotes;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.voicenotes.MainActivity.Recorder;

public class AudioRecorderAdapter extends ArrayAdapter<Recorder> {

	private List<Recorder> mDatas;
	private Context mContext;
	
	private int mMinItemWidth;
	private int mMaxItemWidth;

	private LayoutInflater mInflater;

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

		mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
		mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.tools_voice_notes_recorder_item, parent, false);
			holder=new ViewHolder();
			holder.seconds=(TextView) convertView.findViewById(R.id.recorder_time);
			holder.mLength=convertView.findViewById(R.id.recorder_length);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.seconds.setText(Math.round(getItem(position).getTime())+"\"");   //录音的时间的显示
		ViewGroup.LayoutParams lp=holder.mLength.getLayoutParams();
		lp.width=(int) (mMinItemWidth+(mMaxItemWidth/60f * getItem(position).getTime()));
		return convertView;
	}

	private class ViewHolder {
		TextView seconds;
		View mLength;
	}
}
