package com.athena.xqjs.module.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.module.zygzbj.service.ZygzbjjsParam;
import com.toft.core3.container.annotation.Component;

@Component
public class MaoxqReportParam extends BaseService  {
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(ZygzbjjsParam.class);
	
	/**
	 * 日历版次MAP
	 */
	public static Map<String,String> rilbcMap;
	
	/**
	 * 零件MAP
	 */
	public static Map<String,Lingj> lingjMap;
	
	/**
	 * 零件供应商MAP
	 */
	public static Map<String,List<LingjGongys>> lingjgysMap;
	
	/**
	 * 日历版次明细信息
	 */
	public static Map<String,CalendarCenter> calendarVersionMap;
	
	/**
	 * 中心日历明细信息
	 */
	public static Map<String,CalendarCenter> calendarCenterMap;
	
	/**
	 * 加载毛需求报表参数
	 */
	public void initParam(Map map){
		logger.info("初始化毛需求报表参数开始");
		long start = System.currentTimeMillis();
		rilbcMap = new HashMap<String, String>();
		lingjMap = new HashMap<String, Lingj>();
		lingjgysMap = new HashMap<String, List<LingjGongys>>();
		calendarVersionMap = new HashMap<String, CalendarCenter>();
		calendarCenterMap = new HashMap<String, CalendarCenter>();
		
		/**
		 * 汇总日历版次
		 */
		List<Map<String,String>> allRilbcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllRilbc",map);
		for (int i = 0; i < allRilbcList.size(); i++) {
			Map<String,String> rilbc = allRilbcList.get(i);
			String key = rilbc.get("USERCENTER") + rilbc.get("APPOBJ");
			rilbcMap.put(key, rilbc.get("RILBC"));
		}
		allRilbcList = null;
		
		/**
		 * 零件集合
		 */
		List<Lingj> lingjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selLj",map);
		for (int i = 0; i < lingjList.size(); i++) {
			Lingj lingj = lingjList.get(i);
			String key = lingj.getUsercenter() + lingj.getLingjbh();
			lingjMap.put(key, lingj);
		}
		lingjList = null;
		
		/**
		 * 零件供应商集合
		 */
		List<LingjGongys> lingjgysList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryLingjGysList",map);
		for (int i = 0; i < lingjgysList.size(); i++) {
			LingjGongys lingjgys = lingjgysList.get(i);
			String key = lingjgys.getUsercenter() + lingjgys.getLingjbh();
			//已经存在,添加
			if(lingjgysMap.containsKey(key)){
				lingjgysMap.get(key).add(lingjgys);
			//不存在,新建
			}else{
				List<LingjGongys> lingjgyss = new ArrayList<LingjGongys>();
				lingjgyss.add(lingjgys);
				lingjgysMap.put(key, lingjgyss);
			}
		}
		lingjgysList = null;
		
		/**
		 * 日历版次明细信息集合
		 */
		List<CalendarCenter> calendarVersionList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryNianzxBanc",map);
		for (int i = 0; i < calendarVersionList.size(); i++) {
			CalendarCenter calendarCenter = calendarVersionList.get(i);
			String key = calendarCenter.getUsercenter() + calendarCenter.getBanc() + calendarCenter.getRiq();
			calendarVersionMap.put(key, calendarCenter);
		}
		calendarVersionList = null;
		
		/**
		 * 日历版次明细信息集合
		 */
		List<CalendarCenter> calendarCenterList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryNianzx",map);
		for (int i = 0; i < calendarCenterList.size(); i++) {
			CalendarCenter calendarCenter = calendarCenterList.get(i);
			String key = calendarCenter.getUsercenter() + calendarCenter.getRiq();
			calendarCenterMap.put(key, calendarCenter);
		}
		calendarCenterList = null;
		
		logger.info("加载毛需求报表参数结束,耗时---------------------"+(System.currentTimeMillis() - start));
	}
	
	/**
	 * 销毁
	 */
	public void destory(){
		rilbcMap = null;
		lingjMap = null;
		lingjgysMap = null;
		calendarVersionMap = null;
		calendarCenterMap = null;
		logger.info("毛需求报表参数销毁");
	}

}
