package com.athena.xqjs.module.kdorder.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.common.service.GonghmsMaoxqService;
import com.athena.xqjs.module.kdorder.service.DingdljcxService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component
public class DingdljcxAction extends ActionSupport {
	@Inject
	//订单service
	private DingdljcxService dingdljcxService;
	
	
	@Inject
	//供货模式-毛需求service
	private GonghmsMaoxqService gonghmsMaoxqService;
	@Inject
	private UserOperLog userOperLog;

	
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "零件订单查询", "进入页面") ;
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}
	
	public String queryListDingdlj(@Param Dingdlj bean) {
		Map<String,String> message = new HashMap<String,String>();
		try{
			setResult("result", dingdljcxService.queryAllKDDingdlj(bean, getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "数据查询");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件订单查询", "查询订单结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		return AJAX;
	}
}
