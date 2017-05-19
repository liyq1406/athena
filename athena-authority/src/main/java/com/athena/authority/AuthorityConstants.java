/**
 * 
 */
package com.athena.authority;

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
public final class AuthorityConstants {
	private AuthorityConstants(){
		
	}
	/**
	 * 模块命名空间
	 */
	public final static String MODULE_NAMESPACE = "authority";
	//默认密码
	public final static String DEFAULT_USRR_PASSWORD = "123456";
	//session中记录登录帐号的属性名称
	public final static String SESSION_ACCOUNT_USER = "SESSION_ACCOUNT_USER";
	//开发用户帐号
	public final static String DEV_ACCOUNT_USER = "ROOT";
	//菜单内容创建器配置的key
	public final static String KEY_MENUHTML_BUILDER  = "menuHtml.builder";
	//横向菜单html存储的属性名称
	public final static String SESSION_SYS_LOGIN_H_MENUHTML = "SYS_LOGIN_H_MENUHTML";
	//纵向菜单html存储的属性名称
	public final static String SESSION_SYS_LOGIN_MENUHTML = "SYS_LOGIN_MENUHTML";
	
	public final static String SESSION_SYS_MENU = "SYS_MENU";
}
