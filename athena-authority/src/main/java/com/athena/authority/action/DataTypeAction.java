/**
 * 
 */
package com.athena.authority.action;

import com.athena.authority.entity.DataType;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.service.DataTypeService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.util.cache.CacheManager;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DataTypeAction extends ActionSupport {
	@Inject
	private DataTypeService dataTypeService;

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param DataType bean) {
		return  "select";
	}
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param DataType bean) {
		/**
		 * 获取所有数据权限类型信息
		 */
		setResult("result", dataTypeService.select(bean));
		return AJAX;
	}
	
	/**
	 * 多记录查询
	 * @param bean
	 * @return
	 */
	public String list(@Param DataType bean) {
		/**
		 * 获取所有数据类型信息(不分页)
		 */
		setResult("result", dataTypeService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param DataType bean) {
		/**
		 *获取单条数据权限类型信息
		 */
		setResult("result", dataTypeService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param("operant") String operant,@Param DataType bean) {
		/**
		 * 保存数据类型信息
		 */
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		bean.setBiaos("1");
		if(operant.equals("1")){
			bean.setCreator(loginUser.getUsername());
			bean.setCreateTime(DateUtil.curDateTime());
			dataTypeService.save(bean);
		}else if(operant.equals("2")){
			bean.setMender(loginUser.getUsername());
			bean.setModifyTime(DateUtil.curDateTime());
			dataTypeService.save(bean);
		}
		Object object = dataTypeService.refreshAllData();
		CacheManager cacheManager = CacheManager.getInstance();
		cacheManager.refreshCache("getAllData",object);
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param DataType bean) {
		/**
		 * 删除数据权限类型信息
		 */
		dataTypeService.doDelete(bean);
		setResult("result", new Message("删除成功!"));
		return AJAX;
	}
	public String validateOnlyDataType(@Param("value") String value,@Param("tjName") String tjName){
		DataType bean = new DataType();
		if(tjName.equals("dataCode")){
			bean.setDataCode(value);
		}
		if(tjName.equals("dataName")){
			bean.setDataName(value);
		}
		setResult("result", dataTypeService.validateOnlyDataType(bean));
		return AJAX;
	}
	public String validatedataParam(@Param("dataParam") String dataParam){
		String msg = "1";
		try{
			dataTypeService.validatedataParam(dataParam);	
		}catch(Exception e){
			msg = "2";
		}
		setResult("result", msg);
		return AJAX;
	}
}
