/**
 * 
 */
package com.athena.authority;

import javax.servlet.http.HttpServletRequest;

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
public interface MenuHtmlBuilder {
	
	/**
	 * 创建登录用户菜单html，
	 * 并手工放入session中
	 * @param request
	 * @param account
	 */
	public void createMenuHtml(HttpServletRequest request,AthenaAccount account);
}
