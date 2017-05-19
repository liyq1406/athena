package com.athena.ckx.entity.baob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtilTool {
	/**
	 * 将List中需要的某一个字段值保存到map中
	 * @author hezhiguo
	 * @date 2011-12-21 11458
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
	 * @param factoryList 传入的集合List
	 * @param keyName 用该键来设置值
	 * @param valueName 根据该键获得值
	 * @return 包含某一字段值的map
	 */
	public static String listToJson(List<HashMap<String, String>> factoryList,String keyName,String valueName){
		StringBuffer strBuffer  = new StringBuffer("{"); 
		String flag = "";
		for(Map<String,String> map : factoryList){
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

}
