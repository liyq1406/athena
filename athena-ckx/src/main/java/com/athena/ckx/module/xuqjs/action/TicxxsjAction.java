package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.module.xuqjs.service.TicxxsjService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 未来几日剔除休息时间
 * @author denggq
 * @date 2012-4-7
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class TicxxsjAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private TicxxsjService ticxxsjService;
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-7
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-7
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	public String executesgjs(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "selectsgjs";
	}
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-7
	 * @return String
	 */
	public String query(@Param Ticxxsj bean){
		try{
			setResult("result", ticxxsjService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "未来几日剔除休息时间", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "未来几日剔除休息时间", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 分页查询手工计算
	 * @param bean
	 * @author denggq
	 * @date 2012-4-7
	 * @return String
	 */
	public String querySgjs(@Param Ticxxsj bean){
		try{
			setResult("result", ticxxsjService.selectSgjs(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "未来几日剔除休息时间", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "未来几日剔除休息时间", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 计算未来几日剔除休息时间
	 * @author denggq
	 * @Date 2012-4-7
	 * @param bean
	 * @return String
	 */
	public String calculate() {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(ticxxsjService.calculate( getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_ticxxsj");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "未来几日剔除休息时间", "数据保存");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "未来几日剔除休息时间", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

}
