/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.Map;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.DataGroupItem;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;


@Component
public class DataGroupItemService extends BaseService{

	/**
	 * 获取模块空间
	 */
	protected String getNamespace(){
		return AuthorityConstants.MODULE_NAMESPACE;
	}
	/**
	 * 插入
	 */
	public String doInsert(DataGroupItem bean) {
		//设置id值为uuid
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertDataGroupItem", bean);
		return bean.getId();
	}
	/**
	 * 删除
	 */
	public String doDelete(DataGroupItem bean) {
		//执行删除,id为com.athena.authority.entity.DataGroupItem中deleteDataGroupItem
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".deleteDataGroupItem", bean);
		return bean.getId();
	}
	/**
	 * 更新
	 */
	public String doUpdate(DataGroupItem bean) {
		//执行更新,id为com.athena.authority.entity.DataGroupItem中updateDataGroupItem
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateDataGroupItem", bean);
		return bean.getId();
	}
	
	/**
	 * 查询
	 * @param page
	 * @param params
	 * @return
	 */
	public Map<String, Object> select(Pageable page,Map<String,String> params) {
		//执行分页查询,id为com.athena.authority.entity.DataGroupItem中queryDataGroupItem
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(getNamespace()+".queryDataGroupItem",params, page);
	}
	/**
	 * 保存数据权限组项
	 * @param dataGroupItems
	 * @param groupId
	 * @return
	 */
	public Message save(ArrayList<DataGroupItem> dataGroupItems, String groupId) {
		//初始化DataGroupItem
		DataGroupItem bean = new DataGroupItem();
		//设置groupId
		bean.setGroupId(groupId);
		//先删除数据权限组中的原有数据权限项
		this.doDelete(bean);
		for(DataGroupItem dataGroupItem:dataGroupItems){
			dataGroupItem.setGroupId(groupId);
			//插入
			this.doInsert(dataGroupItem);
		}
		return new Message("authorized.dataitem_success","i18n.authority.data_group_item");
	}
	
	
	/**
	 * 根据groupId查询数据权限组项
	 * @param bean
	 * @return
	 */
	public Object listDataGroupItemByGroupId(DataGroupItem bean) {
		/**
		 * 执行查询
		 */
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".listDataGroupItem",bean);
	}
}