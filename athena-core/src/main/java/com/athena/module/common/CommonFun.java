package com.athena.module.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;

import com.toft.utils.json.JSONException;
import com.toft.utils.json.JSONObject;

/**
 * 公共方法类
 * @author WL
 * @date 2011-10-24
 */
public class CommonFun {

	/**
	 * jsonObject转换成javabean
	 * @param <T> 泛型 
	 * @param c 要转换成的javabean类
	 * @param json JSONObject对象
	 * @return javabean
	 * @throws Exception
	 * @author WL
	 * @date 2011-10-24
	 * @update kong 2011-10-25
	 */
	public static final <T>T jsonToObject(Class<T> c,JSONObject json) throws Exception{
		//创建实例
		T temp = c.newInstance();
		//获取字段对象集合
		Field [] fields = c.getDeclaredFields();
		//循环遍历字段信息
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			//从JSON里取得数据
			String value;
			try {
				value = json.getString(f.getName());//createuser等属性找不到会报异常，捕获异常继续循环
				BeanUtils.setProperty(temp, f.getName(), value);
			} catch (JSONException e) {
				continue;
			}
		}
		return temp;	
	}
	
	/**
	 * javabean的值去除左右空格
	 * @param obj
	 * @return
	 * @throws Exception
	 * @author zhangl
	 * @date 2011-11-07
	 */
	public static Object trimObject( Object obj ) throws Exception {
		
		//获取实体类的所有属性，返回Field数组
		Field[] field = obj.getClass().getDeclaredFields(); 
		
		for( int i=0; i<field.length; i++ ){
			
			String name = field[i].getName();    //获取属性的名字         

			String type = field[i].getGenericType().toString();    //获取属性的类型

			if("class java.lang.String".equals(type)){ //如果type是类类型，则前面包含"class "，后面跟类名

				Method m = obj.getClass().getMethod("get" + name.substring(0,1).toUpperCase() + name.substring(1));

				String value = (String) m.invoke(obj);    //调用getter方法获取属性值 
				
				if( null != value ){
					value = value.trim();
					field[i].set(obj, value);
				}
			}
		}
		
		return obj;
	}
}
