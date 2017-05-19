package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Dfpvfhtzd;
import com.athena.ckx.module.baob.service.DfpvfhtzdService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

//<!--rencong 0011937 -->
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DfpvfhtzdAction extends ActionSupport{
	@Inject
	private DfpvfhtzdService dfpvfhtzdService;

	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取当前用户信息
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @return String
	 */
	
	public String execute(){
		setResult("USERCENTER", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @return String
	 */
	public String query(@Param Dfpvfhtzd bean){
		try {
			setResult("result", dfpvfhtzdService.query(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
}
