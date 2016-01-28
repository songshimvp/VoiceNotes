package songshi.voicenotes.recorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;

public class AudioManage {

	private MediaRecorder mMediaRecorder;
	private String mDir; // �ļ��е�����
	private String mCurrentFilePath;

	private static AudioManage mInstance;

	private boolean isPrepared; // ��ʶMediaRecorder׼�����

	private AudioManage(String dir) {
		mDir = dir;
	}

	// �ص�׼�����
	public interface AudioStateListenter {
		void wellPrepared(); // prepared���
	}

	public AudioStateListenter mListenter;

	public void setOnAudioStateListenter(AudioStateListenter audioStateListenter) {
		mListenter = audioStateListenter;
	}

	// ����ʵ�� AudioManage
	//DialogManage��Ҫ����Dialog��Dialog��Ҫ����Context�����Ҵ�Context������Activity��Context��
	//DialogManageд�ɵ���ʵ�֣�����Application����ģ����޷��ͷţ���������ڴ�й¶���������´���
	public static AudioManage getInstance(String dir) {
		if (mInstance == null) {
			synchronized (AudioManage.class) { // ͬ��
				if (mInstance == null) {
					mInstance = new AudioManage(dir);
				}
			}
		}

		return mInstance;
	}

	// ׼��
	public void prepareAudio() {

		try {
			isPrepared = false;

			File dir = new File(mDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = GenerateFileName(); // �ļ�����
			File file = new File(dir, fileName);  // ·��+�ļ�����

			mCurrentFilePath = file.getAbsolutePath();

			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setOutputFile(file.getAbsolutePath());  // ��������ļ�
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // ����MediaRecorder����ƵԴΪ��˷�
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);  // ������Ƶ�ĸ�ʽ
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  // ������Ƶ�ı���ΪAMR_NB

			mMediaRecorder.prepare();

			mMediaRecorder.start();
			isPrepared = true; // ׼������

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

	// ��������ļ�����
	private String GenerateFileName() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString() + ".amr"; // ��Ƶ�ļ���ʽ
	}

	// ��������ȼ�
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

	// �ͷ�
	public void release() {
		mMediaRecorder.stop();
		mMediaRecorder.release();
		mMediaRecorder = null;
	}

	// ȡ��
	public void cancel() {

		release();

		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			file.delete();    //ɾ��¼���ļ�
			mCurrentFilePath = null;
		}
	}

	public String getCurrentFilePath() {
		// TODO Auto-generated method stub
		return mCurrentFilePath;
	}
}