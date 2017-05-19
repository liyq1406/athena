package com.athena.util.cache;

import com.toft.core3.support.PageableSupport;

/**
 * 缓存属性值
 * @author WL
 * @date 2012-01-16
 */
public class CacheValue extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 键
	 */
	private String key;
	
	/**
	 * 值
	 */
	private String value;
	
	/**
	 * 用户中心
	 */
	private String usercenter;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
}
