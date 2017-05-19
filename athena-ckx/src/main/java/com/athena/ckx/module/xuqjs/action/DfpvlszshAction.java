package com.athena.ckx.module.xuqjs.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Dfpvlszsh;
import com.athena.ckx.module.xuqjs.service.DfpvlszshService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DfpvlszshAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DfpvlszshService dfpvlszshService;
	
	
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
	
	
	public String queryDfpvlszsh(@Param Dfpvlszsh dfpvlszsh){
		try {
			setResult("result", dfpvlszshService.queryDfpvlszsh(dfpvlszsh) );
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "DFPV流水帐审核", "查询-成功");
		} catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "DFPV流水帐审核", "查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
		
	public String queryCaozm(){
//		LoginUser currentUser = AuthorityUtils.getSecurityUser();
//		String usercenter = currentUser.getUsercenter();
//		Map<String,String> postMap = currentUser.getPostAndRoleMap();
//		String postId = postMap.get("CK_003");		
//		
//		Map<String,String> map = new HashMap<String,String>();
//		
//		map.put("usercenter", usercenter);
//		map.put("postId", postId);
		try {
			setResult("result", dfpvlszshService.queryCaozm());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "DFPV流水帐审核", "操作码查询-成功");
		} catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "DFPV流水帐审核", "操作码查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String Shenh(@Param("edit") ArrayList<Dfpvlszsh> ls)
	{
		String result = null;
		try
		{
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			for (Dfpvlszsh dfpvlszsh : ls) {
				HashMap<String,String> map1 = new HashMap<String,String>();
				map1.put("usercenter",dfpvlszsh.getUsercenter());
				map1.put("pingzh", dfpvlszsh.getPingzh());
				map1.put("pingzxmh",dfpvlszsh.getPingzxmh());
				map1.put("caozm",dfpvlszsh.getCaozm());
				map1.put("cangkzck",dfpvlszsh.getCangkzck());
				map1.put("blh",dfpvlszsh.getBlh());
				map1.put("editor", getLoginUser().getUsername());
				map1.put("creator", getLoginUser().getUsername());
				list.add(map1);
			} 
			
			dfpvlszshService.updateShenh(list);
			result = "审核成功!";
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "DFPV流水帐审核", "审核成功");
		}
		catch (Exception e) 
		{
			result = e.getMessage();
			userOperLog.addError(CommonUtil.MODULE_CKX, "DFPV流水帐审核", "审核异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		finally
		{
			setResult("result", result);
		}
		return AJAX;
	}
}
