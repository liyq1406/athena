package com.athena.authority;

import org.apache.shiro.authc.AuthenticationException;

public class DynamicCodeException extends AuthenticationException{
	/**
	 * 动态验证码
	 */
	private String dynamicCode;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3058637004603377259L;
	
	/**
	 * dynamicCode getter方法
	 * @return
	 */
	public String getDynamicCode() {
		return dynamicCode;
	}
	/**
	 * dynamicCode setter方法
	 * @param dynamicCode
	 */
	public void setDynamicCode(String dynamicCode) {
		this.dynamicCode = dynamicCode;
	}

}
