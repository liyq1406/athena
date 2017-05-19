package com.athena.ckx.module.xuqjs.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.module.xuqjs.service.UsercenterService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 用户中心
 * @author denggq
 * @Date 2012-3-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class UsercenterAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private UsercenterService usercenterService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author denggq
	 * @Date 2012-3-19
	 * @return String
	 */
	
	
	public String execute() {
		return "select";
	}
	
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String query(@Param com.athena.ckx.entity.xuqjs.Usercenter bean) {
		try{
			setResult("result", usercenterService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String list(@Param Usercenter bean) {
		try{
			setResult("result", usercenterService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 单记录查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String get(@Param Usercenter bean) {
		try{
			setResult("result", usercenterService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 行编辑保存方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Usercenter> insert,@Param("edit") ArrayList<Usercenter> edit,@Param("delete") ArrayList<Usercenter> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(usercenterService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	/**
	 * 失效方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String remove(@Param Usercenter bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(usercenterService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
