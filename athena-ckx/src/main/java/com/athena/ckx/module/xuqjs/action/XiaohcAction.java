package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.module.xuqjs.service.XiaohcService;
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
 * 小火车
 * @author denggq
 * @date 2012-4-10
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XiaohcAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private XiaohcService xiaohcService;
	
	
	/**
	 * 获得登录人信息
	 * @author denggq
	 * @date 2012-4-10
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
	 * 跳转到初始页面
	 * @author hj
	 * @date 2013-12-10
	 * @return String
	 */
	public String executeZxc(){
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
			if(getzbcZxc()){
				setResult("result", xiaohcService.select(bean));
			}else{
				setResult("result", xiaohcService.selectZxc(bean));
			}
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
	 * 失效
	 * @param bean
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String remove(@Param Xiaohc bean){
		Map<String,String> message = new HashMap<String,String>();
		
		bean.setEditor(getLoginUser().getUsername());			//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());	//修改时间
		try {
			if(getzbcZxc()){
				Message m = new Message(xiaohcService.doDelete(bean), "i18n.ckx.xuqjs.i18n_xiaohc");
				message.put("message",m.getMessage());//逻辑删除
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据失效");
			}else{
				Message m = new Message(xiaohcService.doDeleteZXC(bean), "i18n.ckx.xuqjs.i18n_xiaohc");
				message.put("message",m.getMessage());//逻辑删除
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据失效");
			}
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	/**
	 * @description 保存车厢
	 * @param bean 小火车
	 * @param Xiaohccxs
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String saves(@Param Xiaohc bean,@Param("operant") Integer operant,@Param("list_insert") ArrayList<Xiaohccx> insert,@Param("list_edit") ArrayList<Xiaohccx> edit , @Param("list_delete") ArrayList<Xiaohccx> delete) {
		
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(xiaohcService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", message);
		return AJAX;
	}
	
	/**
	 * @description 保存小火车-小火车模板控制表
	 * @param bean 小火车
	 * @param Xiaohccxs
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String savesZxc(@Param Xiaohc bean) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			String mes = xiaohcService.saveZxc(bean,getLoginUser().getUsername());
			message.put("message", mes);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", message);
		return AJAX;
	}
	

	/**
	 * 判断当前登录是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	public boolean getzbcZxc(){
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		boolean flag = false;
		if("ZBC".equals(zbczxc)){
			flag = true;
		}
		return flag;
	}
}
