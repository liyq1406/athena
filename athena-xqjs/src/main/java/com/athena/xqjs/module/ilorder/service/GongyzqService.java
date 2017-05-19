package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Gongyzx;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:工业周期类
 * </p>
 * <p>
 * Description:工业周期类
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
 * @date 2011-12-27
 */
@SuppressWarnings({ "rawtypes"})
@Component
public class GongyzqService extends BaseService {

	/**
	 * 查询CalendarGroup返回实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 * 参数说明：String gongyzq, 工业周期
	*/
	public Gongyzq queryGongyzq(String gongyzq) {
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("gongyzq", gongyzq) ;
		return (Gongyzq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTime", map);
	}
	public Gongyzx queryGongyzx(String gongyzx) {
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("gongyzx", gongyzx) ;
		return (Gongyzx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTimeZx", map);
	}

	/**
	 * 查询ckx_gongyzq表返回实体
	 * 
	 * @author NIESY
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String rq, 工业周期
	 */
	public Gongyzq queryGongyzqbyRq(String rq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("rq", rq);
		return (Gongyzq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTime", map);
	}

	/**
	 * 查询ckx_gongyzq表返回实体
	 * 
	 * @author NIESY
	 * @version v1.0
	 * @date 2011-12-13 参数说明：Map
	 */
	@SuppressWarnings("unchecked")
	public List<Gongyzq> queryGongyzq(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTime", map);
	}

	/**
	 * 查询ckx_gongyzq表返回指定供应周期后包括指定供应周期后的供应周期LIST集合
	 * 
	 * @author NIESY
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String zq, 工业周期
	 */
	@SuppressWarnings("unchecked")
	public List<Gongyzq> queryGongyzqbyZQ(String zq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("apointzq", zq);
		return (List<Gongyzq>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTime", map);
	}
	

	public Gongyzq queryAllGongyzq(Map map){
		return (Gongyzq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryAllGongyzq",map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Gongyzq> queryVGongyzq(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryVTime", map);
	}
	/**
	 * @param lasttjMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Gongyzq queryLastGongyzq(Map<String, String> lasttjMap) {
		Object gyzq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryLastGongyzq", lasttjMap);
		Gongyzq  result= null;
		if(gyzq!=null){
			result = (Gongyzq)gyzq;
		}
		return result;
	}
	
	
	/**
	 * 获取工业周期的第一天日期
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-20 参数说明：用户中心，版次，年周期
	 */
	
	
	
//	public String getFristDay(String usercenter, String banc, String nianzq) {
//
//		// 获取到满足条的日历的集合
//		List<CalendarVersion> list = this.calendarVersionService.queryCalendarVersionByNianzq(usercenter, banc, nianzq);
//
//		CalendarVersion bean = new CalendarVersion();
//
//		if (list.size() > 0.001) {
//
//			// 获取到第一个实体对象
//			bean = list.get(0);
//
//		}
//
//		return bean.getRiq();
//
//	}
	
	
}