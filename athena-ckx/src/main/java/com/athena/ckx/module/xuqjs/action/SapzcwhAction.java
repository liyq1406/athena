package com.athena.ckx.module.xuqjs.action;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Sapzcwh;
import com.athena.ckx.module.xuqjs.service.SapzcwhService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class SapzcwhAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private SapzcwhService sapzcwhService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	
	public String execute() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	public String querySapzcwh(@Param Sapzcwh sapzcwh){
		try {
			setResult("result", sapzcwhService.querySapzcwh(sapzcwh) );
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "数据查询-成功");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result", message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "数据查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	public String selectSapzcwh(@Param Sapzcwh sapzcwh){
		try {
			setResult("result", sapzcwhService.selectSapzcwh(sapzcwh));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "数据查询-成功");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result", message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "数据查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String deleteSapzcwh(@Param Sapzcwh sapzcwh){
		try {
			setResult("result", sapzcwhService.deleteSapzcwh(sapzcwh));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "删除数据-成功");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result", message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "删除数据-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	public String saveSapzcwh(@Param Sapzcwh sapzcwh,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			sapzcwh.setCreator(getLoginUser().getUsername());
			sapzcwh.setCreate_time(DateTimeUtil.getAllCurrTime());
			sapzcwh.setEditor(getLoginUser().getUsername());
			sapzcwh.setEdit_time(DateTimeUtil.getAllCurrTime());			
			
				//操作符为1为插入操作
				if(1 == operant){
					sapzcwhService.saveSapzcwh(sapzcwh);
					message.put("message", "保存成功!");
				//更新操作
				}else{
					sapzcwhService.updateSapzcwh(sapzcwh);
					message.put("message", "更新成功!");
				}
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "数据保存-成功");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("result", message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP整车供应商维护", "数据保存-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
}
