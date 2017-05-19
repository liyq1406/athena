package com.athena.ckx.module.paicfj.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_lingjkh;
import com.athena.ckx.module.paicfj.service.Ckx_lingjkhService;
import com.athena.ckx.util.GetMessageByKey;
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
 * 零件客户关系action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_lingjkhAction extends ActionSupport {
	@Inject
	private Ckx_lingjkhService lingjkhService;//零件客户关系service
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
	/**
	 * 获取用户信息
	 * @author hj
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	/**
	 * 跳转页面
	 * @author hj
	 * @Date 12/02/28
	 * @return String
	 */
	public String execute(){
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String query(@Param Ckx_lingjkh bean){
		try {
			setResult("result",lingjkhService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件客户关系", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件客户关系", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 保存方法
	 * @author hj
	 * @Date 12/02/28
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param Ckx_lingjkh bean,
			           @Param("operant") Integer operate){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(lingjkhService.save(bean, operate ,getLoginUser()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件客户关系", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件客户关系", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 删除方法（物理删除）
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return
	 */
	public String remove(@Param Ckx_lingjkh bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(lingjkhService.remove(bean),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件客户关系", "删除数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件客户关系", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author hj
	 * @date 2012-8-3
	 * @return String
	 */

	public String downloadMob(@Param Ckx_lingjkh bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Ckx_lingjkh> rows = lingjkhService.listAll(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjkh.ftl", dataSurce, response, "lingjkh", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "lingjkh", "数据导出");
		
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "lingjkh", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
					message.put("message", GetMessageByKey.getMessage("daorcg"));
					userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件客户关系", "数据导入");
			}else{
					message.put("message", error);
					userOperLog.addError(CommonUtil.MODULE_CKX, "零件客户关系", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(error));
			}	
		}catch(Exception e){
			message.put("message", error);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件客户关系", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(error));
		}
		setRequestAttribute("uploudmessage",message.get("message")); 	
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
}
