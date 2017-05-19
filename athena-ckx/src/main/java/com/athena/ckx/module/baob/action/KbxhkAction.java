package com.athena.ckx.module.baob.action;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Beihlgz;
import com.athena.ckx.entity.baob.Kanbxhgm;
import com.athena.ckx.module.baob.service.kbxhkService;

import com.athena.ckx.util.DateTimeUtil;
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

/*
 * 报表编号：2.15.1.4
 */

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KbxhkAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	@Inject 
	private kbxhkService kanbxhkService;
	
	
	/**
	 * 登录人信息
	 * @author wy
	 * @date 2013-3-13
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 * @author wy
	 * @date 20130313
	 */
	public String execute(@Param Kanbxhgm bean) {
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return  "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author wy
	 * @date 2013-3-26
	 * @return String
	 */
	public String query(@Param Kanbxhgm bean){
		try {
			String pianysj ="";
			if(null==bean.getPianysj()){
				pianysj = "0";
			}else{
				pianysj = bean.getPianysj();
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
			c.set(Calendar.DATE, c.get(Calendar.DATE)-Integer.parseInt(pianysj));
			String edit_time = sdf.format(c.getTime());
			bean.setEdit_time(edit_time);
			setResult("result", kanbxhkService.query(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/***
	 * 查询产线
	 */
	public String selectChanx(@Param Kanbxhgm kbxh) {
		setResult("result", kanbxhkService.selectChanx(getParams()));
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
	public String download(){
		Map<String,String> map = getParams();
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			String pianysj ="";
			if(null==map.get("pianysj")){
				pianysj = "0";
			}else{
				pianysj = map.get("pianysj");
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
			c.set(Calendar.DATE, c.get(Calendar.DATE)-Integer.parseInt(pianysj));
			String edit_time = sdf.format(c.getTime());
			map.put("edit_time",edit_time);
			
			List rows = kanbxhkService.list(map);
			dataSurce.put("rows", rows);
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("kbxhk.ftl", dataSurce, response, "看板循环卡", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "看板循环卡", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "看板循环卡", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
}
