/**
 * 代码声明
 */
package com.athena.print.module.pcenter.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.print.entity.pcenter.PrintQtaskinfo;


@Component
public class PrintQtaskinfoService extends BaseService<PrintQtaskinfo>{
	 @Override
	 //返回的SqlMap 的 命名空间
		protected String getNamespace() {
			return "pcenter";
		}
	 
	 public Map<String, Object> selectModulenumber(PrintQtaskinfo bean) {
		 // 插入一条数据
		 return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("pcenter.queryPrintQtask", bean,bean);
	 }
	 
	 
	 	/**
		 * 插入一条数据
		 */
	 @Transactional
	 public String doInsert(PrintQtaskinfo bean) {
		 // 插入一条数据
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.insertPrintQtaskinfo", bean);
		 return "success";
	 }
	 
	 	/**
		 * 准备中、新作业 删除
		 */
		public String doDelete(List<PrintQtaskinfo> delete) {
			//先判断是否为空
			if(null!=delete){
				//循环删除的作业
				for (PrintQtaskinfo printQtaskinfo : delete) {
					//执行删除的操作
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.deletePrintQtaskinfo", printQtaskinfo);
				}
			}
			return "Data deleted success";
		}
		
}