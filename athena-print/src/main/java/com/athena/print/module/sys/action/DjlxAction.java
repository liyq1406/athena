package com.athena.print.module.sys.action;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.print.entity.sys.Danjlx;
import com.athena.print.module.sys.service.DjlxService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 单据管理
 * @author wy
 * @Date 2012-3-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DjlxAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DjlxService djlxService;
	
	
	/**
	  * 获取用户信息
	  * @author wy
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author wy
	 * @Date 2012-3-19
	 * @return String
	 */
	
	
	public String execute() {
		//获取到用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询方法
	 * @author wy
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String query(@Param Danjlx bean) {
		try{
			setResult("result", djlxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "单据管理", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "单据管理", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 多记录查询方法
	 * @author wy
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String list(@Param Danjlx bean) {
		try{
			setResult("result", djlxService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "单据管理", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "单据管理", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 单记录查询方法
	 * @author wy
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String get(@Param Danjlx bean) {
		try{
			setResult("result", djlxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "单据管理", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "单据管理", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 行编辑保存方法
	 * @author wy
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") List<Danjlx> insert,@Param("edit") List<Danjlx> edit,@Param("delete") List<Danjlx> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(djlxService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "单据管理", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "单据管理", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String remove(@Param Danjlx bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(djlxService.doDelete(bean),"i18n.print.i18n_print");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据管理", "数据 删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据管理", "数据 删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
