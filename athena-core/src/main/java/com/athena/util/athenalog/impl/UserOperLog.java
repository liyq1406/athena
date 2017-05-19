package com.athena.util.athenalog.impl;

import org.apache.log4j.Logger;

import com.athena.db.ConstantDbCode;
import com.athena.util.athenalog.AthenaLog;
import com.athena.util.athenalog.IUserOperLog;
import com.athena.util.athenalog.SysLog;
import com.athena.util.date.DateUtil;
import com.athena.util.uid.CreateUid;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 用户级操作日志接口实现类
 * @author zhangl
 *
 */
@Component
public class UserOperLog extends AthenaLog implements IUserOperLog {

	static Logger logger = Logger.getLogger(UserOperLog.class);
	
	@Inject
	/**
	 * 注入的数据源
	 */
	protected AbstractIBatisDao baseDao;
	
	@Override
	public void addCorrect( String module, String title, String contents )
			throws RuntimeException {

		SysLog sysLog = this.createSysLog();
		
		sysLog.setCflag("1");
		sysLog.setClevel("0");
		
		module = this.nulltoKong(module);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		
		sysLog.setModule_name(module);
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).execute("dailog.correctlog_insert", sysLog);
		
		logger.info(sysLog.toString());

	}

	@Override
	public void addError(String module, String title, String contents, String errorClass,
			String errorContent) throws RuntimeException {

		SysLog sysLog = this.createSysLog();
		
		sysLog.setCflag("0");
		sysLog.setClevel("2");
		
		module = this.nulltoKong(module);
		errorClass = this.nulltoKong(errorClass);
		errorContent = this.nulltoKong(errorContent);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		
		sysLog.setModule_name(module);
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);
		sysLog.setCclass(errorClass);
		sysLog.setCexception(errorContent);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).execute("dailog.errorlog_insert", sysLog);
		
		logger.error(sysLog.toString());

	}
	
	@Override
	public void addCorrectPDA(SysLog sysLog)
			throws RuntimeException {

		sysLog.setCflag("1");
		sysLog.setClevel("0");
		sysLog.setCid(CreateUid.getUID(20));				//主键
		sysLog.setCreate_time(DateUtil.curDateTime());		//创建时间
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).execute("dailog.correctlog_insert", sysLog);
		
		logger.info(sysLog.toString());

	}

	@Override
	public void addErrorPDA(SysLog sysLog) throws RuntimeException {
		
		sysLog.setCflag("0");
		sysLog.setClevel("2");
		sysLog.setCid(CreateUid.getUID(20));				//主键
		sysLog.setCreate_time(DateUtil.curDateTime());		//创建时间

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).execute("dailog.errorlog_insert", sysLog);
		
		logger.error(sysLog.toString());

	}
	
	
    /**
     * 写DDBH层数据库正常日志
     * @param module
     * @param title
     * @param contents
     * @throws RuntimeException
     */
	public void addCorrectDDBH( String module, String title, String contents )
			throws RuntimeException {
		SysLog sysLog = this.createSysLog();
		sysLog.setCflag("1");
		sysLog.setClevel("0");
		
		module = this.nulltoKong(module);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		
		sysLog.setModule_name(module);
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("dailog.correctlog_insert", sysLog);
		
		logger.info(sysLog.toString());

	}

	
	/**
	 * 写DDBH层数据库错误日志
	 * @param module
	 * @param title
	 * @param contents
	 * @param errorClass
	 * @param errorContent
	 * @throws RuntimeException
	 */
	public void addErrorDDBH(String module, String title, String contents, String errorClass,
			String errorContent) throws RuntimeException {
		SysLog sysLog = this.createSysLog();
		sysLog.setCflag("0");
		sysLog.setClevel("2");
		
		module = this.nulltoKong(module);
		errorClass = this.nulltoKong(errorClass);
		errorContent = this.nulltoKong(errorContent);
		title = this.nulltoKong(title);
		contents = this.nulltoKong(contents);
		
		sysLog.setModule_name(module);
		sysLog.setTrans_desc(title);
		sysLog.setTrans_content(contents);
		sysLog.setCclass(errorClass);
		sysLog.setCexception(errorContent);

		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("dailog.errorlog_insert", sysLog);
		
		logger.error(sysLog.toString());

	}
	
	
}
