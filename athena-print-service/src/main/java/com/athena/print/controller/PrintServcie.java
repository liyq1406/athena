package com.athena.print.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.athena.component.runner.Runner;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.service.PrinterInterrupService;
import com.athena.print.service.PrinterTaskExecutorService;
import com.athena.util.athenalog.impl.BackgroundRunLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


@Component
public class  PrintServcie {

	private static Logger logger=Logger.getLogger(PrintServcie.class);
	
	public boolean flag = false;
	
	private boolean flag1 = false;
	
	private PrinterInterrupService printerInterrp;
	private PrinterTaskExecutorService printerTaskExecutor;
	

	//PrintServcie 单例
	private static PrintServcie instance;		//单例模式
	
	public static PrintServcie getInstance(){		//单例模式类获取
		if( null == instance ){
			instance = new PrintServcie();       //只有一个对象能访问此类         
		}
		
		return instance;
	}
	
	//构造函数
	public PrintServcie(){
		
		if( null == printerInterrp ){
			printerInterrp = new PrinterInterrupService();
		}
		
//		if( null == printerTaskExecutor ){
//			printerTaskExecutor = new PrinterTaskExecutorService();
//		}
	}

	
	
	//执行打印服务
	public void print(AbstractIBatisDao baseDao,BackgroundRunLog backgroundRunLog) throws RuntimeException {
		
		while(searchQtask(baseDao)){
			
			flag = true;
			//启动线程的数量
			Runner runner= new Runner(10);
			try{
				logger.info("********************打印中断的服务开始***************************");
				//打印中 或  打印中断的服务启动
				printerInterrp.runInterrpTasks(baseDao);
				
				logger.info("********************新作业的服务开始***************************");
				//新作业的服务启动
				//printerTaskExecutor.runTasks(baseDao);
			
			}catch( RuntimeException e ){
				logger.error(e.getMessage());
			}finally{
				runner.shutdown();
				flag = false;
				if(flag1){
					break;
				}
			}
		}
		//flag = false;
	
	}
	
	//查找作业队列表中是否有新增的作业、打印中断 或打印中的作业
	private boolean searchQtask(AbstractIBatisDao baseDao) throws RuntimeException {
		List<PrintQtaskmain> printQatskmainList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskmainenable");
		List<PrintQtaskmain> printQatskmainLists = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskmainenables");
		if(0==printQatskmainList.size()&& 0==printQatskmainLists.size()){
			return false;
		}
		else{
			flag1=true;
			return true;
		}
	}

}
