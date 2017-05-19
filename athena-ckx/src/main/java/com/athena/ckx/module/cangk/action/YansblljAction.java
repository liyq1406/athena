package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Yansbllj;
import com.athena.ckx.module.cangk.service.YansblljService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 零件验收比例设置
 * @author denggq
 * @date 2012-2-6
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YansblljAction extends ActionSupport{

	
	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	
	/**
	 * 注入YansblljService
	 * @author denggq
	 * @date 2012-2-6
	 * @return bean
	 */
	@Inject
	private YansblljService yansblljService;
	
	
	
	
	
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
	 * @date 2012-2-6
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
	 * @date 2012-2-6
	 * @return String
	 */
	public String query(@Param Yansbllj bean){
		try {
			setResult("result", yansblljService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	 * @date 2012-2-6
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Yansbllj> insert,@Param("edit") ArrayList<Yansbllj> edit , @Param("delete") ArrayList<Yansbllj> delete){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(yansblljService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 增加一个供应商的零件的验收项
	 * @author denggq
	 * @date 2012-8-2
	 * @return String
	 */
	public String addYansblljByGongys(@Param Yansbllj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(yansblljService.addYansblljByGongys(bean,getLoginUser().getUsername()), "i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "增加一个供应商的所有零件的验收项");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "增加一个供应商的所有零件的验收项", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-2-6
	 * @return String
	 */
	public String get(@Param Yansbllj bean){
		try {
			setResult("result", yansblljService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-2-6
	 * @return String
	 */
	public String list(@Param Yansbllj bean) {
		try {
			setResult("result", yansblljService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-2-6
	 * @return String
	 */
	public String remove(@Param Yansbllj bean){
		try {
			setResult("result", yansblljService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 删除多个
	 * @author denggq
	 * @date 2012-8-2
	 * @return String
	 */
	public String deletes( @Param("delete") ArrayList<Yansbllj> delete){
		Map<String,String> map = new HashMap<String,String>();
		try {
			yansblljService.deletes(delete);
			map.put("message", "删除成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件验收比例设置", "多选删除零件验收比例设置");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "多选删除零件验收比例设置", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 生效提交按钮，生成零件验收比例设置
	 * @author denggq
	 * @date 2012-5-8
	 * @return String
	 */
	public String commit(){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(yansblljService.commit(getLoginUser().getUsercenter(),getLoginUser().getUsername()), "i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
		}catch (Exception e) {
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据删除结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
}
