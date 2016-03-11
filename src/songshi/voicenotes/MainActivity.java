package songshi.voicenotes;

import java.util.ArrayList;
import java.util.List;

import songshi.voicenotes.recorder.AudioRecorderAdapter;
import songshi.voicenotes.recorder.AudioRecorderButton;
import songshi.voicenotes.recorder.AudioRecorderButton.AudioFinishRecorderListenter;
import songshi.voicenotes.recorder.MediaManage;
import songshi.voicenotes.unite.CurrentTime;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.voicenotes.R;


public class MainActivity extends Activity {

	private ListView mListView;
	private ArrayAdapter<Recorder> mAdapter;
	private List<Recorder> mDatas = new ArrayList<Recorder>();
	
	//设置ListView上Item的图片，找ListView的Item上的控件
	/*image=(ImageView)gridview.getChildAt(0).findViewById(R.id.iv_item);
	image.setBackgroundResource(R.drawable.computer_failed);*/
	
	private AudioRecorderButton mAudioRecorderButton;
	private View mAnimView;
	
	private View topView;
	private boolean flag = false;
	
	public static String currentTimeString = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		topView=findViewById(R.id.topView);
		mListView = (ListView) findViewById(R.id.voiceNotesListView);
		mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.recorderButton);
		
		//回调AudioRecorderButton的录音结束接口
		mAudioRecorderButton
				.setAudioFinishRecorderListenter(new AudioFinishRecorderListenter() {

					@Override
					public void onFinish(float seconds, String FilePath) {
						
						if ( !flag ) {   //第一次录音结束后，设置显示顶部灰度条
							topView.setVisibility(View.VISIBLE);
							flag = true;
						}
						
						currentTimeString=CurrentTime.getCurrentTime();
						
						Recorder recorder = new Recorder(seconds, FilePath, currentTimeString);
						mDatas.add(recorder);
						mAdapter.notifyDataSetChanged();    //更新ListView
						mListView.setSelection(mDatas.size() - 1);   //重新回到最后一个
					}
				});

		mAdapter = new AudioRecorderAdapter(this, mDatas);   //mDatas在上面回调“录音完成”接口时动态增加
		mListView.setAdapter(mAdapter);

		//mListView.getChildAt(index)
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (mAnimView != null) {   //处理如果有一个Item正在播放，然后用户又重新开启了另外一个Item的情况
					mAnimView.setBackgroundResource(R.drawable.voice_right3);   //直接设置成R.drawable.voice_right3
					mAnimView = null;   //然后置成空
				}
				
				// 播放动画
				mAnimView = view.findViewById(R.id.recorder_anim);
				mAnimView.setBackgroundResource(R.drawable.audio_play_anim);
				AnimationDrawable anim = (AnimationDrawable) mAnimView
						.getBackground();
				anim.start();
				
				// 播放音频
				MediaManage.playSound(mDatas.get(position).filePath,
						new MediaPlayer.OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								//在播放结束以后，将动画取消
								mAnimView.setBackgroundResource(R.drawable.voice_right3);
							}
						});
			
			}

		});
		
		
		
		//ListView的左滑——删除、标记、分享
		// TODO
		
		
	}

	/**
	 * 录音记录类（备忘信息）
	 * @author songshi
	 *
	 */
	public class Recorder {
		float time;         //备忘录音时间长度
		String filePath;    //备忘录音文件路径
		String mCurrentTime;     //备忘录音时的系统时间

		public Recorder(float time, String filePath ,String currentTime) {
			super();
			this.time = time;
			this.filePath = filePath;
			this.mCurrentTime = currentTime;
		}

		public float getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public String getmCurrentTime() {
			return mCurrentTime;
		}

		public void setmCurrentTime(String mCurrentTime) {
			this.mCurrentTime = mCurrentTime;
		}

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MediaManage.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MediaManage.resume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaManage.release();
	}
}
