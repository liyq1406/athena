package com.athena.ckx.module.cangk.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Rongqcxhd;
import com.athena.ckx.module.cangk.service.RongqcxhdService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class RongqcxhdAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入RongqcxhdService
	 * @author wangyu
	 * @date 2012-12-10
	 */
	@Inject 
	private RongqcxhdService rongqcxhdService;
	
	
	/**
	 * 登录人信息
	 * @author wangyu
	 * @date 2012-12-10
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 * @author wangyu
	 * @date 20121210
	 */
	public String execute(@Param Rongqcxhd bean) {
		return  "select";
	}

	
	/**
	 * 分页查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 * @return String
	 */
	public String query(@Param Rongqcxhd bean) {
		try{
			setResult("result", rongqcxhdService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场消耗点", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场消耗点", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 未配置容器场分页查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 * @return String
	 */
	public String querywpz(@Param Rongqcxhd bean) {
		try{
			setResult("result", rongqcxhdService.selectwpz(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场消耗点", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场消耗点", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 */
	public String get(@Param Rongqcxhd bean) {
		try{
			setResult("result", rongqcxhdService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场消耗点", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场消耗点", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 */
	public String list(@Param Rongqcxhd bean) {
		try{
			setResult("result", rongqcxhdService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场消耗点", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场消耗点", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * 数据修改（多条数据操作）
	 * @param bean
	 * @author wangyu
	 * @date 2012-12-10
	 */
	public String save(@Param("edit") ArrayList<Rongqcxhd> edit) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(rongqcxhdService.save(edit,getLoginUser()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场消耗点", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场消耗点", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 增加
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String addRongqcxhd(@Param Rongqcxhd bean ){
			Map<String,String> message = new HashMap<String,String>();
			try {
				Message m=new Message(rongqcxhdService.inserts(bean,getLoginUser()),"i18n.print.i18n_print");
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场消耗点", "数据保存");
			} catch (Exception e) {
				setResult("success", false);
				message.put("message", e.getMessage());
				userOperLog.addError(CommonUtil.MODULE_CKX, "容器场消耗点", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
			setResult("result",message);
			return AJAX;
	}
	
}
