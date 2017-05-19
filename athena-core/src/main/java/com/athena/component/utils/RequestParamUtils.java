/**
 * 
 */
package com.athena.component.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 *
 */
public class RequestParamUtils {
	
	
	public static Map<String,Object> getQueryParams(Object bean,
			Map<String,String> webParams){
		
		Map<String,Object> params = new HashMap<String,Object>();
		Set<String> webParamNames = webParams.keySet();
		//
		for(String webParamName:webParamNames){
			if(PropertyUtils.hasProperty(bean, webParamName)){
				params.put(webParamName, webParams.get(webParamName));
			}
		}
		//排序相关参数
		
		return params;
	}
}
