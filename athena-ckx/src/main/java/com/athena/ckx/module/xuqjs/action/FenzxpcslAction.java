package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Fenzxpcsl;
import com.athena.ckx.module.xuqjs.service.FenzxpcslService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 分装线排产数量
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class FenzxpcslAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private FenzxpcslService fenzxpcslService;
	
	/**
	 * 获取登录人信息
	 * @return bean 登录人信息
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 转到分装线排产数量主页面
	 * @return
	 */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 查询分装线排产数量
	 * @param bean 查询参数
	 * @return 查询结果
	 */
	public String queryFenzxpcsl(@Param Fenzxpcsl bean){
		setResult("result", fenzxpcslService.select(bean,this.getParams()));
		return AJAX;
	}
	
	/**
	 * 保存分装线排产数量
	 * @return
	 */
	public String saveFenzxpcsl(@Param Fenzxpcsl bean, @Param("operant") Integer operant){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(fenzxpcslService.save(bean, operant, getLoginUser().getUsername()), "i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分装线排产数量", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分装线排产数量", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 删除分装线排产数量
	 * @return
	 */
	public String deleteFenzxpcsl(@Param Fenzxpcsl bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(fenzxpcslService.delete(bean), "i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "分装线排产数量", "数据删除");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "分装线排产数量", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	

}
