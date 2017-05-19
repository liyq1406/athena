package com.athena.truck.module.kcckx.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Shijdzt;
import com.athena.truck.module.kcckx.service.ShijdztService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 实际大站台
 * @author chenpeng
 * @date 2015-1-7
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShijdztAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private ShijdztService shijdztService;
	
	
	/**
	 * 登录人信息
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String query(@Param Shijdzt bean){
		try{
			setResult("result", shijdztService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实际大站台", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String get(@Param Shijdzt bean){
		try {
			setResult("result", shijdztService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实际大站台", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String list(@Param Shijdzt bean) {
		try {
			setResult("result", shijdztService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实际大站台", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String listShijdztqc(@Param Shijdzt bean) {
		try {
			setResult("result", shijdztService.listShijdztqc(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实际大站台", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 失效实际大站台
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String remove(@Param Shijdzt bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			setResult("result", shijdztService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实际大站台", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 获得大站台编号
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String listDaztbh(@Param Shijdzt bean) {
		try {
			setResult("result", shijdztService.listDazt(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实际大站台", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}
