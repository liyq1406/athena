package com.athena.ckx.util;


import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.athena.util.exception.ServiceException;


public final class DateTimeUtil {
    private DateTimeUtil() {

    }

    /**
     * 以格式format返回表示日期时间的字符串
     * 
     * @param format
     * @return
     */
    public static String getDateTimeStr(String format) {
        Date date = new Date();
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 取得当前日期时间
     * 
     * @return
     */
    public static String getCurrDateTime() {
        return getDateTimeStr("yyyy.MM.dd HH:mm:ss");
    }

    /**
     * 获取当前日期时间
     * @return
     */
    public static String getAllCurrTime(){
    	return getDateTimeStr("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 取得当前日期,不足两位前补零
     * 
     * @return
     */
    public static String getCurrDate() {
        return getDateTimeStr("yyyy-MM-dd");
    }

    /**
     * 取得当前日期
     * 
     * @return
     */
    public static String getSimpleCurrDate() {
        return getDateTimeStr("yyyy.M.d");
    }

    /**
     * 取得当前时间
     * 
     * @return
     */
    public static String getCurrTime() {
        return getDateTimeStr("HH:mm:ss");
    }

    /**
     * 取得当前年月
     * 
     * @return
     */
    public static String getCurrYearMonth() {
        return getDateTimeStr("yyyy.MM");
    }

    /**
     * 从文本形式日期取得Date日期时间
     * 
     * @param strMonth
     * @return
     */
    private static Date getDate(String strMonth) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy.MM.dd");
        try {
            return myFormatter.parse(strMonth);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 得到两个文本日期之间的天数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDaysBetween(String startDate, String endDate) {
        Date dStart = getDate(startDate);
        Date dEnd = getDate(endDate);
        return (dEnd.getTime() - dStart.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 取某月的天数,strMonth的格式是"yyyy.MM"
     * @param strMonth
     * @return
     */
    public static int getDaysInAMonth(String strMonth) {
        String[] arr = strMonth.split("[.]");

        // Create a calendar object of the desired month
//        Calendar cal = new GregorianCalendar(Integer.parseInt(arr[0]), Integer
//                .parseInt(arr[1]) - 1, 1);
        
        Calendar cal = new GregorianCalendar(Integer.parseInt(arr[0]),0, 1);

        // Get the number of days in that month
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return days;
    }
    
    
    
    
    
   



    /** *//**
     * 取某月第一天是周几,strMonth的格式是"yyyy.MM"
     * @param strMonth
     * @return
     */
    public static int getWeekOfFirstDay(String strMonth) {
        String[] arr = strMonth.split("[.]");

        Calendar xmas = new GregorianCalendar(Integer.parseInt(arr[0]), Integer
                .parseInt(arr[1]) - 1, 1);
        int dayOfWeek = xmas.get(Calendar.DAY_OF_WEEK) - 1;
       // dayOfWeek =java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)-1;当前周数

        return dayOfWeek;
    }

    public static void main(String[] args) throws ParseException {

    }
    
    
    
   
    
    
    /**
     * 根据一个日期，返回是星期几 7=星期日
     * @param sdate  日期字符串，格式为 yyyy-MM-dd

     * @return
     */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = DateTimeUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String week = "";

		// ------返回星期序号 7=星期日 1=星期一，其他类推，用于计算
		int hour = c.get(Calendar.DAY_OF_WEEK);
		week = String.valueOf(hour-1);
		if(hour-1==0){week="7";}
		// --------

		//---- 返回星期中文，用于显示
//		week=new SimpleDateFormat("EEEE").format(c.getTime());
		//--------
		return week;
	}
    
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		return formatter.parse(strDate, pos);
	}
	
    /**
     * 获取某年，某月，有多少天
     * @param year
     * @param month
     * @return
     */
    public static int getDaysInAMonth(String year,String month) {
       // String[] arr = strMonth.split("[.]");

        // Create a calendar object of the desired month
        Calendar cal = new GregorianCalendar(Integer.parseInt(year), Integer
                .parseInt(month) - 1, 1);
        
//        Calendar cal = new GregorianCalendar(Integer.parseInt(arr[0]),0, 1);

        // Get the number of days in that month
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return days;
    }
    
    
    
    /**
     * 判断两个日期字符串是否相等
     * @param strDate1 格式为 yyyy-MM-dd 或者 yyyy-M-d
     * @param strDate2
     * @return
     */
    public static boolean isEqualDate(String strDate1,String strDate2){
    	return strToDate(strDate1).equals(strToDate(strDate2));
    }

	public static String getSeqWeek() {
		
		
		Calendar c = Calendar.getInstance();
		//c.setTime(date)
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1){week = "0" + week;}
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
		
		
		
	}
    	 
    /**
     * 返回一个指定日期所在周在本年的周序
     * @param sdate 日期字符串，格式为 yyyy-MM-dd
     * @return
     */
    public static int getWeekSeqNo(String sdate){
    	Calendar   c   =   Calendar.getInstance();
    	c.setFirstDayOfWeek(Calendar.MONDAY);
    	c.setTime(strToDate(sdate));
    	return c.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 比较两个日期格式的字符串
     * @author hj
     * @return 后一个日期大于前一个日期返回true，否则返回false
     */
    public static boolean compare(String beforeDate,String afterDate){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	boolean flag=false;
    	try {
    		flag=formatter.parse(beforeDate).before(formatter.parse(afterDate));
		} catch (ParseException e) {
			flag=false;
		}
		return flag;
    }
    
    /**
     * 自定义日期格式转换
     * @param dateStr
     * @param sf
     * @param mes
     * @return
     */
    public static Date zidyParse(String dateStr,String sf,String mes){
    	SimpleDateFormat formatter = new SimpleDateFormat(sf);
    	try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			throw new ServiceException(mes);
		}
    }
     /**
      * 获取当前日期的前一个月的年月日
      * @return
      */
        public static String getTimeByRedMonth(){
       	String strMonth = getDateTimeStr("yyyy.MM.dd");
        	String[] arr = strMonth.split("[.]");  
        	Calendar xmas = new GregorianCalendar(Integer.parseInt(arr[0]), Integer
                      .parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));
        	int day = xmas.getActualMaximum(Calendar.DAY_OF_MONTH) ;
        	//若当前日期为当月的最大日期，则把该时间减去当前月的最大日期
        	if(day == Integer.parseInt(arr[2])){
        		xmas.set(Calendar.DATE,xmas.get(Calendar.DATE)-day) ;
        	}else{
        		//若不是，则直接月份减去1
        		xmas.set(Calendar.MONTH,xmas.get(Calendar.MONTH)-1);
        	}
        	Date date = xmas.getTime();
        	SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        	return myFormatter.format(date);
        }
        
        public static String getJavaTime(Date date) {
    		Format df = new SimpleDateFormat("yyyy-MM-dd");
    		return df.format(date);
    	}
}
