package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxJiaofrl;
import com.athena.ckx.module.xuqjs.service.CkxJiaofrlService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 交付日历
 * @author denggq
 * @date 2012-4-6
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxJiaofrlAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入JiaofmsdService
	 * @author denggq
	 * @date 2012-4-6
	 * @return bean
	 */
	@Inject
	private CkxJiaofrlService jiaofrlsService;
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-6
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-6
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
	 * @date 2012-4-6
	 * @return String
	 */
	public String query(@Param CkxJiaofrl bean){
		try{
			setResult("result", jiaofrlsService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付日历", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付日历", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 计算交付日历
	 * @author denggq
	 * @Date 2012-4-6
	 * @param bean
	 * @return String
	 */
	public String calculate(@Param("year") String year) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(jiaofrlsService.calculate(getLoginUser().getUsercenter(),year,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "交付日历", "计算交付日历");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "交付日历", "计算交付日历", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

}
