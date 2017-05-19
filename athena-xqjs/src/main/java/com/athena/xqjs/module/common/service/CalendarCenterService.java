package com.athena.xqjs.module.common.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:中心日历类
 * </p>
 * <p>
 * Description:中心日历类
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
 * @date 2012-1-4
 */

@Component
public class CalendarCenterService extends BaseService {

	/**
	 * 查询CalendarCenter返回实体获取到周序
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3 
	 * @参数说明：String usercenter, 用户中心，String riq 日期,String nianzq
	 */
	public CalendarCenter queryCalendarCenterObject(Map map) {
		CommonFun.mapPrint(map, "中心日历实体查询参数map");
		CalendarCenter bean = new  CalendarCenter();
		CommonFun.logger.debug("查询CalendarCenter返回实体queryCalendarCenterObject的sql语句为：common.queryCalendarCenterObject");
		bean = (CalendarCenter) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarCenterObject", map);
		return bean ;
	}

	/**
	 * 查询CalendarCenterList
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3 
	 * @参数说明：String usercenter, 用户中心，String zhoux 周序,String
	 *       nianzq，年周期
	 */
	public List<CalendarCenter> queryCalendarCenterNianzx(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryCalendarCenterObjectNianzx", map);
	}
	
	public List<CalendarCenter> queryCalendarCenter(Map map) {
		CommonFun.mapPrint(map, "CalendarCenter列list查询queryCalendarCenter方法参数map");
		CommonFun.logger.debug("CalendarCenter列list查询queryCalendarCenter方法的sql语句为：common.queryCalendarCenterObject");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryCalendarCenterObject", map);
	}
	
	public List<CalendarCenter> queryRiq(Map<String,String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryKdRq", map);
	}

	/**
	 * 查询CalendarCenter返回实体集合获取到周序
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3 
	 * @参数说明：String usercenter, 用户中心，String nianzx 年周期
	 */
	public List<CalendarCenter> queryCalendarCenterList(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryCalendarCenterObject", map);
	}

	/**
	 * 获取到周期内的周数
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：Map map
	 */
	public List<CalendarCenter> getZhoushu(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryZhoushu", map);
	}
	
	/**
	 * 获取到周期内的周数s
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-4-27
	 * @参数说明：Map map
	 */	
	public List<CalendarCenter> getZhoushus(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryZhoushus", map);
	}
	/**
	 * 获取到周期内的工作日不为0的周的数量
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-4-27
	 * @参数说明：Map map
	 */	
	public List<CalendarCenter> getWorkZhoushus(Map map) {
		CommonFun.mapPrint(map, "获取到周期内的工作日不为0的周的数量getWorkZhoushus方法参数map");
		CommonFun.logger.debug("获取到周期内的工作日不为0的周的数量getWorkZhoushus方法的sql语句为：common.queryWorkZhoushus");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryWorkZhoushus", map);
	}

	/**
	 * 获取到具体日期对应的周序
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzq 年周期,String riq
	 */
	public CalendarCenter getZhouxu(Map map) {
		CalendarCenter bean = new CalendarCenter() ;
		bean =(CalendarCenter) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarCenterObject", map);
		return bean ;
	}
	
	/**
	 * 获取到周内的天数
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzq 年周期,周序（kd）
	 */
	public List<CalendarCenter> getTianshu(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTianshu", map);
	}
	/**
	 * 填充日订单日期
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzq 年周期,周序（kd）
	 */
	public List<CalendarCenter> getRiq(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryRddRiq", map);
	}

	/**
	 * 更具用户中心，年周序得到年周期
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzx 年周序
	 */
	public String getNianzq(String usercenter, String nianzx) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzx", nianzx);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarCenterZhouqi", map).toString();
	}

	/**
	 * 更具用户中心，年周期得到周期内周数
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 用户中心，String nianzx 年周期
	 */
	public int getZhouShu(String usercenter, String nianzq) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzq", nianzq);
		return Integer.parseInt(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarCenterZhouShu", map).toString());
	}

	public String getNianZhouXu(String usercenter, String nianzq, String zhouxu) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("nianzq", nianzq);
		map.put("zhoux", zhouxu);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarCenterNianZhouXu", map).toString();
	}

	/**
	 * 计算周起始结束时间
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-2-1
	 * @param参数：年周期，周序
	 */
	public List<CalendarCenter> getStartandEndDay(Map map) {
		CommonFun.mapPrint(map, "查询周期开始和结束日期getStartandEndDay方法参数map");
		CommonFun.logger.debug("查询周期开始和结束日期getStartandEndDay方法的sql语句为：common.queryCalendarCenterObject");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryCalendarCenterObject", map);
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryMaxNianzx",map).toString();
	}
	/**
	 * 取得全月最大日
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-2-1
	 * @param参数：用户中心，月
	 */
	public String getMaxRi(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryMaxRiq",map).toString();
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryMinRiq",map).toString();
	}
	/**
	 * 查找全年最大的周期或者周序
	 * **/
	public CalendarCenter maxTime(Map<String,String> map){
		CommonFun.mapPrint(map, "查找全年最大的周期或者周序参数map");
		CalendarCenter bean = new CalendarCenter() ;
		CommonFun.logger.debug("查找全年最大的周期或者周序sql语句为：common.maxTime");
		List<CalendarCenter>  all = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.maxTime", map) ;
		if(null != all && all.size() > 0)
			bean = all.get(0);
		return bean ;
	}
	
	/**
	 * 累加年周序或者年周期，解决跨年问题
	 * **/
	public String addNianzqOrNianzx(String zhoux,String max,int index){
		DecimalFormat data = new DecimalFormat("00");
		String front = zhoux.substring(0, 4) ;
		String back = zhoux.substring(4, zhoux.length()) ;
		if(Integer.parseInt(zhoux)+index>Integer.parseInt(front+max)){
			front = ( Integer.parseInt(front)+1)+"" ;
			back = data.format((Integer.parseInt(back)+index-Integer.parseInt(max))) ;
		}else{
			back = data.format(Integer.parseInt(back)+index) ;
		}
		while(Integer.parseInt(back)>Integer.parseInt(max)){
			front = ( Integer.parseInt(front)+1)+"" ;
			back = data.format(Integer.parseInt(back)-Integer.parseInt(max)) ;
		}
		return front+back ;
	}
	
	/**
	 * 生成年周期集合
	 * **/
	public Map<Integer,String> nianzqGroup(String zhouqi,int index,String usercenter){
		Map<String,String> map= new HashMap<String, String>() ;
		map.put("usercenter", usercenter) ;
		map.put("nianzq", zhouqi.substring(0, 4)) ;
		String maxNianzq = this.maxTime(map).getNianzq().substring(4) ;
		Map<Integer,String> map_zhouq = new TreeMap<Integer, String>() ;
		for (int i = 0; i <= index; i++) {
			map_zhouq.put(i, this.addNianzqOrNianzx(zhouqi, maxNianzq, i)) ;
		}
		return map_zhouq ;
	}
	
	/**
	 * 生成年周期集合
	 * **/
	public Map<Integer,String> nianzqGroup(String zhouqi,String maxNianzq,int index){
		Map<Integer,String> map_zhouq = new TreeMap<Integer, String>() ;
		for (int i = 0; i <= index; i++) {
			map_zhouq.put(i, this.addNianzqOrNianzx(zhouqi, maxNianzq, i)) ;
		}
		return map_zhouq ;
	}
	
	
	
	/**
	 * 生成年周序列集合
	 * **/
	public Map<Integer,String> nianzxGroup(String zhouxu,int index,String usercenter){
		Map<String,String> map= new HashMap<String, String>() ;
		map.put("usercenter", usercenter) ;
		map.put("nianzx", zhouxu.substring(0, 4)) ;
		String maxNianzx = this.getMaxNianzx(map) ;
		Map<Integer,String> map_zhoux = new TreeMap<Integer, String>() ;
		for (int i = 0; i <= index; i++) {
			map_zhoux.put(i, this.addNianzqOrNianzx(zhouxu, maxNianzx, i)) ;
		}
		return map_zhoux ;
	}
	
	/**
	 * 生成年周序列集合
	 * **/
	public Map<Integer,String> nianzxGroup(String zhouxu,String maxNianzx,int index)
	{
		Map<Integer,String> map_zhoux = new TreeMap<Integer, String>() ;
		for (int i = 0; i <= index; i++) {
			map_zhoux.put(i, this.addNianzqOrNianzx(zhouxu, maxNianzx, i)) ;
		}
		return map_zhoux ;
	}
	
	/**
	 * 生成日期集合
	 * **/
	public Map<Integer,String> workDayGroup(String riq,int index,String usercenter){
		Map<String,String> map= new HashMap<String, String>() ;
		map.put("usercenter", usercenter) ;
		map.put("shifgzr", Const.GZR_Y) ;
		map.put("riq", riq) ;
		List<CalendarCenter> all = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryWorkDay", map);
		Map<Integer,String> map_riq = new TreeMap<Integer, String>() ;
		if(all.isEmpty()){
			map_riq.put(0,riq) ;
		}else{
			int size = all.size() ;
			if(index>size){
				index = size ;
			}
			for (int i = 0; i <= index; i++) {
				map_riq.put(i,all.get(i).getRiq()) ;
			}
		}
		return map_riq ;
	}
	/**
	 * 得到资源获取日期区间内所有周一
	 * 陈骏
	 * **/
	public List<String> queryZhouyi(String kais,String jies,String xingq){
		Map<String,String> map= new HashMap<String, String>() ;
		map.put("kais", kais);
		map.put("jies", jies);
		map.put("xingq", xingq);
		List<String> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryKDziyRiq", map);
		 return list;
	}

	/**
	 * 查询出某个年周序之后N个工作日不为零的年周序
	 * 
	 * @param nianzx
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getWorkWeeks(String nianzx, String usercenter, String n) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("nianzx", nianzx);
		map.put("usercenter", usercenter);
		map.put("n", n);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWorkWeeks", map);
	}

	/**
	 * 查询出某个日期之后N个工作日
	 * 
	 * @param riq
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getWorkDays(String riq, String usercenter, String n) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("riq", riq);
		map.put("usercenter", usercenter);
		map.put("n", n);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWorkDays", map);
	}
	/**
	 * 日期减法
	 * 
	 * @param riq，recno
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSubRq(String riq, String usercenter, String recno) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("riq", riq);
		map.put("usercenter", usercenter);
		map.put("recno", recno);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.subRq", map);
	}

	///wuyichao 2014-05-17
	public List<String> getZhuanHRq(String usercenter, String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("riq", ziyhqrq);
		map.put("usercenter", usercenter);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.getZhuanhrqByCenter", map);
	}
	///wuyichao 2014-05-17
	
}