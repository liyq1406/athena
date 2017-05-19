/**
 * 
 */
package com.athena.xqjs.module.common.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarGroup;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @author dsimedd001
 *
 */
@Component
public class CommonCalendarService  extends BaseService {
	
	@Inject
	private CalendarGroupService calendarGroupService;
	
	public Map<String,String> getCalendarGroupMap(){
		List<CalendarGroup> cglist = calendarGroupService.queryCalendarGroup();
		Map<String,String> cgmap = new HashMap<String,String>();
		for(CalendarGroup bean:cglist){
			String cgusercenter = bean.getUsercenter();
			String cgAppobj = bean.getAppobj();
			String rilbc = bean.getRilbc();
			String key = cgusercenter +":"+cgAppobj;
			cgmap.put(key, rilbc);
		}
		return cgmap;
	}

	/*
	 * 用于看板计算-每天工作小时数
	 */
	public Map<String, BigDecimal> getWorkTimeMap() {
		List<CalendarGroup> cglist = calendarGroupService.queryCalendarGroup();
		Map<String, BigDecimal> cgmap = new HashMap<String, BigDecimal>();
		for (CalendarGroup bean : cglist) {
			String cgusercenter = bean.getUsercenter();
			String cgAppobj = bean.getAppobj();
			String key = cgusercenter + ":" + cgAppobj;
			BigDecimal worktime = BigDecimal.valueOf(24);
			if (!StringUtils.isEmpty(bean.getBeiz())) {
				worktime = new BigDecimal(bean.getBeiz());
			}
			cgmap.put(key, worktime);
		}
		return cgmap;
	}
	/**
	 * @param usercenter
	 * @param dinhck
	 * @param nowStartofDate
	 * @param nowEndofDate
	 * @param lingjbh
	 * @return
	 */
	public int getGongZts(String usercenter,String rilbc,
			String startofDate, String endofDate) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("rilbc", rilbc) ;
		map.put("startTime", startofDate) ;
		map.put("endTime", endofDate) ;
		map.put("shifgzr", "1") ;
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.countCalendarGongzts", map);
		return count;
	}
	/**
	 * @param usercenter
	 * @param dinhck
	 * @param nowStartofDate
	 * @param nowEndofDate
	 * @param lingjbh
	 * @return
	 */
	public List<Map<String,Object>> getAllGongZts(String startofDate, String endofDate) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("startTime", startofDate) ;
		map.put("endTime", endofDate) ;
		map.put("shifgzr", "1") ;
		map.put("banc", Const.LAXBANCI);
		List<Map<String,Object>> list = (List)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.countAllCalendarGongzts", map);
		return list;
	}

	public List<Map<String, Object>> getAllBancGongZts(String startofDate, String endofDate) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startofDate);
		map.put("endTime", endofDate);
		map.put("shifgzr", "1");
		List<Map<String, Object>> list = (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.countAllCalendarGongzts", map);
		return list;
	}
}
