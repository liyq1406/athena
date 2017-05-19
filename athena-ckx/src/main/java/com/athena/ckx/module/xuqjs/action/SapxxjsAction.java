package com.athena.ckx.module.xuqjs.action;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Sapxxjs;
import com.athena.ckx.module.xuqjs.service.SapxxjsService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 用户中心|容器记账
 * @author xss
 * @Date 2015-2-3
 * 0010495
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class SapxxjsAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private SapxxjsService sapxxjsService;
	
	
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
	
	
	public String querySapxxjs(@Param Sapxxjs sapxxjs){
		try {
			setResult("result", sapxxjsService.querySapxxjs(sapxxjs) );
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP下线结算", "查询-成功");
		} catch (Exception e) {
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP下线结算", "查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result", message);
		}
		return AJAX;
	}

	public String selectSapxxjs(@Param Sapxxjs sapxxjs){
		try {
			setResult("result", sapxxjsService.selectSapxxjs(sapxxjs));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP下线结算", "数据查询-成功");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result", message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP下线结算", "数据查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String deleteSapxxjs(@Param Sapxxjs sapxxjs){
		try {
			setResult("result", sapxxjsService.deleteSapxxjs(sapxxjs));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP下线结算", "删除数据-成功");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result", message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP下线结算", "删除数据-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	public String saveSapxxjs(@Param Sapxxjs sapxxjs,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			sapxxjs.setCreator(getLoginUser().getUsername());
			sapxxjs.setCreate_time(DateTimeUtil.getAllCurrTime());
			sapxxjs.setEditor(getLoginUser().getUsername());
			sapxxjs.setEdit_time(DateTimeUtil.getAllCurrTime());			
			
				//操作符为1为插入操作
				if(1 == operant){
					sapxxjsService.saveSapxxjs(sapxxjs);
					message.put("message", "保存成功!");
				//更新操作
				}else{
					sapxxjsService.updateSapxxjs(sapxxjs);
					message.put("message", "更新成功!");
				}
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "SAP下线结算", "数据保存-成功");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "SAP下线结算", "数据保存-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
}
