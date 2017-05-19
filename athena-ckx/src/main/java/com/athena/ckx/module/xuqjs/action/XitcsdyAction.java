package com.athena.ckx.module.xuqjs.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.module.xuqjs.service.XitcsdyService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 系统参数定义
 * @author denggq
 * @Date 2012-3-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XitcsdyAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private XitcsdyService xitcsdyService;
	
	
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
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 获得字段类型名称
	 * @author denggq
	 * @Date 2012-7-13
	 * @return String
	 */
	public String getZidlxmc() {
		setResult("result", xitcsdyService.getSelectTeamCode(new Xitcsdy()));
		return AJAX;
	}
	
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String query(@Param Xitcsdy bean) {
		try{
			setResult("result", xitcsdyService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "系统参数定义", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "系统参数定义", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String list(@Param Xitcsdy bean) {
		try{
			setResult("result", xitcsdyService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "系统参数定义", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "系统参数定义", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String get(@Param Xitcsdy bean) {
		try{
			setResult("result", xitcsdyService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "系统参数定义", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "系统参数定义", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String save(@Param Xitcsdy bean,@Param("operant") Integer operant, @Param("list_insert") ArrayList<Xitcsdy> insert,@Param("list_edit") ArrayList<Xitcsdy> edit,@Param("list_delete") ArrayList<Xitcsdy> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xitcsdyService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "系统参数定义", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "系统参数定义", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	/**
	 * 删除方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String remove(@Param Xitcsdy bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(xitcsdyService.doDelete(bean), "i18n.ckx.cangk.i18n_fahzt");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "系统参数定义", "数据删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "系统参数定义", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 复制方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String copy(@Param("yusercenter") String yusercenter,@Param("yzidlx") String yzidlx,@Param("xusercenter") String xusercenter) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(xitcsdyService.copy(yusercenter,yzidlx,xusercenter,getLoginUser().getUsername()), "i18n.ckx.cangk.i18n_fahzt");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "系统参数定义", "数据复制");
		
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "系统参数定义", "数据复制", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
