package com.athena.pc.module.action;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.pc.entity.BasicAdjust;
import com.athena.pc.entity.OrderAdjust;
import com.athena.pc.entity.WBDDYGAdjust;
import com.athena.pc.module.service.AdjustService;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-7-30
 * @Time: 下午02:39:16
 * @version 1.0
 * @Description :时间调整
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class AdjustAction extends ActionSupport {
	@SuppressWarnings("rawtypes")
	@Inject
	private AdjustService adjustService = null;

	/* description: 2个工作日 */
	private final String GZR = "4";
	/* description: 获取用户信息 */
	LoginUser loginUser = AuthorityUtils.getSecurityUser();

	/* description: 订单 */
	@SuppressWarnings("unused")
	private final String LX_DD = "PP";
	/* description: 要货令接口 */
	private final String LX_YHL = "YHL";
	/* description: 外部订单鱼告 */
	private final String LX_WBDDYG = "WBDDYG";

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午02:54:25
	 * @version 1.0
	 * @return String
	 * @Description: 得到工作日
	 */
	public String selectDate() {
		// 得到今天的日期
		String today = DateUtil.getCurrentDate();
		// 用户中心
		String UC = loginUser.getUsercenter();
		// 参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("DATE", today);
		// 工作日
		params.put("GZR", GZR);
		params.put("UC", UC);
		// 查询GZR 的工作日 是几号
		@SuppressWarnings("unchecked")
		List<HashMap<String, String>> map = adjustService.selectDate(params);
		// 得到工作日
		String date = map.size() == 0 ? null : map.get(0).get("RIQ");
		// 返回
		this.setResult("DATE", date);
		// 调整类型
		this.setResult("TZLX", this.getParam("LX"));

		return "select".concat(this.getParam("LX"));
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午04:50:32
	 * @version 1.0
	 * @return String
	 * @Description: 返回类型
	 */
	public String selectLx() {

		String tzlx = this.getParam("TZLX");
		List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
		// 如果是订单
		if (tzlx.equalsIgnoreCase("DD")) {

			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("name", "PP/NP订单");
			map1.put("value", "PP");
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("name", "PJ/NJ订单");
			map2.put("value", "PJ");
			datas.add(map1);
			datas.add(map2);

		} else {
			// 接口
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("name", "GEVP要货令");
			map1.put("value", "YHL");
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("name", "外部订单预告");
			map2.put("value", "WBDDYG");
			datas.add(map1);
			datas.add(map2);
		}

		setResult("result", datas);

		return AJAX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午03:14:08
	 * @version 1.0
	 * @param adjusj
	 *            基础类
	 * @return String
	 * @Description: 查询结果集
	 */
	@SuppressWarnings("unchecked")
	public String selectObject(@Param BasicAdjust adjusj) {

		Map<String, String> params = this.getParmasData();

		Map<String, Object> map = new HashMap<String, Object>();
		if(params.get("DATE")==null){
			
			map.put("message", "没有工作日历!");
			setResult("result", map);

			return AJAX;
		}
		// 如果是要货令接口
		if (LX_YHL.equalsIgnoreCase(params.get("BS"))) {
			map = adjustService.selectYHL(params, adjusj);
		}
		// 如果是外部订单鱼告接口
		else if (LX_WBDDYG.equalsIgnoreCase(params.get("BS"))) {
			map = adjustService.selectWBDDYG(params, adjusj);
		}
		// 如果是订单
		else {
			if("PP".equalsIgnoreCase(params.get("BS"))){
				params.put("BSN","NP");
			}else{
				params.put("BSN","NJ");
			}
			map = adjustService.selectOrder(params, adjusj);
		}

		setResult("result", map);

		return AJAX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午03:25:59
	 * @version 1.0
	 * @param obj
	 * @return String
	 * @Description: 调整时间
	 */
	public String updateObject(@Param("edit") ArrayList<OrderAdjust> listOrder,
			@Param("edit") ArrayList<WBDDYGAdjust> listWbddyg) {

		String bs = this.getParam("LX");
		Map<String,String> msgMap = new HashMap<String,String>();
		//影响行数
		int  i = 0;
		try {
			// 如果是要货令接口
			if (LX_YHL.equalsIgnoreCase(bs)) {

				for (WBDDYGAdjust obj : listWbddyg) {
					i = adjustService.updateYHL(obj);
				}
			}
			// 如果是外部订单鱼告接口
			else if (LX_WBDDYG.equalsIgnoreCase(bs)) {
				for (WBDDYGAdjust obj : listWbddyg) {
					i =adjustService.updateWBDDYG(obj);
				}
			}
			// 如果是订单调整
			else {
				for (OrderAdjust obj : listOrder) {
					i =adjustService.updateOrder(obj);
				}
			}
		} catch (Exception e) {
			msgMap.put("message", "调整失败!" + e.getMessage());
			setResult("result", msgMap);
			return AJAX;
		}
		msgMap.put("message", i>0?"调整成功!":"调整失败，不存在此类型的记录!");
		setResult("result", msgMap);
		return AJAX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午02:57:33
	 * @version 1.0
	 * @return Map<String, String>
	 * @Description: 得到查询条件
	 */
	public Map<String, String> getParmasData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("KSSJ", this.getParam("KSSJ"));
		params.put("JSSJ", this.getParam("JSSJ"));
		params.put("LJBH", this.getParam("LJBH"));
		params.put("BS", this.getParam("LX"));
		params.put("DATE", this.getParam("DATE"));
		params.put("UC", loginUser.getUsercenter());
		
		//产线组
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
		params.put("CXZ", jihychanxz);

		return params;
	}

}
