package com.athena.ckx.module.xuqjs.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.*;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.code.EncodeURIComponent;
import com.athena.util.exception.ActionException;
import com.athena.util.exception.ServiceException;
import com.athena.ckx.entity.xuqjs.Anxpy;
import org.apache.commons.lang.StringUtils;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
import com.athena.ckx.module.xuqjs.service.AnxpyService;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;


/* 按需需求平移
 * 0011505
 * xss-201507-17
 * */
@Component
public class AnxpyAction extends ActionSupport {
	@Inject
	private AnxpyService anxpyService;
	
	@Inject
	private UserOperLog userOperLog;

	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute() {
		setResult("loginusercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));		
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param Anxpy bean) {
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需需求平移", "数据查询开始");
		setResult("result", anxpyService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需需求平移", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param Anxpy bean) {
		setResult("result", anxpyService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param Anxpy bean) {
		setResult("result", anxpyService.get(bean));
		return AJAX;
	}
	/**
	 * 行编辑数据保存
	 * @param bean
	 */
	public String save(@Param("addList") ArrayList<Anxpy> addList,@Param("editList") ArrayList<Anxpy> editList,@Param("delList") ArrayList<Anxpy> delList) throws ActionException{
		Map<String,String> message = new HashMap<String,String>();
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需需求平移", "数据编辑开始");
			
			 String k = anxpyService.validateChax(addList, editList); //检验产线 0011651
			
			if(k.equals("0")){
					message.put("message", "您输入的产线有误，请重新输入!");
					setResult("success",false);
					setResult("result",message);
					return AJAX;
			}
			
			//验证标识
			if(!anxpyService.validateFlag(editList, delList)){
				message.put("message", "只有未生效的按需需求平移才能修改或删除，请确认数据!");
				setResult("success",false);
				setResult("result",message);
				return AJAX;
			}
			
			//验证唯一性
			if(!anxpyService.validateUniqueness(addList)){
				message.put("message", "同一用户中心，产线，未生效的按需需求平移的数据只能有一条!");
				setResult("success",false);
				setResult("result",message);
				return AJAX;
			}
			
			//停线时间必须大于上次接口计算时间 0011786
			if(!anxpyService.validateTxsj(addList,editList)){
				message.put("message", "停线时间必须大于接口上次计算时间");
				setResult("success",false);
				setResult("result",message);
				return AJAX;				
			}
			
			message.put("message", anxpyService.save(addList, editList, delList, getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需需求平移", "数据编辑结束");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按需需求平移", "数据编辑结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}

		
	
}
