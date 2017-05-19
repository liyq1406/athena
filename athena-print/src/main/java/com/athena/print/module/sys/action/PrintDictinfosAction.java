/**
 * 
 */
package com.athena.print.module.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.print.entity.sys.PrintDictinfos;
import com.athena.print.module.sys.service.PrintDictinfosService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintDictinfosAction extends ActionSupport {
	@Inject
	private PrintDictinfosService printDictinfosService;
	
	@Inject
	private UserOperLog userOperLog;

	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	/**
	 * 获取用户信息
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param PrintDictinfos bean) {
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		//页面初始化显示的页面
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PrintDictinfos bean) {
		//单据类型的分页查询
		setResult("result", printDictinfosService.selectU(bean));
		return AJAX;
	}
	
	
	/**
	 * 直接数据保存
	 * @param bean
	 */
	public String saves(@Param("insert") ArrayList<PrintDictinfos> insert,@Param("edit") ArrayList<PrintDictinfos> edit,@Param("delete") ArrayList<PrintDictinfos> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//权限的批量处理总方法
			Message m=new Message(printDictinfosService.saves(insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "单据组初始化操作", "保存数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "单据组初始化操作", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
}
