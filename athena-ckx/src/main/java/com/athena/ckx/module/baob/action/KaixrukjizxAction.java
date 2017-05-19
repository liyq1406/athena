package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Kaixrukjizx;
import com.athena.ckx.module.baob.service.KaixrukjizxService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;



/**
 * 2.15.4.3开箱入库集装箱报表
 * @author xryuan
 * @date 2013-3-26
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KaixrukjizxAction extends ActionSupport{
	@Inject
	private KaixrukjizxService kaixrukjizxService;
	/**
	 * 用户级操作日志
	 * @author xryuan
	 * @date 2013-3-26
	 * @return bean
	 */
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
		//grid表格不显示权限字段	
		setResult("USERCENTER", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author xryuan
	 * @date 2013-3-26
	 * @return String
	 */
	public String query(@Param Kaixrukjizx bean){
		try {
			setResult("result", kaixrukjizxService.query(bean,getParam("exportXls"),getParams()));
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
