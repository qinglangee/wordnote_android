package com.xmalloc.wordnote.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

/**
 * 跟时间有关的一个工具类！
 * @author nick
 * warning: dateformat is no thread safe
 */
public class TimeUtil {
	public static final String LONG = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORT = "yyyy-MM-dd";
	/**
	 * 格式化要显示的时间
	 * @param time
	 * @return
	 */
	public static String getDisplayTime(String time){
		Date now = new Date();
		Date date = parseDate(time);
		if(date == null){
			date = now; 
		}
		return getTimeDiff(now, date);
	}
	
	public static Date parseDate(String dateStr){
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
		} catch (ParseException e) {
			Log.e("TimeUtil", e.getMessage());
			return null;
		}
		return date;
	}
	
	public static Date parseDate(String dateStr, String format){
		Date date;
		try {
			date = new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			Log.e("TimeUtil", e.getMessage());
			return null;
		}
		return date;
	}
	
	public static String format(Date date, String format){
		String dateStr;
		dateStr = new SimpleDateFormat(format).format(date);
		return dateStr;
	}
	



	/**
	 * 计算两个时间之间的组合差值！
	 * @param currTime
	 * @param createTime
	 * @return 昨天22:08 | 06-20 15:36
	 * @throws ParseException 
	 */
	public static String getTimeDiff(Date currDate, Date lastDate) {
		  if(currDate == null || lastDate == null) return "1 秒前";
		  Calendar currCalendar = Calendar.getInstance();
		  currCalendar.setTime(currDate);
		  Calendar lastCalendar = Calendar.getInstance();
		  lastCalendar.setTime(lastDate);
		  if (currCalendar.before(lastCalendar)) {
			  return "1秒前";
		  }
		  
		  int currYear = currCalendar.get(Calendar.YEAR);
		  int currDay = currCalendar.get(Calendar.DAY_OF_YEAR);
		  int lastYear = lastCalendar.get(Calendar.YEAR);
		  int lastDay = lastCalendar.get(Calendar.DAY_OF_YEAR);
		  int diff = lastDay - currDay ;
		  String tmp = "";
		  
		  if(currYear == lastYear){
			switch (diff) {
				case 0:
				      long t1 = currDate.getTime();
				      long t2 = lastDate.getTime();
				      int hours=(int) ((t1 - t2)/3600000);
				      int minutes=(int) (((t1 - t2)/1000-hours*3600)/60);
				      int second = (int)(((t1 - t2)/1000));
				      if(second<=0) second=1;
				     if(hours==0 && minutes==0){
				        	tmp= second + "秒前";
				        	break;
				     }
				     if(hours<=0){
				        	if(minutes<0){
				        		minutes = 0 ;
				        	}
				        	tmp= minutes+"分钟前";
				        	break;
				     }
				     if(hours >= 1){
				    	 tmp = "今天 "+ completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + completionSingle(lastCalendar.get(Calendar.MINUTE));
				    	 break;
				     }
				case -1:
					tmp = "昨天 " + completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + completionSingle(lastCalendar.get(Calendar.MINUTE));
					break;
				case -2:
					tmp = "前天 " + completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + completionSingle(lastCalendar.get(Calendar.MINUTE));
					break;
				default:
					tmp = completionSingle((lastCalendar.get(Calendar.MONTH) + 1)) + "-" + completionSingle(lastCalendar.get(Calendar.DAY_OF_MONTH)) + " " + completionSingle(lastCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + completionSingle(lastCalendar.get(Calendar.MINUTE));
					break;
			}
		  }else{
			  tmp = lastCalendar.get(Calendar.YEAR) + "-" + completionSingle((lastCalendar.get(Calendar.MONTH) + 1)) + "-" + completionSingle(lastCalendar.get(Calendar.DAY_OF_MONTH));
		  }
		
		return tmp ;
    }
	

	/**
	 * 补全单个数字
	 * @param data
	 * @return
	 */
	public static String completionSingle(int data){
		return data <= 9 ? ("0" + data) : String.valueOf(data);
	}
}

