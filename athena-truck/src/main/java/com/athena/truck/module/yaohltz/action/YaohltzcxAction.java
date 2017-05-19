package com.athena.truck.module.yaohltz.action;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Yaohltzcx;
import com.athena.truck.module.yaohltz.service.YaohltzService;
import com.athena.truck.module.yaohltz.service.YaohltzcxService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 要货令调整查询
 * @author chenpeng
 * @date 2015-1-29
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YaohltzcxAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private YaohltzcxService yaohltzcxService;
	
	@Inject
	private YaohltzService yaohltz;
	/**
	 * 登录人信息
	 * @author chenpeng
	 * @date 2015-1-29
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author chenpeng
	 * @date 2015-1-29
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-29
	 * @return String
	 */
	public String query(@Param Yaohltzcx bean){
		try {
			setResult("result", yaohltzcxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "要货令调整", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "要货令调整", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	
	/**
	 * 根据大站台查询卸货站台
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-29
	 * @return String
	 */
	public String queryXiehzt(@Param("usercenter") String usercenter,@Param("daztbh") String daztbh) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("usercenter", usercenter);
			params.put("daztbh",daztbh);
			setResult("result", yaohltzcxService.list(params));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-29
	 * @return String
	 */
	public String get(@Param Yaohltzcx bean){
		try {
			setResult("result", yaohltzcxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "要货令调整", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "要货令调整", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String list(@Param Yaohltzcx bean) {
		try {
			setResult("result", yaohltzcxService.listYaohltz(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "要货令调整", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "要货令调整", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}
