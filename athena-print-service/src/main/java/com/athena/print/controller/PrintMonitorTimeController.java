package com.athena.print.controller; 

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.service.PrinteMonitorService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

@Component
public class PrintMonitorTimeController implements Runnable {

	private static Logger logger=Logger.getLogger(PrintMonitorController.class); 
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject 
	private PrintMonitorController printMonitorController;
	
	//定时调用    打印监听   总控制器
	public void run() {
		long startTime = System.currentTimeMillis();
		logger.info("************************任务开始时间:"+startTime);
		final List<PrintDevicegroup> groups = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup");
		ExecutorService exec = Executors.newFixedThreadPool(groups.size());
		exec.execute(new Runnable(){
			public void run(){
				try {
					if( groups.size() > 0 ){	//打印机组数量
						for( int i =0; i<groups.size(); i++){
							PrintDevicegroup pg = groups.get(i);	//打印机组信息				
							new PrinteMonitorService(pg,baseDao).execute();	//子线程
						}
					}
				} catch(Exception e) {
					logger.error(e.getMessage());
				} finally {
					
				}
			}
		});
		exec.shutdown();
		//exec.awaitTermination(timeout, unit)
		//启动监听
		//printMonitorController.monitorStart();
		
		long endTime = System.currentTimeMillis();
		logger.info("************************任务执行时间************:"+ ( (endTime-startTime)/1000) );
	}

	
	
}
