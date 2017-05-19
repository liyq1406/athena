package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_shengcx_lingj;
import com.athena.ckx.entity.xuqjs.Cmj;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.module.paicfj.service.Ckx_shengcx_lingjService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.xls.chanxlj.XlsHandlerUtilschanxlj;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
/**
 * 产线-零件action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_shengcx_lingjAction extends ActionSupport {
	@Inject
	private Ckx_shengcx_lingjService ckx_shengcx_lingjService;//产线-零件service
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
	@Inject
	private AbstractIBatisDao baseDao;
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
	public String query(@Param Ckx_shengcx_lingj bean){
		try {
			setResult("result", ckx_shengcx_lingjService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线零件", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线零件", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String save(@Param("insert") ArrayList<Ckx_shengcx_lingj> insert,
			@Param("edit") ArrayList<Ckx_shengcx_lingj> edit,
			@Param("delete") ArrayList<Ckx_shengcx_lingj> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(ckx_shengcx_lingjService.save(insert,edit,delete, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线零件", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线零件", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String downloadMob(@Param Ckx_shengcx_lingj bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Cmj> rows = ckx_shengcx_lingjService.list(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("shengcx_lingj.ftl", dataSurce, response, "shengcx_lingj", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "shengcx_lingj", "数据导出");
		
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "shengcx_lingj", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
		try {
			
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
			//获取所有的分装线
			Map<String,Fenzx> map=ckx_shengcx_lingjService.getFenzxMap();
			//验证导入数据，并插入到产线零件环表中
			String mes = XlsHandlerUtilschanxlj.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", GetMessageByKey.getMessage("daorcg"));
			}else{
				throw new ActionException(mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线-零件", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线-零件", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
}
