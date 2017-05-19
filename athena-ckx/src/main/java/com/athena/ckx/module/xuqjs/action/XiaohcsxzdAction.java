package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.module.xuqjs.service.XiaohcService;
import com.athena.ckx.module.xuqjs.service.XiaohcsxzdService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 小火车上线指导
 * @author zbb
 * @date 2016-1-11
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XiaohcsxzdAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private XiaohcService xiaohcService;
	
	@Inject
	private XiaohcsxzdService xiaohcsxzdService;
	
	/**
	 * 获得登录人信息
	 * @author zbb
	 * @date 2016-1-11
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String query(@Param Xiaohc bean){
		try {			
			setResult("result", xiaohcService.selectZxc(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String get(@Param Xiaohc bean ){
		try {
			setResult("result", xiaohcService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String list(@Param Xiaohc bean) {
		try {
			setResult("result", xiaohcService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * @description 上线指导
	 * @param tiql 提前量
	 * @param edit 查询小火车的列表
	 * @author zbb
	 * @date 2016-1-11
	 * @return String
	 */
	public String sxzd(@Param("tiql") String tiql , @Param("list") ArrayList<Xiaohc> edit ) {
		//封装的结果集
		List dataList = new ArrayList<Map>();		
		try {
			if(edit.size()==1){
				//监控一个小火车
				xiaohcsxzdService.monitorOne(edit.get(0), tiql, getLoginUser().getUsername(),dataList);
			}else{
				//监控多个小火车
				xiaohcsxzdService.monitorMore(edit, tiql, getLoginUser().getUsername(),dataList);				
			}			
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			setResult("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		setResult("result", dataList);
		return AJAX;
	}
	
	/**
	 * @description 获取上线指导页面
	 * @param tiql 提前量
	 * @param edit 查询小火车的列表
	 * @author zbb
	 * @date 2016-1-11
	 * @return String
	 */
	public String getsxzd(@Param("tiql") String tiql , @Param("list") ArrayList<Xiaohc> monitor ) {
		
		Map<String,String> message = new HashMap<String,String>();
		try {
			//数据校验	
			
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车上线指导", "获取监控页面");
		}catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车上线指导", "获取监控页面", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		setResult("result", monitor);
		setResult("tiql", tiql);
		return "xiaohcsxzd";
	}
	
}
