
package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.module.xuqjs.service.XiaohccxService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 小火车车厢
 * @author denggq
 * @date 2012-4-10
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XiaohccxAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private XiaohccxService xiaohccxService;
	
	
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
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String query(@Param Xiaohccx bean){
		Assert.notNull(bean.getXiaohcbh(),"增加时不能刷新！");
		try {
			setResult("result", xiaohccxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车车厢", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车车厢", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String get(@Param Xiaohccx bean){
		try {
			setResult("result", xiaohccxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车车厢", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车车厢", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String list(@Param Xiaohccx bean) {
		try {
			setResult("result", xiaohccxService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车车厢", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车车厢", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String remove(@Param Xiaohccx bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			setResult("result", xiaohccxService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车车厢", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车车厢", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String delete(@Param Xiaohccx bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			Message m = new Message(xiaohccxService.remove(bean), "i18n.ckx.cangk.i18n_fahzt");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车车厢", "数据删除");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车车厢", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
}
