/**
 * 
 */
package com.athena.authority.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.athena.authority.AthenaUser;
import com.athena.authority.entity.LoginUser;
import com.toft.core3.context.SdcContext;


/**
 *
 * <p>Description:授权工具类</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public final class AuthorityUtils {
	
	private AuthorityUtils(){
		
	}
	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	public final static LoginUser getSecurityUser(){
		Object principal = null;
		
		try{
			principal = SecurityUtils.getSubject().getPrincipal();  
			
		}catch(Exception e){ 
			return null;
		} 
		return (LoginUser)principal;
	}
	
	/**
	 * 转换shiro的登录异常信息
	 * @param request
	 * @return
	 */
	public final static String convertLoginErrorMessage(HttpServletRequest request){
		//从request中获取shiro登录的异常对象
		Object loginError = request
				.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String errorMessage = null;
		//TODO 可配置的异常转换
		if ("org.apache.shiro.authc.UnknownAccountException".equals(loginError)) {
			errorMessage = "未知帐号错误！";
		} else if ("org.apache.shiro.authc.IncorrectCredentialsException"
				.equals(loginError)) {
			errorMessage = "密码错误！";
		} else if ("org.apache.shiro.authc.AuthenticationException"
				.equals(loginError)) {
			errorMessage = "认证失败！";
		}
		return errorMessage==null?"":errorMessage;
	}
	
	/**
	 * 是否为开发用户（全权限，仅开发阶段使用）
	 * @param username
	 * @return
	 */
	public static boolean isDeveloper(SdcContext sdcContext,AthenaUser user){
		if(user==null){
			return false;
		}
		String developUser = sdcContext.getMessage("user.developer");
		return user.getUsername().equals(developUser);
	}
}
