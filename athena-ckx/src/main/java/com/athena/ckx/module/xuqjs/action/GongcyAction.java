package com.athena.ckx.module.xuqjs.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.module.xuqjs.service.GongcyService;
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
 * 供应商-承运商-运输商
 * @author denggq
 * @Date 2012-3-22
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class GongcyAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private GongcyService gongcyService;
	
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	/**
	 * 获取用户信息
	 * @author denggq
	 * @Date 2012-3-22
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author denggq
	 * @Date 2012-3-22
	 * @return String
	 */
	public String execute(){
		//获取到用户中心
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		setResult("gcbh", getParam("guanjz1"));
		return "select";
	}
	
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @Date 2012-3-22
	 * @param bean
	 * @return 
	 */
	public String query(@Param Gongcy bean){
		try{
			setResult("result", gongcyService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "数据查询");
		}catch(Exception e){
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-3-22
	 * @param bean
	 * @return 
	 */
	public String get(@Param Gongcy bean){
		try{
			setResult("result", gongcyService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "单数据查询");
		}catch(Exception e){
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-3-22
	 * @param bean
	 * @return 
	 */
	public String list(@Param Gongcy bean){
		try{
			setResult("result", gongcyService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "多数据查询");
		}catch(Exception e){
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	
	/**
	 * 保存方法
	 * @author denggq
	 * @Date 2012-3-22
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param Gongcy bean,@Param("operant") Integer operant){
		Map<String,String> message=new HashMap<String, String>();
		try {
			Message m=new Message(gongcyService.save(bean, operant, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "数据保存");
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 删除方法（逻辑删除）
	 * @author denggq
	 * @Date 2012-3-22
	 * @param bean
	 * @return
	 */
	public String remove(@Param Gongcy bean){
		Map<String,String> message=new HashMap<String, String>();
		try {
			Message m = new Message(gongcyService.removes(bean, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商-承运商-运输商", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 根据供应商编号获取发运地代码,用于外部物流添加发运地
	 * @param gongysbh
	 * @return
	 */
	public String getFaydByGongys(@Param("gongysbh") String gongysbh){
		Gongcy bean =new Gongcy();
		bean.setGcbh(gongysbh);
		bean.setBiaos("1");
		bean.setUsercenter(getLoginUser().getUsercenter());
		//根据路径编号获取第一个物理点对象
		setResult("result", gongcyService.get(bean));
		return AJAX;
	}
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	public String download(@Param Gongcy bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Gongcy> rows = gongcyService.listByImport(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("gongys.ftl", dataSurce, response, "供应商-承运商", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商-承运商", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商-承运商", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
}
