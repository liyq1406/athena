package com.athena.xqjs.module.fenzxpc.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.jws.WebService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.fenzxpc.Beijwxjh;
import com.athena.xqjs.entity.fenzxpc.CalcDay;
import com.athena.xqjs.entity.fenzxpc.Dax;
import com.athena.xqjs.entity.fenzxpc.Fenzx;
import com.athena.xqjs.entity.fenzxpc.Fenzxpcjh;
import com.athena.xqjs.entity.fenzxpc.Guodsbxx;
import com.athena.xqjs.entity.fenzxpc.Lixd;
import com.athena.xqjs.entity.fenzxpc.Paicls;
import com.athena.xqjs.entity.fenzxpc.Paicsl;
import com.athena.xqjs.entity.fenzxpc.WarnMessage;
import com.athena.xqjs.entity.fenzxpc.Zhuxpcjh;
import com.athena.xqjs.entity.fenzxpc.Zongccfjg;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.google.common.base.Strings;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.jdbc.support.MultiDataSource;
import com.toft.core3.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@WebService(endpointInterface = "com.athena.xqjs.module.fenzxpc.service.FenzxpcService", serviceName = "/fenzxpcService")
@Component
public class FenzxpcService extends BaseService {

	private final Logger log = Logger.getLogger(FenzxpcService.class);
	
	/**	分装线计划类型-大线计划拆分 */
	private final static String TYPE_ZHUXJH_SPILT = "1";
	
	/**	分装线计划类型-备件外销插入 */
	private final static String TYPE_BEIJWX_INSERT = "2";
	
	/**	分装线计划类型-备件外销单独排产 */
	private final static String TYPE_BEIJWX_PRODUCEALONE = "3";
	
	/**	排空默认总成号 */
	private final static String DEFAULT_ZONGCH = "ZONGCH";
	
	/**	不可拆分总成号 */
	private final static String INDIVISIBLE_ZONGCH = "0000000000";
	
	/**	创建人 */
	private String creator = "";
	
	/**	排产开始时间 */
	private String paickssj = "";
		
	/** 排产结束时间 */
	private String paicjssj = "";
	
	/** 计算开始时间 */
	private String jiskssj = "";
	
	/** 计算结束时间 */
	private String jisjssj = "";
	
	/** 产线的计算日（ATHENA日历） */
	private Map<String, List<CalcDay>> calcDays = new HashMap<String, List<CalcDay>>();
	
	/** 主线计划工作日（SPPV日历） */
	private List<String> zhuxjhDays = new ArrayList<String>();
	
	/** 大线工作日（ATHENA日历） */
	private List<String> daxWorkingDays = new ArrayList<String>();
	
	/** 当前大线日期 */
	private CalcDay currentDay = new CalcDay();
	
	public String calcFenzxpcjh() throws ServiceException{
		String result = "";
		try{
			log.info("批量最后更新时间为：2016-07-20 09:34:00");
			MultiDataSource.useDataSource(ConstantDbCode.DATASOURCE_EXTENDS2);	//切换DDBH数据源
//			test();
			result = calcFenzxpcjh("4330");	//计算分装线排产
			MultiDataSource.useDataSource(ConstantDbCode.DATASOURCE_XQJS);	//切回执行层数据源
		}catch(Exception e){
			cleanData();
			MultiDataSource.useDataSource(ConstantDbCode.DATASOURCE_XQJS);
			log.error("计算分装线排产异常结束...",e);
			result = "fail";
			throw new ServiceException(e.getMessage());
		}
		return result;
	}
	
	public void test(){
		Paicls new_paicls = new Paicls(); 
		new_paicls.setUsercenter("UW");	//用户中心
		new_paicls.setDaxxh("TEST");				//大线线号
		new_paicls.setFenzxh("TEST");			//分装线号
		new_paicls.setZuihjscl("000001");	//最后计算车辆
		new_paicls.setFenzxzsxh("1");				//分装线总顺序号
		new_paicls.setPaickssj("2015-11-11");					//排产开始时间
		new_paicls.setPaicjssj("2015-11-11");					//排产结束时间
		new_paicls.setCreate_time(CommonFun.getJavaTime());	//创建时间
		new_paicls.setCreator("TEST");						//创建人
		//保存分装线排产流水
		super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"fenzxpc.insertPaicls", new_paicls);
//		if(1==1){
//			throw new ServiceException();
//		}
	}
	
	/**
	 * 计算分装线排产计划
	 * @return
	 * @throws Exception 
	 */
	public String calcFenzxpcjh(String creator) throws Exception{
		log.info("---------计算分装线排产计划开始---------");
		String result = "success";
		// 从大线排产计划表中获取参与排产的大线
		List<Dax> daxList = getPaicdx();
		// 循环大线
		for (int i = 0; i < daxList.size(); i++) {
			this.creator = creator;
			try{
				System.out.println("----------------------------");
				// 根据用户中心+大线线号，获取大线详细信息
				Dax dax = getDaxInfo(daxList.get(i));
				// 确定排产日期范围
				determineDateScope(dax);
				// 获取主线排产计划
				Map<String, List<Zhuxpcjh>> zhuxpcjhMap = getZhuxpcjh(dax);
				// 获取分装线
				Map<String, Fenzx> fenzxMap = getFenzx(dax);
				// 获取离线点
				Map<String, Lixd> lixdMap = getLixd(dax);
				// 获取未完成的备件外销计划
				Map<String, List<Beijwxjh>> beijwxjhMap = getBeijwxjh(dax);
				// 获取PDS拆分信息
				Map<String, List<Zongccfjg>> pdsCfxxMap =  getPDScfxx(dax, fenzxMap);
				// 获取大线以及分装线排产数量
				Map<String, List<Paicsl>> paicslMap = getPaicsl(dax);
				// 设置大线工作日,获取大线和分装线的工作日和工时
				setWorkingDays(dax, fenzxMap);
				// 待保存的主线排产计划（大线排空）
				List<Zhuxpcjh> zhuxpcjhSaveList = new ArrayList<Zhuxpcjh>();
				// 分装线对应的主线排产计划	key：分装线号		value：（key：日期	value：大线排产计划）
				Map<String, Map<String, List<Zhuxpcjh>>> fenzx2zhuxpcjhMap = new HashMap<String, Map<String, List<Zhuxpcjh>>>();
				// 计算出的分装线排产计划	key：分装线号		value：（key：日期	value：分装线排产计划）
				Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap = new TreeMap<String, Map<String, List<Fenzxpcjh>>>();
				// 计算出的分装线排产计划	key：大线线号		value：（key：日期	value：分装线排产计划）
				List<Zhuxpcjh> saveZhuxpcjhList = new ArrayList<Zhuxpcjh>();
				// 验证大线计划
				checkZhuxpcjh(dax, zhuxpcjhMap, fenzxMap, paicslMap, zhuxpcjhSaveList, saveZhuxpcjhList);
				// 大线离线提前，并计算分装线对应的大线计划
				calcEveryPartDaxsx(dax, fenzxMap, zhuxpcjhMap, lixdMap, fenzx2zhuxpcjhMap);
				// 生成分装线排产计划
				buildFenzxpcjh(dax, fenzxMap, lixdMap, pdsCfxxMap, fenzx2zhuxpcjhMap, fenzxpcjhMap);
				// 验证未拆分的大线计划
				checkUnspiltPlans(dax, fenzx2zhuxpcjhMap, zhuxpcjhMap);
				// 验证分装线大线工时节拍,当分装线工时节拍小于大线工时节拍时取大线的工时和节拍
				checkCapacity(dax, fenzxMap, fenzxpcjhMap);
				// 获取分装线排产流水
				Map<String, Paicls> paiclsMap = getFenzxPaicls(dax);
				// 均衡分装线排产计划，生成下线计划
				calcFenzxOffLinePlans(dax, fenzxpcjhMap, fenzxMap);
				printResultLog(fenzxpcjhMap, Const.FENZXPC_OFFLINE);	//打印测试
				// 处理备件外销计划
				dealWithBeijwxjh(dax, fenzxMap, beijwxjhMap, fenzxpcjhMap, paiclsMap);
				printResultLog(fenzxpcjhMap, Const.FENZXPC_OFFLINE);	//打印测试
				// 均衡分装线排产计划，生成上线计划
				calcFenzxOnLinePlans(dax, fenzxpcjhMap, fenzxMap, paiclsMap);
				printResultLog(fenzxpcjhMap, Const.FENZXPC_ONLINE);	//打印测试
				// 排产开始日期之前需要保存的分装线排产计划
				Map<String, Map<String, List<Fenzxpcjh>>> aheadResult = new HashMap<String, Map<String,List<Fenzxpcjh>>>();
				// 分装线离线提前
				putAheadOfFenzxLixd(dax, fenzxMap, lixdMap, fenzxpcjhMap);
				//把排产开始日期开始之前有离线点却不存在结果表的数据放入排产开始日期的排产计划最前面
				dealWithLiXianTofbq(fenzxpcjhMap,fenzxMap);
				// 总成报废
				List<Guodsbxx> scrapList = scrapZongc(dax, fenzxMap, fenzxpcjhMap, pdsCfxxMap);
				// 检查分装线排产计划是否超出产能
				//checkFenzxpcjhCount(fenzxpcjhMap, fenzxMap);
				// 之前没有分装线的排产时，会将向前一日偏移的数据也保存下来
				specialOperation(dax, fenzxMap, fenzxpcjhMap, paicslMap, getFenzxpcjhDay(dax), aheadResult);
				// 按照计划员输入的分装线排产数量，调整每日的分装线排产计划
				//ensureFenzxpcsl(fenzxpcjhMap, paicslMap, fenzxMap, dax);
				// 分装线排空
				addEmptyFenzxpcjh(dax, fenzxMap, fenzxpcjhMap, paicslMap);
				// 保存数据
				saveData(fenzxpcjhMap, lixdMap, aheadResult, scrapList, paiclsMap, fenzxMap, dax, scrapList, saveZhuxpcjhList);
				// 清除数据
				cleanData(fenzxpcjhMap,pdsCfxxMap);
			}catch(ServiceException e){
				log.info(e.getMessage());
				// 继续计算下一个大线
				continue;
			}
		}
		log.info("---------计算分装线排产计划结束---------");
		return result;
	}

	/**
	 * 验证大线计划
	 * @param dax
	 * @param zhuxpcjhMap
	 * @param fenzxMap
	 * @param paicslMap
	 * @param zhuxpcjhSaveList
	 * @param saveZhuxpcjhList
	 * @throws Exception
	 * @throws ParseException
	 */
	private void checkZhuxpcjh(Dax dax, Map<String, List<Zhuxpcjh>> zhuxpcjhMap,
			Map<String, Fenzx> fenzxMap, Map<String, List<Paicsl>> paicslMap,
			List<Zhuxpcjh> zhuxpcjhSaveList, List<Zhuxpcjh> saveZhuxpcjhList)
			throws Exception{
		//循环计算日期
		for (CalcDay day : calcDays.get(dax.getDaxxh())) {
			this.currentDay = day;
			if(day.getShifgzr().equals("0")){	//大线非工作日
				//检查大线是否有排产计划，有则放到下一个工作日并添加报警信息
				boolean isContinue = checkNotWorkingDayZhuxpcjh(dax, zhuxpcjhMap, fenzxMap);
				if(!isContinue){
					break;
				}
			}else {		//大线工作日
				//检查大线的排产计划数量是否超出产能
				checkWorkingDayZhuxpcjh(dax, zhuxpcjhMap, paicslMap);
				//大线排空
				zhuxpcjhSaveList.addAll(addEmptyZhuxpcjh(dax, zhuxpcjhMap.get(currentDay.getRiq()), paicslMap, saveZhuxpcjhList));
			}
		}
	}

	/**
	 * 生成分装线排产计划
	 * @param dax
	 * @param fenzxMap
	 * @param lixdMap
	 * @param pdsCfxxMap
	 * @param fenzx2zhuxpcjhMap
	 * @param fenzxpcjhMap
	 */
	private void buildFenzxpcjh(Dax dax, Map<String, Fenzx> fenzxMap,
			Map<String, Lixd> lixdMap, Map<String, List<Zongccfjg>> pdsCfxxMap,
			Map<String, Map<String, List<Zhuxpcjh>>> fenzx2zhuxpcjhMap,
			Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap) {
		//循环计算日期
		for (CalcDay day : calcDays.get(dax.getDaxxh())) {
			this.currentDay = day;
			//循环分装线
			for (String fenzxh : fenzxMap.keySet()) {
				//分装线
				Fenzx fenzx = fenzxMap.get(fenzxh);
				//大线计划
				List<Zhuxpcjh> zhuxpcjhList = fenzx2zhuxpcjhMap.get(fenzxh).get(day.getRiq());
				if(zhuxpcjhList != null && zhuxpcjhList.size() != 0){
					//根据大线计划，生成分装线排产计划
					List<Fenzxpcjh> fenzxpcjhList = 
						createFenzxpcjhByZhuxpcjh(fenzx, lixdMap, zhuxpcjhList, pdsCfxxMap);
					//重新生成分装线对应大线顺序号
					rebuildDuiydxsx(fenzxpcjhList);
					//重新生成顺序以及日期
					rebuildXiaxsx(fenzxpcjhList, day.getRiq());
					//将数据加入到结果map中
					addToResult(fenzxh, currentDay.getRiq(), fenzxpcjhList, fenzxpcjhMap);
				}
			}
		}
	}
	
	/**
	 * 清除数据
	 */
	private void cleanData(){
		creator = null;	//创建人
		paickssj = null;	//排产开始时间
		paicjssj = null;	//排产结束时间
		jiskssj = null;	//计算开始时间
		jisjssj = null;	//计算结束时间
		calcDays.clear();	//产线的计算日（ATHENA日历）
		zhuxjhDays.clear();	//主线计划工作日（SPPV日历）
		currentDay = null;	//当前大线日期
	}
	
	/**
	 * 清除数据
	 */
	private void cleanData( Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap ,  Map<String, List<Zongccfjg>> pdsCfxxMap) {
		cleanData();
		fenzxpcjhMap.clear();	//计算出的分装线排产计划
		pdsCfxxMap.clear();		//PDS拆分信息
	}

	/**
	 * 从大线计划表中获取参与排产的大线
	 * @return 参与排产的大线
	 */
	@SuppressWarnings("unchecked")
	private List<Dax> getPaicdx(){
		log.info("获取参与排产的大线...");
		List<Dax> daxList= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getPaicdx");
		if(daxList.size() == 0){
			//添加报警信息
			addWarnMessage(new WarnMessage("大线计划为空，请确认数据！", creator));
		}
		return daxList;
	}
	
	/**
	 * 获取大线信息
	 * @param dax 用户中心+大线线号
	 * @return 大线详细信息
	 */
	private Dax getDaxInfo(Dax daxParam){
		log.info("获取用户中心："+daxParam.getUsercenter()+"大线线号："+daxParam.getDaxxh()+"的详细大线信息...");
		Dax dax = (Dax) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("fenzxpc.getDaxInfo", daxParam);
		if(dax == null){
			//添加报警信息
			WarnMessage message = new WarnMessage(daxParam.getUsercenter(), daxParam.getDaxxh(),
					"用户中心"+daxParam.getUsercenter()+"大线"+daxParam.getDaxxh()+"不存在，对应的大线计划无法处理！", creator);
			addWarnMessage(message);
			throw new ServiceException(message.getXiaox());
		}
		return dax;
	}
	
	/**
	 * 添加报警信息
	 * @param message 报警信息
	 */
	private void addWarnMessage(WarnMessage message){
		log.error(message.toString());	//打印报警信息
		message.setCreate_time(CommonFun.getJavaTime());	//创建时间
		if(message.getPaicrq() == null){
			message.setPaicrq(paickssj);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("fenzxpc.insertWarnMessage", message);		//保存报警信息
	}
	
	/**
	 * 确定排产日期范围
	 * @param dax 大线信息
	 */
	@SuppressWarnings("unchecked")
	private void determineDateScope(Dax dax) throws Exception{
		log.info("确定排产日期范围...");
		//上次的排产流水
		Paicls lastPaicls = (Paicls) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("fenzxpc.getLastPcDateScope", dax);
		//SPPV工作日历（有排产计划的日期）
		this.zhuxjhDays = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getZhuxjhDays", dax);	
		if(lastPaicls != null){
			//排产开始时间 = 上次排产结束时间+1SPPV工作日（根据大线排产计划，取日期算）
			this.paickssj = addDaysByZhuxjh(lastPaicls.getPaicjssj(), 1);
		}else{	//第一次排产，排产流水为空
			//排产开始时间 = 系统当前时间
			this.paickssj = CommonFun.getJavaTime(new Date());
		}
		//TODO 目前的+2天与JL计划推算的+2天算法有点区别，注意是否要改正
		//结束时间 = 系统时间+排产封闭期（SPPV工作日）
		this.paicjssj = addDaysByZhuxjh(CommonFun.getJavaTime(new Date()), dax.getPaicfbq());
		//计算开始时间=排产开始时间-2个SPPV工作日
		this.jiskssj = addDaysByZhuxjh(paickssj, -2);
		//计算结束时间=排产结束时间+2个SPPV工作日
		this.jisjssj = addDaysByZhuxjh(paicjssj, 2);
//		//排产结束时间后面没有SPPV工作日
//		if(jisjssj.equals(paicjssj)){
//			if(isZhuxjhDay(paicjssj)){	//排产结束日期是SPPV工作日，计算结束日期就是排产结束日期
//				this.jisjssj = paicjssj;
//			}else{	//排产结束日期不是SPPV工作日，计算结束日期是排产结束日期的前1个SPPV工作日
//				this.jisjssj = addDaysByZhuxjh(paicjssj, -1);
//			}
//			//计算时间发生改变，排产时间相应改变
//			this.paicjssj = addDaysByZhuxjh(jisjssj, -2);
//		}
		//获取计算时间范围外30天ATHENA工作日
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("usercenter", dax.getUsercenter());
		paramMap.put("daxxh", dax.getDaxxh());
		paramMap.put("jiskssj", CommonFun.getDayTime(jiskssj, -30, "yyyy-MM-dd"));
		paramMap.put("jisjssj", CommonFun.getDayTime(jisjssj, 30, "yyyy-MM-dd"));
		this.daxWorkingDays = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getDaxWorkingDays", paramMap);	
		/*
		 * 排产结束时间 = 排产结束时间 + 1ATHENA工作日 - 1自然日
		 * 如果接下来是非工作日，将会计算这几天的备件外销单独排产
		 * 例如：系统时间=10-1，拆分天数=2，排产结束时间=10-3，但如果10-4、10-5为周末（非工作日）
		 * 		  将会对10-4、10-5备件外销单独排产，故此时的排产结束时间为10-5
		 */
		this.paicjssj = CommonFun.getDayTime(addDays(paicjssj, 1, daxWorkingDays), -1, "yyyy-MM-dd");
		if(jisjssj.equals(paicjssj)){	//大线非工作日但有排产计划
			//计算结束时间=排产结束时间+2个SPPV工作日
			this.jisjssj = addDaysByZhuxjh(paicjssj, 2);
		}
		if(paickssj.compareTo(paicjssj) > 0 
				||( lastPaicls != null && paickssj.compareTo(lastPaicls.getPaicjssj()) <= 0)){
			throw new ServiceException("当日的分装线排产已经计算过，或缺少主线排产计划，无法排产！");
		}
		log.info("排产日期范围为："+paickssj+"至"+paicjssj+"，计算日期范围为："+jiskssj+"至"+jisjssj+"。");
	}
	
	/**
	 * 根据日期集合找到指定日期之间之后的日期
	 * @param date
	 * @param addDays
	 * @param list
	 * @return
	 */
	private String addDays(String date, int addDays, List<String> list){
		String targetDate = findDate(list, date, addDays);	//目标日期（提前后的日期）
		//原始日期不是SPPV工作日，将原始日期按照日期顺序插入到SPPV工作日集合中，再提前
		if(targetDate.equals("")){	
			boolean flag = true;
			for (int i = 0; i < list.size(); i++) {
				if(date.compareTo(list.get(i)) < 0){
					list.add(i, date);
					flag = false;
					break;
				}
			}
			if(flag){
				list.add(date);
			}
			targetDate = findDate(list, date, addDays);
		}
		return targetDate;
	}
	
	/**
	 * 根据主线计划，将日期提前或延后
	 * @param date 原始日期
	 * @param addDays 添加天数
	 * @return 目标日期
	 * @throws ParseException 
	 */
	private String addDaysByZhuxjh(String date, int addDays) throws ParseException{
		List<String> list = new ArrayList<String>(zhuxjhDays);
		return addDays(date, addDays, list);
	}
	
	/**
	 * 判断大线计划是否有指定日期N天后的计划
	 * @param date 指定日期
	 * @param addDays N天
	 * @return
	 */
	private boolean hasTargetZhuxjhDay(String date, int addDays){
		
		//大线计划日期-升序排序
		List<String> list = new ArrayList<String>(zhuxjhDays);
		
		if(addDays == 0){
			return list.contains(date);
		}
		
		// 大线计划日期不包含指定日期，则添加该日期
		if(!list.contains(date)){
			for (int i = 0; i < list.size(); i++) {
				// 找到第一个大于指定日期
				if(date.compareTo(list.get(i)) < 0 ){
					list.add(i, date);
					break;
				}
				// 循环结束仍然没有大于指定日期的日期，说明该日期最大
				if(i == list.size() - 1){
					list.add(date);
					break;
				}
			}
		}
		// 找到指定日期在list中的角标
		for (int i = 0; i < list.size(); i++) {
			if(date.equals(list.get(i))){
				// 根据角标是否越界来判断是否有目标日期
				if(i + addDays < list.size() && i + addDays >= 0){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断指定日期是否SPPV工作日（通过是否有主线排产计划判断）
	 * @param targetDay
	 * @return
	 */
	private boolean isZhuxjhDay(String targetDay){
		for (String day : zhuxjhDays) {
			if(day.equals(targetDay)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从日期集合中找到指定日期指定天数之后的日期
	 * @param days	日期集合
	 * @param date	指定日期
	 * @param addDays	指定天数
	 * @return
	 */
	private String findDate(List<String> days, String date, int addDays){
		String targetDate = "";
		for (int i = 0; i < days.size(); i++) {
			if(date.equals(days.get(i))){
				int targetIndex = i + addDays;	//目标序号
				if(targetIndex >= days.size()){
					targetDate = days.get(days.size()-1);
				}else if(targetIndex < 0){
					targetDate = days.get(0);
				}else {
					targetDate = days.get(targetIndex);
				}
				break;
			}
		}
		return targetDate;
	}
	
	/**
	 * 根据排产日期范围获取主线排产计划
	 * 
	 * @param dax
	 *            大线信息
	 * @return 主线排产计划Map key:预计进焊装时间 value：对应时间的主线计划
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Zhuxpcjh>> getZhuxpcjh(Dax dax)
			throws ParseException {
		log.info("获取大线" + dax.getDaxxh() + "的主线排产计划...");
		Map<String, String> paramMap = new HashMap<String, String>(); // 查询参数Map
		Map<String, List<Zhuxpcjh>> zhuxpcjhMap = new TreeMap<String, List<Zhuxpcjh>>();	//主线排产计划Map key:预计进焊装时间 value：对应时间的主线计划
		paramMap.put("usercenter", dax.getUsercenter()); // 用户中心
		paramMap.put("daxxh", dax.getDaxxh()); // 大线线号
		paramMap.put("selectBeginDate", jiskssj);
		paramMap.put("selectEndDate", jisjssj);
		List<Zhuxpcjh> zhuxpcjhList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getZhuxjh",
				paramMap); // 主线排产计划
		if (zhuxpcjhList == null || zhuxpcjhList.size() == 0) {
			throw new ServiceException("大线" + dax.getDaxxh() + "的主线排产计划为空");
		}
		dax.setDaxlx(zhuxpcjhList.get(0).getXuqly()); // 取主线排产计划的第一个的需求来源为大线的大线类型（焊装/总装）
		for (Zhuxpcjh zhuxpcjh : zhuxpcjhList) { // 将list封装为map形式，并添加大线当日顺序号
			String key = zhuxpcjh.getYujjhzsj();
			if (zhuxpcjhMap.containsKey(key)) {
				List<Zhuxpcjh> list = zhuxpcjhMap.get(key);
				zhuxpcjh.setDaxsx(list.size() + 1);
				zhuxpcjh.setNewyujjhzsj(zhuxpcjh.getYujjhzsj());
				list.add(zhuxpcjh);
			} else {
				List<Zhuxpcjh> list = new ArrayList<Zhuxpcjh>();
				zhuxpcjh.setDaxsx(1);
				zhuxpcjh.setNewyujjhzsj(zhuxpcjh.getYujjhzsj());
				list.add(zhuxpcjh);
				zhuxpcjhMap.put(key, list);
			}
		}
		printZhuxpcjhLog(dax, zhuxpcjhMap);
		return zhuxpcjhMap;
	}
	
	/**
	 * 获取分装线信息
	 * @param dax 大线信息
	 * @return	分装线Map	key:分装线号		value：分装线信息
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Fenzx> getFenzx(Dax dax){
		log.info("获取大线"+dax.getDaxxh()+"的分装线信息...");
		Map<String, Fenzx> fenzxMap = new TreeMap<String, Fenzx>();	//分装线Map key:分装线号	value：分装线信息
		List<Fenzx> fenzxList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getFenzx", dax);	//分装线List
		if(fenzxList.size() == 0){
			addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), null,
						null, "大线"+dax.getDaxxh()+"没有对应的分装线，请先补全基础数据", creator));
		}
		for (Fenzx fenzx : fenzxList) {
			if(fenzx.getFenzxxs() > 2){
				addWarnMessage(new WarnMessage(fenzx.getUsercenter(), fenzx.getDaxxh(), fenzx.getFenzxh(),
						null, "分装线"+fenzx.getFenzxh()+"的分装线系数为"+fenzx.getFenzxxs()+
						"，后台无法处理，将不会生成该产线的分装线排产计划", creator));
			}else if(fenzx.getChews() == null || fenzx.getChessl() == null){
				addWarnMessage(new WarnMessage(fenzx.getUsercenter(), fenzx.getDaxxh(), fenzx.getFenzxh(),
						null, "分装线"+fenzx.getFenzxh()+"的车身数或消耗点工艺车身数为空"+
						"，后台无法处理，将不会生成该产线的分装线排产计划", creator));
			}else{
				fenzxMap.put(fenzx.getFenzxh(), fenzx);
			}
		}
		return fenzxMap;
	}
	
	/**
	 * 获取离线点信息
	 * @param dax 大线信息
	 * @return	离线点Map key:离线点名称	value：离线点信息
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Lixd> getLixd(Dax dax){
		log.info("获取大线"+dax.getDaxxh()+"的离线点信息...");
		Map<String, Lixd> lixdMap = new HashMap<String, Lixd>();	//离线点Map key:离线点名称	value：离线点信息
		List<Lixd> lixdList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getLixd", dax);	//离线点List
		for (Lixd lixd : lixdList) {	//封装成map
			if(lixd.getChessl() == null){
				addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), "大线"+dax.getDaxxh()+
						"的离线点"+lixd.getLixd()+"，对应消耗点的车身数量为空，有关该离线点的提前将不会处理", creator));
			}else{
				lixdMap.put(lixd.getLixd(), lixd);
			}
		}
		return lixdMap;
	}
	
	/**
	 * 获取未完成的备件外销计划
	 * @param dax 大线信息
	 * @return 备件外销计划Map key:分装线号	 value：备件外销计划
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Beijwxjh>> getBeijwxjh(Dax dax){
		log.info("获取大线"+dax.getDaxxh()+"的备件外销计划...");
		Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
		paramMap.put("usercenter", dax.getUsercenter());	//用户中心
		paramMap.put("daxxh", dax.getDaxxh());	//大线线号
		paramMap.put("selectEndDate", addDays(jisjssj, 1, daxWorkingDays));	//排产结束时间	
		List<Beijwxjh> beijwxjhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getBeijwxjh", paramMap);	//备件外销计划List
		Map<String, List<Beijwxjh>> beijwxjhMap = new HashMap<String, List<Beijwxjh>>();	//备件外销计划Map key:分装线号	 value：备件外销计划
		for (Beijwxjh beijwxjh : beijwxjhList) {	//封装成map	
			if(beijwxjhMap.containsKey(beijwxjh.getFenzxh())){
				beijwxjhMap.get(beijwxjh.getFenzxh()).add(beijwxjh);
			}else{
				List<Beijwxjh> list = new ArrayList<Beijwxjh>();
				list.add(beijwxjh);
				beijwxjhMap.put(beijwxjh.getFenzxh(), list);
			}
		}
		//将备件外销计划归集并排序
		sortBeijwxjh(beijwxjhMap,addDays(jisjssj, 1, daxWorkingDays));
		return beijwxjhMap;
	}
	
	/**
	 * 将备件外销计划集中并排序
	 * @param beijwxjhMap
	 */
	private void sortBeijwxjh(Map<String, List<Beijwxjh>> beijwxjhMap,String riq){
		for (String fenzxh : beijwxjhMap.keySet()) {
			List<Beijwxjh> list = beijwxjhMap.get(fenzxh);
			//按时间排序
			Collections.sort(list, Beijwxjh.timeComparator);
			//当日的备件外销计划
			List<Beijwxjh> todayList = new ArrayList<Beijwxjh>();
			int index = list.size();
			for (int i = 0; i < list.size(); i++) {	//循环得到当日的备件外销计划
				Beijwxjh beijwxjh  = list.get(i);
				if(beijwxjh.getYujjhzsj().compareTo(paickssj) >= 0 	//预计进焊装日期>=排产开始时间
						&& beijwxjh.getYujjhzsj().compareTo(riq) <= 0){	//预计进焊装日期<=排产结束时间
					todayList.add(beijwxjh);
					index = index == list.size() ? i : index;	//记录第一个当日的备件外销计划角标
				}
			}
			//移除当日及以后的备件外销计划
			removeList(list, index, list.size());	
			//将当日的备件外销计划按照同种总成号+备件外销属性+展开日期集中
			centralizeBeijwxjh(todayList);
			//将当日的备件外销计划按照数量排序
			Collections.sort(todayList, Beijwxjh.shulComparator);
			//加上当日的备件外销计划
			list.addAll(todayList);
		}
	}
	
	
	/**
	 * 获取PDS拆分信息
	 * @param dax 大线信息
	 * @return PDS拆分信息Map key:总成号+展开日期	 value:拆分结果
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Zongccfjg>> getPDScfxx(Dax dax, Map<String, Fenzx> fenzxMap) throws ParseException{
		log.info("获取大线"+dax.getDaxxh()+"的PDS拆分信息...");
		Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
		paramMap.put("usercenter", dax.getUsercenter());	//用户中心
		paramMap.put("daxxh", dax.getDaxxh());	//大线线号
		paramMap.put("selectBeginDate", jiskssj);	//计算开始时间
		paramMap.put("selectEndDate", jisjssj);		//计算结束时间
		List<Zongccfjg> zongccfjgList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getPDScfxx", paramMap);	//PDS拆分信息List
		//PDS拆分信息Map key:总成号+展开日期 value:拆分结果
		Map<String, List<Zongccfjg>> pdsCfxxMap = spiltZhankrq(zongccfjgList, dax);	
		//检查侧围线的零件类型
		checkLingjlx(pdsCfxxMap, fenzxMap);
		return pdsCfxxMap;
	}
	
	/**
	 * 将总成拆分结果的开始展开日期~结束展开日期拆分为展开日期
	 * 例如：某条记录的展开日期为3-10~3-12，将拆分为3-10、3-11、3-12 三条结果
	 * @param zongccfjgList
	 * @return
	 * @throws ParseException
	 */
	private Map<String, List<Zongccfjg>> spiltZhankrq(List<Zongccfjg> zongccfjgList, Dax dax) throws ParseException{
		// key:总成号+展开日期 value:拆分结果
		Map<String, List<Zongccfjg>> pdsCfxxMap = new HashMap<String, List<Zongccfjg>>();
		// 开始展开日期
		Calendar start = new GregorianCalendar();
		// 结束展开日期
		Calendar end = new GregorianCalendar();
		for (Zongccfjg zongccfjg : zongccfjgList) {
			start.setTime(CommonFun.sdfAxparse(zongccfjg.getKaiszkrq()));
			end.setTime(CommonFun.sdfAxparse(zongccfjg.getJieszkrq()));
			// 循环开始展开日期~结束展开日期
			while(start.before(end) || start.equals(end)){
				zongccfjg.setZhankrq(CommonFun.sdfAxformat(start.getTime()));
				String key = zongccfjg.getZongch()+zongccfjg.getZhankrq();
				if(pdsCfxxMap.containsKey(key)){
					List<Zongccfjg> list = pdsCfxxMap.get(key);
					//检查同一总成-零件-消耗点-系数展开日期是否重叠
					checkPDScfxx(dax, list, zongccfjg);
				}else{
					List<Zongccfjg> list = new ArrayList<Zongccfjg>();
					list.add(zongccfjg);
					pdsCfxxMap.put(key, list);
				}
				start.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		zongccfjgList.clear();
		return pdsCfxxMap;
	}
	
	/**
	 * 检查侧围线，如果零件类型为空，添加报警信息
	 * @param pdsCfxxMap
	 * @param fenzxMap
	 */
	private void checkLingjlx(Map<String, List<Zongccfjg>> pdsCfxxMap, Map<String, Fenzx> fenzxMap){
		//侧围线没有零件类型	key：分装线号	value：零件
		Map<String, Set<String>> errorMap = new HashMap<String, Set<String>>();
		//循环零件拆分结果，找到侧围线没有零件类型的零件
		for (Entry<String, List<Zongccfjg>> entry : pdsCfxxMap.entrySet()) {
			List<Zongccfjg> zongccfjgList = entry.getValue();
			for (Zongccfjg zongccfjg : zongccfjgList) {
				Fenzx fenzx = fenzxMap.get(zongccfjg.getFenzxh());
				//侧围线且拆分出的零件类型为空
				if(fenzx != null && fenzx.getFenzxxs() == 2 && zongccfjg.getLingjlx() == null){
					if(errorMap.containsKey(fenzx.getFenzxh())){
						errorMap.get(fenzx.getFenzxh()).add(zongccfjg.getLingj());
					}else{
						Set<String> errorLingj = new HashSet<String>();
						errorLingj.add(zongccfjg.getLingj());
						errorMap.put(fenzx.getFenzxh(), errorLingj);
					}
				}
			}
		}
		//循环添加报警信息
		for (Entry<String, Set<String>> entry : errorMap.entrySet()) {
			String fenzxh = entry.getKey();
			Fenzx fenzx = fenzxMap.get(fenzxh);
			for (String lingj : entry.getValue()) {
				WarnMessage warnMessage = new WarnMessage(fenzx.getUsercenter(), fenzx.getDaxxh(), 
						fenzxh, paickssj, "分装线"+fenzxh+"为侧围线，但零件"+lingj+"的零件类型为空，请确定产线零件表数据", creator);
				//添加报警信息
				addWarnMessage(warnMessage);
			}
		}
	}
	
	/**
	 * 获取大线以及分装线的排产数量
	 * @param dax
	 * @return 排产数量Map key:日期	 value：排产数量
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Paicsl>> getPaicsl(Dax dax){
		log.info("获取大线"+dax.getDaxxh()+"以及其分装线的排产数量...");
		Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
		paramMap.put("usercenter", dax.getUsercenter());	//用户中心
		paramMap.put("daxxh", dax.getDaxxh());	//大线线号
		paramMap.put("selectBeginDate", jiskssj);	//计算开始时间
		paramMap.put("selectEndDate", jisjssj);		//计算结束时间
		Map<String, List<Paicsl>> paicslMap = new HashMap<String, List<Paicsl>>();	//排产数量Map key:日期	 value：排产数量
		List<Paicsl> paicslList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getPaicsl", paramMap);	//排产数量计划List
		for (Paicsl paicsl : paicslList) {	//封装成map	
			if(paicslMap.containsKey(paicsl.getRiq())){
				paicslMap.get(paicsl.getRiq()).add(paicsl);
			}else{
				List<Paicsl> list = new ArrayList<Paicsl>();
				list.add(paicsl);
				paicslMap.put(paicsl.getRiq(), list);
			}
		}
		return paicslMap;
	}
	
	/**
	 * 检查同一总成-零件-消耗点-系数展开日期是否重叠
	 * @param dax
	 * @param list
	 * @param zongccfjg
	 */
	private void checkPDScfxx(Dax dax, List<Zongccfjg> list, Zongccfjg zongccfjg){
		for (Zongccfjg bean : list) {
			if(bean.getLingj().equals(zongccfjg.getLingj()) && 
					bean.getXiaohd().equals(zongccfjg.getXiaohd()) &&
					bean.getZhankrq().equals(zongccfjg.getZhankrq())){
				if(bean.getFenzxh().equals(zongccfjg.getFenzxh())){
					throw new ServiceException("总成"+zongccfjg.getZongch()+"展开日期"+
							zongccfjg.getZhankrq()+"匹配多个总成-零件-消耗点-系数，大线"+
							dax.getDaxxh()+"计算终止");
				}else{
//					throw new ServiceException("总成"+zongccfjg.getZongch()+"零件"+
//							zongccfjg.getLingj()+"消耗点"+zongccfjg.getXiaohd()+"匹配多个分装线，大线"+
//							dax.getDaxxh()+"计算终止");
				}
			}
		}
		list.add(zongccfjg);
	}
	
	/**
	 * 设置大线工作日
	 * @param dax
	 * @param fenzxMap
	 */
	@SuppressWarnings("unchecked")
	private void setWorkingDays(Dax dax, Map<String, Fenzx> fenzxMap){
		log.info("获取大线"+dax.getDaxxh()+"的工作日...");
		Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
		paramMap.put("usercenter", dax.getUsercenter());	//用户中心
		paramMap.put("selectBeginDate", jiskssj);	//计算开始时间
		paramMap.put("selectEndDate", addDays(jisjssj, 1, daxWorkingDays));		//计算结束时间
		paramMap.put("shengcx", getShengcxStr(dax, fenzxMap));	//生产线
		List<CalcDay> workingDaysList = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getWorkingDays", paramMap);	//工作日List
		for (CalcDay bean : workingDaysList) { 	//封装成map key：线号	value：工作日
		//	bean.setGongs(Double.parseDouble("4"));
			if(this.calcDays.containsKey(bean.getXianh())){
				this.calcDays.get(bean.getXianh()).add(bean);
			}else{
				List<CalcDay> list = new ArrayList<CalcDay>();
				list.add(bean);
				this.calcDays.put(bean.getXianh(), list);
			}
		}
		//SPPV工作日添加这个工作日
		if(!zhuxjhDays.contains(paramMap.get("selectEndDate"))){
			zhuxjhDays.add(paramMap.get("selectEndDate"));
		}
		//检查工作日历是否为空
		checkWorkingDays(dax, fenzxMap);
	}
	
	/**
	 * 获取分装线当日的产能
	 * @param fenzx
	 * @param riq
	 * @return
	 */
	private int getFenzxCapacity(Fenzx fenzx, String riq){
		for (CalcDay daxDay : calcDays.get(fenzx.getDaxxh())) {
			if(daxDay.getRiq().equals(riq)){
				CalcDay fenzxDay = getFenzxCurrentCalcDay(
						calcDays.get(fenzx.getFenzxh()), daxDay.getRiq());
				return (int)(fenzx.getShengcjp()*fenzxDay.getGongs());
			}
		}
		return 0;
	}
	
	/**
	 * 检查工作日历是否为空
	 * @param dax
	 * @param fenzxMap
	 * @return
	 */
	private void checkWorkingDays(Dax dax, Map<String, Fenzx> fenzxMap){
		if(calcDays.get(dax.getDaxxh()) == null){	//工作日历为空
			WarnMessage message = new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), "大线"+dax.getDaxxh()+
					"的工作日历为空，无法计算该大线的分装线排产计划", creator);
			addWarnMessage(message);
			throw new ServiceException(message.getXiaox());
		}
		//剔除休息时间往前最多只有2天的数据  但是排产计划可能取到n天前的计划  那些天就没有工时了
		for (CalcDay daxDay : calcDays.get(dax.getDaxxh())) {
			if(daxDay.getRiq().compareTo(paickssj)<0 && daxDay.getGongs()==0){
				daxDay.setGongs(24.0);
			}	
		}
		List<String> removeList = new ArrayList<String>();
		for (String fenzxh : fenzxMap.keySet()) {
			if(calcDays.get(fenzxh) == null){	//工作日历为空
				removeList.add(fenzxh);	
				addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), fenzxh, null, 
						"分装线"+fenzxh+"的工作日历为空，无法计算该分装线的排产计划", creator));
			}
		}
		for (String fenzxh : removeList) {	//移除分装线
			fenzxMap.remove(fenzxh);
		}
		for (CalcDay daxDay : calcDays.get(dax.getDaxxh())) {	//循环大线日历
			for (String fenzxh : fenzxMap.keySet()) {	//循环分装线
				for (CalcDay calcDay : calcDays.get(fenzxh)) {	//	循环分装线日历
					if(daxDay.getRiq().compareTo(calcDay.getRiq()) == 0){	//找到同一天
						if(daxDay.getShifgzr().equals("1") && calcDay.getShifgzr().equals("0")){	//大线工作日，分装线非工作日
							calcDay.setShifgzr("1");	//设置分装线为工作日
							addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), 
									fenzxh, calcDay.getRiq(), "分装线"+fenzxh+"在"+calcDay.getRiq()+
									"非工作日，而大线为工作日，计算将会把分装线当日视为工作日", creator));
						}
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 得到生产线字符串
	 * @param dax 大线
	 * @param fenzxMap 分装线
	 * @return
	 */
	private String getShengcxStr(Dax dax, Map<String, Fenzx> fenzxMap){
		Set<String> shengcxSet = fenzxMap.keySet();
		StringBuilder sb = new StringBuilder("'"+dax.getDaxxh()+"',");
		for (String str : shengcxSet) {
			sb.append("'" + str + "',");
		}
		if(!sb.equals("")){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}
	
	/**
	 * 检查大线非工作日是否有排产计划
	 * @param dax
	 * @param fenzxMap
	 * @param zhuxpcjhMap
	 * @throws Exception 
	 */
	private boolean checkNotWorkingDayZhuxpcjh(Dax dax, Map<String, List<Zhuxpcjh>> zhuxpcjhMap, Map<String, Fenzx> fenzxMap) throws Exception{
		log.info("大线"+dax.getDaxxh()+"在"+currentDay.getRiq()+"非工作日，检查是否有排产计划...");
		List<Zhuxpcjh> todayZhuxpcjh = zhuxpcjhMap.get(currentDay.getRiq());	//当日的主线排产计划
		boolean result = true;
		if(todayZhuxpcjh != null && todayZhuxpcjh.size() != 0){	//主线排产计划不为空
			//String nextWorkingday = getNextWorkgingDay(dax.getDaxxh(), currentDay.getRiq());
			//获取下一个SPPV工作日，不是athena工作日
			String nextWorkingday = addDaysByZhuxjh(currentDay.getRiq(), 1);
			
			if(nextWorkingday == null || currentDay.getRiq().equals(nextWorkingday)){	//后面没有SPPV工作日
				//下一个SPPV工作日 = 当前日期 + 1大线工作日
				nextWorkingday = addDays(currentDay.getRiq(), 1, daxWorkingDays);
				//虚拟出这个工作日
				createVirtualDay(nextWorkingday, dax, fenzxMap);
				//下一工作日的主线计划
				zhuxpcjhMap.put(nextWorkingday, new ArrayList<Zhuxpcjh>());
				//跳出循环，避免出现死循环
				result = false;
			}
			//下一工作日的主线计划
			List<Zhuxpcjh> nextDayZhuxpcjh = zhuxpcjhMap.get(nextWorkingday);
			if(nextDayZhuxpcjh != null){
				//将当日的排产计划放到下一个工作日中
				nextDayZhuxpcjh.addAll(0 ,todayZhuxpcjh);
				//重新生成下一个工作日的大线顺序号
				rebuildDaxsxh(nextDayZhuxpcjh, nextWorkingday);
				addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), currentDay.getRiq(), 
						"大线"+dax.getDaxxh()+"在"+currentDay.getRiq()+
						"非工作日，但有排产计划，当日的排产计划将放到下一个工作日处理", creator));
			}
			//清除当日的主线计划
			todayZhuxpcjh.clear();
			//SPPV工作日移除大线非工作日
			zhuxjhDays.remove(currentDay.getRiq());
			zhuxpcjhMap.remove(currentDay.getRiq());
		}
		return result;
	}
	
	/**
	 * 创建一个虚拟的SPPV工作日
	 * @param day
	 * @param dax
	 * @param fenzxMap
	 */
	private void createVirtualDay(String day, Dax dax, Map<String, Fenzx> fenzxMap){
		//SPPV工作日添加这个工作日
		zhuxjhDays.add(day);
		CalcDay daxDay = new CalcDay();
		daxDay.setXianh(dax.getDaxxh());
		daxDay.setRiq(day);
		daxDay.setShifgzr("1");
		daxDay.setGongs(Double.valueOf("9999"));
		//大线工作日添加这个工作日
		calcDays.get(dax.getDaxxh()).add(daxDay);
		for (String fenzxh : fenzxMap.keySet()) {
			CalcDay fenzxDay = new CalcDay();
			daxDay.setXianh(fenzxh);
			fenzxDay.setRiq(day);
			fenzxDay.setShifgzr("1");
			fenzxDay.setGongs(Double.valueOf("9999"));
			//分装线工作日添加这个工作日
			calcDays.get(fenzxh).add(fenzxDay);
		}
	}
	
	/**
	 * 创建一个虚拟的SPPV工作日
	 * @param day
	 * @param dax
	 * @param fenzxMap
	 */
	private void createVirtualDay(String day, Dax dax,Fenzx fenzx){
		//SPPV工作日添加这个工作日
		if(!zhuxjhDays.contains(day)){
			zhuxjhDays.add(day);
		}
		CalcDay daxDay = new CalcDay();
		daxDay.setXianh(dax.getDaxxh());
		daxDay.setRiq(day);
		daxDay.setShifgzr("1");
		daxDay.setGongs(Double.valueOf("9999"));
		//大线工作日添加这个工作日
		List<CalcDay>  newcalcDays=calcDays.get(dax.getDaxxh());
		
		String flag="1";
		for (CalcDay calcDay : newcalcDays) {
			if(calcDay.getRiq().equals(daxDay.getRiq())){
				flag="2";				
			}
		}
		if(flag.equals("1")){
			calcDays.get(dax.getDaxxh()).add(daxDay);//为大线添加这个工作日
		}
		CalcDay fenzxDay = new CalcDay();
		daxDay.setXianh(fenzx.getFenzxh());
		fenzxDay.setRiq(day);
		fenzxDay.setShifgzr("1");
		fenzxDay.setGongs(Double.valueOf("23"));
		//分装线工作日添加这个工作日
		calcDays.get(fenzx.getFenzxh()).add(fenzxDay);//为分装线添加这个工作日
	}
	
	/**
	 * 重新生成大线顺序号
	 * @param list
	 */
	private void rebuildDaxsxh(List<Zhuxpcjh> list, String yujjhzsj){
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setDaxsx(i+1);
			list.get(i).setYujjhzsj(yujjhzsj);
		}
	}
	
	/**
	 * 将分装线排产计划加入到结果集合中
	 * @param fenzxh	分装线号
	 * @param riq	日期
	 * @param fenzxpcjhList	分装线排产计划
	 * @param result	key：分装线号		value：（key：日期	value：分装线排产计划）
	 */
	private void addToResult(String fenzxh, String riq, List<Fenzxpcjh> fenzxpcjhList, 
			Map<String, Map<String, List<Fenzxpcjh>>> result){
		if(result.containsKey(fenzxh)){
			Map<String, List<Fenzxpcjh>> fenzxResult= result.get(fenzxh);
			if(fenzxResult.containsKey(riq)){
				fenzxResult.get(riq).addAll(fenzxpcjhList);
			}else{
				fenzxResult.put(riq, fenzxpcjhList);
			}
		}else{
			Map<String, List<Fenzxpcjh>> fenzxResult= new TreeMap<String, List<Fenzxpcjh>>();
			fenzxResult.put(riq, fenzxpcjhList);
			result.put(fenzxh, fenzxResult);
		}
	}
	
	/**
	 * 将分装线排产计划加入到结果集合中
	 * @param fenzxh	分装线号
	 * @param riq	日期
	 * @param fenzxpcjhList	分装线排产计划
	 * @param result	key：分装线号		value：（key：日期	value：分装线排产计划）
	 */
	private void addToZhuxpcjhResult(String fenzxh, String riq, List<Zhuxpcjh> zhuxpcjhList, 
			Map<String, Map<String, List<Zhuxpcjh>>> result){
		if(result.containsKey(fenzxh)){
			Map<String, List<Zhuxpcjh>> zhuxResult= result.get(fenzxh);
			if(zhuxResult.containsKey(riq)){
				zhuxResult.get(riq).addAll(zhuxpcjhList);
			}else{
				zhuxResult.put(riq, zhuxpcjhList);
			}
		}else{
			Map<String, List<Zhuxpcjh>> zhuxResult= new TreeMap<String, List<Zhuxpcjh>>();
			zhuxResult.put(riq, zhuxpcjhList);
			result.put(fenzxh, zhuxResult);
		}
	}
	
	/**
	 * 将相同总成号+备件外销属性+展开日期的备件外销计划集中
	 * @param beijwxjhList
	 */
	private List<Beijwxjh> centralizeBeijwxjh(List<Beijwxjh> beijwxjhList){
		//key:总成号+备件外销属性+展开日期 	value:备件外销计划
		Map<String, List<Beijwxjh>> map = new HashMap<String, List<Beijwxjh>>();
		//集中后list
		List<Beijwxjh> resultList = new ArrayList<Beijwxjh>();
		Iterator<Beijwxjh> iter = beijwxjhList.iterator();
		while(iter.hasNext()){
			Beijwxjh beijwxjh = iter.next();
			String key = beijwxjh.getZongch()+beijwxjh.getBeijwx()+beijwxjh.getZhankrq();//按照同种总成号+备件外销属性+展开日期
			beijwxjh.setTotalCount(beijwxjh.getShul());
			if(map.containsKey(key)){
				List<Beijwxjh> list = map.get(key);
				list.add(beijwxjh);
				for (Beijwxjh temp : list) {
					temp.setTotalCount(temp.getTotalCount()+beijwxjh.getShul());
				}
			}else{
				List<Beijwxjh> list = new ArrayList<Beijwxjh>();
				list.add(beijwxjh);
				map.put(key, list);
			}
		}
		for (String key : map.keySet()) {
			resultList.addAll(map.get(key));
		}
		return resultList;
	}
	
	/**
	 * 根据备件外销计划生成分装线排产计划
	 * @param beijwxjhList
	 * @return
	 */
	private List<Fenzxpcjh> createFenzxpcjhByBeijwxjh(List<Beijwxjh> beijwxjhList, String riq, String leix){
		List<Fenzxpcjh> fenzxpcjhList = new ArrayList<Fenzxpcjh>();
		for (Beijwxjh beijwxjh : beijwxjhList) {
			for (int i = 0; i < beijwxjh.getPaicsl(); i++) {
				Fenzxpcjh fenzxpcjh = new Fenzxpcjh();
				fenzxpcjh.setUsercenter(beijwxjh.getUsercenter());	//用户中心
				fenzxpcjh.setFenzxh(beijwxjh.getFenzxh());			//分装线号
				fenzxpcjh.setYujsxrq(riq);							//预计上线日期
				fenzxpcjh.setYujxxrq(riq);							//预计下线日期
				fenzxpcjh.setFenzch(beijwxjh.getZongch());			//总成号
				fenzxpcjh.setLingjlx(beijwxjh.getLingjlx());		//离线类型
				fenzxpcjh.setLingjyt(beijwxjh.getBeijwx());			//零件用途
				fenzxpcjh.setZhankrq(beijwxjh.getZhankrq());		//展开日期
				fenzxpcjh.setXuqly(beijwxjh.getXuqly());			//需求来源
				fenzxpcjh.setBeijwx(true);							//是否备件外销计划
				fenzxpcjh.setLeix(leix);							//计划类型
				if(!Strings.isNullOrEmpty(beijwxjh.getFiller()) && beijwxjh.getFiller().length()>=3){
				fenzxpcjh.setHanzcx(beijwxjh.getFiller().substring(0, 3));//焊装车型来源于备件外销Filler的1至3位。
				}
				fenzxpcjhList.add(fenzxpcjh);
			}
		}
		return fenzxpcjhList;
	}
	
	/**
	 * 侧围线重新排序，按照零件类型左右配对
	 * @param fenzxpcjhList
	 */
	private List<Fenzxpcjh> sortFenzxpcjh(List<Fenzxpcjh> fenzxpcjhList){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		int l_index = -1;	//L零件角标
		int r_index = -1;	//R零件角标
		while(true){
			l_index = findNextFenzxpcjhIndex(fenzxpcjhList, l_index, "L");	//找到下一个零件类型为L的排产计划
			if(l_index == -1){	//没有零件类型为L的排产计划
				//剩余的排产计划都是R零件类型的
				resultList.addAll(subListByLingjlx(fenzxpcjhList, r_index, "R"));
				break;
			}else{	//有零件类型为L的排产计划，添加进返回结果list中，角标+1
				resultList.add(fenzxpcjhList.get(l_index));
			}
			r_index = findNextFenzxpcjhIndex(fenzxpcjhList, r_index, "R");	//零件类型为R的，逻辑同上
			if(r_index == -1){
				resultList.addAll(subListByLingjlx(fenzxpcjhList, l_index, "L"));
				break;
			}else{
				resultList.add(fenzxpcjhList.get(r_index));
			}
		}
		return resultList;
	}
	
	/**
	 * 侧围线重新排序，按照零件类型左右配对
	 * @param fenzxpcjhList
	 */
	private  void sortFenzxpcjhA(List<Fenzxpcjh> fenzxpcjhList){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		int l_index = -1;	//L零件角标
		int r_index = -1;	//R零件角标
		while(true){
			l_index = findNextFenzxpcjhIndex(fenzxpcjhList, l_index, "L");	//找到下一个零件类型为L的排产计划
			if(l_index == -1){	//没有零件类型为L的排产计划
				//剩余的排产计划都是R零件类型的
				resultList.addAll(subListByLingjlx(fenzxpcjhList, r_index, "R"));
				break;
			}else{	//有零件类型为L的排产计划，添加进返回结果list中，角标+1
				resultList.add(fenzxpcjhList.get(l_index));
			}
			r_index = findNextFenzxpcjhIndex(fenzxpcjhList, r_index, "R");	//零件类型为R的，逻辑同上
			if(r_index == -1){
				resultList.addAll(subListByLingjlx(fenzxpcjhList, l_index, "L"));
				break;
			}else{
				resultList.add(fenzxpcjhList.get(r_index));
			}
		}
		fenzxpcjhList.clear();
		fenzxpcjhList.addAll(resultList);
	}
	
	/**
	 * 侧围线重新排序，按照零件类型左右配对,当左右不匹配时，生成一个空的备件计划，使之形成匹配
	 * @param fenzxpcjhList
	 */
	private List<Fenzxpcjh> sortFenzxpcjhMatching(List<Fenzxpcjh> fenzxpcjhList){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		int l_index = -1;	//L零件角标
		int r_index = -1;	//R零件角标
		while(true){
			l_index = findNextFenzxpcjhIndex(fenzxpcjhList, l_index, "L");	//找到下一个零件类型为L的排产计划
			if(l_index == -1){	//没有零件类型为L的排产计划
				//剩余的排产计划都是R零件类型的
				resultList.addAll(subListByLingjlxMatchingR(fenzxpcjhList, r_index, "R",resultList));
				break;
			}else{	//有零件类型为L的排产计划，添加进返回结果list中，角标+1
				resultList.add(fenzxpcjhList.get(l_index));
			}
			r_index = findNextFenzxpcjhIndex(fenzxpcjhList, r_index, "R");	//零件类型为R的，逻辑同上
			if(r_index == -1){
				resultList.addAll(subListByLingjlxMatchingL(fenzxpcjhList, l_index, "L",resultList));
				break;
			}else{
				resultList.add(fenzxpcjhList.get(r_index));
			}
		}
		return resultList;
	}
	/**
	 * 在分装线排产计划List中，从fromIndex开始起，找到指定零件类型的排产计划
	 * @param fenzxpcjhList
	 * @param fromIndex
	 * @param lingjlx
	 * @return
	 */
	private int findNextFenzxpcjhIndex(List<Fenzxpcjh> fenzxpcjhList, int fromIndex, String lingjlx) {
		for (int i = fromIndex+1; i < fenzxpcjhList.size(); i++) {
			Fenzxpcjh jh = fenzxpcjhList.get(i);
			if(lingjlx.equals(jh.getLingjlx())){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 排产计划List中，从fromIndex开始，截取指定零件类型的排产计划
	 * @param list
	 * @param fromIndex
	 * @param lingjlx
	 * @return
	 */
	private List<Fenzxpcjh> subListByLingjlx(List<Fenzxpcjh> list, int fromIndex, String lingjlx){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		for (int i = fromIndex+1; i < list.size(); i++) {
			Fenzxpcjh jh = list.get(i);
			if(lingjlx.equals(jh.getLingjlx())){
				resultList.add(jh);
			}
		}
		return resultList;
	}

	private List<Fenzxpcjh> subListByLingjlxMatchingL(List<Fenzxpcjh> list, int fromIndex, String lingjlx,List<Fenzxpcjh> result){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		Boolean flag=true;
		for (int i = fromIndex+1; i < list.size(); i++) {
			Fenzxpcjh jh = list.get(i);
			if(lingjlx.equals(jh.getLingjlx())){
				//检验左右是否匹配
				if(result.size()>0 && flag==true){
					if(result.get(result.size()-1).getLingjlx().equalsIgnoreCase("L")){
						MatchingFenzxpcjh(resultList, jh,"R");	
					}
					flag=false;
				}
				resultList.add(jh);
				MatchingFenzxpcjh(resultList, jh,"R");
				
			}
		}
		return resultList;
	}

	private void MatchingFenzxpcjh(List<Fenzxpcjh> resultList, Fenzxpcjh jh,String mark) {
		Fenzxpcjh f = new Fenzxpcjh();
		f.setUsercenter(jh.getUsercenter());	//用户中心
		f.setFenzxh(jh.getFenzxh());			//分装线号
		f.setYujsxrq(jh.getYujsxrq());							//预计上线日期
		f.setYujxxrq(jh.getYujxxrq());							//预计下线日期
		f.setFenzch(INDIVISIBLE_ZONGCH);			//总成号
		f.setLingjlx(mark);		//离线类型
		f.setLingjyt(jh.getLingjyt());			//零件用途
		f.setZhankrq(jh.getZhankrq());		//展开日期
		f.setXuqly(jh.getXuqly());			//需求来源
		f.setBeijwx(true);							//是否备件外销计划
		f.setLeix(jh.getLeix());							//计划类型
		f.setHanzcx(jh.getHanzcx());//焊装车型来源于备件外销Filler的1至3位。
		resultList.add(f);
	}
	
	private List<Fenzxpcjh> subListByLingjlxMatchingR(List<Fenzxpcjh> list, int fromIndex, String lingjlx,List<Fenzxpcjh> result){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		Boolean flag=true;
		for (int i = fromIndex+1; i < list.size(); i++) {
			Fenzxpcjh jh = list.get(i);
			if(lingjlx.equals(jh.getLingjlx())){
				//检验左右是否匹配
				if(result.size()>0 && flag==true){
					if(result.get(result.size()-1).getLingjlx().equalsIgnoreCase("L")){
						MatchingFenzxpcjh(resultList, jh,"R");	
					}
					flag=false;
				}
				MatchingFenzxpcjh(resultList, jh,"L");
				resultList.add(jh);
			}
		}
		return resultList;
	}
	
	/**
	 * 从备件外销计划List中截取指定长度，指定零件类型的备件外销计划
	 * @param list 备件外销计划List
	 * @param maxLingjCount	截取的零件数量
	 * @param lingjlx 零件类型
	 * @param riq 预计上线日期
	 * @return 截取的备件外销计划List
	 */
	private List<Beijwxjh> subBeijwxjh(Dax dax, List<Beijwxjh> list, int maxLingjCount, String lingjlx, String riq){
		List<Beijwxjh> resultList = new ArrayList<Beijwxjh>();
		int remainCount = maxLingjCount;	//未安排的备件外销计划数量
		Iterator<Beijwxjh> iter = list.iterator();
		while(iter.hasNext()){
			Beijwxjh beijwxjh = iter.next();
			if(remainCount == 0){	//备件外销计划截取完成
				break;
			}
			if(!Strings.isNullOrEmpty(lingjlx)){	//侧围线
				if(Strings.isNullOrEmpty(beijwxjh.getLingjlx())){	//零件类型为空
					addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), beijwxjh.getFenzxh(),
							null, "流水号为"+beijwxjh.getLiush()+"的备件外销计划的分装线为侧围线，" +
									"且总成对应的零件类型为空，该备件外销计划将不会被处理", lingjlx));
				}
				if(!lingjlx.equals(beijwxjh.getLingjlx())){	//零件类型与指定的零件类型不一致
					continue;
				}
			}
			if(beijwxjh.getYujjhzsj().compareTo(riq) > 0){	//备件外销计划不能提前插入
				continue;
			}
			if((beijwxjh.getShul() - beijwxjh.getWancsl()) <= remainCount){	//可完成该备件外销计划
				beijwxjh.setPaicsl(beijwxjh.getShul() - beijwxjh.getWancsl());	//备件外销计划的实际排产数量
				resultList.add(beijwxjh);
				iter.remove();
				remainCount = remainCount - (beijwxjh.getShul() - beijwxjh.getWancsl());
			}else{	//只能完成该备件外销计划的一部分
				Beijwxjh subBean;
				try {
					subBean = (Beijwxjh) BeanUtils.cloneBean(beijwxjh);
					subBean.setPaicsl(remainCount);
					subBean.setWancsl(subBean.getShul()-remainCount);
					resultList.add(subBean);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				beijwxjh.setWancsl(beijwxjh.getWancsl() + remainCount);
				remainCount = 0;
			}
		}
		return resultList;
	}
	
	/**
	 * 从备件外销计划List中截取指定长度，指定分装线系数的备件外销计划
	 * @param dax
	 * @param list
	 * @param maxLingjCount
	 * @param xis
	 * @return
	 */
	private List<Beijwxjh> subBeijwxjh(Dax dax, List<Beijwxjh> list, Map<String, Integer> countMap, int xis, String riq){
		List<Beijwxjh> subList = new ArrayList<Beijwxjh>();
		if (xis == 1) { // 一般分装线
			subList.addAll(subBeijwxjh(dax, list, countMap.get("maxLingjCount"), "", riq)); // 截取
		} else if (xis == 2) { // 侧围线
			subList.addAll(subBeijwxjh(dax, list, countMap.get("maxLingjCount_L"), "L", riq));
			subList.addAll(subBeijwxjh(dax, list, countMap.get("maxLingjCount_R"), "R", riq));
		}
		return subList;
	}
	
	/**
	 * 获取分装线当前日期信息
	 * @param days 分装线日期信息
	 * @return
	 */
	private CalcDay getFenzxCurrentCalcDay(List<CalcDay> days, String riq){
		for (CalcDay day : days) {
			if(day.getRiq().compareTo(riq) == 0){
				return day;
			}
		}
		return null;
	}
	
	/**
	 * 获取大线下一个工作日
	 * @param dax 大线信息
	 * @return 日期
	 */
	private String getNextWorkgingDay(String daxxh, String riq){
		List<CalcDay> days = calcDays.get(daxxh);
		for (int i = 0; i < days.size(); i++) {
			CalcDay day = days.get(i);
			if(day.getRiq().compareTo(riq) == 0 && i != calcDays.size()){	//找到当天且不是最后一天
				for (int j = i+1; j < days.size(); j++) {	//循环当天后面的日期
					CalcDay targetDay = days.get(j);
					if(targetDay.getShifgzr().equals("1")){	//为工作日
						return targetDay.getRiq();
					}
				}
				return null;	//后面的日期都不是工作日
			}
		}
		return null;
	}
	
	/**
	 * 得到产线指定日期，指定提前日期后的工作日
	 * @param fenzxh	分装线号
	 * @param riq	指定日期
	 * @param addDays	提前天数
	 * @return
	 */
	private CalcDay getChanxWorkingDay(String xianh, String riq, int addDays){
		List<CalcDay> days = calcDays.get(xianh);
		CalcDay resultDay = new CalcDay();
		for (int i = 0; i < days.size(); i++) {
			if(days.get(i).getRiq().equals(riq)){
				resultDay = days.get(i);
				if(addDays > 0){
					for (int j = i+1; j < days.size(); j++) {
						if(days.get(j).getShifgzr().equals("1")){
							addDays--;
							if(addDays == 0){
								return days.get(j);
							}
						}
					}
					return days.get(days.size() - 1);
				}else if(addDays < 0){
					for (int j = i-1; j >= 0; j--) {
						if(days.get(j).getShifgzr().equals("1")){
							addDays++;
							if(addDays == 0){
								return days.get(j);
							}
						}
					}
					return days.get(0);
				}
			}
		}
		return resultDay;
	}
	
	/**
	 * 大线排空，生成空的主线排产计划
	 * @param dax
	 * @param zhuxpcjhList
	 * @param paicslMap
	 * @return
	 */
	private List<Zhuxpcjh> addEmptyZhuxpcjh(Dax dax, List<Zhuxpcjh> zhuxpcjhList, Map<String, List<Paicsl>> paicslMap, List<Zhuxpcjh> saveZhuxpcjhList){
		//大线排空生成的主线排产计划
		List<Zhuxpcjh> list = new ArrayList<Zhuxpcjh>();
		if(paicslMap.get(currentDay.getRiq()) != null && zhuxpcjhList != null){		//排产数量和主线计划都不为空
			for (Paicsl paicsl : paicslMap.get(currentDay.getRiq())) {	
				if(paicsl.getXianh().equals(dax.getDaxxh())){	//找到大线的排产数量
					Integer jihsxl = paicsl.getJihsxl();	//计划上线量
					Integer jihxxl = paicsl.getJihxxl();	//计划下线量
					//计划上线量<计划下线量且为焊装大线
					if(jihsxl != null && jihxxl != null && jihsxl < jihxxl && dax.getDaxlx().equals("1")){
						log.info("大线"+dax.getDaxxh()+"排空，数量为："+ (jihxxl - jihsxl)+"...");
						int daxsx = zhuxpcjhList.get(zhuxpcjhList.size()-1).getDaxsx();
						int number = 0;
						//截取5位年月日	2015-10-01	51001
						String riq = currentDay.getRiq();
						riq = riq.substring(3, 4)+riq.substring(5, 7)+riq.substring(8, 10);
						for (int i = 0; i < jihxxl - jihsxl; i++) {
							daxsx++;
							number++;
							Zhuxpcjh jh = new Zhuxpcjh();
							String yujjhzsj = currentDay.getRiq().substring(0, 4)+currentDay.getRiq().substring(5, 7)+currentDay.getRiq().substring(8, 10);
							jh.setYujjhzsj(yujjhzsj);	//预计进焊装时间
							jh.setDaxsx(daxsx);						//大线顺序号
							jh.setUsercenter(dax.getUsercenter());	//用户中心
							jh.setDaxxh(dax.getDaxxh());			//大线线号
							jh.setXuqly("1");						//需求来源
							//总顺序号=K+5位年月日+4位顺序号
							String zongsxh = "K"+riq + String.format("%04d", number);
							jh.setDaxzsx_lixq(zongsxh);				//大线总顺序（离线前）
							jh.setDaxzsx_lixh(zongsxh);				//大线总顺序（离线后）
							jh.setZongch(DEFAULT_ZONGCH);					//排空默认总成号
							jh.setSpiltFinished(false);		//是否拆分
							jh.setCreate_time(CommonFun.getJavaTime());
							jh.setCreator(creator);
							jh.setDingdh("temp_" + riq + "-" + String.format("%04d", number));	//排空订单判断标记，不保存订单号
							list.add(jh);
						}
						zhuxpcjhList.addAll(list);
					}
				}
			}
			if(currentDay.getRiq().compareTo(paickssj) >= 0 && currentDay.getRiq().compareTo(paicjssj) <= 0){
				saveZhuxpcjhList.addAll(list);	//只保存排产日期范围内的排空主线计划
			}
		}
		return list;
	}
	
	/**
	 * 检查大线是否能完成当日的所有排产计划
	 * @param dax
	 * @param zhuxpcjhMap
	 * @throws ParseException 
	 */
	private void checkWorkingDayZhuxpcjh(Dax dax, Map<String, List<Zhuxpcjh>> zhuxpcjhMap, Map<String, List<Paicsl>> paicslMap) throws ParseException{
		log.info("大线"+dax.getDaxxh()+"在"+currentDay.getRiq()+"工作日，检查是否能完成当日的所有排产计划...");
		List<Zhuxpcjh> todayZhuxpcjh = zhuxpcjhMap.get(currentDay.getRiq());	//当日的主线排产计划
		Paicsl paicsl = findPaicsl(paicslMap.get(currentDay.getRiq()), dax.getDaxxh());
		//大线排产数量的上线量与当日SPPV传来的大线计划数量不一致
		if(paicsl != null && todayZhuxpcjh != null && paicsl.getJihsxl() != todayZhuxpcjh.size()){
			addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), currentDay.getRiq(), 
					"大线"+dax.getDaxxh()+"在"+currentDay.getRiq()+"的排产计划数量为"+todayZhuxpcjh.size()+
					"，与预计上线量"+paicsl.getJihsxl()+"不一致！", creator));
		}
		if(todayZhuxpcjh != null && todayZhuxpcjh.size() != 0){	//主线排产计划不为空
			//排产计划数量 > 大线工时 * 大线节拍
			if(todayZhuxpcjh.size() > dax.getShengcjp() * currentDay.getGongs()){
				//String nextWorkingday = getNextWorkgingDay(dax.getDaxxh(), currentDay.getRiq());	
				//获取下一个SPPV工作日
				String nextWorkingday = addDaysByZhuxjh(currentDay.getRiq(), 1);	
				if(nextWorkingday != null && !currentDay.getRiq().equals(nextWorkingday)){
					//第二个工作日的主线排产计划
					List<Zhuxpcjh> nextDayZhuxpcjh = zhuxpcjhMap.get(nextWorkingday);
					if(nextDayZhuxpcjh != null){
						//超出的主线排产计划
						List<Zhuxpcjh> extraZhuxpcjh = todayZhuxpcjh.subList(
								(int)(dax.getShengcjp() * currentDay.getGongs()), todayZhuxpcjh.size());	
						//第二个工作日加上当日超出的主线计划
						nextDayZhuxpcjh.addAll(0, extraZhuxpcjh);
						//重新生成第二个工作日的大线顺序号
						rebuildDaxsxh(nextDayZhuxpcjh , nextWorkingday);
						//当日移除多余的主线计划
						removeList(todayZhuxpcjh, (int)(dax.getShengcjp() * currentDay.getGongs()), todayZhuxpcjh.size());
						addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), currentDay.getRiq(), 
								"大线"+dax.getDaxxh()+"在"+currentDay.getRiq()+"的排产计划数量超出了产能，调整后的数量为"+todayZhuxpcjh.size()+
								"，超出的部分将在第二个工作日完成", creator));
					}else{
						nextDayZhuxpcjh=new ArrayList<Zhuxpcjh>();
						//超出的主线排产计划
						List<Zhuxpcjh> extraZhuxpcjh = todayZhuxpcjh.subList(
								(int)(dax.getShengcjp() * currentDay.getGongs()), todayZhuxpcjh.size());	
						//第二个工作日加上当日超出的主线计划
						nextDayZhuxpcjh.addAll(0, extraZhuxpcjh);
						zhuxpcjhMap.put(nextWorkingday, nextDayZhuxpcjh);//放入主线计划中
						//重新生成第二个工作日的大线顺序号
						rebuildDaxsxh(nextDayZhuxpcjh , nextWorkingday);
						//当日移除多余的主线计划
						removeList(todayZhuxpcjh, (int)(dax.getShengcjp() * currentDay.getGongs()), todayZhuxpcjh.size());
						addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), currentDay.getRiq(), 
								"大线"+dax.getDaxxh()+"在"+currentDay.getRiq()+"的排产计划数量超出了产能，调整后的数量为"+todayZhuxpcjh.size()+
								"，超出的部分将在第二个工作日完成", creator));
					}
				}
			}
		}
	}
	
	/**
	 * 计算离线点各段大线顺序
	 * @param dax	大线
	 * @param fenzxMap	分装线
	 * @param zhuxpcjhList	大线排产计划
	 * @param lixdMap	离线点
	 * @return	分装线对应的大线顺序
	 * @throws ParseException 
	 */
	private void calcEveryPartDaxsx(Dax dax, Map<String, Fenzx> fenzxMap, 
			Map<String, List<Zhuxpcjh>> zhuxpcjhMap, Map<String, Lixd> lixdMap, 
			Map<String, Map<String, List<Zhuxpcjh>>> fenzx2zhuxpcjhMap) throws ParseException{
		log.info("大线"+dax.getDaxxh()+"计算离线点各段的大线顺序...");
		List<CalcDay> dayList = calcDays.get(dax.getDaxxh());
		//离线点对应的主线排产计划	key：日期	value：（key：离线点	value：主线排产计划）
		Map<String, Map<String, List<Zhuxpcjh>>> lixdZhuxpcjhMap = new HashMap<String, Map<String,List<Zhuxpcjh>>>();
		//初始化离线点各段的主线顺序
		Map<String, Map<String, List<Zhuxpcjh>>> initLixdZhuxpcjh= initLixdZhuxpcjh(dax, zhuxpcjhMap, lixdMap, dayList);
		//得到离线点各段的主线顺序，从最后一天开始算
		for (int k = dayList.size() - 1; k >=0 ; k--) {
			Map<String, List<Zhuxpcjh>> todayLixdZhuxpcjh = new TreeMap<String, List<Zhuxpcjh>>();
			CalcDay day = dayList.get(k);
			//当日的主线排产计划
			List<Zhuxpcjh> todayZhuxpcjh = zhuxpcjhMap.get(day.getRiq());
			if(todayZhuxpcjh != null && todayZhuxpcjh.size() != 0){
				//检查主线排产计划的离线点在参考系中是否存在
				checkLixd(dax, todayZhuxpcjh, new ArrayList<Lixd>(lixdMap.values()), day.getRiq());
				//取出大线离线点，并按照对应消耗点的车身数量降序排序
				List<Lixd> lixdList = sortLixd(dax.getDaxxh(), lixdMap);
				//当前离线点
				Lixd currLixd = null;
				todayLixdZhuxpcjh.put("original", cloneList(todayZhuxpcjh));	//原始顺序
				if(lixdList.size() > 0){	//有离线点
					for (int i = 0; i < lixdList.size(); i++) {	//循环大线离线点
						currLixd = lixdList.get(i);
						//将该离线点之前的离线点对应的大线顺序都提前
						for (int j = i; j < lixdList.size(); j++) {
							Lixd beforeLixd = lixdList.get(j);
							//大线离线点提前
							putAheadOfDaxLixd(initLixdZhuxpcjh.get(beforeLixd.getLixd()), currLixd, day);
						}
						todayLixdZhuxpcjh.put(currLixd.getLixd(), initLixdZhuxpcjh.get(currLixd.getLixd()).get(day.getRiq()));
					}
				}
				lixdZhuxpcjhMap.put(day.getRiq(), todayLixdZhuxpcjh);
			}
		}
		
		//找到每个分装线对应的大线顺序
		for (CalcDay day : dayList) {
			//当日的离线点对应的主线排产计划
			Map<String, List<Zhuxpcjh>> todayLixdZhuxpcjh = lixdZhuxpcjhMap.get(day.getRiq());
			if(todayLixdZhuxpcjh != null && todayLixdZhuxpcjh.size() != 0){
				List<Lixd> lixdList = sortLixd(dax.getDaxxh(), lixdMap);
				//下一个离线点
				Lixd nextLixd = null;
				//当前离线点
				Lixd currLixd = null;
				if(lixdList.size() > 0){
					for (int i = 0; i < lixdList.size(); i++) {//循环大线离线点
						currLixd = lixdList.get(i);
						if(i == 0){	//原始顺序，对应在最远离线点之后的分装线
							currLixd = new Lixd();
							currLixd.setChessl(99999999);
							nextLixd = lixdList.get(0);
							findFenzxZhuxpcjhRelation(day.getRiq(), todayLixdZhuxpcjh.get("original"), currLixd, nextLixd, fenzxMap, fenzx2zhuxpcjhMap);
							currLixd = lixdList.get(i);
						}
						if(i == lixdList.size() - 1){	//已经是最后一个离线点，虚拟出下一个离线点
							nextLixd = new Lixd();
							nextLixd.setChessl(-99999999);
						}else{
							nextLixd = lixdList.get(i+1);
						}
						//生成分装线、大线计划的对应关系
						findFenzxZhuxpcjhRelation(day.getRiq(), todayLixdZhuxpcjh.get(currLixd.getLixd()), currLixd, nextLixd, fenzxMap, fenzx2zhuxpcjhMap);
					}	
				}else{	//无离线点
					for (Entry<String, Fenzx> entry : fenzxMap.entrySet()) {
						Fenzx fenzx = entry.getValue();
						addToZhuxpcjhResult(fenzx.getFenzxh(), day.getRiq(), todayLixdZhuxpcjh.get("original"), fenzx2zhuxpcjhMap);//生成分装线、大线计划对应关系
					}
				}
			}
		}
		lixdZhuxpcjhMap.clear();
		initLixdZhuxpcjh.clear();
	}
	
	/**
	 * 初始化离线点各段的主线顺序（都添加原始顺序）
	 * @param dax
	 * @param zhuxpcjhMap
	 * @param lixdMap
	 * @param fenzx2zhuxpcjhMap
	 * @param dayList
	 * @param lixdZhuxpcjhMap
	 */
	private Map<String, Map<String, List<Zhuxpcjh>>> initLixdZhuxpcjh(Dax dax, Map<String, List<Zhuxpcjh>> zhuxpcjhMap, 
			Map<String, Lixd> lixdMap, List<CalcDay> dayList){
		Map<String, Map<String, List<Zhuxpcjh>>> lixdZhuxpcjhMap = new HashMap<String, Map<String,List<Zhuxpcjh>>>();
		for (CalcDay day : dayList) {
			//当日的主线排产计划
			List<Zhuxpcjh> todayZhuxpcjh = zhuxpcjhMap.get(day.getRiq());
			if(todayZhuxpcjh != null && todayZhuxpcjh.size() != 0){
				//取出大线离线点，并按照对应消耗点的车身数量降序排序
				List<Lixd> lixdList = sortLixd(dax.getDaxxh(), lixdMap);
				//当前离线点
				Lixd currLixd = null;
				addToZhuxpcjhResult("original", day.getRiq(), cloneList(todayZhuxpcjh), lixdZhuxpcjhMap); 	//原始顺序
				if(lixdList.size() > 0){	//有离线点
					for (int i = 0; i < lixdList.size(); i++) {	//循环大线离线点
						currLixd = lixdList.get(i);
						addToZhuxpcjhResult(currLixd.getLixd(), day.getRiq(), cloneList(todayZhuxpcjh), lixdZhuxpcjhMap);
					}
				}
			}
		}
		return lixdZhuxpcjhMap;
	}
	
	/**
	 * copy list
	 * @param list
	 * @return
	 */
	private List<Zhuxpcjh> cloneList(List<Zhuxpcjh> list){
		List<Zhuxpcjh> resultList = new ArrayList<Zhuxpcjh>();
		if(list != null && list.size() != 0){
			for (Zhuxpcjh zhuxpcjh : list) {
				try {
					resultList.add((Zhuxpcjh) BeanUtils.cloneBean(zhuxpcjh));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 找到分装线、大线计划的对应关系
	 * @param todayZhuxpcjh	当日的大线计划
	 * @param currLixd	当前离线点
	 * @param nextLixd	下一个离线点
	 * @param fenzxMap	大线所有的分装线
	 * @return	分装线、大线计划对应关系
	 */
	private void findFenzxZhuxpcjhRelation(String riq, List<Zhuxpcjh> todayZhuxpcjh,
			Lixd currLixd, Lixd nextLixd, Map<String, Fenzx> fenzxMap, 
			Map<String, Map<String, List<Zhuxpcjh>>> fenzx2zhuxpcjhMap){
		for (Entry<String, Fenzx> entry : fenzxMap.entrySet()) {	//循环分装线
			Fenzx fenzx = entry.getValue();
			if(fenzx.getChessl() < currLixd.getChessl() //分装线处于2个离线点之间（依靠车身数量判断）
					&& fenzx.getChessl() >= nextLixd.getChessl()){
				addToZhuxpcjhResult(fenzx.getFenzxh(), riq, todayZhuxpcjh, fenzx2zhuxpcjhMap);//生成分装线、大线计划对应关系
			}
		}
	}
	
	/**
	 * 检查主线排产计划的离线点在参考系中是否存在
	 * @param dax
	 * @param zhuxpcjhList
	 * @param ckxLixd
	 */
	private void checkLixd(Dax dax, List<Zhuxpcjh> zhuxpcjhList, List<Lixd> ckxLixd, String riq){
		Set<String> zhuxpcjhLixd = findZhuxpcjhLixd(zhuxpcjhList);
		for (String jh_lixd : zhuxpcjhLixd) {	//循环主线排产计划的离线点
			boolean flag = false;
			for (Lixd ckx_lixd : ckxLixd) {		//参考系离线点中找到该离线点
				if(jh_lixd.equals(ckx_lixd.getLixd())){
					flag = true;
					break;
				}
			}
			if(!flag){	//主线排产计划的离线点在参考系中不存在
				//添加报警信息
				addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), riq,
						"离线点"+jh_lixd+"在参考系不存在，有关该离线点的提前将不会处理", creator));
			}
		}
	}
	
	/**
	 * 找到主线排产计划需要提前的离线点
	 * @param zhuxpcjhList
	 * @return
	 */
	private Set<String> findZhuxpcjhLixd(List<Zhuxpcjh> zhuxpcjhList){
		//封装后的主线排产计划
		Set<String> resultSet = new HashSet<String>();
		if(zhuxpcjhList != null){
			for (Zhuxpcjh zhuxpcjh : zhuxpcjhList) {
				String key = zhuxpcjh.getLixd();
				if(!Strings.isNullOrEmpty(key) && zhuxpcjh.getTiqcw() != null){	//离线点、提前车位数不为空
					resultSet.add(key);
				}
			}
		}
		return resultSet;
	}
	
	/**
	 * 取出离线点，并按照对应消耗点的车身数量降序排序
	 * @param xianh
	 * @param lixdMap
	 * @return
	 */
	private List<Lixd> sortLixd(String xianh, Map<String, Lixd> lixdMap){
		List<Lixd> resultList = new ArrayList<Lixd>();
		for (Entry<String, Lixd> entry : lixdMap.entrySet()) {
			Lixd lixd = entry.getValue();
			if(lixd.getXianh().equals(xianh)){
				resultList.add(lixd);
			}
		}
		//按照对应消耗点的车身数量降序排序
		Collections.sort(resultList);
		return resultList;
	}
	
	/**
	 * 大线离线点提前
	 * @param zhuxpcjhList
	 * @param lixd
	 * @throws ParseException 
	 */
	private void putAheadOfDaxLixd(Map<String, List<Zhuxpcjh>> zhuxpcjhMap, Lixd lixd, CalcDay day) throws ParseException{
		// 当日的主线排产计划
		List<Zhuxpcjh> zhuxpcjhList = zhuxpcjhMap.get(day.getRiq());
		// 所有的主线排产计划
		List<Zhuxpcjh> totalList = getZhuxpcjhList(zhuxpcjhMap);
		if(zhuxpcjhList != null){
			for (int i = 0; i < zhuxpcjhList.size(); i++) {
				Zhuxpcjh jh = zhuxpcjhList.get(i);
				if(lixd.getLixd().equals(jh.getLixd())){
					// 实际提前车位数（越过空订单和未拆分订单）
					int tiqcw = findPracticalTiqcwByZhuxjh(totalList, jh.getDingdh(), jh.getTiqcw());
					// 有离线点的排产计划：大线顺序=大线顺序-提前车位
					jh.setDaxsx(jh.getDaxsx() - tiqcw);
					// 受影响的排产计划：大线顺序=大线顺序+1
					int startIndex = i - tiqcw < 0 ? 0 : i - tiqcw;
					for (int j = startIndex; j < i && j >= 0; j++) {
						Zhuxpcjh temp= zhuxpcjhList.get(j);
						temp.setDaxsx(temp.getDaxsx() + 1);
					}
					// 按照大线顺序号重新排序
					Collections.sort(zhuxpcjhList);
					// 确保大线顺序大于0
					ensureDaxsx(zhuxpcjhMap, jh);
				}
			}
		}
	}
	
	/**
	 * 找到实际的提前车位数（要越过空订单和未拆分订单）
	 * @param totalList
	 * @param dingdh
	 * @param tiqcw
	 * @return
	 */
	private int findPracticalTiqcwByZhuxjh(List<Zhuxpcjh> totalList, String dingdh, int tiqcw){
		if(dingdh == null)	
			return 0;
		// 待提前计划Index
		int dingdIndex = -1;
		Zhuxpcjh jh;
		for (int i = 0; i < totalList.size(); i++) {
			jh = totalList.get(i);
			if(dingdh.equals(jh.getDingdh())){
				dingdIndex = i;
				break;
			}
		}
		// 仍未提前车位数
		int remain_tiqcw = tiqcw;
		// 实际提前车位数
		int practical_tiqcw = 0;
		for (int i = dingdIndex; i >= 0 && remain_tiqcw > 0; i--, practical_tiqcw++) {
			jh = totalList.get(i);
			// 不是默认总成或未拆分总成，才算作提前一个车位
			if(!isDefaultOrIndivisibleZongc(jh.getZongch())){
				remain_tiqcw--;
			}
		}
		return practical_tiqcw;
	}
	
	/**
	 * 判断总成号是否为默认总成或未拆分总成
	 * @param zongch
	 * @return
	 */
	private boolean isDefaultOrIndivisibleZongc(String zongch){//未拆分算产能2016.10.15
//		return zongch == null ? false : 
//			zongch.equals(DEFAULT_ZONGCH) || zongch.equals(INDIVISIBLE_ZONGCH);
		return zongch == null ? false : 
			zongch.equals(DEFAULT_ZONGCH);
	}
	
	/**
	 * 判断是否是一个新的且有效的订单（订单号和前一个计划的不一样，且总成号不是排空或未拆分生成的）
	 * @param zongch 总成号
	 * @param curr_dingdh	当前订单
	 * @param last_dingdh	上一个订单
	 * @return
	 */
	private boolean isDifferentAndExistDingd(String curr_zongch, String curr_dingdh, 
			String last_zongch, String last_dingdh) {
		return !isDefaultOrIndivisibleZongc(curr_zongch)
				&& (curr_dingdh == null || isDefaultOrIndivisibleZongc(last_zongch) || !curr_dingdh.equals(last_dingdh));
	}
	/**
	 * 判断是否是一个新的且有效的订单（订单号和前一个计划的不一样，且总成号不是排空）---未拆分算产能
	 * @param zongch 总成号
	 * @param curr_dingdh	当前订单
	 * @param last_dingdh	上一个订单
	 * @return
	 */
//	private boolean isDifferentAndExistDingdEmpty(String curr_zongch, String curr_dingdh, 
//			String last_zongch, String last_dingdh) {
//		return !(curr_zongch == null ? false : 
//			curr_zongch.equals(DEFAULT_ZONGCH))
//				&& (curr_dingdh == null || (last_zongch == null ? false : 
//					last_zongch.equals(DEFAULT_ZONGCH)) || !curr_dingdh.equals(last_dingdh));
//	}
	
	/**
	 * 将每日的大线计划map汇总为一个list
	 * @param zhuxpcjhMap
	 * @return
	 */
	private List<Zhuxpcjh> getZhuxpcjhList(Map<String, List<Zhuxpcjh>> zhuxpcjhMap){
		// 所有的大线计划
		List<Zhuxpcjh> totalList = new ArrayList<Zhuxpcjh>();
		// 当日的大线计划
		List<Zhuxpcjh> dayList;
		for (String date : zhuxpcjhMap.keySet()) {
			dayList = zhuxpcjhMap.get(date);
			if(dayList != null && !dayList.isEmpty()){
				totalList.addAll(dayList);
			}
		}
		return totalList;
	}
	
	/**
	 * 确保大线顺序大于0
	 * @param zhuxpcjhList
	 * @param jh
	 * @throws ParseException
	 */
	private void ensureDaxsx(Map<String, List<Zhuxpcjh>>zhuxpcjhList, Zhuxpcjh jh) throws ParseException{
		if(jh.getDaxsx() <= 0){
			String currentDay = jh.getYujjhzsj();	//当前日期
			String previousDay = addDaysByZhuxjh(currentDay, -1);	//前一SPPV工作日
//			String previousDay = getChanxWorkingDay(jh.getDaxxh(), currentDay, -1).getRiq();	//前一大线工作日
			List<Zhuxpcjh> todayZhuxpcjh = zhuxpcjhList.get(currentDay);	//当日的SPPV主线计划
			List<Zhuxpcjh> priviousZhuxpcjh = zhuxpcjhList.get(previousDay);	//前一日的SPPV主线计划
			if(previousDay.compareTo(currentDay) < 0 && priviousZhuxpcjh != null){	//有前一日
				jh.setYujjhzsj(previousDay);
				jh.setDaxsx(priviousZhuxpcjh.size() + jh.getDaxsx());
				int startIndex = jh.getDaxsx() - 1 < 0 ? 0 : jh.getDaxsx() - 1;
				for (int i = startIndex; i < priviousZhuxpcjh.size() ; i++) {
					Zhuxpcjh effectiveJh = priviousZhuxpcjh.get(i);
					//受影响的排产计划：大线顺序=大线顺序+1
					effectiveJh.setDaxsx(effectiveJh.getDaxsx() + 1);
					if(i == priviousZhuxpcjh.size() - 1){	//前一日的最后一个主线排产计划
						effectiveJh.setYujjhzsj(currentDay);
						effectiveJh.setDaxsx(1);
					}
				}
				//将当日的第一个大线排产计划与前一日的最后一个大线排产计划交换
				todayZhuxpcjh.remove(0);	
				todayZhuxpcjh.add(0, priviousZhuxpcjh.get(priviousZhuxpcjh.size() - 1));
				jh.setTiqcw(0);
				priviousZhuxpcjh.add(jh);
				//将前一日的大线排产计划按照大线顺序重新排序
				Collections.sort(priviousZhuxpcjh);
				priviousZhuxpcjh.remove(priviousZhuxpcjh.size() - 1);
				//大线顺序<0，继续提前（离线点提前多个工作日情况）
				if(jh.getDaxsx() <= 0){
					ensureDaxsx(zhuxpcjhList, jh);
				}
			}else{	//已经是计算日期的第一日，无前一工作日
				return;
			}
		}
	}
	
	
	/**
	 * 分装线离线提前，确保上线顺序>0
	 * @param fenzxpcjhList
	 * @param jh
	 * @throws ParseException
	 */
	private void ensureShangxsx(Map<String, List<Fenzxpcjh>> fenzxpcjhList, Fenzxpcjh jh, Fenzx fenzx) throws ParseException{
		if(jh.getShangxsx() <= 0){
			String currentDay = jh.getYujsxrq();	//当前日期
			String previousDay = addDaysByZhuxjh(currentDay, -1);	//前一SPPV工作日
//			String previousDay = getChanxWorkingDay(fenzx.getDaxxh(), currentDay, -1).getRiq();	//前一大线工作日
			List<Fenzxpcjh> todayFenzxpcjh = fenzxpcjhList.get(currentDay);	//当日的分装线排产计划
			List<Fenzxpcjh> priviousFenzxpcjh = fenzxpcjhList.get(previousDay);	//前一日的分装线排产计划
			if(previousDay.compareTo(currentDay) < 0 && priviousFenzxpcjh != null && priviousFenzxpcjh.size()>0){	//有前一日
				jh.setYujsxrq(previousDay);
				jh.setShangxsx(priviousFenzxpcjh.size() + jh.getShangxsx());
				int startIndex = jh.getShangxsx() - 1 < 0 ? 0 : jh.getShangxsx() - 1;
				for (int i = startIndex; i < priviousFenzxpcjh.size() ; i++) {
					Fenzxpcjh effectiveJh = priviousFenzxpcjh.get(i);
					//受影响的排产计划：上线顺序=上线顺序+1
					effectiveJh.setShangxsx(effectiveJh.getShangxsx() + 1);
					if(i == priviousFenzxpcjh.size() - 1){	//前一日的最后一个分装线排产计划
						effectiveJh.setYujsxrq(currentDay);
						if(effectiveJh.getYujsxrq().compareTo(effectiveJh.getYujxxrq()) > 0){
							effectiveJh.setYujxxrq(currentDay);
						}
						effectiveJh.setShangxsx(1);
					}
				}
					//将当日的第一个分装线排产计划与前一日的最后一个分装线排产计划交换
					todayFenzxpcjh.remove(0);	
					priviousFenzxpcjh.add(jh);
					//将前一日的分装线排产计划按照上线顺序重新排序
					Collections.sort(priviousFenzxpcjh);
					priviousFenzxpcjh.get(priviousFenzxpcjh.size() - 1).setXixldjh("1");//被挤过来的计划
					todayFenzxpcjh.add(0, priviousFenzxpcjh.get(priviousFenzxpcjh.size() - 1));
					priviousFenzxpcjh.remove(priviousFenzxpcjh.size() - 1);
				//上线顺序<0，继续提前（离线点提前多个工作日情况）
				if(jh.getShangxsx() <= 0){
					ensureShangxsx(fenzxpcjhList, jh, fenzx);
				}
			}
			else if(priviousFenzxpcjh!=null &&  priviousFenzxpcjh.size() == 0  ){
			
				todayFenzxpcjh.remove(0);	//把当天的第一个计划删掉
				jh.setYujsxrq(previousDay);//把离线的计划的上线日期设为前一个工作日日期
				priviousFenzxpcjh.add(jh);
				//将前一日的分装线排产计划按照上线顺序重新排序
				Collections.sort(priviousFenzxpcjh);
				String newpreviousDay = addDaysByZhuxjh(previousDay, -1);	//前一天的前一个工作日
				List<Fenzxpcjh> newpriviousFenzxpcjh = fenzxpcjhList.get(newpreviousDay);	//前一日的分装线排产计划
				//有前一天的前一个工作日，并且它的计划不为空
				if(newpreviousDay.compareTo(previousDay)<0  && newpriviousFenzxpcjh.size()>0){			
					todayFenzxpcjh.add(0, newpriviousFenzxpcjh.get(newpriviousFenzxpcjh.size() - 1));//当前日期中加入前前天的最后一个计划
					todayFenzxpcjh.get(0).setYujsxrq(currentDay);//然后设置它的上线日期为当前日期
					newpriviousFenzxpcjh.remove(newpriviousFenzxpcjh.size() - 1);//从前前天中把最后一个计划删除
				}
				ensureShangxsx(fenzxpcjhList, jh, fenzx);//从新调用该方法判断该离线点是否要需要提前
			}
			else{	//已经是计算日期的第一日，无前一工作日
				return;
			}
		}
	}
	
	/**
	 * 根据大线计划生成分装线排产计划
	 * @param fenzx	分装线
	 * @param lixdMap	离线点
	 * @param zhuxpcjhList	大线计划
	 * @param pdsCfxxMap	PDS拆分信息
	 * @return	分装线排产计划
	 */
	private List<Fenzxpcjh> createFenzxpcjhByZhuxpcjh(Fenzx fenzx, Map<String, Lixd> lixdMap,
			List<Zhuxpcjh> zhuxpcjhList, Map<String, List<Zongccfjg>> pdsCfxxMap){
		//分装线排产计划
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		if(zhuxpcjhList != null && zhuxpcjhList.size() != 0){
			for (int i = 0; i < zhuxpcjhList.size(); i++) {
				Zhuxpcjh zhuxpcjh = zhuxpcjhList.get(i);
				if(DEFAULT_ZONGCH.equals(zhuxpcjh.getZongch())){	
					// 创建排空生成的分装线排产计划
					createEmptyFenzxpcjh(fenzx, resultList, zhuxpcjh);
				}else{
					// 创建非排空生成的分装线排产计划
					createNotEmptyFenzxpcjh(fenzx, lixdMap, pdsCfxxMap, resultList, zhuxpcjh);
				}
			}
			if(fenzx.getFenzxxs() == 2){
				// 侧围线重新排序
				resultList = sortFenzxpcjh(resultList);
			}
		}
		log.info("分装线"+fenzx.getFenzxh()+"根据大线计划生成"+currentDay.getRiq()+"分装线排产计划，数量为"+resultList.size());
		return resultList;
	}

	/**
	 * 创建非排空生成的分装线排产计划
	 * @param fenzx
	 * @param lixdMap
	 * @param pdsCfxxMap
	 * @param resultList
	 * @param zhuxpcjh
	 */
	private void createNotEmptyFenzxpcjh(Fenzx fenzx,
			Map<String, Lixd> lixdMap, Map<String, List<Zongccfjg>> pdsCfxxMap,
			List<Fenzxpcjh> resultList, Zhuxpcjh zhuxpcjh) {
		List<Zongccfjg> cfjgList = pdsCfxxMap.get(zhuxpcjh.getZongch()+zhuxpcjh.getZhankrq());	//总成的拆分结果
		int spiltCount = 0;
		String lingjlx = "";
		if(cfjgList != null && cfjgList.size() != 0){
			for (Zongccfjg zongccfjg : cfjgList) {
				if(fenzx.getFenzxh().equals(zongccfjg.getFenzxh())){	//拆分到该分装线上
					spiltCount ++;
					lingjlx = zongccfjg.getLingjlx();
					//标记主线计划可拆分
					zhuxpcjh.setSpiltFinished(true);
					//系数有多少，就拆分为多少个分装线排产计划
					if(zongccfjg.getXis()>=1){
						Fenzxpcjh fenzxpcjh = createFenzxpcjh(zhuxpcjh, fenzx, TYPE_ZHUXJH_SPILT);
						fenzxpcjh.setFenzch(zongccfjg.getLingj());		//总成号
						fenzxpcjh.setZhankrq(zhuxpcjh.getZhankrq());	//展开日期
						fenzxpcjh.setLingjlx(zongccfjg.getLingjlx());	//零件类型
						fenzxpcjh.setZhongccfxs(zongccfjg.getXis());
						setLixd(zhuxpcjh, fenzxpcjh, lixdMap, fenzx);	//离线点
						resultList.add(fenzxpcjh);
					}
				}
			}
		}
		if(fenzx.getFenzxxs() == 2 && spiltCount == 1){ //侧围线只有一边能拆分出来的情况
			Fenzxpcjh fenzxpcjh = createFenzxpcjh(zhuxpcjh, fenzx, TYPE_ZHUXJH_SPILT);
			fenzxpcjh.setFenzch(INDIVISIBLE_ZONGCH);		//总成号
			fenzxpcjh.setLingjlx(lingjlx == null ? null : lingjlx.equals("L") ? "R" : "L");	//零件类型
			fenzxpcjh.setZhankrq(zhuxpcjh.getZhankrq());	//展开日期
			setLixd(zhuxpcjh, fenzxpcjh, lixdMap, fenzx);	//离线点
			resultList.add(fenzxpcjh);
		}
		if(spiltCount == 0){	//没有总成对应的拆分信息，生成一个0000000000的分装线计划
			for (int j = 0; j < fenzx.getFenzxxs(); j++) {
				Fenzxpcjh fenzxpcjh = createFenzxpcjh(zhuxpcjh, fenzx, TYPE_ZHUXJH_SPILT);
				fenzxpcjh.setFenzch(INDIVISIBLE_ZONGCH);		//总成号
				if(fenzx.getFenzxxs() == 2 && j == 0){
					lingjlx = "L";
				}else if(fenzx.getFenzxxs() == 2 && j == 1){
					lingjlx = "R";
				}
				fenzxpcjh.setLingjlx(lingjlx);	//零件类型
				fenzxpcjh.setZhankrq(zhuxpcjh.getZhankrq());	//展开日期
				setLixd(zhuxpcjh, fenzxpcjh, lixdMap, fenzx);	//离线点
				resultList.add(fenzxpcjh);
			}
		}
	}

	/**
	 * 创建排空生成的分装线排产计划
	 * @param fenzx
	 * @param resultList
	 * @param zhuxpcjh
	 */
	private void createEmptyFenzxpcjh(Fenzx fenzx, List<Fenzxpcjh> resultList,
			Zhuxpcjh zhuxpcjh) {
		String lingjlx = null;
		//标记主线计划可拆分
		zhuxpcjh.setSpiltFinished(true);
		//侧围线排空生成两个分装线排产计划
		for (int j = 0; j < fenzx.getFenzxxs(); j++) {
			if(fenzx.getFenzxxs() == 2 && j == 0){
				lingjlx = "L";
			}else if(fenzx.getFenzxxs() == 2 && j == 1){
				lingjlx = "R";
			}
			Fenzxpcjh jh = createFenzxpcjh(zhuxpcjh, fenzx, TYPE_ZHUXJH_SPILT);
			jh.setLingjlx(lingjlx);	//零件类型
			jh.setFenzch(DEFAULT_ZONGCH);
			resultList.add(jh);
		}
	}
	
	/**
	 * 将大线计划的离线点带到对应的分装线中
	 * @param zhuxpcjh
	 * @param fenzxpcjh
	 * @param lixdMap
	 * @param fenzx
	 */
	private void setLixd(Zhuxpcjh zhuxpcjh, Fenzxpcjh fenzxpcjh, Map<String, Lixd> lixdMap, Fenzx fenzx){
		if(zhuxpcjh.getLixd() != null){
			Lixd lixd = lixdMap.get(zhuxpcjh.getLixd());
			//主线计划的离线点为该分装线的离线点
			if(lixd != null && lixd.getXianh().equals(fenzx.getFenzxh())){	
				fenzxpcjh.setLixd(lixd.getLixd());	//离线点
				fenzxpcjh.setTiqcw(zhuxpcjh.getTiqcw());	//提前车位
				fenzxpcjh.setLingjyt("P");//分装线有离线点的零件用途设置为"P"
			}
			
			if(lixd != null && lixd.getXianh().equals(fenzx.getDaxxh())){	
				fenzxpcjh.setDaxlxd(lixd.getLixd());	//离线点
			}
		}
	}
	
	/**
	 * 创建分装线排产计划
	 * @param zhuxpcjh
	 * @param fenzx
	 */
	private Fenzxpcjh createFenzxpcjh(Zhuxpcjh zhuxpcjh, Fenzx fenzx, String leix){
		Fenzxpcjh fenzxpcjh = new Fenzxpcjh();
		fenzxpcjh.setYujsxrq(zhuxpcjh.getYujjhzsj());		//预计上线日期
		fenzxpcjh.setYujxxrq(zhuxpcjh.getYujjhzsj());		//预计下线日期
		fenzxpcjh.setDuiydxsx(zhuxpcjh.getDaxsx());			//对应的大线顺序号
		fenzxpcjh.setUsercenter(zhuxpcjh.getUsercenter());	//用户中心
		fenzxpcjh.setFenzxh(fenzx.getFenzxh());				//分装线号
		fenzxpcjh.setXuqly(zhuxpcjh.getXuqly());			//需求来源
		fenzxpcjh.setDingdh(zhuxpcjh.getDingdh());			//订单号
		fenzxpcjh.setHanzcx(zhuxpcjh.getHanzcx());			//焊装车型
		fenzxpcjh.setBeijwx(false);							//是否备件外销计划
		fenzxpcjh.setDaxzsx_liq(zhuxpcjh.getDaxzsx_lixq());	//大线总顺序（离线前）
		fenzxpcjh.setLeix(leix);
		if(!Strings.isNullOrEmpty(zhuxpcjh.getNewyujjhzsj())){
		fenzxpcjh.setCreator(zhuxpcjh.getNewyujjhzsj());
		}
		return fenzxpcjh;
	}
	
	/**
	 * 重新生成分装线对应大线顺序号（大线计划拆分成的分装线计划可能为零或者多个，故需重新排序）
	 * @param fenzxpcjhList
	 */
	private void rebuildDuiydxsx(List<Fenzxpcjh> fenzxpcjhList){
		for (int i = 0; i < fenzxpcjhList.size(); i++) {
			Fenzxpcjh fenzxpcjh = fenzxpcjhList.get(i);
			//其他的重新生成对应大线顺序号
			fenzxpcjh.setDuiydxsx(i+1);
		}
	}
	
	/**
	 * 分装线离线点提前
	 * @param fenzxpcjhList
	 * @param fenzx
	 * @throws ParseException 
	 */
	private void putAheadOfFenzxLixd(Dax dax, Map<String, Fenzx> fenzxMap, 
			Map<String, Lixd> lixdMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap) throws ParseException{
		log.info("分装线离线提前...");
		for (CalcDay day : calcDays.get(dax.getDaxxh())) {
			this.currentDay = day;
			if(currentDay.getShifgzr().equals("1")){
				//循环分装线
				for (String fenzxh : fenzxMap.keySet()) {
					//分装线
					Fenzx fenzx = fenzxMap.get(fenzxh);
					if(fenzxpcjhMap != null){
						//取出大线离线点，并按照对应消耗点的车身数量降序排序
						List<Lixd> lixdList = sortLixd(fenzx.getFenzxh(), lixdMap);
						for (int i = 0; i < lixdList.size(); i++) {
							//按照最远的离线点依次提前
							putAheadOfFenzxLixd(fenzxpcjhMap.get(fenzxh), fenzx, lixdList.get(i));
						}
					}
				}
			}
		}
	} 
	
	/**
	 * 分装线排产计划按照某个离线点提前
	 * @param fenzxpcjhList
	 * @param fenzx
	 * @param lixd
	 * @throws ParseException 
	 */
	private void putAheadOfFenzxLixd(Map<String, List<Fenzxpcjh>> fenzxpcjhMap, Fenzx fenzx, Lixd lixd) throws ParseException{
		// 当日的分装线计划
		List<Fenzxpcjh> fenzxpcjhList = fenzxpcjhMap.get(currentDay.getRiq());
		// 所有的分装线计划
		List<Fenzxpcjh> totalList = getFenzxjhList(fenzxpcjhMap);
		if(fenzxpcjhList != null){
			for (int i = 0; i < fenzxpcjhList.size(); i++) {
				Fenzxpcjh jh = fenzxpcjhList.get(i);
				if(lixd.getLixd().equals(jh.getLixd())){
					// 实际提前车位数（越过空订单和未拆分订单）
					int tiqcw = findPracticalTiqcwByFenzxjh(totalList, jh.getDingdh(), jh.getTiqcw(), fenzx.getFenzxxs());
					//得到提前后的上线顺序
					jh.setShangxsx(jh.getShangxsx() - tiqcw);
					//受影响的排产计划
					int startShangxsx = jh.getShangxsx()-1 < 0 ? 0 : jh.getShangxsx()-1;
					for (int j = startShangxsx; j < i && j >= 0; j++) {
						Fenzxpcjh temp= fenzxpcjhList.get(j);
						temp.setShangxsx(temp.getShangxsx() + 1);
					}
					//按照上线顺序号重新排序
					Collections.sort(fenzxpcjhList);
					//确保上线顺序大于0
					ensureShangxsx(fenzxpcjhMap, jh, fenzx);
				}
			}
		}
	}
	
	/**
	 * 找到实际的提前车位数（要越过空订单和未拆分订单）
	 * @param totalList
	 * @param dingdh
	 * @param tiqcw
	 * @return
	 */
	private int findPracticalTiqcwByFenzxjh(List<Fenzxpcjh> totalList, String dingdh, int tiqcw, int xis){
		if(dingdh == null)	
			return 0;
		// 待提前计划Index
		int dingdIndex = -1;
		Fenzxpcjh jh;
		for (int i = 0; i < totalList.size(); i++) {
			jh = totalList.get(i);
			// 第一个订单号相同的计划
			if(dingdh.equals(jh.getDingdh())&& jh.getLixd() != null){
				dingdIndex = i;
				break;
			}
		}
		// 仍未提前车位数
		int remain_tiqcw = tiqcw;
		// 实际提前车位数
		int practical_tiqcw = 0;
		if(xis == 1){
			for (int i = dingdIndex - 1; i >= 0 && remain_tiqcw > 0; i--, practical_tiqcw++) {
				jh = totalList.get(i);
				// 不是默认总成或未拆分总成，才算作提前一个车位
				if(!isDefaultOrIndivisibleZongc(jh.getFenzch())){
					remain_tiqcw--;
				}
			}
		}else if(xis == 2){
			String last_dingdh = "";
			String last_zongch = "";
			for (int i = dingdIndex - 1; i >= 0; i--, practical_tiqcw++) {
				jh = totalList.get(i);
				if(remain_tiqcw <= 0 && (jh.getDingdh() == null || !jh.getDingdh().equals(last_dingdh))){
					break;
				}
				if(isDifferentAndExistDingd(jh.getFenzch(), jh.getDingdh(), last_zongch, last_dingdh)){
					remain_tiqcw--;
				}
				last_zongch = jh.getFenzch();
				last_dingdh = jh.getDingdh();
			}
		}
		return practical_tiqcw;
	}
	
	/**
	 * 找到实际的返还车位数（要越过空订单和未拆分订单）
	 * @param totalList
	 * @param dingdh
	 * @param tiqcw
	 * @return
	 */
	private int findPracticalTiqcwByFenzxjhAnotherSide(List<Fenzxpcjh> totalList, int dingdIndex, int tiqcw, int xis){
		Fenzxpcjh jh;
		// 仍未提前车位数
		int remain_tiqcw = tiqcw;
		// 实际提前车位数
		int practical_tiqcw = 0;
		if(xis == 1){
			for (int i = dingdIndex + 1; i < totalList.size() && remain_tiqcw > 0; i++, practical_tiqcw++) {
				jh = totalList.get(i);
				// 不是默认总成或未拆分总成，才算作提前一个车位
				if(!isDefaultOrIndivisibleZongc(jh.getFenzch())){
					remain_tiqcw--;
				}
			}
		}else if(xis == 2){
			String last_dingdh = "";
			String last_zongch = "";
			for (int i = dingdIndex + 1; i < totalList.size(); i++, practical_tiqcw++) {
				jh = totalList.get(i);
				if(remain_tiqcw <= 0 && (jh.getDingdh() == null || !jh.getDingdh().equals(last_dingdh))){
					break;
				}
				if(isDifferentAndExistDingd(jh.getFenzch(), jh.getDingdh(), last_zongch, last_dingdh)){
					remain_tiqcw--;
				}
				last_dingdh = jh.getDingdh();
				last_zongch = jh.getFenzch();
				
			}
		}
		return practical_tiqcw;
	}
	
	/**
	 * 将每日的分装线计划map汇总为一个list
	 * @param fenzxpcjhMap
	 * @return
	 */
	private List<Fenzxpcjh> getFenzxjhList(Map<String, List<Fenzxpcjh>> fenzxpcjhMap){
		// 所有的分装线计划
		List<Fenzxpcjh> totalList = new ArrayList<Fenzxpcjh>();
		// 当日的分装线计划
		List<Fenzxpcjh> dayList;
		for (String date : fenzxpcjhMap.keySet()) {
			dayList = fenzxpcjhMap.get(date);
			if(dayList != null && !dayList.isEmpty()){
				totalList.addAll(dayList);
			}
		}
		return totalList;
	}
	
	/**
	 * 处理备件外销计划
	 * @param dax
	 * @param fenzx
	 * @param beijwxjhList
	 * @param fenzxpcjhList
	 * @throws ParseException 
	 */
	private void dealWithBeijwxjh(Dax dax, Map<String, Fenzx> fenzxMap,
			Map<String, List<Beijwxjh>> beijwxjhMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, 
			Map<String, Paicls> paiclsMap) throws ParseException {
		log.info("大线" + dax.getDaxxh() + "处理备件外销计划中...");
		// 循环分装线
		for (String fenzxh : fenzxpcjhMap.keySet()) {
			// 分装线
			Fenzx fenzx = fenzxMap.get(fenzxh);

			List<Beijwxjh> beijwxjhList = beijwxjhMap.get(fenzxh);
			// 备件外销计划为空
			if (beijwxjhList == null || beijwxjhList.size() == 0) {
				continue;
			}
			// 排产第一日的分装线排产计划，只从最后计算车辆后开始插入备件外销计划
			String targetDate = dealWithFirstDay(dax, fenzx, fenzxpcjhMap.get(fenzxh)
					, paiclsMap.get(fenzxh), beijwxjhMap.get(fenzxh));
			// 之后的日期的分装线排产计划，按照正常逻辑进行插入
			for (CalcDay daxDay : calcDays.get(dax.getDaxxh())) {
				if(daxDay.getRiq().compareTo(targetDate) > 0 
						&& daxDay.getRiq().compareTo(paickssj) >= 0){ // 上线日期第二日及以后
					if (daxDay.getShifgzr().equals("1")) { //大线工作日
						//备件外销插入
						fenzxInsertBeijwxjh(dax, fenzx, daxDay, beijwxjhMap, fenzxpcjhMap);
					}else if(daxDay.getShifgzr().equals("0")){	//大线非工作日
						//备件外销单独排产
						fenzxProduceAlone(dax, fenzx, daxDay, beijwxjhMap, fenzxpcjhMap);
					}
				}
			}
			// 第一次排产，调整分装线的下线顺序
			//adjustXiaxsxForFirstTime(targetDate, fenzx, paiclsMap.get(fenzxh), fenzxpcjhMap.get(fenzxh));
			// 给订单号为空的计划新增一个虚拟的订单号
			createVirtualDingdh(fenzxpcjhMap, fenzxMap);
		}
	}
	
	/**
	 * 处理离线点到封闭期的计划
	 * @param dax
	 * @param fenzx
	 * @param beijwxjhList
	 * @param fenzxpcjhList
	 * @throws ParseException 
	 */
	private void dealWithLiXianTofbq(Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap,Map<String, Fenzx> fenzxMap){
		try {
		//遍历所有的分装线上线排产计划
		for (String fenzxh : fenzxpcjhMap.keySet()) {

			Fenzx fenzx = fenzxMap.get(fenzxh);
			//分装线的所有排产计划
			Map<String, List<Fenzxpcjh>> fenzxResult = fenzxpcjhMap.get(fenzxh);
			
			//处理计划离线到排产开始日期之前日期的计划中，导致排产开始日期前一天的计划被挤到排产开始日期的第一天计划里
			List<Fenzxpcjh> oneDayremovelist =new ArrayList<Fenzxpcjh>();//用于存放排产开始时间当天存在于结果表的计划		
			List<Fenzxpcjh> oneFenzxpcjh = fenzxResult.get(paickssj);//获取排产第一天的所有分装线计划
			if(oneFenzxpcjh!=null && oneFenzxpcjh.size()>0){	
			String beforepaickssj = addDaysByZhuxjh(paickssj, -1);		
			int j=0;//排产前一天集合中的排空量
			int h=0;//排产前一天数据库中的排空量
			if(beforepaickssj.compareTo(paickssj)<0){
				List<Fenzxpcjh> beforeFenzxpcjh= fenzxResult.get(beforepaickssj);
				if(beforeFenzxpcjh!=null && beforeFenzxpcjh.size()>0){
					for (Fenzxpcjh fenzxpcjh : beforeFenzxpcjh) {
						if(fenzxpcjh.getFenzch().equalsIgnoreCase("ZONGCH")){
							j++;
						}
					}
				}
				Fenzxpcjh f=new Fenzxpcjh();
				f.setYujsxrq(beforepaickssj);
				f.setFenzxh(fenzxh);
				//查询开始排产日期的前一天数据库中的空数量
				List<Fenzxpcjh>	beforeZONGCHlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.queryBeforeZhuxpcjhpaikl", f);	
				if(beforeZONGCHlist!=null && beforeZONGCHlist.size()>0){
					h=beforeZONGCHlist.size();
				}
			}
			for (Fenzxpcjh fenzxpcjh : oneFenzxpcjh) {
				List<Fenzxpcjh> fenzxpcjhList=new ArrayList<Fenzxpcjh>();
				//第一日的计划如果存在于结果表中，就把这些计划从第一天的计划中删除
				if(!fenzxpcjh.getFenzch().equalsIgnoreCase("ZONGCH")){
					if(!Strings.isNullOrEmpty(fenzxpcjh.getCreator())){
						 fenzxpcjhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.queryFenzxpcjhBydingdh", fenzxpcjh);		
						}				    
				}else{
					//因为离线从前一天最后挤过来的空计划，并且排产前一日的排产数量不等于排产前一日的排产结果数量
					if(fenzxpcjh.getXixldjh()!=null && fenzxpcjh.getXixldjh().equalsIgnoreCase("1") && h>j){
						
					 fenzxpcjhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.queryZhuxpcjhBylixq", fenzxpcjh);	
					}
				}
				if(fenzxpcjhList!=null  && fenzxpcjhList.size() >0){
					oneDayremovelist.add(fenzxpcjh);//把存在于结果表中的计划放入集合中
				}	
			}
			}
			//遍历要删除的集合，把它从第一天的计划中删除
			for (Fenzxpcjh fenzxpcjh : oneDayremovelist) {
				oneFenzxpcjh.remove(fenzxpcjh);
			}
			if(oneFenzxpcjh!=null && oneFenzxpcjh.size()>0){
			//重新生成顺序以及日期
			rebuildShangxsx(oneFenzxpcjh, paickssj);
			}
			
			
			int i=0;
			for (String riq : fenzxResult.keySet()) {
				List<Fenzxpcjh> removelist =new ArrayList<Fenzxpcjh>();//用于存放小于开始排产日期,但又不存在排产结果表的计划
				if(riq.compareTo(paickssj) < 0){
				List<Fenzxpcjh> dayFenzxpcjh = fenzxResult.get(riq);
				for (Fenzxpcjh fenzxpcjh : dayFenzxpcjh) {
					if(fenzxpcjh.getDaxlxd()!=null || fenzxpcjh.getLixd()!=null){
						List<Fenzxpcjh> fenzxpcjhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.queryFenzxpcjhBydingdh", fenzxpcjh);	
						//不存在排产结果表的就把这个计划加入到排产第一日的计划中
						if(fenzxpcjhList==null  || fenzxpcjhList.size()==0){
						fenzxResult.get(paickssj).add(i,fenzxpcjh);
						//重新生成顺序以及日期
						rebuildShangxsx(fenzxResult.get(paickssj), paickssj);
						i++;
						removelist.add(fenzxpcjh);
					}
					}
				}
				//删除重复 的计划
				for (Fenzxpcjh fenzxpcjh : removelist) {
					List<Fenzxpcjh> newFenzxpcjh =fenzxResult.get(riq);
					newFenzxpcjh.remove(fenzxpcjh);
				}
				}
			}
			
			//根据总成拆分系数的大小，生存对应的分装线计划
			// 当日的分装线计划
			List<Fenzxpcjh> dayList;
			for (String date : fenzxResult.keySet()) {
				dayList = fenzxResult.get(date);//每天的分装线排产计划
				int flag=0;//是否有系数大于1的分装线计划
				if(dayList != null && !dayList.isEmpty()){
					for (int k = dayList.size()-1; k >= 0; k--) {
						if(dayList.get(k).getZhongccfxs()!=null && dayList.get(k).getZhongccfxs()>1){
							flag=1;
							for (int j = 0; j < dayList.get(k).getZhongccfxs()-1; j++) {
								Fenzxpcjh f=new Fenzxpcjh();
								f.setZhongccfxs(1);
								f.setCreator(dayList.get(k).getCreator());
								f.setDaxzsx_liq(dayList.get(k).getDaxzsx_liq());
								f.setDingdh(dayList.get(k).getDingdh());
								f.setDuiydxsx(dayList.get(k).getDuiydxsx());
								f.setFenzch(dayList.get(k).getFenzch());
								f.setFenzxh(dayList.get(k).getFenzxh());
								f.setHanzcx(dayList.get(k).getHanzcx());
								f.setLeix(dayList.get(k).getLeix());
								f.setShangxsx(dayList.get(k).getShangxsx());
								f.setUsercenter(dayList.get(k).getUsercenter());
								f.setXiaxsx(dayList.get(k).getXiaxsx());
								f.setXuqly(dayList.get(k).getXuqly());
								f.setYujsxrq(dayList.get(k).getYujsxrq());
								f.setYujxxrq(dayList.get(k).getYujxxrq());
								f.setZhankrq(dayList.get(k).getZhankrq());
								f.setLingjlx(dayList.get(k).getLingjlx());
								dayList.add(k+1,f);
						
							}				
						}
					}
				}
				if(fenzx.getFenzxxs()==2){
					sortFenzxpcjhA(dayList);
				}
				//有系数大于1的分装线计划
				if(flag>0){
				//重新生成顺序以及日期
				rebuildShangxsx(dayList, date);
				}
			}
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 给订单号为空的计划新增一个虚拟的订单号，不做保存，只做唯一性判断
	 * @param allFenzxpcjhMap
	 */
	private void createVirtualDingdh(Map<String, Map<String, List<Fenzxpcjh>>> allFenzxpcjhMap, Map<String, Fenzx> fenzxMap){
		// 一个分装线的所有排产计划
		Map<String, List<Fenzxpcjh>> oneFenzxpcjhMap;
		// 一个分装线一日的分装线排产计划
		List<Fenzxpcjh> dayFenzxpcjhList;
		for (String fenzxh : allFenzxpcjhMap.keySet()) {
			int number = 0;
			oneFenzxpcjhMap = allFenzxpcjhMap.get(fenzxh);
			// 分装线系数
			int xis = fenzxMap.get(fenzxh).getFenzxxs();
			if(oneFenzxpcjhMap == null){
				continue;
			}
			if(xis == 1){
				for (String date : oneFenzxpcjhMap.keySet()) {
					dayFenzxpcjhList = oneFenzxpcjhMap.get(date);
					if(dayFenzxpcjhList == null){
						continue;
					}
					for (Fenzxpcjh jh : dayFenzxpcjhList) {
						if(jh.getDingdh() == null){
							number++;
							jh.setDingdh("temp_" + fenzxh + "-" + date + "-" + String.format("%04d", number));
						}
					}
				}
			} else if(xis == 2){	// 侧围线
				for (String date : oneFenzxpcjhMap.keySet()) {
					dayFenzxpcjhList = oneFenzxpcjhMap.get(date);
					if(dayFenzxpcjhList == null){
						continue;
					}
					String last_lingjlx = "R";
					for (Fenzxpcjh jh : dayFenzxpcjhList) {
						if(jh.getDingdh() == null){
							if(jh.getLingjlx() == null || "L".equals(jh.getLingjlx()) || "R".equals(last_lingjlx)){
								number++;
							}
							
							jh.setDingdh("temp_" + fenzxh + "-" + date + "-" + String.format("%04d", number));
							last_lingjlx = jh.getLingjlx();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 验证分装线大线工时节拍
	 * @param dax
	 * @param fenzxMap
	 * @param fenzxpcjhMap
	 */
	private void checkCapacity(Dax dax, Map<String, Fenzx> fenzxMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap){
		for (String fenzxh : fenzxpcjhMap.keySet()) {
			Fenzx fenzx = fenzxMap.get(fenzxh);
			for (CalcDay daxDay : calcDays.get(dax.getDaxxh())) {
				// 获取分装线的当日日期信息
				CalcDay fenzxDay = getFenzxCurrentCalcDay(
						calcDays.get(fenzxh), daxDay.getRiq());
				if (fenzxDay.getGongs() < daxDay.getGongs()) { // 分装线工时<大线工时
					fenzxDay.setGongs(daxDay.getGongs()); // 工时设为大线工时
					addWarnMessage(new WarnMessage(dax.getUsercenter(),
							dax.getDaxxh(), fenzxh, fenzxDay.getRiq(),
							"分装线" + fenzxh + "的工时小于大线工时，该分装线的排产将会按照大线工时进行",
							creator));
				}
			}
			if (fenzx.getShengcjp() < dax.getShengcjp()) {// 分装线节拍<大线节拍
				fenzx.setShengcjp(dax.getShengcjp()); // 节拍设为大线节拍
				addWarnMessage(new WarnMessage(dax.getUsercenter(),
						dax.getDaxxh(), fenzxh, paickssj,
						"分装线" + fenzxh + "的节拍小于大线节拍，该分装线的排产将会按照大线节拍进行",
						creator));
			}
		}
	}
	
	/**
	 * 验证未拆分的大线计划
	 * @param dax
	 * @param map
	 */
	private void checkUnspiltPlans(Dax dax, Map<String, Map<String, List<Zhuxpcjh>>> totalMap
			, Map<String, List<Zhuxpcjh>> zhuxjhMap){
		// 已拆分的计划
		List<String> finishedList = new ArrayList<String>();
		
		// 获取已拆分的订单（只要有一条分装线能拆分出来，则判断拆分成功）
		Map<String, List<Zhuxpcjh>> jhMap;
		for (String fenzxh : totalMap.keySet()) {
			jhMap = totalMap.get(fenzxh);
			if(jhMap == null || jhMap.isEmpty()){
				continue;
			}
			List<Zhuxpcjh> list;
			for (String riq : jhMap.keySet()) {
				list = jhMap.get(riq);
				if(list == null || list.isEmpty()){
					continue;
				}
				for (Zhuxpcjh jh : list) {
					if(jh.isSpiltFinished() && !finishedList.contains(jh.getDaxzsx_lixq()+jh.getDingdh()+jh.getZongch())){
						finishedList.add(jh.getDaxzsx_lixq()+jh.getDingdh()+jh.getZongch());
					}
				}
			}
		}
		
		// 找到未拆分的订单，并添加错误信息
		List<Zhuxpcjh> list;
		for (String riq : zhuxjhMap.keySet()) {
			list = zhuxjhMap.get(riq);
			if(list == null || list.isEmpty()){
				continue;
			}
			for (Zhuxpcjh jh : list) {
				if(!finishedList.contains(jh.getDaxzsx_lixq()+jh.getDingdh()+jh.getZongch())){
					addWarnMessage(new WarnMessage(dax.getUsercenter(),
							dax.getDaxxh(), riq,
							"订单号为"+jh.getDingdh()+"，总成号为"+jh.getZongch()+
							"，展开日期为"+jh.getZhankrq()+"的大线计划无法找到对应的拆分信息",
							creator));
				}
			}
		}
	}
	
	/**
	 * 针对排产第一日的分装线排产计划，只从最后计算车辆后开始插入备件外销计划
	 * @param dax
	 * @param fenzx
	 * @param map
	 * @param paicls
	 * @param beijwxjhList
	 * @return
	 * @throws ParseException 
	 */
	private String dealWithFirstDay(Dax dax, Fenzx fenzx, Map<String, List<Fenzxpcjh>> map
			, Paicls paicls, List<Beijwxjh> beijwxjhList) throws ParseException{
		boolean isFirstTime = false;
		// 此次要保存的上线日期
		String targetDate = null;
		// 最后计算车辆的订单号
		String zuihjscl = paicls == null ? null : paicls.getZuihjscl();
		// 排产流水为空或未记录上次最后计算订单，默认为第一次排产
		if(Strings.isNullOrEmpty(zuihjscl) || "NULL".equals(zuihjscl)){
			// 默认排产开始时间
			targetDate = findTargetDate(map, paickssj, fenzx.getChews()*fenzx.getFenzxxs());
			// 计算出最后计算车辆的订单号
			zuihjscl = calcZuihjscl(map, fenzx);
			isFirstTime = true;
		}else{
			// 找到上次最后计算订单的后一个计划对应此次计算的下线日期
			targetDate = findTargetDate(map, zuihjscl, fenzx);
		}
		// 第一日的分装线排产计划
		List<Fenzxpcjh> fenzxpcjhList = map.get(targetDate);
		if(fenzxpcjhList == null || fenzxpcjhList.isEmpty()){
			return targetDate;
		}
		// 截取后的第一日的分装线排产计划
		List<Fenzxpcjh> subList = subListByZuihjscl(fenzxpcjhList, zuihjscl, fenzx);
		// 获取分装线的当日日期信息
		CalcDay fenzxDay = getFenzxCurrentCalcDay(
				calcDays.get(fenzx.getFenzxh()), targetDate);
		List<Fenzxpcjh> temp = new ArrayList<Fenzxpcjh>();
		if(isFirstTime){
			temp.addAll(fenzxpcjhList);
			temp.addAll(subList);
		}else{
			temp.addAll(subList);
			for (; targetDate.compareTo(paickssj) < 0; ) {
				targetDate = addDaysByZhuxjh(targetDate, 1);
				temp.addAll(map.get(targetDate));
				fenzxDay = getFenzxCurrentCalcDay(
						calcDays.get(fenzx.getFenzxh()), targetDate);
			}
		}
		// 当日可完成的备件外销零件数量
		Map<String, Integer> countMap = sumFenzxpcjhCount(fenzx, fenzxDay, temp);
		String chakpyl="0";
		if(paicls!=null){
			 chakpyl = paicls.getChakpyl() == null ? "0" : paicls.getChakpyl();//获取插空偏移量
		}
		if(fenzx.getFenzxxs() == 1){
			countMap.put("maxLingjCount", Math.max(countMap.get("maxLingjCount") - Integer.parseInt(chakpyl), 0));//第一日的备件外销要减去插空偏移量
		}else if(fenzx.getFenzxxs() == 2){
			countMap.put("maxLingjCount_L", Math.max(countMap.get("maxLingjCount_L") - Integer.parseInt(chakpyl)/2, 0));
			countMap.put("maxLingjCount_R", Math.max(countMap.get("maxLingjCount_R") - Integer.parseInt(chakpyl)/2, 0));
		}
		temp.clear();
		// 减去当日已进入封闭期的分装线计划数量
		reduceFenzxpcjhCount(fenzx, targetDate, countMap);
		// 当日可完成的备件外销计划
		List<Beijwxjh> todayBeijwxjhList = subBeijwxjh(dax, beijwxjhList,
				countMap, fenzx.getFenzxxs(), targetDate);
		// 通过备件外销计划生成分装线排产计划（待插入的分装线计划）
		List<Fenzxpcjh> fenzxpcjhList_beijwxjh = createFenzxpcjhByBeijwxjh(
				todayBeijwxjhList, targetDate,TYPE_BEIJWX_INSERT);
		// 侧围线把备件外销计划按左右配对
		if (fenzx.getFenzxxs() == 2) {
			fenzxpcjhList_beijwxjh = sortFenzxpcjhMatching(fenzxpcjhList_beijwxjh);
		}
		// 分装线节拍 > 大线节拍，才考虑备件外销插入
		if(fenzx.getShengcjp() - dax.getShengcjp() > 0){
			// 插入间隔=大线节拍/(分装线节拍-大线节拍)，向上取整
			Double jiange = Math.ceil(Double.valueOf(dax.getShengcjp())
					/ (Double.valueOf(fenzx.getShengcjp()) - Double
							.valueOf(dax.getShengcjp())));
			// 插入备件外销计划
			insertBeijwxjh(subList, fenzxpcjhList_beijwxjh,
					dax.getShengcjp(), jiange.intValue(),
					fenzx.getFenzxxs());
		}
		if (fenzxpcjhList_beijwxjh.size() > 0) { // 还有备件外销计划未分配
			subList.addAll(fenzxpcjhList_beijwxjh); // 全部插入在当日的末尾处
		}
		// 加上当日已进入封闭期的计划
		//fenzxpcjhList.addAll(queryFenzxpcjhByDate(fenzx, targetDate, zuihjscl));
		// 加上当日此次计算的计划
		fenzxpcjhList.addAll(subList);
		// 重新生成下线顺序号
		rebuildXiaxsx(fenzxpcjhList, targetDate);
		
		return targetDate;
	}
	
	/**
	 * 第一次排产，调整分装线的下线顺序
	 * @param targetDate
	 * @param fenzx
	 * @param paicls
	 * @param map
	 */
	private void adjustXiaxsxForFirstTime(String targetDate, Fenzx fenzx, Paicls paicls, Map<String, List<Fenzxpcjh>> map){
		// 第一次排产，每日的最后分装线长度个的计划，下线日期应该是第二个分装线工作日
		if(paicls == null || Strings.isNullOrEmpty(paicls.getZuihjscl()) || "NULL".equals(paicls.getZuihjscl())){
			String riq;
			List<Fenzxpcjh> fenzxpcjhList;
			List<CalcDay> days = calcDays.get(fenzx.getFenzxh());
			for (int i = 0; i < days.size(); i++) {
				riq = days.get(i).getRiq();
				fenzxpcjhList = map.get(riq);
				if(riq.compareTo(targetDate) >=0 
						&& i+1 < days.size()
						&& fenzxpcjhList != null 
						&& !fenzxpcjhList.isEmpty()){
					for (int j = i+1; j < days.size(); j++) {
						if(days.get(j).getShifgzr().equals("1")){
							rebuildXiaxsx(fenzxpcjhList.subList(Math.max(
									fenzxpcjhList.size() - fenzx.getChews()*fenzx.getFenzxxs(), 0),
									fenzxpcjhList.size()), days.get(j).getRiq());
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 查询指定的日期的分装线排产计划
	 * @param fenzx
	 * @param yujxxrq
	 * @param zuihjscl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Fenzxpcjh> queryFenzxpcjhByDate(Fenzx fenzx,String yujxxrq, String zuihjscl){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("usercenter", fenzx.getUsercenter());
		paramMap.put("fenzxh", fenzx.getFenzxh());
		paramMap.put("yujxxrq", yujxxrq);
		paramMap.put("zuihjscl", yujxxrq);
		return (List<Fenzxpcjh>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
			.selectObject("fenzxpc.queryFenzxpcjhByDate", paramMap);
	}
	
	/**
	 * 截取出最后计算订单后的当日订单
	 * @param fenzxpcjhList
	 * @param zuihjscl
	 * @param fenzx
	 * @return
	 */
	private List<Fenzxpcjh> subListByZuihjscl(List<Fenzxpcjh> fenzxpcjhList, String zuihjscl, Fenzx fenzx){
		List<Fenzxpcjh> resultList = new ArrayList<Fenzxpcjh>();
		// 截取index
		int index = 0;
		if(zuihjscl != null){
			for (int i = fenzxpcjhList.size() - 1; i >= 0; i--) {
				// 最后计算车辆的订单号对应的分装线排产计划
				if(zuihjscl.equals(fenzxpcjhList.get(i).getDingdh())){	
					index = i + 1;
					break;
				}
			}
		}else{
			index = fenzxpcjhList.size();
		}
		// 返回从最后计算车辆开始处到当日分装线排产计划结束处的所有订单
		resultList.addAll(fenzxpcjhList.subList(index, fenzxpcjhList.size()));
		// 移除截取的部分
		removeList(fenzxpcjhList, index, fenzxpcjhList.size());
		return resultList;
	}
	
	/**
	 * 根据分装线的产线长度，虚拟出一个最后计算车辆订单号
	 * @param map
	 * @param fenzx
	 * @return
	 */
	private String calcZuihjscl(Map<String, List<Fenzxpcjh>> map, Fenzx fenzx){
		String zuihjscl = null;
		List<Fenzxpcjh> tempList = new ArrayList<Fenzxpcjh>();
		for (Entry<String, List<Fenzxpcjh>> entry : map.entrySet()) {
			if(entry.getKey().compareTo(paickssj) >= 0){
				tempList.addAll(entry.getValue());
			}
		}
		String last_dingdh = "";
		Fenzxpcjh jh;
		// 往后数出产线长度个的订单数量，得到最后计算车辆订单
		int count = fenzx.getChews();
		for (int i = 0; i < tempList.size(); i++) {
			jh = tempList.get(i);
			if(jh.getDingdh() == null || !jh.getDingdh().equals(last_dingdh)){
				count--;
			}
			if(count == 0){
				zuihjscl = jh.getDingdh();
				break;
			}
			last_dingdh = jh.getDingdh();
		}
		tempList.clear();
		return zuihjscl;
	}
	
	/**
	 * 减去已进入封闭期的分装线排产数量
	 * 考虑到分装线的车位数发生变动，或上次推算的下线日期错误，导致前一日的下线车辆变少
	 * ，那么会导致当日插入的备件外销计划数量过多。如果此时插入备件计划后的数量达到产能
	 * ，则会造成部分应该当日上线的计划被挤到下一日中，出现偏移错误。现在用分装线车位数
	 * ，和已进入封闭期的分装线排产数量的最大值计算。会有什么问题？
	 * @param fenzx
	 * @param yujxxrq
	 * @return
	 */
	private void reduceFenzxpcjhCount(Fenzx fenzx, String yujxxrq, Map<String, Integer> countMap){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("usercenter", fenzx.getUsercenter());
		paramMap.put("fenzxh", fenzx.getFenzxh());
		paramMap.put("yujxxrq", yujxxrq);
		paramMap.put("defaultZongch", DEFAULT_ZONGCH);
	//	paramMap.put("indivisibleZongch", INDIVISIBLE_ZONGCH);//0000000000算产能
		if(fenzx.getFenzxxs() == 1){
			// 已进入封闭期的分装线排产数量
			int finishedCount = Integer.valueOf(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.selectObject("fenzxpc.getFenzxpcjhCount", paramMap).toString());
			// 已进入封闭期的数量与分装线的车位数的最大值
			int dingd_num = Math.max(finishedCount, fenzx.getChews());
			countMap.put("maxLingjCount", Math.max(countMap.get("maxLingjCount") - dingd_num, 0));
		}else if(fenzx.getFenzxxs() == 2){	// 侧围线
			paramMap.put("lingjlx", "L");
			int finishedCount_L = Integer.valueOf(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.selectObject("fenzxpc.getFenzxpcjhCount", paramMap).toString());
			
			paramMap.put("lingjlx", "R");
			int finishedCount_R = Integer.valueOf(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.selectObject("fenzxpc.getFenzxpcjhCount", paramMap).toString());
			// 订单数量为L与R类型零件与车位数的最大值
			int dingd_num = Math.max(Math.max(finishedCount_L, finishedCount_R), fenzx.getChews());
			countMap.put("maxLingjCount_L", Math.max(countMap.get("maxLingjCount_L") - dingd_num, 0));
			countMap.put("maxLingjCount_R", Math.max(countMap.get("maxLingjCount_R") - dingd_num, 0));
		}
		
	}
	
	
	/**
	 * 从分装线排产计划中找到指定订单号的后一个计划的预计下线日期
	 * @param map
	 * @param zuihjscl
	 * @return
	 */
	private String findTargetDate(Map<String, List<Fenzxpcjh>> map, String zuihjscl, Fenzx fenzx){
		Fenzxpcjh fenzxpcjh = null;
		Iterator<Entry<String, List<Fenzxpcjh>>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, List<Fenzxpcjh>> entry = iter.next();
			for (int i = 0; i < entry.getValue().size(); i++) {
				fenzxpcjh = entry.getValue().get(i);
				if(zuihjscl.equals(fenzxpcjh.getDingdh())){
					// 当天最后一个计划，且有下一天的计划
					if(i == entry.getValue().size() - fenzx.getFenzxxs() && iter.hasNext()){
						return iter.next().getKey();
					}
					return entry.getKey();
				}
			}
		}
		// 无法从分装线计划中找到该订单号，则返回一个最大日期
		return "2099-12-31";
	}
	
	/**
	 * 从分装线排产计划中找到指定序号的后一个计划的预计下线日期
	 * @param map
	 * @param zuihjscl
	 * @return
	 */
	private String findTargetDate(Map<String, List<Fenzxpcjh>> map, String riq, int offset){
		Iterator<Entry<String, List<Fenzxpcjh>>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, List<Fenzxpcjh>> entry = iter.next();
			for (int i = 0; i < entry.getValue().size(); i++) {
				if(entry.getKey().compareTo(riq) >= 0){
					offset--;
					if(offset == 0){
						return entry.getKey();
					}
				}
			}
		}
		// 无法从分装线计划中找到该订单号，则返回一个最大日期
		return "2099-12-31";
	}
	
	/**
	 * 备件外销插入
	 * @param dax
	 * @param fenzx
	 * @param daxDay
	 * @param beijwxjhMap
	 * @param fenzxpcjhMap
	 */
	private void fenzxInsertBeijwxjh(Dax dax, Fenzx fenzx, CalcDay daxDay,
			Map<String, List<Beijwxjh>> beijwxjhMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap){
		String fenzxh = fenzx.getFenzxh();	//分装线号
		log.info("分装线"+fenzxh+"在"+daxDay.getRiq()+"备件外销插入...");
		// 分装线排产计划
		List<Fenzxpcjh> fenzxpcjhList = fenzxpcjhMap.get(fenzxh)
				.get(daxDay.getRiq());
		// 获取分装线的当日日期信息
		CalcDay fenzxDay = getFenzxCurrentCalcDay(
				calcDays.get(fenzxh), daxDay.getRiq());
		if(fenzxDay.getGongs() > 24 || fenzxpcjhList == null){
			return;
		}
		// 当日可完成的备件外销零件数量
		Map<String, Integer> countMap = sumFenzxpcjhCount(fenzx, fenzxDay, fenzxpcjhList);
		// 当日可完成的备件外销计划
		List<Beijwxjh> todayBeijwxjhList = subBeijwxjh(dax, beijwxjhMap.get(fenzxh),
				countMap, fenzx.getFenzxxs(), daxDay.getRiq());
		// 通过备件外销计划生成分装线排产计划（待插入的分装线计划）
		List<Fenzxpcjh> fenzxpcjhList_beijwxjh = createFenzxpcjhByBeijwxjh(
				todayBeijwxjhList, daxDay.getRiq(), TYPE_BEIJWX_INSERT);
		// 侧围线把备件外销计划按左右配对
		if (fenzx.getFenzxxs() == 2) {
			fenzxpcjhList_beijwxjh = sortFenzxpcjhMatching(fenzxpcjhList_beijwxjh);
		}
		// 分装线节拍 > 大线节拍，才考虑备件外销插入
		if(fenzx.getShengcjp() - dax.getShengcjp() > 0){
			// 插入间隔=大线节拍/(分装线节拍-大线节拍)，向上取整
			Double jiange = Math.ceil(Double.valueOf(dax.getShengcjp())
					/ (Double.valueOf(fenzx.getShengcjp()) - Double
							.valueOf(dax.getShengcjp())));
			// 插入备件外销计划
			insertBeijwxjh(fenzxpcjhList, fenzxpcjhList_beijwxjh,
					dax.getShengcjp(), jiange.intValue(),
					fenzx.getFenzxxs());
		}
		if (fenzxpcjhList_beijwxjh.size() > 0) { // 还有备件外销计划未分配
			fenzxpcjhList.addAll(fenzxpcjhList_beijwxjh); // 全部插入在当日的末尾处
		}
		// 重新生成下线顺序号
		rebuildXiaxsx(fenzxpcjhList, daxDay.getRiq());
	}
	
	/**
	 * 汇总分装线计划的数量
	 * @param fenzx
	 * @param fenzxDay
	 * @param fenzxpcjhList
	 * @return
	 */
	private Map<String, Integer> sumFenzxpcjhCount(Fenzx fenzx, CalcDay fenzxDay, List<Fenzxpcjh> fenzxpcjhList){
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		if(fenzx.getFenzxxs() == 1){
			// 当日可完成的备件外销零件数量=分装线节拍*分装线工时-当日分装线的排产计划数量
			int maxLingjCount = (int)(fenzx.getShengcjp() * fenzxDay.getGongs()) - getPracticalDingdSize(fenzxpcjhList);
			resultMap.put("maxLingjCount", Math.max(maxLingjCount, 0));
		}else if(fenzx.getFenzxxs() == 2){
			int maxLingjCount_L = (int)(fenzx.getShengcjp() * fenzxDay.getGongs()) - getPracticalDingdSize(fenzxpcjhList);
			int maxLingjCount_R = (int)(fenzx.getShengcjp() * fenzxDay.getGongs()) - getPracticalDingdSize(fenzxpcjhList);
			resultMap.put("maxLingjCount_L", Math.max(maxLingjCount_L, 0));
			resultMap.put("maxLingjCount_R", Math.max(maxLingjCount_R, 0));
		}
		return resultMap;
	}
	
	/**
	 * 获取实际的订单数量
	 * @param fenzxpcjhList
	 * @return
	 */
	private int getPracticalDingdSize(List<Fenzxpcjh> fenzxpcjhList){
		int size = 0;
		String last_dingdh = "";
		String last_zongch = "";
		for (Fenzxpcjh jh : fenzxpcjhList) {
			if(isDifferentAndExistDingd(jh.getFenzch(), jh.getDingdh(), last_zongch, last_dingdh)){
				size++;//去除排空的订单数量
			}
			last_dingdh = jh.getDingdh();
			last_zongch = jh.getFenzch();
		}
		return size;
	}
	
	
	/**
	 * 备件外销单独排产
	 * @param dax
	 * @param fenzx
	 * @param riq
	 * @param beijwxjhMap
	 * @param fenzxpcjhMap
	 */
	private void fenzxProduceAlone(Dax dax, Fenzx fenzx, CalcDay riq,
			Map<String, List<Beijwxjh>> beijwxjhMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap){
		String fenzxh = fenzx.getFenzxh();	//分装线号
		log.info("分装线"+fenzxh+"在"+riq.getRiq()+"备件外销单独排产...");
		//某日某个分装线的排产计划
		List<Fenzxpcjh> fenzxpcjhList = new ArrayList<Fenzxpcjh>();		
		//获取分装线的当日日期信息
		CalcDay day = getFenzxCurrentCalcDay(calcDays.get(fenzxh), riq.getRiq());	
		if(day.getShifgzr().equals("1") &&  beijwxjhMap.get(fenzxh) != null 
				&& beijwxjhMap.get(fenzxh).size() != 0){	//分装线为工作日且备件外销计划不为空
			// 当日可完成的备件外销零件数量
			Map<String, Integer> countMap = sumFenzxpcjhCount(fenzx, day, fenzxpcjhList);
			//可完成的备件外销计划
			List<Beijwxjh> subList = subBeijwxjh(dax, beijwxjhMap.get(fenzxh),
					countMap, fenzx.getFenzxxs(), riq.getRiq());
			//根据备件外销计划生成分装线排产顺序
			fenzxpcjhList = createFenzxpcjhByBeijwxjh(subList, riq.getRiq(), TYPE_BEIJWX_PRODUCEALONE);
			if(fenzx.getFenzxxs() == 2){	//侧围线
				//重新排序，按照零件类型左右配对
				fenzxpcjhList = sortFenzxpcjhMatching(fenzxpcjhList);
			}
			//添加上线顺序号
			rebuildXiaxsx(fenzxpcjhList, riq.getRiq());
			//将数据加入到fenzxpcjhMap中
			addToResult(fenzxh, riq.getRiq(), fenzxpcjhList, fenzxpcjhMap);
		}
	}
	
	/**
	 * 插入备件外销计划
	 * @param fenzxpcjhList 分装线排产计划
	 * @param beijwxList	备件外销计划
	 * @param daxjp			大线节拍
	 * @param jiange		插入间隔
	 * @param xis			分装线系数
	 */
	private void insertBeijwxjh(List<Fenzxpcjh> fenzxpcjhList, List<Fenzxpcjh> beijwxList
			, int daxjp, int jiange, int xis){
		
		//将一天的分装线排产计划按大线节拍分割成每个小时的分装线排产计划
		Map<Integer, List<Fenzxpcjh>> everyHourMap = spiltEveryHourMap(fenzxpcjhList, daxjp, jiange, xis);
		//每隔“插入间隔”个零件，插入一个备件外销零件
		int beijwxIndex = 0;
		String last_dingdh = "";
		String last_zongch = "";
		Fenzxpcjh jh;
		A:for (Integer h : everyHourMap.keySet()) {
			List<Fenzxpcjh> hourFenzxpcjhList = everyHourMap.get(h);
			int dingd_num = 0;
			B:for (int i = 0; i < hourFenzxpcjhList.size(); i++) {
				jh = hourFenzxpcjhList.get(i);
				//订单计数器++
				if(isDifferentAndExistDingd(jh.getFenzch(), jh.getDingdh(), last_zongch, last_dingdh) && !jh.isBeijwx()){
					dingd_num++;
				}
				//检查侧围线的备件外销计划，当侧围线只剩下单边零件时（只有L或者只有R），一次插入一个计划
				int count = checkXis(xis, beijwxList);
				if(dingd_num == jiange + 1){	//找到间隔“插入间隔”个后的第一个零件
					for (int j = 0; j < count; j++) {	//侧围线一次插入一对备件外销零件
						if(beijwxIndex < beijwxList.size()){	//还有备件外销零件
							hourFenzxpcjhList.add(i + j , beijwxList.get(beijwxIndex));	//插入在“插入间隔”零件的后一个之前
							beijwxIndex++;
							dingd_num = 0;
							//dingd_num = 0 - j;
						}else{
							break A;	//备件外销插入完毕
						}
					}
				}else if(i == hourFenzxpcjhList.size() - xis){	//每小时的最后一个分装线排产计划
					for (int j = 0; j < count; j++) {	//侧围线一次插入一对备件外销零件
						if(beijwxIndex < beijwxList.size()){	//还有备件外销零件
							hourFenzxpcjhList.add(hourFenzxpcjhList.size(), beijwxList.get(beijwxIndex));
							beijwxIndex++;
							if(j + 1 == count){
								break B;	//继续插入下一小时备件外销计划
							}
						}else{
							break A;
						}
					}
				}else{
					last_dingdh = jh.getDingdh();
					last_zongch = jh.getFenzch();
				}
			}
		}
		removeList(beijwxList, 0, beijwxIndex);	//移除已分配的备件外销计划
		fenzxpcjhList.clear();
		for (Integer h : everyHourMap.keySet()) {
			fenzxpcjhList.addAll(everyHourMap.get(h));
		}
	}
	
	/**
	 * 将一天的分装线排产计划按大线节拍分割成每个小时的分装线排产计划
	 * @param fenzxpcjhList
	 * @param daxjp
	 * @param jiange
	 * @param xis
	 * @return
	 */
	private Map<Integer, List<Fenzxpcjh>> spiltEveryHourMap(List<Fenzxpcjh> fenzxpcjhList 
			, int daxjp, int jiange, int xis){
		//每小时的分装线排产计划
		Map<Integer, List<Fenzxpcjh>> everyHourMap = new TreeMap<Integer, List<Fenzxpcjh>>();	
		//当前小时
		int hour = 1;
		// 订单数量
		int dingdCount = 0;
		// 上一个订单号
		String last_dingdh = "";
		String last_zongch = "";
		Fenzxpcjh jh;
		for (int i = 0; i < fenzxpcjhList.size(); i++) {
			jh = fenzxpcjhList.get(i);
			// 订单数量达到一个小时的节拍，继续放在下一小时中，考虑到侧围线情况，要等到订单号不同时，才开始下一个小时
			if(dingdCount  == daxjp && (jh.getDingdh() == null || !jh.getDingdh().equals(last_dingdh))){
				dingdCount = 0;
				hour++;	
			}
			if(everyHourMap.containsKey(hour)){
				everyHourMap.get(hour).add(jh);
			}else {
				List<Fenzxpcjh> hourList = new ArrayList<Fenzxpcjh>();
				hourList.add(jh);
				everyHourMap.put(hour, hourList);
			}
			if(isDifferentAndExistDingd(jh.getFenzch(), jh.getDingdh(), last_zongch, last_dingdh)){
				dingdCount++;
			}
			last_dingdh = jh.getDingdh();
			last_zongch = jh.getFenzch();
		}
		return everyHourMap;
	}
	
	/**
	 * 检查侧围线的备件外销计划
	 * @param xis
	 * @param beijwxList
	 * @return
	 */
	private int checkXis(int xis, List<Fenzxpcjh> beijwxList){
		//侧围线且剩余备件外销数量大于2
		if(xis == 2 && beijwxList.size() >= 2){	
			//都是同一种零件类型
			if(beijwxList.get(0).getLingjlx().equals(beijwxList.get(1).getLingjlx())){	
				//一次插入一个
				xis = 1;	
			}
		}
		return xis;
	}
	
	/**
	 * 均衡分装线排产计划（偏移数据）
	 * @param allResult
	 * @param shunx	s:上线顺序	x:下线顺序
	 * @throws ParseException
	 */
	private void calcFenzxOffLinePlans(Dax dax, Map<String, Map<String, List<Fenzxpcjh>>> allResult
			, Map<String, Fenzx> fenzxMap) throws ParseException{
		log.info("推算分装线下线计划中...");
		//计算日期
		List<CalcDay> dayList = calcDays.get(dax.getDaxxh());
		for (String fenzxh : allResult.keySet()) {	//循环分装线
			//分装线
			Fenzx fenzx = fenzxMap.get(fenzxh);	
			//分装线的所有排产计划
			Map<String, List<Fenzxpcjh>> fenzxResult = allResult.get(fenzxh);	
			//分装线的所有排产计划（由上面的map转换而来）
			List<Fenzxpcjh> servalDaysFenzxpcjh = new ArrayList<Fenzxpcjh>();
			//分装线每日的排产计划数量
			Map<String, Integer> dayFenzxpcjhCount = new HashMap<String, Integer>();
			//汇总每日的分装线排产数量
			sumDailyCountOffLine(fenzxResult, servalDaysFenzxpcjh, dayFenzxpcjhCount);
			//计算偏移量
			int moveLength = calcMoveLength(fenzx);	
			if(moveLength == 0){	//偏移量为0，继续下个分装线的偏移
				continue;
			}
			if(moveLength < 0){
				//偏移量小于0，向前偏移
				moveAheadFenzxpcjh(dayList, dayFenzxpcjhCount, servalDaysFenzxpcjh, fenzxResult, moveLength,dax,fenzx);
			}else{	
				//偏移量大于0，向后偏移
				moveBehindFenzxpcjh(dayList, dayFenzxpcjhCount, servalDaysFenzxpcjh, fenzxResult, moveLength,dax,fenzx);
			}
			servalDaysFenzxpcjh.clear();
			dayFenzxpcjhCount.clear();
		}
	}
	
	/**
	 * 计算分装线的上线顺序
	 * @param dax
	 * @param allResult
	 * @param fenzxMap
	 * @param zhuxpcjhMap
	 * @param paiclsMap
	 */
	private void calcFenzxOnLinePlans(Dax dax, Map<String, Map<String, List<Fenzxpcjh>>> allResult
			, Map<String, Fenzx> fenzxMap, Map<String, Paicls> paiclsMap){
		log.info("推算分装线上线计划中...");
		for (String fenzxh : allResult.keySet()) {
			//分装线的所有排产计划
			Map<String, List<Fenzxpcjh>> fenzxResult = allResult.get(fenzxh);
			Fenzx fenzx = fenzxMap.get(fenzxh);
			//排产流水
			Paicls paicls = paiclsMap.get(fenzxh);
			//分装线的所有排产计划（由上面的map转换而来）
			List<Fenzxpcjh> servalDaysFenzxpcjh = new ArrayList<Fenzxpcjh>();
			Map<String, Integer> dayFenzxpcjhCount = new HashMap<String, Integer>();
			//汇总每日的分装线排产数量
			sumDailyCountOnLine(dax, fenzxResult, servalDaysFenzxpcjh, paicls, dayFenzxpcjhCount, fenzx);
			//偏移分装线计划生成上线顺序
			moveFenzxpcjhByZuihjscl(fenzxResult, servalDaysFenzxpcjh, dayFenzxpcjhCount, paicls, fenzx);
		}
	}
	
	/**
	 * 偏移分装线计划生成上线顺序
	 * @param fenzxResult
	 * @param servalDaysFenzxpcjh
	 * @param dayFenzxpcjhCount
	 * @param paicls
	 * @param fenzx
	 */
	private void moveFenzxpcjhByZuihjscl(Map<String, List<Fenzxpcjh>> fenzxResult, List<Fenzxpcjh> servalDaysFenzxpcjh
			, Map<String, Integer> dayFenzxpcjhCount, Paicls paicls, Fenzx fenzx){
		// 此次排产第一个上线计划index
		int centerIndex = 0;
		if(servalDaysFenzxpcjh.isEmpty()){
			return;
		}
		boolean endFlag = false;
		if(paicls != null && !Strings.isNullOrEmpty(paicls.getZuihjscl()) 
				&& !"NULL".equals(paicls.getZuihjscl())){
			for (int i = 0; i < servalDaysFenzxpcjh.size(); i++) {
				if(paicls.getZuihjscl().equals(servalDaysFenzxpcjh.get(i).getDingdh())){
					endFlag = true;
				}
				if(endFlag && !paicls.getZuihjscl().equals(servalDaysFenzxpcjh.get(i).getDingdh())){
					centerIndex = i;
					break;
				}
			}
		}
		if(!endFlag){
			for (int i = 0; i < servalDaysFenzxpcjh.size(); i++) {
				if(servalDaysFenzxpcjh.get(i).getYujxxrq().compareTo(paickssj) >= 0){
					centerIndex = i;
					break;
				}
			}
			centerIndex += (fenzx.getChews()*fenzx.getFenzxxs());
		}
		
		//插空偏移量不为空，则上线计划从插空偏移量后开始计算
		if(paicls!=null && !Strings.isNullOrEmpty(paicls.getChakpyl())){
		centerIndex+=Integer.parseInt(paicls.getChakpyl());
		}
		int nextIndex = centerIndex;
		List<CalcDay> dayList = calcDays.get(fenzx.getDaxxh());
		// 生成排产开始日期后的上线计划
		for (int i = 0; i < dayList.size(); i++) {
			String riq = dayList.get(i).getRiq();
			if(riq.compareTo(paickssj) >= 0 && dayFenzxpcjhCount.containsKey(riq)){
				nextIndex = Math.min(centerIndex + dayFenzxpcjhCount.get(riq), servalDaysFenzxpcjh.size());
				List<Fenzxpcjh> list = fenzxResult.get(riq);
				list.clear();
				list.addAll(servalDaysFenzxpcjh.subList(centerIndex, nextIndex));
				removeList(servalDaysFenzxpcjh, centerIndex, nextIndex);
				//重新生成顺序以及日期
				rebuildShangxsx(list, riq);
			}
		}
		nextIndex = centerIndex;
		// 生成排产开始日期前的上线计划
		for (int i = dayList.size() - 1; i >= 0; i--) {
			String riq = dayList.get(i).getRiq();
			if(riq.compareTo(paickssj) < 0 && dayFenzxpcjhCount.containsKey(riq)){
				nextIndex = Math.max(nextIndex - dayFenzxpcjhCount.get(riq), 0);
				List<Fenzxpcjh> list = fenzxResult.get(riq);
				list.clear();
				list.addAll(servalDaysFenzxpcjh.subList(nextIndex, centerIndex));
				removeList(servalDaysFenzxpcjh, nextIndex, centerIndex);
				centerIndex = nextIndex;
				//重新生成顺序以及日期
				rebuildShangxsx(list, riq);
			}
		}
		//把最前面多的一部分计划放入计划开始日期中，不把它丢掉
		if(centerIndex > 0){
			if(fenzxResult.get(jiskssj)==null){
				fenzxResult.put(jiskssj, new ArrayList<Fenzxpcjh>());
			}
			fenzxResult.get(jiskssj).addAll(0, servalDaysFenzxpcjh.subList(0, centerIndex));
			//重新生成顺序以及日期
			rebuildShangxsx(fenzxResult.get(jiskssj), jiskssj);
		}
	}
	
	
	/**
	 * 汇总每日的分装线排产数量(下线)
	 * @param fenzxResult
	 * @param servalDaysFenzxpcjh
	 * @param dayFenzxpcjhCount
	 * @param zhuxpcjhMap
	 * @param position
	 */
	private void sumDailyCountOffLine(Map<String, List<Fenzxpcjh>> fenzxResult, List<Fenzxpcjh> servalDaysFenzxpcjh
			, Map<String, Integer> dayFenzxpcjhCount){
		for (String riq : fenzxResult.keySet()) {	// 循环日期
			List<Fenzxpcjh> dayFenzxpcjh = fenzxResult.get(riq);
			dayFenzxpcjhCount.put(riq, dayFenzxpcjh.size());
			servalDaysFenzxpcjh.addAll(dayFenzxpcjh);
		}
	}
	
	/**
	 * 汇总每日的分装线排产数量（上线）
	 * @param dax
	 * @param fenzxResult
	 * @param servalDaysFenzxpcjh分装线的所有排产计划
	 * @param zhuxpcjhMap
	 * @return
	 */
	private void sumDailyCountOnLine(Dax dax, Map<String, List<Fenzxpcjh>> fenzxResult
			, List<Fenzxpcjh> servalDaysFenzxpcjh, Paicls paicls
			, Map<String, Integer> dayFenzxpcjhCount, Fenzx fenzx){
		
		// 每日的首个和末个订单号
		Map<String, String> dayDingdh = new HashMap<String, String>();
		
		// 将所有分装线计划放进list中，并统计每日的第一个订单号和最后一个订单号
		for (String riq : fenzxResult.keySet()) {
			List<Fenzxpcjh> dayFenzxpcjh = fenzxResult.get(riq);
			String firstDingdh = getFirstDingdh(dayFenzxpcjh);
			String lastDingdh = getLastDingdh(dayFenzxpcjh);
			if(firstDingdh != null && lastDingdh != null){
				dayDingdh.put(riq, getFirstDingdh(dayFenzxpcjh)+","+getLastDingdh(dayFenzxpcjh));
			}else{
				dayDingdh.put(riq, null);
			}
			servalDaysFenzxpcjh.addAll(dayFenzxpcjh);//每日的分装线计划
		}
		// 最后计算车辆的订单号
		String zuihjscl = paicls == null ? null : paicls.getZuihjscl();
		
		// 第一个上线计划的index（根据车位数或排产流水计算出）
		int startDingdIndex = -1;
		
		// 计算下线计划~上线计划之间的偏移量
		int moveLength = 0;
		if(Strings.isNullOrEmpty(zuihjscl) || "NULL".equals(zuihjscl)){//没有排产流水的情况
			moveLength = fenzx.getChews() * fenzx.getFenzxxs();
			int xiaxjh_first_index = 0;
			for (int i = 0; i < servalDaysFenzxpcjh.size(); i++) {
				if(paickssj.compareTo(servalDaysFenzxpcjh.get(i).getYujxxrq()) <= 0){
					xiaxjh_first_index = i;
					break;
				}
			}
			startDingdIndex = getIndexByAddDingd(servalDaysFenzxpcjh, fenzx, xiaxjh_first_index, moveLength, true);
		}else{
			// 此次计算的第一个下线计划
			int xiaxjh_first_index = 0;
			// 根据排产流水得到的第一个上线计划
			int paicls_first_index = 0;
			for (int i = 0; i < servalDaysFenzxpcjh.size(); i++) {
				if(xiaxjh_first_index == 0 && 
						paickssj.compareTo(servalDaysFenzxpcjh.get(i).getYujxxrq()) <= 0){
					xiaxjh_first_index = i;//此次计算的第一个下线计划
				}
				if(zuihjscl.equals(servalDaysFenzxpcjh.get(i).getDingdh())){
					paicls_first_index = i;
					startDingdIndex = i + 1;//排产开始日期的第一个上线计划
				}
			}
			for (int i = Math.min(xiaxjh_first_index, paicls_first_index); i < Math.max(xiaxjh_first_index, paicls_first_index); i++) {
				if(!servalDaysFenzxpcjh.get(i).isBeijwx()){
					moveLength ++;//不是备件外销
				}
			}
		}
		// 每日的首个订单，根据偏移量，偏移后的index
		int firstIndex = startDingdIndex;
		// 每日的末个订单，根据偏移量，偏移后的index
		int lastIndex = 0;
		// 将每日的首个计划和最后个计划，偏移后，得到的每日排产数量
		for (CalcDay daxDay : calcDays.get(dax.getDaxxh())) {
			String riq = daxDay.getRiq();
			String dingdhs = dayDingdh.get(riq);
			if (daxDay.getShifgzr().equals("1") 
					&& daxDay.getRiq().compareTo(paickssj) >= 0 
					&& dingdhs != null && moveLength != 0) { //大线工作日
//				String firstDingdh = dingdhs.split(",")[0];
				String lastDingdh = dingdhs.split(",")[1];
				Fenzxpcjh jh;
				for (int i = 0; i < servalDaysFenzxpcjh.size(); i++) {
					jh = servalDaysFenzxpcjh.get(i);
//					if(firstDingdh.equals(jh.getDingdh()) && firstIndex == startDingdIndex){
//						// 每日的首个计划，按照偏移量，偏移后的index，只有排产日期后的第一天会计算，以后会根据lastIndex + 1得到 
//						firstIndex = getIndexByAddDingd(servalDaysFenzxpcjh, fenzx, i, moveLength, true);
//					}
					if(lastDingdh.equals(jh.getDingdh())){//从当天计划的最后一个计划开始偏移分装线车身数的数量得到的就是当日的排产量
						// 侧围线情况，如果下一个计划的订单号相同的话，那么结束index应该以后一个相同订单的计划index为准
						if(i + 1 < servalDaysFenzxpcjh.size() && lastDingdh.equals(servalDaysFenzxpcjh.get(i + 1).getDingdh())){
							continue;
						}
						// 每日的末个计划，按照偏移量，偏移后的index
						lastIndex = getIndexByAddDingd(servalDaysFenzxpcjh, fenzx, i, fenzx.getChews()*fenzx.getFenzxxs(), false);
					}
				}
				// 根据index，去掉备件外销插入的数量，得到当日的排产量，和下一日首个计划偏移后的index
				firstIndex = removeProduceAloneCount(servalDaysFenzxpcjh, firstIndex, lastIndex, 
						dayFenzxpcjhCount, daxDay.getRiq(), fenzx);
			}else{	//大线非工作日
				// 取当日的计划数量
				if(fenzxResult.get(daxDay.getRiq()) != null && !fenzxResult.get(daxDay.getRiq()).isEmpty()){
					dayFenzxpcjhCount.put(daxDay.getRiq(), fenzxResult.get(daxDay.getRiq()).size());
				}
			}
		}
	}
	
	/**
	 * 根据起始index，得到分装计划的数量（去除备件单独排产），返回下次的起始index
	 * @param list
	 * @param startIndex
	 * @param endIndex
	 * @param dayFenzxpcjhCount
	 * @param riq
	 * @param fenzx
	 * @return
	 */
	private int removeProduceAloneCount(List<Fenzxpcjh> list, int startIndex, int endIndex, 
			Map<String, Integer> dayFenzxpcjhCount, String riq, Fenzx fenzx){
		// 非备件单独排产的计划数量
		int count = 0;
		int nextIndex = endIndex + 1;
		// 分装线产能
		int capacity = getFenzxCapacity(fenzx, riq);
		if(fenzx.getFenzxxs() == 1 && capacity > 0){
			// 总成为排空生成或未拆分生成的数量
			int empty_count = 0;
			for (int i = startIndex; i <= Math.min(endIndex, list.size()-1); i++) {
			//	if(!TYPE_BEIJWX_PRODUCEALONE.equals(list.get(i).getLeix())){//备件外销单独排产要参与上线偏移
					count++;
			//	}
				if(isDefaultOrIndivisibleZongc(list.get(i).getFenzch())){
					empty_count++;//排空或未拆分的总成
				}
				// 产能不考虑排空或未拆分的总成
				if(count - empty_count>= capacity){
					nextIndex = i+1;
					break;
				}
			}
		}else if(fenzx.getFenzxxs() == 2 && capacity > 0){
			int count_L = 0;
			int count_R = 0;
			int empty_count_L = 0;
			int empty_count_R = 0;
			for (int i = startIndex; i <= Math.min(endIndex, list.size()-1); i++) {
				//if(!TYPE_BEIJWX_PRODUCEALONE.equals(list.get(i).getLeix())){//备件外销单独排产要参与上线偏移
					if("L".equals(list.get(i).getLingjlx())){
						count_L++;
					}else if("R".equals(list.get(i).getLingjlx())){
						count_R++;
					}
					count++;
			//	}
				if("L".equals(list.get(i).getLingjlx()) && isDefaultOrIndivisibleZongc(list.get(i).getFenzch())){
					empty_count_L++;
				}else if("R".equals(list.get(i).getLingjlx()) && isDefaultOrIndivisibleZongc(list.get(i).getFenzch())){
					empty_count_R++;
				}
				if(count_L - empty_count_L > capacity || count_R - empty_count_R > capacity){
					count--;
					nextIndex = i;
					break;
				}
			}
		}
		dayFenzxpcjhCount.put(riq, count);
		return nextIndex;
	}
	
	/**
	 * 获取指定startIndex后面moveLength个订单的index
	 * @param list
	 * @param startIndex
	 * @param moveLength
	 * @return
	 */
	private int getIndexByAddDingd(List<Fenzxpcjh> list, Fenzx fenzx, int startIndex, int moveLength, boolean stopNow){
		int remain = moveLength;
		if(remain > 0){
			for (int i = startIndex + 1; i < list.size(); i++) {
				//备件外销是不需要算排产量的，如果算排产量会导致本身的产量排少，包含"temp_"为备件外销和排空
				if(list.get(i).getDingdh() != null  && !list.get(i).getDingdh().contains("temp_")){
					remain--;
				}
				if(stopNow){
					if(remain == 0){
						return i;
					}
				}else{
					if(remain < 0){
						return i-1;//偏移分装线车身数的下标
					}
				}
			}
		}else if(remain < 0){
			for (int i = startIndex - 1; i >= 0; i--) {
				if(list.get(i).getDingdh() != null && !list.get(i).getDingdh().contains("temp_")){
					remain++;
				}
				if(stopNow){
					if(remain == 0){
						return i;
					}
				}else{
					if(remain > 0){
						return i+1;
					}
				}
			}
		}else{
			return startIndex;
		}
		return list.size()-1;
	}
	
	/**
	 * 获取list中第一个订单号
	 * @param list
	 * @return
	 */
	private String getFirstDingdh(List<Fenzxpcjh> list){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getDingdh() != null){
				return list.get(i).getDingdh();
			}
		}
		return null;
	}
	
	
	/**
	 * 获取list中最后一个订单号
	 * @param list
	 * @return
	 */
	private String getLastDingdh(List<Fenzxpcjh> list){
		for (int i = list.size() - 1; i >= 0; i--) {
			if(list.get(i).getDingdh() != null){
				return list.get(i).getDingdh();
			}
		}
		return null;
	}
	
	/**
	 * 计算偏移量
	 * @param fenzx
	 * @param position
	 * @return
	 */
	private int calcMoveLength(Fenzx fenzx){
		// 偏移量 = (分装线车身数量（分装线对应消耗点到大线的距离） - 分装线到大线悬链总成数)*分装线系数
		return (fenzx.getChessl()-fenzx.getFenzxddxxlzcs()) * fenzx.getFenzxxs();
	}
	
	/**
	 * 向前偏移分装线排产计划
	 * @param dayList	大线工作日
	 * @param dayFenzxpcjhCount	每日的分装线排产数量
	 * @param servalDaysFenzxpcjh	几日的分装线排产计划
	 * @param fenzxResult	该分装线所有的排产计划
	 * @param moveLength	偏移量
	 */
	private void moveAheadFenzxpcjh(List<CalcDay> dayList, Map<String, Integer> dayFenzxpcjhCount, 
			List<Fenzxpcjh> servalDaysFenzxpcjh, Map<String, List<Fenzxpcjh>> fenzxResult, 
			int moveLength,Dax dax,Fenzx fenzx){
		boolean firstFlag = true;
		for (int k = 0; k < dayList.size(); k++) {
			String riq = dayList.get(k).getRiq();
			Integer count = dayFenzxpcjhCount.get(riq);//当日的分装线排产数量
			List<Fenzxpcjh> dayFenzxpcjh = fenzxResult.get(riq);
			if(dayFenzxpcjh != null && count != null){
				dayFenzxpcjh.clear();
			}
			if(count == null || servalDaysFenzxpcjh.size() == 0){
				continue;
			}
			//截取结束角标
			int subEndIndex = count;
			if(firstFlag){
				if(riq.compareTo(paickssj) < 0){
					//第一天且不在排产日期范围内，则使应该偏移出去的计划放在第一天中保留，避免缺少数据
					subEndIndex -= moveLength;
				}else {
					//第一天且在排产日期范围内，则不保留偏移的计划，避免第一天的计划量增大
					removeList(servalDaysFenzxpcjh, 0, Math.abs(moveLength));
				}
			}
			subEndIndex = subEndIndex > servalDaysFenzxpcjh.size() ? servalDaysFenzxpcjh.size() : subEndIndex;
			//从0开始截取原始分装线数量的分装线排产计划
			dayFenzxpcjh.addAll(servalDaysFenzxpcjh.subList(0, subEndIndex));
			//移除被截取的部分
			removeList(servalDaysFenzxpcjh, 0, subEndIndex);
			//重新生成顺序以及日期
			rebuildXiaxsx(dayFenzxpcjh, riq);
			firstFlag = false;
		}
	}
	
	/**
	 * 向前偏移分装线排产计划
	 * @param dayList	大线工作日
	 * @param dayFenzxpcjhCount	每日的分装线排产数量
	 * @param servalDaysFenzxpcjh	几日的分装线排产计划
	 * @param fenzxResult	该分装线所有的排产计划
	 * @param moveLength	偏移量
	 */
	private void moveBehindFenzxpcjh(List<CalcDay> dayList, Map<String, Integer> dayFenzxpcjhCount, 
			List<Fenzxpcjh> servalDaysFenzxpcjh, Map<String, List<Fenzxpcjh>> fenzxResult, 
			int moveLength,Dax dax,Fenzx fenzx){
		//下一个athen工作日 = 当前日期 + 1大线工作日
	//	String nextWorkingday = addDays(jisjssj, 1, daxWorkingDays);
		//虚拟出这个工作日
	//	createVirtualDay(nextWorkingday, dax,fenzx);
	//	fenzxResult.put(dayList.get(dayList.size()-1).getRiq(),new ArrayList<Fenzxpcjh>());
		//为空证明没有超产能的计划被转过来，不为空说明超出产能的计划被转到虚拟的工作日来了
		if(fenzxResult.get(dayList.get(dayList.size()-1).getRiq())==null){
			fenzxResult.put(dayList.get(dayList.size()-1).getRiq(),new ArrayList<Fenzxpcjh>());	
			dayFenzxpcjhCount.put(dayList.get(dayList.size()-1).getRiq(), moveLength);//最后一天的数量为偏移的数量		
		}else{
			dayFenzxpcjhCount.put(dayList.get(dayList.size()-1).getRiq(), fenzxResult.get(dayList.get(dayList.size()-1).getRiq()).size()+moveLength);//最后一天的数量加上偏移过来的数量
		}
		for (int k = dayList.size() - 1; k >=0 ; k--) {
			String riq = dayList.get(k).getRiq();
			Integer count = dayFenzxpcjhCount.get(riq);
			List<Fenzxpcjh> dayFenzxpcjh = fenzxResult.get(riq);
			if(dayFenzxpcjh != null && count != null){
				dayFenzxpcjh.clear();
			}
			if(count == null || servalDaysFenzxpcjh.size() == 0){
				continue;
			}
			int subStartIndex=Math.max(servalDaysFenzxpcjh.size() - count, 0);
			dayFenzxpcjh.addAll(servalDaysFenzxpcjh.subList(subStartIndex, servalDaysFenzxpcjh.size()) );
			//移除被截取的部分
			removeList(servalDaysFenzxpcjh, subStartIndex, servalDaysFenzxpcjh.size());
			//重新生成顺序以及日期
			rebuildXiaxsx(dayFenzxpcjh, riq);
		}
	}
	
	/**
	 * 偏移到前面的日期
	 * @param day	日期
	 * @param fenzxh	分装线号
	 * @param addDays	提前天数
	 * @param fenzxResult	几天的分装线排产计划
	 * @param extraResult	需要偏移的分装线排产计划
	 * @throws ParseException
	 */
	private void moveToPreviousDay(String day, Fenzx fenzx, int addDays,
			Map<String, List<Fenzxpcjh>> fenzxResult, List<Fenzxpcjh> extraResult, String shunx) throws ParseException{
		String previousDay = getChanxWorkingDay(fenzx.getFenzxh(), day, addDays).getRiq();	//分装线前一工作日
		if(!day.equals(previousDay) && previousDay != null){	//有前一工作日
			List<Fenzxpcjh> priviousFenzxpcjh = fenzxResult.get(previousDay);	//前一工作日的分装线排产计划
			if(priviousFenzxpcjh == null){	//前一天没有分装线排产计划
				moveToPreviousDay(previousDay, fenzx, addDays--, fenzxResult, extraResult, shunx);	//移到前两天
			}else{
				priviousFenzxpcjh.addAll(extraResult);	//添加到前一工作日的分装线排产计划中
				if(shunx.equals(Const.FENZXPC_OFFLINE)){
					rebuildXiaxsx(priviousFenzxpcjh, previousDay);	//重新生成下线顺序号和预计上线时间
				}else if(shunx.equals(Const.FENZXPC_ONLINE)){
					rebuildShangxsx(priviousFenzxpcjh, previousDay);	//重新生成上线顺序号和预计上线时间
				}
			}
		}
	}
	
	/**
	 * 偏移到后面的日期
	 * @param day	日期
	 * @param fenzxh	分装线号
	 * @param addDays	提前天数
	 * @param fenzxResult	几天的分装线排产计划
	 * @param extraResult	需要偏移的分装线排产计划
	 * @throws ParseException
	 */
	private void moveToNextDay(String day, String fenzxh, int addDays,
			Map<String, List<Fenzxpcjh>> fenzxResult, int moveCount){
		List<Fenzxpcjh> todayFenzxpcjh = fenzxResult.get(day);	//分装线当日的排产计划
		String nextDay  = getChanxWorkingDay(fenzxh, day, addDays).getRiq();	//分装线后一工作日
		if(!day.equals(nextDay) && nextDay != null){	//有后一工作日
			List<Fenzxpcjh> nextFenzxpcjh = fenzxResult.get(nextDay);	//后一工作日的分装线排产计划
			if(nextFenzxpcjh == null){	//后一天没有分装线排产计划
				moveToNextDay(nextDay, fenzxh, addDays++, fenzxResult, moveCount);	//移动后两天
			}else{
				//截取要转移的分装线排产计划
				List<Fenzxpcjh> tempList = todayFenzxpcjh.subList(todayFenzxpcjh.size() - moveCount, todayFenzxpcjh.size());	
				nextFenzxpcjh.addAll(0, tempList);	//添加到后一工作日的分装线排产计划开始处
				removeList(todayFenzxpcjh, todayFenzxpcjh.size() - moveCount, todayFenzxpcjh.size());//删除要转移的部分
				rebuildShangxsx(nextFenzxpcjh, nextDay);	//重新生成上线顺序号和预计上线时间
			}
		}
	}
	
	/**
	 * 从后面的工作日偏移分装线排产计划到当日
	 * @param day
	 * @param fenzxh
	 * @param addDays
	 * @param fenzxResult
	 * @param removeCount
	 */
	private void moveFromNextDay(String day, String fenzxh, int addDays,
			Map<String, List<Fenzxpcjh>> fenzxResult, int removeCount, List<Fenzxpcjh> todayFenzxpcjh){
		if(removeCount > 0){
			String nextDay = getChanxWorkingDay(fenzxh, day, addDays).getRiq();	//后一工作日
			if(!day.equals(nextDay)){	//有后一工作日
				List<Fenzxpcjh> nextFenzxpcjh = fenzxResult.get(nextDay);	//后一工作日的分装线排产计划
				if(nextFenzxpcjh == null){	//后一天没有分装线排产计划
					moveFromNextDay(nextDay, fenzxh, addDays++, fenzxResult, removeCount, todayFenzxpcjh);	//移动后两天
				}else{
					if(nextFenzxpcjh.size() >= removeCount){	//后一天可转移要转移的数量
						List<Fenzxpcjh> tempList = nextFenzxpcjh.subList(0, removeCount);	//截取分装线排产计划
						todayFenzxpcjh.addAll(tempList);	//当日添加要转移的部分
						removeList(nextFenzxpcjh, 0, removeCount);	//后一日删除要转移的部分
						rebuildShangxsx(todayFenzxpcjh, day);	//重新生成当日计划的上线顺序
						rebuildShangxsx(nextFenzxpcjh, nextDay);		//重新生成下一日的上线顺序
					}else {	//满足不了转移数量，从后面工作日继续转移
						todayFenzxpcjh.addAll(nextFenzxpcjh);	//添加后一天所有的计划
						nextFenzxpcjh.clear();	//后一天移除所有计划
						rebuildShangxsx(todayFenzxpcjh, day);	//重新生成当日计划的上线顺序
						moveFromNextDay(nextDay, fenzxh, addDays, fenzxResult, removeCount-nextFenzxpcjh.size(), todayFenzxpcjh);
					}
				}
			}
		}
	}
	
	/**
	 * 分装线排空
	 * @param dax
	 * @param fenzx
	 * @param fenzxpcjhList
	 * @param paicslMap
	 */
	private void addEmptyFenzxpcjh(Dax dax, Map<String, Fenzx> fenzxMap,Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, 
			Map<String, List<Paicsl>> paicslMap){
		for (CalcDay day : calcDays.get(dax.getDaxxh())) {//循环计算日期
			if(day.getShifgzr().equals("1")){	//分装线工作日
				for (String fenzxh : fenzxpcjhMap.keySet()) {
					Fenzx fenzx = fenzxMap.get(fenzxh);	//分装线
					List<Fenzxpcjh> fenzxpcjhList = fenzxpcjhMap.get(fenzxh).get(day.getRiq());	//分装线排产计划
					if(paicslMap.get(day.getRiq()) != null && fenzxpcjhList != null){	//排产数量和分装线计划都不为空
						for (Paicsl paicsl : paicslMap.get(day.getRiq())){
							if(paicsl.getXianh().equals(fenzx.getFenzxh())){	//找到分装线的排产数量
								Integer jihsxl = paicsl.getJihsxl();	//计划上线量
								Integer jihxxl = paicsl.getJihxxl();	//计划下线量
								if(jihsxl != null && jihxxl != null && jihsxl < jihxxl){	//计划上线量<计划下线量
									log.info("分装线"+fenzx.getFenzxh()+"排空，数量为："+ (jihxxl - jihsxl)+"...");
									int shangxsx = 0;	//上线顺序
									if(fenzxpcjhList != null && fenzxpcjhList.size() != 0){
										shangxsx = fenzxpcjhList.get(fenzxpcjhList.size()-1).getShangxsx();	//上线顺序接着最后一个计划
									}
									for (int i = 0; i < jihxxl - jihsxl; i++) {
										shangxsx++;
										Fenzxpcjh jh = new Fenzxpcjh();
										jh.setYujsxrq(day.getRiq());			//预计上线时间
										jh.setUsercenter(fenzx.getUsercenter());	//用户中心
										jh.setFenzxh(fenzx.getFenzxh());			//分装线号
										jh.setShangxsx(shangxsx);						//上线顺序
										jh.setYujsxrq(day.getRiq());			//预计上线日期
										jh.setXuqly(dax.getDaxlx());				//需求来源
										jh.setBeijwx(false);						//是否备件外销计划
										jh.setFenzch(DEFAULT_ZONGCH);					//分总成号为排空默认值
										jh.setShiffzxpk("1");//是分装线排空
										fenzxpcjhList.add(jh);
									}
								}
							}
						}
					}
					
				}
			}
		}
	}
	
	
	/**
	 * 第一次排产或长期未排产，会将排产日期之前的部分也保存
	 * @param fenzx
	 * @param fenzxpcjhList
	 * @param day
	 * @param paicslMap
	 * @param noFenzxpcjhDayList
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, List<Fenzxpcjh>>> specialOperation(Dax dax, Map<String, Fenzx> fenzxMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap , 
			Map<String, List<Paicsl>> paicslMap, Map<String, List<String>> fenzxpcjhDayMap, Map<String, Map<String, List<Fenzxpcjh>>> specialResult) throws Exception{
		//分装线对应大线的排产数量
		Paicsl paicsl_dax = findPaicsl(paicslMap.get(paickssj), dax.getDaxxh());	
		if(paicsl_dax != null){ //大线排产流水不为空
			//大线计划上线量
		    Integer jihsxl_dax = paicsl_dax.getJihsxl();	
		    //大线计划下线量
			Integer jihxxl_dax = paicsl_dax.getJihxxl();
			if(jihsxl_dax != null && jihxxl_dax != null && jihsxl_dax > jihxxl_dax){	//大线上线量>大线下线量
				for (String fenzxh : fenzxpcjhMap.keySet()) {
					Fenzx fenzx = fenzxMap.get(fenzxh);
					//取出排产开始时间之前的计划
					List<Fenzxpcjh> beforeList = getBeforeFenzxpcjh(fenzxpcjhMap.get(fenzxh));
					//过去一个月中有分装线排产计划的日期
					List<String> fenzxpcjhDayList= fenzxpcjhDayMap.get(fenzxh);	
					//要转移计划
					List<Fenzxpcjh> extraPreviousResult = new ArrayList<Fenzxpcjh>();
					//偏移量
					int removeLength = fenzx.getChessl()-fenzx.getFenzxddxxlzcs()-fenzx.getChews();	
					if(removeLength < 0){	//有偏移到前一日的计划
						removeLength = Math.min(Math.abs(removeLength), beforeList.size());
						//根据偏移量截取指定数量的计划
						extraPreviousResult = beforeList.subList(beforeList.size()-removeLength, beforeList.size());
						log.info("分装线"+fenzx.getFenzxh()+"处理布线中...");
						Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
						paramMap.put("usercenter", fenzx.getUsercenter());	//用户中心
						paramMap.put("daxxh", fenzx.getDaxxh());	//大线线号
						paramMap.put("fenzxh", fenzx.getFenzxh());	//分装线线号
						paramMap.put("selectBeginDate", CommonFun.getEndDate(paickssj, -1));		//排产开始时间之前一个月
						paramMap.put("selectEndDate", paickssj);	//排产开始时间
						//一个月内的分装线排产数量
						List<Paicsl> paicslList = 
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getAllPaicsl", paramMap);
						//一个月内的分装线工作日
						List<String> workingDayList = 
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getAllWorkingDays", paramMap);
						if(extraPreviousResult.size() > 0){
							//移除已存在的分装线排产计划
							removeExistDingd(extraPreviousResult, fenzxh);
							if(fenzxpcjhDayList == null){
								fenzxpcjhDayList = new ArrayList<String>();
							}
							//转移到前一天
							moveToPreviousDay(extraPreviousResult, paickssj, fenzx, specialResult, paicslList, fenzxpcjhDayList, workingDayList);
						}
					}
					//重新把排产开始时间之前的计划放回去
					returnFenzxpcjh(fenzxpcjhMap.get(fenzxh), beforeList);
				}
			}
		}
		return specialResult;
	}
	
	/**
	 * 移除已存在的分装线排产计划
	 * @param list
	 * @param fenzxh
	 */
	@SuppressWarnings("unchecked")
	private void removeExistDingd(List<Fenzxpcjh> list, String fenzxh){
		if(list != null && list.size() > 0){
			//拼接订单号'A','B'
			StringBuilder dingdh = new StringBuilder();
			for (Fenzxpcjh fenzxpcjh : list) {
				if(fenzxpcjh.getDingdh() != null){
					dingdh.append("'").append(fenzxpcjh.getDingdh()).append("',");
				}
			}
			dingdh.deleteCharAt(dingdh.lastIndexOf(","));
			Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
			paramMap.put("fenzxh", fenzxh);
			paramMap.put("dingdh", dingdh.toString());
			//根据订单号查询分装线排产计划是否存在
			List<String> dingdhList = 
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.findFenzxpcjhByDingdh", paramMap);
			Iterator<Fenzxpcjh> iter = list.iterator();
			while(iter.hasNext()){
				Fenzxpcjh jh = iter.next();
				//分装线排产计划中存在该订单号，移除
				if(dingdhList.contains(jh.getDingdh())){
					iter.remove();
				}
			}
		}
	}
	
	/**
	 * 获取排产开始时间之前的排产计划
	 * @param fenzxpcjhMap
	 * @return
	 */
	private List<Fenzxpcjh> getBeforeFenzxpcjh(Map<String, List<Fenzxpcjh>> fenzxpcjhMap){
		List<Fenzxpcjh> beforList = new ArrayList<Fenzxpcjh>();
		for (String riq : fenzxpcjhMap.keySet()) {
			List<Fenzxpcjh> dayFenzxpcjh = fenzxpcjhMap.get(riq);
			if(riq.compareTo(paickssj) < 0 && dayFenzxpcjh != null){
				beforList.addAll(dayFenzxpcjh);
				//清除分装线排产计划
				dayFenzxpcjh.clear();
			}
		}
		return beforList;
	}
	
	/**
	 * 把排产开始时间之前的计划放回去
	 * @param fenzxpcjhMap
	 * @param beforeList
	 */
	private void returnFenzxpcjh(Map<String, List<Fenzxpcjh>> fenzxpcjhMap, List<Fenzxpcjh> beforeList){
		for (Fenzxpcjh fenzxpcjh : beforeList) {
			if(fenzxpcjhMap.containsKey(fenzxpcjh.getYujsxrq())){
				fenzxpcjhMap.get(fenzxpcjh.getYujsxrq()).add(fenzxpcjh);
			}
		}
	}
	
	/**
	 * 将分装线排产计划转移到前一天
	 * @param extraPreviousResult
	 * @param startDay
	 * @param fenzxh
	 * @param specialResult
	 * @throws Exception 
	 */
	private void moveToPreviousDay(List<Fenzxpcjh> extraPreviousResult,String startDay, Fenzx fenzx, 
			Map<String, Map<String, List<Fenzxpcjh>>> specialResult, List<Paicsl> paicslList,
			List<String> fenzxpcjhDayList, List<String> workingDayList) throws Exception{
		//前一个分装线工作日
		String previousDay = addDays(startDay, -1, workingDayList);
		//排产数量
		Paicsl paicsl = findPaicsl(paicslList, fenzx.getFenzxh(), previousDay);
		if(!previousDay.equals(startDay) && !fenzxpcjhDayList.contains(previousDay)){	//有下一个日期并且没有分装线排产计划
			if(paicsl != null){
				//要转移的计划数量
				int canMoveCount = Math.min(extraPreviousResult.size(), paicsl.getJihsxl());
				//截取可完成的计划
				List<Fenzxpcjh> canFinishList = extraPreviousResult.subList(extraPreviousResult.size()-canMoveCount, extraPreviousResult.size());
				List<Fenzxpcjh> list = new ArrayList<Fenzxpcjh>();
				for (int j = 0; j < canFinishList.size(); j++) {
					Fenzxpcjh jh = canFinishList.get(j);
					jh.setYujsxrq(previousDay);
					jh.setShangxsx(j+1);
					list.add(jh);
				}
				//移除要转移的计划
				removeList(extraPreviousResult, extraPreviousResult.size()-canMoveCount, extraPreviousResult.size());
				addToResult(fenzx.getFenzxh(), previousDay, list, specialResult);
			}
			//前一日的上线数量不能满足偏移量，继续向前一日转移
			if(extraPreviousResult.size() > 0){
				moveToPreviousDay(extraPreviousResult, previousDay, fenzx, specialResult, paicslList, fenzxpcjhDayList, workingDayList);
			}
		}
	}
	
	/**
	 * 获取有排产计划的日期
	 * @param dax
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> getFenzxpcjhDay(Dax dax) throws Exception{
		Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
		paramMap.put("usercenter", dax.getUsercenter());	//用户中心
		paramMap.put("selectBeginDate", CommonFun.getEndDate(jiskssj, -1));	//计算开始时间的之前的一个月
		paramMap.put("selectEndDate", jiskssj);		//计算开始时间
		paramMap.put("daxxh", dax.getDaxxh());	//大线线号
		//查询有排产计划的日期
		List<Map<String, String>> noFenzxpcjhDayList = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getFenzxpcjhDay", paramMap);
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		for (Map<String, String> bean : noFenzxpcjhDayList) { 	//封装成map key：分装线号	value：日期集合
			if(resultMap.containsKey(bean.get("FENZXH"))){
				resultMap.get(bean.get("FENZXH")).add(bean.get("RIQ"));
			}else{
				List<String> list = new ArrayList<String>();
				list.add(bean.get("RIQ"));
				resultMap.put(bean.get("FENZXH"), list);
			}
		}
		return resultMap;
	}
	
	/**
	 * 查询分装线排产流水
	 * @param dax
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Paicls> getFenzxPaicls(Dax dax){
		Map<String, String> paramMap = new HashMap<String, String>();	//查询参数Map
		paramMap.put("usercenter", dax.getUsercenter());	//用户中心
		paramMap.put("dax", dax.getDaxxh());	//大线线号
		//查询分装线排产流水
		List<Paicls> paiclsList = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getFenzxPaicls", dax);
		Map<String, Paicls> resultMap = new HashMap<String, Paicls>();
		for (Paicls paicls : paiclsList) { 	//封装成map key：分装线号	value：排产流水
			resultMap.put(paicls.getFenzxh(), paicls);
		}
		return resultMap;
	}
	
	/**
	 * 找到指定产线的排产数量
	 * @param list
	 * @param xianh
	 * @return
	 */
	private Paicsl findPaicsl(List<Paicsl> list, String xianh){
		if(list != null){
			for (Paicsl paicsl : list){
				if(paicsl.getXianh().equals(xianh)){
					return paicsl;
				}
			}
		}
		return null;
	}
	
	/**
	 * 找到指定产线指定日期的排产数量
	 * @param list
	 * @param xianh
	 * @param day
	 * @return
	 */
	private Paicsl findPaicsl(List<Paicsl> list, String xianh, String day){
		if(list != null){
			for (Paicsl paicsl : list){
				if(paicsl.getXianh().equals(xianh) && paicsl.getRiq().equals(day)){
					return paicsl;
				}
			}
		}
		return null;
	} 
	
	/**
	 * 重新生成分装线的下线顺序
	 * @param list	分装线排产计划
	 * @param riq	偏移日期
	 * @param direction	方向
	 * @throws ParseException
	 */
//	private void rebuildXiaxsx(List<Fenzxpcjh> list, String date, String direction){
//		if(direction.equals(PREVIOUS_DAY)){
//			for (int i = 0; i < list.size(); i++) {
//				Fenzxpcjh jh = list.get(i);
//				if(jh.getYujsxrq().compareTo(date) > 0 && i != 0){	//预计上线日期
//					jh.setYujsxrq(date);
//					jh.setXiaxsx(list.get(i-1).getXiaxsx()+1);
//				}
//			}
//		}else if(direction.equals(NEXT_DAY)){
//			for (int i = list.size() - 1; i >= 0; i--) {
//				Fenzxpcjh jh = list.get(i);
//				if(jh.getYujsxrq().compareTo(date) < 0 && i == list.size() - 1){	//预计上线日期
//					jh.setYujsxrq(date);
//					jh.setXiaxsx(list.size());
//				}else if(jh.getYujsxrq().compareTo(date) < 0 && i != list.size() - 1){
//					jh.setYujsxrq(date);
//					jh.setXiaxsx(list.get(i+1).getXiaxsx()-1);
//				}
//			}
//		}
//	}
	
	/**
	 * 重新生成分装线的上线顺序
	 * @param list	分装线排产计划
	 * @param riq	偏移日期
	 * @param direction	方向
	 * @throws ParseException
	 */
//	private void rebuildShangxsx(List<Fenzxpcjh> list, String date, String direction){
//		if(direction.equals(PREVIOUS_DAY)){
//			for (int i = 0; i < list.size(); i++) {
//				Fenzxpcjh jh = list.get(i);
//				if(jh.getYujsxrq().compareTo(date) > 0 && i == 0){	//预计上线日期
//					jh.setYujsxrq(date);
//					jh.setShangxsx(1);
//				}else if(jh.getYujsxrq().compareTo(date) > 0 && i != 0){	//预计上线日期
//					jh.setYujsxrq(date);
//					jh.setShangxsx(list.get(i-1).getShangxsx()+1);
//				}
//			}
//		}else if(direction.equals(NEXT_DAY)){
//			for (int i = list.size() - 1; i >= 0; i--) {
//				Fenzxpcjh jh = list.get(i);
//				if(jh.getYujsxrq().compareTo(date) < 0 && i != list.size() - 1){
//					jh.setYujsxrq(date);
//					jh.setShangxsx(list.get(i+1).getShangxsx()-1);
//				}
//			}
//		}
//	}
	
	/**
	 * 重新开始生成分装线上线顺序
	 * @param list
	 */
	private void rebuildShangxsx(List<Fenzxpcjh> list){
		for (int i = 0; i < list.size(); i++) {
			Fenzxpcjh jh = list.get(i);
			jh.setShangxsx(i+1);
		}
	}
	
	/**
	 * 重新开始生成分装线上线顺序
	 * @param list
	 */
	private void rebuildShangxsx(List<Fenzxpcjh> list, String riq){
		for (int i = 0; i < list.size(); i++) {
			Fenzxpcjh jh = list.get(i);
			jh.setShangxsx(i+1);
			jh.setYujsxrq(riq);
			if(jh.getYujsxrq().compareTo(jh.getYujxxrq()) > 0){
				jh.setYujxxrq(riq);
			}
		}
	}
	
	/**
	 * 重新开始生成分装线下线顺序
	 * @param list
	 */
	private void rebuildXiaxsx(List<Fenzxpcjh> list, String riq){
		for (int i = 0; i < list.size(); i++) {
			Fenzxpcjh jh = list.get(i);
			jh.setXiaxsx(i+1);
			jh.setYujsxrq(riq);
			jh.setYujxxrq(riq);
		}
	}
	
	
	/**
	 * 总成报废
	 * @param dax
	 * @param fenzxMap
	 * @param fenzxpcjhMap
	 * @param pdsCfxxMap
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private List<Guodsbxx> scrapZongc(Dax dax, Map<String, Fenzx> fenzxMap, Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap,
			Map<String, List<Zongccfjg>> pdsCfxxMap) throws ParseException{
		log.info("大线"+dax.getDaxxh()+"总成报废中...");
		String shengcx = getShengcxStr(dax, fenzxMap);
		//报废总成
		List<Guodsbxx> scrapList = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getScrapZong", shengcx);
		//获取产线零件信息
		Map<String, Zongccfjg> chanxljMap= getChanxlj(dax);
		for (Guodsbxx guodsbxx : scrapList) {
			String zhankrq = guodsbxx.getZhankrq();
			//根据订单关联主线计划得到展开日期，没有则为排产开始时间
			zhankrq = zhankrq == null ? paickssj : zhankrq;
			if(dax.getDaxxh().equals(guodsbxx.getShengcx())){	//总成报废
				//pds拆分信息
				List<Zongccfjg> list = pdsCfxxMap.get(guodsbxx.getZongch()+zhankrq);
				for (String fenzxh : fenzxpcjhMap.keySet()) {
					int count = 0;	//报废总成数量
					String lingjlx = "";
					List<Fenzxpcjh> fenzxpcjhList = fenzxpcjhMap.get(fenzxh).get(paickssj);
					//排产开始日期的为大线非工作日，则将报废总成放在下一个athena工作日中
					if(fenzxpcjhList == null){
						//下一个SPPV工作日
						String nextWorkingday = addDaysByZhuxjh(paickssj, 1);
						while(nextWorkingday != null && fenzxpcjhList == null){
							fenzxpcjhList = fenzxpcjhMap.get(fenzxh).get(nextWorkingday);
							nextWorkingday = addDaysByZhuxjh(nextWorkingday, 1);
						}
					}
					if(list != null){
						for (Zongccfjg zongccfjg : list) {
							if(fenzxh.equals(zongccfjg.getFenzxh())){	//找到拆分到的分装线
								count++;
								lingjlx = zongccfjg.getLingjlx();
								addScrapFenzxpcjh(fenzxpcjhList, guodsbxx, zongccfjg, zhankrq);
							}
						}
					}
					Zongccfjg zongccfjg = new Zongccfjg();
					zongccfjg.setLingj(INDIVISIBLE_ZONGCH);
					zongccfjg.setFenzxh(fenzxh);
					if(count == 0){	//无拆分出来的分装线计划
						if(fenzxMap.get(fenzxh).getFenzxxs() == 1){
							addScrapFenzxpcjh(fenzxpcjhList, guodsbxx, zongccfjg, zhankrq);
						}else if(fenzxMap.get(fenzxh).getFenzxxs() == 2){
							zongccfjg.setLingjlx("R");
							addScrapFenzxpcjh(fenzxpcjhList, guodsbxx, zongccfjg, zhankrq);
							zongccfjg.setLingjlx("L");
							addScrapFenzxpcjh(fenzxpcjhList, guodsbxx, zongccfjg, zhankrq);
						}
					}else if(count == 1 && fenzxMap.get(fenzxh).getFenzxxs() == 2){	//侧围线只有一边能拆分出来
						zongccfjg.setLingjlx(lingjlx == null ? null : lingjlx.equals("L") ? "R" : "L");
						addScrapFenzxpcjh(fenzxpcjhList, guodsbxx, zongccfjg, zhankrq);
					}
					if(fenzxMap.get(fenzxh).getFenzxxs() == 2){	//侧围线重新排序
						List<Fenzxpcjh> sortList = sortFenzxpcjh(fenzxpcjhList.subList(0, 2));	//按照零件左右排序
						removeList(fenzxpcjhList, 0, 2);	//移除报废总成
						fenzxpcjhList.addAll(0, sortList);	//添加排序后的总成
						rebuildShangxsx(fenzxpcjhList);	//重新生成上线顺序
					}
				}
				if(list == null){	//报废总成无法拆分
					//添加报警信息
					addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), paickssj, 
							"总成号为"+guodsbxx.getZongch()+"，展开日期为"+zhankrq+"的报废总成无法找到对应的拆分信息", creator));
				}
			}else{	//分总成报废
				//该分装线的每日排产计划
				Map<String, List<Fenzxpcjh>> map = fenzxpcjhMap.get(guodsbxx.getShengcx());
				if(map != null){
					//排产第一日的计划
					Zongccfjg zongccfjg = chanxljMap.get(guodsbxx.getUsercenter()+guodsbxx.getShengcx()+guodsbxx.getZongch());
					if(zongccfjg != null){
						zongccfjg.setZhankrq(zhankrq);
						List<Fenzxpcjh> list = map.get(paickssj);
						if(list == null){
							//下一个SPPV工作日
							String nextWorkingday = addDaysByZhuxjh(paickssj, 1);
							while(nextWorkingday != null && list == null){
								list = map.get(nextWorkingday);
								nextWorkingday = addDaysByZhuxjh(nextWorkingday, 1);
							}
						}
						addScrapFenzxpcjh(list, guodsbxx, zongccfjg, zhankrq);
					}else{
						//添加报警信息
						addWarnMessage(new WarnMessage(dax.getUsercenter(), dax.getDaxxh(), guodsbxx.getShengcx(), paickssj, 
								"用户中心："+dax.getUsercenter()+"，生产线编号："+guodsbxx.getShengcx()+
								"总成号："+guodsbxx.getZongch()+"，的报废总成无法找到对应的产线零件信息", creator));
					}
				}
			}
		}
		
		return scrapList;
	}
	
	/**
	 * 获取大线的产线零件信息
	 * @param dax
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Zongccfjg> getChanxlj(Dax dax){
		Map<String, Zongccfjg> chanxljMap = new HashMap<String, Zongccfjg>();
		List<Zongccfjg> chanxljList = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("fenzxpc.getChanxlj", dax);
		for (Zongccfjg zongccfjg : chanxljList){	//封装成map
			chanxljMap.put(zongccfjg.getUsercenter()+zongccfjg.getFenzxh()+zongccfjg.getLingj(), zongccfjg);
		}
		return chanxljMap;
	}
	
	/**
	 * 将报废总成添加到分装线排产计划中
	 * @param list
	 * @param guodsbxx
	 */
	private void addScrapFenzxpcjh(List<Fenzxpcjh> list, Guodsbxx guodsbxx, Zongccfjg zongccfjg, String zhankrq){
		if(list != null && list.size() != 0){
			Fenzxpcjh fenzxpcjh = new Fenzxpcjh();
			fenzxpcjh.setUsercenter(guodsbxx.getUsercenter());	//用户中心
			fenzxpcjh.setFenzxh(zongccfjg.getFenzxh());			//分装线号
			fenzxpcjh.setYujsxrq(list.get(0).getYujsxrq());		//预计上线日期
			fenzxpcjh.setFenzch(zongccfjg.getLingj());			//总成号
			fenzxpcjh.setLingjyt("A");							//零件用途=安全库存
			fenzxpcjh.setZhankrq(zhankrq);						//展开日期=排产开始时间
			fenzxpcjh.setLingjlx(zongccfjg.getLingjlx());		//零件类型
			fenzxpcjh.setXuqly(list.get(0).getXuqly());			//需求来源
			fenzxpcjh.setBeijwx(false);							//是否备件外销计划
			fenzxpcjh.setDingdh(guodsbxx.getDingdh());			//订单号
			list.add(0, fenzxpcjh);		//添加到开始处
			guodsbxx.setBaofzt("1");	//以处理
			//重新生成上线顺序
			rebuildShangxsx(list);
		}
	}
	
	/**
	 * 根据计划员输入的分装线排产数量调整每日的分装线排产计划
	 * @param fenzxpcjhMap
	 * @param paicslMap
	 */
	private void ensureFenzxpcsl(Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, Map<String, List<Paicsl>> paicslMap
			, Map<String, Fenzx> fenzxMap, Dax dax){
		log.info("根据计划员输入的分装线排产数量调整每日的分装线排产计划中...");
		for (String fenzxh : fenzxpcjhMap.keySet()) {
			Map<String, List<Fenzxpcjh>> map = fenzxpcjhMap.get(fenzxh);
			if(map != null){
				for (String riq : map.keySet()) {
					//当日的分装线排产数量
					Paicsl paicsl = findPaicsl(paicslMap.get(riq), fenzxh);
					List<Fenzxpcjh> list = map.get(riq);
					if(riq.compareTo(paickssj) >= 0 && riq.compareTo(paicjssj) <= 0 && paicsl != null && list != null){
						//计划上线量
						int jihsxl = paicsl.getJihsxl();
						//分装线的所有排产计划
						List<Fenzxpcjh> servalDaysFenzxpcjh = new ArrayList<Fenzxpcjh>();
						//分装线每日的排产计划数量
						Map<String, Integer> dayFenzxpcjhCount = new HashMap<String, Integer>();
						List<CalcDay> dayList = calcDays.get(dax.getDaxxh());
						//统计每日的分装线排产数量
						for (String r : map.keySet()) {	//循环日期
							for (CalcDay day : dayList) {
								if(r.equals(day.getRiq())){
									List<Fenzxpcjh> dayFenzxpcjh = map.get(r);
									if(dayFenzxpcjh != null && r.compareTo(riq) >= 0){
										dayFenzxpcjhCount.put(r, dayFenzxpcjh.size());
										servalDaysFenzxpcjh.addAll(dayFenzxpcjh);
									}
								}
							}
						}
						//按照数量差偏移分装线排产计划
						moveAheadFenzxpcjh(dayList, dayFenzxpcjhCount, servalDaysFenzxpcjh, map, list.size() - jihsxl,dax,fenzxMap.get(fenzxh));
					}
				}
			}
		}
	}
	
	/**
	 * 检查分装线排产计划是否超出产能
	 * @param fenzxpcjhMap
	 * @param fenzxMap
	 */
	private void checkFenzxpcjhCount(Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, Map<String, Fenzx> fenzxMap)  throws ParseException{
		log.info("检查分装线排产计划是否超出产能中...");
		for (String fenzxh : fenzxpcjhMap.keySet()) {
			Map<String, List<Fenzxpcjh>> fenzx_allDay_map= fenzxpcjhMap.get(fenzxh);
			if(fenzx_allDay_map != null && !fenzx_allDay_map.isEmpty()){
				for (String date : fenzx_allDay_map.keySet()) {
					if(fenzx_allDay_map.get(date) != null){
						//检查当日的分装线排产计划是否超出产能
						checkFenzxpcjh(date, fenzxpcjhMap.get(fenzxh), fenzxMap.get(fenzxh));
					}
				}
			}
		}
	}
	
	
	/**
	 * 检查分装线排产计划是否超出了产能
	 * @param riq
	 * @param fenzxpcjhMap
	 * @param fenzx
	 */
	private void checkFenzxpcjh(String riq, Map<String, List<Fenzxpcjh>> fenzxpcjhMap, Fenzx fenzx) throws ParseException{
		// 当日的分装线排产计划
		List<Fenzxpcjh> todayFenzxpcjh = fenzxpcjhMap.get(riq);
		// 获取分装线当前日期信息
		CalcDay fenzx_day = getFenzxCurrentCalcDay(calcDays.get(fenzx.getFenzxh()), riq);
		// 获取第一个超出分装线产能的计划的角标（没超出则为-1）
		int subIndex = getOutOfBoundsIndex(fenzx, fenzx_day, todayFenzxpcjh);
		if(subIndex > -1){
			// 获取下一个工作日
			String nextWorkingday = addDaysByZhuxjh(riq, 1);	
			if(nextWorkingday != null){
				// 超出的分装线排产计划
				List<Fenzxpcjh> extraFenzxpcjh = todayFenzxpcjh.subList(subIndex, todayFenzxpcjh.size());	
				// 第二个工作日的分装线排产计划
				List<Fenzxpcjh> nextDayFenzxpcjh = fenzxpcjhMap.get(nextWorkingday);	
				if(nextDayFenzxpcjh == null){
					do{
						nextWorkingday = addDaysByZhuxjh(nextWorkingday, 1);
						nextDayFenzxpcjh = fenzxpcjhMap.get(nextWorkingday);
					}while(nextDayFenzxpcjh == null && nextWorkingday != null);
				}
				if(nextDayFenzxpcjh != null){
					// 第二个工作日加上当日超出的分装线计划
					nextDayFenzxpcjh.addAll(0, extraFenzxpcjh);
					// 重新生成第二个工作日的上线顺序
					rebuildShangxsx(nextDayFenzxpcjh, nextWorkingday);
					// 当日移除多余的分装线计划
					removeList(todayFenzxpcjh, subIndex, todayFenzxpcjh.size());
					// 依次检查下一天的分装线排产计划
					checkFenzxpcjh(nextWorkingday, fenzxpcjhMap, fenzx);
				}
			}
		}
	}
	
	/**
	 * 获取第一个超出分装线产能的计划的角标（没超出则为-1）
	 * @param fenzx
	 * @param fenzx_day
	 * @param todayFenzxpcjh
	 * @return
	 */
	private int getOutOfBoundsIndex(Fenzx fenzx, CalcDay fenzx_day, List<Fenzxpcjh> todayFenzxpcjh){
		// 截取角标
		int subIndex = -1;
		if(fenzx.getFenzxxs() == 1){
			int maxLingjCount = (int) (fenzx.getShengcjp() * fenzx_day.getGongs());
			for (int i = 0; i < todayFenzxpcjh.size(); i++) {
				if(isDefaultOrIndivisibleZongc(todayFenzxpcjh.get(i).getFenzch())){
					continue;
				}
				maxLingjCount--;
				if(maxLingjCount < 0){
					subIndex = i;
					break;
				}
			}
		}else if(fenzx.getFenzxxs() ==2){
			int maxLingjCount_L = (int)(fenzx.getShengcjp() * fenzx_day.getGongs());
			int maxLingjCount_R = (int)(fenzx.getShengcjp() * fenzx_day.getGongs());
			for (int i = 0; i < todayFenzxpcjh.size(); i++) {
				if(isDefaultOrIndivisibleZongc(todayFenzxpcjh.get(i).getFenzch())){
					continue;
				}
				if("L".equals(todayFenzxpcjh.get(i).getLingjlx())){
					maxLingjCount_L--;
				}else if("R".equals(todayFenzxpcjh.get(i).getLingjlx())){
					maxLingjCount_R--;
				}
				if(maxLingjCount_L < 0 || maxLingjCount_R < 0){
					subIndex = i;
					break;
				}
			}
		}
		return subIndex;
	}
	
	/**
	 * 保存数据
	 * @param fenzxpcjhMap
	 * @param lixdMap
	 * @param specialResult
	 * @param scrapList
	 * @param paiclsMap
	 * @param fenzxMap
	 * @param dax
	 */
	@Transactional
	private void saveData(Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, Map<String, Lixd> lixdMap ,
			Map<String, Map<String, List<Fenzxpcjh>>> specialResult, List<Guodsbxx> scrapList, Map<String, Paicls> paiclsMap,
			Map<String, Fenzx> fenzxMap, Dax dax, List<Guodsbxx> guodsbxxList, List<Zhuxpcjh> saveZhuxpcjhList){
		log.info("保存数据中...");
		//保存需要特殊处理的分装线排产计划
		saveSpecialResult(specialResult, paiclsMap);
		//保存分装线排产计划和排产流水，并统计完成的备件外销计划
		Map<String,Beijwxjh> beijwxjhMap = saveFenzxpcjhList(fenzxpcjhMap, paiclsMap, lixdMap, fenzxMap, dax);
		//保存主线排产计划
		saveZhuxpcjh(saveZhuxpcjhList);
		//更新备件外销计划的完成数量
		updateBeijwxjh(beijwxjhMap, dax);
		//更新已处理报废的过点申报信息
		updateGuodsbxx(guodsbxxList);
	}
	
	/**
	 * 保存主线排产计划
	 * @param saveZhuxpcjhList
	 */
	public void saveZhuxpcjh(List<Zhuxpcjh> saveZhuxpcjhList){
		for (Zhuxpcjh zhuxpcjh : saveZhuxpcjhList) {
			if(zhuxpcjh.getDingdh() != null && zhuxpcjh.getDingdh().contains("temp_")){
				zhuxpcjh.setDingdh(null);
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch(
				"fenzxpc.insertZhuxpcjh", saveZhuxpcjhList);
	}
	
	/**
	 * 更新已报废的过点申报信息
	 * @param guodsbxxList
	 */
	private void updateGuodsbxx(List<Guodsbxx> guodsbxxList){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch(
				"fenzxpc.updateGuodsbxx", guodsbxxList);
	}
	
	/**
	 * 保存需要特殊处理的分装线排产计划
	 * @param specialResult
	 * @param paiclsMap
	 * @return
	 */
	private void saveSpecialResult(Map<String, Map<String, List<Fenzxpcjh>>> specialResult, Map<String, Paicls> paiclsMap){
		for (String fenzxh : specialResult.keySet()) {	//循环分装线
			//某分装线几日的排产计划
			Map<String, List<Fenzxpcjh>> servalDaysFenzxpcjh = specialResult.get(fenzxh);
			//排产流水
			Paicls paicls = paiclsMap.get(fenzxh);	
			//分装线总顺序号
			BigInteger startZongsxh = new BigInteger(paicls == null ? "0": paicls.getFenzxzsxh());
			if(servalDaysFenzxpcjh != null && servalDaysFenzxpcjh.size() != 0){
				List<String> keyList = new ArrayList<String>(servalDaysFenzxpcjh.keySet());
				Collections.sort(keyList);	//按照日期顺序排序
				for (int i = 0; i < keyList.size(); i++) {
					String day = keyList.get(i);
					//某日的分装线排产计划
					List<Fenzxpcjh> dayFenzxpcjh = servalDaysFenzxpcjh.get(day);
					if(dayFenzxpcjh != null && dayFenzxpcjh.size() != 0){
						for (Fenzxpcjh fenzxpcjh : dayFenzxpcjh) {
							startZongsxh = startZongsxh.add(BigInteger.ONE);
							fenzxpcjh.setFenzxzsx_lixh(String.format("%010d", startZongsxh));	//分装线总顺序号_离线后
							fenzxpcjh.setFenzxzsx_lixq(fenzxpcjh.getFenzxzsx_lixh());		//分装线总顺序号_离线前
							fenzxpcjh.setFenzsx(String.valueOf(fenzxpcjh.getShangxsx()));	//当日分装线顺序
							fenzxpcjh.setCreate_time(CommonFun.getJavaTime());	//创建时间
							fenzxpcjh.setCreator(creator);						//创建人
							if(fenzxpcjh.getDingdh() != null && fenzxpcjh.getDingdh().contains("temp_")){
								fenzxpcjh.setDingdh(null);
							}
						}
						//保存一日的分装线排产计划
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch(
								"fenzxpc.insertFenzxpcjh", dayFenzxpcjh);
					}
				}
			}
			if(paicls == null){
				paicls = new Paicls();
				paicls.setFenzxh(fenzxh);
			}
			paicls.setFenzxzsxh(String.valueOf(startZongsxh));
			paiclsMap.put(fenzxh, paicls);
		}
	}
	
	/**
	 * 保存分装线排产计划和排产流水，并统计完成的备件外销计划
	 * @param fenzxpcjhMap
	 * @param paiclsMap
	 * @param lixdMap
	 * @param fenzxMap
	 * @return
	 */
	private Map<String,Beijwxjh> saveFenzxpcjhList(Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, 
			Map<String, Paicls> paiclsMap, Map<String, Lixd> lixdMap, Map<String, Fenzx> fenzxMap, Dax dax){
		//已完成排产的备件外销计划
		Map<String, Beijwxjh> beijwxjhMap = new HashMap<String, Beijwxjh>();
		for (String fenzxh : fenzxpcjhMap.keySet()) {	//循环分装线
			//某分装线几日的排产计划
			Map<String, List<Fenzxpcjh>> servalDaysFenzxpcjhMap = fenzxpcjhMap.get(fenzxh);
			//某分装线几日的排产计划 (由上面的map转换形式)
			List<Fenzxpcjh> servalDaysFenzxpcjhList = new ArrayList<Fenzxpcjh>();
			//排产流水
			Paicls paicls = paiclsMap.get(fenzxh);	
			//分装线总顺序号
			BigInteger startZongsxh = new BigInteger(paicls == null ? "0": paicls.getFenzxzsxh());
			//TODO 验证有分装线离线点的计划，是否已经生成过上线计划，避免前一日最后一个计划有离线点的时候，造成排产重复的情况
			if(servalDaysFenzxpcjhMap != null && servalDaysFenzxpcjhMap.size() != 0){
				for (String day : servalDaysFenzxpcjhMap.keySet()) {	//循环日期
					//某日的分装线排产计划
					List<Fenzxpcjh> dayFenzxpcjh = servalDaysFenzxpcjhMap.get(day);
					if(dayFenzxpcjh != null && dayFenzxpcjh.size() != 0 
							&& day.compareTo(paickssj) >= 0 && day.compareTo(paicjssj) <= 0){	//日期不是计算开始时间或计算结束时间
						for (Fenzxpcjh fenzxpcjh : dayFenzxpcjh) {
							startZongsxh = startZongsxh.add(BigInteger.ONE);
							fenzxpcjh.setFenzxzsx_lixh(String.format("%010d", startZongsxh));	//分装线总顺序号_离线后
							fenzxpcjh.setFenzxzsx_lixq(fenzxpcjh.getFenzxzsx_lixh());		//分装线总顺序号_离线前
							fenzxpcjh.setFenzsx(String.valueOf(fenzxpcjh.getShangxsx()));	//分装顺序
							fenzxpcjh.setCreate_time(CommonFun.getJavaTime());	//创建时间
						//	fenzxpcjh.setCreator(creator);						//创建人
							//统计完成的备件外销计划
							sumBeijwxjh(fenzxpcjh, beijwxjhMap);
							if(fenzxpcjh.getDingdh() != null && fenzxpcjh.getDingdh().contains("temp_")){
								fenzxpcjh.setDingdh(null);
							}
						}
						servalDaysFenzxpcjhList.addAll(dayFenzxpcjh);
					}
				}
				if(servalDaysFenzxpcjhList.size() != 0){
					Fenzx fenzx = fenzxMap.get(fenzxh);
					//计算分装线总顺序_离线前
					calcFenzxzsx_liq(fenzx, servalDaysFenzxpcjhList, lixdMap, startZongsxh);
					//设置分装顺序
					//setFenzxsx(servalDaysFenzxpcjhList);
					//printFenzxpcjhLog(servalDaysFenzxpcjhList);
					//保存几日的分装线排产计划
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch(
							"fenzxpc.insertFenzxpcjh", servalDaysFenzxpcjhList);
					//保存排产流水
					insertPaicls(fenzx, servalDaysFenzxpcjhList, startZongsxh, dax);
				}
			}
		}
		return beijwxjhMap;
	}
	
	
	/**
	 * 保存排产流水
	 * @param fenzx
	 * @param servalDaysFenzxpcjhList
	 * @param startZongsxh
	 */
	private void insertPaicls(Fenzx fenzx, List<Fenzxpcjh> servalDaysFenzxpcjhList, BigInteger startZongsxh, Dax dax){
		Paicls new_paicls = new Paicls(); 
		new_paicls.setUsercenter(fenzx.getUsercenter());	//用户中心
		new_paicls.setDaxxh(fenzx.getDaxxh());				//大线线号
		new_paicls.setFenzxh(fenzx.getFenzxh());			//分装线号
		new_paicls.setZuihjscl(getLastDingdh(servalDaysFenzxpcjhList, fenzx));	//最后计算车辆
		new_paicls.setFenzxzsxh(String.valueOf(startZongsxh));				//分装线总顺序号
		new_paicls.setPaickssj(paickssj);					//排产开始时间
		new_paicls.setPaicjssj(paicjssj);					//排产结束时间
		new_paicls.setCreate_time(CommonFun.getJavaTime());	//创建时间
		new_paicls.setCreator(creator);						//创建人
			
		new_paicls.setChakpyl(getChakpyl(servalDaysFenzxpcjhList, fenzx));

		//保存分装线排产流水
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute(
				"fenzxpc.insertPaicls", new_paicls);
	}
	
	/**
	 * 从集合中移除指定角标范围的集合
	 * @param list
	 * @param startIndex
	 * @param endIndex
	 */
	private void removeList(List list, int startIndex, int endIndex){
		startIndex = startIndex < 0 ? 0 : startIndex;
		endIndex = endIndex > list.size() ? list.size() : endIndex;
		for (int i = startIndex; i < endIndex; i++) {
			list.remove(i);
			i--;
			endIndex--;
		}
	}
	
	/**
	 * 统计完成的备件外销计划
	 * @param fenzxpcjh
	 * @param beijwxjhMap
	 */
	private void sumBeijwxjh(Fenzxpcjh fenzxpcjh, Map<String, Beijwxjh> beijwxjhMap){
		if(fenzxpcjh.isBeijwx()){	//是备件外销计划生成的分装线排产
			String key = fenzxpcjh.getUsercenter() + fenzxpcjh.getFenzxh() + fenzxpcjh.getFenzch() + 
			fenzxpcjh.getZhankrq() + fenzxpcjh.getLingjyt() + fenzxpcjh.getXuqly();
			if(beijwxjhMap.containsKey(key)){
				Beijwxjh jh = beijwxjhMap.get(key);
				jh.setWancsl(jh.getWancsl()+1);
			}else{
				Beijwxjh jh = new Beijwxjh();
				jh.setBeijwx(fenzxpcjh.getLingjyt());
				jh.setUsercenter(fenzxpcjh.getUsercenter());
				jh.setFenzxh(fenzxpcjh.getFenzxh());
				jh.setXuqly(fenzxpcjh.getXuqly());
				jh.setZhankrq(fenzxpcjh.getZhankrq());
				jh.setZongch(fenzxpcjh.getFenzch());
				jh.setWancsl(1);
				beijwxjhMap.put(key, jh);
			}
		}
	}
	
	/**
	 * 获取分装线排产计划的最后一个订单号
	 * @param list
	 * @return
	 */
	private String getLastDingdh(List<Fenzxpcjh> list, Fenzx fenzx){
		for (int i = list.size() - 1; i >= 0; i--) {
			if(!Strings.isNullOrEmpty(list.get(i).getDingdh()) && list.get(i).getLixd() == null){	//订单不为空且未离线提前
				return list.get(i).getDingdh();
			}
		}
		//上次的排产流水
		Paicls lastPaicls = (Paicls) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
				.selectObject("fenzxpc.getLastPcls", fenzx);
		if(lastPaicls != null){	//上次排产流水的最后计算车辆
			return lastPaicls.getZuihjscl();
		}
		return "NULL";
	}
	/**
	 * 获取大线排空偏移量
	 * @param list
	 * @return
	 */
	private String getChakpyl(List<Fenzxpcjh> list, Fenzx fenzx){
		int j=0;//分装线排空数量
		for (int i = list.size() - 1; i >= 0; i--) {
			if(!Strings.isNullOrEmpty(list.get(i).getFenzch()) && !list.get(i).getFenzch().equalsIgnoreCase(DEFAULT_ZONGCH) &&   list.get(i).getLixd() == null){	//订单不为空且未离线提前			
				return Integer.toString(list.size()-1-i-j);//大线排空量等于总排空量-分装线排空量
			}else{
				if(list.get(i).getShiffzxpk()!=null && list.get(i).getShiffzxpk().equals("1")){
					j++;
				}
			}
		}
		return "0";
	}
	
	/**
	 * 计算分装线总顺序_离线前
	 * @param fenzx
	 * @param servalDaysFenzxpcjhList
	 * @param lixdMap
	 */
	private void calcFenzxzsx_liq(Fenzx fenzx, List<Fenzxpcjh> servalDaysFenzxpcjhList, 
			Map<String, Lixd> lixdMap, BigInteger startZongsxh){
		if(servalDaysFenzxpcjhList.size() > 0){
			// 取出大线离线点，并按照对应消耗点的车身数量降序排序
			List<Lixd> lixdList = sortLixd(fenzx.getFenzxh(), lixdMap);
			for (int i = lixdList.size()-1 ; i >= 0; i--) {	// 按照车身数量升序循环
				Lixd lixd = lixdList.get(i);
				for (int j = servalDaysFenzxpcjhList.size()-1; j >= 0; j--) {
					Fenzxpcjh jh = servalDaysFenzxpcjhList.get(j);
					if(lixd.getLixd().equals(jh.getLixd())){
						// 实际提前车位数（越过空订单和未拆分订单）
						int tiqcw = findPracticalTiqcwByFenzxjhAnotherSide(servalDaysFenzxpcjhList, j, jh.getTiqcw(), fenzx.getFenzxxs());
						BigInteger lixq = new BigInteger(jh.getFenzxzsx_lixq()).add(new BigInteger(String.valueOf(tiqcw)));
						jh.setFenzxzsx_lixq(String.format("%010d", lixq.min(startZongsxh)));
						// 受影响的排产计划：离线前顺序=离线前顺序-分装线系数
						for (int k = j + 1 ; k <= j + tiqcw && k < servalDaysFenzxpcjhList.size(); k++) {
							Fenzxpcjh temp= servalDaysFenzxpcjhList.get(k);
							temp.setFenzxzsx_lixq(String.format("%010d", new BigInteger(temp.getFenzxzsx_lixq()).subtract(BigInteger.ONE)));
						}
						Collections.sort(servalDaysFenzxpcjhList);
					}
				}
			}
		}
	}
	
	/**
	 * 保存备件外销计划
	 * @param saveMap
	 * @param dax
	 */
	private void updateBeijwxjh(Map<String,Beijwxjh> saveMap, Dax dax){
		//重新获取未完成备件外销计划
		Map<String, List<Beijwxjh>> map = getBeijwxjh(dax);
		//待保存的备件外销计划
		List<Beijwxjh> saveList = new ArrayList<Beijwxjh>();
		for (String fenzxh : map.keySet()) {	//循环分装线
			List<Beijwxjh> beijwxjhList = map.get(fenzxh);
			for (Beijwxjh beijwxjh : beijwxjhList) {	//循环未完成的备件外销计划
				String key = beijwxjh.getUsercenter() + beijwxjh.getFenzxh() + beijwxjh.getZongch() + 
				beijwxjh.getZhankrq() + beijwxjh.getBeijwx() + beijwxjh.getXuqly();
				if(saveMap.containsKey(key)){	//此次排产的备件外销计划包含未完成的备件外销计划
					Beijwxjh saveBeijwxjh = saveMap.get(key);
					int thisCount = beijwxjh.getShul() - beijwxjh.getWancsl();	//未完成的备件外销计划数量
					int saveCount = saveBeijwxjh.getWancsl();	//要保存的备件外销数量
					if(saveCount <= thisCount){	//要保存的备件外销数量<未完成的备件外销计划数量
						beijwxjh.setWancsl(beijwxjh.getWancsl()+saveCount);
						saveBeijwxjh.setWancsl(0);
						saveMap.remove(key);
					}else {
						beijwxjh.setWancsl(beijwxjh.getShul());
						saveBeijwxjh.setWancsl(saveBeijwxjh.getWancsl()-thisCount);
					}
					saveList.add(beijwxjh);
				}
			}
		}
		//保存已完成的备件外销数量
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch(
				"fenzxpc.updateBeijwxjh", saveList);
	}
	
	/**
	 * 打印主线计划
	 * @param list
	 */
	private void printZhuxpcjhLog(Dax dax, Map<String, List<Zhuxpcjh>> map){
		List<Zhuxpcjh> list;
		StringBuilder sb = new StringBuilder();
		sb.append("\n-------------------------------------------------\n");
		sb.append("|\t\t\t"+dax.getDaxxh()+"\t\t\t|\n");
		int count = 0;
		for (String day : map.keySet()) {
			count++;
			list = map.get(day);
			if(list != null && !list.isEmpty()){
				sb.append("|-----------------------------------------------|\n");
				sb.append("|\t\t\t"+day+"\t\t|\n");
				sb.append("|-----------------------------------------------|\n");
				sb.append("|大线当日顺序\t|\t大线总顺序（离线前）\t|\n");
				for (int i = 0; i < list.size(); i++) {
					Zhuxpcjh jh = list.get(i);
					sb.append("|-----------------------------------------------|\n");
					sb.append("|\t"+jh.getDaxsx()+"\t|\t"+jh.getDaxzsx_lixq()+"\t\t|\n");
				}
				if(count != map.keySet().size()){
					sb.append("|-----------------------------------------------|\n");
				}else{
					sb.append("-------------------------------------------------\n");
				}
			}
		}
		sb.append("\n\n");
		log.info(sb.toString());
	}
	
	/**
	 * 打印分装线计划
	 * @param list
	 */
	private void printFenzxpcjhLog(List<Fenzxpcjh> list){
		if(list != null && list.size() != 0){
			StringBuilder sb = new StringBuilder();
			if(list.get(0).getFenzxzsx_lixq() != null){
				sb.append("\n----------------------------------------------------------\n");
				sb.append("|离线前顺序\t|\t离线后顺序\t|\t订单号\t\t|\t分总成号\t|\n");
				for (int i = 0; i < list.size(); i++) {
					Fenzxpcjh jh = list.get(i);
					sb.append("----------------------------------------------------------\n");
					sb.append("|\t"+jh.getFenzxzsx_lixq()+"\t|\t"+jh.getFenzxzsx_lixh()+"\t|\t"+jh.getDingdh()+"\t|\t"+jh.getFenzch()+"\t|\n");
				}
			}else if(list.get(0).getShangxsx() != null){
				sb.append("\n----------------------------------------------------------\n");
				sb.append("|上线顺序\t|\t订单号\t\t|\t分总成号\t|\n");
				for (int i = 0; i < list.size(); i++) {
					Fenzxpcjh jh = list.get(i);
					sb.append("----------------------------------------------------------\n");
					sb.append("|\t"+jh.getShangxsx()+"\t|\t"+jh.getDingdh()+"\t|\t"+jh.getFenzch()+"\t|\n");
				}
			}else if(list.get(0).getXiaxsx() != null){
				sb.append("\n----------------------------------------------------------\n");
				sb.append("|下线顺序\t|\t订单号\t\t|\t分总成号\t|\n");
				for (int i = 0; i < list.size(); i++) {
					Fenzxpcjh jh = list.get(i);
					sb.append("----------------------------------------------------------\n");
					sb.append("|\t"+jh.getXiaxsx()+"\t|\t"+jh.getDingdh()+"\t|\t"+jh.getFenzch()+"\t|\n");
				}
			}else{
				sb.append("|对应大线顺序\t|\t订单号\t\t|\t分总成号\t|\n");
				for (int i = 0; i < list.size(); i++) {
					Fenzxpcjh jh = list.get(i);
					sb.append("----------------------------------------------------------\n");
					sb.append("|\t"+jh.getDuiydxsx()+"\t|\t"+jh.getDingdh()+"\t|\t"+jh.getFenzch()+"\t|\n");
				}
			}
			sb.append("----------------------------------------------------------\n");
			log.info(sb.toString());
		}
	}
	
	/**
	 * 打印所有分装线排产计划
	 * @param fenzxpcjhMap
	 */
	private void printResultLog(Map<String, Map<String, List<Fenzxpcjh>>> fenzxpcjhMap, String shunx){
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String fenzxh : fenzxpcjhMap.keySet()) {
			count++;
			Map<String, List<Fenzxpcjh>> map = fenzxpcjhMap.get(fenzxh);
			if(map != null && map.size() != 0){
				sb.append("\n-------------------------------------------------------------------------\n");
				sb.append("|\t\t\t\t"+fenzxh+"\t\t\t\t\t|\n");
				for (String day : map.keySet()) {
					List<Fenzxpcjh> list = map.get(day);
					if(list != null && list.size() != 0 
							&& day.compareTo(paickssj) >= 0 && day.compareTo(paicjssj) <= 0){
						sb.append("|-----------------------------------------------------------------------|\n");
						sb.append("|\t\t\t\t"+day+"\t\t\t\t|\n");
						sb.append("|-----------------------------------------------------------------------|\n");
						if(shunx.equals(Const.FENZXPC_ONLINE)){
							sb.append("|\t上线顺序|\t订单号|\t分总成号\t|\t零件用途\t|\n");
							for (Fenzxpcjh fenzxpcjh : list) {
								sb.append("|-----------------------------------------------------------------------|\n");
								sb.append("|\t"+fenzxpcjh.getShangxsx()+"\t|\t"+fenzxpcjh.getDingdh()+"|\t"+fenzxpcjh.getFenzch()+"\t|\t"+fenzxpcjh.getLingjyt()+"\t|\n");
							}
						}else if(shunx.equals(Const.FENZXPC_OFFLINE)){
							sb.append("|\t下线顺序|\t订单号|\t分总成号\t|\t零件用途\t|\n");
							for (Fenzxpcjh fenzxpcjh : list) {
								sb.append("|-----------------------------------------------------------------------|\n");
								sb.append("|\t"+fenzxpcjh.getXiaxsx()+"\t|\t"+fenzxpcjh.getDingdh()+"|\t"+fenzxpcjh.getFenzch()+"\t|\t"+fenzxpcjh.getLingjyt()+"\t|\n");
							}
						}
					}
				}
				if(count != fenzxpcjhMap.keySet().size()){
					sb.append("|-----------------------------------------------------------------------|\n");
				}else{
					sb.append("-------------------------------------------------------------------------\n");
				}
				sb.append("\n\n");
			}
		}
		log.info(sb.toString());
	}
}
