/**
 * 
 */
package com.athena.authority.action;

import java.util.ArrayList;

import com.athena.authority.entity.DataGroupItem;
import com.athena.authority.service.DataGroupItemService;
import com.athena.component.service.Message;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DataGroupItemAction extends ActionSupport {
	@Inject
	private DataGroupItemService dataGroupItemService;

	/**
	 * 1：主页面
	 * 2：新增，更新，删除交易
	 * @param bean
	 * @param operant
	 * @return
	 */
	public String execute(@Param DataGroupItem bean, @Param("operant") Integer operant) {
		return "select";
	}

	/**
	 * 数据查询
	 * @param bean
	 * @return
	 */
	public String query(@Param DataGroupItem bean) {
		/**
		 * 查询数据权限项信息
		 */
		setResult("result", dataGroupItemService.select(bean,getParams()));
		return AJAX;
	}
	/**
	 * 根据groupId，数据权限组ID获取数据权限组项信息
	 * @param groupId
	 * @return
	 */
	public String list(@Param("groupId") String groupId){
		/**
		 * 初始化权限组项
		 */
		DataGroupItem bean = new DataGroupItem();
		/**
		 * 设置groupId
		 */
		bean.setGroupId(groupId);
		/**
		 * 获取数据权限组项
		 */
		setResult("result", dataGroupItemService.listDataGroupItemByGroupId(bean));
		return AJAX;
	}
	/**
	 * 数据权限组
	 * @param dataGroupItems
	 * @param groupId
	 * @return
	 */
	public String save(@Param("dataGroupItems")  ArrayList<DataGroupItem> dataGroupItems,@Param("groupId") String groupId){
		/**
		 * 保存数据权限组
		 */
		dataGroupItemService.save(dataGroupItems,groupId);
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
}
