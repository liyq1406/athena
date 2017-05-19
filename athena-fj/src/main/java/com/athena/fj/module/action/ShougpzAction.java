package com.athena.fj.module.action;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils; 
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component; 
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.interceptor.supports.log.Log;
/**
 * <p>
 * Title:手工配载逻辑类
 * </p>
 * <p>
 * Description:定义手工配载逻辑类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-2-20
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShougpzAction extends ActionSupport {

	/**
	 * 获取用户信息
	 * @return 用户信息
	 */
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	
	/**
	 * 手工配载跳转页面
	 * @author 贺志国
	 * @date 2012-1-4
	 * @return String 返回跳转
	 */
	@Log(description="accessShougpz",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessShougpz(){
		LoginUser user = AuthorityUtils.getSecurityUser();
		setResult("usercenter",user.getUsercenter());
		return "select";
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-1
	 * @time 下午02:26:45
	 * @return String
	 * @description   系统推荐配载
	 */
//	public String tuiJanPeiZ(){
//		try {
//			LoginUser user = AuthorityUtils.getSecurityUser();
//			
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("KSSJ", this.getParam("KSSJ"));
//			params.put("JSSJ", this.getParam("JSSJ"));
//			params.put("KEH", this.getParam("KEH"));
//			params.put("UC", user.getUsercenter());
//			
//			
//			List<YaoCJhMx>  getYaoCjhMxOfFull  =new ArrayList<YaoCJhMx>(); 
//	    	getYaoCjhMxOfFull = shougpzService.tuiJYaoCJHmx(params) ;
//			setResult("result",getYaoCjhMxOfFull );
//			
//		} catch (Exception e) {
//			throw new ActionException(e.getMessage()); 
//		}
//		return AJAX;
//	}
	
	
}
