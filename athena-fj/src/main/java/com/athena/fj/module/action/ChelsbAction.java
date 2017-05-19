package com.athena.fj.module.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.fj.entity.Chelsb;
import com.athena.fj.module.common.CollectionUtil;
import com.athena.fj.module.service.ChelsbService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ActionException;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * <p>
 * Title:车辆资源申报逻辑类
 * </p>
 * <p>
 * Description:定义车辆资源申报逻辑类
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
public class ChelsbAction extends ActionSupport {
	//注入service
	@Inject
	private ChelsbService chelsbService;
	@Inject
	private UserOperLog userOperLog;
	
	LoginUser user = AuthorityUtils.getSecurityUser();
	/**
	 * 获取用户信息
	 * @return 用户信息
	 */
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}	
	
	/**
	 * 执行跳转页面方法
	 * @author 贺志国
	 * @date 2011-12-21
	 * @param bean 车辆资源bean
	 * @param operate 操作类型
	 * @return String 返回跳转
	 */
	public String accessChelsb(){
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报页面开始");
			List<Map<String,String>> chexList = chelsbService.selectChex();
			List<Map<String,String>> yunssList = chelsbService.selectYunss(user.getUsercenter());
			String strChex = CollectionUtil.listToJson(chexList, "CHEXBH", "CHEXBH");
			String strYunss = CollectionUtil.listToJson(yunssList, "GCBH", "GCBH");
			setResult("chexJson",strChex);
			setResult("yunssJson",strYunss);
			setResult("usercenter",user.getUsercenter());
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报页面跳转并初始化下拉框数据结束");
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return "select";
	}
	
	
	/**
	 * 车辆资源查询
	 * @author 贺志国
	 * @date 2011-12-21
	 * @param bean 车辆资源bean
	 * @return String 返回ajax
	 */
	public String queryChelsb(@Param Chelsb bean){ 
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报查询开始");
			bean.setUsercenter(user.getUsercenter());
			setResult("result", chelsbService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报查询结束");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报查询异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	
	/**
	 * 执行保存数据方法（增加|修改|删除）
	 * @author 贺志国
	 * @date 2011-12-22
	 * @param bean
	 * @return String
	 */
	public String saveChelsb(@Param("insert")ArrayList<Chelsb> insertList,@Param("delete")ArrayList<Chelsb> deleteList){
		Map<String,String> msgMap = new HashMap<String,String>();
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报批量保存数据开始");
			Message message = new Message(chelsbService.save(insertList, deleteList, user.getUsername(),user.getUsercenter()),"i18n.fj.fj_message");
			msgMap.put("message", message.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报批量保存数据结束");
		} catch (Exception e) {
			msgMap.put("message", e.getMessage());//若出现异常则抛出错误消息
			userOperLog.addError(CommonUtil.MODULE_FJ, "车辆申报", "车辆申报批量保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",msgMap);
		return AJAX;
		
	}
	
}
