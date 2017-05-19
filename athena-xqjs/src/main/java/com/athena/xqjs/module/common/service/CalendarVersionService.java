package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarVersion;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:日历版次类
 * </p>
 * <p>
 * Description:操作日历版次类
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

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class CalendarVersionService extends BaseService{
	
	
	/**
	 * 查询全部信息，返回list集合
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	public List<CalendarVersion> queryAllCalendarVersion() {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersion") ;
		
	}
	
	/**
	 * 查询年周期
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	public CalendarVersion queryCalendarVersionNianzq(String riq,String usercenter,String banc) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		CalendarVersion bean = new CalendarVersion() ;
		map.put("riq", riq.substring(0, 10)) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		//map.put("shifgzr", Const.GZR_Y) ;
		//map.put("shifjfr", Const.JIAOFR_Y) ;
		bean = (CalendarVersion) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryCalendarVersionNianzq", map) ;
		map.clear() ;
		return  bean ;
	}
	/**
	 * 查询日期集合
	 * **/
	public List<CalendarVersion > queryCalendarVersionRiq(String nianzq,String usercenter,String banc) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("nianzq", nianzq.substring(0, 10)) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionRiq", map) ;
	}
	
	/**
	 * 根据多条件查询，主要用来查找向后查找日期
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数说明：开始日期，截止日期，用户中心，版次，是否交付日
	 */
	public List<CalendarVersion> queryCalendarVersionList(String startTime,String endTime,String usercenter,String banc,String shifjfr) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("zhoux", startTime) ;
		map.put("biaos", endTime) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		map.put("shifjfr", shifjfr) ;
		CommonFun.mapPrint(map, "向后查找日期queryCalendarVersionList方法参数map");
		CommonFun.logger.debug("向后查找日期queryCalendarVersionList方法的sql语句为：calendarVersion.queryCalendarVersionByRi");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionByRi", map) ;
	}
	
	
	
	
	/**
	 * 查询两个交付日之间的工作日
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数说明：开始日期，截止日期，用户中心，版次，是否交付日
	 */
	
	public List<CalendarVersion> queryCalendarVersionListGzr(String startTime,String endTime,String usercenter,String banc,String shifgzr) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("zhoux", startTime) ;
		map.put("biaos", endTime) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		map.put("shifgzr", shifgzr) ;
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionByGzr", map) ;
	}
	
	
	
	
	/**
	 * 根据多条件查询,用来查找工业周期第一天
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数：用户中心，版次，年周期
	 */
	public List<CalendarVersion> queryCalendarVersionByNianzq(String usercenter,String banc,String nianzq) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("nianzq", nianzq) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionByNianzq", map) ;
	}
	
	
	
	
	/**
	 * 查找工作日，针对第一个交付日
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数：用户中心，版次，年周期，日期，是否工作日标记1是，0不是
	 */
	public List<CalendarVersion> queryCalendarVersionGzr(String usercenter,String banc,String nianzq,String riq,String shifgzr) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("nianzq", nianzq) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		map.put("riq", riq) ;
		map.put("shifgzr", shifgzr) ;
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionGzrByMin", map) ;
	}
	
	/**
	 * 查找否存在该版次的工作日历
	 * 
	 * @author 聂士元
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数：用户中心，版次
	 */
	public List<CalendarVersion> queryCalendarVersion(String usercenter, String banc) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usercenter", usercenter);
		map.put("banc", banc);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.countGzr", map);
	}
	
	
	/**
	 * 查找工作日,针对最后一个交付日
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数：用户中心，版次，年周期，日期，是否工作日标记1是，0不是
	 */
	public List<CalendarVersion> queryCalendarVersionGzrByMax(String usercenter,String banc,String nianzq,String riq,String shifgzr) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("nianzq", nianzq) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		map.put("riq", riq) ;
		map.put("shifgzr", shifgzr) ;
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionGzrByMax", map) ;
	}
	
	
	/**
	 * 计算工作日天数
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-21
	 * @param参数：用户中心，版次，年周期，是否工作日标记1是，0不是
	 */
	public List<CalendarVersion> queryCountGzr(String usercenter,String banc,String nianzq,String shifgzr) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("nianzq", nianzq) ;
		map.put("usercenter", usercenter) ;
		map.put("banc", banc) ;
		map.put("shifgzr", shifgzr) ;
		CommonFun.mapPrint(map, "计算工作日天数queryCountGzr方法参数map");
		CommonFun.logger.debug("计算工作日天数queryCountGzr方法的sql语句为：calendarVersion.countGzr");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.countGzr", map) ;
	}
	
    public Integer countDay(String usercenter,String cangkdm,String p0zhouqxh){
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("usercenter", usercenter);
    	map.put("cangkdm", cangkdm);
    	map.put("p0zhouqxh", p0zhouqxh);
    	CommonFun.mapPrint(map, "查询计算周期内工作天数countDay方法参数map");
    	CommonFun.logger.debug("查询计算周期内工作天数countDay方法sql语句为：calendarVersion.countDay");
    	return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.countDay",map).toString());
    }
    public Integer countLastDay(String usercenter,String cangkdm,String p0zhouqxh,String ziyhqrq){
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("usercenter", usercenter);
    	map.put("cangkdm", cangkdm);
    	map.put("p0zhouqxh", p0zhouqxh);
    	map.put("ziyhqrq", ziyhqrq);
    	CommonFun.mapPrint(map, "查询计算周期内剩余工作天数countLastDay方法参数map");
    	CommonFun.logger.debug("查询计算周期内剩余工作天数countDay方法sql语句为：calendarVersion.countLastDay");
    	return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.countLastDay",map).toString());
    }
    public Integer countWeekDay(String usercenter,String cangkdm,String s0zxh){
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("usercenter", usercenter);
    	map.put("cangkdm", cangkdm);
    	map.put("s0zxh", s0zxh);
    	return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.countWeekDay",map).toString());
    }
    //////////////////wuyichao ////////////////
    public List<CalendarVersion> pscountWeekDay()
    {
    	return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.pscountWeekDay");
    }
    
    public List<CalendarVersion> pscountLastWeekDay(String ziyhqrq){
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("ziyhqrq", ziyhqrq);
    	return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.pscountLastWeekDay",map);
    }
    
    //////////////////wuyichao ////////////////
    public Integer countLastWeekDay(String usercenter,String cangkdm,String s0zxh,String ziyhqrq){
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("usercenter", usercenter);
    	map.put("cangkdm", cangkdm);
    	map.put("s0zxh", s0zxh);
    	map.put("ziyhqrq", ziyhqrq);
    	return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.countLastWeekDay",map).toString());
    }
	public List<String> getZhuanHRq(String usercenter,String ziyhqrq,String chanx){
		Map<String,String> map = new HashMap<String,String>();
    	map.put("usercenter", usercenter);
    	map.put("ziyhqrq", ziyhqrq);
    	map.put("chanx", chanx);
    	return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.getZhuanhrq", map);
	}
	/**
	 * 查找全年最大的周期或者周序
	 * **/
	public CalendarVersion maxTime(Map<String,String> map){
		CommonFun.mapPrint(map, "查找全年最大的周期或者周序参数map");
		CalendarVersion bean = new CalendarVersion() ;
		CommonFun.logger.debug("查找全年最大的周期或者周序sql语句为：calendarVersion.maxTime");
		List<CalendarVersion>  all = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.maxTime", map) ;
		if(all.size()>0){
			bean = all.get(0);
		}else{
			bean = null;
		}
		
		return bean ;
	}
	/**
	 * 更具用户中心，年周期得到周期内周数
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzx 年周期
	 */
	public int getZhouShu(String usercenter, String nianzq,String rilbc,String riq) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzq", nianzq);
		map.put("banc", rilbc);
		map.put("riq", riq);
		return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryCalendarversionZhouShu", map).toString());
	}
///////////////wuyichao///////////////////
	public List<CalendarVersion> getZhouShu(String usercenter, String rilbc) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("banc", rilbc);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarversionZhouShuList", map);
	}
/////////////wuyichao///////////////////   
    
	
	public String getNianZhouXu(String usercenter, String nianzq, String zhouxu,String rilbc) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzq", nianzq);
		map.put("zhoux", zhouxu);
		map.put("banc", rilbc);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryCalendarversionNianZhouXu", map).toString();
	}
	
	
	public List<CalendarVersion> getWorkZhoushus(Map map) {
		CommonFun.mapPrint(map, "获取到周期内的工作日不为0的周的数量getWorkZhoushus方法参数map");
		CommonFun.logger.debug("获取到周期内的工作日不为0的周的数量getWorkZhoushus方法的sql语句为：calendarVersion.queryBefWorkZhoushus");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryWorkZhoushus", map);
	}
	public List<CalendarVersion> getBefWorkZhoushus(Map map) {
		CommonFun.mapPrint(map, "获取到周期内的工作日不为0的周的数量queryBefWorkZhoushus方法参数map");
		CommonFun.logger.debug("获取到周期内的工作日不为0的周的数量queryBefWorkZhoushus方法的sql语句为：calendarVersion.queryBefWorkZhoushus");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryBefWorkZhoushus", map);
	}
	
	
	
	
	
	/**
	 * 取得全月最小日期
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-2-1
	 * @param参数：用户中心，年周期序号
	 */
	public String getMinRi(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryMinRiq",map).toString();
	}
	public CalendarVersion queryCalendarVersionObject(Map map) {
		CommonFun.mapPrint(map, "中心日历实体查询参数map");
		CommonFun.logger.debug("查询CalendarVersion返回实体queryCalendarVersion的sql语句为：calendarVersion.queryCalendarCenterObject");
		CalendarVersion bean = (CalendarVersion) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryCalendarVersionObject", map);
		return bean ;
	}
	/**
	 * 计算周起始结束时间
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-2-1
	 * @param参数：年周期，周序
	 */
	public List<CalendarVersion> getStartandEndDay(Map map) {
		CommonFun.mapPrint(map, "查询周期开始和结束日期getStartandEndDay方法参数map");
		CommonFun.logger.debug("查询周期开始和结束日期getStartandEndDay方法的sql语句为：calendarVersion.queryCalendarVersionObject");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionObject", map);
	}
	
	
	/**
	 * 获取到周内的天数
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzq 年周期,周序（kd）
	 */
	public List<CalendarVersion> getTianshu(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryTianshu", map);
	}
	/**
	 * 取得全年最大周序
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-2-1
	 * @param参数：用户中心，年
	 */
	public String getMaxNianzx(Map map) {
		CommonFun.mapPrint(map, "取得全年最大周序getMaxNianzx方法参数map");
		CommonFun.logger.debug("取得全年最大周序getMaxNianzx方法的sql语句为：common.queryMaxNianzx");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryMaxNianzx",map).toString();
	}
	
	
	/**
	 * 更具用户中心，年周期得到周期内工作日的天数
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @date 2012-12-13
	 * @参数说明：String usercenter, 用户中心，String nianzx 年周期
	 */
	public int getGongZRsByZhouqi(String usercenter, String nianzq,String rilbc,String riq) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzq", nianzq);
		map.put("banc", rilbc);
		map.put("riq", riq);
		return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryGongZRByNianZq", map).toString());
	}
	
	/**
	 * 更具用户中心，年周期得到周期内工作日的天数
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @date 2012-12-13
	 * @参数说明：String usercenter, 用户中心，String nianzx 年周期
	 */
	public int getGongZRsByZhou(String usercenter, String nianzq,String rilbc,String zhoux,String riq) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzq", nianzq);
		map.put("banc", rilbc);
		map.put("zhoux", zhoux);
		map.put("riq", riq);
		return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryGongZRByZhoux", map).toString());
	}
	
	
	
	/**
	 * @see 吴易超新建,查找指定日历版次中参数日期后所有的工作日，按日期排序
	 * @param usercenter
	 * @param banc
	 * @param nianzq
	 * @param riq
	 * @param shifgzr
	 * @return
	 */
	public List<CalendarVersion> queryCalendarVersionGzrByMax(String banc,String riq) {
		if(StringUtils.isBlank(riq) || StringUtils.isBlank(banc))
		return null;
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("banc", banc) ;
		map.put("riq", riq) ;
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarVersionGzrByBancAndRiq", map) ;
	}
	
	
	
}