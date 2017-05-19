/**
 * 
 */
package com.athena.authority.log;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.service.LogService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.monitor.MonitorException;
import com.athena.util.exception.ServiceException;
import com.toft.mvc.interceptor.supports.log.LogContext;
import com.toft.mvc.interceptor.supports.log.LogDefinition;
import com.toft.mvc.interceptor.supports.log.LogHandler;

/**
 * <p>Title:SDC 权限管理</p>
 *
 * <p>Description:日记组件</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class AthenaLogHandler implements LogHandler{

	private static final Log logger = LogFactory.getLog(AthenaLogHandler.class);
	
	private LogService busLogService;
	
	private String ip;//用户访问IP
	
	private String url;//用户访问URL
	
	private LoginUser loginUser;//用户信息
	
	/* 
	 * (non-Javadoc)
	 * @see com.toft.mvc.interceptor.supports.log.LogHandler#handler(com.toft.mvc.interceptor.supports.log.LogContext)
	 */
	public void handler(LogContext logContext) {
		
	}

	public void convert(HttpServletRequest request, LogContext logContext) {
		this.ip = request.getRemoteAddr();//IP
		this.url = request.getRequestURI();//URI
		this.loginUser = AuthorityUtils.getSecurityUser();//登录用户信息
		request.setAttribute("Toft_SessionKey_UserData", loginUser);
	}

	public LogService getBusLogService() {
		return busLogService;
	}

	public void setBusLogService(LogService busLogService) {
		this.busLogService = busLogService;
	}
	
}
