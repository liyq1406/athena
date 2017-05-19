/**
 * 
 */
package com.athena.authority;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.db.ConstantDbCode;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;

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
public final class AthenaFormAuthenticationFilter extends FormAuthenticationFilter {
	public static final String FORM_DYNAMIC_CODE = "FORM_DYNAMIC_CODE";
	
	public static final String DEFAULT_DYNAMICCODE_PARAM = "dynamicCode";
    public static final String DEFAULT_VERIFICATIONCODE_PARAM = "verificationCode";
    public static final String DEFAULT_AGENCYID_PARAM = "agencyId";
    
    private boolean isDynamicCheck;//动态校验
    private final Log log = LogFactory.getLog(AthenaFormAuthenticationFilter.class); 
//	private String dynamicUrl = "dynamic.jsp";
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		String username = "";
		String password = "";
		try{
			Md5Hash encryptedPassword =  new Md5Hash(getPassword(request));
			if(encryptedPassword!=null){
				password =  encryptedPassword.toHex();
			}
			username = getUsername(request);
		}catch(Exception e){
			log.error(e);
		}
		AthenaLoginFormToken athenaLoginFormToken = new AthenaLoginFormToken(username,password,this.isRememberMe(request),this.getHost(request));
		athenaLoginFormToken.setDynamicCode(getDynamicCode(request));
		athenaLoginFormToken.setVerificationCode(getVerificationCode(request));
		athenaLoginFormToken.setDynamicCheck(isDynamicCheck);
		athenaLoginFormToken.setAgencyId(getAgencyId(request));
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		athenaLoginFormToken.setContextPath(httpRequest.getContextPath());
		return athenaLoginFormToken;
	}

	private String getVerificationCode(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_VERIFICATIONCODE_PARAM);
	}

	private String getDynamicCode(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_DYNAMICCODE_PARAM);
	}
	
	private String getAgencyId(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_AGENCYID_PARAM);
	}

	/*
	 * 登录失败处理
	 * 1：登录失败的信息转换
	 * 2：动态校验码的启用
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		//动态校验码
		if(isDynamicCheck&&e instanceof DynamicCodeException){
			request.setAttribute("dynamicCheck", true);
			request.setAttribute(getUsernameParam(), getUsername(request));
			request.setAttribute(getPasswordParam(),getPassword(request));
		}
		boolean flag = super.onLoginFailure(token, e, request, response);
		if(e instanceof UnknownAccountException){
			request.setAttribute("msg", "用户名错误,请重新输入!");
		}else if(e instanceof IncorrectCredentialsException){
			request.setAttribute("msg", "密码错误,请重新输入!");
		}else if(e instanceof AccountStopException){
			request.setAttribute("msg", "用户名已停用,请重新输入!");
		}
		return flag;
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response) {
		//boolean success = super.onLoginSuccess(token, subject, request, response);
		
		boolean success = false;
		//登陆成功后，根据主页标识决定跳转到哪个主页
		
		boolean isSkip = true;
		HttpServletRequest hsr = (HttpServletRequest) request;
		HttpServletResponse r = (HttpServletResponse) response;

		WebSdcContext sdcContext = WebSdcContextUtils.getWebSdcContext(hsr.getSession().getServletContext());
		AbstractIBatisDao baseDao = sdcContext.getComponent(AbstractIBatisDao.class);
		//查找当前用户对应的用户组，是否有 仓库组 
		//有仓库组  则 将主页设置为  仓库的主页
		LoginUser user = new LoginUser();
		user.setUsername(request.getParameter("username"));
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(AuthorityConstants.MODULE_NAMESPACE+".isHasCKSkip",user);
		BigDecimal db = (BigDecimal) ((HashMap)(list.get(0))).get("ID");
		int count = db.intValue();
		if(count>0){
			//则有仓库的权限菜单，则认为可以进仓库的主页
			isSkip = false;
		}
		if(isSkip){
			SecurityUtils.getSubject().getSession().setAttribute("sysLoginMode", "nck");
			try {
				success = super.onLoginSuccess(token, subject, request, response);
			} catch (Exception e) {
				//e.printStackTrace();
				log.error("登录失败");
			}
		}else{
			SecurityUtils.getSubject().getSession().setAttribute("sysLoginMode", "yck");
			try {
				r.sendRedirect(hsr.getContextPath()+"/index_ck.jsp");
			} catch (IOException e) {
				log.error("跳转仓库页面失败");
			}
		}
		
		return success;
	}
	
}
