package com.athena.print.controller;

import org.apache.log4j.Logger;

import com.athena.component.runner.Runner;
import com.athena.print.service.PrinterInterrupService;
import com.athena.print.service.PrinterTaskExecutorService;
import com.athena.util.athenalog.impl.BackgroundRunLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/*
 * 打印总控制器  计时器来定时调用
 */
@Component
public class PrintQtaskController {
	
	private static Logger logger=Logger.getLogger(PrintQtaskController.class);
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject 
	private PrinterInterrupService printerInterrp;
	
	@Inject
	private PrinterTaskExecutorService printerTaskExecutor;
	
	@Inject
	private BackgroundRunLog backgroundRunLog;

	
	public void startController(){
		
		//启动线程的数量
		Runner runner= new Runner(50);
		
		try{
		}catch( Exception e ){
			logger.error(e.getMessage());
		}finally{
			runner.shutdown();
		}
		
	}
	
}
