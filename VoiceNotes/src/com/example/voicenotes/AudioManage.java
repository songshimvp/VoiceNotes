package com.example.voicenotes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;

public class AudioManage {

	private MediaRecorder mMediaRecorder;
	private String mDir; // 文件夹的名称
	private String mCurrentFilePath;

	private static AudioManage mInstance;

	private boolean isPrepared; // 标识MediaRecorder准备完毕

	private AudioManage(String dir) {
		mDir = dir;
	}

	// 回调准备完毕
	public interface AudioStateListenter {
		void wellPrepared(); // prepared完毕
	}

	public AudioStateListenter mListenter;

	public void setOnAudioStateListenter(AudioStateListenter audioStateListenter) {
		mListenter = audioStateListenter;
	}

	// 单例实现 AudioManage
	
	//DialogManage主要管理Dialog，Dialog主要依赖Context，而且此Context必须是Activity的Context，
	//DialogManage写成单例实现，将是Application级别的，将无法释放，容易造成内存泄露，甚至导致错误
	public static AudioManage getInstance(String dir) {
		if (mInstance == null) {
			synchronized (AudioManage.class) { // 同步
				if (mInstance == null) {
					mInstance = new AudioManage(dir);
				}
			}
		}

		return mInstance;
	}

	// 准备
	public void prepareAudio() {

		try {
			isPrepared = false;

			File dir = new File(mDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = GenerateFileName(); // 文件名字
			File file = new File(dir, fileName); // 路径+文件名字

			mCurrentFilePath = file.getAbsolutePath();

			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setOutputFile(file.getAbsolutePath());// 设置输出文件
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);// 设置音频的格式
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置音频的编码为AMR_NB

			mMediaRecorder.prepare();

			mMediaRecorder.start();
			isPrepared = true; // 准备结束

			if (mListenter != null) {
				mListenter.wellPrepared();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 随机生成文件名称
	private String GenerateFileName() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString() + ".amr"; // 音频文件格式
	}

	// 获得音量等级
	public int getVoiceLevel(int maxLevel) {
		if (isPrepared) {
			try {
				return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		return 1;
	}

	// 释放
	public void release() {
		mMediaRecorder.stop();
		mMediaRecorder.release();
		mMediaRecorder = null;
	}

	// 取消
	public void cancel() {

		release();

		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			file.delete();    //删除录音文件
			mCurrentFilePath = null;
		}
	}

	public String getCurrentFilePath() {
		// TODO Auto-generated method stub
		return mCurrentFilePath;
	}
}
