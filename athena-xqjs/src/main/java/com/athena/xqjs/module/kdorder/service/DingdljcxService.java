package com.athena.xqjs.module.kdorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;


@Component
public class DingdljcxService extends BaseService {
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private CalendarCenterService calendarCenterService;// -用户中心日历service
	@Inject
	private DingdljService dingdljService;// 订单零件service
	@Inject
	private DingdService dingdService;// 订单service
	@Inject
	private LingjService lingjService;// 参考系零件service
	@Inject
	private CalendarVersionService calendarversionservice;
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryAllKDDingdlj(Dingdlj bean, String exportXls) {
		//0011736 hanwu 20150909 使用表单自带的导出功能时，查询方法应该分开来写
		Map<String,Object> map = new HashMap<String, Object>();
		if("exportXls" .equals(exportXls)){			
			List<Dingdlj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.queryAllKDDingdlj", bean);
			map.put("totas", list.size());
			map.put("rows", list);
		}else{
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("kdorder.queryAllKDDingdlj", bean, bean);
		}
		return map;
		//return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kdorder.queryAllKDDingdlj", bean, bean);
	}
	
	
}