package com.athena.ckx.module.baob.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Beihlgz;
import com.athena.ckx.entity.baob.Fahtzcx;
import com.athena.ckx.entity.baob.Youxqljbj;
import com.athena.ckx.module.baob.service.BeihlgzService;
import com.athena.ckx.module.baob.service.FahtzcxService;
import com.athena.ckx.module.baob.service.YouxqljbjService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;



/**
 * 备货令跟踪  
 * @author wy
 * @date 2013-3-21
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YouxqljbjAction extends ActionSupport{
	@Inject
	private YouxqljbjService youxqljbjService;

	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取当前用户信息
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author wy
	 * @date 2013-1-16
	 * @return String
	 */
	public String query(@Param Youxqljbj bean){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
			c.set(Calendar.DATE, c.get(Calendar.DATE)+Integer.parseInt(bean.getBaojtqq()));
			String edit_time = sdf.format(c.getTime());
			bean.setEditime(edit_time);
			setResult("result", youxqljbjService.query(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
}
