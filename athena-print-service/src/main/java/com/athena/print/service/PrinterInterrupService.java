package com.athena.print.service;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.athena.component.runner.Runner;
import com.athena.db.ConstantDbCode;
import com.athena.print.coon.ComparatorPrintQtaskmain;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.BackgroundRunLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * <p>
 * Title:打印监管平台
 * </p>
 * 
 * <p>
 * Description:打印任务执行服务(打印中断作业 和 打印中作业)
 * </p>
 * 
 * <p>
 * Copyright:Copyright (c) 2011.9
 * </p>
 * 
 * <p>
 * Company:iSoftstone
 * </p>
 * 
 * @author Administrator
 * @version 1.0
 */
@Component
public class PrinterInterrupService{
	
	private static Logger logger=Logger.getLogger(PrinterInterrupService.class);
	
	
	private AbstractIBatisDao baseDao;
	
	public AbstractIBatisDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(AbstractIBatisDao baseDao) {
		this.baseDao = baseDao;
	}



	/**
	 * 执行   打印中断 和 打印中   打印任务
	 */
	public void runInterrpTasks(AbstractIBatisDao baseDao)throws RuntimeException {
	
		//获取   打印中断  此时打印机已分配    无需在从新分配
		try {
			
			//获取  打印中断  的作业  List 对List按创建时间排序    此时打印机的主状态一直是占用状态  只需看辅状态
			List<PrintQtaskmain> pList =  getPrintTask(baseDao);
			
			if( 0!=pList.size() ){
				//循环 打印中断  的作业
				for ( PrintQtaskmain printQtask : pList) {
					new PrinterInterrupCommandService(printQtask,baseDao).execute();
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		} 
	}
		
	
	//获取  打印中断  的作业  List 对List按创建时间排序    此时打印机的主状态一直是占用状态  只需看辅状态
	private List<PrintQtaskmain> getPrintTask(AbstractIBatisDao baseDao) throws RuntimeException{

		//查询出主作业状态中为   打印中断 
	  	List<PrintQtaskmain> printQtaskmainList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskmainstatus3");
	  	ComparatorPrintQtaskmain comparatorPrintQtaskmain = new ComparatorPrintQtaskmain();
	  	Collections.sort(printQtaskmainList, comparatorPrintQtaskmain);
	  	return printQtaskmainList;
	}
	
	
}
