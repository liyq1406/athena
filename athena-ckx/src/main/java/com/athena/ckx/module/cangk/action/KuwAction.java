package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.module.cangk.service.KuwService;
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
 * 库位
 * @author denggq
 * @date 2012-2-8
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KuwAction extends ActionSupport{
	
	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入KuwService
	 * @author denggq
	 * @date 2012-2-8
	 * @return bean
	 */
	@Inject
	private KuwService kuwService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	/**
	 * 获得登录人信息
	 * @author denggq
	 * @date 2012-4-10
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-2-8
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
	 * @date 2012-2-8
	 * @return String
	 */
	public String query(@Param Kuw bean){
		try {
			setResult("result", kuwService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 多行增加
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author denggq
	 * @date 2012-2-8
	 * @return String
	 */
	public String insertKuw(@Param("kuws_insert") ArrayList<Kuw> inserts,@Param Kuw kuw ){
		Map<String,String> message = new HashMap<String,String>();
		try {
			message.put("message", kuwService.inserts(inserts, kuw,getLoginUser().getUsername()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据增加");
		}catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据增加", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-2-8
	 * @return String
	 */
	public String get(@Param Kuw bean){
		try {
			setResult("result", kuwService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String list(@Param Kuw bean) {
		try {
			setResult("result", kuwService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 修改查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String kuwlist(@Param Kuw bean) {
		try {
			setResult("result", kuwService.kuwlist(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 修改查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String kuwupdate(@Param("kuws_edit") ArrayList<Kuw> inserts,@Param Kuw bean) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message",  kuwService.kuwupdate(inserts, bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "多数据查询");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map);
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String remove(@Param("delete") ArrayList<Kuw> inserts,@Param Kuw bean){
		try {
			setResult("result", kuwService.doDelete(inserts,bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 修改
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String updatekw(@Param("edit") ArrayList<Kuw> inserts, @Param Kuw kuw ){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", kuwService.update(inserts, kuw,getLoginUser().getUsername()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据修改");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据修改", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 修改库位状态为解冻
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String kwztjied(@Param("edit") ArrayList<Kuw> inserts, @Param Kuw kuw ){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", kuwService.updatekuwztjied(inserts, kuw));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据修改");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据修改", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	/**
	 * 修改库位状态为冻结
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String kuwztdongj(@Param("edit") ArrayList<Kuw> inserts, @Param Kuw kuw ){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", kuwService.updatekuwztdongj(inserts, kuw));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据修改");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据修改", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 库位导出
	 * @param bean
	 * @author wangyu
	 * @date 2012-8-3
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param Kuw bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//库位
			List<Kuw> kuwData = kuwService.list(bean);
			dataSurce.put("kuwData", kuwData);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("kuw.ftl", dataSurce, response, "库位", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库位", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库位", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
}
