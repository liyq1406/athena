package com.athena.ckx.module.cangk.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Rongqc;
import com.athena.ckx.module.cangk.service.RongqcService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 空容器场
 * @author denggq
 * @date 2012-2-2
 * @modify 2012-2-18
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class RongqcAction extends ActionSupport{

	
	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	

	
	/**
	 * 注入RongqcService
	 * @author denggq
	 * @date 2012-2-2
	 * @return bean
	 */
	@Inject
	private RongqcService rongqcService;
	
	/**
	 * 获取当前用户信息
	 * @author denggq
	 * @date 2012-2-6
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String query(@Param Rongqc bean){
		try {
			setResult("result", rongqcService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 多行保存
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String save(@Param Rongqc bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(rongqcService.edits(bean,getLoginUser().getUsername()) ,"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 增加按钮
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String addRqc(@Param Rongqc bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(rongqcService.inserts(bean,getLoginUser().getUsername()) ,"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String get(@Param Rongqc bean){
		try {
			setResult("result", rongqcService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String list(@Param Rongqc bean) {
		try {
			setResult("result", rongqcService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 逻辑删除
	 * @param bean
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String remove(@Param Rongqc bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			setResult("result", rongqcService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "数据失效");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 物理删除
	 * @param bean
	 * @author denggq
	 * @date 2012-2-2
	 * @return String
	 */
	public String removes(@Param Rongqc bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
//			setResult("result", rongqcService.deletes(bean));
			map.put("message", rongqcService.deletes(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器场", "数据删除");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器场", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map);
		return AJAX;
	}
}
