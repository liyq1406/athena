/**
 * 
 */
package com.athena.authority;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

/**
 * com.athena.component.security.LoginSuccessfulStrategy
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
public class LoginSuccessfulStrategy extends AtLeastOneSuccessfulStrategy {

	public LoginSuccessfulStrategy(){
		
	}
	
	@Override
	public AuthenticationInfo afterAttempt(Realm realm,
			AuthenticationToken token, AuthenticationInfo singleRealmInfo,
			AuthenticationInfo aggregateInfo, Throwable t)
			throws AuthenticationException {
		if(t instanceof AuthenticationException){
			//System.out.println("登录异常信息:"+t.getMessage());
			throw (AuthenticationException)t;
		}
		return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
	}

}
