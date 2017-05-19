package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Shengcpt;
import com.athena.ckx.module.xuqjs.service.ShengcptService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 生产平台
 * @author denggq
 * @date 2012-3-28
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShengcptAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private ShengcptService shengcptService;
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-6
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-3-28
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
	 * @date 2012-3-28
	 * @return String
	 */
	public String query(@Param Shengcpt bean){
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产平台", "数据查询");
			setResult("result", shengcptService.select(bean));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产平台", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-28
	 * @return String
	 */
	public String get(@Param Shengcpt bean){
		try {
			setResult("result", shengcptService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产平台", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产平台", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-28
	 * @return String
	 */
	public String list(@Param Shengcpt bean) {
		try {
			setResult("result", shengcptService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产平台", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产平台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * @description 保存
	 * @param bean
	 * @param Shengcpts
	 * @author denggq
	 * @date 2012-3-28
	 */
	public String save(@Param("list_insert") ArrayList<Shengcpt> insert,@Param("list_edit") ArrayList<Shengcpt> edit , @Param("list_delete") ArrayList<Shengcpt> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(shengcptService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产平台", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产平台", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		
		return AJAX;
	}
}
