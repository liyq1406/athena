package com.athena.util.athenalog.impl;

import org.apache.log4j.Logger;

import com.athena.db.ConstantDbCode;
import com.athena.util.athenalog.AthenaLog;
import com.athena.util.athenalog.IBackgroundRunLog;
import com.athena.util.athenalog.SysLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 后台运行/系统调度 操作日志实现类
 * @author zhangl
 *
 */
@Component
public class BackgroundRunLog extends AthenaLog implements IBackgroundRunLog{

	static Logger logger = Logger.getLogger(BackgroundRunLog.class);
	
	@Inject
	/**
	 * 注入的数据源
	 */
	protected AbstractIBatisDao baseDao;
			
	@Override
	public void addCorrect(String operator, String usercenter, 
			String module, String title, String contents) throws RuntimeException {

		SysLog sysLog = this.createSysLogBy();
		
		operator = this.nulltoKong(operator);
		usercenter = this.nulltoKong(usercenter);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		
		module = this.nulltoKong(module);
		sysLog.setModule_name(module);
		
		sysLog.setOperators(operator);
		sysLog.setUsercenter(usercenter);
		sysLog.setCflag("1");
		sysLog.setClevel("1");
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dailog.correctlog_insert", sysLog);

		logger.info(sysLog.toString());
	}


	@Override
	public void addError(String operator, String usercenter, 
			String module, String title,
			String contents, String errorClass, String errorContent)
			throws RuntimeException {

		SysLog sysLog = this.createSysLogBy();
		
		operator = this.nulltoKong(operator);
		usercenter = this.nulltoKong(usercenter);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		errorClass = this.nulltoKong(errorClass);
		errorContent = this.nulltoKong(errorContent);
		
		module = this.nulltoKong(module);
		sysLog.setModule_name(module);
		
		sysLog.setOperators(operator);
		sysLog.setUsercenter(usercenter);
		
		sysLog.setCflag("2");
		sysLog.setClevel("5");		
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);
		sysLog.setCclass(errorClass);
		sysLog.setCexception(errorContent);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dailog.errorlog_insert", sysLog);
		logger.error(sysLog.toString());
	}


	@Override
	public void addDataError(String operator, String usercenter, 
			String module, String title,
			String contents, String errorClass, String errorContent)
			throws RuntimeException {

		SysLog sysLog = this.createSysLogBy();
		
		operator = this.nulltoKong(operator);
		usercenter = this.nulltoKong(usercenter);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		errorClass = this.nulltoKong(errorClass);
		errorContent = this.nulltoKong(errorContent);
		
		module = this.nulltoKong(module);
		sysLog.setModule_name(module);
		
		sysLog.setOperators(operator);
		sysLog.setUsercenter(usercenter);
		
		sysLog.setCflag("3");
		sysLog.setClevel("3");		
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);
		sysLog.setCclass(errorClass);
		sysLog.setCexception(errorContent);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dailog.errorlog_insert", sysLog);
		logger.error(sysLog.toString());
	}


	@Override
	public void addExceptionError(String operator, String usercenter,
			String module, String title, String contents, String errorClass,
			String errorContent) throws RuntimeException {

		SysLog sysLog = this.createSysLogBy();
		
		operator = this.nulltoKong(operator);
		usercenter = this.nulltoKong(usercenter);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		errorClass = this.nulltoKong(errorClass);
		errorContent = this.nulltoKong(errorContent);
		
		module = this.nulltoKong(module);
		sysLog.setModule_name(module);
		
		sysLog.setOperators(operator);
		sysLog.setUsercenter(usercenter);
		
		sysLog.setCflag("3");
		sysLog.setClevel("4");		
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);
		sysLog.setCclass(errorClass);
		sysLog.setCexception(errorContent);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dailog.errorlog_insert", sysLog);
		logger.error(sysLog.toString());
	}

}
