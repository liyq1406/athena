package com.athena.xqjs.module.kdorder.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.GonghmsMaoxqService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：KdOrderJisAction
 * <p>
 * 类描述： Kd订单计算
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-3-2
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component
public class KdOrderJisAction extends ActionSupport {
	@Inject
	//毛需求service
	private MaoxqService maoxqService;
	
	@Inject
	//订单service
	private DingdService dingdService;
	@Inject
	//供货模式-毛需求service
	private GonghmsMaoxqService gonghmsMaoxqService;
	@Inject
	private UserOperLog userOperLog;
	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	/**
	 * 设置用户中心查询条件
	 * @param params 查询条件
	 * @return 带用户中心的查询条件
	 */
	public Map<String,String> setUsercenter(Map<String,String> params) {
		params.put("usercenter", this.getUserInfo().getUsercenter());
		return params;
	}
	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单计算", "进入页面") ;
		return "success";
	}
	
	/**
	 * @：查询毛需求列表
	 * @：李智
	 * @：2012-3-2
	 */
	public String queryListKdMaoxq(@Param Maoxq bean) {
		Map<String,String> map = getParams();
		//设置查询条件
		String zhizlx = map.get("zhizlxForXuqly");
		//1为一个制造路线为空的特指值
		map.put("zhizlx", zhizlx);
		//map.put("zhizlx2", "97X");
		//获取毛需求列表
		setResult("result", this.maoxqService.queryMaoxqByXqlx(bean, map));
		return AJAX;
	}
	/**
	 * 初始化需求来源
	 * @：李智
	 * @：2012-3-2
	 */
	public String executeMaoxqXqly() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单计算", "获取需求来源开始") ;
		Map<String,String> map = setUsercenter(getParams());
		String zhizlx = map.get("zhizlxForXuqly");
		//1为一个制造路线为空的特指值
		if(!zhizlx.equals("1")) {
			map.put("zhizlx", map.get("zhizlxForXuqly"));
		}
		map.put("gonghms", Const.PP);
		setResult("result", this.gonghmsMaoxqService.queryGonghmsMaoxq(map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单计算", "获取需求来源结束") ;
		return AJAX;
	}
	
	/**
	 * 在session中查询选中的毛需求
	 * @：李智
	 * @：2012-3-2
	 */
	public String querySessionMaoxq() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单计算", "session中查询选中的毛需求开始") ;
		Map<String,String> map = getParams();
		map.put("usercenter", getUserInfo().getUsercenter());
		map.put("zidlx", Const.ZIDLX_XQLY);
		if(map.get("xuqbc") != null) {
			Maoxq maoxq = this.maoxqService.queryOneMaoxq(map);
			
			List<Maoxq> list = new ArrayList<Maoxq>();
			
			//如果之前有放过
			if(getSessionAttribute("chooseMaoxq") != null) {
				list = (List)getSessionAttribute("chooseMaoxq");
			}
			if(maoxq != null) {
				if("1".equals(map.get("isDel"))) {
					list.remove(maoxq);
				} else {
					for(int i=0; i < list.size(); i++) {
						Maoxq oneMaoxq = list.get(i);
						if(oneMaoxq.getXuqly().equals(maoxq.getXuqly())) {
							setErrorMessage("相同需求来源的需求只能存在一条！");
							return AJAX;
						}
					}
					list.add(maoxq);
				}
			}
			//放入session
			setSessionAttribute("chooseMaoxq", list);
			setResult("result", CommonFun.listToMap(list));
		}
		//当没版次的时候表示订单发生变化,清空session
		else {
			//放入session
			setSessionAttribute("chooseMaoxq", null);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单计算", "session中查询选中的毛需求结束") ;
		return AJAX;
	}
	
	/**
	 * 初始化获取订单号
	 * @：李智
	 * @：2012-3-2
	 */
	public String executeDingdh() {
		Map<String,String> map = getParams();
		map.put("dingdlx1", Const.DINGD_LX_TS);
		map.put("dingdlx2", Const.DINGD_LX_KD);
		map.put("dingdlx3", Const.DINGD_LX_AX);
		map.put("dingdzt",  Const.DINGD_STATUS_YDY);
		
		setResult("result", this.dingdService.queryDingdh(map));
		//清空选择好的毛需求session
		setSessionAttribute("chooseMaoxq", null);
		return AJAX;
	}
	/**
	 * 根据订单查到类型返回制造路线
	 * @：李智
	 * @：2012-4-12
	 * @return 
	 */
	public String getZhizlxByDingd() {
		Map<String, String> params = getParams();
		Dingd dingd = this.dingdService.queryDingdByDingdh(params);
		if (dingd.getDingdlx().equals(Const.DINGD_LX_KD) || dingd.getDingdlx().equals(Const.DINGD_LX_TS)) {
			//0(KD订单)返回97X路线
			setResult("result", Const.ZHIZAOLUXIAN_KD_PSA);
		}
		else if(dingd.getDingdlx().equals(Const.DINGD_LX_AX)) {
			//2(爱信订单)返回97D路线
			setResult("result", Const.ZHIZAOLUXIAN_KD_AIXIN);
		}
		else {
			//返回空字符串
			setResult("result", "");
		}
		return AJAX;
	}
}
