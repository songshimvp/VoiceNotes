package songshi.voicenotes.unite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.voicenotes.R;

/**
 * 判断网络状态
 * @author songshi
 *
 */
public class NetWork {

	public static boolean isNetworkAvailable(Context context) {
		// 判断网络状态
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			Toast.makeText(context, "当前没有网络", Toast.LENGTH_LONG).show();
			return false;

		} else {
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
			if (networkInfo != null) {
				for (NetworkInfo netWork : networkInfo) {
					if (netWork.getState() == NetworkInfo.State.CONNECTED) {
						Toast.makeText(context, "有网络，请刷新服务", Toast.LENGTH_LONG)
								.show();
						return true;
					}
				}
			}
		}
		Toast.makeText(context, "没有任何网络数据", Toast.LENGTH_LONG).show();
		return false;
	}

	public static void setNetWork(final Context context) {
		if (!isNetworkAvailable(context)) {

			new AlertDialog.Builder(context).setIcon(R.drawable.dialog_alert)
					.setTitle("网络状态提示").setMessage("当前没有可用的网络，请设置网络数据")
					.setPositiveButton("设置", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							context.startActivity(new Intent(
									android.provider.Settings.ACTION_SETTINGS));
							
							//context.finish();
						}
					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return ;
						}
					}).create().show();
		}
	}
}
