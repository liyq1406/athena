/**
 * 代码声明
 */
package com.athena.print.module.sys.service;

import java.util.Map;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.athena.print.entity.sys.PrintDevicestatus;

@Component
public class PrintDevicestatusService extends BaseService<PrintDevicestatus>{
	
	
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "sys";
	}
	
	/**
	 * 重打按钮 查询出所有状态为空闲的打印机 （此时不判断是否有权限  直接跨权限）
	 */
	public Map<String,Object> selectStatus(PrintDevicestatus bean){
		//查询出所有的空闲打印机
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("sys.queryPrintDevices",bean,bean);
		
	}
	
	
	/**
	 * 更新
	 */
	public String doUpdate(PrintDevicestatus bean){
		//更新打印机状态表中的最后作业号
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updatePrintDevicestatusBydevice4",bean);
		return bean.getSpcode();
	}
	
}