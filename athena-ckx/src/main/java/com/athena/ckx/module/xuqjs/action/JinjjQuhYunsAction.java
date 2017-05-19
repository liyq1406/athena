package com.athena.ckx.module.xuqjs.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.JinjjQuhYuns;
import com.athena.ckx.entity.xuqjs.QuhYuns;
import com.athena.ckx.entity.xuqjs.Tuopbzdygx;
import com.athena.ckx.module.xuqjs.service.BaozService;
import com.athena.ckx.module.xuqjs.service.JinjjQuhYunsService;
import com.athena.ckx.module.xuqjs.service.QuhyunsService;
import com.athena.ckx.module.xuqjs.service.TuopbzdygxService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.quhyuns.XlsHandlerUtilsljshhbftj;
import com.athena.ckx.util.xls.quhyunsjinjj.XlsHandlerUtilsquhyunsjinjj;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.util.tag.XlsHandlerUtils;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class JinjjQuhYunsAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出

	
	/**取货运输急件参考系
	 * 注入BaozService
	 * @author denggq
	 * @date 2012-3-6
	 */
	@Inject 
	private JinjjQuhYunsService jinjjQuhYunsService;
	

	@Inject
	private AbstractIBatisDao baseDao;
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2018-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 * @return String
	 */
	public String query(@Param JinjjQuhYuns bean) {
		Map<String,String> message = new HashMap<String,String>();
		try{
			GetPostOnly.checkqhqx(bean.getUsercenter());
			if(bean.getUsercenter()==null){
				if((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")!=null && ((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")).size()>0){
					List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
					StringBuffer sb=new StringBuffer();
					for (String s : uclist) {
					sb.append("'"+s+"',");
					}
					sb.delete(sb.length()-1, sb.length());
					bean.setUclist(sb.toString());
				}
			}
			setResult("result", jinjjQuhYunsService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输急件参考系", "数据查询");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
	
		return AJAX;
	}
	
	
	/**
	 * 单记录查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 */
	public String get(@Param JinjjQuhYuns bean) {
		try{
			setResult("result", jinjjQuhYunsService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输急件参考系", "单数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询
	 * @param bean
	 * @author denggq
	 * @date 20120224
	 */
	public String list(@Param JinjjQuhYuns bean) {
		try{
			setResult("result", jinjjQuhYunsService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输急件参考系", "多数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 数据保存（多条数据操作）
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 */
	public String saves(@Param("insert") ArrayList<JinjjQuhYuns> insert,@Param("edit") ArrayList<JinjjQuhYuns> edit,@Param("delete") ArrayList<JinjjQuhYuns> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(jinjjQuhYunsService.save(insert, edit ,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输急件参考系", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	
	
	
	/**
	 * 导出模板
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	public String downloadMob(@Param JinjjQuhYuns bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("quhyunsJinjjMob.ftl", dataSurce, response, "quhyunsJinjjMob", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输急件参考系", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}

	
	/**
	 * 导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String upload(){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			Map<String, String> map=new HashMap<String, String>();
			String mes = XlsHandlerUtilsquhyunsjinjj.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输急件参考系", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输急件参考系", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		if("1".equals(message.get("message"))){
			message.put("message", "导入成功");
		}
		setRequestAttribute("uploudmessage",message.get("message")); 
		return "upload";
	}
}
