/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority.action;

import com.athena.authority.entity.CkDataType;
import com.athena.authority.entity.DataType;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.service.CkDataTypeService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.entity.Domain;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkDataTypeAction  extends ActionSupport {
	@Inject
	private CkDataTypeService ckDataTypeService;
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param CkDataType bean) {
		return  "select";
	}
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkDataType bean) {
		/**
		 * 获取所有仓库数据权限类型信息
		 */
		setResult("result", ckDataTypeService.select(bean));
		return AJAX;
	}
	
	/**
	 * 多记录查询
	 * @param bean
	 * @return
	 */
	public String list(@Param CkDataType bean) {
		/**
		 * 获取所有数据类型信息(不分页)
		 */
		setResult("result", ckDataTypeService.listDicCode(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param CkDataType bean) {
		/**
		 *获取单条数据权限类型信息
		 */
		setResult("result", ckDataTypeService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param("operant") String operant,@Param CkDataType bean) {
		/**
		 * 保存数据类型信息
		 */
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		bean.setBiaos("1");
		if(operant.equals("1")){
			try{
				bean.setCreator(loginUser.getUsername());
				bean.setCreateTime(DateUtil.curDateTime());
				ckDataTypeService.save(bean);
				userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理","新增数据"+bean.getDataParamName()+"操作成功");
			}catch(Exception e){
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理","新增数据"+bean.getDataParamName()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
			}
		}else if(operant.equals("2")){
			try{
				bean.setMender(loginUser.getUsername());
				bean.setModifyTime(DateUtil.curDateTime());
				ckDataTypeService.save(bean);
				userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理","修改数据"+bean.getDataParamName()+"操作成功");
			}catch(Exception e){
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理","修改数据"+bean.getDataParamName()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
			}
		}
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
		try{
			ckDataTypeService.doDelete(bean);
			setResult("result", new Message("删除成功!"));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理","删除数据操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理","删除数据操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String getCkDataInfo(@Param("id") String id,@Param("postCode") String postCode){
		CkDataType bean = new CkDataType();
		bean.setId(id);
		try{
		//gswang 2012-09-27 mantis 0004307
			setResult("result", ckDataTypeService.getCkDataInfo(bean,postCode));
		}catch(Exception e){
			setErrorMessage("请确认仓库数据权限中所配置参数是否正确!");
		}
		
		return AJAX;
	}

}
