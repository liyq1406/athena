package com.athena.xqjs.module.maoxq.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.maoxq.CompareQr;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareSendService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component
public class MaoxqCompareSendAction extends ActionSupport {
	
	private final Log log = LogFactory.getLog(MaoxqCompareSendAction.class);
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private  MaoxqCompareSendService maoxqCompareSendService;
	
	// 获取用户信息
	public LoginUser getUserInfo() {

		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		return "success";
	}
	
	/**
	 * 页面初始化，执行跳转 
	 * 0007182: 增加按需毛需求查询界面  按需 毛需求主页面初始化 
	 */
	public String executeAn_x() {
		setResult("usercenter",getUserInfo().getUsercenter());
		return "success";
	}
	


	//查询毛需求比较结果信息
	public String queryCompareQr(@Param CompareQr bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求比较结果查询", "毛需求查询开始");
		Map<String, String> map = getParams();
		setResult( "result", maoxqCompareSendService.select(bean) );
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求比较结果查询", "毛需求查询结束");
		return AJAX;
	}
	 

	/**
	 * 毛需求比较结果信息删除
	 *
	 **/
	public String removeCompareQr(@Param CompareQr bean) { 
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(maoxqCompareSendService.doDelete(bean),"i18n.ckx.xuqjs.i18n_maoxqcxbj");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "毛需求比较结果信息", "数据删除");  
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "毛需求比较结果信息", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		} 
		setResult("result",message);
		return AJAX;
	}



}
