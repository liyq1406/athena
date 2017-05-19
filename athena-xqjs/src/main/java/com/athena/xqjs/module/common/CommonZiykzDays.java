/**
 * 
 */
package com.athena.xqjs.module.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author dsimedd001
 * 
 */
public class CommonZiykzDays {

	/**
	 * 获取当前日期的星期
	 * 
	 * @return
	 */
	public static int getWeekDay() {
		Date dt = new Date();
		// String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
		// "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return w;
	}

	/**
	 * 根据星期获取所需日期
	 * 
	 * @param i
	 * @return
	 */
	public static String getDate(int i) {
		Date date = new Date();
		long time = date.getTime();
		long timeInfo = time - (long)(i * 24 * 60 * 60 * 1000);
		Date date1 = new Date(timeInfo);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dates = df.format(date1);
		return dates;
	}

}
