package com.athena.pc.module.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.pc.entity.Leijce;
import com.athena.pc.module.service.LeijceService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * <p>
 * Title:累计交付差额处理类
 * </p>
 * <p>
 * Description:定义累计交付差额处理方法
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
 * @date 2012-6-12
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LeijceAction extends ActionSupport {

	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private LeijceService leijceService;
	
	
	/**
	 * 页面跳转
	 * @author 贺志国
	 * @date 2012-6-12
	 * @return String 跳转后的页面
	 */
	public String accessLeijce(){
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "累计交付差额", "菜单跳转到累计交付差额页面");
		return "select";
	}
	
	/**
	 * 查询累计交付差额
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param bean 实体类
	 * @return String AJAX 累计交付差额结果集
	 */
	public String queryLeijce(@Param Leijce bean){
		Map<String,Object> leijceMap = leijceService.selectLeijce(bean, getParams());
		setResult("result",leijceMap);
		return AJAX;
	}
	
	
	/**
	 * 删除累计交付差额,调用系统提供方法doDelete
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param bean 实体类
	 * @return String AJAX 消息
	 */
	public String deleteLeijce(@Param Leijce bean){
		try {	
			Map<String, String> message = new HashMap<String,String>();
			leijceService.deleteLeijce(bean);
			Message msg = new Message("del_success","i18n.pc.pc_message");
			message.put("message", msg.getMessage());
			setResult("result",message);
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
			return AJAX;
	}
	
	
	/**
	 * 批量执行更新操作
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param editList 页面编辑结果集
	 * @return String AJAX 消息
	 */
	public String updateLeijce(@Param("edit")ArrayList<Leijce> editList){
		try {
			Map<String,String> msgMap = new HashMap<String,String>();
			Message message = new Message(leijceService.updateLeijce(editList,loginUser.getUsername()),"i18n.pc.pc_message");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
			return AJAX;
	}
	
	
}
