package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.module.xuqjs.service.FenpqService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 分配区
 * @author denggq
 * @date 2012-3-21
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class FenpqAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private FenpqService fenpqService;
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-3-21
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-3-21
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-3-21
	 * @return String
	 */
	public String query(@Param Fenpq bean){
		try{
			Assert.notNull(bean.getShengcxbh(),"增加时不能刷新！");
			setResult("result", fenpqService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分配区", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分配区", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-21
	 * @return String
	 */
	public String get(@Param Fenpq bean){
		try {
			setResult("result", fenpqService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分配区", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分配区", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-21
	 * @return String
	 */
	public String list(@Param Fenpq bean) {
		try {
			setResult("result", fenpqService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分配区", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分配区", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 失效
	 * @param bean
	 * @author denggq
	 * @date 2012-3-21
	 * @return String
	 */
	public String remove(@Param Fenpq bean){
		try {
			setResult("result", fenpqService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分配区", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分配区", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}
