/**
 * 
 */
package com.athena.component.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.toft.core3.util.ReflectionUtils;
import com.toft.mvc.utils.BeanUtils;


/**
 * @author Administrator
 *
 */
public class PropertyUtils {
	private final static Log logger = LogFactory.getLog(PropertyUtils.class);
	
	private static final IntParser INT_PARSER = new IntParser();

	private static final LongParser LONG_PARSER = new LongParser();

	private static final FloatParser FLOAT_PARSER = new FloatParser();

	private static final DoubleParser DOUBLE_PARSER = new DoubleParser();

	private static final BooleanParser BOOLEAN_PARSER = new BooleanParser();

	private static final BigDecimalParser BIGDECIMAL_PARSER = new BigDecimalParser();
	/**
	 * @param bean
	 * @param property
	 * @param value
	 */
	public static void setSimplePropertyValue(Object bean,String property,Object value){
		//
		Object convertValue = value;
		Field field = ReflectionUtils.findField(bean.getClass(), property);
		if(field!=null){
			Class<?> fieldType =field.getType();
			if(fieldType!=String.class&&(value==null||value instanceof String)){
				convertValue = getConvertValue(value,fieldType);
			}
			//设置值
			BeanUtils.setProperty(bean, property, convertValue);
		}
	}
	
	private static Object getConvertValue(Object value, Class<?> type) {
		String strValue = null;
		if(value!=null){
			strValue = value.toString();
		}
		if(type==int.class||type==Integer.class){
			return strValue==null||strValue.equals("")?0:INT_PARSER.doParse(strValue);
		}else if(type==long.class||type==Long.class){
			return strValue==null||strValue.equals("")?0l:LONG_PARSER.doParse(strValue);
		}else if(type==float.class||type==Float.class){
			return strValue==null||strValue.equals("")?0f:FLOAT_PARSER.doParse(strValue);
		}else if(type==double.class||type==Double.class){
			return strValue==null||strValue.equals("")?0d:DOUBLE_PARSER.doParse(strValue);
		}else if(type==boolean.class||type==Boolean.class){
			return strValue==null||strValue.equals("")?false:BOOLEAN_PARSER.doParse(strValue);
		}else if(type==BigDecimal.class){
			return strValue==null||strValue.equals("")?BigDecimal.ZERO:BIGDECIMAL_PARSER.doParse(strValue);
		}
		return null;
	}
	/**
	 * 设置属性值
	 * @param bean
	 * @param property
	 * @param value
	 */
	public static void setPropertyValue(Object bean,String property,Object value){
		String[] properties = property.split("\\.");
		if(properties.length>1){
			Object objBean = bean;
			Object valueBean = null;
			for(int i=0;i<properties.length;i++){
				if(i<properties.length-1){
					//
					valueBean = PropertyUtils.getSimplePropertyValue(objBean, properties[i]);
					if(valueBean==null){
						valueBean = createObject(objBean,properties[i]);
					}
					if(valueBean==null)break;
					setPropertyValue(objBean,properties[i], valueBean);
					objBean = valueBean;
				}else{
					//最后一级
					setSimplePropertyValue(objBean,properties[i], value);
				}
			}
		}else{
			setSimplePropertyValue(bean,property, value);
		}
	}
	
	public static boolean hasProperty(Object bean,String property){
		String[] properties = property.split("\\.");
		if(properties.length>1){
			Object objBean = bean;
			Object valueBean = null;
			for(int i=0;i<properties.length;i++){
				if(i<properties.length-1){
					valueBean = createObject(objBean,properties[i]);
					if(valueBean==null)break;
					objBean = valueBean;
				}else{
					//最后一级
					return ReflectionUtils.findField(objBean.getClass(), properties[i])!=null;
				}
			}
		}else{
			return ReflectionUtils.findField(bean.getClass(), property)!=null;
		}
		return false;
	}
	/**
	 * 获取简单属性值
	 * @param bean
	 * @param property
	 * @return
	 */
	public static Object getSimplePropertyValue(Object bean,String property){
		try {
			return org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(bean, property);
		} catch (IllegalAccessException e) {
			logger.warn("【"+property+"】IllegalAccessException："+e.getMessage());
		} catch (IllegalStateException e) {
			logger.warn("【"+property+"】IllegalStateException："+e.getMessage());
		} catch (InvocationTargetException e) {
			logger.warn("【"+property+"】InvocationTargetException："+e.getMessage());
		} catch (NoSuchMethodException e) {
			logger.warn("【"+bean.getClass()+" "+property+"】NoSuchMethodException："+e.getMessage());
		} catch(IllegalArgumentException e){
			logger.warn("【"+bean.getClass()+" "+property+"】IllegalArgumentException："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取属性值
	 * @param bean
	 * @param property
	 * @return
	 */
	public static Object getPropertyValue(Object bean, String property) {
		String[] properties = property.split("\\.");
		if(properties.length>1){
			Object objBean = bean;
			for(int i=0;i<properties.length;i++){
				if(i<properties.length-1){
					objBean = getSimplePropertyValue(objBean,properties[i]);
					if(objBean==null)return null;
				}else{
					//最后一级
					return getSimplePropertyValue(objBean,properties[i]);
				}
			}
		}else{
			return getSimplePropertyValue(bean,property);
		}
		return null;
	}
	
	/**
	 * 创建属性对象
	 * @param objBean
	 * @param property
	 * @return
	 */
	private static Object createObject(Object objBean,String property){
		
		Field field = ReflectionUtils.findField(objBean.getClass(), property);
		if(field!=null){
			Class<?> valueClass = field.getType();
			Object valueBean = null;
			try {
				valueBean = valueClass.newInstance();
			} catch (InstantiationException e) {
				logger.error("创建对象异常【InstantiationException】:"+e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error("创建对象异常【IllegalAccessException】:"+e.getMessage());
			}
			return valueBean;
		}
		return null;
	}
	


	private abstract static class ParameterParser<T> {

		protected abstract String getType();

		protected abstract T doParse(String parameter) throws NumberFormatException;
	}


	private static class IntParser extends ParameterParser<Integer> {

		@Override
		protected String getType() {
			return "int";
		}

		@Override
		protected Integer doParse(String s) throws NumberFormatException {
			return Integer.valueOf(s);
		}
	}


	private static class LongParser extends ParameterParser<Long> {

		@Override
		protected String getType() {
			return "long";
		}

		@Override
		protected Long doParse(String parameter) throws NumberFormatException {
			return Long.valueOf(parameter);
		}

	}


	private static class FloatParser extends ParameterParser<Float> {

		@Override
		protected String getType() {
			return "float";
		}

		@Override
		protected Float doParse(String parameter) throws NumberFormatException {
			return Float.valueOf(parameter);
		}

	}


	private static class DoubleParser extends ParameterParser<Double> {

		@Override
		protected String getType() {
			return "double";
		}

		@Override
		protected Double doParse(String parameter) throws NumberFormatException {
			return Double.valueOf(parameter);
		}
	}


	private static class BooleanParser extends ParameterParser<Boolean> {

		@Override
		protected String getType() {
			return "boolean";
		}

		@Override
		protected Boolean doParse(String parameter) throws NumberFormatException {
			return (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("on") ||
					parameter.equalsIgnoreCase("yes") || parameter.equals("1"));
		}
	}
	
	private static class BigDecimalParser extends ParameterParser<BigDecimal> {

		@Override
		protected String getType() {
			return "BigDecimal";
		}

		@Override
		protected BigDecimal doParse(String parameter) throws NumberFormatException {
			BigDecimal bigDecimal = new BigDecimal(parameter);
			return bigDecimal;
		}
	}
}
