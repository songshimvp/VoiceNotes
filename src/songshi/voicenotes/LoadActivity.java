package songshi.voicenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.example.voicenotes.R;



/**
 * 加载页面 
 * @author songshi
 *
 */
public class LoadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load_image);
		
		ImageView loadImage = (ImageView) findViewById(R.id.loadImage);

		// 透明度渐变动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);// 从微弱显示到完全显示
		// 动画时间
		alphaAnimation.setDuration(1500);
		// 将组件和动画进行关联
		loadImage.setAnimation(alphaAnimation);

		alphaAnimation.setAnimationListener(new AnimationListener() {

			// 动画开始
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				//Toast.makeText(LoadActivity.this, "欢迎使用随手", Toast.LENGTH_SHORT).show();
				//NetWork.isNetworkAvailable(LoadActivity.this);
				
				//NetWork.setNetWork(LoadActivity.this);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			// 动画结束
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
				Intent mainIntent = new Intent(LoadActivity.this,
						MainActivity.class);
				startActivity(mainIntent);
				LoadActivity.this.finish();
			}
		});
	}
}
