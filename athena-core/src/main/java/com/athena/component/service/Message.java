/**
 * 
 */
package com.athena.component.service;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.toft.core3.i18n.CachingResourceBundleFactory;

/**
 * <p>Title:ATHENA 核心组件</p>
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
public class Message {
	
	//全局的i18n配置文件
	private static final String ATHENA_COMMON_I18N = "i18n.athena";

	//信息
	private String message;

	/**
	 * 构造函数：使用i18配置文件
	 * @param i18nKey
	 * @param i18nResource
	 */
	public Message(String i18nKey,String i18nResource){
		//当参数中的i18nKey为空时，使用全局的配置文件
		if(i18nResource==null)i18nResource = ATHENA_COMMON_I18N;
		//加载配置文件
		
		//根据i18nKey取值
		try {
			ResourceBundle rs = CachingResourceBundleFactory.getResourceBundle(i18nResource, 
					CachingResourceBundleFactory.getDefaultLocale());
			this.message = rs.getString(i18nKey);
		} catch (MissingResourceException e) {
			this.message = i18nKey;
		}
	}
	
	/**
	 * 构造函数：普通信息
	 * @param message
	 */
	public Message(String message) {
		super();
		this.message = message;
	}

	/**
	 * 获取message
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 获取message，使用参数rValues替换message中的形如{0},{1}的字串
	 * @param rValues
	 * @return
	 */
	public String getMessage(Object[] rValues){
		//使用MessageFormat格式化文本
		return MessageFormat.format(message, rValues);
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
