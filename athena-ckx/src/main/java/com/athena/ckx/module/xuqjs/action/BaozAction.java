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
import com.athena.ckx.module.xuqjs.service.BaozService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
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
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class BaozAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	/**
	 * 注入BaozService
	 * @author denggq
	 * @date 2012-3-6
	 */
	@Inject 
	private BaozService baozService;
	
	
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
	 * 主页面
	 * @param bean
	 * @return
	 * @author denggq
	 * @date 20111229
	 */
	public String execute(@Param Baoz bean) {
		@SuppressWarnings("rawtypes")
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map) ;
		String  params = 
		    "edit_baozlx:   root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_baozmc:   	root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_changd:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_kuand:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_gaod:      root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_baozzl:        root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_leib:         root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_caiz:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_shifhs:       root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_zhedgd:      root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_duidcs:       root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_baiffx: 	root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_biaos:    root,ZBCPOA,WULGYY,ZXCPOA;";
	
		for(String s0:params.split(";")){
			String name = s0.split(":")[0].trim();		//字段隐藏属性名
			String roles = s0.split(":")[1].trim();	//所有不隐藏的角色
			if(roles.contains(key)){
				setResult(name, true);
			}else{
				setResult(name, false);
			}
		}
		setResult("role", key);//角色
		return  "select";
	}

	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 * @return String
	 */
	public String query(@Param Baoz bean) {
		try{
			setResult("result", baozService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 单记录查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 */
	public String get(@Param Baoz bean) {
		try{
			setResult("result", baozService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "单数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询
	 * @param bean
	 * @author denggq
	 * @date 20120224
	 */
	public String list(@Param Baoz bean) {
		try{
			setResult("result", baozService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "多数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 数据保存（多条数据操作）
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 */
	public String saves(@Param("insert") ArrayList<Baoz> insert,@Param("edit") ArrayList<Baoz> edit,@Param("delete") ArrayList<Baoz> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(baozService.save(insert, edit ,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 数据失效
	 * @author denggq
	 * @date 20111229
	 */
	public String remove(@Param Baoz bean) {
		
		bean.setEditor(getLoginUser().getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(baozService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String downloadMob(@Param Baoz bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("baozMob.ftl", dataSurce, response, "baozMob", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
			message.put("message", XlsHandlerUtils.analyzeXls(request));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		if("1".equals(message.get("message"))){
			message.put("message", "导入成功");
		}
		setRequestAttribute("uploudmessage",message.get("message")); 
		return "upload";
	}
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	public String download(@Param Baoz bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Baoz> rows = baozService.listImport(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("baoz.ftl", dataSurce, response, "包装", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "包装", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "包装", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
}
