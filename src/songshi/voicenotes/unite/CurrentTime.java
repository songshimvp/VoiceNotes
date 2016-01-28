package songshi.voicenotes.unite;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CurrentTime {
	
	private SimpleDateFormat dateFormat;
	
	// 获取系统时间，设置当前录音时的时间
	public static String getCurrentTime() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss EEEE",Locale.getDefault());
		//Date date = new Date(System.currentTimeMillis());
		String timeString = dateFormat.format(new Date());
		
		/*Calendar calendar =Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));  
        if("1".equals(mWeek)){  
        	mWeek ="日";  
        }else if("2".equals(mWeek)){  
        	mWeek ="一";  
        }else if("3".equals(mWeek)){  
        	mWeek ="二";  
        }else if("4".equals(mWeek)){  
        	mWeek ="三";  
        }else if("5".equals(mWeek)){  
        	mWeek ="四";  
        }else if("6".equals(mWeek)){  
        	mWeek ="五";  
        }else if("7".equals(mWeek)){  
        	mWeek ="六";  
        }  
		return timeString + mWeek;*/
		
		return timeString;
	}
	
	/*//判断是AM还是PM
	private String getAMorPM(){
		String am_pm=Calendar.getInstance().getTime().getHours() >= 12 ? "PM" : "AM";
		return am_pm;
	}*/
}
