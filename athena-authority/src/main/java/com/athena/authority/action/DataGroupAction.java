/**
 * 
 */
package com.athena.authority.action;

import java.util.Map;

import com.athena.authority.entity.DataGroup;
import com.athena.authority.entity.DataType;
import com.athena.authority.service.DataGroupService;
import com.athena.authority.service.DataTypeService;
import com.athena.component.service.Message;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DataGroupAction extends ActionSupport {
	@Inject
	private DataGroupService dataGroupService;
	@Inject
	private DataTypeService dataTypeService;
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param DataGroup bean) {
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param DataGroup bean) {
		/**
		 * select 查询数据权限组信息
		 */
		String groupName = bean.getGroupName();
		String temp = "";
		if(groupName!=null){
			temp = groupName.trim();
		}
		bean.setGroupName(temp);
		setResult("result", dataGroupService.select(bean));
		return AJAX;
	}
	
	/**
	 * 多记录查询
	 * @param bean
	 * @return
	 */
	public String list(@Param DataGroup bean) {
		/**
		 * 根据dataId 查询数据权限组信息
		 */
		setResult("result", dataGroupService.getListByDataId(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param DataGroup bean) {
		/**
		 * 根据ID，获取单记录信息
		 */
		setResult("result", dataGroupService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param DataGroup bean) {
		/**
		 * 保存 数据权限组信息
		 */
		dataGroupService.save(bean);
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param DataGroup bean) {
		/**
		 * 删除权限组信息
		 */
		dataGroupService.doDelete(bean);
		setResult("result", new Message("删除成功!"));
		return AJAX;
	}
/**
 * 获取数据权限
 * @param bean
 * @return
 */
	public String getData(@Param DataGroup groupBean) {
		//获取数据权限组ID
		String groupId = groupBean.getGroupId();
		//获取数据权限ID
		String dataId = groupBean.getDataId();
		//初始化数据权限类型
		DataType bean = new DataType();
		bean.setDataId(dataId);
		//获取数据类型数据
		DataType dataType = (DataType) dataTypeService.get(bean);
		//获取数据权限参数
		String dataParam = dataType.getDataParam();
		String usercenter = dataType.getUsercenter();
		//根据数据权限组ID获取数据权限信息
		Map map = dataGroupService.getData(dataParam,groupBean,groupId,usercenter);
		setResult("result",map);
		this.setRequestAttribute("groupId", groupId);
		return AJAX;
	}
	public String getSelectedData(@Param DataGroup groupBean){
		//获取数据权限组ID
		String groupId = groupBean.getGroupId();
		//获取数据权限ID
		String dataId = groupBean.getDataId();
		//初始化数据权限类型
		DataType bean = new DataType();
		bean.setDataId(dataId);
		//获取数据类型数据
		DataType dataType = (DataType) dataTypeService.get(bean);
		//获取数据权限参数
		String dataParam = dataType.getDataParam();
		//根据数据权限组ID获取数据权限信息
		Map map = dataGroupService.getSelectedData(dataParam,groupId);
		setResult("result",map);
		this.setRequestAttribute("groupId", groupId);
		return AJAX;
	}
}
