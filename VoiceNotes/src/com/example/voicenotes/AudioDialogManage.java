package com.example.voicenotes;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AudioDialogManage {
	private Dialog mDialog;

	private ImageView mIcon;
	private ImageView mVoice;

	private TextView mLabel;

	private Context mContext;

	public AudioDialogManage(Context context) {
		this.mContext = context;
	}

	//默认的对话框的显示
	public void showRecorderingDialog() {
		mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(
				R.layout.tools_voice_notes_dialog_recorder, null);
		mDialog.setContentView(view);

		mIcon = (ImageView) mDialog.findViewById(R.id.recorder_dialog_icon);
		mVoice = (ImageView) mDialog.findViewById(R.id.recorder_dialog_voice);
		mLabel = (TextView) mDialog.findViewById(R.id.recorder_dialog_label);

		mDialog.show();
	}

	//正在录音时，Dialog的显示
	public void recording() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLabel.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.drawable.recorder);
			mLabel.setText("手指滑动，取消录音");
		}
	}

	public void wantToCancel() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLabel.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.drawable.cancel);
			mLabel.setText("松开手指，取消录音");
		}
	}

	//录音时间过短
	public void tooShort() {
		if (mDialog != null && mDialog.isShowing()) {
			
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLabel.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.voice_to_short);
			mLabel.setText("录音时间过短");
		}
	}

	public void dimissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}	
	}

	//通过Level更新Voice的图片V1——V7
	public void updateVoiceLevel(int level) {
		if (mDialog != null && mDialog.isShowing()) {
			//mIcon.setVisibility(View.VISIBLE);
			//mVoice.setVisibility(View.VISIBLE);
			//mLabel.setVisibility(View.VISIBLE);
			
			int resId=mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
			mVoice.setImageResource(resId);
		}
	}
}
