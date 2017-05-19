package com.athena.pc.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:公共方法类
 * </p>
 * <p>
 * Description:定义供其他类调用的公共方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @createDate 2011-12-21
 */
@Component
public class CollectionUtil {

	public final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String YMDFORMAT = "yyyy-MM-dd";
	/**
	 * 将List中需要的某一个字段值保存到map中
	 * @author hezhiguo
	 * @date 2011-12-21
	 * @param list 传入的集合List
	 * @param keyName   用该键来设置值
	 * @param valueName 根据该键获得值
	 * @return 包含某一字段值的map
	 */
	public static Map<String,String> convertListToMap(List<Map<String,String>> list,String keyName,String valueName){
		Map<String,String> map = new HashMap<String,String>();
		for(Map<String,String> temp : list){
			map.put(temp.get(keyName).toString(), String.valueOf(temp.get(valueName)));
		}
		return map;
	}
	
	
	/**
	 * 将List中的值拼成一个json字符串返回用于下拉列表框,比如：{'cx001':'大车','cx002':'小车'} 
	 * @author hezhiguo
	 * @date 2011-12-27
	 * @param list 传入的集合List
	 * @param keyName 用该键来设置值
	 * @param valueName 根据该键获得值
	 * @return 包含某一字段值的map
	 */
	public static String listToJson(List<Map<String,String>> list,String keyName,String valueName){
		StringBuffer strBuffer  = new StringBuffer("{"); 
		String flag = "";
		for(Map<String,String> map : list){
			strBuffer.append(flag).append("'").append(map.get(keyName).toString()).append("'");
			strBuffer.append(":").append("'").append(map.get(valueName).toString()).append("'");
			flag = ",";
		}
		strBuffer.append("}").toString();
		return strBuffer.toString();
	}
	
	
	/**
	 * 将字符串型List拼装成SQL中in能接受的字符串，形如'20001','20002'
	 * @author 贺志国
	 * @date 2012-3-23
	 * @param strList 字符串型List
	 * @return String 拼接后的字符串
	 */
	public static String listToString(List<String> strList){
		StringBuffer buffer = new StringBuffer();
		String flag = "";
		for(String str : strList){
			buffer.append(flag).append("'").append(str).append("'");
			flag=",";
		}
		return buffer.toString();
	}
	
	/**
	 * 集合转成String类型
	 * @author gswang
	 * @param list 集合
	 * @return String 产线L1,L2,L3
	 */
	public static String ListToStringWithSplit(List<Map<String,String>> list,String name){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append(chanxMap.get(name));
			flag=",";
		}
		return buf.toString();
	}
	
	/**
	 * 集合转成String类型，用于in操作
	 * @author gswang
	 * @param list 集合
	 * @return String 产线'L1','L2','L3'
	 */
	public static String chanxListToString(List<Map<String,String>> list,String name){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append("'").append(chanxMap.get(name)).append("'");
			flag=",";
		}
		return buf.toString();
	}
	
	/**
	 * 得到yyyy年mm月dd日，格式的日期
	 * @author gswang
	 * @param source String类型的日期
	 * @return yyyy年mm月dd日
	 */
	public static String getYearMonthDayChina(String source){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		StringBuffer buf = new StringBuffer();
		if(source.length()>0){
			Date strtodate = formatter.parse(source, pos);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(strtodate);
			buf.append(cal1.get(Calendar.YEAR)).append("年").append(cal1.get(Calendar.MONTH)+1).append("月").append(cal1.get(Calendar.DAY_OF_MONTH)).append("日");
		}
		return buf.toString();
	}	
	
	/**
	 * @description 得到今天的时间
	 * @author 王国首
	 * @date 2012-1-14
	 * @param format 	日期的格式
	 * @param String	 一定格式的日期
	 */
	public static String getTimeNow(String format){
		Date date = new Date(); 
		SimpleDateFormat fmat = new SimpleDateFormat(format); 
		return fmat.format(date);
	}
}
