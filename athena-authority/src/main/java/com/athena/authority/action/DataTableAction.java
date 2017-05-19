/**
 * 
 */
package com.athena.authority.action;

import com.athena.authority.entity.DataTable;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.service.DataTableService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DataTableAction extends ActionSupport {
	@Inject
	private DataTableService dataTableService;

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param DataTable bean) {
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param DataTable bean) {
		/**
		 * 获取所有DataTable信息
		 */
		setResult("result", dataTableService.select(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param DataTable bean) {
		/**
		 * 获取单条业务数据表信息并设置值
		 */
		setResult("result", dataTableService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param("operant") String operant,@Param DataTable bean) {
		/**
		 * 保存业务数据表信息
		 */
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		bean.setBiaos("1");
		if(operant.equals("1")){
			bean.setCreator(loginUser.getUsername());
			bean.setCreateTime(DateUtil.curDateTime());
			dataTableService.save(bean);
		}else if(operant.equals("2")){
			bean.setMender(loginUser.getUsername());
			bean.setModifyTime(DateUtil.curDateTime());
			dataTableService.save(bean);
		}
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param DataTable bean) {
		/**
		 * 删除业务数据表信息
		 */
		setResult("result", dataTableService.doDelete(bean));
		return AJAX;
	}
	public String getDataTableByGroupid(@Param("postGroupId") String postGroupId,@Param("usercenter") String usercenter){
		DataTable bean = new DataTable();
		bean.setPostGroupId(postGroupId);
		bean.setUsercenter(usercenter);
		setResult("result", dataTableService.getDataTableByGroupId(bean));
		return AJAX;
	}
}
