package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Gongysfe;
import com.athena.ckx.module.xuqjs.service.GongysfeService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * AB份额重置
 * @author hanwu
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class GongysfeAction extends ActionSupport{
	
	@Inject 
	private GongysfeService gongysfeService;
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	/**
	 * 跳转到AB份额重置页面
	 * @return
	 */
	public String execute() {
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 查询供应商份额
	 * @param bean
	 * @return
	 */
	public String queryGongysfe(@Param Gongysfe bean){
		try{
			setResult("result", gongysfeService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "AB份额重置", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "AB份额重置", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 供应商份额重置
	 * @param bean
	 * @return
	 */
	public String fenecz(@Param Gongysfe bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			String msg = gongysfeService.resetGongysfe(bean);//重置AB点供应商份额
			message.put("message", "供应商份额重置成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "AB份额重置", msg);
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "AB份额重置", "重置AB点供应商份额", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
