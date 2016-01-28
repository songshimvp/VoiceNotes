package songshi.voicenotes.recorder;


import songshi.voicenotes.recorder.AudioManage.AudioStateListenter;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.voicenotes.R;


public class AudioRecorderButton extends Button implements AudioStateListenter {

	private static final int STATE_NORMAL = 1;           //默认状态
	private static final int STATE_RECORDERING = 2;      //录音状态
	private static final int STATE_WANT_TO_CALCEL = 3;   //取消状态

	private int mCurState = STATE_NORMAL;  //当前状态
	private boolean isRecordering = false; // 是否已经开始录音
	private boolean mReady; // 是否触发onLongClick

	private static final int DISTANCE_Y_CANCEL = 50;

	private AudioDialogManage audioDialogManage;

	private AudioManage mAudioManage;
	
	//构造方法
	public AudioRecorderButton(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		audioDialogManage = new AudioDialogManage(getContext());

		String dir = Environment.getExternalStorageDirectory()
				+ "/VoiceRecorder";                             // 此处需要判断是否有存储卡(外存)
		mAudioManage = AudioManage.getInstance(dir);
		mAudioManage.setOnAudioStateListenter(this);

		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				mReady = true;
				// 真正显示应该在audio end prepared以后
				mAudioManage.prepareAudio();
				//return true;
				return false;
			}
		});
		// TODO Auto-generated constructor stub
	}

	//录音完成后的回调
	public interface AudioFinishRecorderListenter{
		void onFinish(float seconds,String FilePath);
	}
	
	private AudioFinishRecorderListenter mListenter;
	
	public void setAudioFinishRecorderListenter(AudioFinishRecorderListenter listenter){
		this.mListenter=listenter;
	}
	
	//复写onTouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();   //获取当前Action
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDERING);
			break;

		case MotionEvent.ACTION_MOVE:

			// 根据X、Y的坐标，判断是否想要取消
			if (isRecordering) {
				if (wantToCancel(x, y)) {
					changeState(STATE_WANT_TO_CALCEL);
				} else {
					changeState(STATE_RECORDERING);
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			if (!mReady) {   //没有触发onLongClick
				reset();
				return super.onTouchEvent(event);
			}

			if (!isRecordering || mTime < 0.7f) {
				audioDialogManage.tooShort();
				mAudioManage.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 延迟，1.3秒以后关闭时间过短对话框
			} 
			/*else if (mTime < 0.5f) {
				audioDialogManage.tooShort();
				isRecordering=false;
				mAudioManage.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 延迟
			}*/

			else if (mCurState == STATE_RECORDERING) { //正常录制结束
				audioDialogManage.dimissDialog();
				// release
				mAudioManage.release();
				// callbackToAct
				if(mListenter!=null){
					mListenter.onFinish(mTime, mAudioManage.getCurrentFilePath());
				}				
				
			} else if (mCurState == STATE_WANT_TO_CALCEL) {
				// cancel
				audioDialogManage.dimissDialog();
				mAudioManage.cancel();
			}

			reset();
			break;
		}
		return super.onTouchEvent(event);
	}

	// 恢复状态以及一些标志位
	private void reset() {
		isRecordering = false;
		mReady = false;
		mTime = 0;
		changeState(STATE_NORMAL);
	}

	private boolean wantToCancel(int x, int y) {
		// 判断手指的滑动是否超出范围
		if (x < 0 || x > getWidth()) {
			return true;
		}
		if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
			return true;
		}
		return false;
	}

	//改变Button的背景和文本
	private void changeState(int state) {
		if (mCurState != state) {
			mCurState = state;
			switch (state) {
			case STATE_NORMAL:
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.str_recorder_normal);
				break;

			case STATE_RECORDERING:
				setBackgroundResource(R.drawable.btn_recorder_recordering);
				setText(R.string.str_recorder_recording);
				if (isRecordering) {
					// 更新Dialog.recording()
					audioDialogManage.recording();
				}
				break;

			case STATE_WANT_TO_CALCEL:
				setBackgroundResource(R.drawable.btn_recorder_recordering);
				setText(R.string.str_recorder_want_cancel);
				// 更新Dialog.wantCancel()
				audioDialogManage.wantToCancel();
				break;
			}
		}
	}

	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGE = 0x111;
	private static final int MSG_DIALOG_DIMISS = 0x112;

	private float mTime;

	// 获取音量大小的Runnable
	private Runnable mGetVoiceLevelRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRecordering) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:
				audioDialogManage.showRecorderingDialog();
				isRecordering = true;

				new Thread(mGetVoiceLevelRunnable).start();
				break;

			case MSG_VOICE_CHANGE:
				audioDialogManage.updateVoiceLevel(mAudioManage
						.getVoiceLevel(7));
				break;

			case MSG_DIALOG_DIMISS:
				audioDialogManage.dimissDialog();
				break;
			}
		};
	};

	@Override
	public void wellPrepared() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
	}
}
