package com.athena.print.service;

import java.util.List;
import java.util.Map;

import com.athena.db.ConstantDbCode;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtasklist;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.pcenter.PrintQtasksheet;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.BackgroundRunLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

@Component
public class PreparQtaskService {

	@Inject
	private AbstractIBatisDao baseDao;
	@Inject
	private BackgroundRunLog backgroundRunLog;

	
	/**
	 * 公用方法
	 */
	
	/**
	 * 获取准备任务列表信息
	 */
	public List<PrintQtaskmain> getPrepareTaskList(Map map) throws RuntimeException{		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskmainstatus",map);
	}
	
	/**
	 * 获取打印机组(分组好的打印机组)
	 */
	public List<PrintQtaskmain> getPrintPgid() throws RuntimeException{		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskmainbypgid");
	}
	
	
	/**
	 * 根据主列表获取   子列表信息
	 */
	
	public List<PrintQtaskinfo> getTaskinfoList(String qid) throws RuntimeException{	
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfobyqid",qid);
	}
	
	
	/**
	 * 获取只有一个区域时的打印参数
	 */
	public PrintQtasksheet getPrintsheet(PrintQtaskinfo printQtaskinfo) throws RuntimeException{		
		return (PrintQtasksheet)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pcenter.queryPrintSheet",printQtaskinfo);
	}
	
	public PrintQtasksheet getSheet(PrintQtasksheet printQtasksheet) throws RuntimeException{		
		return (PrintQtasksheet)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pcenter.querySheet",printQtasksheet);
	}
	
	public List<PrintQtasklist> getGrid(PrintQtasksheet printQtasksheet) throws RuntimeException{		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("pcenter.queryGrid",printQtasksheet);
	}
	
}
