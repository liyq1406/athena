package com.athena.print.controller;


import org.apache.log4j.Logger;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@Component
public class GetQtaskTimeController implements Runnable {

	private static Logger logger=Logger.getLogger(PrintMonitorController.class);
	
	@Inject 
	private GetQtaskController getQtaskController;
	
	//定时调用   取作业的  总控制器
	public void run() {
		long startTime = System.currentTimeMillis();
		logger.info("任务开始时间:"+startTime);
		
		//ToatlController controller = new ToatlController(baseDao);
		
		//启动打印服务
		getQtaskController.startQtaskController();
		
		long endTime = System.currentTimeMillis();
		logger.info("任务结束时间:"+endTime);
		logger.info("任务执行时间:"+ ( (endTime-startTime)/1000) );
	}
	


}
