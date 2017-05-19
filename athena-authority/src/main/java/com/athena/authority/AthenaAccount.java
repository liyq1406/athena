/**
 * 
 */
package com.athena.authority;

import java.util.Set;

import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.subject.SimplePrincipalCollection;

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
public class AthenaAccount extends SimpleAccount {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7916042978293311561L;
	
	private AthenaUser toftUser;//登录用户信息
	
	private Set<String> menuAndButtonsIds;//用户可见的菜单和按钮
	
	//完整的菜单树
	//当前模块ID
	
	private boolean permissionChanged = true;//权限是否有更新
	
	public AthenaAccount(AthenaUser user,String realm){
		super(user.getUsername(),user.getPassword(),realm);
		this.toftUser = user;
		SimplePrincipalCollection principals = new SimplePrincipalCollection();
		principals.add(user, realm);
		this.setPrincipals(principals);
	}

	public boolean isPermissionChanged() {
		return permissionChanged;
	}

	public void setPermissionChanged(boolean permissionChanged) {
		this.permissionChanged = permissionChanged;
	}

	public AthenaUser getToftUser() {
		return toftUser;
	}

	public void setToftUser(AthenaUser toftUser) {
		this.toftUser = toftUser;
	}

	public Set<String> getMenuAndButtonsIds() {
		return menuAndButtonsIds;
	}

	public void setMenuAndButtonsIds(Set<String> menuAndButtonsIds) {
		this.menuAndButtonsIds = menuAndButtonsIds;
	}
	
	
}
