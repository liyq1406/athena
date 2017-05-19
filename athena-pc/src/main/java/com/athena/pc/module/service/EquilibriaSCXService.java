package com.athena.pc.module.service;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.entity.Beic;
import com.athena.pc.entity.EquilibDate;
import com.athena.pc.entity.EquilibLJ;
import com.athena.pc.entity.EquilibMessage;
import com.athena.pc.entity.EquilibScx;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;


/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-6-12
 * @Time: 下午4:12:49
 * @version 1.0
 * @Description :产线均衡 (均衡计算，共线零件交换计算，增减产计算，期初库存计算,写数据库)
 */
@Component
public class EquilibriaSCXService extends BaseService<Beic>  {
	
	/* description:   产线间容忍时间(分钟)*/
	private final static float ACCEPT_TIME = 0.15F;
	
	/* description:   最小工时*/
	private final static float MIN_TIME = 8.00F;
	/* description:   最大工时*/
	private final static float MAX_TIME = 22.00F;
	
	/* description:   日期产线*/
	private final static String MESSAGE_LEIX_DATE_SCX="2";
	/* description: 增产 */
	private final static int ZC = 1;
	/* description: 减产 */
	private final static int JC = 2;
	
	//时间格式
	protected final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/******** 取数据 *************/
	static Logger logger = Logger.getLogger(EquilibriaSCXService.class.getName());
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午4:56:18
	 * @version 1.0
	 * @param params
	 *            用户中心,日期，生产线编号
	 * @return List<EquilibScx>
	 * @Description: 得到生产线的工时
	 */
	public List<EquilibScx> selectScxGs(Map<String, String> params) {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("equilib.selectScxGsBy".concat(params.get("BS").toUpperCase()),params);
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午4:57:47
	 * @version 1.0
	 * @param params
	 *            用户中心,日期，生产线编号
	 * @return List<EquilibLJ>
	 * @Description: 得到零件数量
	 */
	public List<EquilibLJ> selectLjSL(Map<String, String> params) {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("equilib.selectLJSLBy".concat(params.get("BS").toUpperCase()),params);

	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午9:30:02
	 * @version 1.0
	 * @param params 生产线,用户中心.
	 * @return List<EquilibLJ>
	 * @Description:  查询产线下的共线零件
	 */
	public List<EquilibLJ> selectGXLJ(Map<String, String> params) {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("equilib.selectGXLJ",params);

	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 上午11:22:51
	 * @version 1.0
	 * @param params 用记中心，开始日期，结束日期
	 * @return List<Map<String,String>> 
	 * @Description:  查询这周内所有的毛需求
	 */
	public List<Map<String,String>> selectMaoXQ(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("equilib.selectMaoXQ",params);
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:36:24
	 * @version 1.0
	 * @param params 用户中心 ,开始时间，结束时间
	 * @return  List<Map<String,String>>
	 * @Description:  查询这周产线所有的工作编号
	 */
	public  List<Map<String,String>> selectGZBH(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("equilib.selectGZBH",params);
	}
	
	/******** 更新数据 *************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:45:37
	 * @version 1.0
	 * @param params 产线号，日期，用户中心
	 * @return int
	 * @Description:  更新产线工时
	 */
	public int  updateCXGS(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.updateCXGSBy".concat(params.get("BS").toUpperCase()),params);
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:48:53
	 * @version 1.0
	 * @param params 工作编号，零件数量
	 * @return int
	 * @Description:  更新产线的数量
	 */
	public int updateCXLJSL(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.updateCXLJSLBy".concat(params.get("BS").toUpperCase()),params);
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午11:08:20
	 * @version 1.0
	 * @param params 零件编号，用户中心，期初库存，标识
	 * @return int
	 * @Description:  更新零件的期初库存
	 */
	public int updateLjQckc(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.updateLjQckc",params);
	}
	/******** 插入数据 *************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:54:42
	 * @version 1.0
	 * @param params 工作编号，产线组号，零件数量
	 * @return  int
	 * @Description:  新增产线零件
	 */
	public int insertCXLJSL(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.insertCXLJSLBy".concat(params.get("BS").toUpperCase()),params);
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午11:28:48
	 * @version 1.0
	 * @param message 产线的报警消息
	 * @return int
	 * @Description:  新增产线消息报警
	 */
	public int insertCxMessage(EquilibMessage message){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.insertCxMessage",message);
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午11:28:48
	 * @version 1.0
	 * @param message 产线的报警消息
	 * @return int
	 * @Description:  新增产线消息报警
	 */
	public int insertLjQckc(Map<String, String> params ){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.insertLjQckc",params);
	}
	/******** 删除数据 *************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:55:21
	 * @version 1.0
	 * @param params 工作编号 
	 * @return int
	 * @Description:  删除产线零件数量
	 */
	public int deleteCXLJSL(Map<String, String> params){
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("equilib.deleteCXLJSLBy".concat(params.get("BS").toUpperCase()),params);
	}
	
	/******** 封装数据 *************/
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午5:01:45
	 * @version 1.0
	 * @param ljSl
	 *            零件数量
	 * @return Map<String,List<EquilibLJ>>
	 * @Description: 封装零件数量 ( key:产线编号 , value :零件对象集合)
	 */
	public Map<String, List<EquilibLJ>> wrapLjSl(List<EquilibLJ> ljSl) {

		Map<String, List<EquilibLJ>> scxLj = new HashMap<String, List<EquilibLJ>>();
		// 迭代零件数量
		for (Iterator<EquilibLJ> ljIt = ljSl.iterator(); ljIt.hasNext();) {
			// 得到零件对象
			EquilibLJ lj = ljIt.next();
			// 如果存在此产线，则将此零件挂至该产线下
			if (scxLj.containsKey(lj.getCHANXH())) {
				// 挂接零件
				scxLj.get(lj.getCHANXH()).add(lj);
			}
			else {
				// 不存在，则新增新集合存放此零件
				List<EquilibLJ> ljList = new ArrayList<EquilibLJ>();
				ljList.add(lj);
				scxLj.put(lj.getCHANXH(), ljList);
			}
		}
		return scxLj;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午10:05:15
	 * @version 1.0
	 * @param ljSl 共线零件集合
	 * @return Map<String,HashSet<String>>
	 * @Description:   封装共线零件( key:零件编号 , value :产线集合)
	 */
	public Map<String,HashSet<String>> warpGXLJ(List<EquilibLJ> ljSl){
		//共线零件MAP
		Map<String,HashSet<String>> gxljMap = new HashMap<String, HashSet<String>>();
		
		// 迭代零件数量
		for (Iterator<EquilibLJ> ljIt = ljSl.iterator(); ljIt.hasNext();) {
			// 得到零件对象
			EquilibLJ lj = ljIt.next();
			// 如果存在此产线，则将此零件挂至该产线下
			if (gxljMap.containsKey(lj.getLINGJBH())) {
				// 挂接零件
				gxljMap.get(lj.getLINGJBH()).add(lj.getCHANXH());
			}
			else {
				// 不存在，则新增新集合存放此零件
				HashSet<String> scxSet = new HashSet<String>();
				// 新增产线号
				scxSet.add(lj.getCHANXH());
				// 写入缓存区
				gxljMap.put(lj.getLINGJBH(), scxSet);
			}
		}

		return gxljMap;
		
	}
	
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 上午11:34:39
	 * @version 1.0
	 * @param maoXQ 一周的毛需求集合
	 * @return  Map<String,HashMap<String,Integer>>
	 * @Description:   封装毛需求｛key:日期,value:{key:零件编号,value:数量}｝
	 */
	public Map<String,HashMap<String,Integer>> warpMaoXQ(List<Map<String, String>> maoXQ){
		
		//毛需未MAP
		 Map<String,HashMap<String,Integer> > maoXq = new HashMap<String, HashMap<String,Integer>>() ;
		 //迭代mao需求
		 for(Iterator<Map<String, String>> maoIt =maoXQ.iterator();maoIt.hasNext() ;){
			 //需求对象
			 Map<String, String> mao = maoIt.next();
			 //如果存在此日期
			 if(maoXq.containsKey(mao.get("SHIJ"))){
				 
				 maoXq.get(mao.get("SHIJ")).put(mao.get("LINGJBH"), Integer.parseInt(mao.get("LINGJSL")));
//				 //是否存在此零件
//				 if(maoXq.get(mao.get("SHIJ")).containsKey(mao.get("LINGJBH"))){
//					 
//				 }
				 
			 }
			 //如果不存在此日期
			 else{
				 
				 //零件
				 HashMap<String, Integer> ljMao = new HashMap<String, Integer>();
				 //零售编号,零件数量
				 ljMao.put(mao.get("LINGJBH"), Integer.parseInt(mao.get("LINGJSL")));
				 //将此日期下的零件毛需求写到map中
				 maoXq.put(mao.get("SHIJ"), ljMao);
			 }
		 }
		 
		 return maoXq;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午1:48:46
	 * @version 1.0
	 * @param qckc 期初库存
	 * @return  Map<String,Integer>
	 * @Description:  封装传过来的期初库存
	 */
	public Map<String,Integer>  warpKclFormRceive(List<Map<String,String>> qckcList){
		
		 //下一天的期初库存
		 Map<String,Integer> qckc = new HashMap<String, Integer>() ;
		 
		 //迭代传过来的库存
		 for(Iterator<Map<String,String>> itMap = qckcList.iterator();itMap.hasNext();){
			 Map<String,String> ljKcxs = itMap.next();
			 qckc.put(ljKcxs.get("LINGJBH"), (int) Float.parseFloat(ljKcxs.get("QCKC"))) ;
		 }
		 return qckc;
		
	}
	
	 /**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午3:05:12
	 * @version 1.0
	 * @param qckcList 库存
	 * @return   Map<String,Integer>
	 * @Description:  封装 零件的库存上限
	 */
	public Map<String,Integer>  warpLJKCTop(List<Map<String,String>> qckcList){
		 //下一天的期初库存
		 Map<String,Integer> kcTop = new HashMap<String, Integer>() ;
		 //迭代传过来的库存
		 for(Iterator<Map<String,String>> itMap = qckcList.iterator();itMap.hasNext();){
			 Map<String,String> ljKcxs = itMap.next();
			 kcTop.put(ljKcxs.get("LINGJBH"), (int) Float.parseFloat(ljKcxs.get("ANQKCTOP"))) ;
		 }
		 return kcTop;
	 }

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午3:05:12
	 * @version 1.0
	 * @param qckcList
	 *            库存
	 * @return Map<String,Integer>
	 * @Description: 封装 零件的库存上限
	 */
	public Map<String, Integer> warpLJKCLower(List<Map<String, String>> qckcList) {
		// 下一天的期初库存
		Map<String, Integer> kcTop = new HashMap<String, Integer>();
		// 迭代传过来的库存
		for (Iterator<Map<String, String>> itMap = qckcList.iterator(); itMap
						.hasNext();) {
			Map<String, String> ljKcxs = itMap.next();
			kcTop.put(ljKcxs.get("LINGJBH"),(int) Float.parseFloat(ljKcxs.get("ANQKC")));
		}
		return kcTop;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 下午2:41:28
	 * @version 1.0
	 * @param workCalendar 工作日历
	 * @return  Set<String>
	 * @Description: 工作日 
	 */
	public Set<String> warpWorkCalendar(Map<String, List<String>> workCalendar){
		
		Set<String> calen = new HashSet<String>();
		//迭代产线的工作日历
		for(Map.Entry<String, List<String>> wordEntry:workCalendar.entrySet()){
			//新增所有工作日历
			calen.addAll(wordEntry.getValue()) ;
		}
		return new TreeSet<String>(calen); 
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:41:50
	 * @version 1.0
	 * @param gzbhList 工作编号list
	 * @return Map<String,String>
	 * @Description:  封装工作编号
	 */
	public Map<String,String> warpGZBH(List<Map<String,String>> gzbhList){
		
		//工作编号
		Map<String,String> gzbh = new HashMap<String, String>();
		//循环所有工作编号List
		for(Map<String,String> map:gzbhList){
			//设置
			gzbh.put(map.get("KEY"), map.get("GZBH")) ;
		}
		return gzbh;
	}
	
	/************均衡前数据的抽取及封装********************/

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午5:22:40
	 * @version 1.0
	 * @param paransJh
	 *            数据(基本参数,生产线,期初库存,工作日历)
	 * @Description: 周期内均衡
	 */
	@SuppressWarnings("unchecked")
	public void equilibData(Map<String, Object> paransJh) {


		// 基本参数
		Map<String, String> jbInfo = this.initJbInfo((Map<String, Object>) paransJh.get("params"));
		
		//工作日历
		Map<String, List<String>> workCalendar = (Map<String, List<String>>) paransJh.get("workCalendar") ;
		
		//取的这周的下一个工作日
		//封装工作日历
		Set<String> worKList  = this.warpWorkCalendar(workCalendar) ;
		//设置这周的下一个工作日
		this.setNextWorkDayOfWeek(jbInfo,worKList);
		
		//第一天的各零件的期初库存
		List<Map<String,String>> firstqckc = (List<Map<String,String>>)paransJh.get("qckc");
		//更新期初库存的时间为这周的开始时间
		this.updateQckcFirst(firstqckc, jbInfo) ;
		
		/****0.异常情况均衡**************/
		//如果不是日棍动，则跳过 ，当工时大于22小时
		if(!jbInfo.get("BS").equalsIgnoreCase("R")){
			this.equilibExceptionData(jbInfo, workCalendar) ;
		}
		
		/****1.均衡计算****/
		List<EquilibDate> listEquilib = this.equilibWeekData(jbInfo, workCalendar) ;
		
//		for(Map.Entry<String, List<EquilibLJ>> ee:listEquilib.get(0).getLjMap().entrySet()){
//		List<EquilibLJ> stt= ee.getValue();
//		System.out.println(stt) ;
//		}
//		for(EquilibScx scx:listEquilib.get(0).getScxs()){
//			System.out.println(scx.getGS()) ;
//			System.out.println(scx) ;
//			}
		/****2.平衡数据（工时取整，零件取整 ）****/
		this.balanceData(jbInfo, listEquilib) ;
		
		/****3.经济批量，（共线零件交换）****/
		//每天的产线间共线零件交换，减少换线
		for(Iterator<EquilibDate> listEquilibIt  =listEquilib.iterator();listEquilibIt.hasNext();  ){
			//共线交换零件
			this.balanceJJPL(listEquilibIt.next(),jbInfo.get("MINTIME")) ;
		}
		
		/**** 4.排产的增产.减产计算 ****/
		// 得到这周所零件的毛需求
		Map<String, HashMap<String, Integer>> maoData = this.warpMaoXQ(this
				.selectMaoXQ(jbInfo));
		// 封装第一天的期初库存
		Map<String, Integer> kcL = this.warpKclFormRceive(firstqckc);
		// 安全库存上限
		Map<String, Integer> top = this.warpLJKCTop(firstqckc);
		// 安全库存下限
		Map<String, Integer> lower = this.warpLJKCLower(firstqckc);

		// 增减产计算 日滚动则跳出
		incrementOrReducePlan(listEquilib, maoData, kcL, top, lower, jbInfo);
		
			
		/****5.数据库的写入****/
		
		this.writeDataToDB(listEquilib, jbInfo) ;
		          
		/*****6.计算期初库存******/
		//如果是日棍动，则直接返回
		if(jbInfo.get("BS").equalsIgnoreCase("R")){
			return ;
		}
		this.caculateQcKc(listEquilib, kcL, maoData, firstqckc,jbInfo);
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午4:56:05
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param listEquilib 工作日的排产计划
	 * @Description:  平衡数据(零件，工时,经济批量)
	 */
	public void balanceData(Map<String, String> jbInfo,List<EquilibDate> listEquilib){
		
		//迭代排产计划
		for(Iterator<EquilibDate> itDate = listEquilib.iterator();itDate.hasNext(); ){
			//排产计划对象
			EquilibDate dateEquilib = itDate.next();
			
			//零件向上取整
			this.balanceLjs(dateEquilib.getLjMap()) ;
			//工时拉平
			this.balanceGS(jbInfo, dateEquilib.getScxs()) ;
			//均衡经济批量
//			this.balanceJJPL(dateEquilib );
			//计算期初库存
			
			
			
		}
		
	}
	
	

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午6:08:25
	 * @version 1.0
	 * @param paransJh 数据
	 * @return Map<String, String>
	 * @Description:  基本参数
	 */
	public Map<String, String> initJbInfo(Map<String, Object> paransJh) {

		Map<String, String> jbInfo = new HashMap<String, String>();
		// 用户中心
		jbInfo.put("UC", paransJh.get("USERCENTER").toString());
		// 标识
		jbInfo.put("BS", paransJh.get("biaos").toString());
		// 产线组编号
		jbInfo.put("SCXZBH", paransJh.get("chanxzbh").toString());
		// 生产线号
		jbInfo.put("SCX", paransJh.get("shengcx").toString());
		// 开始时间
		jbInfo.put("KSSJ", paransJh.get("kaissjWeeK").toString());
		// 结束时间
		jbInfo.put("JSSJ", paransJh.get("jiessjWeeK").toString());
		//最小时间单位
		jbInfo.put("MINTIME", paransJh.get("MINTIME").toString());
		//句点
		jbInfo.put("PERIOD", paransJh.get("period").toString());
		//日期
		jbInfo.put("TODAY", paransJh.get("today").toString().replaceAll("-", ""));
		
		
		String timeNow = getTimeNow(TIMEFORMAT);
		//操作人，操作时间
		jbInfo.put("EDITOR", paransJh.get("jihybh").toString());
		jbInfo.put("EDIT_TIME", timeNow);
		
		//创建人，创建时间
		jbInfo.put("CREATOR", paransJh.get("jihybh").toString());
		jbInfo.put("CREATE_TIME", timeNow);
		return jbInfo;
		
	}

	/**
	 * @description 得到今天的时间
	 * @author 王冲
	 * @date 2012-07-05
	 * @param format 	日期的格式
	 * @param String	 一定格式的日期
	 */
	public String getTimeNow(String format){
		Date date = new Date(); 
		SimpleDateFormat fmat = new SimpleDateFormat(format); 
		return fmat.format(date);
	}
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 下午2:55:55
	 * @version 1.0
	 * @param jbinfo 基本参数
	 * @param workDayList 工作日
	 * @Description:  设置这周完后的下一个工作日
	 */
	public void setNextWorkDayOfWeek(Map<String, String> jbInfo,Set<String> workDayList){
		
		//这周的最后一个工作日
		String theWeekLastDay = jbInfo.get("JSSJ") ;
		//工作日
		for(String day:workDayList){
			//设置下一周的开始时间
			if(day.compareTo(theWeekLastDay)>0) {
				jbInfo.put("NEXTKSSJ", day);
				break;
			}
		}
		
		
	}
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午6:08:50
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param workCalendar 工作日历
	 * @Description:  均衡一周内每天的产线工时
	 */
	public List<EquilibDate> equilibWeekData(Map<String, String> jbInfo,Map<String, List<String>> workCalendar) {
		
		//开始日期
		Calendar ksCalendar  = Calendar.getInstance();
		
		try {
			ksCalendar.setTime(DateUtil.stringToDateYMD(jbInfo.get("KSSJ"))) ;
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		//结束日期
		Date jsDate = new Date();
		try {
			jsDate = DateUtil.stringToDateYMD(jbInfo.get("JSSJ"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		//查询共线零件
		List<EquilibLJ> gsLj = this.selectGXLJ(jbInfo) ;
		//封装共线零件
		Map<String,HashSet<String>> gxLj =  this.warpGXLJ(gsLj) ;
		
		//设置共线零件
		EquilibDate.setGxLj(gxLj) ;
		
		//如果这周内没有共线零件，则终止均衡计算
//		if(gxLj.keySet().size()==0){
//			//
//			return new ArrayList<EquilibDate>();
//		}
		
		//工作日
		List<EquilibDate> listEquilib  = new ArrayList<EquilibDate>() ;
		
		//周内循环
		while (ksCalendar.getTime().compareTo(jsDate)<=0){
			
			//设置日期
			jbInfo.put("DATE", DateUtil.dateToStringYMD(ksCalendar.getTime())) ;
			//得到这一天的产线
			List<EquilibScx> scxs = this.selectScxGs(jbInfo) ;
			
			//天内循环
			EquilibDate equilibDate =  this.equilibDayDataOfWeek(jbInfo,scxs,gxLj, workCalendar);
			
			if(equilibDate!=null){
				//保存这一天的排产计划
				listEquilib.add(equilibDate) ;
			}
			
			//
			
			//加一天
			ksCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		//返回这周的排产计划
		return listEquilib;

	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午6:09:15
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param scxs 产线
	 * @param gxLj 共线零件
	 * @param workCalendar 工作日历
	 * @Description:  均衡天内的生产线
	 */
	public EquilibDate equilibDayDataOfWeek(Map<String, String> jbInfo,List<EquilibScx> scxs,Map<String,HashSet<String>> gxLj,Map<String, List<String>> workCalendar) {
		
		// 得到这一天产线下的零件
		List<EquilibLJ> ljSl = this.selectLjSL(jbInfo);
		// 封装零件数
		Map<String, List<EquilibLJ>> ljMap = this.wrapLjSl(ljSl);

		// 过滤掉这一天内不上班的生产线
		//gswang 2012-09-05
//		scxs = this.getScxDataOfDay(jbInfo, scxs, workCalendar);	
		this.getScxDataOfDay(jbInfo, scxs, workCalendar);
		// 判断是否可均 ,(判断是否至少有两条生产线)
		if (!this.hasTwoSCX(scxs)) {

			
			// 如果没有两条生产线，则终止这天的均衡计算
			return scxs.size() == 1 ? initEquilibDate(jbInfo, scxs, ljMap)
							: null;
		}

		// 产线按工时升序排列
		// this.sortScxAsc(scxs) ;

		// 产线下的所有零件按升排序排列
		this.sortLJSLAsc(ljMap);

		// 均衡
		this.loopEquilibScxLJ(scxs, gxLj, ljMap);

		// 返回这天的排产计划
		return initEquilibDate(jbInfo, scxs, ljMap);

	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:53:47
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param scxs 产线
	 * @param ljMap 零件
	 * @return EquilibDate
	 * @Description:  这天的排产计划
	 */
	public EquilibDate initEquilibDate(Map<String, String> jbInfo,List<EquilibScx> scxs,Map<String, List<EquilibLJ>> ljMap){
		// 工作日
		EquilibDate dateEquilib = new EquilibDate();
		// 日期
		dateEquilib.setDate(jbInfo.get("DATE"));
		// 零件
		dateEquilib.setLjMap(ljMap);
		// 产线
		dateEquilib.setScxs(scxs);
		//如果有两条产线以上
		if(scxs.size()>=2){
		// 是否正常
		dateEquilib.setSfzc(scxs.get(scxs.size() - 1).getGS()
						- scxs.get(0).getGS() <= ACCEPT_TIME ? true : false);
		}
		else{
			//如果只有一条产线上班.则非常
			dateEquilib.setSfzc(false) ;
		}
		return dateEquilib;
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午6:17:33
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param scxs 生产线
	 * @param workCalendar 工作日历
	 * @return  List<EquilibScx>
	 * @Description:  得到这一天（工作日）的产线
	 */
	public List<EquilibScx>  getScxDataOfDay(Map<String, String> jbInfo,List<EquilibScx> scxs,Map<String, List<String>> workCalendar) {
		
		//迭代生产线工作日历
		for(Map.Entry<String, List<String>> scxCal:workCalendar.entrySet()){
			//迭代生产线
			for(Iterator<EquilibScx>  scxIt = scxs.iterator();scxIt.hasNext();){
				//得到生产线
				EquilibScx scx = scxIt.next();
				//如果生线相同
				if(scx.getCHANXH().equalsIgnoreCase(scxCal.getKey())){
					//如果在此产线的工作日历中没找到这个日期,则将此生线移除掉
					if(!scxCal.getValue().contains(jbInfo.get("DATE"))){
						//移除掉此生产线
						scxs.remove(scx) ;
						break;
					}
				}
			}
			
		}
		
		return scxs;
		

	}
	/************开始均衡计算啦********************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午10:24:43
	 * @version 1.0
	 * @param scxs 产线
	 * @param gxLj 共线零件
	 * @param ljMap 产线对应的零件
	 * @Description:  产线间的均衡
	 */
	public void loopEquilibScxLJ(List<EquilibScx> scxs,Map<String,HashSet<String>> gxLj,Map<String, List<EquilibLJ>> ljMap) {
		
		
		// 迭代产线（按正序）
		for (int i = 0; i < scxs.size(); i++) {
			// 迭代产线（按倒序）
			for (int j = scxs.size() - 1; j >= 0; j--) {
				// 最小工时产线
				EquilibScx minScx = scxs.get(i);
				// 最大工时产线
				EquilibScx maxScx = scxs.get(j);

				/****** 终止均衡产线条件 ********/
				// 1.自己与自己均衡时
				if (i == j) {
					break;
				}
				// 2.最小工时产线与最大工时产线,在容忍时间内
				if ((maxScx.getGS() - minScx.getGS()) <= ACCEPT_TIME) {
					return;
				}
				// 3.无共线零件时
				// 得到共线零件
				Set<String> gxljSet = this.getGXLJOfScx(minScx, maxScx,ljMap.get(maxScx.getCHANXH()), gxLj);

				// 判断是否有无共线零件
				if (!hasGXLJ(gxljSet)) {
					// 进入下一个产线均衡
					continue;
				}

				 //4.转移零件数量为0
				 if(this.devolveLJSL(maxScx, minScx)==0.00f){
					// 进入下一个产线均衡
					continue;
				 }

				/************ 均衡产线 ****************/

				// 1.得到转移零件数量
				float devolveLjS = this.devolveLJSL(maxScx, minScx);
				// 2.转移零件

				// 计算最大工时产线中，可转移共线零件的数量
				float gxljs = this.sumGXLJSL(gxljSet,
								ljMap.get(maxScx.getCHANXH()));

				// 如果可转移的零件数为0
				 if(gxljs==0.00f){
					 continue;
				 }

				/*判断共线零件的数量是否大于转移的零件数量,
				   如果可转移的共线零件数大于需要转移的零件数,则取需要转移的零件数，
				   反之，取可转移的零件数*/
				float  moveLj =(gxljs >= devolveLjS?devolveLjS:gxljs);
				//转移零件啦
				this.devolveLJ(ljMap.get(maxScx.getCHANXH()), ljMap.get(minScx.getCHANXH()), gxljSet, moveLj) ;
				
				// 3.计算工时
				float maxGs = this.caculateLJToHours(
								Float.parseFloat(maxScx.getSCXJP()), moveLj);
				float minGs = this.caculateLJToHours(
						Float.parseFloat(minScx.getSCXJP()), moveLj);

				// 最大产线减去gs
				this.caculateScxHours(maxScx, -maxGs);
				// 最小产线加上gs
				this.caculateScxHours(minScx, minGs);

				/*** 如果发生了均衡,则按产线工时排序，重新均衡 **/

				// 1.按产线工时
				this.sortScxAsc(scxs);
				// 2.产线下的所有零件按零件数量排序
				this.sortLJSLAsc(ljMap);
				// 3.重新均衡
				i = -1;
				break;

			}
		}
	}
	
	/*************数据逻辑处理**************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午10:30:33
	 * @version 1.0
	 * @param ljMap 产线-零件
	 * @Description:  将所有产线下的零件按升序排列
	 */
	public void sortLJSLAsc(Map<String, List<EquilibLJ>> ljMap){
		//将每个产线下的零件按升序排列
		for(Map.Entry<String, List<EquilibLJ>> lj:ljMap.entrySet()){
			//升序排列
			Collections.sort(lj.getValue()) ;			
		}
	}

	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午12:53:33
	 * @version 1.0
	 * @Description: 按产线工时升序排列
	 */
	public void sortScxAsc(List<EquilibScx> scxs) {
		//升序排列
		Collections.sort(scxs) ;	
	}
	
	

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午11:43:15
	 * @version 1.0
	 * @param minScx 最小工时产线
	 * @param maxScx 最大工时产线
	 * @param maxScxLj 最大工时产线零件
	 * @param gxLj  共线零件
	 * @return Set<String>
	 * @Description:  得到产线间的共线零件
	 */
	public Set<String> getGXLJOfScx(EquilibScx minScx,EquilibScx maxScx,List<EquilibLJ> maxScxLj,Map<String,HashSet<String>> gxLj) {
		// 共线零件集合
		Set<String> gxljSet = new HashSet<String>();
		// 迭代最大产线的零件类型
		for (Iterator<EquilibLJ> maxLJ = maxScxLj.iterator(); maxLJ.hasNext();) {

			// 得到零件对象
			EquilibLJ lj = maxLJ.next();
			// 判断此零件是否存在于共线零件map中,如果存在，则说明此零件是共线零件
			if (gxLj.containsKey(lj.getLINGJBH())) {
				// 得到此零件所有的共线产线
				HashSet<String> gxcx = gxLj.get(lj.getLINGJBH());
				// 判断此零件是否在两条产线中共线 ,如果共线，则返回true
				if (gxcx.contains(minScx.getCHANXH())
								&& gxcx.contains(maxScx.getCHANXH())) {
					//将零件写入此hashSet中
					gxljSet.add(lj.getLINGJBH()) ;
				}
			}
		}

		return gxljSet;
				
	}

	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午11:56:05
	 * @version 1.0
	 * @param maxScx 最大工时生产线
	 * @param minScx 最小工时生产线
	 * @return  float
	 * @Description:  计算要转移零件的数量
	 */
	public float devolveLJSL(EquilibScx maxScx, EquilibScx minScx) {

		float devLjSL = (maxScx.getGS() - minScx.getGS())
						* Integer.parseInt(maxScx.getSCXJP())
						* Integer.parseInt(minScx.getSCXJP())
						/ (Integer.parseInt(maxScx.getSCXJP()) + Integer
										.parseInt(minScx.getSCXJP()));
		return devLjSL;
	}
	


	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午12:56:17
	 * @version 1.0
	 * @param gxljSet 产线间的共线零件
	 * @param cxLj  产线零件
	 * @return int
	 * @Description:  统计共结零件的数量
	 */
	public float sumGXLJSL(Set<String> gxljSet,List<EquilibLJ> cxLj){
		//共结零的数量
		float gxsl = 0.00f;
		//迭代共线零件
		for(Iterator<String> gxlj =gxljSet.iterator();gxlj.hasNext(); ){
			//零件号
			String ljh = gxlj.next();
			//迭代产线零件
			for(Iterator<EquilibLJ> cxLjs =cxLj.iterator();cxLjs.hasNext(); ){
				//零件
				EquilibLJ cL = cxLjs.next();
				//如果产线零件号与共线零件号相同,则统计零件个数
				if(ljh.equalsIgnoreCase(cL.getLINGJBH())){
					//统计数
					gxsl+= cL.getLINGJSL();
					//终止循环了
					break;
				}
			}
		}
		return gxsl;
	}
	

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午1:07:35
	 * @version 1.0
	 * @param jp 产线节拍
	 * @param ljsl 零件数量
	 * @return float
	 * @Description:  将转移的零件换算成工时
	 */
	public float caculateLJToHours(float jp,float ljsl) {
		//工时
		float gs = ljsl/jp;
		//计算工时
//		DecimalFormat df = new DecimalFormat("#.00") ;
		//格式化工时
		float gsS = this.formatFloat(gs) ; 
//			Float.parseFloat(df.format(gs));
		return gsS;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午1:08:29
	 * @version 1.0
	 * @param scx 产线
	 * @param gs  工时
	 * @Description:  转移零件后，将转移的零件换算成工时并重新计算产线工时
	 */
	public void caculateScxHours(EquilibScx scx,float gs) {
		//重算工时
		scx.setGS(scx.getGS()+gs) ;
	}


	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午2:08:42
	 * @version 1.0
	 * @param maxLj 最大工时产线下的零件
	 * @param minLj 最小工时产线下的零件
	 * @param gxljSet 共线零件类型
	 * @param moveLJSL 数量
	 * @Description:  将最大产线上的零件移moveLJSL个数量至最小产线下
	 */
	public void devolveLJ(List<EquilibLJ> maxLj,List<EquilibLJ> minLj,Set<String> gxljSet,final float moveLJSL){
		
		// 剩余零件数
		float syLjs = moveLJSL;
		// 迭代最大零件
		for (Iterator<EquilibLJ> maxLjs = maxLj.iterator(); maxLjs
						.hasNext();) {
//		// 迭代共线零件
//		for (Iterator<String> gxljIt = gxljSet.iterator(); gxljIt.hasNext();) {
//			// 共线零件编号
//			String gxljBh = gxljIt.next();
			
				// 最大零件数
				EquilibLJ lj = maxLjs.next();
				// 如果找到共线零件编号
//				if (gxljBh == lj.getLINGJBH())
				if(gxljSet.contains(lj.getLINGJBH()))
				{

					// 转移零件数
					float moveSl = syLjs >= lj.getLINGJSL() ? lj.getLINGJSL()
									: syLjs;
					// 剩余零件数量
					syLjs = syLjs - moveSl;
					// 从最大产线中移掉此数量的零件
					this.moveLjs(lj.getLINGJBH(), -moveSl, maxLj);

					// 如果最小零件集中存在此零件型号
					if (hasLj(lj.getLINGJBH(), minLj)) {

						// 从最小产线中新增此数量的零件
						this.moveLjs(lj.getLINGJBH(), moveSl, minLj);
					}
					else {
						// 不存在,则新增
						this.addLjs(lj.getLINGJBH(), moveSl, minLj);

					}

					// 移完后，判断剩余零件数量是否为0,终止循环
					if (syLjs == 0.00f) {
						return;
					}

				}
//			}

		}
		
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午2:24:09
	 * @version 1.0
	 * @param ljBh 零件编号
	 * @param minLj 零件数据集
	 * @return boolean 
	 * @Description:   判断此零件数据集是否存在此零件(true:存在,false:不存在)
	 */
	public boolean hasLj(String ljBh,List<EquilibLJ> ljs){
		//
		boolean bool = false;
		//迭代最小零件
		for(Iterator<EquilibLJ> ljIt = ljs.iterator();ljIt.hasNext(); ){
			//最大零件数
			EquilibLJ lj = ljIt.next();
			//如果存在此零件
			if(lj.getLINGJBH().equalsIgnoreCase(ljBh)){
				return true;
			}
			
		}
		return bool;
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午2:31:01
	 * @version 1.0
	 * @param ljBh 转移的零件编号
	 * @param ljsl 转移的零件数量
	 * @param ljs 零件集
	 * @Description:   将零件转移至另一条产线中
	 */
	public void moveLjs(String ljBh,float ljsl,List<EquilibLJ> ljs){
		// 迭代最小零件
		for (Iterator<EquilibLJ> ljIt = ljs.iterator(); ljIt.hasNext();) {
			// 最大零件数
			EquilibLJ lj = ljIt.next();
			// 如果存在此零件
			if (lj.getLINGJBH().equalsIgnoreCase(ljBh)) {
				lj.setLINGJSL(lj.getLINGJSL() + ljsl);
			}

		}
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午2:31:01
	 * @version 1.0
	 * @param ljBh 转移的零件编号
	 * @param ljsl 转移的零件数量
	 * @param ljs 零件集
	 * @Description:   将零件添加至至另一条产线中
	 */
	public void addLjs(String ljBh,float ljsl,List<EquilibLJ> ljs){
		//创建一个零件对象
		EquilibLJ lj = new EquilibLJ();
		//零件编号
		lj.setLINGJBH(ljBh);
		//零件数量
		lj.setLINGJSL(ljsl);
		
		//产线下新增零件
		ljs.add(lj) ;
	}
	
	/*********** 条件分支 **************/


	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午11:40:38
	 * @version 1.0
	 * @param minScx 最小工时产线
	 * @param maxScx 最大工时产线
	 * @param maxScxLj 最大工时产线零件
	 * @param gxLj  共线零件
	 * @return  boolean
	 * @Description:  判断是否有共线零件(有:true,无:false)
	 */
	public boolean hasGXLJ(EquilibScx minScx,EquilibScx maxScx,List<EquilibLJ> maxScxLj,Map<String,HashSet<String>> gxLj) {
		//返回是否有无共线零件
		return getGXLJOfScx(minScx, maxScx, maxScxLj, gxLj).size()==0?false:true;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午11:42:42
	 * @version 1.0
	 * @param gxljSet 共线零件数
	 * @return boolean
	 * @Description:  判断是否有共线零件(有:true,无:false)
	 */
	public boolean hasGXLJ(Set<String> gxljSet) {
		//返回是否有无共线零件
		return gxljSet.size()==0?false:true;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 上午11:46:21
	 * @version 1.0
	 * @return boolean
	 * @Description: 判断是否至少有两条生产线在工作日内(有:true,无 :false)
	 */
	public boolean hasTwoSCX(List<EquilibScx> scxs) {
		return scxs.size()>=2?true:false;
	}



	/*************平衡 工时，零件，经济批量 *******************/

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午4:17:26
	 * @version 1.0
	 * @param ljMap 零件数量
	 * @Description:  零件数向上取整(取带小数的零件数量向上取整)
	 */
	public void balanceLjs(Map<String, List<EquilibLJ>> ljMap) {
		
		//迭代ljMap
		for(Map.Entry<String, List<EquilibLJ>> ljEntry:ljMap.entrySet()){
			//迭代零件集合
			for(Iterator<EquilibLJ> ljIt=ljEntry.getValue().iterator();ljIt.hasNext();){
				//零件
				EquilibLJ lj = ljIt.next();
				//向上取整
				lj.setLJSL((int)Math.ceil(lj.getLINGJSL()));
				
			}
		}
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午4:40:52
	 * @version 1.0
	 * @param mintime 最小时间单位
	 * @param gongShi 工时
	 * @return  float
	 * @Description:  工时向上取整
	 */
	public float getMinTime(float mintime,float gongShi){
		float result = 0.00f;
		if(gongShi%mintime == 0.00){
			result = gongShi;
		}else{
			result = BigDecimal.valueOf(Math.ceil(gongShi/mintime)).multiply(BigDecimal.valueOf(mintime)).floatValue();
			
		}
		return result;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午4:19:23
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param scxs 生产线
	 * @Description:  拉平工时(将带小数点的工时向上取整)
	 */
	public void balanceGS(Map<String, String> jbInfo,List<EquilibScx> scxs ) {
		
		//得到最小时间单位
		String minTime = jbInfo.get("MINTIME") ;
		//迭代工时
		for(Iterator<EquilibScx> scxIt = scxs.iterator();scxIt.hasNext();){
			//产线对象
			EquilibScx scx = scxIt.next();
			//拉平工时
			scx.setGS(this.getMinTime(Float.parseFloat(minTime), scx.getGS())) ; 
			
		}

	}
	
	/*************共线零件交换*********************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 上午10:20:34
	 * @version 1.0
	 * @param dateEquilib 一天的排产计划
	 * @param minTime 最小时间单位
	 * @Description: 平衡经济批量 
	 */
	public void balanceJJPL(EquilibDate dateEquilib,String minTime ){
		
		//得到这天所有的产线
		List<EquilibScx> scxs = dateEquilib.getScxs();
		//迭代产线 (按工时从小至大)
		for(int i=0;i<scxs.size();i++){
			
			//得到此产线下的所有零件
			List<EquilibLJ> ljList  = dateEquilib.getLjMap().get(scxs.get(i).getCHANXH()) ==null?new ArrayList<EquilibLJ>():dateEquilib.getLjMap().get(scxs.get(i).getCHANXH());
			//迭代此产线下的所有零件
			for(int k=0;k<ljList.size();k++){
				//得到零件对象
				EquilibLJ lj = ljList.get(k) ;
				
				//如果零件数量为0，则进入下一循环
				if(lj.getLJSL()==0){
					continue;
				}
				
				//如果零件数小于经济批量，且启用经济批量
				if(lj.getSFQYJJPL()==1&&lj.getLJSL()<Integer.parseInt(lj.getJJPL())){
					//如果是共线零件
					if(EquilibDate.getGxLj().containsKey(lj.getLINGJBH())){
						//共线零件交换
						this.replaceGXLJ(lj, scxs.get(i), EquilibDate.getGxLj().get(lj.getLINGJBH()),dateEquilib,minTime) ;
					}
					//如果不是,则拉平经济批量
					else{
						//拉平经济批量
						this.levelJJPL(lj) ;
						//工时重置
						this.resetGs(scxs.get(i), dateEquilib, minTime) ;
						
					}
					
				}
			}
			
			
				
		}
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午1:35:47
	 * @version 1.0
	 * @param lj 零件
	 * @Description: 拉平经济批量  
	 */
	public void levelJJPL(EquilibLJ lj){
		//
		lj.setLJSL( lj.getLJSL()+Integer.parseInt(lj.getJJPL())-(lj.getLJSL()%Integer.parseInt(lj.getJJPL()))     ) ;
		
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午1:12:13
	 * @version 1.0
	 * @param lj 零件
	 * @param gxljs 共线生产线
	 * @param equilibDate 排产计划
	 * @param minTime 最小时间单位
	 * @Description:   共线零件交换
	 */
	public void replaceGXLJ(EquilibLJ lj,EquilibScx scx,Set<String> gxscx,EquilibDate equilibDate,String minTime){
		
		//得到这一天内，与该产线共零件的产线
		Set<String> dayGx = this.getGXCX(scx, equilibDate.getScxs(), gxscx) ;
		//得到与该产线下共线的零件类型
		Map<String,HashSet<String>> gxLj = this.getGXLjLx(scx, dayGx) ;
		//过滤掉只共线一种零件的产线
		this.filterGxOneLjLx(gxLj) ;
		//如果没有共线两种零件以上的，则拉平经济批量
		if(gxLj.size()==0){
			//拉平经济批量
			this.levelJJPL(lj) ;
			//重置工时
			this.resetGs(scx, equilibDate, minTime) ;
			return ;
		}
		
		//交换零件
		boolean bool = this.replaceLj(lj, gxLj, equilibDate) ;
		//如果未完成共线零件交换，则拉平经济批量
		if(!bool){
			//拉平经济批量
			this.levelJJPL(lj) ;
			//重置工时
			this.resetGs(scx, equilibDate, minTime) ;
		}
		
	}
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午1:12:13
	 * @version 1.0
	 * @param scx 生产线
	 * @param equilibDate 排产计划
	 * @param minTime 最小时间单位
	 * @Description:   工时重置
	 */
	public  void resetGs(EquilibScx scx,EquilibDate equilibDate,String minTime){
		//零件数量
		int i=0;
		//零件集
		List<EquilibLJ> ljs = equilibDate.getLjMap().get(scx.getCHANXH()) ;
		//迭代零件集
		for(EquilibLJ lj :ljs){
			//零件数量累加
			i+=lj.getLJSL() ;
		}
		
		//设置工时
		scx.setGS(Float.parseFloat(String.valueOf(i))/Float.parseFloat(scx.getSCXJP())) ;
		//根据最小时间单位 工时向上取整
		scx.setGS(this.getMinTime(Float.parseFloat(minTime), scx.getGS())) ; 
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午3:11:58
	 * @version 1.0
	 * @param lj 零件
	 * @param gxLj 共线的产线，以及产线下共线的零件(key:产线编号, value:零件类型集合)
	 * @param equilibDate  零件，产线
	 * @Description:  交换零件啦
	 */
	public boolean replaceLj(EquilibLJ lj,Map<String,HashSet<String>> gxLj,EquilibDate equilibDate){
		
		//要交换的产线编号
		String  cxBh = lj.getCHANXH(); 
		//得到此产线下的所有零件
		List<EquilibLJ> ljs = equilibDate.getLjMap().get(cxBh) ;
		
		//是否已完成交换 ,魔认为未交换
		boolean bool = false;
		
		//迭代 共线
		for(Map.Entry<String, HashSet<String>>  gxEntry:gxLj.entrySet()){
			//迭代共线下的零件类型
			for(Iterator<String> ljLx = gxEntry.getValue().iterator();ljLx.hasNext();){
				//零件编号
				String ljBh = ljLx.next();
				//如果是同一种零件类型,则判断是否交换零件
				if(ljBh.equalsIgnoreCase(lj.getLINGJBH())){
				//得到与之交换产线下的零件集
				List<EquilibLJ> ljTmp = equilibDate.getLjMap().get(gxEntry.getKey()) ;
				//如果有与之交换的零件 (接收的零件方)
				EquilibLJ jsLj = checkLessThanJJPL(lj, ljTmp);
					//如果转移过去后，是大于经济批量的
					if(jsLj!=null){
						//接着检查是否有可交换的零件(交换的零件方),且交换后双方都大于经济批量
						Map<String,Integer> jhLjs = checkSFYJHLJ(lj,gxEntry.getValue(),ljs, ljTmp);
						//如果交换后均大于经济批量
						if(jhLjs.keySet().size()!=0){
							//进行交换
							//接收方零件相加
							jsLj.setLJSL(jsLj.getLJSL()+lj.getLJSL()) ;
							
							//交换方零件归零
							lj.setLJSL(0) ;
							
							//交换方接收零件
							this.addLJ(ljs, jhLjs) ;
							//接收方减去零件
							this.reduceLJ(ljTmp, jhLjs) ;
							
							return true;
							
						}
						else{
							//如果没有可交换的零件
							//拉平经济批量
//							this.levelJJPL(lj) ;
//							return ;
							bool = false;
						}
						
						
//						bool = true;
					}
					else{
						//拉平经济批量
//						this.levelJJPL(lj) ;
//						return ;
						bool = false;
					}
				}
			}
			
		}
		
//		如果未完成共线零件交换，则拉平经济批量
//		if(!bool){
//			this.levelJJPL(lj) ;
//		}
		return bool;
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午5:26:22
	 * @version 1.0
	 * @param ljJ 零件集
	 * @param ljMap 要加的零件集
	 * @Description:  加零件
	 */
	public void addLJ(List<EquilibLJ> ljJ,Map<String,Integer> ljMap ){
		
		// 找到此零件类型
		for (Iterator<EquilibLJ> ljIt = ljJ.iterator(); ljIt.hasNext();) {
			// 零件对象
			EquilibLJ ljTmp = ljIt.next();
			// 如果零件编号相同
			if (ljMap.containsKey(ljTmp.getLINGJBH())) {
				//零件相加
				ljTmp.setLJSL(ljTmp.getLJSL()+ljMap.get(ljTmp.getLINGJBH())) ;
				}
			}
	 }
		
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午5:26:22
	 * @version 1.0
	 * @param ljJ 零件集
	 * @param ljMap 要减的零件集
	 * @Description:  减零件
	 */
	public void reduceLJ(List<EquilibLJ> ljJ,Map<String,Integer> ljMap ){
		
		// 找到此零件类型
		for (Iterator<EquilibLJ> ljIt = ljJ.iterator(); ljIt.hasNext();) {
			// 零件对象
			EquilibLJ ljTmp = ljIt.next();
			// 如果零件编号相同
			if (ljMap.containsKey(ljTmp.getLINGJBH())) {
				//零件相加
				ljTmp.setLJSL(ljTmp.getLJSL()-ljMap.get(ljTmp.getLINGJBH())) ;
				}
			}
	 }
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午3:48:50
	 * @version 1.0
	 * @param lj 零件对象
	 * @param ljJ 零件集
	 * @return boolean
	 * @Description:  检查零件移过去后是否大于经济批量
	 */
	public EquilibLJ checkLessThanJJPL(EquilibLJ lj,List<EquilibLJ> ljJ ){
		/***1.如果转移的零件加上接收的产线上的同种零件小于此产线下的经济批量，则不可以转移*****************/
		// 找到此零件类型
		for (Iterator<EquilibLJ> ljIt = ljJ.iterator(); ljIt.hasNext();) {
			// 零件对象
			EquilibLJ ljTmp = ljIt.next();
			// 如果零件编号相同
			if (lj.getLINGJBH().equalsIgnoreCase(ljTmp.getLINGJBH())) {
				// 判断如果原有的零件加上移过去的零件大于经济批量
				if ((ljTmp.getLJSL() + lj.getLJSL()) >= Integer.parseInt(ljTmp
								.getJJPL())) {
					
					return ljTmp;
				}
			}
		}
		
		//魔认小于经济批量
		return null;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午4:35:39
	 * @version 1.0
	 * @param ljH 不用参与交换的零件号
	 * @param gxljs 共线的零件类型
	 * @param ljJHF 零件交换方
	 * @param ljJSF 零件接收方
	 * @return 
	 * @Description:  检查是否有可交换过来的零件
	 */
	public Map<String, Integer> checkSFYJHLJ(EquilibLJ lj,
			HashSet<String> gxljs, List<EquilibLJ> ljJHF, List<EquilibLJ> ljJSF) {

		/*** 2.如果转移的零件加上接收的产线上的同种零件大于此产线下的经济批量，且此产线下无可交换的零件,则不转移 *****************/
		Map<String, Integer> ljData = new HashMap<String, Integer>();

		int sl = lj.getLJSL();
		// 迭代共线零件
		for (Iterator<String> gxljIt = gxljs.iterator(); gxljIt.hasNext();) {
			// 零件号
			String ljHTmp = gxljIt.next();
			// 如果零件相同，则跳过
			if (ljHTmp.equalsIgnoreCase(lj.getLINGJBH())) {
				continue;
			}

			// 迭代零件接收方
			for (Iterator<EquilibLJ> ljIt = ljJSF.iterator(); ljIt.hasNext();) {

				EquilibLJ tmp = ljIt.next();
				//
				if (tmp.getLINGJBH().equalsIgnoreCase(ljHTmp)) {
					// 数量
					int tmpSl = tmp.getLJSL() - Integer.parseInt(tmp.getJJPL());
					if (tmpSl > 0) {
						// 如果接收方的零件移走后，仍能大于经济批量,则判断交换方的零件接收后是否大于经济批量
						if ((tmp.getLJSL() - Integer.parseInt(tmp.getJJPL())) >= sl) {
							// 判断交换方交换后零件是否大于经济批量,且交换方接收交换过来的零件后也大于经济批理
							if (checkJSLjMoreThanJJPL(tmp.getLINGJBH(), sl,
									ljJHF)) {
								// 缓存数据
								ljData.put(tmp.getLINGJBH(), sl);
								return ljData;
							}
						}
						//
						else {
							// 剩余数量
							sl = sl- (tmp.getLJSL() - Integer.parseInt(tmp
											.getJJPL()));
							// 判断交换方交换后零件是否大于经济批量,且交换方接收交换过来的零件后也大于经济批理
							if (checkJSLjMoreThanJJPL(
									tmp.getLINGJBH(),
									tmp.getLJSL()
											- Integer.parseInt(tmp.getJJPL()),
									ljJHF)) {
								// 缓存数据
								ljData.put(tmp.getLINGJBH(), tmp.getLJSL()
										- Integer.parseInt(tmp.getJJPL()));
							}

							// 如果交换后剩余的数量等于0,则直接返回
							if (sl == 0) {
								return ljData;
							}
						}

					}
				}

			}
		}

		// 如果没有交换零件
		if (sl > 0) {
			// 如果交换后sl大于0，则说明没有零件可交换,清空map
			ljData.clear();
		}
		return ljData;

	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午4:59:35
	 * @version 1.0
	 * @param lj 转移的零件
	 * @param ljJHF 零件集
	 * @return boolean
	 * @Description:  检查接收移过的零件后，判断其零件数量是否大于经济批量(true:大于或者等于,false:小于)
	 */
	public boolean checkJSLjMoreThanJJPL(String ljh,int ljs,List<EquilibLJ> ljJHF){

		//迭代零件
		for(Iterator<EquilibLJ> ljIt = ljJHF.iterator();ljIt.hasNext();){
			
			EquilibLJ tmp = ljIt.next();
			if(tmp.getLINGJBH().equalsIgnoreCase(ljh)){
				return (tmp.getLJSL()+ljs)>=Integer.parseInt(tmp.getJJPL())?true:false;
			}
		}
		
		return false;
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午1:55:22
	 * @version 1.0
	 * @param scx 生产线
	 * @param scxs 这一天所有的产线
	 * @param gxscx  零件在这周内所有共线的产线
	 * @return Set<String>
	 * @Description:  得到这一天内与scx共线的产线
	 */
	public Set<String> getGXCX(EquilibScx scx,List<EquilibScx> scxs,Set<String> gxscx){
		//共线产线
		Set<String> gxcx = new HashSet<String>();
		
		//迭代产线 找出所有共线产线
		for(Iterator<EquilibScx> cxIt = scxs.iterator();cxIt.hasNext();){
			//产线对象
			EquilibScx cx = cxIt.next();
			//如果这一天存在与其共零件的产线
			if(gxscx.contains(cx.getCHANXH())&&(!scx.getCHANXH().equalsIgnoreCase(cx.getCHANXH()))){
				//新增产线
				gxcx.add(cx.getCHANXH()) ;
			}
		}
		return gxcx;
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午1:57:36
	 * @version 1.0
	 * @param scx 生产线
	 * @param gxcx 这一天内共线产线
	 * @return   Map<String,HashSet<String>>
	 * @Description:  返回与该生线至少共线两种零件以上的产线
	 */
	public Map<String,HashSet<String>> getGXLjLx(EquilibScx scx,Set<String> gxcx){
		
		//共线两种零件以上的产线
		 Map<String,HashSet<String>> cxXiaGxLj = new HashMap<String, HashSet<String>>() ;
		 
		 //迭代共线产线
		 for(Iterator<String> gxcxIt = gxcx.iterator();gxcxIt.hasNext();){
			 //产线编号
			 String cxbh = gxcxIt.next();
			 //迭代这周的共线零件
			 for(Map.Entry<String, HashSet<String>> gxlj: EquilibDate.getGxLj().entrySet()){
				 //判断是否共线多种零件
				 if(gxlj.getValue().contains(scx.getCHANXH())&&gxlj.getValue().contains(cxbh)){
					 //封装零件号
					 this.wrapCxXiaGxLj(cxXiaGxLj, cxbh, gxlj.getKey()) ;
				 }
			 }
		 }
		 
		 return cxXiaGxLj;
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午2:15:12
	 * @version 1.0
	 * @param cxXiaGxLj 
	 * @param cxbh 产线编号
	 * @param gxljH 零件号
	 * @Description:   封装共产线下的共线零(key:产线号,value:零号)
	 */
	public void wrapCxXiaGxLj(Map<String,HashSet<String>> cxXiaGxLj,String cxbh ,String gxljH){
		
		//如果存在，则直接放入
		if(cxXiaGxLj.containsKey(cxbh)){
			cxXiaGxLj.get(cxbh).add(gxljH);
		}
		else{
			//不存在，则直接新增
			HashSet<String> set =new HashSet<String>();
			set.add(gxljH) ;
			cxXiaGxLj.put(cxbh,set) ;
		}
		
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午2:24:18
	 * @version 1.0
	 * @param cxXiaGxLj 产线共线的零件类型
	 * @Description:  过滤掉只共线一种零件类型的产线
	 */
	public void filterGxOneLjLx(Map<String,HashSet<String>> cxXiaGxLj){
		
		//产线
		Set<String> cx = cxXiaGxLj.keySet();
		
		//迭代产线，过滤掉只共线一种零的产线
		for(Iterator<String > cxIt = cx.iterator();cxIt.hasNext();){
			//产线号
			String cxh = cxIt.next();
			//如果只共线一种零件
			if(cxXiaGxLj.get(cxh).size()<=1){
				//移掉产线号
				cxXiaGxLj.remove(cxh);
				//重新循环
				cxIt = cxXiaGxLj.keySet().iterator();
			}
			
		}
		
	}
	
	/**********排产 增减产计算**************/
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午2:29:02
	 * @version 1.0
	 * @param pcjhs 一周的排产计划
	 * @param maoData 毛需求
	 * @param kcL 库存量
	 * @param jbInfo 基本参数
	 * @Description:  增减产计算
	 */
	public void incrementOrReducePlan(List<EquilibDate>  pcjhs,Map<String,HashMap<String,Integer>> maoData,Map<String,Integer> kcL,Map<String,Integer> top,Map<String,Integer> lower,Map<String, String> jbInfo){

		//如果是日棍动，则直接返回
		if(jbInfo.get("BS").equalsIgnoreCase("R")){
			return ;
		}
		
		
		// 第一天的期初库存
		Map<String, Integer> kcLTmp = kcL;
		// 迭代这周的
		for (Iterator<EquilibDate> eqIt = pcjhs.iterator(); eqIt.hasNext();) {
			// 排产计划
			EquilibDate pcjh = eqIt.next();
			
			// 将产线按工时升序排列
			sortScxAsc(pcjh.getScxs());
			// 这天所有的生产线
			List<EquilibScx> scxs = pcjh.getScxs();
			// 如果这天没有产线，则进入下天的增产减产计算
			if (scxs.size() == 0) {
				continue;
			}
			// 迭代这天的产线
			for (Iterator<EquilibScx> scxIt = scxs.iterator(); scxIt.hasNext();) {
				// 产线
				EquilibScx tmpCx = scxIt.next();
				// 如果这天所有的产线工时正常，（也就是在8-22小时之间）
				if (tmpCx.getGS() >= MIN_TIME
								&& tmpCx.getGS() <= MAX_TIME) {
					
					// 将这一天的库存量作为下一天的期初库存
//					kcL = this.getPcjhKcL(pcjh) ;
					continue;
				}

				//日期
				String date = null;
				//如果不是最后一个工作日
				if(pcjhs.indexOf(pcjh)<pcjhs.size()-1){
					//下一天的日期
					date  = pcjhs.get(pcjhs.indexOf(pcjh)+1).getDate();
				}else{
					//取下周的开始时间
					date = jbInfo.get("NEXTKSSJ" ) ;
				}
				//这个工作日的消耗量
				HashMap<String,Integer> xhL = maoData.containsKey(pcjh.getDate())? maoData.get(pcjh.getDate()):new HashMap<String, Integer>() ;
				pcjh.setXhL(xhL) ;
				//这天的期初库存
				pcjh.setQcKc(kcLTmp) ;
				//下个工作日的毛需求
				HashMap<String,Integer> maoXq = maoData.containsKey(date)? maoData.get(date):new HashMap<String, Integer>() ; 
				// 增产
				if (tmpCx.getGS() < MIN_TIME) {
					// 增产计算
					this.incrementPlan(tmpCx,pcjh,maoXq,jbInfo , top,new HashSet<String>());
				}
				// 减产
				else if (tmpCx.getGS() > MAX_TIME) {
					// 减产计算
					this.reducePlan(tmpCx,pcjh,maoXq,jbInfo,lower,new HashSet<String>());
				}
				// 将这一天的库存量作为下一天的期初库存
				//begin by gswang 2012-09-05
//				kcL.clear();
//				kcL.putAll(this.getPcjhKcL(pcjh));
				kcL = this.getPcjhKcL(pcjh) ;
				
			}

		}
	}

	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:47:51
	 * @version 1.0
	 * @param scx 产线
	 * @param pcjh 这天的排产计划
	 * @param maoXq 下一个工作日的毛需求
	 * @param jbInfo 基本参数
	 * @param top 零件库存上限表
	 * @param ljbhs 不需要计算库存系数的零件编号
	 * @Description:  增产计算
	 */
	public void incrementPlan(EquilibScx scx, EquilibDate pcjh,HashMap<String,Integer> maoXq,Map<String, String> jbInfo,Map<String, Integer> top,Set<String> ljBhs){
		//库存系数
		Map<String,Float> kcxs = this.getPcjhKcxs(pcjh, maoXq,ljBhs,ZC) ;
		
		//如果没有要增产的零件,则报警
		if(kcxs.size()==0){
			//消息
			String message = "日期:"+pcjh.getDate()+",产线组("+scx.getCHANXZBH()+")下的产线编号("+scx.getCHANXH()+")的工时为"+scx.getGS()+"小时,该产线下所有零件均已达到库存上限,无法通过增产计算达到最小工时8小时。请审查!";
			//报警
			EquilibMessage waring =   this.alarmMessage(scx, pcjh, jbInfo, message) ;
			pcjh.getChanxMessage().add(waring) ;
			//终止递归啦
			return;
		}
		//零件
		List<EquilibLJ> ljs = pcjh.getLjMap().get(scx.getCHANXH())==null?new ArrayList<EquilibLJ>():pcjh.getLjMap().get(scx.getCHANXH()) ;
		// 得到库存系数最小的 的零件
		String ljbh = this.findMinKCXS(kcxs);
		// 得到零件对象
		EquilibLJ lj = this.findLJBYBh(ljbh, ljs);
		// 如果零件为空，则不增产此零件
		
		if (lj == null) {
			// 不计算此零件的库存系数
			ljBhs.add(ljbh);
			this.incrementPlan(scx, pcjh, maoXq, jbInfo, top, ljBhs);
			return;
		}
		// 或者增产后的零件数量不足一个经济批量，则不增产此零件
		int tmpLJsl = lj.getLJSL()
				+ (int) ((MIN_TIME - scx.getGS()) * Integer.parseInt(scx
						.getSCXJP()));
		if (tmpLJsl < Integer.parseInt(lj.getJJPL())) {
			// 不计算此零件的库存系数
			ljBhs.add(ljbh);
			this.incrementPlan(scx, pcjh, maoXq, jbInfo, top, ljBhs);
			return;
		}
		
		// 工时差
		float gs = MIN_TIME - scx.getGS();
		// 如果增产后小于或者等 于该零件的经济批量.且不超过安全库存上限
		if (gs * Integer.parseInt(scx.getSCXJP()) <= Integer.parseInt(lj
						.getJJPL())
						&& (gs * Integer.parseInt(scx.getSCXJP()) + lj
										.getLJSL()) <= top.get(lj.getLINGJBH())) {
			
			//设置零件数量 (增加工时*生产线节拍 的零件数量)
			float sl = gs * Integer.parseInt(scx.getSCXJP());
			lj.setLJSL((int) (sl + lj.getLJSL()));
			// 设置最小工时
			scx.setGS(MIN_TIME);
			
			//终止递归啦
			return;

		}
		//如果增产后大于该零件的经济批量.且不超过安全库存上限
		else if(gs * Integer.parseInt(scx.getSCXJP()) > Integer.parseInt(lj
						.getJJPL())
						&& (Integer.parseInt(lj.getJJPL()) + lj
										.getLJSL()) <= top.get(lj.getLINGJBH())){
			// 增产一个零件的经济批量
			lj.setLJSL(Integer.parseInt(lj.getJJPL()) + lj.getLJSL());
			//设置增产时间
			//计算工时
//			DecimalFormat df = new DecimalFormat("#.00") ;
			//格式化工时
			float gsS = this.formatFloat(Float.parseFloat(lj.getJJPL())/Float.parseFloat(scx.getSCXJP())) ;
				//Float.parseFloat(df.format(Float.parseFloat(lj.getJJPL())/Float.parseFloat(scx.getSCXJP())));
			scx.setGS(scx.getGS()+gsS) ;
			
			//增产后刚好达到上限
			if(lj.getLJSL()==top.get(lj.getLINGJBH())){
				//则不需要在计算此零件的库存每系数
				ljBhs.add(lj.getLINGJBH()) ;
			}
			//递归按库存系数最小的增产
			this.incrementPlan(scx, pcjh, maoXq,jbInfo, top,ljBhs) ;
		}
		else{  //lj.getLJSL()) > top.get(lj.getLINGJBH()
			//增产后突破安全库存上限	,则 不计算此零件的库存系数
			ljBhs.add(ljbh);
			this.incrementPlan(scx, pcjh, maoXq, jbInfo, top, ljBhs);
			return;
		}
		
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午2:36:48
	 * @version 1.0
	 * @param kcxs 库存系数
	 * @param scx 减产计算
	 * @param jbInfo 基本参数
	 * @param lower 安全库存下限
	 * @param ljbhs 不需要计算库存系数的零件编号
	 * @Description:  减产计算
	 */
	public void reducePlan(EquilibScx scx, EquilibDate pcjh,HashMap<String,Integer> maoXq,Map<String, String> jbInfo,Map<String, Integer> lower,Set<String> ljBhs){
		
		// 库存系数
		Map<String, Float> kcxs = this.getPcjhKcxs(pcjh, maoXq,ljBhs,JC);
		//如果没有要减产的零件,则报警
		if(kcxs.size()==0){
			//消息
			String message = "日期:"+pcjh.getDate()+",产线组("+scx.getCHANXZBH()+")下的产线编号("+scx.getCHANXH()+")的工时为"+scx.getGS()+"小时,该产线下所有零件均已达到库存下限,无法通过减产计算达到最达最大工时22小时。请审查!";
			//报警
			EquilibMessage waring =   this.alarmMessage(scx, pcjh, jbInfo, message) ;
			pcjh.getChanxMessage().add(waring) ;
			//终止递归啦
			return;
		}
		// 零件
		List<EquilibLJ> ljs = pcjh.getLjMap().get(scx.getCHANXH())==null?new ArrayList<EquilibLJ>():pcjh.getLjMap().get(scx.getCHANXH()) ;
		// 得到库存系数最大的零件
		String ljbh = this.findMaxKCXS(kcxs);
		// 得到零件对象
		EquilibLJ lj = this.findLJBYBh(ljbh, ljs);
		//如果零件为空,或者都零件数量为0,，则不能对此零件进行减产
		if(lj==null||lj.getLJSL()==0){
			//不计算此零件的库存系数
			ljBhs.add(ljbh) ;
			this.reducePlan(scx, pcjh, maoXq, jbInfo, lower, ljBhs) ;
			return ;
		}
		
		// 工时差
		float gs = scx.getGS() - MAX_TIME ;
	
		// 如果减产后小于或者等 于该零件的经济批量.且不低于经济批量或者是减产后为0
		if (gs * Integer.parseInt(scx.getSCXJP()) <= Integer.parseInt(lj
						.getJJPL())
						&& (((lj.getLJSL() - gs
										* Integer.parseInt(scx.getSCXJP())) == 0)
						|| (lj.getLJSL() - gs
										* Integer.parseInt(scx.getSCXJP())) >= Integer
											.parseInt(lj.getJJPL())) ){
			// 设置零件数量 (减产工时*生产线节拍 的零件数量)
			float sl = gs * Integer.parseInt(scx.getSCXJP()); 
			lj.setLJSL((int) (lj.getLJSL()	- sl));
			//消息
			String message = "日期:"+pcjh.getDate()+",产线组("+scx.getCHANXZBH()+")下的产线编号("+scx.getCHANXH()+")的工时为"+scx.getGS()+"小时,减产编号为（"+ljbh+"）的零件"+(int)sl+"个,减产后工时达到"+MAX_TIME+"小时。请计划员审查!";			//报警
			// 设置最大工时
			scx.setGS(MAX_TIME);
			
			//报警
			EquilibMessage waring =   this.alarmMessage(scx, pcjh, jbInfo, message) ;
			pcjh.getChanxMessage().add(waring) ;
			// 终止递归了
			return;
		}
		// 如果减产后大于该零件的经济批量.且不低于经济批量或者是减产后为0
		else if (gs * Integer.parseInt(scx.getSCXJP()) > Integer.parseInt(lj
						.getJJPL())
						&& (((lj.getLJSL() - Integer.parseInt(lj.getJJPL())) == 0)
						|| (lj.getLJSL() - Integer.parseInt(lj.getJJPL())) >= Integer
											.parseInt(lj.getJJPL()))) {
			// 减产一个零件的经济批量
			lj.setLJSL(lj.getLJSL() - Integer.parseInt(lj.getJJPL()));
			//设置减产时间
			//格式化工时
			float gsS =this.formatFloat(Float.parseFloat(lj.getJJPL())/Float.parseFloat(scx.getSCXJP())) ; 
			//消息
			String message = "日期:"+pcjh.getDate()+",产线组("+scx.getCHANXZBH()+")下的产线编号("+scx.getCHANXH()+")的工时为"+scx.getGS()+"小时,减产编号为（"+ljbh+"）的零件"+(int)Integer.parseInt(lj.getJJPL())+"个,减产后工时达到"+(scx.getGS()-gsS)+"个小时。请计划员审查!";
			scx.setGS(scx.getGS()-gsS) ;
			//如果减产后小于经济批量或者减产后为0,则不需要计算此零件的库存系数
			if(lj.getLJSL()==0||lj.getLJSL()<Integer.parseInt(lj.getJJPL())){
				//则不需要在计算此零件的库存系数
				ljBhs.add(lj.getLINGJBH()) ;
			}
			
			//报警
			EquilibMessage waring =   this.alarmMessage(scx, pcjh, jbInfo, message) ;
			pcjh.getChanxMessage().add(waring) ;
			
			//递归减产计算
			this.reducePlan(scx, pcjh, maoXq,jbInfo, lower,ljBhs) ;

		}else{
			//减产后，该零件的排产量为负数或者不足一个经济批量,则 不计算此零件的库存系数
			ljBhs.add(ljbh) ;
			this.reducePlan(scx, pcjh, maoXq, jbInfo, lower, ljBhs) ;
			return ;
		}
		
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午11:56:44
	 * @version 1.0
	 * @param scx 生产线
	 * @param pcjh 排产计划
	 * @param jbInfo 基本参数
	 * @Description:   消息报警
	 */
	public EquilibMessage alarmMessage(EquilibScx scx, EquilibDate pcjh,Map<String, String> jbInfo,String info){
		//生成计划号
		String jIHH  = this.createJIHH(scx, jbInfo) ;
		//消息
		EquilibMessage message = new EquilibMessage();
		//设置标识
		message.setBIAOS(jbInfo.get("BS")) ;
		//设置产线号
		message.setCHANXH(scx.getCHANXH()) ;
		//产线组编号
		message.setCHANXZBH(scx.getCHANXZBH()) ;
		//设置计划号
		message.setJIHH(jIHH) ;
		//设置类型(日期产线)
		message.setLEIX(MESSAGE_LEIX_DATE_SCX) ;
		//时间
		message.setSHIJ(pcjh.getDate()) ;
		//用户中心
		message.setUSERCENTER(jbInfo.get("UC")) ;
		//消息内容
		message.setXIAOX(info) ;
		//创建者
		message.setCREATOR(jbInfo.get("CREATOR"));
		//创建时间
		message.setCREATE_TIME(jbInfo.get("CREATE_TIME")) ;
		//编辑者
		message.setEDITOR(jbInfo.get("EDITOR")) ;
		//编辑时间
		message.setEDIT_TIME(jbInfo.get("EDIT_TIME")) ;
		
		//保存报警消息
//		pcjh.getChanxMessage().put(scx.getCHANXH(), message) ;
		
		return message;
		
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午11:18:38
	 * @version 1.0
	 * @param scx 生产
	 * @param jbInfo 基本参数
	 * @return String
	 * @Description:  生成报警消息的计划号 
	 */
	public String createJIHH(EquilibScx scx,Map<String,String> jbInfo){
		// 计划号
		String jihh = "";
		/*
		 * //月模拟 if(jbInfo.get("BS").equalsIgnoreCase("Y")){ //计划号 jihh =
		 * jihh.concat
		 * (jbInfo.get("UC")).concat(scx.getCHANXH()).concat(jbInfo.get
		 * ("PERIOD")); } //滚动周期 else if
		 * (jbInfo.get("BS").equalsIgnoreCase("G")){ //计划号 jihh =
		 * jihh.concat(jbInfo
		 * .get("UC")).concat(scx.getCHANXH()).concat(jbInfo.get("PERIOD")); }
		 * //日滚动 else if (jbInfo.get("BS").equalsIgnoreCase("R")){
		 * 
		 * }
		 */
		// 计划号
		jihh = jihh.concat(jbInfo.get("UC")).concat(scx.getCHANXH().trim())
						.concat(jbInfo.get("PERIOD"));
		return jihh;
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午9:56:43
	 * @version 1.0
	 * @param pcjh 一天的排产计划
	 * @param maoData 下一个工作日的毛需求
	 * @param ljbhs 不需要计算库存系数的零件编号
	 * @param zjc 增减产类型 (1：增产,2:减产)
	 * @return Map<String,Float>
	 * @Description:  得到库存系数 
	 */
	public Map<String,Float>  getPcjhKcxs(EquilibDate pcjh,HashMap<String,Integer> maoXq,Set<String> ljBhs,int zjc){
		
		Map<String, Float> kcxs = new HashMap<String, Float>();
		//得到这天的库存量
		Map<String, Integer> nextDayQckcl = this.getPcjhKcL(pcjh);
		// 库存系数
		kcxs = caculateLjKcXs(nextDayQckcl, maoXq,ljBhs,zjc);
		return kcxs;
		
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:19:37
	 * @version 1.0
	 * @param pcjh
	 *            这天的排产计划
	 * @return Map<String, Integer>
	 * @Description: 得到这天的库存量(也就是下一天的期初库存)
	 */
	public Map<String, Integer> getPcjhKcL(EquilibDate pcjh) {

		Map<String, Integer> nextDayQckcl = new HashMap<String, Integer>();

		// 各个零件这天的排产量
		Map<String, Integer> pcl = this.sumPCL(pcjh);
		// 计算这天的库存量
		nextDayQckcl = this.caculateQcKc(pcl, pcjh.getQcKc(), pcjh.getXhL());

		return nextDayQckcl;

	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午2:57:19
	 * @version 1.0
	 * @param ljbh 零件编号
	 * @param ljs 零件集合
	 * @return  EquilibLJ
	 * @Description:  根据零件编号找到零件对象
	 */
	public EquilibLJ findLJBYBh(String ljbh, List<EquilibLJ> ljs){
		//零件
		EquilibLJ lj = null;
		//零件集迭代
		for(Iterator<EquilibLJ> ljIt=ljs.iterator();ljIt.hasNext();){
			//得到零件对象
			EquilibLJ tmpLj = ljIt.next();
			if(tmpLj.getLINGJBH().equalsIgnoreCase(ljbh)){
				return tmpLj;
			}
		}
		return lj;
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 上午11:56:15
	 * @version 1.0
	 * @param qckcL 期初库存
	 * @param xhL 消耗量
	 * @param ljbhs 不需要计算库存系数的零件编号
	 * @param zjc 增减产类型 (1：增产,2:减产)
	 * @return  Map<String,Float> 
	 * @Description:  计算库存系数 (下一个工作日的期初库存/下一个工作日的毛需求)
	 */
	public Map<String,Float> caculateLjKcXs(Map<String,Integer> qckcL,HashMap<String, Integer> xhL,Set<String> ljBhs, int zjc){
		
		//库存系数
		Map<String,Float> kcxs = new HashMap<String, Float>();
		//期初库存
		for(Map.Entry<String, Integer> qckc:qckcL.entrySet()){
			
			//是否需要计算库存系数
			if(ljBhs.contains(qckc.getKey())){
				continue;
			}
			
			//如果存在毛需求
			if(xhL.containsKey(qckc.getKey())){
				//计算库存系数
			    kcxs.put(qckc.getKey(), Float.parseFloat(String.valueOf(qckc.getValue()))/Float.parseFloat(String.valueOf(xhL.get(qckc.getKey()))));
			}
			else{
				//不存在此毛需求的话
				//增产 ,库存系数最大
				if(zjc==ZC){
					kcxs.put(qckc.getKey(), 10000000.00f);
				}else if (zjc==JC){
					//减产,库存系数最小
					 kcxs.put(qckc.getKey(), 0.00f);
				}
			}
		}
		return kcxs;
	}
	
	
	
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午1:28:57
	 * @version 1.0
	 * @param kcxs 库存系数
	 * @return  String
	 * @Description:  找出库存系数最小的零件
	 */
	public String findMinKCXS(Map<String,Float> kcxs){
		//零件编号
		String ljbh = null;
		//零件系数
		float  ljxs = 0.00f;
		//迭代库存系数
		for(Map.Entry<String, Float> kcxsEntry:kcxs.entrySet()){
			
			//初始化一 个零件号
			if(ljbh==null){
				//零件编号
				ljbh = kcxsEntry.getKey();
				//零件的库存系数
				ljxs = kcxsEntry.getValue();
			}
			else{
				//如果库存系数小于或者等于,则指向新的零件号
				if(ljxs>=kcxsEntry.getValue()){
					//零件编号
					ljbh = kcxsEntry.getKey();
					//零件的库存系数
					ljxs = kcxsEntry.getValue();
				}
				
			}
			
			
		}
		return ljbh;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午1:28:57
	 * @version 1.0
	 * @param kcxs 库存系数
	 * @return  String
	 * @Description:  找出库存系数最大的零件
	 */
	public String findMaxKCXS(Map<String,Float> kcxs){
		//零件编号
		String ljbh = null;
		//零件系数
		float  ljxs = 0.00f;
		//迭代库存系数
		for(Map.Entry<String, Float> kcxsEntry:kcxs.entrySet()){
			
			//初始化一 个零件号
			if(ljbh==null){
				//零件编号
				ljbh = kcxsEntry.getKey();
				//零件的库存系数
				ljxs = kcxsEntry.getValue();
			}
			else{
				//如果库存系数小于或者等于,则指向新的零件号
				if(ljxs<=kcxsEntry.getValue()){
					//零件编号
					ljbh = kcxsEntry.getKey();
					//零件的库存系数
					ljxs = kcxsEntry.getValue();
				}
				
			}
			
			
		}
		return ljbh;
	}
	
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午1:16:29
	 * @version 1.0
	 * @param pcL 排产量
	 * @param qckc 期初库存
	 * @param xhL 毛需求量(消耗量)
	 * @return Map<String,Integer>
	 * @Description:  计算下一天的期初库存(排产量+库存量-消耗量)
	 */
	public Map<String,Integer> caculateQcKc( Map<String,Integer> pcL, Map<String,Integer> qckc,HashMap<String, Integer> xhL){
		 //下一天的期初库存
		 Map<String,Integer> nextDayQckc = new HashMap<String, Integer>() ;
		 
		 //迭代排产量
		 for(Map.Entry<String,Integer> pc : pcL.entrySet()){
			 //得到消耗量(如果这天没有消耗，则将其消耗置为0)
			 int xhlSl = xhL.containsKey(pc.getKey())?xhL.get(pc.getKey()) :0;
			 //库存
			 int qckcs = pc.getValue()+ (qckc.containsKey(pc.getKey())? qckc.get(pc.getKey()):0) - xhlSl ;
//			 int qckcs = pc.getValue()+ qckc.get(pc.getKey()) - xhlSl ;
			 //作为下一天的期 初库存
			 nextDayQckc.put(pc.getKey(), qckcs) ;
		 }
		 return nextDayQckc;
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-18
	 * @Time: 下午12:59:11
	 * @version 1.0
	 * @param dataData 排产计划
	 * @return  Map<String,Integer>
	 * @Description:  各种零件的排产量
	 */
	public Map<String,Integer> sumPCL(EquilibDate pcjh){
		
		//各零件的map数据
		Map<String,Integer> ljPCL = new HashMap<String, Integer>() ;
		
		//这天的所有产线
		List<EquilibScx> scxs  = pcjh.getScxs();
		//这天的所有零件
		Map<String, List<EquilibLJ>> ljMap = pcjh.getLjMap();
		
		//迭代所有的产线
		for(Iterator<EquilibScx> scx = scxs.iterator();scx.hasNext();){
			//得到此产线下的所有零件
			String scxh = scx.next().getCHANXH() ;
			List<EquilibLJ> ljList = ljMap.get(scxh)==null?new ArrayList<EquilibLJ>():ljMap.get(scxh) ;
			//统计零件的排产量
			for(Iterator<EquilibLJ> ljIt = ljList.iterator();ljIt.hasNext();){
				//零件对象
				EquilibLJ lj = ljIt.next();
				//如果存在些零件号
				if(ljPCL.containsKey(lj.getLINGJBH())){
					//则与已添加的零件数相加
					ljPCL.put(lj.getLINGJBH(), ljPCL.get(lj.getLINGJBH())+lj.getLJSL()) ;
				}
				else{
//					//如果这天的零件排产量为0,则代表这天不生产此零件，也就不需要计算此零件的排产量
//					if(lj.getLJSL()==0){
//						continue;
//					}
					//如果没有此零件
					ljPCL.put(lj. getLINGJBH(),lj.getLJSL()) ;
				}
			}
		} 
		
		return ljPCL;
		
	}
	
	/**************写数据库*******************/
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午9:57:10
	 * @version 1.0
	 * @param equili 周排产计划
	 * @param jbInfo 基本参数
	 * @Description:  将均衡后的数据写入数据库
	 */
	public void writeDataToDB(List<EquilibDate> equili,Map<String,String> jbInfo) {
		
		//查询这周所有工作编号
		List<Map<String, String>> gzbhList = this.selectGZBH(jbInfo) ;
		//封装工作编号
		Map<String,String> warpGZBH = this.warpGZBH(gzbhList) ;
		//查询
		for(EquilibDate pcjh:equili){
			//更新排产计划
			this.updatePcjh(pcjh, jbInfo, warpGZBH) ;
			//记录这天的产线报警消息
			List<EquilibMessage> messages = pcjh.getChanxMessage();
//			//迭代这天的报警
//			for(Map.Entry<String, EquilibMessage> messageEntry:messages.entrySet()){
//				//写入报警消息
//				this.insertCxMessage(messageEntry.getValue()) ;
//			}
			//迭代这天的报警
			for(EquilibMessage message:messages){
				//写入报警消息
				this.insertCxMessage(message) ;
			}
		}
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午10:01:46
	 * @version 1.0
	 * @param pcjh 排产计划
	 * @param jbInfo 基本参数
	 * @param warpGZBH 工作编号
	 * @Description:  更新排产计划
	 */
	public void updatePcjh(EquilibDate pcjh,Map<String,String> jbInfo,Map<String,String> warpGZBH){
		
		Map<String,String> params = new HashMap<String, String>();
		//标识
		params.put("BS", jbInfo.get("BS")) ;
		//日期
		params.put("DATE", pcjh.getDate()) ;
		//日期
		params.put("TODAY", jbInfo.get("TODAY")) ;
		//用户中心
		params.put("UC", jbInfo.get("UC")) ;
		//产线组编号
		params.put("SCXZBH", jbInfo.get("SCXZBH")) ;
		
		//编辑者
		params.put("EDITOR", jbInfo.get("EDITOR"));
		//编辑时间
		params.put("EDIT_TIME", jbInfo.get("EDIT_TIME"));
		//创建者
		params.put("CREATOR", jbInfo.get("CREATOR"));
		//创建时间
		params.put("CREATE_TIME", jbInfo.get("CREATE_TIME"));
		
		
		//得到这天所有的产线
		List<EquilibScx> scxList = pcjh.getScxs();
		//迭代所有的产线
		for(EquilibScx scx:scxList){
			//更新产线的工时
			//(如果这天产线上班但不生产零件则零件件数量为0)
			List<EquilibLJ> ljs = pcjh.getLjMap().get(scx.getCHANXH())==null?new ArrayList<EquilibLJ>():pcjh.getLjMap().get(scx.getCHANXH());
			updateScxOfPcjh(params, scx,ljs,warpGZBH);
		}

		
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午10:08:38
	 * @version 1.0
	 * @param params 参数:标识,产线组号,用户中心,日期 
	 * @param scx 生产线
	 * @param ljs 产线下的所有零件
	 * @param warpGZBH 工作编号map
	 * @Description:   更新产线下工时及零件数量
	 */
	public void updateScxOfPcjh(Map<String,String> params,EquilibScx scx,List<EquilibLJ> ljs,Map<String,String> warpGZBH){
		
		//产线工时
		params.put("GS", String.valueOf(scx.getGS())) ;
		//产线号
		params.put("SCX", String.valueOf(scx.getCHANXH())) ;
		
		//零件数量 
		params.put("LJSL",String.valueOf( this.totalLjSl(ljs)));
		
		//如果不是是日棍动，则直接返回
		if(!params.get("BS").equalsIgnoreCase("R")){
			//更新产线工时
			this.updateCXGS(params) ;
		}
		
		
		//得到工作编号
		String gzbh = null;
		//如果不是是日棍动，则直接返回
		if(!params.get("BS").equalsIgnoreCase("R")){
			gzbh = warpGZBH.get(params.get("UC").concat(params.get("SCX")).concat(params.get("DATE")));
		}
		else{
			gzbh = params.get("UC").concat(params.get("SCX")).concat(params.get("TODAY"));
		}
		
		//工作编号
		params.put("GZBH", gzbh);
		//生产线节拍
		params.put("CXJP",scx.getSCXJP() );
		
		//更新产线下的各零件数量
		for(EquilibLJ lj:ljs){
			//更新零件
			this.updateLJOfSCX(lj, params) ;
			
		}
		
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-20
	 * @Time: 上午10:45:43
	 * @version 1.0
	 * @param lj 零件对象
	 * @param params  标识,产线组号,用户中心,日期 ,产线工时,产线号,工作编号,生产线节拍
	 * @Description:  
	 */
	public void updateLJOfSCX(EquilibLJ lj,Map<String,String> params){
		//零件数量
		params.put("SL", String.valueOf(lj.getLJSL()));
		//零件编号
		params.put("LJBH", lj.getLINGJBH());
		//工时
		//计算工时
//		DecimalFormat df = new DecimalFormat("#.00") ;
		//格式化工时
		//四舍五入
		float gsS = (Float.valueOf(String.valueOf(lj.getLJSL()))/ Float.valueOf(String.valueOf(params.get("CXJP") ))) + 0.005f;
		gsS = this.formatFloat(gsS) ;
		params.put("GS", String.valueOf(gsS));
		
		//如果零件数量为0,则直接删除
		if(lj.getLJSL()==0){
			//删除此零件
			this.deleteCXLJSL(params) ;
			return;
		}
		
		//如果没有影响行数
		if(this.updateCXLJSL(params)<=0){
			//新增此零件
			params.put("ID", getUUID());
			this.insertCXLJSL(params) ;
		}
		
	}
	
	
	/**********期初库存计算**************/
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-21
	 * @Time: 上午9:43:15
	 * @version 1.0
	 * @param pcjhs 周排产计划
	 * @param kcL 第一天的期初库存量
	 * @param maoData 周毛需求
	 * @param firstqckc 所有零件在这周的期初库存
	 * @Description:  期初库库存计算
	 */
	public void caculateQcKc(List<EquilibDate> pcjhs,Map<String,Integer> kcL,Map<String,HashMap<String,Integer>> maoData,List<Map<String,String>> qckcList ,Map<String, String> jbInfo){
		
		//库存量
//		Map<String,Integer> tmpKcL = new HashMap<String, Integer>();
		
		
//		//更新期初库存的时间为这周的开始时间
//		this.updateQckcFirst(qckcList, jbInfo) ;
		
		//迭代这周的排产计划
		for(EquilibDate pcjh:pcjhs){
			
			
			//计算这天各个零件的排产量
			Map<String, Integer> pcl = this.sumPCL(pcjh);
			//得到这天的消耗量
			HashMap<String, Integer>  xhl = maoData.containsKey(pcjh.getDate())? maoData.get(pcjh.getDate()):new HashMap<String, Integer>() ;
			
			//如果今天没有排产，但有肖耗，则将其在今天的排产量设置为0
			this.resetPcl(pcl, xhl) ;
			
			
			//写入数据库
			this.updateQckcToDb(qckcList,pcl,xhl) ;
			//得到今天要排产零件的期初库存
			Map<String,Integer> tmpKcL = this.getQckcByPcl(pcl, qckcList);
			// 计算这天排产完后的库存量
			Map<String, Integer>  kcl = this.caculateQcKc(pcl, tmpKcL, xhl);
			
			//日期
			String date = null;
			//如果不是最后一个工作日
			if(pcjhs.indexOf(pcjh)<pcjhs.size()-1){
				//下一天的日期
				date  = pcjhs.get(pcjhs.indexOf(pcjh)+1).getDate();
			}else{
				//取下周的开始时间
				date = jbInfo.get("NEXTKSSJ" ) ;
			}
			//更新list表
			this.updateQckcToList(qckcList, kcl, date) ;
			
			//清空报警信息
			pcjh.getChanxMessage().clear();
			//下一天的日期
			String tmpDate = pcjh.getDate();
			pcjh.setDate(date);
			//报警
			this.waringQckcINFO(pcjh, qckcList, jbInfo) ;
			//迭代这天的报警
			for(EquilibMessage message:pcjh.getChanxMessage()){
				//写入报警消息
				this.insertCxMessage(message) ;
			}
			//还原日期
			pcjh.setDate(tmpDate) ;
			
		}
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-8-10
	 * @Time: 下午03:25:48
	 * @version 1.0
	 * @param pcl 排产量
	 * @param xhl 消耗量
	 * @Description:  如果今天没有排产，但有肖耗，则将其在今天的排产量设置为0
	 */
	public void resetPcl(Map<String, Integer> pcl,HashMap<String, Integer>  xhl){
		
		for(Map.Entry<String, Integer> entry:xhl.entrySet()){
			
			if(!pcl.containsKey(entry.getKey())){
				pcl.put(entry.getKey(), 0) ;
			}
			
		}
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-07-14
	 * @Time: 上午19:43:15
	 * @version 1.0
	 * @param qckcList 第一天的期初库存量
	 * @param jbInfo  基本参数
	 * @Description:  更新期初库库存
	 */
	public void updateQckcFirst(List<Map<String,String>> qckcList,Map<String, String> jbInfo){
		
		for(Map<String,String> map:qckcList){
			map.put("SHIJ", jbInfo.get("KSSJ")) ;
		}
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-07-12
	 * @Time: 上午9:43:15
	 * @version 1.0
	 * @param pcjh 排产计划
	 * @param qckcList 第一天的期初库存量
	 * @param jbInfo  基本参数
	 * @Description:  期初库库存小于0则报警
	 */
	public void waringQckcINFO(EquilibDate pcjh,
			List<Map<String, String>> qckcList, Map<String, String> jbInfo) {
		// 迭代这天的零件
		for (Map.Entry<String, List<EquilibLJ>> cxEntry : pcjh.getLjMap()
				.entrySet()) {
			// 迭代期初库存零件
			for (Map<String, String> qckcMap : qckcList) {
				// 期初库存零件编号
				String ljbh = qckcMap.get("LINGJBH");
				// 期初库存
				float qckc = Float.parseFloat(qckcMap.get("QCKC"));
				// 如果期初库存为负数，则报警
				if (qckc < 0.00f) {
					// 判断这个零件是否在此产线上生产
					EquilibLJ lj = this.findLJBYBh(ljbh, cxEntry.getValue());
					
					// 如果在此线上生产，则判断其数量是否大于0
					if (lj != null && lj.getLJSL() > 0) {
						// 得到产线
						EquilibScx scx = this.findSCXByCxh(pcjh,
								cxEntry.getKey());
						String message = "零件编号" + ljbh + "的期初库存为" + qckc
								+ ",请计划员审查!";
						EquilibMessage waring = alarmMessage(scx, pcjh, jbInfo,
								message);
						pcjh.getChanxMessage().add(waring);
					}
				}
			}
		}
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-25
	 * @Time: 下午15:38
	 * @version 1.0
	 * @param pcl 排产量
	 * @param qckcList 期初库存
	 * @Description:  得到排产零件所对应的期初存库量
	 */
	public Map<String,Integer> getQckcByPcl(Map<String, Integer> pcl,List<Map<String,String>> qckcList){
		// 库存量
		Map<String, Integer> tmpKcL = new HashMap<String, Integer>();
		// 迭代排产量
		for (Map.Entry<String, Integer> cl : pcl.entrySet()) {
			// 迭代期初库库存量
			for (Map<String, String> map : qckcList) {
				// 如果零件编号相同
				if (cl.getKey().equalsIgnoreCase(map.get("LINGJBH"))) {
					// 记录此零件的期初库存
					tmpKcL.put(cl.getKey(),
							(int) Float.parseFloat(map.get("QCKC")));
					break;
				}
			}
		}
		return tmpKcL;
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-21
	 * @Time: 上午10:02:52
	 * @version 1.0
	 * @param qckcList  期初库存List
	 * @param kcl 库存量
	 * @param date 日期
	 * @Description:   更新库存
	 */
	public  void updateQckcToList(List<Map<String,String>> qckcList,Map<String, Integer>  kcl,String date){

		// 库存量
		for (Map.Entry<String, Integer> kclMap : kcl.entrySet()) {
			// 迭代库存map
			for (Map<String, String> qckcMap : qckcList) {
					// 时间
					qckcMap.put("SHIJ", date);
				// 如果存在零件编号,则更新期库存量，和日期
				if (kclMap.getKey().equalsIgnoreCase(qckcMap.get("LINGJBH"))) {
					// 期初库存量
					qckcMap.put("QCKC", String.valueOf(kclMap.getValue()));
					
				}
			}
		}
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-21
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param qckcList 期初厍存
	 * @Description:   更新零件日需求汇总表
	 */
	public void updateQckcToDb(List<Map<String,String>> qckcList,Map<String, Integer> pcl,Map<String, Integer> xhl){
		// 迭代库存map
		for (Map<String, String> qckcMap : qckcList) {
			// 更新零件日需求
			if(this.updateLjQckc(qckcMap)<=0){
				// 零件编号
				String ljbh = qckcMap.get("LINGJBH");
				//如果零件在这天有排产有消耗,则写表
				if (pcl.containsKey(ljbh) && pcl.get(ljbh) > 0 && xhl.containsKey(ljbh)) {
//				if ((pcl.containsKey(ljbh) && pcl.get(ljbh) > 0 ) || (xhl.containsKey(ljbh) && xhl.get(ljbh)>0)) {
					// 如果影响行数为0，则执行新增操作
					this.insertLjQckc(qckcMap);
				}
			}
		}
	}
	
	
	/****0.异常情况*********************/
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param workCalendar 工作日历
	 * @Description:   得到这周的数据
	 */
	public void equilibExceptionData(Map<String, String> jbInfo,Map<String, List<String>> workCalendar){
		
		//得到这周的数据
		List<EquilibDate> pcjhList = this.getWeekData(jbInfo, workCalendar) ;
		//得到需要处理的异常数据
		Map<EquilibDate,List<EquilibDate>> exceptionData = findUnWorkerDay(pcjhList,jbInfo) ;
		//均衡异常数据
		this.doExceptionData(exceptionData) ;
		
		//零件取整
		for(EquilibDate pcjh:pcjhList){
			//平衡零件 ,向上取整
			this.balanceLjs(pcjh.getLjMap()) ;
		}
		//更新数据库
		this.writeDataToDB(pcjhList, jbInfo) ;
		
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param jbInfo 基本参数
	 * @param workCalendar 工作日历
	 * @Description:   得到这周的数据
	 */
	public List<EquilibDate> getWeekData(Map<String, String> jbInfo,Map<String, List<String>> workCalendar){
		//开始日期
		Calendar ksCalendar  = Calendar.getInstance();
		try {
			ksCalendar.setTime(DateUtil.stringToDateYMD(jbInfo.get("KSSJ"))) ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		//结束日期
		Date jsDate = new Date();
		try {
			jsDate = DateUtil.stringToDateYMD(jbInfo.get("JSSJ"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		//工作日
		List<EquilibDate> listEquilib  = new ArrayList<EquilibDate>() ;
		
		//周内循环
		while (ksCalendar.getTime().compareTo(jsDate)<=0){
			//设置日期
			jbInfo.put("DATE", DateUtil.dateToStringYMD(ksCalendar.getTime())) ;
			//得到这一天的产线
			List<EquilibScx> scxs = this.selectScxGs(jbInfo) ;
			// 得到这一天产线下的零件
			List<EquilibLJ> ljSl = this.selectLjSL(jbInfo);
			// 封装零件数
			Map<String, List<EquilibLJ>> ljMap = this.wrapLjSl(ljSl);

			// 过滤掉这一天内不上班的生产线
			scxs = this.getScxDataOfDay(jbInfo, scxs, workCalendar);
			//加一天
			ksCalendar.add(Calendar.DAY_OF_MONTH, 1);
			//如果这天没有产线上班
			if(scxs.size()==0){
				continue;
			}
			//生成排产对象
			EquilibDate equilibDate = this.initEquilibDate(jbInfo, scxs, ljMap) ;
			//保存排产对象
			listEquilib.add(equilibDate);
		}
		//返回这周的排产计划
		return listEquilib;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param listEquilib 周排产计划
	 * @Description:   得到需要处理的异常数据
	 */
	public Map<EquilibDate,List<EquilibDate>> findUnWorkerDay(List<EquilibDate> listEquilib,Map<String, String> jbInfo){
		
		//异常 数据map
		Map<EquilibDate,List<EquilibDate>> exceptionData = new HashMap<EquilibDate, List<EquilibDate>>();
		//0012321
		//1条产线，且小于22小时的工作日
		List<EquilibDate> work1DayList = new ArrayList<EquilibDate>();
		//2条产线的工作日
		List<EquilibDate> work2DayList = new ArrayList<EquilibDate>();
		//大于22小时的工作日
		List<EquilibDate> workMaxDayList = new ArrayList<EquilibDate>();
		
		/*xss-0012321 修复此周只有一条产线工作，无法均衡的问题*/
		for(EquilibDate tmpDate:listEquilib){
			//如果是正常工作日 xss
			if(tmpDate.getScxs().size()>=2){////找出这周有2个产线的工作日，放入work2DayList
					work2DayList.add(tmpDate) ;
			}else{
				if(tmpDate.getScxs().get(0).getGS()<MAX_TIME){//找出本周只有1个产线 并且工时小于等于22的工作日work1DayList
					work1DayList.add(tmpDate) ;
				}
			}
			
			if(tmpDate.getScxs().get(0).getGS()>MAX_TIME){//大于22个小时的工作日，workMaxDayList
					workMaxDayList.add(tmpDate) ;
			}
		}  
		
		for(EquilibDate tmpDate:listEquilib){
		  //大于22小时才均衡
		  if(tmpDate.getScxs().get(0).getGS()>MAX_TIME){
			  
			if(work2DayList.size() ==0 && work1DayList.size() !=0 ){//如果本周只有1个产线工作,且还有未满22小时的工作日，则把多出的零件放入 该工作日中生产
				//工作日
				List<EquilibDate> tmpList = new ArrayList<EquilibDate>();
				//将正常工作日新增至tmpList中
				tmpList.addAll(work1DayList) ;
				//缓存
				exceptionData.put(tmpDate, tmpList);
				
				/********报警********/
				//此处用作报警
				Set<String> dates = new HashSet<String>() ;
				for(EquilibDate dat:work1DayList){
					dates.add(dat.getDate()) ;
				}
				//消息
				String message = "产线组("+tmpDate.getScxs().get(0).getCHANXZBH()+")下的产线编号("+tmpDate.getScxs().get(0).getCHANXH()+")的工时为"+tmpDate.getScxs().get(0).getGS()+"小时,该产线工时大于22小时，已均衡至工作日:"+dates.toString()+"。请审查!";
				EquilibMessage waring =   this.alarmMessage(tmpDate.getScxs().get(0), tmpDate, jbInfo, message) ;
				tmpDate.getChanxMessage().add(waring) ;
			} else if(work2DayList.size() !=0 ){//如果本周有几天是2个产线同时工作的），则把多出的零件放入2条产线的工作日中生产
				//工作日
				List<EquilibDate> tmpList = new ArrayList<EquilibDate>();
				//将正常工作日新增至tmpList中
				tmpList.addAll(work2DayList) ;
				//缓存
				exceptionData.put(tmpDate, tmpList);
				
				/********报警********/
				//此处用作报警
				Set<String> dates = new HashSet<String>() ;
				for(EquilibDate dat:work2DayList){
					dates.add(dat.getDate()) ;
				}
				//消息
				String message = "产线组("+tmpDate.getScxs().get(0).getCHANXZBH()+")下的产线编号("+tmpDate.getScxs().get(0).getCHANXH()+")的工时为"+tmpDate.getScxs().get(0).getGS()+"小时,该产线工时大于22小时，已均衡至工作日:"+dates.toString()+"。请审查!";
				EquilibMessage waring =   this.alarmMessage(tmpDate.getScxs().get(0), tmpDate, jbInfo, message) ;
				tmpDate.getChanxMessage().add(waring) ;
			} else if(work2DayList.size() == 0 && work1DayList.size() == 0 ){//如果List1 和List2都为空 。。。则无法转移均衡，发出报警
				//消息
				String message = "产线组("+tmpDate.getScxs().get(0).getCHANXZBH()+")下的产线编号("+tmpDate.getScxs().get(0).getCHANXH()+")的工时为"+tmpDate.getScxs().get(0).getGS()+"小时,此周所有产线工时大于22小时，且此周只有1条产线，无法均衡，请审查!";
				EquilibMessage waring =   this.alarmMessage(tmpDate.getScxs().get(0), tmpDate, jbInfo, message) ;
				tmpDate.getChanxMessage().add(waring) ;
			}
		  }
		} 
		
		return  exceptionData;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param listEquilib 异常数据
	 * @Description:   均衡异常数据
	 */
	public void  doExceptionData( Map<EquilibDate,List<EquilibDate>> exceptionData )	{
		
		//如果没有异常数据,则终止均衡
		if(exceptionData.isEmpty()){
			return;
		}
		
		//迭代异常数据,进行减产
		for(Map.Entry<EquilibDate, List<EquilibDate>> entry:exceptionData.entrySet()){
			//得到要转移的零件数
			Map<String,Float> ljs = this.redeuceLjForException(entry.getKey()) ;
			//均衡产线工时，及产线下的零件 entry.getValue()= 有2条产线的数据。 entry.getKey().getScxs().get(0).getCHANXH() = 大于22时的那天时间
			equilibScxForException(ljs, entry.getValue(), entry.getKey().getScxs().get(0).getCHANXH()) ;
		}
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param date 异常数据
	 * @Description:   得到要转移的零件数
	 */
	public Map<String,Float> redeuceLjForException(EquilibDate date ){
		
		Map<String,Float> ljMap = new HashMap<String, Float>();
		//计算要转移的零件数量
		float  slF = this.formatFloat((date.getScxs().get(0).getGS()-MAX_TIME)*Integer.parseInt(date.getScxs().get(0).getSCXJP()) );
		//设置最大工时
		date.getScxs().get(0).setGS(MAX_TIME) ;
		//将零件按倒序排列
		this.sortLJSLAsc(date.getLjMap()) ;
		//得到零件map 
		List<EquilibLJ> ljList = date.getLjMap() .get(date.getScxs().get(0).getCHANXH());
		//迭代零件
		for(EquilibLJ lj:ljList){
			//如果零件大于要转移的零件数
			if(lj.getLINGJSL()>=slF){
				//更新零件数量
				lj.setLINGJSL(lj.getLINGJSL()-slF);
				//记录要转移的零件
				ljMap.put(lj.getLINGJBH(), slF) ;
				
				return ljMap;
			}
			else{
				ljMap.put(lj.getLINGJBH(), lj.getLINGJSL()) ;
				lj.setLINGJSL(0.00f) ;
				slF = slF - lj.getLINGJSL()   ;
			}
		}
		
		
		return ljMap;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param ljs 零件集
	 * @param avgLjS 平均零件数
	 * @param ljMap 要转移的零件
	 * @Description:   增加零件数
	 */
	public void incrementLjForException(List<EquilibLJ> ljs ,float avgLjS ,Map<String,Float> ljMap){
		
		float sl = avgLjS;
		
		//迭代零件数
		for(String ljbh :ljMap.keySet()){
			//如果要转移的零件数为空,则转移下一个零件
			if(ljMap.get(ljbh)==0.00f){
				continue;
			}
			
			//得到零件对象
			EquilibLJ lj = this.findLJBYBh(ljbh, ljs) ;
			//如果这天不生产此零件
			if(lj==null){
				lj = new EquilibLJ();
				ljs.add(lj) ;
			}
			
			//如果要转移的零件数大于平均零件数
			if(ljMap.get(ljbh)>=sl){
				//更新零件数
				ljMap.put(ljbh, ljMap.get(ljbh)-sl) ;
				//重设零件数
				lj.setLINGJSL(lj.getLINGJSL()+sl) ;
				return;
			}
			else{
				//重设零件数
				lj.setLINGJSL(lj.getLINGJSL()+ljMap.get(ljbh)) ;
				//更新零件数
				sl= sl-ljMap.get(ljbh);
				ljMap.put(ljbh,0.00f) ;
				
			}
			
		}
		
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param ljMap 要转移的零件
	 * @param list 日期
	 * @param chanxH 产线号
	 * @Description:   计算产线工时，及更新零件
	 */
	public void equilibScxForException(Map<String,Float> ljMap,List<EquilibDate> list,String chanxH){
		
		//往前均几天
		int days = list.size() ;
		//得到所有零件数
		Float ljs = 0.00f;
		for(Float f:ljMap.values()){
			ljs = ljs + f;
		}
		//每天均多少的零件数
		float avgLj = this.formatFloat(ljs/days);
		
		//迭代每个天的排产计划
		for(EquilibDate pcjh:list){
			//得到生产线
			EquilibScx scx = this.findSCXByCxh(pcjh, chanxH) ;
			//如果这条产线在这天不上班
			if(scx==null){
				continue;
			}
			//转移零件转换成工时
			float gs = this.formatFloat(avgLj/Integer.parseInt(scx.getSCXJP()));
			//更新工时
			scx.setGS(scx.getGS()+gs) ;
			//更新零件
			this.incrementLjForException(pcjh.getLjMap().get(chanxH), avgLj, ljMap) ;
		}
		
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param pcjh 排产计划
	 * @param chanxH 产线号
	 * @Description:   根据产线号得到产线对象
	 */
	public EquilibScx findSCXByCxh(EquilibDate pcjh,String chanxH){
		//迭代排产计划中的产线
		for(EquilibScx scx:pcjh.getScxs()){
			//如果产线号相同
			if(scx.getCHANXH().equalsIgnoreCase(chanxH)){
				return scx ;
			}
		}
		return null;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param f 要格式化的数据
	 * @Description:   格式化保留两位小数的float类型的数据
	 */
	public Float formatFloat(float f){
		//计算工时
		DecimalFormat df = new DecimalFormat("#.00") ;
		//格式化工时
		float fData = Float.parseFloat(df.format(f));
		//返回
		return fData ;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com 
	 * @Date: 2012-6-29
	 * @Time: 上午10:14:11
	 * @version 1.0
	 * @param ljs 排产计划
	 * @Description:   统计这天排产的零件总数
	 */
	public int  totalLjSl(List<EquilibLJ> ljs){
		int  ljsl =0;
			//得到零件集
			for(EquilibLJ lj:ljs){
				//统计零件数量
				ljsl+=lj.getLJSL() ;
			}
			
		return ljsl;
	}
	
	
	
}
