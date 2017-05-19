package com.athena.print.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.athena.component.runner.Runner;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.service.PrinteMonitorService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @description 打印监听控制器
 * @author wy
 * @date  2012-2-23
 *
 */
@Component
public class PrintMonitorController {

	private static Logger logger=Logger.getLogger(PrintMonitorController.class);
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	private Runner runner=null;
	
	public void monitorStart(){
		
		try{
			//查询打印机组表，获取有效的打印机组信息
			List<PrintDevicegroup> groups = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup");
			
			if( groups.size() > 0 ){	//打印机组数量
				
				for( int i =0; i<groups.size(); i++){
					PrintDevicegroup pg = groups.get(i);	//打印机组信息				
					new PrinteMonitorService(pg,baseDao).execute();	//子线程
				}
			}
		
		}catch( Exception e ){
			logger.error(e.getMessage());
		}finally{
			runner.shutdown();
		}

	}
	
	
}
