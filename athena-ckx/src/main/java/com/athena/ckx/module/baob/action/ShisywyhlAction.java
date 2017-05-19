package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Shisywyhl;
import com.athena.ckx.module.baob.service.ShisywyhlService;


import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;



/**
 * 2.15.5.7 实时延误要货令
 * @author hj
 * @date 2013-3-13
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShisywyhlAction extends ActionSupport{
	@Inject
	private ShisywyhlService shisywyhlService;
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
	
	public String execute(){
		setResult("USERCENTER", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author rencong
	 * @date 2016-4-5
	 * @return String
	 */
	public String query(@Param Shisywyhl bean){
		try {
			Map<String,String> params = this.getParams();
			bean.setYaohllx(strToSql(params.get("yaohllx"),"yaohllx"));
			//12898
			if(null != params.get("yaohllx"))
				bean.setYaohlzt(strToSql(params.get("yaohlzt"),"yaohlzt"));
			setResult("result", shisywyhlService.query(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	/**
	 * 查询要货令表所有要货令类型
	 * @author rencong
	 * @date 2016-1-19
	 * @return AJAX
	 */
	public String queryYhllx(){
//		LoginUser currentUser = AuthorityUtils.getSecurityUser();
//		String usercenter = currentUser.getUsercenter();
//		Map<String,String> postMap = currentUser.getPostAndRoleMap();
//		String postId = postMap.get("CK_003");		
//		
//		Map<String,String> map = new HashMap<String,String>();
//		
//		map.put("usercenter", usercenter);
//		map.put("postId", postId);
		
		setResult("result", shisywyhlService.queryYhllx());
		return AJAX;
	}
	
	public String strToSql(String inStr,String reg)
	{
		String str = new String();
		String str1 = new String();
		String[] s = inStr.split(",");
		int i;
		for (i = 0;i<s.length;i++)
		{
			str = str + "'"+s[i]+"',";
		}
		str = str.substring(0, str.length()-1);
		str1 = " and a."+reg+ " in ( "+str+" )";
		
		return str1;
	}
}
