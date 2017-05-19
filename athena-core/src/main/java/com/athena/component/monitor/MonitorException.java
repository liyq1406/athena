/**
 * 
 */
package com.athena.component.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.util.exception.ServiceException;

/**
 * <p>Title:</p>
 *
 * <p>Description:集中监控平台报警异常类</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class MonitorException extends ServiceException{
	 private Log logger = LogFactory.getLog(MonitorException.class);
	   
	/**
	 * 
	 */
	private static final long serialVersionUID = 7755008979957966804L;
	
	private final static String ERROR_LEVEL_1 = "1";
	
	private final static String ERROR_LEVEL_2 = "2";
//	
//	private final static String ERROR_LEVEL_3 = "3";
//	
//	private final static String ERROR_LEVEL_4 = "4";
//	
//	private final static String ERROR_LEVEL_5 = "5";
	
	public final static AlertInfo ERROR_INFO_TRANSACTION = 
		new AlertInfo("0003",ERROR_LEVEL_2);//关键交易错误
	
	public final static AlertInfo ERROR_INFO_DB_CONNECT = 
		new AlertInfo("0005",ERROR_LEVEL_1);//数据库连接失败
	
	public final static AlertInfo ERROR_INFO_TUXEDO_CONNECT = 
		new AlertInfo("0006",ERROR_LEVEL_1);//TUXEDO连接错误
	
	private static SystemMonitor systemMonitor;
	
	private AlertInfo alertInfo;//故障编码
	
	public MonitorException(AlertInfo alertInfo,String message){
		super(message);
		this.alertInfo = alertInfo;
	}
	
	public MonitorException(AlertInfo alertInfo,Throwable error){
		super(error);
		this.alertInfo = alertInfo;
	}
	
	public MonitorException(AlertInfo alertInfo,String message,Throwable error){
		super(message,error);
		this.alertInfo = alertInfo;
	}
	/**
	 * 发送报警信息到集中监控平台
	 */
	public void sendAlert(String[] alertlevels){
		if(systemMonitor==null){
			systemMonitor = new SystemMonitor();
		}
		
		boolean send = false;
		for(String level:alertlevels){
			if(level.equals(alertInfo.getLevel())){
				send = true;
				break;
			}
		}
		if(send){
			logger.error("系统故障并发送到集中监控平台，故障码【"+alertInfo.getCode()+"】，错误信息："+this.getMessage());
			//调用集中监控平台方法
			//systemMonitor.sendAlert("", alertInfo.getCode(), alertInfo.getLevel(), this.getMessage());
		}
	}
}
