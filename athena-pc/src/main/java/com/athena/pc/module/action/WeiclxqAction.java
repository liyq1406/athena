package com.athena.pc.module.action;

import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.pc.entity.Yaonbhl;
import com.athena.pc.module.service.WeiclxqService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * <p>
 * Title:未处理需求查询处理类
 * </p>
 * <p>
 * Description:未处理需求查询处理方法
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
 * @date 2012-6-18
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class WeiclxqAction extends ActionSupport {

	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private WeiclxqService weiclxqService;
	
	/**
	 * 未处理需求查询页面跳转
	 * @author 贺志国
	 * @date 2012-6-18
	 * @return String select 跳转后的页面
	 */
	public String accessWeiclxq(){
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "未处理需求查询", "菜单跳转到未处理需求查询页面");
		return "select";
	}
	
	/**
	 * 根据权限查询计划员所在产线组下的未处理的零件需求
	 * @author 贺志国
	 * @date 2012-6-19
	 * @param bean 实体集
	 * @return String AJAX 结果集
	 */
	public String queryWeiclxq(@Param Yaonbhl bean){
		//查询计划员所在用户中心下的所有产线
//		String jihyzbh =loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihyzbh = postMap.get("PCJIHY");
		Map<String,String> param = this.getParams();
		param.put("jihyzbh", jihyzbh);
		param.put("usercenter", loginUser.getUsercenter());
		Map<String,Object> weiclxqMap = weiclxqService.selectWeiclxq(bean, param);
		setResult("result",weiclxqMap);
		return AJAX;
	}
	
}
