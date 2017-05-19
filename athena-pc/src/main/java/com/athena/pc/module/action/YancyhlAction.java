package com.athena.pc.module.action;

import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.pc.entity.Yaonbhl;
import com.athena.pc.module.service.YancyhhlService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * Title:延迟要货令查询处理类
 * </p>
 * <p>
 * Description:定义延迟要货令查询处理方法
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
 * @date 2012-6-15
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YancyhlAction extends ActionSupport {
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private YancyhhlService yancyhhlService;
	/**
	 * 页面跳转到延迟要货令查询页面
	 * @author 贺志国
	 * @date 2012-6-15
	 * @return String success 成功后跳转
	 */
	public String accessYancyhl(){
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "延迟要货令查询", "菜单跳转到延迟要货令查询页面");
		return "select";
	}
	
	/**
	 * 根据权限查询计划员所在产线组下的产线所延迟交付的要货令
	 * @author 贺志国
	 * @date 2012-6-15
	 * @param bean 要货令实体集
	 * @return String AJAX 结果集
	 */
	public String queryYancyhl(@Param Yaonbhl bean){
		//查询计划员所在用户中心下的所有产线
//		String jihyzbh =loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihyzbh = postMap.get("PCJIHY");
		Map<String,String> param = this.getParams();
		param.put("jihyzbh", jihyzbh);
		param.put("usercenter", loginUser.getUsercenter());
		Map<String,Object> yaohlMap = yancyhhlService.selectYancyhl(bean, getParams());
		setResult("result",yaohlMap);
		return AJAX;
	}
	
}
