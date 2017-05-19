/**
 * 
 */
package com.athena.authority;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;

/**
 * <p>Title:SDC 安全控制组件</p>
 *
 * <p>Description: 安全控制管理类</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class AthenaWebSecurityManager extends DefaultWebSecurityManager
		 implements WebSecurityManager{
	
	private AthenaRealmAuthenticator toftAuthenticator;
	
	public void init(){
		this.setAuthenticator(toftAuthenticator);
	}

	public AthenaRealmAuthenticator getToftAuthenticator() {
		return toftAuthenticator;
	}

	public void setToftAuthenticator(AthenaRealmAuthenticator toftAuthenticator) {
		this.toftAuthenticator = toftAuthenticator;
	}
	
	
}
