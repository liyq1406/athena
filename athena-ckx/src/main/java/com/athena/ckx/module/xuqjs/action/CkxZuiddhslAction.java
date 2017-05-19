package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxZuiddhsl;
import com.athena.ckx.module.xuqjs.service.CkxZuiddhslService;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.tag.XlsHandlerUtils;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;


/**
 * 最大订货数量Action
 * @author qizhongtao
 * @date 2012-4-07
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxZuiddhslAction extends ActionSupport{
	
	@Inject
	private CkxZuiddhslService zuiddhslService;
	
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
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
	  * @author qizhongtao
	  * @Date 2012-4-07
	  * @return String
	  */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author qizhongtao
	 * @Date 2012-4-07
	 * @return String
	 * */
	public String query(@Param CkxZuiddhsl bean){
		try {
			setResult("result",zuiddhslService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "最大订货数量", "数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "最大订货数量", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	
	/**
	 * 分页查询
	 * @author denggq
	 * @Date 2012-05-09
	 * @return String
	 * */
	public String get(@Param CkxZuiddhsl bean){
		try {
			setResult("result",zuiddhslService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "最大订货数量", "单数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "最大订货数量", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author denggq
	 * @Date 2012-05-09
	 * @return String
	 * */
	public String list(@Param CkxZuiddhsl bean){
		try {
			setResult("result",zuiddhslService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "最大订货数量", "多数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "最大订货数量", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author denggq
	 * @Date 2012-05-09
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<CkxZuiddhsl> insert,@Param("edit") ArrayList<CkxZuiddhsl> edit,@Param("delete") ArrayList<CkxZuiddhsl> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(zuiddhslService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "最大订货数量", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "最大订货数量", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 数据导出功能  
	 * mantis  1971
	 * @param bean 查询条件实体
	 * @return
	 */
	public String exports(@Param CkxZuiddhsl bean){
		Map<String,String> map = new HashMap<String,String>();
		Map<String, Object> dataSurce = new HashMap<String, Object>();
		try {
			dataSurce.put("rows", null);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downloadServices.downloadFile("zuiddhsl.ftl", dataSurce, response, "zuiddhsl", ExportConstants.FILE_XLS, false);
			map.put("message","数据导出成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "zuiddhsl", "数据导出");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_CKX, "zuiddhsl", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			map.put("message","数据导出失败"+e.getMessage());
		}
		
		setResult("result", map);
		return "downLoad";
	}
	
	/**
	 * 模板导入
	 * 
	 * @return
	 */
	public String upload(){
		Map<String,String> message = new HashMap<String,String>();
		String error = null;
		try{
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
			error = XlsHandlerUtils.analyzeXls(request);
			if("1".equals(error)){
					message.put("message", "导入成功");
					userOperLog.addCorrect(CommonUtil.MODULE_CKX, "最大订货数量", "数据导入");
			}else{
					message.put("message", error);
					userOperLog.addError(CommonUtil.MODULE_CKX, "最大订货数量", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(error));
			}	
		}catch(Exception e){
			message.put("message", error);
			userOperLog.addError(CommonUtil.MODULE_CKX, "最大订货数量", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(error));
		}
		setRequestAttribute("uploudmessage",message.get("message")); 	
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
}
