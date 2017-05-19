package com.athena.ckx.module.cangk.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.ChengpkJihyz;
import com.athena.ckx.module.cangk.service.ChengpkJihyzService;
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
 * 成品库计划员组
 * @author denggq
 * @date 2012-8-15
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChengpkJihyzAction extends ActionSupport{
	

	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入chengpkJihyzService
	 * @author denggq
	 * @date 2012-8-15
	 * @return bean
	 */
	@Inject
	private ChengpkJihyzService chengpkJihyzService;
	
	
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
	 * @date 2012-8-15
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-8-15
	 * @return String
	 */
	public String query(@Param ChengpkJihyz bean){
		
		try {
			setResult("result", chengpkJihyzService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "成品库计划员组", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "成品库计划员组", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-8-15
	 * @return String
	 */
	public String get(@Param ChengpkJihyz bean ){
		
		try {
			setResult("result", chengpkJihyzService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "成品库计划员组", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "成品库计划员组", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-8-15
	 * @return String
	 */
	public String list(@Param ChengpkJihyz bean) {
		
		try {
			setResult("result", chengpkJihyzService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "成品库计划员组", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "成品库计划员组", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-8-15
	 * @return String
	 */
	public String remove(@Param ChengpkJihyz bean){
		try {
			bean.setEditor(getLoginUser().getUsername());			//修改人
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
			setResult("result", chengpkJihyzService.doDelete(bean));//物理删除
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "成品库计划员组", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "成品库计划员组", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * @description 保存
	 * @param bean 
	 * @param String
	 * @author denggq
	 * @date 2012-8-15
	 * @return String
	 */
	public String save(@Param ChengpkJihyz bean,@Param("operant") Integer operant) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(chengpkJihyzService.save(bean,operant,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "成品库计划员组", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "成品库计划员组", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
	}
}
