package com.athena.xqjs.module.maoxq.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.maoxq.JLMaoxq;
import com.athena.xqjs.module.maoxq.service.JLMaoxqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component
public class JlMaoxqAction extends ActionSupport {

	@Inject
	private JLMaoxqService jlMaoxqService;
	
	@Inject
	private UserOperLog userOperLog;

	// 获取用户信息
	public LoginUser getUserInfo() {

		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		setResult("usercenter",getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * 0012649: 增加JL毛需求查询界面  JL 毛需求查询
	 */
	public String queryJLMaoxq(@Param JLMaoxq bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "JL毛需求查询", "JL毛需求查询开始");
		try {
			setResult("result", jlMaoxqService.selectJLMxq(getParams(), bean));
		} catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			setResult("success", false);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "JL毛需求查询", "JL毛需求查询结束");
		return AJAX;
	}
}
