package com.athena.ckx.module.transTime.action;



import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxYunssk;
import com.athena.ckx.module.transTime.service.CkxYunsskService;
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
 * 运输时刻
 * @author hj
 * @date 2012-03-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxYunsskAction extends ActionSupport{
	@Inject
	private CkxYunsskService ckxYunsskService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices downloadServices = null;
	/**
	 * 获取当前用户信息
	 * @author hj
	 * @Date 2012-4-07
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转页面（运输时刻）
	 * @param bean
	 * @return
	 */
	public String execute() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "select";
	}
	
	/**
	 * 分页查询（运输时刻）
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxYunssk bean){
//		ckxYunsskService.insertTimeOut();
		if(StringUtils.isNotBlank(bean.getGongzr())){
			bean.setGongzr(bean.getGongzr().replace("-", ""));
		}
		setResult("result", ckxYunsskService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时刻", "数据查询结束");
		return AJAX;
	}
	
	

	public String download(@Param CkxYunssk bean) {

		// 数据源
		Map<String, Object> dataSource = null;
		Map<String, String> map = getParams();
		bean.setPageNo(1);
		bean.setPageSize(500000);
		if(StringUtils.isNotBlank(bean.getGongzr())){
			bean.setGongzr(bean.getGongzr().replace("-", ""));
		}
		dataSource =  ckxYunsskService.select(bean);
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName ="yunssj.ftl";

		downloadServices.downloadFile(fileName, dataSource, response, "yunssk", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}

	/**
	 * 数据操作 运输时间（实际）
	 * 修改
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param CkxYunssk bean,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//ckxYunsskService.insertTimeOut();
			bean.setCreator(getLoginUser().getUsername());
			bean.setEditor(getLoginUser().getUsername());
			Message m=new Message(ckxYunsskService.save(bean,operant),"i18n.ckx.paicfj.i18n_shengcx");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时刻", "数据修改结束");
			message.put("message", m.getMessage());
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时刻", "数据修改结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 数据操作 运输时间（实际）
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String remove(@Param CkxYunssk bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(ckxYunsskService.remove(bean),"i18n.ckx.paicfj.i18n_shengcx");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时刻", "数据删除结束");
			message.put("message", m.getMessage());
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时刻", "数据删除结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 运输时刻添加修改时下拉框查询运势时刻模板集合
	 * @return
	 */
}
