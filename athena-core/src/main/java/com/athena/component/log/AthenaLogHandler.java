/**
 * 
 */
package com.athena.component.log;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.monitor.MonitorException;
import com.toft.core3.container.annotation.Component;
import com.toft.mvc.interceptor.supports.log.LogContext;
import com.toft.mvc.interceptor.supports.log.LogHandler;

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
@Component(value="logHandler")
public class AthenaLogHandler implements LogHandler {
	
	private static final Log logger = LogFactory.getLog(AthenaLogHandler.class);
	
	/* (non-Javadoc)
	 * @see com.toft.mvc.interceptor.supports.log.LogHandler#handler(com.toft.mvc.interceptor.supports.log.LogContext)
	 */
	@Override
	public void handler(LogContext logContext) {
		logger.info("业务日记 :"+logContext.getLogContent());
		
		//
		Throwable error = logContext.getThrowable();
		if(error instanceof MonitorException){
			//写监控信息到日记
			
		}
	}

	/* (non-Javadoc)
	 * @see com.toft.mvc.interceptor.supports.log.LogHandler#convert(javax.servlet.http.HttpServletRequest, com.toft.mvc.interceptor.supports.log.LogContext)
	 */
	@Override
	public void convert(HttpServletRequest request, 
			LogContext logContext) {
		//
		//
	}

}
