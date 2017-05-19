/**
 * 
 */
package com.athena.authority;

import org.apache.shiro.authc.UsernamePasswordToken;

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
public class AthenaLoginFormToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8866503207510464030L;
	
	private String verificationCode;//校验码
	
	private String dynamicCode;//动态码
	
	private boolean isDynamicCheck;//是否开启动态码校验
	
	private String contextPath;//上下文路径
	
	private String agencyId;//登录机构唯一标识

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public AthenaLoginFormToken(String username, String password,
			boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getDynamicCode() {
		return dynamicCode;
	}

	public void setDynamicCode(String dynamicCode) {
		this.dynamicCode = dynamicCode;
	}

	public boolean isDynamicCheck() {
		return isDynamicCheck;
	}

	public void setDynamicCheck(boolean isDynamicCheck) {
		this.isDynamicCheck = isDynamicCheck;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

}
