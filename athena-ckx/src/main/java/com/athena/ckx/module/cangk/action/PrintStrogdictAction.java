/**
 * 
 */
package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.PrintStrogdict;
import com.athena.ckx.module.cangk.service.PrintStrogdictService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;



@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintStrogdictAction extends ActionSupport {
	
	@Inject
	private PrintStrogdictService printStrogdictService;

	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
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
	public String execute(@Param PrintStrogdict bean) {
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
	public String query(@Param PrintStrogdict bean) {
		//单据类型的分页查询
		try{
			setResult("result", printStrogdictService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据录入", "数据查询");
		}catch(Exception e){
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据录入", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 直接数据保存
	 * @param bean
	 */
	public String saves(@Param("ckx_strogdict_edit") ArrayList<PrintStrogdict> edit,@Param PrintStrogdict bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//权限的批量处理总方法
			Message m=new Message(printStrogdictService.saves(edit,bean),"i18n.ckx.cangk.i18n_fahzt");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
}
