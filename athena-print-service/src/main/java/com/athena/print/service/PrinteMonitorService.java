/**
 * 
 */
package com.athena.print.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;


import com.athena.db.ConstantDbCode;

import com.athena.print.Constants;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.print.coon.PrintStatus;
import com.athena.print.core.SnmpFactory;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * <p>Title:打印子系统</p>
 *
 * <p>Description:打印机监听</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author wy
 * @version 1.0
 */
@Component
public class PrinteMonitorService{
	
	private static Logger logger=Logger.getLogger(PrinteMonitorService.class);
	
	private AbstractIBatisDao baseDao;
		
	private PrintDevicegroup devicegroup;
	
	public PrintDevicegroup getDevicegroup() {
		return devicegroup;
	}
	public void setDevicegroup(PrintDevicegroup devicegroup) {
		
		this.devicegroup = devicegroup;
	}

	public PrinteMonitorService(PrintDevicegroup devicegroup,AbstractIBatisDao baseDao){
		super();
		this.devicegroup = devicegroup;
		this.baseDao = baseDao;
	}
	
	public PrinteMonitorService(){
		super();
	}
	

	public void execute() {	
		
		try{

			//根据打印机组编号查询打印机表，获取打印机组下打印机信息列表
			List<PrintDevicestatus> printdevice =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicelist", devicegroup.getSpcodes());
			int size = printdevice.size();	//组下打印机数量
			if( size > 0 ){
				for (int i = 0; i < size; i++) {
					PrintDevicestatus pt = printdevice.get(i);	//打印机对象
					pt.setSocketPort(9100);					//设置打印端口
					//连接打印机并获取打印机状态
					PrintStatus status =SnmpFactory.getInstance().getPrintStatus(pt);
					if( null!=status ){
						logger.info(Thread.currentThread().getName()+"打印机组编号【"+pt.getSpcodes()+"】"+"打印机【"+pt.getSip()+"】状态："+status.value());
						//根据获得的打印机状态码来判断
						getStatus(status,pt);
					}else{
						//更新打印机设备的状态为网路故障
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus4", pt.getSip());
						continue;
					}
				}
			}
			
		}catch( Exception e ){
			logger.error(Thread.currentThread().getName() + e.getMessage());
		}
	}


	
	/**
	 * 根据获得的打印机状态码来判断
	 */
	public  void  getStatus(PrintStatus status,PrintDevicestatus pd){
		
		int m = status.finalValue(status);
		
		if(Constants.PIRNTER_JAM_PAPER==m){
			//更新打印机状态为 卡纸
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus3", pd.getSip());
			
		}else if(Constants.PIRNTER_OUT_PAPER==m){
			//更新打印机状态为 缺纸
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus2", pd.getSip());
			
		}else if(Constants.PIRNTER_CONN_BROKEN==m){
			//是否存在作业号
			 if( null!= pd.getLastqid()){
				
				 getQtask(pd); //根据作业号 查询打印队列详细表中 是否有未发送的子任务来更新打印机状态
				 
			 }else{
				//更新打印机状态为  网络故障    同时更新主状态为 不占用
				 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus4", pd.getSip());
			 }
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus4", pd.getSip());
		}else if(Constants.PIRNTER_BUSY==m){
			//更新打印机状态为 繁忙
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus1", pd.getSip());
			//同时更新打印作业为 打印中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBylastqid", pd.getLastqid());
		}else if(Constants.PIRNTER_IDLE==m){
			//是否存在作业号
			//|| !"".equals(pd.getLastqid())
			 if( null!= pd.getLastqid()){
				
				 getQtask(pd); //根据作业号 查询打印队列详细表中 是否有未发送的子任务来更新打印机状态
				 
			 }else{
				//更新打印机状态为 空闲
				 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus0", pd.getSip());
			 }
		}else{
			//更新打印机状态为 打印机故障
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus5", pd.getSip());
		}
	}
	
		
		
		/**
		 * 根据作业号 查询打印队列详细表中 是否有未发送的子任务来更新打印机状态
		 */
		public void getQtask(PrintDevicestatus pd){
			
			List<PrintQtaskinfo> printQtaskinfoList =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("pcenter.queryPrintQtaskinfo1", pd.getLastqid());
			
				if(0!=printQtaskinfoList.size()){
						//存在未发送的子任务 修改打印机   辅状态为  空闲
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus0", pd.getSip());
				}else{
						// 不存在未发送的子任务 主队列的作业状态为 打印完成 辅状态为停止
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain1", pd.getLastqid());
						//同时更新作业的完成时间
						PrintQtaskmain pk = new PrintQtaskmain();
						pk.setQid(pd.getLastqid());
						pk.setFinishedtime(DateUtil.curDateTime());
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmaintime", pk);
						//删除主队列表中   已发送的子任务
						//List<PrintQtaskinfo> printQtaskList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfo2",pd.getLastqid());
			// for (int i = 0; i < printQtaskList.size(); i++) {
			// File dir = new File(printQtaskList.get(i).getSfilename());
			// Delete(dir);
			// }
					//修改打印机主状态为  不占用    辅状态为  空闲  作业号置为空  
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusByqid1", pd.getSip());
				}
		}
		
		
		//删除备份在服务器上的作业的文档 txt
		@SuppressWarnings("unused")
		private void Delete(File dir){
			if(dir.isFile()){
				//该路径下是否是一个标准的文件
					dir.delete();
			}else{
				logger.info("文件不存在");
			}
			
		}
}
