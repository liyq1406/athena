package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Yugzjb;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @Title:对预告中间表行列转换
 * @Description:对预告中间表行列转换
 * @author 李明
 * @version v1.0
 * @date 2012-01-03
 */

@Component
public class ConversionYugzjbService extends BaseService {
	@Inject
	private YugzjbService yugzjbService;// 注入预告中间表service
	@Inject
	private DingdmxService dingdmxService;// 注入订单明细service
	@Inject
	private DingdljService dingdljService;// 注入订单零件service
	@Inject
	private DingdService dingdService;// 注入订单零件service
	@Inject
	private UserOperLog userOperLog;// 注入订单零件service
	@Inject
	private CalendarCenterService calendarCenterService;// 注入中心日历service

	/**
	 * @方法：对预告中间表插入到订单明细中去(行列转换)
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 */

	@SuppressWarnings("unchecked")
	public void insertDingdmx(String parrten) {
		// 获取各模式下的数据集合
		List<Dingdmx> queryDingdmx = this.dingdmxService.queryAllByParrten(parrten);
		// 判断得到的结果集是为空
		if (!queryDingdmx.isEmpty()) {
			// 把数据插入到订单明细中去
			this.dingdmxService.insertListMx(queryDingdmx, new Dingdmx());
		}
	}

	/**
	 * @方法：对预告中间表进行行列转换
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public void conversionYugzjbRowLine(String parrten) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "预告中间表行列转换开始");
		// 存放查询条件的map
		Map<String, String> map = new HashMap<String, String>();
		// 查询得到所有中间表
		List<Yugzjb> yugzjbList = this.yugzjbService.queryYugzjbList();
		// 判断得到的集合不为空
		if (!yugzjbList.isEmpty()) {
			// 循环开始
			for (int i = 0; i < yugzjbList.size(); i++) {
				map.put("dingdh", yugzjbList.get(i).getDingdh());
				// 通过订单号查询到订单信息，获取到订单制作时间
				Dingd dingd = this.dingdService.queryDingdByDingdh(map);
				map.clear();
				// 通过中心日历获取到年周期和年周序
				map.put("usercenter", yugzjbList.get(i).getUsercenter());
				map.put("riq", yugzjbList.get(i).getYaohqsrq().toString().substring(0, 10));
				CalendarCenter calendarCenter = this.calendarCenterService.queryCalendarCenterObject(map);
				map.clear();
				// 判断对象不为空
				if (null != dingd && null != calendarCenter) {
					// 行列转换插入到订单零件表中
					List<Dingdlj> dingdljList = this.dingdljService.queryDingdljListPJS(yugzjbList.get(0).getShul(),
							yugzjbList.get(1).getShul(), yugzjbList.get(2).getShul(), yugzjbList.get(3).getShul(),
							dingd.getDingdjssj(), calendarCenter.getNianzq(), parrten);
					// 插入到订单零件表中去
					this.dingdljService.insertList(dingdljList, new Dingdlj());
				}
			}
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "预告中间表行列转换结束");
	}
}
