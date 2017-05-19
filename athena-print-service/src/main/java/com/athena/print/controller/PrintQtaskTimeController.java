package com.athena.print.controller;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

@Component
public class PrintQtaskTimeController extends TimerTask {

	private static Logger logger=Logger.getLogger(PrintMonitorController.class);
	
	@Inject 
	private PrintQtaskController printQtaskController;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	//定时调用   打印中和打印中断   总控制器
	public void run() {
		long startTime = System.currentTimeMillis();
		logger.info("************************任务开始时间:"+startTime);
		
		//启动打印服务
		printQtaskController.startController();
		
		long endTime = System.currentTimeMillis();
		logger.info("任务结束时间************************:"+endTime);
		logger.info("************************任务执行时间************:"+ ( (endTime-startTime)/1000) );
	}

}
