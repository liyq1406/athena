package com.athena.truck.module.kacApp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 卡车APP工具
 * @author CSY
 *
 */
public class kacAppUtil {
	
	public static String code_fail = "0";
	public static String code_success = "1";
	public static String code_logout = "2";
	public static String code_fangk = "3";

	/**
	 * 获取返回json
	 * @param state
	 * @param error
	 * @return
	 */
	public static JSONObject getResult(String state, String error){
		JSONObject result = new JSONObject();
		result.put("state", state);
		if (!"1".equals(state)) {
			result.put("error", error);
		}
		return result;
	}
	
	/**
	 * 字符串传日期
	 * @param str
	 * @return
	 */
	public static Date strToDate(String str, String fmt){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			Date date = sdf.parse(str);
			return date;
		} catch (Exception e) {
			return new Date();
		}
	}
	
	/**
	 * 数组字符串转ListJSONObject
	 * @param str [{"k1":"v1","k2":"v2"},{"k3":"v3","k4":"v4"},{...}]
	 * @return ListJSONObject
	 */
	public static List<JSONObject> strToList(String str){
		str = str.replace(" ", "");
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (null != str && !str.equals("")) {
			JSONArray jsonArr = JSONArray.fromObject(str);
			for (int i = 0; i < jsonArr.size(); i++) {
				list.add(JSONObject.fromObject(jsonArr.get(i)));
			}
		}
		return list;
	}
	
	/**
	 * 数组字符串转ListString
	 * @param str ["aaa","bbb","ccc"...]
	 * @return ListString
	 */
	public static List<String> strToListstr(String str){
		str = str.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "");
		String [] strs = str.split(",");
		List<String> list = new ArrayList<String>();
		if (null != strs && strs.length > 0) {
			list = Arrays.asList(strs);
		}
		return list;
	}
	
}
