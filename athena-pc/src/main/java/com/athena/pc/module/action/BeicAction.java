package com.athena.pc.module.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.pc.entity.Beic;
import com.athena.pc.module.service.BeicService;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;
/**
 * <p>
 * Title:备储计划处理类
 * </p>
 * <p>
 * Description:定义备储计划处理方法
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
 * @date 2012-2-24
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class BeicAction extends ActionSupport {

	@Inject
	private BeicService beicService;
	
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	
	/**
	 * 页面跳转
	 * @return String 跳转后的页面
	 */
	@Log(description="accessBeic",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessBeic(){
		String today = DateUtil.getCurrentDate();
		String todayYYMM = "";
		try{
			todayYYMM = DateUtil.StringFormatToString(today).substring(0,6);
		}catch(ParseException e){
			Logger log =  Logger.getLogger(BeicAction.class);
			log.error(e.getMessage());
		}
		String bcjhh = loginUser.getUsercenter()+todayYYMM;
		setResult("bcjhh",bcjhh);
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}
	
	/**
	 * 查询备储信息
	 * @param bean 备储实体类
	 * @return AJAX 备储集合
	 */
	public String queryBeic(@Param Beic bean){
		setResult("result",beicService.select(bean));
		return AJAX;
	}
	/**
	 * 查询备储明细信息
	 * @param bean 备储实体集
	 * @return AJAX
	 */
	public String queryBeicmx(@Param Beic bean){
		setResult("result",beicService.select(bean,this.getParams()));
		return AJAX;
	}
	
	/**
	 * 查询备储明细信息
	 * @param bean 备储实体集
	 * @return AJAX
	 */
	public String queryBeicjhh(@Param Beic bean){
		Map<String,String> param = this.getParams();
		param.put("USERCENTER", loginUser.getUsercenter());
		String result = beicService.selectBeicjhh(param);
		setResult("usercenter",param.get("USERCENTER"));
		setResult("sxCount",Integer.parseInt(result));
		setResult("beicjhh",param.get("bcjhh"));
		return AJAX;
	}
	/**
	 * 批量保存备储信息
	 * @param bean 备储实体集
	 * @param operate 操作类型
	 * @param insertList 备储明细批量插入集合
	 * @param editList 备储明细批量修改集合
	 * @param deleteList 备储明细批量删除集合
	 * @return AJAX
	 */
	@Log(description="saveBeic",content="{Toft_SessionKey_UserData.userName}执行了：编辑操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：编辑操作出现异常")
	public String saveBeic(@Param Beic bean,@Param("operant") Integer operate,@Param("f_pc_insert")ArrayList<Beic> insertList,
			@Param("f_pc_edit")ArrayList<Beic> editList,@Param("f_pc_delete")ArrayList<Beic> deleteList){
		Map<String,String> msgMap = new HashMap<String,String>();
		try {
			Message message = new Message(beicService.saveBeic(bean,operate,insertList,editList,deleteList,loginUser.getUsername()),"i18n.pc.pc_message");
			msgMap.put("message", message.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PC,"特殊计划","特殊计划操作,操作员:"+loginUser.getUsername());
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_PC, "特殊计划", "特殊计划操作出错,操作员:"+loginUser.getUsername(), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("success",false );
			msgMap.put("message", e.getMessage());
		}
		setResult("result",msgMap);
		return AJAX;
	}
	/**
	 * 删除备储及备储明细信息
	 * @param bean 备储实体集
	 * @return AJAX
	 */
	public String deleteBeic(@Param Beic bean){
		try {
			beicService.batchDelete(bean);
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
	
}
