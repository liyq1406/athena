package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Jiaofmsd;
import com.athena.ckx.module.xuqjs.service.JiaofmsdService;
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
 * 交付码设定
 * @author denggq
 * @date 2012-4-6
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class JiaofmsdAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private JiaofmsdService jiaofmsdService;
	
	
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
	 * @date 2012-4-6
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		
		StringBuffer sb = new StringBuffer(); 		
		sb.append("{"); 		
		Integer year = Integer.parseInt(DateTimeUtil.getDateTimeStr("yyyy")); 	
		setResult("currentYear", year);
		for (int i = 0; i < 4; i++) { 	
			sb.append("'"+year+"':'"+year+"',"); 
			year++; 			
		}
		sb.delete(sb.length()-1,sb.length());
		sb.append("}"); 		
		setResult("year", sb.toString());

		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-6
	 * @return String
	 */
	public String query(@Param Jiaofmsd bean){
		try{
			setResult("result", jiaofmsdService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码设定", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-4-6
	 * @param bean
	 * @return String
	 */
	public String list(@Param Jiaofmsd bean) {
		try{
			setResult("result", jiaofmsdService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码设定", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 单记录查询方法
	 * @author denggq
	 * @Date 2012-4-6
	 * @param bean
	 * @return String
	 */
	public String get(@Param Jiaofmsd bean) {
		try{
			setResult("result", jiaofmsdService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码设定", "多数据查询");
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
	 * @Date 2012-4-6
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Jiaofmsd> insert,@Param("edit") ArrayList<Jiaofmsd> edit,@Param("delete") ArrayList<Jiaofmsd> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(jiaofmsdService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码设定", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	/**
	 * 删除方法
	 * @author denggq
	 * @Date 2012-4-6
	 * @param bean
	 * @return String
	 */
	public String remove(@Param Jiaofmsd bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(jiaofmsdService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付码设定", "数据删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付码设定", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
