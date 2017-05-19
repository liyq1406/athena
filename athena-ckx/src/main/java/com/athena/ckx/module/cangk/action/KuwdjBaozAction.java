package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Kuwdj;
import com.athena.ckx.entity.cangk.KuwdjBaoz;
import com.athena.ckx.module.cangk.service.KuwdjBaozService;
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
 * 库位等级与包装
 * @author denggq
 * @date 2012-1-17
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class KuwdjBaozAction extends ActionSupport{


	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入KuwdjBaozService
	 * @author denggq
	 * @date 2012-1-17
	 * @return bean
	 */
	@Inject
	private KuwdjBaozService kuwdjBaozService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	
	/**
	 * 获取当前用户信息
	 * @author denggq
	 * @date 2012-2-6
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		String baozlx=this.getParam("baozlx");
		if(null==baozlx){
			baozlx="";
		}
		setResult("baozlx", baozlx);
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String query(@Param KuwdjBaoz bean){
		try {
			setResult("result", kuwdjBaozService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 保存
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<KuwdjBaoz> insert,@Param("edit") ArrayList<KuwdjBaoz> edit , @Param("delete") ArrayList<KuwdjBaoz> delete){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(kuwdjBaozService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 保存
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String saves(@Param Kuwdj kuwdj,@Param("djbz_insert") ArrayList<KuwdjBaoz> insert,@Param("djbz_edit") ArrayList<KuwdjBaoz> edit , @Param("djbz_delete") ArrayList<KuwdjBaoz> delete){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(kuwdjBaozService.saves(kuwdj,insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String get(@Param KuwdjBaoz bean){
		try {
			setResult("result", kuwdjBaozService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String list(@Param KuwdjBaoz bean) {
		try {
			setResult("result", kuwdjBaozService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-17
	 * @return String
	 */
	public String remove(@Param KuwdjBaoz bean){
		try {
			setResult("result", kuwdjBaozService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author wangyu
	 * @date 2012-11-20
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param KuwdjBaoz bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//生产线
			List<KuwdjBaoz> kuwdjbzData = kuwdjBaozService.list(bean);
			dataSurce.put("kuwdjbzData", kuwdjbzData);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("kuwdjbz.ftl", dataSurce, response, "库位等级与包装", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位等级与包装", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位等级与包装", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	
}
