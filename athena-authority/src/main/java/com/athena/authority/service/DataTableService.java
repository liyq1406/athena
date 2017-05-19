/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.List;

import com.athena.authority.entity.DataTable;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;

@Component
public class DataTableService extends BaseService<DataTable>{
	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return "authority";
	}
	
	/**
	 * 根据业务表编码查询
	 */
	@SuppressWarnings("rawtypes")
	public DataTable getDataTableByCode(String tableCode){
		/**
		 * 初始化DataTable
		 */
		DataTable query = new DataTable();
		/**
		 * 设置业务表编码
		 */
		query.setTableCode(tableCode);
		/**
		 * 获取该业务表信息
		 */
		List results = list(query);
		//判断,如果业务数据results为零,则返回空,不然则返回该数据信息
		return results.size()==0?null:(DataTable)results.get(0);
		
	}

	public String getDataTableByGroupId(DataTable bean) {
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(getNamespace()+".countDataTableByGroupId", bean);
		String msg = "";
		if(count>0){
			msg = "1";
		}else if(count==0){
			msg = "0";
		}
		return msg; 
	}
	
}