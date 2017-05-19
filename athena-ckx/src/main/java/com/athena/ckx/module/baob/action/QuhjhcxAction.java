package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Quhjhcx;
import com.athena.ckx.module.baob.service.QuhjhcxService;
import com.athena.ckx.util.DateTimeUtil;
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
 * 取货计划查询 
 * @author lc
 * @date 2016-11-24
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class QuhjhcxAction extends ActionSupport{
	@Inject
	private QuhjhcxService quhjhcxService;

	@Inject
	private UserOperLog userOperLog;
	
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;
	
	/**
	 * 获取当前用户信息
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @param usercenter
	 * @param yunsjhh
	 * @return String
	 */
	public String execute(@Param("usercenter") String usercenter,@Param("yunsjhh") String yunsjhh){
		if(null!=yunsjhh&&!"".equals(yunsjhh)){
			this.setRequestAttribute("usercenter", usercenter);
		}else{
			setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		}
		this.setRequestAttribute("yunsjhh", yunsjhh);		
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author lc
	 * @date 2016-11-24
	 * @return String
	 */
	public String query(@Param Quhjhcx bean){
		try {			
			setResult("result", quhjhcxService.query(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", "查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", "查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author lc
	 * @date 2017-1-9
	 * @return String
	 */
	public String download(@Param Quhjhcx bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Quhjhcx> rows = quhjhcxService.listImport(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("quhjhcx.ftl", dataSurce, response, "取货计划查询", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货计划查询", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货计划查询", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 查询检查数据条数是否超过5000
	 * @author lc
	 * @date 2017-01-12
	 * @param bean
	 * @return
	 */
	public String expcheck(@Param Quhjhcx bean){
		Map<String,String> params = this.getParams();
		int re = 0;
		re = quhjhcxService.selectQuhjhcxCount(params);
		if(re>5000){
			setResult("errorMessage", "导出数据超过5000条，请重新选择导出条件！");
			return AJAX;
		}
		return AJAX;
	}
	
	
	/**
	 * 跳转到初始页面
	 * @return String
	 */
	public String executeztgzcx(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	/**
	 * 分页查询在途跟踪
	 * @param bean
	 * @author lc
	 * @date 2016-11-25
	 * @return String
	 */
	public String queryzaitgzcx(@Param Quhjhcx bean){
		try {			
			setResult("result", quhjhcxService.queryzaitgzcx(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", "查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", "查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 按照查询条件导出在途跟踪
	 * @param bean
	 * @author lc
	 * @date 2017-1-10
	 * @return String
	 */
	public String downloadZaitgz(@Param Quhjhcx bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Quhjhcx> rows = quhjhcxService.listImportZaitgz(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("zaitgzcx.ftl", dataSurce, response, "在途跟踪查询", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "在途跟踪查询", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "在途跟踪查询", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 跳转到初始页面
	 * @return String
	 */
	public String executegysqhjhwcl(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		
		StringBuffer sb = new StringBuffer(); 		
		sb.append("{"); 		
		//获取当前年
		Integer year1 = Integer.parseInt(DateTimeUtil.getDateTimeStr("yyyy"));
		//获取当前年的前一年
		Integer year = year1-1; 
		setResult("currentYear", year1);
		for (int i = 0; i < 4; i++) { 	
			sb.append("'"+year+"':'"+year+"',"); 
			year++; 			
		}
		sb.delete(sb.length()-1,sb.length());
		sb.append("}"); 		
		setResult("year", sb.toString());
		
		return "select";
	}
	
	/**
	 * 分页查询供应商取货计划完成率计算
	 * @param bean
	 * @author lc
	 * @date 2016-11-26
	 * @return String
	 */
	public String querygysqhjhwcl(@Param Quhjhcx bean){
		try {			
			setResult("result", quhjhcxService.querygysqhjhwcl(bean,getParam("exportXls")));
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
