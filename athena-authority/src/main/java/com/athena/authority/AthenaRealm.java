/**
 * 
 */
package com.athena.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import com.athena.authority.service.MenuDirectoryService;
import com.athena.authority.service.UserService;

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
public class AthenaRealm extends AuthorizingRealm implements Realm {
	
	private static final Log logger = LogFactory.getLog(AthenaRealm.class);
	
	private UserService userService;
	
	private MenuDirectoryService menuDirectoryService;
	
	/* 
     * 登录时调用
     * (non-Javadoc)
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //we can safely cast to a UsernamePasswordToken here, because this class 'supports' UsernamePasswordToken
        //objects.  See the Realm.supports() method if your application will use a different type of token.
    	if(!(token instanceof AthenaLoginFormToken)){
    		throw new IllegalArgumentException("登录对象["+token.getClass()+"]类型错误!");
    	}
    	AthenaLoginFormToken upToken = (AthenaLoginFormToken) token;
    	String username = upToken.getUsername();
    	String usercenter = upToken.getAgencyId();
        if(upToken.isDynamicCheck()&&upToken.getDynamicCode()==null){
        	//动态验证码
        	throw new DynamicCodeException();
        }
        AthenaAccount account = null;
		try {
			account = getAccount(username, usercenter);
		} catch (Exception e) {
			//动态验证码
        	throw new AccountStopException();
		}
        SecurityUtils.getSubject().getSession().setAttribute("SESSION_ACCOUNT_USER",account);
        logger.info("登录信息---："+account);
        return account;
    }

    /*
     * 	 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     *  (non-Javadoc)
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AthenaUser user = (AthenaUser) getAvailablePrincipal(principals);
        //TODO  
        //this.getAuthorizationInfo(principals);
        try {
			return getAccount(user.getUsername(),null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    private AthenaAccount getAccount(String username,String usercenter) throws Exception{
    	 //基本账户信息
        AthenaAccount account = userService.getAccount(username, getName(), usercenter);
        return account;
    }

//	filterChainDefinitions

    public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public MenuDirectoryService getMenuDirectoryService() {
		return menuDirectoryService;
	}

	public void setMenuDirectoryService(MenuDirectoryService menuDirectoryService) {
		this.menuDirectoryService = menuDirectoryService;
	}
}
