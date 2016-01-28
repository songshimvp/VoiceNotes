package songshi.voicenotes.unite;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.example.voicenotes.R;

public class ReminderDialog {

	private Context mContext;
	private LayoutInflater mInflater;
	private View remiderView;
	
	public ReminderDialog(Context context){
		this.mContext=context;
		
		mInflater=LayoutInflater.from(mContext);
		remiderView=mInflater.inflate(R.layout.reminder_dialog, null);
	}
	
	public void DateTimeDialogShow() {
		
		//CustomDialog是自定义对话框
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		//builder.setMessage("这个就是自定义的备注提示框");
		builder.setTitle("备注提醒");
		
		builder.setContentView(remiderView);
		
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
						//返回
					}
				});

		builder.create().show();
	}
}
