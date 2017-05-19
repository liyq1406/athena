package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_lingjkh;
import com.athena.ckx.entity.xuqjs.Zhizlxzh;
import com.athena.ckx.module.xuqjs.service.ZhizlxzhService;
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
 * 制造路线转换Action
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZhizlxzhAction extends ActionSupport{
	
	@Inject
	private ZhizlxzhService zhizlxzhService;
	
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-6
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	  * 主页面
	  * @author qizhongtao
	  * @Date 2012-4-17
	  * @return String
	  */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author qizhongtao
	 * @Date 2012-4-17
	 * @return String
	 * */
	public String query(@Param Zhizlxzh bean){
		
		try{
			setResult("result",zhizlxzhService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "制造路线转换", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "制造路线转换", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String get(@Param Zhizlxzh bean){
		
		try{
			setResult("result",zhizlxzhService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "制造路线转换", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "制造路线转换", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String list(@Param Zhizlxzh bean){
		
		try{
			setResult("result",zhizlxzhService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "制造路线转换", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "制造路线转换", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author qizhongtao
	 * @Date 2012-4-17
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<Zhizlxzh> insert,@Param("edit") ArrayList<Zhizlxzh> edit,@Param("delete") ArrayList<Zhizlxzh> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(zhizlxzhService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "制造路线转换", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "制造路线转换", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 *模板导出
	 * @param bean
	 * @author hj
	 * @date 2012-8-6
	 * @return String
	 */

	public String downloadMob(@Param Ckx_lingjkh bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();		
			dataSurce.put("rows", null);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("zhizlxzh.ftl", dataSurce, response, "zhizlxzh", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "zhizlxzh", "数据导出");
		
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "zhizlxzh", "数据导出失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
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
					userOperLog.addCorrect(CommonUtil.MODULE_CKX, "制造路线转换", "数据导入");
			}else{
					message.put("message", error);
					userOperLog.addError(CommonUtil.MODULE_CKX, "制造路线转换", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(error));
			}	
		}catch(Exception e){
			message.put("message", error);
			userOperLog.addError(CommonUtil.MODULE_CKX, "制造路线转换", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(error));
		}
		setRequestAttribute("uploudmessage",message.get("message")); 
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
}
