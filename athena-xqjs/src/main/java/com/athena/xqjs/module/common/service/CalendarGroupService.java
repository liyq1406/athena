package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarGroup;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:产线/仓库 -日历版次-工作时间编组类
 * </p>
 * <p>
 * Description:产线/仓库 -日历版次-工作时间编组类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2011-12-13
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class CalendarGroupService extends BaseService {

	/**
	 * 查询CalendarGroup返回实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	public String insertCalendarGroup(CalendarGroup bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.insertCalendarGroupObject", bean);
		return bean.getUsercenter();
	}

	/**
	 * 查询CalendarGroup返回实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String usercenter, 用户中心，String appobj 产线
	 */

	public CalendarGroup queryCalendarGroupObject(String usercenter, String appobj) {
		Map map = new HashMap();
		map.put("usercenter", usercenter);
		map.put("appobj", appobj);
		CalendarGroup bean = (CalendarGroup) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarGroupObject", map);
		map.clear();
		return bean;
	}
	
	public List<CalendarGroup> queryCalendarGroup() {
		List<CalendarGroup> list = (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryCalendarGroupObject");
		return list;
	}

}