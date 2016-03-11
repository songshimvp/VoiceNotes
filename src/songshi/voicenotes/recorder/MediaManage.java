package songshi.voicenotes.recorder;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;

public class MediaManage {
	
	private static MediaPlayer mMediaPlayer;   //播放录音文件
	private static boolean isPause;
	
	/**
	 * 播放音频
	 * @param filePath
	 * @param onCompletionListenter
	 */
	public static void playSound(String filePath,MediaPlayer.OnCompletionListener onCompletionListenter){
		if(mMediaPlayer==null){
			mMediaPlayer=new MediaPlayer();
			mMediaPlayer.setOnErrorListener( new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					mMediaPlayer.reset();
					return false;
				}
			});
		}else{
			mMediaPlayer.reset();
		}
		
		try {
			//详见“MediaPlayer”调用过程图
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(onCompletionListenter);
			mMediaPlayer.setDataSource(filePath);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 	暂停
	 */
	public static void pause(){
		if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
			isPause=true;
		}
	}
	
	/**
	 * resume重新开始
	 */
	public static void resume(){
		if(mMediaPlayer!=null && isPause){
			mMediaPlayer.start();
			isPause=false;
		}
	}
	
	/**
	 * release释放资源
	 */
	public static void release(){
		if(mMediaPlayer!=null){
			mMediaPlayer.release();
			mMediaPlayer=null;
		}
	}
}
