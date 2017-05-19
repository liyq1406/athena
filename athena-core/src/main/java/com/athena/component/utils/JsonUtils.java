/**
 * 
 */
package com.athena.component.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class JsonUtils {
	
	public static Map<String,Object> jsonStrToMap(String jsonStr){
		Map<String,Object> result = new HashMap<String,Object>();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		
		Set<String> keys = jsonObject.keySet();
		Object value;
		for(String key:keys){
			value = jsonObject.get(key);
			if(value instanceof JSONObject){
				//
				result.put(key, jsonStrToMap(value.toString()));
			}else if(value instanceof JSONArray){
				result.put(key,jsonToList((JSONArray)value));
			}else{
				result.put(key, value);
			}
			value = null;
		}
		
		return result;
	}
	
	/**
	 * 将json数组转换成  list<map>形式
	 * @param JSONArray
	 * @return Object
	 * @author zhangl 20120202
	 */
	public static Object jsonToList(JSONArray value) {
		List<Object> results = new ArrayList<Object>();
		Object[] objArray =  value.toArray();
		for(Object obj:objArray){
			if(obj instanceof JSONObject){
				results.add(jsonStrToMap(obj.toString()));
			}else{
				results.add(obj);
			}
		}
		return results;
	} 
}
