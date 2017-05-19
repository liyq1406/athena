/**
 * 
 */
package com.athena.authority.action;

import org.apache.shiro.SecurityUtils;

import com.athena.authority.util.MenuUtils;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;

/**
 * <p>Title:SDC 权限</p>
 *
 * <p>Description:系统登录</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LoginAction extends ActionSupport{
	/**
	 * 登录主页面
	 * @return
	 */
	public String login(){
		return "login";
	}
	/**
	 * 退出登录
	 * @return
	 */
	public String logout(){
		SecurityUtils.getSubject().logout();
		return "logout";
	}
	/**
	 * 
	 * @return
	 */
	public String moduleChange(){
		String moduleIndex = this.getParam("moduleIndex");
		
		//其他处理
		this.setSessionAttribute("moduleIndex", moduleIndex);
		this.setSessionAttribute(MenuUtils.MENU_PARAMS_SELECTED, "");
		return "select";
	}
}
