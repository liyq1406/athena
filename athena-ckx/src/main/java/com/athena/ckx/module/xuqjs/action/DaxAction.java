package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Dax;
import com.athena.ckx.module.xuqjs.service.DaxService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 焊装大线
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DaxAction extends ActionSupport{

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DaxService hanzdxService;
	
	/**
	 * 登录人信息
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 获取大线信息（多记录查询）
	 * @param bean
	 * @return
	 */
	public String listHanzdx(@Param Dax bean){
		try {
			setResult("result", hanzdxService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "大线", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "大线", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	
	/**
	 * 获取大线信息（单记录查询）
	 * @return
	 */
	public String getDaxInfo(@Param Dax bean){
		try {
			setResult("result", hanzdxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "大线", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "大线", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 保存大线
	 * @return
	 */
	public String saveHanzdx(@Param Dax bean, @Param("operant") Integer operant){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(hanzdxService.save(bean, operant, getLoginUser().getUsername()), "i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "大线", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "大线", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

}
