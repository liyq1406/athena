package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Lingjjhygys;
import com.athena.ckx.module.baob.service.LingjjhygysService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;



/**
 * 零件－计划员－供应商   2.15.9.13
 * @author hj
 * @date 2013-3-13
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjjhygysAction extends ActionSupport{
	@Inject
	private LingjjhygysService lingjjhygysService;
	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
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
	@SuppressWarnings("rawtypes")
	public String execute(){
		//grid表格不显示权限字段	
		setResult("USERCENTER", getLoginUser().getUsercenter());	//登录人所在的用户中心
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String value = (String) map.get(key);
		setResult(key, value);
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String query(@Param Lingjjhygys bean){
		try {
			setResult("result", lingjjhygysService.query(bean,getParam("exportXls")));
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
