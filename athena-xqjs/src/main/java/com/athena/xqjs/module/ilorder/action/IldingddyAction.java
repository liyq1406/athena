package com.athena.xqjs.module.ilorder.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.JidygzqService;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：IldingddyAction
 * <p>
 * 类描述： Il订单定义
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-4-17
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class IldingddyAction extends ActionSupport {
	
	/**
	 * 注入DingdService
	 */
	@Inject
	private DingdService dingdService;

	/**
	 * 注入零件供应商Service
	 */
	@Inject
	private LingjGongysService lingjGongysService;
	
	/**
	 * 供应商Service
	 */
	@Inject
	private GongysService gongysService;
	
	/**
	 * 既定预告周期Service
	 */
	@Inject
	private JidygzqService jdygzqService;
	
	static Logger logger = Logger.getLogger(IldingddyAction.class); 
	
	@Inject
	private UserOperLog log;
	
	/**
	 * 订单定义页面初始化
	 * @return
	 */
	public String initDingddy(){
		String time =  CommonFun.getJavaTime("yyyy-MM-dd HH:mm:ss");
		setResult("dingdjssj", time);
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("editor", loginUser.getUsername());
		setResult("editorHidden", loginUser.getUsername());
		setResult("usercenter", loginUser.getUsercenter());
		//setResult("creator",loginUser.getUsername());
		this.setRequestAttribute("creator", loginUser.getUsername());
		//setResult("create_time",time);
		this.setRequestAttribute("create_time", time);
		return "ildingddy";
	}	
	
	/**
	 * 查询订单列表
	 * @param bean
	 * @return
	 */
	public String queryDingd(@Param Dingd bean){
		Map<String,String> map = getParams();
		map.put("dingdlx", Const.DINGDLX_JL);
		map.put("_isJL", "Y");
		setResult("result", dingdService.kdQueryList(bean, map));
		return AJAX;
	}
	
	/**
	 * 添加订单
	 * @param bean
	 * @return
	 */
	public String addDingd(@Param Dingd bean,@Param("operant") String operant){
		String msg = "";
		String loginfo = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put("gongysdm", bean.getGongysdm());
			map.put("gcbh", bean.getGongysdm());
			map.put("usercenter", bean.getUsercenter());
			map.put("leix", "2");
			//map.put("gonghlx", "UGB");
			Gongys gongys = gongysService.queryGongys(map);
			if(gongys != null){
					//新增
					if(operant.equals("1")){
							bean.setActive("1") ;
							boolean flag = dingdService.doInsert(bean);
							if (flag) {
								msg = "";
							} else {
								msg = "卷料订单定义保存失败,订单号已经存在";
						}
					//更新
					}else if(operant.equals("2")){
							boolean flag = dingdService.doUpdate(bean); 
							if (flag) {
								msg = "";
							} else {
								msg = Const.UPDATE_FAIL;
							}
					}
						loginfo = "卷料订单定义"+operant+"成功";
			}else{
				loginfo = "供应商不存在,请重新输入";
				msg = "供应商不存在,请重新输入";
			}
			logger.info(loginfo);
			log.addCorrect(CommonUtil.MODULE_XQJS,"卷料订单定义", loginfo);
		} catch (Exception e) {
			msg = "卷料订单定义异常"+e.getMessage();
			loginfo = "卷料订单定义异常"+CommonUtil.replaceBlank(e.toString());
			logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "卷料订单定义", "卷料订单定义异常", CommonUtil.getClassMethod(), loginfo);
		}
		if(!msg.equals("")){
			setErrorMessage(msg);
		}
		return AJAX;
	}
	
	/**
	 * 删除订单
	 * @param bean
	 * @return
	 */
	public String removeDingd(@Param Dingd bean){
		String msg = "";
		String loginfo = "";
		try {
			boolean bl = dingdService.doRemove(bean);
			if(bl){
				msg = Const.DELETE_SUCSS;
			}else{
				msg = Const.DELETE_FAIL;
			}
			loginfo = "卷料订单定义删除成功";
			log.addCorrect(CommonUtil.MODULE_XQJS,"卷料订单定义", loginfo);
			logger.info(loginfo);
		} catch (Exception e) {
			msg = "卷料订单定义删除异常"+e.getMessage();
			log.addError(CommonUtil.MODULE_XQJS, "卷料订单定义", "卷料订单定义删除异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.toString()));
			logger.error(msg);
			setErrorMessage(msg);
		}
		return AJAX;
	}
	
	/**
	 * 带出订单内容
	 * @return
	 */
	public String queryDingdNr(){
		setResult("result", jdygzqService.queryDingdnr("UGB", getParam("jiszq").substring(4)));
		return AJAX;
	}
}
