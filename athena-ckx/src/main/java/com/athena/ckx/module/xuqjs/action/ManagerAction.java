package com.athena.ckx.module.xuqjs.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Manager;
import com.athena.ckx.module.xuqjs.service.ManagerService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 计划员分组
 * @author denggq
 * @Date 2012-3-20
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ManagerAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private ManagerService managerService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-20
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author denggq
	 * @Date 2012-3-20
	 * @return String
	 */
	public String execute() {
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @Date 2012-3-20
	 * @param bean
	 * @return String
	 */
	public String query(@Param com.athena.ckx.entity.xuqjs.Manager bean) {
		try{
			setResult("result", managerService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计划员分组", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计划员分组", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-3-20
	 * @param bean
	 * @return String
	 */
	public String list(@Param Manager bean) {
		try{
			setResult("result", managerService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计划员分组", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计划员分组", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 单记录查询方法
	 * @author denggq
	 * @Date 2012-3-20
	 * @param bean
	 * @return String
	 */
	public String get(@Param Manager bean) {
		try{
			setResult("result", managerService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计划员分组", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计划员分组", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 行编辑保存方法
	 * @author denggq
	 * @Date 2012-3-20
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Manager> insert,@Param("edit") ArrayList<Manager> edit,@Param("delete") ArrayList<Manager> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(managerService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计划员分组", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计划员分组", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	/**
	 * 失效方法
	 * @author denggq
	 * @Date 2012-3-20
	 * @param bean
	 * @return String
	 */
	public String remove(@Param Manager bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(managerService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计划员分组", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计划员分组", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
