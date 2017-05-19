package com.athena.pc.module.action;

import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.pc.entity.Qickc;
import com.athena.pc.module.service.QickcService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * <p>
 * Title:期初库存处理类
 * </p>
 * <p>
 * Description:定义期初库存处理方法
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
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class QickcAction extends ActionSupport {
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private QickcService qickcService;
	
	/**
	 * 菜单页面跳转
	 * @author 贺志国
	 * @date 2012-6-18
	 * @return  String success 成功后跳转
	 */
	public String accessQickc(){
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "期初库存", "菜单跳转到期初库存页面");
		return "select";
	}
	
	
	/**
	 * 根据权限查询计划员所以产线组下的产线期初存查询
	 * @author 贺志国
	 * @date 2012-6-18
	 * @param bean 实体类
	 * @return String AJAX 结果集
	 */
	public String queryQickcAll(@Param Qickc bean){
		//查询计划员所在用户中心下的所有产线
//		String jihyzbh =loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihyzbh = postMap.get("PCJIHY");
		Map<String,String> param = this.getParams();
		param.put("jihyzbh", jihyzbh);
		param.put("usercenter", loginUser.getUsercenter());
		Map<String,Object> qickcMap = qickcService.selectQickcAll(bean,param);
		setResult("result",qickcMap);
		return AJAX;
	}
	
}
