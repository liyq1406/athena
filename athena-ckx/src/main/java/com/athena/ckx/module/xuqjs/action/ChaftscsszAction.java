package com.athena.ckx.module.xuqjs.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.module.xuqjs.service.ChaftscsszService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 系统参数定义
 * @author denggq
 * @Date 2012-3-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChaftscsszAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private ChaftscsszService chaftscsszService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author denggq
	 * @Date 2012-3-19
	 * @return String
	 */
	public String execute() {
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	

	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String query(@Param Xitcsdy bean) {
		try{
			bean.setZidlx("CFTS");
			setResult("result", chaftscsszService.selectchaf(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "DDBH拆分天数参数设置", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "DDBH拆分天数参数设置", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 行编辑保存方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Xitcsdy> insert,@Param("edit") ArrayList<Xitcsdy> edit,@Param("delete") ArrayList<Xitcsdy> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(chaftscsszService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "DDBH拆分天数参数设置", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "DDBH拆分天数参数设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	
	
}
