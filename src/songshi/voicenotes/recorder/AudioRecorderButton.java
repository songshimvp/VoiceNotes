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


/**
 * 控制录音Button
 * @author songshi
 * 1、重写onTouchEvent；（changeState方法、wantToCancel方法、reset方法）；
 * 2、编写AudioDialogManage、并与该类AudioRecorderButton进行整合；
 * 3、编写AudioManage、并与该类AudioRecorderButton进行整合；
 */
public class AudioRecorderButton extends Button implements AudioStateListenter {

	/**
	 * AudioRecorderButton的三个状态
	 */
	private static final int STATE_NORMAL = 1;           //默认状态
	private static final int STATE_RECORDERING = 2;      //录音状态
	private static final int STATE_WANT_TO_CALCEL = 3;   //取消状态

	private int mCurState = STATE_NORMAL;    // 当前录音状态
	private boolean isRecordering = false;   // 是否已经开始录音
	private boolean mReady;    // 是否触发onLongClick

	private static final int DISTANCE_Y_CANCEL = 50;

	private AudioDialogManage audioDialogManage;

	private AudioManage mAudioManage;
	
	/**
	 * 正常录音完成后的回调
	 * @author songshi
	 *
	 */
	public interface AudioFinishRecorderListenter{
		void onFinish(float seconds, String FilePath);
	}
	
	private AudioFinishRecorderListenter mListenter;
	
	public void setAudioFinishRecorderListenter(AudioFinishRecorderListenter listenter){
		this.mListenter=listenter;
	}
	
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
				mReady = true;
				// 真正显示应该在audio end prepared以后
				mAudioManage.prepareAudio();
				//return true;
				return false;
			}
		});
		// TODO Auto-generated constructor stub
	}

	/* 
	 * 复写onTouchEvent
	 * @see android.widget.TextView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();   //获取当前Action
		
		int x = (int) event.getX();       //获取当前的坐标
		int y = (int) event.getY();

		switch (action) {
		
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDERING);
			break;

		case MotionEvent.ACTION_MOVE:

			// 已经开始录音状态时，根据X、Y的坐标，判断是否想要取消
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

			if (!isRecordering || mTime < 0.7f) {  //录音时间过短
				audioDialogManage.tooShort();
				mAudioManage.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 延迟，1.3秒以后关闭“时间过短对话框”
			} 
			
			else if (mCurState == STATE_RECORDERING) { //正常录制结束
				audioDialogManage.dimissDialog();
				// release
				mAudioManage.release();
				// callbackToAct
				// 正常录制结束，回调录音时间和录音文件完整路径——在播放的时候需要使用
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

	/**
	 * 恢复状态以及一些标志位
	 */
	private void reset() {
		isRecordering = false;
		mReady = false;                 //是否触发onLongClick
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

	/**
	 * 改变Button的背景和文本、展示不同状态的录音提示对话框
	 * @param state
	 */
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

	/* 
	 * 实现“准备完毕”接口
	 * (non-Javadoc)
	 * @see songshi.voicenotes.recorder.AudioManage.AudioStateListenter#wellPrepared()
	 */
	@Override
	public void wellPrepared() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
	}
	
	private static final int MSG_AUDIO_PREPARED = 0x110;   //准备完全
	private static final int MSG_VOICE_CHANGE = 0x111;     //声音改变
	private static final int MSG_DIALOG_DIMISS = 0x112;    //销毁对话框
	
	/**
	 * 接收子线程数据，并用此数据配合主线程更新UI
	 * Handler运行在主线程（UI线程）中，它与子线程通过Message对象传递数据。
	 * Handler接受子线程传过来的(子线程用sedMessage()方法传弟)Message对象，把这些消息放入主线程队列中，配合主线程进行更新UI。
	 */
	private Handler mHandler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:        //216:mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
				audioDialogManage.showRecorderingDialog();
				isRecordering = true;
				//已经在录制，同时开启一个获取音量、并且计时的线程
				new Thread(mGetVoiceLevelRunnable).start();
				break;

			case MSG_VOICE_CHANGE:          //265:mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
				audioDialogManage.updateVoiceLevel(mAudioManage
						.getVoiceLevel(7));
				break;

			//这里在Handler里面处理DIALOG_DIMISS，是因为想让该对话框显示一段时间，延迟关闭，——详见125行
			case MSG_DIALOG_DIMISS:         //125:mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
				audioDialogManage.dimissDialog();
				break;
			}
		};
	};
	
	private float mTime;  //开始录音时，计时；（在reset()中置空）
	/**
	 * 获取音量大小的Runnable
	 */
	private Runnable mGetVoiceLevelRunnable = new Runnable() {

		@Override
		public void run() {
			
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
	
}
