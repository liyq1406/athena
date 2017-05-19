package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Lingjshhbftj;
import com.athena.ckx.module.baob.service.LingjshhbftjService;
import com.athena.ckx.util.xls.lingjshhbftj.XlsHandlerUtilsljshhbftj;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;



/**
 * 零件收货和报废统计  
 * @author lc
 * @date 2016-11-1
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjshhbftjAction extends ActionSupport{
	@Inject
	private LingjshhbftjService lingjshhbftjService;

	@Inject
	private UserOperLog userOperLog;
	
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
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
	 * @author lc
	 * @date 2016-11-1
	 * @return String
	 */
	public String query(@Param Lingjshhbftj bean){
		try {			
			setResult("result", lingjshhbftjService.query(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 分页查询收货明细
	 * @param bean
	 * @author lc
	 * @date 2016-11-4
	 * @return String
	 */
	public String queryshouhmx(@Param Lingjshhbftj bean){
		try {			
			setResult("result", lingjshhbftjService.queryshouhmx(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 分页查询报废明细
	 * @param bean
	 * @author lc
	 * @date 2016-11-7
	 * @return String
	 */
	public String querybaofmx(@Param Lingjshhbftj bean){
		try {			
			setResult("result", lingjshhbftjService.querybaofmx(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 零件收货和报废统计导入模板下载
	 * lc 2016-11-3
	 */
	public String downloadLjshhbftjMob(@Param Lingjshhbftj bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("ljshhbftjMob.ftl", dataSurce, response, "零件收货和报废统计-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件收货和报废统计", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件收货和报废统计", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件收货和报废统计", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 零件收货和报废统计导入
	 * @author lc
	 * @Date 2016-11-3
	 * @Param bean
	 * @return String
	 * */
	public String uploadLjshhbftj(@Param Lingjshhbftj bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			Map<String, String> map=new HashMap<String, String>();
			String mes = XlsHandlerUtilsljshhbftj.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "零件收货和报废统计", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "零件收货和报废统计", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getLoginUser().getUsercenter());
		return "upload";
     }
	
}
