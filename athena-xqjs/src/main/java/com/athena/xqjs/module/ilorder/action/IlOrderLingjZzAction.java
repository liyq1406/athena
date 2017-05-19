package com.athena.xqjs.module.ilorder.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.athena.authority.entity.LoginUser;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：IlOrderLingjgzAction
 * <p>
 * 类描述： Il订单零件终止
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-2-15
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class IlOrderLingjZzAction extends BaseWtcAction {

	@Inject
	//IL件订单零件Service
	private DingdljService dingdljservice;
	@Inject
	private UserOperLog userOperLog;
	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		// String currentDate = DateUtil.getCurrentDate();
		// this.setRequestAttribute("currentDate", currentDate);
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}
	
	/**
	 * @：根据查询条件查询已生效订单零件
	 * @：李智
	 * @：2012-2-15
	 */
	public String queryDingdljByParam(@Param Dingdlj bean) {
		Map<String,String> map = getParams();
		//订单类型
		map.put("dingdlx", Const.DINGD_LX_IL);
		map.put("dingdzt1", Const.DINGD_STATUS_YSX);
		map.put("dingdzt2", Const.DINGD_STATUS_ZXZ);
		map = dingdljservice.getParamsByBean(map,bean);
		
		//获取订单零件列表
		setResult("result", this.dingdljservice.queryDingdljByParam(bean, map));
		return AJAX;
	}
	
	/**
	 * 终止订单零件
	 * @param bean 选中的订单零件
	 * @return String
	 * @date 2012-2-27
	 */
	@SuppressWarnings("unchecked")
	public String dingdljZz(@Param("checkedDingdlj") ArrayList<Dingdlj> beans) {
		//终止要货令
		// List<Yaohl> gzYaohls = dingdljservice.dingdljZz(beans,getUserInfo());

		Map<String, List<Dingdlj>> csmap = threeCenterList(beans);
		// 分批次调用WTC
		StringBuilder result = new StringBuilder();
		Set<Entry<String, List<Dingdlj>>> setDd = csmap.entrySet();
		for (Entry<String, List<Dingdlj>> entry : setDd) {
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", entry.getValue());
			WtcResponse wtcResponse = callWtc(entry.getKey(), "Q0201", map);
			if (wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
				Map<String, Object> wtcParameter = (Map<String, Object>) wtcResponse.get("parameter");
				List<Map<String, Object>> ls = (List<Map<String, Object>>) wtcParameter.get("list");
				// Q0201 / Q0202
					for (int i = 0; i < ls.size(); i++) {
						Map<String, Object> yaohl = ls.get(i); // 拼接返回消息
						if (!yaohl.get("shul").equals(-1)) {
							result.append("用户中心:").append(yaohl.get("usercenter")).append(",订单号:").append(yaohl.get("dingdh")).append(",零件号:").append(yaohl.get("lingjbh")).append("已终止零件数量")
									.append(yaohl.get("shul")).append(";");
						} else {
							result.append("用户中心:").append(yaohl.get("usercenter")).append(",订单号:").append(yaohl.get("dingdh")).append(",零件号:").append(yaohl.get("lingjbh")).append("可终止零件数量为0;");
						}
					}
				}
		}
		setResult("result",result.toString().isEmpty()?"WTC调用失败":result.toString());
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单零件终止", "终止订单零件成功") ;
		return AJAX;
	}

	private Map<String, List<Dingdlj>> threeCenterList(List<Dingdlj> all) {
		List<Dingdlj> uwls = new ArrayList<Dingdlj>();
		List<Dingdlj> ulls = new ArrayList<Dingdlj>();
		List<Dingdlj> uxls = new ArrayList<Dingdlj>();
		// 拆分成三个用户中心
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).getUsercenter().equalsIgnoreCase(Const.WTC_CENTER_UW)) {
				uwls.add(all.get(i));
			} else if (all.get(i).getUsercenter().equalsIgnoreCase(Const.WTC_CENTER_UL)) {
				ulls.add(all.get(i));
			} else {
				uxls.add(all.get(i));
			}
		}

		Map<String, List<Dingdlj>> csmap = new LinkedHashMap<String, List<Dingdlj>>();
		csmap.put(Const.WTC_CENTER_UW, uwls);
		csmap.put(Const.WTC_CENTER_UL, ulls);
		csmap.put(Const.WTC_CENTER_UX, uxls);
		return csmap;
	}
}
