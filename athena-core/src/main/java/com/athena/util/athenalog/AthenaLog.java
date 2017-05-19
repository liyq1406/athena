package com.athena.util.athenalog;

import javax.servlet.http.HttpServletRequest;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.date.DateUtil;
import com.athena.util.uid.CreateUid;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 接口辅助基类
 * @author Zhangl
 *
 */


public class AthenaLog {
	
	/**
	 * 初始化sysLog类-有用户
	 * @return
	 * @throws RuntimeException
	 */
	public SysLog createSysLog() throws RuntimeException {
		
		SysLog sysLog = new SysLog();
		
		sysLog.setCid(CreateUid.getUID(20));				//主键
		
		sysLog.setProcess_name(
				this.nulltoKong(String.valueOf(Thread.currentThread().getId())) + "|" + this.nulltoKong(Thread.currentThread().getName())
						);
		
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		
		if( null != loginUser ){
			sysLog.setUsercenter(loginUser.getUsercenter());	//用户中心
			sysLog.setOperators(loginUser.getUsername());		//用户
		}else{
			sysLog.setUsercenter("");		//用户中心
			sysLog.setOperators("");		//用户
		}
		
		
		if( null != ActionContext.getActionContext() ){
			
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
		
			if( null != request ){
				sysLog.setOperators_ip(request.getRemoteAddr());		//用户ip
				sysLog.setTrans_url(request.getRequestURI());			//用户操作URL
			}else{
				sysLog.setOperators_ip("");			//用户ip
				sysLog.setTrans_url("");			//用户操作URL
			}

		}else{
			sysLog.setOperators_ip("");			//用户ip
			sysLog.setTrans_url("");			//用户操作URL
		}

		sysLog.setCreate_time(DateUtil.curDateTime());		//创建时间
		
		return sysLog;
	}
	
	
	/**
	 * 初始化sysLog类-无用户
	 * @return
	 * @throws RuntimeException
	 */
	public SysLog createSysLogBy() throws RuntimeException {
		
		SysLog sysLog = new SysLog();
		
		sysLog.setCid(CreateUid.getUID(20));				//主键
		
		sysLog.setProcess_name(this.nulltoKong(Thread.currentThread().getName()));
		
		if( null != ActionContext.getActionContext() ){
			
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
		
			if( null != request ){
				sysLog.setOperators_ip(request.getRemoteAddr());		//用户ip
				sysLog.setTrans_url(request.getRequestURI());			//用户操作URL
			}else{
				sysLog.setOperators_ip("");			//用户ip
				sysLog.setTrans_url("");			//用户操作URL
			}

		}else{
			sysLog.setOperators_ip("");			//用户ip
			sysLog.setTrans_url("");			//用户操作URL
		}
		
		sysLog.setCreate_time(DateUtil.curDateTime());		//创建时间
		
		return sysLog;
	}	
	
	/**
	 * null 转 空字符串
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public String nulltoKong(String str) throws RuntimeException{
		
		if( null == str ){
			str = "";
		}
		
		return str;
	}
	


}
