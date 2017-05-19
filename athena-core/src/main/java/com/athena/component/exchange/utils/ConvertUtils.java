/**
 * 
 */
package com.athena.component.exchange.utils;

import com.athena.component.exchange.ParserException;
import com.athena.component.exchange.field.DataField;
import com.athena.component.exchange.field.DateFieldFormat;
import com.athena.component.exchange.field.NumberFieldFormat;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class ConvertUtils {
	
	/**
	 * 数据格式转换
	 * @param dataField
	 * @param strValue
	 * @return
	 * @throws ParserException
	 */
	public static Object convertValue(DataField dataField, String strValue) throws ParserException {
		String type = dataField.getType();
//		Class<?> convertClass;
		if("number".equalsIgnoreCase(type)){
			return new NumberFieldFormat(dataField.getFormat()).parse(strValue);
		}else if("date".equalsIgnoreCase(type)){//日期格式
			return new DateFieldFormat(dataField.getFormat()).parse(strValue);
		}else{
			//convertClass = String.class;
			return strValue;
		}
		//return ConvertUtils.convert(strValue, convertClass);
	}
	
	
	
	/**
	 * 空处理
	 * 
	 * @param obj
	 *            对象
	 * @return 空对象返回空串
	 */
	public static String strNull(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
	
	
	
	
	
}
