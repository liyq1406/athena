/**
 * 
 */
package com.athena.ckx.module.paicfj.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.ChexYunss;
import com.athena.ckx.module.paicfj.service.ChexYunssService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 车型运输商关系
 * @author hj
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChexYunssAction extends ActionSupport {
	@Inject
	private ChexYunssService chexYunssService;
	@Inject
	private UserOperLog userOperLog;
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param ChexYunss bean) {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param ChexYunss bean) {
		try {
			setResult("result", chexYunssService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车型运输商", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "车型运输商", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param("insert") ArrayList<ChexYunss> insert,
			@Param("edit") ArrayList<ChexYunss> edit,
			@Param("delete") ArrayList<ChexYunss> delete) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(chexYunssService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车型运输商", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "车型运输商", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
