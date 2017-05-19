package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Lingjgyslsbz;
import com.athena.ckx.module.xuqjs.service.LingjgyslsbzService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 零件供应商临时包装
 * @author denggq
 * @date 2012-4-17
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjgyslsbzAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private LingjgyslsbzService lingjgyslsbzService;
	
	
	/**
	 * 获得LoginUser
	 * @author denggq
	 * @date 2012-4-17
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-17
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
	 * @date 2012-4-17
	 * @return String
	 */
	public String query(@Param Lingjgyslsbz bean){
		try{
			setResult("result", lingjgyslsbzService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商临时包装", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商临时包装", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-17
	 * @return String
	 */
	public String list(@Param Lingjgyslsbz bean) {
		try {
			setResult("result", lingjgyslsbzService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商临时包装", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商临时包装", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-17
	 * @return String
	 */
	public String get(@Param Lingjgyslsbz bean) {
		try {
			setResult("result", lingjgyslsbzService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商临时包装", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商临时包装", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * @description 保存
	 * @param bean
	 * @param Lingjgyslsbzs
	 * @return String
	 * @author denggq
	 * @date 2012-4-17
	 */
	public String save(@Param("list_insert") ArrayList<Lingjgyslsbz> insert,@Param("list_edit") ArrayList<Lingjgyslsbz> edit , @Param("list_delete") ArrayList<Lingjgyslsbz> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(lingjgyslsbzService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商临时包装", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商临时包装", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		
		return AJAX;
	}
}
