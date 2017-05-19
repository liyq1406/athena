package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.module.xuqjs.service.FenzxService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 焊装分装线
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class FenzxAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private FenzxService hanzfzxService;
	
	/**
	 * 登录人信息
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 获取分装线信息
	 * @return
	 */
	public String getFenzxInfo(@Param Fenzx bean){
		try {
			setResult("result", hanzfzxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分装线", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分装线", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 保存分装线
	 * @return
	 */
	public String saveHanzfzx(@Param Fenzx bean, @Param("operant") Integer operant){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(hanzfzxService.save(bean, operant, getLoginUser().getUsername()), "i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分装线", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分装线", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
}
