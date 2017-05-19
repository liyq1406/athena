package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.module.xuqjs.service.ShengcxService;
import com.athena.ckx.util.DateTimeUtil;
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
 * 生产线
 * @author denggq
 * @date 2012-1-30
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShengcxAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private ShengcxService shengcxService;
	
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
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
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String execute(){
		setResult("zbczxc", getzbcZxc());
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String query(@Param Shengcx bean){
		try {
			setResult("result", shengcxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String get(@Param Shengcx bean){
		try {
			setResult("result", shengcxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String list(@Param Shengcx bean) {
		try {
			setResult("result", shengcxService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String remove(@Param Shengcx bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			setResult("result", shengcxService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * @description 保存生产线和分配区
	 * @param bean
	 * @param Fenpqs
	 * @return
	 * @author denggq
	 * @date 2012-1-30
	 */
	public String saveFenpqs(@Param Shengcx bean,@Param("operant") Integer operant,@Param("fenpqs_insert") ArrayList<Fenpq> insert,@Param("fenpqs_edit") ArrayList<Fenpq> edit , @Param("fenpqs_delete") ArrayList<Fenpq> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(shengcxService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		
		return AJAX;
	}
	
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-8-3
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param Shengcx bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//生产线
			List<Shengcx> shengcxData = shengcxService.list(bean);
			dataSurce.put("shengcxData", shengcxData);
			//分配区
			List<Shengcx> fenpqData = shengcxService.listFenpq(bean);
			dataSurce.put("fenpqData", fenpqData);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("shengcx.ftl", dataSurce, response, "生产线-分配区", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线-分配区", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线-分配区", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 判断当前登录是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	public boolean getzbcZxc(){
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		boolean flag = false;
		if("ZBC".equals(zbczxc)){
			flag = true;
		}
		return flag;
	}
}
