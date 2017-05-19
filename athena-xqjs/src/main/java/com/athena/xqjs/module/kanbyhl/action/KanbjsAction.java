package com.athena.xqjs.module.kanbyhl.action;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.exception.ActionException;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.kanbyhl.service.KanbjsService;
import com.athena.xqjs.module.zygzbj.service.ZygzbjjsService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 
 * <p>
 * 看板循环计算action
 * </p>
 * 
 * <p>
 * 
 * @author Niesy
 *         </p>
 * 
 *         <p>
 * @date:2012-02-01 </p>
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KanbjsAction extends ActionSupport {
	@Inject
	private KanbjsService kanbjsService;
	@Inject
	private ZygzbjjsService zygzbjjsservice;
	@Inject
	private final Log log = LogFactory.getLog(KanbjsAction.class);

	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();

	}

	/**
	 * 初始化页面
	 * 
	 * @param bean
	 * @return success
	 */
	public String init() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", Const.WTC_CENTER_UW);
		map.put("jiscldm", Const.KANB_MKDM);
		Map<String, String> hMap = kanbjsService.selJscssz(map);
		String p7 = hMap.get("PARAM7")==null?"":hMap.get("PARAM7").toString();
		setResult("param6", hMap.get("PARAM6"));
		setResult("param7", p7);
		setResult("usercenter", getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * 初始化加载数据
	 * 
	 * @param bean
	 * @return ajax
	 */
	public String initJS(@Param Maoxq bean) {
		setResult("usercenter", getUserInfo().getUsercenter());
		setResult("result", kanbjsService.selectAll(bean));
		return AJAX;
	}

	/**
	 * 查询毛需求明细,页面初始化
	 */
	public String queryMX(@Param Maoxq bean) {
		setResult("xuqbc", bean.getXuqbc());
		setResult("usercenter", getUserInfo().getUsercenter());
		setResult("removeId", getParam("removeId"));
		return "mxQuery";
	}

	/**
	 * 查询毛需求明细
	 */
	public String maoxqMxQr(@Param Maoxq bean) {
		Map<String, String> map = getParams();
		if(map.get("xuqbc").equals("最新")){
			map.put("xuqbc", "anx");
			setResult("result", zygzbjjsservice.selMxqmx(bean, map));
		}else{
		setResult("result", kanbjsService.selectMx(bean, map));
		}
		return AJAX;
	}

	/**
	 * 点击确认按钮
	 */
	public String verify() {
		Map<String, String> map = getParams();
		String xuqbc = map.get("xuqbc");
		String xuqly = map.get("xuqly");
		String xhfw = map.get("xhfw");
		map.put("usercenter", Const.WTC_CENTER_UW);
		map.put("jiscldm", Const.KANB_MKDM);
		map.put("param1", "1");
		map.put("param2", map.get("isZhouqRi"));
		map.put("param3", xhfw);
		map.put("param4", this.getUserInfo().getUsername());
		map.put("param5", CommonFun.getJavaTime());
		map.put("param6", getParam("shifzdfs"));
		map.put("param7", getParam("shengxsj"));
		try {
			kanbjsService.kanbjsMap.put("xuqbc", kanbjsService.getxuq(xuqbc));
			kanbjsService.kanbjsMap.put("xuqly", kanbjsService.getxuq(xuqly));
			kanbjsService.updateJssz(map);		
		} catch (Exception e) {
			// System.out.println(e.toString());
			log.info(e.toString());
			setResult("result", "确认失败！");
			setResult("success",false );
			return AJAX;
		}
		setResult("result", "确认成功！");
		return AJAX;
	}
	
	/**
	 * <p>
	 * 测试使用
	 * </p>
	 * 
	 * @return
	 */
	public String computer() {
		String result = "计算出错！";
		try {
//			 yicbjService.clearYcbjXxByUser(Const.JISMK_kANB_CD,
			// AuthorityUtils.getSecurityUser());
			result = kanbjsService.numeration(getParam("usercenter") + getParam("num"));
		} catch (ActionException e) {
		}
		setResult("result", result);
		return AJAX;
	}

}
