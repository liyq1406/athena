package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Lixd;
import com.athena.ckx.module.xuqjs.service.LixdService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 离线点
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LixdAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private LixdService lixdService;
	
	/**
	 * 获取登录人信息
	 * @return bean 登录人信息
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 转到离线点主页面
	 * @return
	 */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 查询离线点
	 * @param bean 查询参数
	 * @return 查询结果
	 */
	public String queryLixd(@Param Lixd bean){
		setResult("result", lixdService.select(bean));
		return AJAX;
	}
	
	/**
	 * 保存离线点
	 * @return
	 */
	public String saveLixd(@Param Lixd bean, @Param("operant") Integer operant){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(lixdService.save(bean, operant, getLoginUser().getUsername()), "i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "离线点", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "离线点", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

}
