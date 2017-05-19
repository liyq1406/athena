/**
 * 
 */
package com.athena.print.coon;

import java.sql.SQLException;
import java.util.List;

import com.athena.db.ConstantDbCode;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @author Administrator
 * @description 多线程辅助类
 */
public class MultithreadingTool {
	
	private static MultithreadingTool instance;		//单例模式
	
	public static MultithreadingTool getInstance(){		//单例模式类获取
		
		if( null == instance ){
			instance = new MultithreadingTool();       //多线程保证了同一时间只能只能有一个对象访问此同步块                
		}
		return instance;
	}
	
	/**
	 * 新作业同步操作的工作
	 * @param printQtaskinfo
	 * @param printQtaskmain
	 * @param baseDao
	 */
	public synchronized  void newTaskOperate ( 
			List<PrintQtaskinfo> printQtaskinfo, PrintQtaskmain printQtaskmain, AbstractIBatisDao baseDao )throws SQLException{
		
		//把分配的打印机编号写入主作业表中
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain5", printQtaskmain);
		// 更新子任务作业状态为 已执行
		for (int i = 0; i <printQtaskinfo.size(); i++) {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfo1",printQtaskinfo.get(i));
		}
		// 主作业状态为 打印中
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain3",printQtaskmain.getQid());

		// 打印机辅状态为 繁忙
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice2",printQtaskmain.getSdevicecode());

		// 打印机状态表更新主作业任务号、用户编号
		PrintDevicestatus bean = new PrintDevicestatus();
		bean.setLastqid(printQtaskmain.getQid());
		bean.setUsercode(printQtaskmain.getSaccount());
		bean.setSpcode(printQtaskmain.getSdevicecode());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice3",bean);
	}
}
