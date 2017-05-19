package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Cmj;
import com.athena.ckx.module.xuqjs.service.CmjService;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * CMJ
 * @author denggq
 * @date 2012-3-26
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CmjAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private CmjService cmjService;
	
	@Inject
	private DownLoadServices  downloadServices;

	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-3-26
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-3-26
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
	 * @date 2012-3-26
	 * @return String
	 */
	public String query(@Param Cmj bean){
		try{
			setResult("result", cmjService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "CMJ", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "CMJ", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 计算CMJ
	 * @param bean
	 * @author denggq
	 * @date 2012-4-5
	 * @return String
	 */
	public String calculateCmj(){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(cmjService.calculateCmj(getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "CMJ", "计算CMJ");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "CMJ", "计算CMJ", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param Cmj bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Cmj> rows = cmjService.list(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("cmj.ftl", dataSurce, response, "cmj", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "CMJ", "数据导出");
		
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "CMJ", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
}
