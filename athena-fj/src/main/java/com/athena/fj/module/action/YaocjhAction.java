package com.athena.fj.module.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.fj.entity.Yaocjh;
import com.athena.fj.module.service.YaocjhService;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;
/**
 * <p>
 * Title:要车计划逻辑类
 * </p>
 * <p>
 * Description:定义要车计划逻辑类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2011-12-21
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YaocjhAction extends ActionSupport {
	//注入service
	@Inject
	private YaocjhService yaocjhService;
	
	LoginUser user = AuthorityUtils.getSecurityUser();
	
	
	/**
	 * 执行跳转页面方法
	 * @author 贺志国
	 * @date 2011-12-21
	 * @param bean 要车计划bean
	 * @param operate 操作类型
	 * @return String 返回跳转
	 */
	@Log(description="accessJihgz",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessJihgz(@Param Yaocjh bean,@Param("operate") Integer operate){
	
		return "select";
	}
	
	
	/**
	 * 要车计划查询
	 * @author 贺志国
	 * @date 2011-12-21
	 * @param bean 要车计划bean
	 * @return String 返回ajax
	 */
	@Log(description="queryYaocjh",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String queryJihgz(){ 
		try {
			Map<String,String> params = this.getParams();
			params.put("usercenter", user.getUsercenter());
			List<Map<String,Object>> listObj = yaocjhService.selectJihgz(params);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("total", listObj.size());
			map.put("rows", listObj);
			setResult("result", map);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
}
