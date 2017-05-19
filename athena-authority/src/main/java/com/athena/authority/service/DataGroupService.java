/**
 * 代码声明
 */
package com.athena.authority.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.DataGroup;
import com.athena.authority.util.PropertyLoader;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

@Component
public class DataGroupService extends BaseService<DataGroup>{
	
	/**
	 * 获取命名空间
	 */
	protected String getNamespace() {
		return "authority";
	}
	/**
	 * 获取未分配数据模板的数据权限项
	 * @param dataParam
	 * @param page
	 * @param groupId
	 * @return
	 */
	public Map getData(String dataParam,Pageable page,String groupId,String usercenter) {
		//初始化Map items
		Map<String, Object> items = new HashMap<String, Object>();
		PropertyLoader pl = new PropertyLoader();
		String newParam = pl.getPropertyInfo(dataParam);
		String sysTableName =  pl.getPropertyInfo("[dbSchemal0]SYS_DATA_GROUP_ITEM");
		//拼SQL,查出未被分配数据权限的数据
		String newDataParam = " select * from ("+newParam+") b where b.value NOT IN (select value from "+sysTableName+"  where group_id='"+groupId+"') and b.USERCENTER = '"+usercenter+"'order by b.value ";
		//分页查询,newDataParam为动态查询
		items =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(getNamespace()+".getDataParam", newDataParam,page);
		
		return items;
	}
	/**
	 * 根据dataId，获取数据权限组
	 * @param bean
	 * @return
	 */
	public Object getListByDataId(DataGroup bean) {
		//执行查询,数据权限组
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".listDataGroup", bean);
	}
	public Map getSelectedData(String dataParam,String groupId) {
		//初始化Map items
		List items = new ArrayList();
		PropertyLoader pl = new PropertyLoader();
		String newParam = pl.getPropertyInfo(dataParam);
		String sysTableName =  pl.getPropertyInfo("[dbSchemal0]SYS_DATA_GROUP_ITEM");
		//拼SQL,查出未被分配数据权限的数据
		String newDataParam = " select * from ("+newParam+") b where b.value  IN (select value from "+sysTableName+" where group_id='"+groupId+"') order by b.value ";
		//分页查询,newDataParam为动态查询
		items =  (List)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".getDataParam", newDataParam);
		Map map = new HashMap();
		map.put("total", items.size());
		map.put("rows", items);
		return map;
	}

}