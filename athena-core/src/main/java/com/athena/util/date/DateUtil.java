package com.athena.util.date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;


/**
 * <p>
 * Title:日期处理类
 * </p>
 * <p>
 * Description:处理日期格式
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-1-13
 */
public final class DateUtil { 
	private DateUtil(){};
	/**
	 * 获得系统当前日期
	 * @date 2012-1-18 
	 * @author hzg
	 * @return String 当前日期字符串
	 */
	public static String getCurrentDate() {
		DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date date = new Date();
		return yyyyMMddFormat.format(date);
	}
	
	/**
	 * 获得系统当前时间
	 * @date 2012-1-18 
	 * @author hzg
	 * @return String 当前时间字符串
	 */
	public static String curDateTime() {
		DateFormat YMDHmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date curDate = new Date(System.currentTimeMillis());
		return YMDHmsFormat.format(curDate);
	}

	/**
	 * 将日期转换为yyyy-MM-dd字符串
	 * @date 2012-1-18 
	 * @author hzg
	 * @param date
	 * @return String 日期字符串
	 */
	public static String dateToStringYMD(Date date){
		DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		return yyyyMMddFormat.format(date);
	}
	
	/**
	 * 将日期转换为yyyy-MM-dd HH:mm:ss字符串
	 * @date 2012-1-18 
	 * @author hzg
	 * @param date
	 * @return String 日期字符串
	 */
	public static String dateToStringYMDHms(Date date){
		DateFormat YMDHmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		return YMDHmsFormat.format(date);
	}
	
	
	/**
	 * 将字符串转换为日期类型
	 * @date 2013-4-9 
	 * @author hzg
	 * @param str 日期字符串
	 * @return String 日期
	 */
	public static String stringToStringYMDHMS(String str) {
		DateFormat yyyyMMddFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date date = new Date();
		try {
			date = yyyyMMddFormat.parse(str);
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return dateToStringYMDHms(date); 
	}
	
	
	/**
	 * 将字符串转换为yyyy-MM-dd日期类型
	 * @date 2012-1-18 
	 * @author hzg
	 * @param str 日期字符串
	 * @return Date 日期
	 */
	public static Date stringToDateYMD(String str) throws ParseException{
		DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date date = null; 
		if (null != str && !"".equals(str)) {
			date = yyyyMMddFormat.parse(str); 
		}
		return date;
	}
	
	/**
	 * 将字符串转换为yyyy-MM-dd hh:mm:ss日期类型
	 * @date 2013-3-6
	 * @author hzg
	 * @param str 日期字符串
	 * @return Date 日期
	 */
	public static Date stringToDateYMDHMS(String str) throws ParseException{
		if(str==null||str.trim().length()==0){
			return null;
		}
		DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date date = new Date(); 
		date = yyyyMMddFormat.parse(str); 
		return date;
	}
	
	/**
	 * 将字符串转换为yyyy-MM-dd hh:mm日期类型
	 * @date 2013-3-27
	 * @author hzg
	 * @param str 日期字符串
	 * @return Date 日期
	 */
	public static Date stringToDateYMDHM(String str) throws ParseException{
		if(str==null||str.trim().length()==0){
			return null;
		}
		DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Date date = new Date(); 
		date = yyyyMMddFormat.parse(str); 
		return date;
	}
	

	/**
	 * 按给定的格式将日期转换为字符串
	 * @date 2012-1-18 
	 * @author hzg
	 * @param date 日期
	 * @param format 转换格式
	 * @return String 日期字符串
	 */
	public static String dateFromat(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format,Locale.CHINA);
		return dateFormat.format(date);
	}

	/**
	 * 日期字符串转化为时间撮日期
	 * @date 2012-1-18 
	 * @author hzg
	 * @param str
	 * @return
	 */
	public static Timestamp stringToTimestamp(String str) {
		DateFormat YMDHmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Timestamp tstamp = null;
		try {
			tstamp = new java.sql.Timestamp(YMDHmsFormat.parse(str).getTime());
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return tstamp;
	}	
	
	
	
	/**
	 * 日期和分钟相减得到新的日期
	 * @date 2012-2-20 
	 * @author hzg
	 * @param oldDate 日期（yyyy-MM-dd HH:mm）
	 * @param minutes 分钟
	 * @return String 相减后的日期
	 */
	public static String DateSubtractMinutes(String oldDate,int minutes){
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String subtDate="";
		try {
			Date date = sdf.parse(oldDate);
			Date newDate = new Date();
			newDate.setTime(date.getTime()-(long)(minutes*60*1000));
			subtDate = sdf.format(newDate);
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return subtDate;
	}
	
	/**
	 * 日期和小时相减得到新的日期
	 * @date 2012-2-20 
	 * @author hzg
	 * @param oldDate 日期（yyyy-MM-dd HH:mm:ss）
	 * @param hours 小时
	 * @return String 相减后的日期
	 */
	public static String DateSubtractHours(String oldDate,int hours){
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String subtDate="";
		try {
			Date date = sdf.parse(oldDate);
			Date newDate = new Date();
			newDate.setTime(date.getTime()-(long)(hours*60*60*1000));
			subtDate = sdf.format(newDate);
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return subtDate;
	}
	
	/**
	 * 将yyyyMMdd转换成yyyy-MM-dd
	 * @date 2012-2-23 
	 * @author hzg
	 * @param strDate 要转换的日期yyyyMMdd
	 * @return String 日期 yyyy-MM-dd
	 */
	public static String StringFormatWithLine(String strDate)throws ParseException{
		DateFormat oldYMD = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
		DateFormat newYMD = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		String newStrDate="";
		try {
			if (null != strDate && !"".equals(strDate)) {
				Date date = oldYMD.parse(strDate);
				newStrDate = newYMD.format(date);
			}
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		
		return newStrDate;
	}
	
	/**
	 * 将yyyyMMddhhmm转换成yyyy-MM-dd HH:mm
	 * @date 2012-2-23 
	 * @author hzg
	 * @param strDate 要转换的日期yyyyMMddhhmm
	 * @return String 日期 yyyy-MM-dd HH:mm
	 */
	public static String StringFormatWithLineYMDHM(String strDate)throws ParseException{
		DateFormat oldYMD = new SimpleDateFormat("yyyyMMddHHmm",Locale.CHINA);
		DateFormat newYMD = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String newStrDate="";
		try {
			if (null != strDate && !"".equals(strDate)) {
				Date date = oldYMD.parse(strDate);
				newStrDate = newYMD.format(date);
			}
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		
		return newStrDate;
	}
	
	
	
	
	/**
	 * 将yyyy-MM-dd转换成yyyyMMdd
	 * @date 2012-2-23 
	 * @author hzg
	 * @param strDate 要转换的日期yyyy-MM-dd
	 * @return String 日期yyyyMMdd
	 */
	public static String StringFormatToString(String strDate) throws ParseException{
		DateFormat  newYMD = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
		DateFormat  oldYMD = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		String newStrDate="";
		try {
			if (null != strDate && !"".equals(strDate)) {
				Date date = oldYMD.parse(strDate);
				newStrDate = newYMD.format(date);
			}
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return newStrDate;
	}
	
	/**
	 * 指定的日期类型（比如ddMMyyyy或ddMMyy）转换成yyyy-MM-dd
	 * @date 2013-3-12 
	 * @author hzg
	 * @param strDate 要转换的日期ddMMyyyy或ddMMyy
	 * @return String 日期 yyyy-MM-dd
	 */
	public static String StringFormatddMMYYYY(String strDate,String dateType)throws ParseException{
		DateFormat oldYMD = new SimpleDateFormat(dateType,Locale.CHINA);
		DateFormat newYMD = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		String newStrDate="";
		try {
			if (null != strDate && !"".equals(strDate)) {
				Date date = oldYMD.parse(strDate);
				newStrDate = newYMD.format(date);
			} 
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		
		return newStrDate;
	}
	
	/**
	 * 将字符串转换为日期类型
	 * @date 2012-1-18 
	 * @author 王冲
	 * @param str 日期字符串
	 * @return String 日期
	 */
	public static String stringSFMToStringYMD(String str) {
		DateFormat yyyyMMddFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date date = new Date();
		try {
			date = yyyyMMddFormat.parse(str);
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return dateToStringYMD(date); 
	}
	
	/**
	 * 计算两个时间段相隔的天数
	 * @date 2012-2-23 
	 * @author hzg
	 * @param startDate 开始时间（格式必须为yyyy-MM-dd）
	 * @param endDate 结束时间（格式必须为yyyy-MM-dd）
	 */
	public static int getDays(String startDate,String endDate){
		GregorianCalendar c1=new GregorianCalendar();
		GregorianCalendar c2=new GregorianCalendar();
		int intons = startDate.indexOf("-");
		int intofs = startDate.lastIndexOf("-");
		int startYear = Integer.parseInt(startDate.substring(0,intons));
		int startMonth = Integer.parseInt(startDate.substring((intons+1),intofs));
		int startDay = Integer.parseInt(startDate.substring(intofs+1));
		c1.set(startYear, startMonth, startDay);
		int intone = startDate.indexOf("-");
		int intofe = startDate.lastIndexOf("-");
		int endYear = Integer.parseInt(endDate.substring(0,intone));
		int endMonth = Integer.parseInt(endDate.substring((intone+1),intofe));
		int endDay = Integer.parseInt(endDate.substring(intofe+1));
		c2.set(endYear, endMonth, endDay);
		return (int)((c2.getTimeInMillis()-c1.getTimeInMillis())/(24*60*60*1000));
	}
	
	
	/**
	 * 日期相减天数，跨月份
	 * @author 贺志国
	 * @date 2016-3-23
	 * @param beginDateStr
	 * @param endDateStr
	 */
	 public static int  getDaysOfDateSubtract(String beginDateStr,String endDateStr){
		   	long days = 0;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
	        try
	        {
	        	Date beginDate = format.parse(beginDateStr);
	        	Date endDate= format.parse(endDateStr);    
	            days =(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
	        } catch (ParseException e)
	        {
	        	Logger log =  Logger.getLogger(DateUtil.class);
				log.error("日期相减取自然天数异常"+e.getMessage());
	        }   
	        return (int) days;
	 }
	
	
	 /**
	 * 日期和分钟相加得到新的日期
	 * @date 2012-2-20 
	 * @author hzg
	 * @param oldDate 日期（yyyy-MM-dd HH:mm）
	 * @param minutes 分钟
	 * @return String 相加后的日期
	 */
	public static String dateAddMinutes(String oldDate,int minutes){
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String subtDate="";
		try {
			Date date = sdf.parse(oldDate);
			Date newDate = new Date();
			newDate.setTime(date.getTime()+(long)(minutes*60*1000));
			subtDate = sdf.format(newDate);
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return subtDate;
	}

	
	/**
	 * 日期加n天
	 * @date 2012-2-24 
	 * @author hzg
	 * @param strDate 日期字符串（yyyy-MM-dd）
	 * @return 增加n天后的日期
	 */
	@SuppressWarnings("static-access")
	public static String dateAddDays(String strDate,int n){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date date = new Date();
		try {
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(format.parse(strDate)); 
			calendar.add(calendar.DATE,n);//把日期往后增加n天.整数往后推,负数往前移动 
			date=calendar.getTime();      //这个时间就是日期往后推一天的结果 
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return format.format(date);
	}
}
