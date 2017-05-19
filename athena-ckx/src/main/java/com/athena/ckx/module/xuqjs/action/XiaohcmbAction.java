package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xiaohcmb;
import com.athena.ckx.module.xuqjs.service.XiaohcmbService;
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
 * 小火车运输时刻模板
 * @author denggq
 * @date 2012-4-11
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XiaohcmbAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private XiaohcmbService xiaohcmbService;
	
	
	/**
	 * 获得登录人信息
	 * @author denggq
	 * @date 2012-4-11
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-11
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
	 * @date 2012-4-11
	 * @return String
	 */
	public String query(@Param Xiaohcmb bean){
		try {
			setResult("result", xiaohcmbService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-11
	 * @return String
	 */
	public String get(@Param Xiaohcmb bean ){
		try {
			setResult("result", xiaohcmbService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-11
	 * @return String
	 */
	public String list(@Param Xiaohcmb bean) {
		try {
			setResult("result", xiaohcmbService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-4-11
	 * @return String
	 */
	public String remove(@Param Xiaohcmb bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			bean.setEditor(getLoginUser().getUsername());			//修改人
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
			Message m = new Message(xiaohcmbService.doDelete(bean),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据删除");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);	
		return AJAX;
	}
	
	/**
	 * @description 弹出框保存
	 * @author denggq
	 * @date 2012-4-11
	 * @return String
	 */
	public String save(@Param Xiaohcmb bean , @Param("operant") Integer operant , @Param("list_insert") ArrayList<Xiaohcmb> insert) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xiaohcmbService.save(bean,operant,insert,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * @description 行编辑保存
	 * @author denggq
	 * @date 2012-4-11
	 * @return String
	 */
	public String saves(@Param("insert") ArrayList<Xiaohcmb> insert,@Param("edit") ArrayList<Xiaohcmb> edit , @Param("delete") ArrayList<Xiaohcmb> delete) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xiaohcmbService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻模板", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
	}
	
}
