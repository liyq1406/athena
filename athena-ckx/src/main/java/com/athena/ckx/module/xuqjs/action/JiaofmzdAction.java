package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Jiaofmzd;
import com.athena.ckx.module.xuqjs.service.JiaofmzdService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 交付码字典
 * @author denggq
 * @date 2012-3-29
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class JiaofmzdAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private JiaofmzdService jiaofmzdService;
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-3-29
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-3-29
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
	 * @date 2012-3-29
	 * @return String
	 */
	public String query(@Param Jiaofmzd bean){
		try {
			setResult("result", jiaofmzdService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码字典", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-29
	 * @return String
	 */
	public String get(@Param Jiaofmzd bean){
		try {
			setResult("result", jiaofmzdService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码字典", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-29
	 * @return String
	 */
	public String list(@Param Jiaofmzd bean) {
		try {
			setResult("result", jiaofmzdService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码字典", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存方法
	 * @author denggq
	 * @Date 2012-3-29
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Jiaofmzd> insert,@Param("edit") ArrayList<Jiaofmzd> edit,@Param("delete") ArrayList<Jiaofmzd> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(jiaofmzdService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码字典", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	/**
	 * 物理删除
	 * @param bean
	 * @author denggq
	 * @date 2012-4-23
	 * @return String
	 */
	public String remove(@Param Jiaofmzd bean){
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(jiaofmzdService.remove(bean, getLoginUser().getUsername()), "i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码字典", "数据失效");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
}
