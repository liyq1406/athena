package com.athena.xqjs.module.anxorder.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.anxorder.AnxHandSet;
import com.athena.xqjs.module.anxorder.service.AnxHandSetService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component
public class AnxHandSetAction extends ActionSupport {

	@Inject
	private AnxHandSetService anxHandSetService;
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 日志
	 */
	public static final Logger logger = Logger.getLogger(AnxHandSetAction.class);
	
	//获取用户中心
	public LoginUser getUserInfo()
	{
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * 初始化页面方法
	 * @see init page
	 * @return
	 */
	public String initAnxHandset()
	{
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}
	
	
	/**
	 * 按需手动下发流水查询
	 * @author 贺志国
	 * @date 2015-7-8
	 * @param bean
	 * @return
	 */
	public String queryAnxHandSet(@Param AnxHandSet bean){
		setResult("result", anxHandSetService.select(bean));
		return AJAX;
	}
	
	/**
	 * 按需手动下发保存，更新按需订单状态
	 * @author 贺志国
	 * @date 2015-7-8
	 * @param bean
	 * @return
	 */
	public String saveAnxHandset(@Param AnxHandSet bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			bean.setCreator(getUserInfo().getUsername());
			bean.setCreate_time(DateUtil.curDateTime());
			//如果手动下发状态为Y，则更新xqjs_dingd表订单状态为生效4
			if("Y".equals(bean.getZhuangt())){
				Map<String,String> param = new HashMap<String,String>();
				param.put("usercenter", bean.getUsercenter());
				param.put("creator", "UW".equals(bean.getUsercenter())?"4101":"4100");
				param.put("dingdlx", "'5','6'");
				param.put("create_time", DateUtil.curDateTime());
				anxHandSetService.updateDindzt(param);
			}
			Message m=new Message(anxHandSetService.saveHandSet(bean),getUserInfo().getUsercenter()+"按需手动下发设置，状态"+bean.getZhuangt());
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需手动下发设置", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_XQJS, "按需手动下发设置", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
}
