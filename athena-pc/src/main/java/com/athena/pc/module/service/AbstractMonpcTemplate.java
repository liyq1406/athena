package com.athena.pc.module.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.BackgroundRunLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.utils.UUIDHexGenerator;

/**
 * <p>
 * Title:月模拟计划
 * </p>
 * <p>
 * Description:按照选择模拟的时间进行月模拟排产
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 王国首
 * @version v1.0
 * @date 2011-12-06
 **/
@Component
public abstract class AbstractMonpcTemplate extends BaseService { 

	@Inject
	private BackgroundRunLog BackgroundRunLog;
	
	protected final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/*零件产线对应关系*/
	protected Map<String, List<Map<String,String>>> lingjOfLine; 
	protected String shengxIds;
	/*产线组工作日历*/
	protected Map<String, List<String>> workCalendar;
	/*没工作日历，没有拆分成功的需求*/
	protected List<Map<String,String>> noWorkCalReqs;
	/*产线组月模拟，日滚动计划号*/
	protected Map<String, String> yueMnjhh;
	/*排产报警消息列表*/
	protected List<Map<String, String>> messagePc;
	
	/*产线组计算日期内每个工作日对应的提前期日期*/
	private Map<String, String> aheadPeriod;
	/*滚动月模拟产线封闭期*/
	private Map<String, Map<String,String>> blockWorkCalendar;
	/*包装容量集合key=用户中心+生产线号+零件编号*/
	private Map<String, String> usPack;
	protected Map<String,List<Map<String,String>>> aheadJXQ;
	/*是否计算一周净需求*/
	private boolean isWeekCal = true;
	
	/*产线组零件品种*/
	protected Map<String, Map<String,String>> riLingjpz;

	/*生产线零件品种*/
	protected Map<String, Map<String,Map<String,String>>> riScxLingjpz;

	public Map<String, Map<String, String>> getRiLingjpz() {
		return riLingjpz;
	}
	
	public void setRiLingjpz(Map<String, Map<String, String>> riLingjpz) {
		this.riLingjpz = riLingjpz;
	}
	
	public Map<String, Map<String, Map<String, String>>> getRiScxLingjpz() {
		return riScxLingjpz;
	}

	public void setRiScxLingjpz(
			Map<String, Map<String, Map<String, String>>> riScxLingjpz) {
		this.riScxLingjpz = riScxLingjpz;
	}
	
	static Logger logger = Logger.getLogger(AbstractMonpcTemplate.class.getName());
	public Map<String, List<Map<String, String>>> getLingjOfLine() {
		return lingjOfLine;
	}

	public void setLingjOfLine(Map<String, List<Map<String, String>>> lingjOfLine) {
		this.lingjOfLine = lingjOfLine;
	}	
	public List<Map<String, String>> getNoWorkCalReqs() {
		return noWorkCalReqs;
	}

	public void setNoWorkCalReqs(final List<Map<String, String>> noWorkCalReqs) {
		this.noWorkCalReqs = noWorkCalReqs;
	}

	public Map<String, List<String>> getWorkCalendar() {
		return workCalendar;
	}

	public void setWorkCalendar(final Map<String, List<String>> workCalendar) {
		this.workCalendar = workCalendar;
	}

//	public String getShengxIds(){
//		return shengxIds;
//	}
	
	public void setShengxIds(final String shengxIds){
		this.shengxIds = shengxIds;
	}
	
	public Map<String, String> getYueMnjhh() {
		return yueMnjhh;
	}

	public void setYueMnjhh(Map<String, String> yueMnjhh) {
		this.yueMnjhh = yueMnjhh;
	}
	
	public List<Map<String, String>> getMessagePc() {
		return messagePc;
	}

	public void setMessagePc(List<Map<String, String>> messagePc) {
		this.messagePc = messagePc;
	}
	
	public Map<String, String> getAheadPeriod() {
		return aheadPeriod;
	}

	public void setAheadPeriod(Map<String, String> aheadPeriod) {
		this.aheadPeriod = aheadPeriod;
	}
	
	public Map<String, Map<String,String>> getBlockWorkCalendar() {
		return blockWorkCalendar;
	}

	public void setBlockWorkCalendar(Map<String, Map<String,String>> blockWorkCalendar) {
		this.blockWorkCalendar = blockWorkCalendar;
	}
	
	public Map<String, String> getUsPack() {
		return usPack;
	}

	public void setUsPack(Map<String, String> usPack) {
		this.usPack = usPack;
	}

	public Map<String, List<Map<String, String>>> getAheadJXQ() {
		return aheadJXQ;
	}

	public void setAheadJXQ(Map<String, List<Map<String, String>>> aheadJXQ) {
		this.aheadJXQ = aheadJXQ;
	}
	
	public boolean isWeekCal() {
		return isWeekCal;
	}

	public void setWeekCal(boolean isWeekCal) {
		this.isWeekCal = isWeekCal;
	}
	/**
	 * @description  获取订单提前期
	 * @author 余飞
	 * @date 2012-1-9
	 * @param keihParam 订单客户的提前期列表
	 * @return Map<String, String>
	 */ 
	public Map<String, String> getOrderAheadOfTime(final List<Map<String, String>> keihParam) {  
		final Map<String,String> dingdParam = new HashMap<String,String>();
		for (Map<String, String> keih : keihParam) {
		      if(keih.get("DINGDTQQ") != null && String.valueOf(keih.get("DINGDTQQ")).length()>0){
		          dingdParam.put(keih.get("LINGJBH")+keih.get("CANGKDM"), String.valueOf(keih.get("DINGDTQQ")));
		       }
		} 
		return dingdParam;
	}
	
	/**
	 * @description	模拟排产
	 * @author 余飞
	 * @date 2012-1-9
	 * @param params			排产初始参数
	 * @param beginStocks       期初库存
	 */ 
	public void calPC(final Map<String,String> params,final List<Map<String, String>> beginStocks) {
		final List<Map<String, String>> shengxList = getChangxList(params);
		logger.info("模拟排产开始");
		String logString = this.getListMapKeyValue(beginStocks);
		logger.info("模拟排产开始，期初库存：" + logString);
		if (shengxList.size() <= 0) {
			throw new PCRunTimeException(new Message(
					"pc.ymn.nullChangx.error", "i18n.pc.pc").getMessage());
		}
		/*初始化参数*/
		initParam(shengxList, params);
		updatePcShij(shengxList, params);
		/*模拟排产前，按照月模拟，滚动月模拟，日滚动排产的业务逻辑，删除掉需要删除的数据*/
		cleanInitData(params);
		/*PP订单拆分*/
		logger.info("模拟排产PP订单拆分");
		parseMaoxq(shengxList, params);
		/*外部订单预告接口表拆分*/
		logger.info("模拟排产外部订单预告拆分");
		parseMaoxqWbddyg(shengxList, params);
		/*PJ订单拆分*/
		if ("G".equals(params.get("biaos")) || "R".equals(params.get("biaos"))) {
			parseNoWorkCalReq(params, getNoWorkCalReqs());
			setNoWorkCalReqs(new ArrayList<Map<String, String>>());
			logger.info("模拟排产PJ订单拆分");
			parseRiMaoxq(shengxList, params);
			parseRiMargin(shengxList, params);
		}
		/*备储计划拆分*/
		logger.info("模拟排产备储拆分");
		parseBeic(shengxList, params);
		logger.info("模拟排产,将产线下没有工作日历的需求拆分到其他产线");
		/*将产线下没有工作日历的需求拆分到其他产线*/
		parseNoWorkCalReq(params, getNoWorkCalReqs());
		logger.info("模拟排产,将零件日需求表中数据汇总到零件日需求汇总表中");
		/*将零件日需求表中数据汇总到零件日需求汇总表中*/
		statDailyLingj(shengxList, params);
		logger.info("模拟排产,检查模拟排产时间范围内，是否有需求");
		/*检查模拟排产时间范围内，是否有需求*/
		checkRequirement(shengxList, params);
		/*模拟排产*/
		logger.info("模拟排产每日需求排产");
		calYueMoN(shengxList, params, beginStocks);
		/*排产完将警报提示信息插入警报消息表中*/
		insertMessage();
		logger.info("模拟排产结束");
	}
	
	/**
	 * @description	分配没有工作日的产线零件订单到其他产线 
	 * @author 余飞
	 * @date 2012-1-10
	 * @param params			排产初始参数
	 * @param noWorkReqs     没有工作日的产线零件订单
	 */
	public void parseNoWorkCalReq(final Map<String, String> params, List<Map<String, String>> noWorkReqsA) {
		/*将没有工作日的产线零件订单拷贝到一个新的List中*/
		List<Map<String, String>> noWorkReq = this.listMapCopy(noWorkReqsA);
		/*将全局的有工作日的产线零件清空*/
		setNoWorkCalReqs(new ArrayList<Map<String, String>>());
		for(Map<String,String> noWorkCalReq : noWorkReq) {
			List<Map<String, String>> line = new ArrayList<Map<String,String>>();
			line.addAll(getLingjOfLine().get(noWorkCalReq.get("LINGJBH")));
			/*移除掉当前产线*/
//			line.remove(noWorkCalReq.get("SHENGCXBH"));
			Map<String, String> otherline = getWorkedOfLine(line,noWorkCalReq);
			if (otherline.size() <= 0) {
				/*当没有其他产线时，将分配没有工作日的产线零件订单再次写入到全局的有工作日的产线零件List*/
				noWorkCalReqs.add(noWorkCalReq);
			}
			/*将没有拆分的订单，备储记录拆分到其他产线*/
			parseReqOfLine(params, otherline,noWorkCalReq);
		}
	}

	/**
	 * @description	拆分需求到其他产线
	 * @author 余飞
	 * @date 2012-1-10
	 * @param params         排产参数
	 * @param line			  其他有工作日历的产线
	 * @param noWorkCalReq   没有工作日历的零件需求
	 */
	public void parseReqOfLine(final Map<String, String> params, Map<String, String> line,
			Map<String, String> noWorkCalReq) {
//		double sumLingjPercent = getLingJPercent(line);
		final Map<String,String> dateRange = new HashMap<String, String>();
		dateRange.put("MINSJ", noWorkCalReq.get("KAISSJ"));
		dateRange.put("MAXSJ", noWorkCalReq.get("JIESSJ")); 
//		Iterator<Entry<String, String>> iterator = line.entrySet().iterator(); 
//		while(iterator.hasNext()) {
//			Entry<String, String> entry = (Entry<String, String>)iterator.next();
			Map<String, String> req = new HashMap<String,String>();
			req.putAll(noWorkCalReq); 
//			double percent = Integer.parseInt(entry.getValue())/sumLingjPercent;
			req.put("LINGJSLCX", noWorkCalReq.get("LINGJSLCX"));
			req.put("SHENGCXBH", line.get("SHENGCXBH"));
			parseRequirement(params, req, getWorkCalendar(params, dateRange));
//		} 
	}

	/**
	 * @description	获取零件在产线上的生产比例之和
	 * @author 余飞
	 * @date 2012-1-11
	 * @param line 产线比例的集合
	 * @return 生产比例之和
	 */
//	private double getLingJPercent(Map<String, String> line) {
//		double result = 0;
//		Iterator<Entry<String, String>> iterator = line.entrySet().iterator();
//		while(iterator.hasNext()) {
//			Entry<String, String> entry = (Entry<String, String>)iterator.next();
//			result += Integer.parseInt(entry.getValue());
//		}
//		return result;
//	}

	/**
	 * @description	获取对应订单需求的产线
	 * @author 余飞
	 * @date 2012-1-12
	 * @param line	产线集合
	 * @param req	订单需求
	 * @return		有订单工作日的产线集合
	 */
	private Map<String, String> getWorkedOfLine(List<Map<String, String>> line, Map<String, String> req) {
		Map<String, String> result = new HashMap<String,String>();
		boolean flag = false;
		for( Map<String, String> lineMap: line) {
			if(lineMap.get(req.get("LINGJSLCX")) != null){
				flag = true;
				continue;
			}
			if(flag){
				List<String> lineCal = workCalendar.get(lineMap.get("SHENGCXBH"));
				try {
					findSubCal(lineCal, req);
					result.putAll(lineMap); 
					break;
				} catch(NoWorkDayException e) {
					logger.error("错误的工作日历范围:" + req.get("KAISSJ") + "到" + req.get("JIESSJ"));
				}
			}
		}
		return result;
	}

	/**
	 * @description	初始排产参数
	 * @author 余飞
	 * @date 2012-1-13
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */ 
	@SuppressWarnings("unchecked")
	public void initParam(final List<Map<String,String>> shengxList, final Map<String,String> params) {
		final StringBuffer lStrChanx = new StringBuffer();
		noWorkCalReqs = new ArrayList<Map<String,String>>();
		String flag = "";
		for (Map<String, String> changx : shengxList) {
			changx.put("SHENGCJP", String.valueOf(changx.get("SHENGCJP")));
			changx.put("EVERYTIME", String.valueOf(changx.get("EVERYTIME")));
			lStrChanx.append(flag).append('\'').append(changx.get("SHENGCXBH").toString()).append('\'');
			flag = ",";
		}
		/*订单状态 已发送(4)*/
		params.put("ZHUANGT", "4");
		params.put("DINGDH", "");
		params.put("ZHUXFX", "0");
		params.put("MINROUND", "0.01");
		params.put("shengcx", lStrChanx.toString());
		params.putAll((Map<String,String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("monpc.getPCArg", params));
		params.put("TIQQ", String.valueOf(params.get("TIQQ")));
		params.put("ZENGCTS", String.valueOf(params.get("ZENGCTS")));
		params.put("MINTIME", String.valueOf(params.get("MINTIME")));
		params.put("GUNDZQ", String.valueOf(params.get("GUNDZQ")));
		params.put("FENGBQ", String.valueOf(params.get("FENGBQ")));
		setShengxIds(lStrChanx.toString());
		/*解析开始时间和结束时间*/
		parseDailyRollSJ(shengxList,params);
		final Map<String,String> dateRange = new HashMap<String, String>();
		dateRange.put("MINSJ", params.get("kaissj"));
		dateRange.put("MAXSJ", params.get("nextjiessj")); 	
		/*将生产线的工作日历设置到全局变量中*/
		setWorkCalendar(getWorkCalendar(params, dateRange));
		setLingjOfLine(getLingjOfLines(params));
		setMessagePc(new ArrayList<Map<String, String>>());
		/*生成月模拟计划号，日滚动计划号*/
		setYueMnjhh(getYueMoNijhh(shengxList,params));
		if("Y".equals(params.get("biaos")) || "G".equals(params.get("biaos"))){
			setGongyzqKaissj(params);
			Map<String, String> gongyzq = (Map<String, String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("monpc.queryNextGyzq",params);
			params.put("kaissjNextZQ", gongyzq.get("KAISSJ"));
			params.put("jiessjNextZQ", gongyzq.get("JIESSJ"));
		}else{
			params.put("period", "");
		}
		/*清除消息提示*/
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteMessage",params);
		/*模拟排产前检查数据*/
		checkDate(shengxList,params);
	}
	/**
	 * @description	获得排产零件对应生产线关系集合
	 * @author 余飞
	 * @date 2012-1-16
	 * @param params  包含产线和排产时间范围
	 * @return        零件对应生产线关系集合
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Map<String,String>>> getLingjOfLines(Map<String, String> params) { 
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.getLinesOfLingj", params);
		Map<String, List<Map<String,String>>> result = new HashMap<String, List<Map<String,String>>>();
		for(Map<String,String> map : list) {
			if (result.containsKey(map.get("LINGJBH"))) {
				result.get(map.get("LINGJBH")).add(map);
			} else {
				List<Map<String,String>> line = new ArrayList<Map<String,String>>();
				line.add(map);  
				result.put(map.get("LINGJBH"), line);
			}
		}
		return result;
	}

	public abstract void parseMaoxq(List<Map<String,String>>shengxList, Map<String,String> params);
	
	public abstract void parseMaoxqWbddyg(List<Map<String,String>>shengxList, Map<String,String> params);
	
	public abstract Map<String, String> getYueMoNijhh(List<Map<String,String>>shengxList, Map<String,String> params);
	
	public abstract void parseRiMargin(List<Map<String,String>>shengxList, Map<String,String> params);
	/**
	 * @description	拆分备储零件毛需求
	 * @author 余飞
	 * @date 2012-1-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public void parseBeic(final List<Map<String, String>> shengxList,
			final Map<String, String> params) {  
		Map<String,List<Map<String, String>>> DailyXQ = new HashMap<String,List<Map<String, String>>>();
		Map<String, String> paramsTemp = new HashMap<String,String>();
		paramsTemp.putAll(params);
		paramsTemp.put("Dingdlx", "BC");
//		paramsTemp.put("kaissj", DateUtil.dateAddDays(params.get("today"),-2));
		List<Map<String,String>> requirements = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryBeicjh",paramsTemp);
//		parseBeicxh(paramsTemp, requirements, shengxList);
		parseRequirements(paramsTemp, requirements,shengxList,DailyXQ);
		createBeicxh(shengxList,paramsTemp, requirements,DailyXQ);
	}
	
	public abstract void cleanInitData(Map<String,String> params);
	
	public abstract void statDailyLingj(List<Map<String,String>>shengxList, Map<String,String> params);
	
	public abstract void calYueMoN(List<Map<String,String>>shengxList, Map<String,String> params,List<Map<String,String>> qckc);
	
	public abstract void parseOutPutNup(Map<String,String> params);
	
	public abstract void deleteZCPC(Map<String,String> params);
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getChangxList(final Map<String,String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryChanx",params); 
	} 

	@SuppressWarnings("unchecked")
	public void parseRiMaoxq(List<Map<String, String>> shengxList,
			Map<String, String> params) {
		Map<String, String> map = new HashMap<String,String>();
		Map<String,List<Map<String, String>>> DailyXQ = new HashMap<String,List<Map<String, String>>>();
		map.putAll(params);
		map.put("Dingdlx", "PJ");
		map.put("DingdlxN", "NJ");
		map.put("jiessj", params.get("nextjiessjRi"));
		/*根据仓库代码，查询出客户提前期*/
		Map<String, String> maoParam = getOrderAheadOfTime(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQParam",map));
		maoParam.putAll(params);
		maoParam.put("Dingdlx", "PJ");
		maoParam.put("DingdlxN", "NJ");
		/*查询出PJ订单的既定订单数据*/
		parseRequirements(maoParam, baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQ",map),shengxList,DailyXQ);
//		/*查询出需要拆分订单的用户中心*/
//		List<Map<String, String>> usercenter = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQUsercenter",map);
//		for(Map<String, String> usercenterMap : usercenter){
//			Map<String, String> userMap = new HashMap<String,String>();
//			userMap.putAll(map);
////			String userc = usercenterMap.get("USERCENTER");
//			userMap.put("NewUsercenter", usercenterMap.get("USERCENTER"));
//			userMap.put("GONGYSDM", usercenterMap.get("GONGYSDM"));
//			/*查询这个用户中心下最新的订单号*/
//			String dingdh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("monpc.queryYueMaoXQNewDingdh",userMap);
//			userMap.put("DINGDH", dingdh);
//			/*根据最新的订单号，查询出这个用户中心下PJ订单的预告*/
//			List<Map<String, String>> req = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQYuG",userMap);
//			/*拆分每天的pj预告*/
//			parseRequirements(maoParam, req,shengxList,DailyXQ);
//		}
		parseMaoxqGEVP(shengxList,params,DailyXQ);
		/*将拆分好的pj订单插入系统，同时删除掉对应日期的PP订单数据*/
		if(DailyXQ.size()>0){
			insertDailyXQ(map,shengxList,DailyXQ);
		}

	}

	/**
	 * @description 解析GEVP外部要货令
	 * @author 王国首
	 * @date 2012-3-20
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public void parseMaoxqGEVP(List<Map<String, String>> shengxList,
			Map<String, String> params,Map<String,List<Map<String, String>>> DailyXQ) {
		Map<String, String> map = new HashMap<String,String>();
		map.putAll(params);
		map.put("xqjiessj", params.get("nextjiessjRi"));
		Map<String, String> maoParam = getOrderAheadOfTime(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGEVPMaoXQParam",map));
//		Map<String, String> maoParam = new HashMap<String, String>();
		maoParam.put("xqjiessj", params.get("nextjiessjRi"));
		maoParam.put("Dingdlx", "GEVP");
		maoParam.putAll(params);
		List<Map<String,String>> requirements = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGEVPMaoXQ",maoParam);
		Map<String, List<String>> workCal = getReqMatchOfWorkCal(map, requirements);
		for(int i = requirements.size()-1;i>=0;i--){
			Map<String,String> reqMap = requirements.get(i);
			int deferShij = Integer.parseInt(maoParam.get("DEFERGEVP"));
			if(reqMap.get("JIESSJ").compareTo(reqMap.get("KAISSJ"))>=0){
				List<String> ril = getWorkSubCal(workCal.get(reqMap.get("SHENGCXBH")),reqMap.get("KAISSJ"),reqMap.get("JIESSJ"));
				if(ril.size()>deferShij){
					reqMap.put("KAISSJ", ril.get(deferShij));
				}else{
					requirements.remove(i);
				}
			}else{
				requirements.remove(i);
			}
		}
		parseRequirements(maoParam, requirements,shengxList,DailyXQ);
	} 
	
	/**
	 * @description	根据时间范围及产线查询各个产线的工作日历
	 * @author 余飞
	 * @date 2012-1-17
	 * @param params    包含产线和排产时间范围
	 * @param dateRange 查询的时间范围
	 * @return          各个产线的工作日历
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getWorkCalendar(final Map<String, String> params, 
			final Map<String, String> dateRange){
		final Map<String, List<String>> result = new HashMap<String, List<String>>();
		final Map<String, String> query = new HashMap<String, String>();
		query.putAll(params);
		query.putAll(dateRange);
		
		final List<Map<String, String>> cals = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrl",query);
		Map<String, String> chanxz = new TreeMap<String,String>();
		for (Map<String, String> shengcxCal : cals){
			List<String> shengcxList = null;
			if (result.containsKey(shengcxCal.get("SZXBH"))) {
				shengcxList = result.get(shengcxCal.get("SZXBH"));
			} else {
				shengcxList = new ArrayList<String>();
			}
			shengcxList.add(shengcxCal.get("RIQ"));
			chanxz.put(shengcxCal.get("RIQ"), shengcxCal.get("RIQ"));
			result.put(shengcxCal.get("SZXBH"), shengcxList);
		} 
		List<String> chanxzList = new ArrayList<String>();
		chanxzList.addAll(chanxz.values());
		result.put(params.get("chanxzbh"), chanxzList);
		return result;
	}


	/**
	 * @description	按照产线的工作日历对零件需求进行拆分
	 * @author 余飞
	 * @date 2012-1-17
	 * @param params        包含产线和排产时间范围
	 * @param requirements  零件需求列表 
	 */
	public void parseRequirements(final Map<String, String> params, 
			final List<Map<String,String>> requirements) { 
		if (requirements.size() <= 0){return;}   
		final Map<String, List<String>> workCal = getReqMatchOfWorkCal(params, requirements); 
		parseMaoxqxh(params, requirements);
		for (Map<String,String> req : requirements) {
			try {
				parseRequirement(params, req, workCal);
			} catch(NoWorkDayException noWorkDay) {
				if(req.get("JIESSJ").compareTo(params.get("kaissj"))>=0){
					noWorkCalReqs.add(req);
				}
			} 
		}
		
	}
	
	/**
	 * @description 获取订单对应的各个生产线工作日历
	 * @author 余飞
	 * @date 2012-1-18
	 * @param params         包含产线和排产时间范围
	 * @param requirements   零件需求列表 
	 * @return               各个生产线工作日历
	 */
	private Map<String, List<String>> getReqMatchOfWorkCal(final Map<String, String> params, 
			final List<Map<String, String>> requirements) {
		final List<String> list = new ArrayList<String>(); 
		for (Map<String,String> req : requirements) {
			setOrderAheadOfTime(req,params);
			list.add(req.get("KAISSJ"));
			list.add(req.get("JIESSJ"));
		}
		list.add(params.get("kaissj"));
		Collections.sort(list); 
		final Map<String,String> dateRange = new HashMap<String, String>();
		dateRange.put("MINSJ", list.get(0));
		dateRange.put("MAXSJ", list.get(list.size() - 1)); 
		return this.getWorkCalendar(params, dateRange);
	}

	/**
	 * @description 按照产线的工作日历对零件需求进行拆分
	 * @author 余飞
	 * @date 2012-1-18
	 * @param params        包含产线和排产时间范围
	 * @param req           零件需求
	 * @param workCal       产线的工作日历
	 * @throws NoWorkDayException   没有工作日抛出NoWorkDayException异常
	 */	
	public List<Map<String, String>> parseRequirement(final Map<String, String> params, final Map<String, String> req,
			final Map<String, List<String>> workCal) throws NoWorkDayException { 
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		final int lingjsl = Integer.parseInt(req.get("LINGJSLCX"));
		if(0==lingjsl && params.get("Dingdlx") != null && !"BC".equals(params.get("Dingdlx"))){
			return result;
		}
		List<String> shengcxCal = workCal.get(req.get("SHENGCXBH"));
		if(shengcxCal == null || shengcxCal.size() <= 0){
			shengcxCal = new ArrayList<String>();
			shengcxCal.addAll(getWorkCalendar().get(req.get("SHENGCXBH")));
		}
		List<String> subCal = findSubCal(shengcxCal, req);
		final int days = subCal.size();
		final int lingjslAvg = lingjsl / days;
		int mod = lingjsl - (lingjslAvg * days);
		for (String aCal : subCal) {
			Map<String, String> temp = new HashMap<String, String>();
			temp.putAll(req);
			temp.put("date", aCal);
			if (mod > 0) {
				mod--;
				temp.put("ljsl", String.valueOf(lingjslAvg + 1));
			} else {
				temp.put("ljsl", String.valueOf(lingjslAvg));
			}
			temp.put("biaos", params.get("biaos"));
			temp.put("USERCENTER", params.get("USERCENTER"));
			if(params.get("Dingdlx")!= null && ("PJ".equals(params.get("Dingdlx")) || "GEVP".equals(params.get("Dingdlx")) || "BC".equals(params.get("Dingdlx")))){
				result.add(temp);
			}else{
				insertLingjrxqb(temp);
			}
		} 
		return result;
	}

	/**
	 * @description 查找生产线对应订单的工作日历
	 * @author 余飞
	 * @date 2012-1-18
	 * @param shengcxCal  生产线日历
	 * @param req	                订单需求
	 * @return			  订单对应产线的工作日历
	 * @throws NoWorkDayException   没有工作日抛出NoWorkDayException异常
	 */
	public List<String> findSubCal(List<String> shengcxCal, Map<String, String> req) 
			throws NoWorkDayException {
		List<String> subCal = null;
		if (req.get("KAISSJ").equals(req.get("JIESSJ"))) {
			int index = shengcxCal.indexOf(req.get("KAISSJ"));
			subCal = new ArrayList<String>();
			if (index >= 0){ 
				subCal.add(shengcxCal.get(index));
			} 
		} else {
			subCal = getWorkSubCal(shengcxCal,req.get("KAISSJ"),req.get("JIESSJ"));
		}
		if (subCal.size() <= 0) {
			subCal = getAheadOfTimeCal(workCalendar.get(req.get("SHENGCXBH")), req.get("KAISSJ"));
		}
		return subCal;
	}
	
	/**
	 * @description 获取开始时间之前的所有工作时间
	 * @author 余飞
	 * @date 2012-1-19
	 * @param shengcxCal        工作日历列表
	 * @param beginDateStr		开始时间
	 * @return 					工作日历
	 * @throws NoWorkDayException   没有工作日抛出NoWorkDayException异常
	 */ 
	private List<String> getAheadOfTimeCal(final List<String> shengcxCal, 
			final String beginDateStr) throws NoWorkDayException {
		final List<String> subCal = getWorkSubCal(shengcxCal, shengcxCal.get(0), beginDateStr);
		if (subCal.size() <= 0) {
			final StringBuffer buf = new StringBuffer();
			throw new NoWorkDayException(buf.append(shengcxCal.get(0)).append(" ")
					.append(beginDateStr).toString());
		}
		return subCal;
	}

	/**
	 * @description 设置订单提前期
	 * @author 余飞
	 * @date 2012-1-31
	 * @param req     毛需求订单
	 * @param params  包含订单提前期
	 */
	public void setOrderAheadOfTime(final Map<String, String> req, final Map<String, String> params) {
		if (!req.containsKey("LINGJBH")||!req.containsKey("CANGKDM")) {
			return;
		}
		final String key = req.get("LINGJBH") + req.get("CANGKDM");
		if (params.containsKey(key)) {
			final int day = Integer.parseInt(params.get(key)); 
			try {
				if(req.get("PCFLAG") != null && !req.get("PCFLAG").equals("GEVP")){
					req.put("KAISSJ", calAheadOfTime(req.get("KAISSJ"), day));
				}
				req.put("JIESSJ", calAheadOfTime(req.get("JIESSJ"), day));  
			} catch (ParseException e) {
				logger.error("错误的时间字符:" + req.get("KAISSJ") + "到" + req.get("JIESSJ"));
			}
		} 
	}
	
	/**
	 * @description 计算时间提前期
	 * @author 余飞
	 * @date 2012-1-31
	 * @param dateStr  时间字符串（格式是yyyy-MM-dd）
	 * @param day	        提前天数
	 * @return         提前后的时间字符串（格式是yyyy-MM-dd）
	 * @throws ParseException  输入时间字符串格式与解析格式不匹配
	 */
	public String calAheadOfTime(final String dateStr, final int day) throws ParseException {
		final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		final Date date = formatDate.parse(dateStr);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1*day);
		return formatDate.format(calendar.getTime());
	}

	/**
	 * @description 在工作日历内截取特定开始时间，结束时间的工作日历】
	 * @author 余飞
	 * @date 2012-2-1
	 * @param shengcxCal    工作日历列表
	 * @param kaissj        开始时间
	 * @param jiessj        结束时间
	 * @return subCal		工作日历
	 */	
	public List<String> getWorkSubCal(final List<String> shengcxCal, final String kaissj, final String jiessj) {
		if (!validDateOfBetween(kaissj, jiessj)) {
			return new ArrayList<String>();
		}
		boolean removeBegin = false;
        boolean removeEnd = false;
		int fromIndex = findWorkCalIndex(shengcxCal, kaissj);
		if (fromIndex < 0) {
			shengcxCal.add(kaissj);
			fromIndex = findWorkCalIndex(shengcxCal, kaissj);
			removeBegin = true;			
		}
        int toIndex = findWorkCalIndex(shengcxCal, jiessj);
		if (toIndex < 0) {
			shengcxCal.add(jiessj);
			toIndex = findWorkCalIndex(shengcxCal, jiessj);
			removeEnd = true;
		}
		if (removeEnd) {
			toIndex = findWorkCalIndex(shengcxCal, jiessj);
		}
		if (removeBegin) {
			fromIndex = findWorkCalIndex(shengcxCal, kaissj);
		}
		final List<String> subCal = shengcxCal.subList(fromIndex, toIndex+1); 
		if (removeBegin) {
			subCal.remove(0); 
		}
		if (removeEnd) {
			subCal.remove(subCal.size() - 1);
		} 
		return subCal;
	}	
	
	/**
	 * @description 检查开始时间是否大于结束时间
	 * @author 余飞
	 * @date 2012-2-1
	 * @param beginDate      开始时间
	 * @param endDate        结束时间
	 * @return boolean		 正确返回true，否者返回false
	 */	
	public boolean validDateOfBetween(String beginDate, String endDate) {
		boolean result = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = format.parse(beginDate);
			Date end = format.parse(endDate);
			result = begin.getTime() <= end.getTime();
		} catch (ParseException e) { 
			logger.error("错误的时间字符:" + beginDate + "到" + endDate);
		}
		return result;
	}
	/**
	 * @description 插入零件日需求表
	 * @author 王国首
	 * @date 2012-1-4
	 * @param result	插入数据的参数
	 */
	@Transactional
	public void insertLingjrxqb(final Map<String, String> result) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertLingjrxqb",result);
	}
	
	/**
	 * @description 在工作日历中查找输入时间的下标数
	 * @author 余飞
	 * @date 2012-1-9
	 * @param shengcxCal 生产线工作日历
	 * @param dateStr	 日期
	 * @return 在工作日历中查找输入时间的下标数
	 */
	public int findWorkCalIndex(final List<String> shengcxCal, final String dateStr) {
		Collections.sort(shengcxCal); 
		return shengcxCal.indexOf(dateStr); 	 
	}
	
	/**
	 * @description 按照产线的工作日历对日订单零件需求进行拆分
	 * @author 余飞
	 * @date 2012-1-11
	 * @param params        包含产线和排产时间范围
	 * @param requirements  零件需求列表 
	 */
	public void parseRequirements(final Map<String, String> params, final List<Map<String,String>> requirements,
			List<Map<String, String>> shengxList,Map<String,List<Map<String, String>>> DailyXQ) { 
		if (requirements.size() <= 0){return;} 
		final Map<String, List<String>> workCal = getReqMatchOfWorkCal(params, requirements); 
		parseMaoxqxh(params, requirements);
		List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
		for (Map<String,String> req : requirements) {
			try {
				if (DailyXQ.containsKey(req.get("SHENGCXBH"))) {
					temp = DailyXQ.get(req.get("SHENGCXBH"));
				} else {
					temp = new ArrayList<Map<String, String>>();
				}
				temp.addAll(parseRequirement(params, req, workCal));
				DailyXQ.put(req.get("SHENGCXBH"), temp);
			} catch(NoWorkDayException noWorkDay) {
				//如果需求的结束时间小于计算开始时间，则此条需求不记录在无工作日历列表中
				if(req.get("JIESSJ").compareTo(params.get("kaissj"))>=0){
					noWorkCalReqs.add(req);
				}
			}
		}
	}
	
	/**
	 * @description 在拆分的日需求列表中得到一条产线最大的日期
	 * @author 余飞
	 * @date 2012-1-16
	 * @param temp 一条产线所有的日订单需求
	 * @return 最大的时间
	 */
	public String findMaxTime(final List<Map<String, String>> temp,String key,final Map<String, String> params) {
		String result = "";
		String jiessrRi = params.get("jiessjRi");
//		String kaissjTIQQ = params.get("kaissjTIQQ");
		for( int i = temp.size()-1;i>=0;i--){
			Map<String, String> dailyXQ = temp.get(i);
			if("G".equals(params.get("biaos")) && dailyXQ.get(key).compareTo(jiessrRi)>0){
				temp.remove(i);
				continue;
			}
			if(dailyXQ.get(key).compareTo(result)>0){
				result = dailyXQ.get(key);
			}
		}
		updateXuQState(params,temp);
		return result;
	}
	
	/**
	 * @description 删除掉需求要覆盖的PP订单数据
	 * @author 王国首
	 * @date 2012-2-10
	 * @param params	 包含产线和排产时间范围
	 */
	public void deleteCoverRXQ(Map<String, String> params) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteCoverLingjrxqb",params);
	} 
	
	/**
	 * @description 将一个List<Map<String,String>>结构的List 转换为一个Map<String,String>
	 * @author 王国首
	 * @date 2011-12-22
	 * @param List<Map<String,String>>,， 
	 * @param key "key"对应的值作为KEY
	 * @param value "Value"对应的值作为Value
	 * @return Map<String,String> 
	 */
	public Map<String,String> convertListToMap(final List<Map<String,String>> list,
			final String key,final String value){
		final Map<String,String> map = new HashMap<String,String>();
		for(Map<String,String> temp : list){
			map.put( String.valueOf(temp.get(key).toString()), String.valueOf(temp.get(value)));
		}
		return map;
	}
	
	/**
	 * @description 根据时间范围及产线查询各个产线的工作日历
	 * @author 王国首
	 * @date 2011-12-26
	 * @param params    包含产线和排产时间范围
	 * @param dateRange 查询的时间范围
	 * @return          各个产线的工作日历
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String,String>> getWorkCalendarMap(final Map<String, String> params){
		final Map<String, Map<String,String>> result = new HashMap<String, Map<String,String>>();

		final List<Map<String, String>> cals = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrl",params);  
		for (Map<String, String> shengcxCal : cals){
			Map<String, String> temp = null;
			if (result.containsKey(shengcxCal.get("SZXBH"))) {
				temp = result.get(shengcxCal.get("SZXBH"));
			} else {
				temp = new HashMap<String, String>();
			}
			temp.put(shengcxCal.get("RIQ"),shengcxCal.get("RIQ"));
			result.put(shengcxCal.get("SZXBH"), temp);
		} 
		return result;
	}
	
	/**
	 * @description 将警报消息插入系统
	 * @author 王国首
	 * @date 2012-2-8
	 * @param params	 包含产线和排产时间范围
	 */
	public void insertMessage() {
		List<Map<String, String>> messageList = getMessagePc();
		//修改为使用批量插入数据
		if(messageList != null && messageList.size()>0){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertMessage",messageList);
		}
//		for( Map<String, String> msg : getMessagePc()){
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMessage",msg);
//		}
	} 
	
	/**
	 * @description 将一个List 拷贝到另一个List中
	 * @author 王国首
	 * @date 2012-2-2
	 * @param source	原List
	 * @return result	拷贝完后的List
	 */
	public List<Map<String, String>> listMapCopy(List<Map<String, String>> source){
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for(Map<String, String> sourceTemp : source){
			Map<String, String> ob = new HashMap<String, String>();
			ob.putAll(sourceTemp);
			result.add(ob);
		}
		return result;
	}

	/**
	 * @description 将报警消息添加到消息列表
	 * @author 王国首
	 * @date 2012-2-7
	 * @param usercenter	用户中心
	 * @param shengcxbh		生产线
	 * @param riq			日期
	 * @param yuemnjhh		月模拟计划号
	 * @param xiaox			消息
	 * @param leix			类型
	 */
	public void addMessage(Map<String, String> params,String yuemnjhh,String usercenter,String shengcxbh,String shij,String xiaox,String leix){
		Map<String, String> tempMsg = new HashMap<String, String>();
		tempMsg.put("USERCENTER", usercenter);
		tempMsg.put("CHANXH", shengcxbh);
		tempMsg.put("SHIJ", shij);
		tempMsg.put("JIHH", yuemnjhh);
		tempMsg.put("XIAOX", xiaox);
		tempMsg.put("LEIX", leix);
		tempMsg.put("BIAOS", params.get("biaos"));
		tempMsg.put("CHANXZBH", params.get("chanxzbh"));
		tempMsg.put("EDITOR", params.get("jihybh"));
		tempMsg.put("EDIT_TIME", getTimeNow(TIMEFORMAT));
		tempMsg.put("CREATOR", params.get("jihybh"));
		tempMsg.put("CREATE_TIME", getTimeNow(TIMEFORMAT));
		getMessagePc().add(tempMsg);
	}
	
	/**
	 * @description 得到今天的时间
	 * @author 王国首
	 * @date 2012-1-14
	 * @param format 	日期的格式
	 * @param String	 一定格式的日期
	 */
	public String getTimeNow(String format){
		Date date = new Date(); 
		SimpleDateFormat fmat = new SimpleDateFormat(format); 
		return fmat.format(date);
	}
	
	/**
	 * @description 模拟排产前检查数据
	 * @author 王国首
	 * @date 2012-2-2
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkDate(final List<Map<String,String>> shengxList, final Map<String,String> params){
		/*检查计划排产时间内，产线组工作日历是否为空*/
		checkCalendar(shengxList,params);
		checkBaozrl(shengxList,params);
//		checkShengcbl(shengxList,params);
	}
	
	/**
	 * @description 检查需求是否为空，为空时抛出提示信息给用户
	 * @author 王国首
	 * @date 2012-2-2
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkRequirement(final List<Map<String,String>> shengxList, final Map<String,String> params){
		List<Map<String, String>> addTiqqRilList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryAddTiqqRil",params);
		int tiQianqi = Integer.parseInt(params.get("TIQQ"));
		if(addTiqqRilList.size() <= tiQianqi){
			for( Map<String,String> shengcx : shengxList){
				String xiaox = "产线"+shengcx.get("SHENGCXBH")+","+params.get("kaissj")+"到"+params.get("jiessj")+"计划排产时间内需求为空";
				this.addMessage(params,getYueMnjhh().get(shengcx.get("SHENGCXBH")),params.get("USERCENTER"),shengcx.get("SHENGCXBH"),params.get("kaissj"),xiaox,"9");
			}
			insertMessage();
			throw new PCRunTimeException(new Message("paicXqIsNull.error","i18n.pc.pc").getMessage());
		}
	}
	
	/**
	 * @description 检查工作日历，当工作日历为空时，提示给用户
	 * @author 王国首
	 * @date 2012-2-2
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkCalendar(final List<Map<String,String>> shengxList, final Map<String,String> params){
		List<String> chanxzCalendar = new ArrayList<String>();
		/*将产线组所有产线汇总到一个EXCEL中*/
		for( int i = 0 ;i<shengxList.size() && getWorkCalendar().size()>0;i++){
			Map<String,String> shengcx = shengxList.get(i);
			if(getWorkCalendar().get(shengcx.get("SHENGCXBH")) == null){
				String xiaox = "生产线"+shengcx.get("SHENGCXBH")+"没有对应的工作日历，请增加工作日历！";
				this.addMessage(params,getYueMnjhh().get(shengcx.get("SHENGCXBH")),params.get("USERCENTER"),shengcx.get("SHENGCXBH"),params.get("kaissj"),xiaox,"9");
				insertMessage();
				throw new ServiceException(xiaox);
			}
			chanxzCalendar.addAll(getWorkSubCal(getWorkCalendar().get(shengcx.get("SHENGCXBH")),params.get("kaissj"),params.get("jiessj")));
		}
		/*判断当产线组下所有产线的工作日历为空时，将提示信息写入数据库，类型为9，并抛出异常*/
		if(chanxzCalendar.size() == 0){
			for( Map<String,String> shengcx : shengxList){
				String xiaox = "产线"+shengcx.get("SHENGCXBH")+", "+params.get("kaissj")+"到"+params.get("jiessj")+"计划排产时间内工作日历为关闭状态，请调整工作日历";
				this.addMessage(params,getYueMnjhh().get(shengcx.get("SHENGCXBH")),params.get("USERCENTER"),shengcx.get("SHENGCXBH"),params.get("kaissj"),xiaox,"9");
			}
			insertMessage();
			throw new PCRunTimeException(new Message("calendarIsNull.error","i18n.pc.pc").getMessage());
		}
	}

	/**
	 * @description 解析日订单结束时间
	 * @author 王国首
	 * @date 2012-1-12
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void parseDailyRollSJ(List<Map<String, String>> shengxList,Map<String, String> params) {
		Map<String, String> paramsTemp = new HashMap<String, String>();
		paramsTemp.putAll(params);
		String jiessj = params.get("jiessj") == null ? params.get("kaissj"):params.get("jiessj");
		paramsTemp.put("jiessj", DateUtil.dateAddDays(jiessj,30));
		List<Map<String, String>> chanxzrl =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrlChanxz",paramsTemp); 
		int gunD = Integer.parseInt(paramsTemp.get("GUNDZQ"));
		int index =  Integer.parseInt(paramsTemp.get("TIQQ")) + gunD;
		if(chanxzrl.size() >0 && chanxzrl.size()>=index+5){
			params.put("kaissjRi", chanxzrl.get(0).get("RIQ"));
			params.put("jiessjRi", chanxzrl.get(gunD-1).get("RIQ"));
//			params.put("nextjiessjRi", chanxzrl.get(index-1).get("RIQ"));
			//取日需求时，结束时间为 滚动周期 + 提前期 + 5天的天数（用来考虑订单提前期）。
			params.put("nextjiessjRi", chanxzrl.get(index+4).get("RIQ"));
			params.put("kaissjTIQQ", chanxzrl.get(Integer.parseInt(paramsTemp.get("TIQQ"))).get("RIQ"));
			int fengBQ =Integer.parseInt(params.get("FENGBQ"));
			fengBQ = fengBQ>0?fengBQ-1:fengBQ;
			int tiqq =  Integer.parseInt(params.get("TIQQ"));
			int deferShij = fengBQ + tiqq;
			params.put("DEFERGEVP", String.valueOf(deferShij));
			if("R".equals(params.get("biaos"))){
				params.put("kaissj", params.get("kaissjRi"));
				params.put("jiessj", params.get("jiessjRi"));
				params.put("nextjiessj", params.get("nextjiessjRi"));
			}
		}
	}

	/**
	 * @description 计算每个零件的安全库存
	 * @author 王国首
	 * @date 2011-12-19
	 * @param 	params	包含产线和排产时间范围
	 * @return  Map		用户中心+零件编号为key的Map类型的安全库存
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> calAnqkc(Map<String, String> params){
		List<Map<String, String>> gongztsLIst = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryCangkuWorkDays",params);
		List<Map<String, String>> anqkcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryAnqkcts",params);
		Map<String, String> gongzTsMap = convertListToMap(gongztsLIst,"IDH","TIANS");
		Map<String, String> anqkcMap = new HashMap<String, String>();
		double Anqkc = 0;
		double gongZuots= 0;
		for(int i = 0 ;i<anqkcList.size();i++){
			Anqkc = 0;
			gongZuots= 0;
			double anqkcTop = 0;
			Map<String, String> tempMap = anqkcList.get(i);
			/*计算每个零件的安全库存，如果安全库存数量为空时，则安全库存 = 安全库存天数乘以日均交付*/
			if(tempMap.get("ANQKCSL") != null && Double.parseDouble(String.valueOf(tempMap.get("ANQKCSL")))>0){
				Anqkc = Double.parseDouble(String.valueOf(tempMap.get("ANQKCSL")));
			}else if(tempMap.get("ANQKCTS") != null && Double.parseDouble(String.valueOf(tempMap.get("ANQKCTS")))>0){
				String IDH= tempMap.get("USERCENTER")+tempMap.get("CANGKBH"); 
				if(gongzTsMap.containsKey(IDH)){
					gongZuots = Double.parseDouble(gongzTsMap.get(IDH));
					Anqkc = Math.ceil((Double.parseDouble( String.valueOf(tempMap.get("LINGJSL")))/gongZuots)*Double.parseDouble( String.valueOf(tempMap.get("ANQKCTS"))));
				}
			}
			if(tempMap.get("ANQKCTOPSL") != null){
				anqkcTop = Double.parseDouble(String.valueOf(tempMap.get("ANQKCTOPSL")));
			}
			anqkcMap.put(tempMap.get("USERCENTER")+tempMap.get("LINGJBH"), String.valueOf(Anqkc));
			anqkcMap.put(tempMap.get("USERCENTER")+tempMap.get("LINGJBH")+"TOP", String.valueOf(anqkcTop));
		}
		return anqkcMap;
	}
	
	/**
	 * @description 检测期初库存是否小于等于0
	 * @author 王国首
	 * @date 2012-2-12
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkQckc(Map<String,String> params,String lingjbh,double kuc,String shengcx,String usercenter,String shij,String leix){
		 if(kuc<=0){ 
			 String xiaox = "产线"+shengcx+", 零件"+lingjbh+" 在"+shij+"期初库存为"+kuc+",期初库存小于等于0，请计划员调整生产"; 
			 this.addMessage(params,yueMnjhh.get(shengcx),usercenter,shengcx,shij,xiaox,leix); 
		} 

	}
	
	/**
	 * @description 将未拆分的需求从期初库存中减去
	 * @author 王国首
	 * @date 2012-2-25
	 * @param params	包含产线和排产时间范围
	 * @param qckc		期初库存
	 */
	public void calQckcMinusXQ(Map<String,String> params,List<Map<String,String>> qckc){
		if(noWorkCalReqs != null && noWorkCalReqs.size()>0){
			for(Map<String, String> Temp : noWorkCalReqs){
				for(int i = 0;i<qckc.size();i++){
					Map<String, String> qckcTemp = qckc.get(i);
					if(qckcTemp.get("LINGJBH").equals(Temp.get("LINGJBH"))){
						double kuc = Double.parseDouble(String.valueOf(qckcTemp.get("QCKC")))-Double.parseDouble(String.valueOf(Temp.get("LINGJSLCX")));
						qckcTemp.put("QCKC", String.valueOf(kuc));
						checkQckc(params,qckcTemp.get("LINGJBH"),kuc,Temp.get("SHENGCXBH"),params.get("USERCENTER"),params.get("kaissj"),"2");
						qckc.set(i, qckcTemp);
						break;
					}
				}
			}			
		}
	}
	
	/**
	 * @description 将计算出的零件安全库存设置到零件期初库存列表中
	 * @author 王国首
	 * @date 2011-12-21
	 * @param qckc	 	期初库存
	 * @param anqkcMap	安全库存
	 * @return  qckc	期初库存
	 */
	public List<Map<String, String>> setAnqkcToQckc(Map<String,String> params,List<Map<String,String>> qckc,Map<String, String> anqkcMap){
		String anqkcKey = "";
		for(int i = 0;i<qckc.size();i++){
			Map<String, String> qckcMap = qckc.get(i);
			qckcMap.put("QCKC", String.valueOf(Double.parseDouble(String.valueOf(qckcMap.get("QCKC")))));
			anqkcKey = qckcMap.get("USERCENTER")+qckcMap.get("LINGJBH");
			if(anqkcMap.containsKey(anqkcKey)){
				qckcMap.put("ANQKC", anqkcMap.get(anqkcKey));
			}else{
				qckcMap.put("ANQKC", "0");
			}
			if(anqkcMap.containsKey(anqkcKey+"TOP")){
				qckcMap.put("ANQKCTOP", anqkcMap.get(anqkcKey+"TOP"));
			}else{
				qckcMap.put("ANQKCTOP", "0");
			}
			qckcMap.put("CREATE_TIME", getTimeNow(TIMEFORMAT));
			qckcMap.put("CREATOR", params.get("jihybh"));
			qckcMap.put("EDITOR", params.get("jihybh"));
			qckcMap.put("EDIT_TIME", getTimeNow(TIMEFORMAT));
			qckc.set(i, qckcMap);
		}
		return qckc;
	}
	
	/**
	 * @description 得到计划时间内的产线组的工作日历
	 * @author 王国首
	 * @date 2011-12-27
	 * @param params	 包含产线和排产时间范围
	 * @return  List	 计划时间内产线组开启的日期
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getChanxzWorkCalendar(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrlChanxz",params);  
	}
	
	/**
	 * @description 根据计划时间查询出的计划工作日历，加上提起期，得到加上提起前的工作日历
	 * @author 王国首
	 * @date 2011-12-19
	 * @param params   		 	包含产线和排产时间范围
	 * @param gongzrlChanxzLIst 排产计划时间段内工作日历
	 * @return Map         		Key为计划工作日历日期，Value为加上提前期的工作日历
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> calAddTiqqRil(Map<String, String> params,List<Map<String, String>> chanxzrl) {
		Map<String,String> addTiqqRlMap = new HashMap<String,String>();
		int tiQianqi = Integer.parseInt(params.get("TIQQ"));
		List<Map<String, String>> addTiqqRilList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryAddTiqqRil",params);
		for(int i = 0;i<chanxzrl.size();i++){
			String addTiqqRiq = "";
			if((i+tiQianqi)<addTiqqRilList.size()){
				addTiqqRiq = addTiqqRilList.get(i+tiQianqi).get("RIQ").toString();
			}
			addTiqqRlMap.put(chanxzrl.get(i).get("RIQ").toString(), addTiqqRiq);
		}
		return addTiqqRlMap;
	}	
	
	/**
	 * @description 计算一天中每个零件的净需求
	 * @author 王国首
	 * @date 2011-12-22
	 * @param gongzrlChanxz 产线组的工作日历
	 * @param beginKucList	期初库存列表
	 * @param params	 包含产线和排产时间范围
	 * @return  Map		计算完成的一天中每个零件的净需求，并计算更新了下一天的期初库存
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<Map<String, String>>> calOneDayJXQ(Map<String, String> gongzrlChanxz,List<Map<String, String>> beginKucList,Map<String, String> params){
		List<Map<String, String>> lingjrxqList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryLingjrxq",params);
		List<Map<String, String>> lingjJinxuqiuUpdate = new ArrayList<Map<String, String>>();
		for(Map<String, String> lingjrxq : lingjrxqList){
			double meiRiJingXuQiu = 0;
			double qiChuKuc = 0;
			lingjrxq.put("RIQ", gongzrlChanxz.get("RIQ").toString());
			//wgs-test
			double aheadJXQMinus= 0;
			if(!isWeekCal() && !params.get("biaos").equals("R")){
				aheadJXQMinus = calAheadJXQMinus(params,lingjrxq.get("LINGJBH"));
			}
			for(int i = 0;i<beginKucList.size();i++){
				Map<String, String> beginKucMap = beginKucList.get(i);
				if(beginKucMap.get("LINGJBH").equals(lingjrxq.get("LINGJBH"))){
					double anqkc = Double.parseDouble( String.valueOf(beginKucMap.get("ANQKC")));
					qiChuKuc= Double.parseDouble(String.valueOf(beginKucMap.get("QCKC")));
					double rixq = Double.parseDouble(String.valueOf(lingjrxq.get("LINGJSLTQ")));
					meiRiJingXuQiu = rixq + anqkc - qiChuKuc + aheadJXQMinus;	
					meiRiJingXuQiu = meiRiJingXuQiu<0?0:meiRiJingXuQiu;
					lingjrxq.put("JINXQ", String.valueOf(meiRiJingXuQiu));
					qiChuKuc = qiChuKuc + meiRiJingXuQiu - Double.parseDouble(String.valueOf(lingjrxq.get("LINGJSLJS")));
					beginKucMap.put("QCKC", String.valueOf(qiChuKuc));
					lingjJinxuqiuUpdate.add(lingjrxq);
					beginKucList.set(i, beginKucMap);
					break;
				}
			}
		}
		Map<String,List<Map<String, String>>> tempMap = new HashMap<String,List<Map<String, String>>>();
		tempMap.put("beginKucList", beginKucList);
		tempMap.put("Jinxuqiu", lingjJinxuqiuUpdate);
		return tempMap;
	}
	
	/**
	 * @description 判断生产线某一天是否工作。
	 * @author 王国首
	 * @date 2011-12-29
	 * @param riq 		日期
	 * @param shengcxbh	生产线编号
	 * @return boolean	如果生产线当天工作则返回true，否者返回false
	 */
	public boolean parseShengcxWork(String riq,String shengcxbh){
		boolean result = false;
		List<String>  ril= getWorkCalendar().get(shengcxbh);
		for(String rilcal: ril){
			if(riq.equals(rilcal)){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * @description 将提前了的净需求从每日的净需求中减去
	 * @author 王国首
	 * @date 2011-12-22
	 * @param params	 包含产线和排产时间范围
	 * @param lingjhb	 零件编号
	 * @return  double		返回需要减去的净需求
	 */
	@SuppressWarnings("unchecked")
	public double calAheadJXQMinus(Map<String, String> params,String lingjhb){
		double result = 0;
		List<String> ril = getWorkSubCal(getWorkCalendar().get(params.get("chanxzbh")),params.get("kaissjWeeKAhead"),params.get("jiessjWeeK"));
		for( String rilcol : ril){
			if(getAheadJXQ().get(rilcol) != null){
				for(int i = 0;i<getAheadJXQ().get(rilcol).size();i++){
					Map<String, String> temp = getAheadJXQ().get(rilcol).get(i);
					if(temp.get("LINGJBH").equals(lingjhb)){
						result = result + Double.valueOf(String.valueOf(temp.get("ljsl")));
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * @description 将计算出的每个零件的净需求更新到系统
	 * @author 王国首
	 * @date 2011-12-20
	 * @param DailyJXQ   	Map中包括（个零件的净需求，零件编号，用户中心，日期）
	 * @return  null
	 */
	public void updateDailyJXQ(List<Map<String, String>> DailyJXQ){
		for(Map<String,String> temp : DailyJXQ){
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiJinXuqiu",temp);
			if(num == 0 && Double.parseDouble( String.valueOf(temp.get("JINXQ"))) >0){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertLingjrxqhzbNotUpdate",temp);
			}
		} 
	}
	
	/**
	 * @description 将拆分好的PJ订单插入系统
	 * @author 王国首
	 * @date 2012-2-26
	 * @param params	 包含产线和排产参数
	 * @return void
	 */
	public void insertDailyXQ(final Map<String, String> params,List<Map<String, String>> shengxList,
			Map<String,List<Map<String, String>>>DailyXQ) {
		Map<String, String> paramsTemp = new HashMap<String, String>();
		List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
		paramsTemp.putAll(params);
		for( Map<String, String> shengcxMap :  shengxList){
			temp = DailyXQ.get(shengcxMap.get("SHENGCXBH"));
			String maxTime = findMaxTime(temp,"date",params);
			paramsTemp.put("maxTime", maxTime);
			paramsTemp.put("shengcx", shengcxMap.get("SHENGCXBH"));
			deleteCoverRXQ(paramsTemp);
			//使用批量插入数据
			if(temp.size()>0){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertLingjrxqb",temp);
			}
//			for( Map<String, String> cxDailyXQ: temp){
//				insertLingjrxqb(cxDailyXQ);
//			}
		}
	}
	
	/**
	 * @description 将拆满足条件的，进行排产了的需求的状态进行更新。
	 * @author 王国首
	 * @date 2012-6-15
	 * @param flagZt	 需要修改状态的需求
	 * @return void
	 */
	public void updateXuQState(Map<String, String> params,final List<Map<String, String>> requirements ) {
		Map<String, Map<String, String>> flagZt= new HashMap<String, Map<String, String>>();
		for(Map<String,String> xuq :requirements){
			String kaissjTIQQ = params.get("kaissjTIQQ");
			String id = xuq.get("ID");
			if(("PJ".equals(xuq.get("PCFLAG")) || "GEVP".equals(xuq.get("PCFLAG"))) && xuq.get("JIESSJ").compareTo(kaissjTIQQ)>=0 && flagZt.get(id) == null){
				flagZt.put(id, xuq);
			}else if(("PP".equals(xuq.get("PCFLAG")) || "WBDDYG".equals(xuq.get("PCFLAG"))) && flagZt.get(id) == null){
				flagZt.put(id, xuq);
			}
		}
		final Iterator<String> it = flagZt.keySet().iterator(); 
		while(it.hasNext()){
			Map<String, String> xuq =flagZt.get(it.next());
			if("PJ".equals(xuq.get("PCFLAG")) || "PP".equals(xuq.get("PCFLAG"))){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlag",xuq);
			}else if("GEVP".equals(xuq.get("PCFLAG"))){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateGevpFlag",xuq);
			}else if("WBDDYG".equals(xuq.get("PCFLAG"))){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateWbddyFlag",xuq);
			}
		}
	}
	
	/**
	 * @description 检查零件US包装容量，US包装数量是否为空
	 * @author 王国首
	 * @date 2012-03-05
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkBaozrl(List<Map<String, String>> shengxList,Map<String, String> params){
		/*查询出零件与包装的对应集合*/
		List<Map<String, String>> baozrl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryBaoz",params);
		Map<String, String> usBaoz = new HashMap<String, String>();
		boolean  flag = false;
		for( Map<String, String> pack : baozrl){
			StringBuffer xiaox = new StringBuffer();
			String baozxx = "产线"+pack.get("SHENGCXBH")+",零件"+pack.get("LINGJBH")+"包装信息有误，请修改零件包装信息";
			/*判断仓库US的包装类型不能为空，仓库US的包装容量不能为空，仓库US的包装容量不能为0*/
			if(pack.get("USBZLX") == null || "".equals(pack.get("USBZLX"))){
				xiaox.append(baozxx+",仓库US的包装类型不能为空");
			}
			if(pack.get("USBZRL") == null || "".equals(pack.get("USBZRL"))){
				xiaox.append(baozxx+",仓库US的包装容量不能为空");
			}else if(Double.parseDouble(String.valueOf(pack.get("USBZRL")))<=0){
				xiaox.append(baozxx+",仓库US的包装容量不能为0");
			}
			if(xiaox.length()>0){
				flag = true;
				this.addMessage(params,yueMnjhh.get(pack.get("SHENGCXBH")),params.get("USERCENTER"),pack.get("SHENGCXBH"),params.get("kaissj"),xiaox.toString(),"9");
			}
			usBaoz.put(params.get("USERCENTER")+pack.get("SHENGCXBH")+pack.get("LINGJBH"),String.valueOf(pack.get("USBZRL")));
		}
		/*判断当包装有错误时间，将错误信息写入系统，并抛出错误信息，终止计算*/
		if(flag){
			insertMessage();
			throw new PCRunTimeException(new Message("packIsNull.error","i18n.pc.pc").getMessage());
		}
		this.setUsPack(usBaoz);
	}
	
	/**
	 * @description 检查产线组生产比例是否为100%
	 * @author 王国首
	 * @date 2012-03-05
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkShengcbl(List<Map<String, String>> shengxList,Map<String, String> params){
		/*查询出产线组下零件的生产比例大于100% 和小于100%  的零件 */
		List<Map<String, String>> shengcbl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryShengcbl",params);
		boolean  flag = false;
		for( Map<String, String> percent : shengcbl){
			for(Map<String, String> changx : shengxList){
				StringBuffer xiaox = new StringBuffer();
				String shencbl = String.valueOf(percent.get("SHENGCBL"));
				if(Double.parseDouble(shencbl)>100){
					xiaox.append("产线组"+percent.get("CHANXZBH")+",零件"+percent.get("LINGJBH")+"生产比例为"+shencbl+"%,超过100%,请调整零件生产比例");
				}else if(Double.parseDouble(shencbl)<100){
					xiaox.append("产线组"+percent.get("CHANXZBH")+",零件"+percent.get("LINGJBH")+"生产比例为"+shencbl+"%,小于100%,请调整零件生产比例");
				}else{
					xiaox.append("产线组"+percent.get("CHANXZBH")+",零件"+percent.get("LINGJBH")+"生产比例为"+shencbl+"%,产线零件生产比例之和应该为100%,请调整零件生产比例");
				}
				flag = true;
				this.addMessage(params,yueMnjhh.get(changx.get("SHENGCXBH")),params.get("USERCENTER"),changx.get("SHENGCXBH"),params.get("kaissj"),xiaox.toString(),"9");
			}
		}
		/*判断当零件生产比例误时，将错误信息写入系统，并抛出错误信息，终止计算*/
		if(flag){
			insertMessage();
			throw new PCRunTimeException(new Message("shengcblIsNull.error","i18n.pc.pc").getMessage());
		}
	}
	
	/**
	 * @description 得到一天中处于开启状态的产线
	 * @author 王国首
	 * @date 2011-12-23
	 * @param gongzrlChanxz   	计算日期
	 * @param shengxList		产线组下生产线列表
	 * @param workCalendar		一段时间内产线组内每天处于开启状态的产线
	 * @return  String			当天开启的产线编号	
	 */
	public String getDailyWorkChanx(String gongzrlChanxz,List<Map<String, String>> shengxList,Map<String, Map<String,String>> workCalendar){
		Map<String,String> temp = new HashMap<String,String>();
		StringBuffer lStrChanx = new StringBuffer();
		String flag = "";
		for(Map<String,String> shengx : shengxList ){
			String shengcxh = shengx.get("SHENGCXBH");
			if(workCalendar.containsKey(shengcxh)){
				temp = workCalendar.get(shengcxh);
				if(temp.containsKey(gongzrlChanxz)){
					lStrChanx.append(flag).append("'").append(shengcxh).append("'");
					flag = ",";
				}
			}
		}
		return lStrChanx.toString();
	}
	
	/**
	 * @description 计算排产日的库存系数，并对计算出的库存系数排序
	 * @author 王国首
	 * @date 2012-1-9
	 * @param params	 包含产线和排产时间范围
	 * @return gongShi	产线组下每条产线考虑最小时间单位后的工时
	 */
	public List<Map<String,String>> calkuCunXiShu(List<Map<String,String>> qckc,List<Map<String, String>> chanxlj,String shengcxbh){
		List<Map<String,String>> kuCunXiShu = new ArrayList<Map<String,String>>();
		for( int i = 0;i<chanxlj.size();i++){
			Map<String,String> chanxljMap = chanxlj.get(i);
			double kucxs = 0;
			for( Map<String,String> qckcMap : qckc){
//				if(chanxljMap.get("LINGJBH").equals(qckcMap.get("LINGJBH")) && Double.parseDouble(String.valueOf(chanxljMap.get("MAOXQ")))>0){
				if(chanxljMap.get("LINGJBH").equals(qckcMap.get("LINGJBH"))){
					if(shengcxbh != null && shengcxbh.length()>0){
						boolean flag = false;
						for( Map<String,String> shengcxList : getLingjOfLine().get(chanxljMap.get("LINGJBH"))){
							if(shengcxbh.equals(shengcxList.get("SHENGCXBH"))){
								flag = true;
								break;
							}
						}
						if(!flag){
							break;
						}
					}
					double maoxq = Double.parseDouble(String.valueOf(chanxljMap.get("MAOXQ")));
					if(maoxq <= 0){
						kucxs = 1000000.00;
					}else{
						kucxs = Double.parseDouble(String.valueOf(qckcMap.get("QCKC")))/maoxq;
					}
					kucxs = Math.round(kucxs*100)/100.0;
					Map<String,String> kucxsMap = new HashMap<String,String>();
					kucxsMap.put("LINGJBH", chanxljMap.get("LINGJBH"));
					kucxsMap.put("KUCXS", String.valueOf(kucxs));
					kucxsMap.put("SHENGCJP", String.valueOf(chanxljMap.get("SHENGCJP")));
					kucxsMap.put("ANQKCTOP", qckcMap.get("ANQKCTOP"));
					kucxsMap.put("JINGJPL", String.valueOf(chanxljMap.get("JINGJPL")));
					kucxsMap.put("QCKC", qckcMap.get("QCKC"));
					for(int j = 0;j<kuCunXiShu.size();){
						Map<String,String> temp = kuCunXiShu.get(j);
						if(Double.parseDouble(temp.get("KUCXS"))>=kucxs){
							kuCunXiShu.add(j, kucxsMap);
							break;
						}
						j++;
						if(j == kuCunXiShu.size()){
							kuCunXiShu.add(kucxsMap);
							break;
						}
					}
					if(kuCunXiShu.size()==0){kuCunXiShu.add(kucxsMap);} 
				}
			}
		}
		return kuCunXiShu;
	}

	/**
	 * @description 计算排产日的库存系数，并对计算出的库存系数排序
	 * @author 王国首
	 * @date 2012-1-9
	 * @param params	 包含产线和排产时间范围
	 * @return gongShi	产线组下每条产线考虑最小时间单位后的工时
	 */
	public List<Map<String,String>> calkuCunXiShuRi(List<Map<String,String>> qckc,List<Map<String, String>> chanxlj,String shengcxbh){
		List<Map<String,String>> kuCunXiShu = new ArrayList<Map<String,String>>();
		Map<String, Map<String,String>>rljpz = this.getRiLingjpz();
		Map<String, Map<String,String>>scxrljpz = this.getRiScxLingjpz().get(shengcxbh);
		final Iterator<String> it = scxrljpz.keySet().iterator(); 
		while(it.hasNext()){
			Map<String,String> pclj = scxrljpz.get(it.next());
			double kucxs = 0;
			String pclingjbh = pclj.get("LINGJBH");
			if(rljpz.get(pclingjbh) != null && scxrljpz.get(pclingjbh)!= null){
				Map<String,String> qckcMapPc = new HashMap<String,String>();
				for( Map<String,String> qckcMap : qckc){
					if(qckcMap.get("LINGJBH").equals(pclingjbh)){
						qckcMapPc = qckcMap;
						break;
					}
				}
				if(qckcMapPc.get("LINGJBH")!=null && rljpz.get(pclingjbh)!=null){
					Map<String,String> rljpzMap= rljpz.get(pclingjbh);
					double maoxq = Double.parseDouble(String.valueOf(rljpzMap.get("MAOXQ")));
					if(maoxq <= 0){
						kucxs = 1000000.00;
					}else{
						kucxs = Double.parseDouble(String.valueOf(qckcMapPc.get("QCKC")))/maoxq;
					}
					kucxs = Math.round(kucxs*100)/100.0;
					Map<String,String> kucxsMap = new HashMap<String,String>();
					kucxsMap.put("LINGJBH", pclingjbh);
					kucxsMap.put("KUCXS", String.valueOf(kucxs));
					kucxsMap.put("SHENGCJP", String.valueOf(pclj.get("SHENGCJP")));
					kucxsMap.put("ANQKCTOP", qckcMapPc.get("ANQKCTOP"));
					kucxsMap.put("JINGJPL", String.valueOf(pclj.get("JINGJPL")));
					kucxsMap.put("QCKC", qckcMapPc.get("QCKC"));
					for(int j = 0;j<kuCunXiShu.size();){
						Map<String,String> temp = kuCunXiShu.get(j);
						if(Double.parseDouble(temp.get("KUCXS"))>=kucxs){
							kuCunXiShu.add(j, kucxsMap);
							break;
						}
						j++;
						if(j == kuCunXiShu.size()){
							kuCunXiShu.add(kucxsMap);
							break;
						}
					}
					if(kuCunXiShu.size()==0){kuCunXiShu.add(kucxsMap);} 
					
				}
			}
		}
		return kuCunXiShu;
	}
	
	/**
	 * @description 检测当实际增产时间小于计划增产时间时间，将信息写入提示信息
	 * @author 王国首
	 * @date 2012-2-12
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkZengcNotEnough (Map<String,String> params,String shengcx,String usercenter,String shij,double zengcsj,double shiji,String leix){
		 if(shiji<zengcsj){ 
			 String xiaox = "产线"+shengcx+", 在"+shij+"计划增产"+zengcsj+"小时,实际增产"+shiji+"，请计划员调整生产"; 
			 this.addMessage(params,yueMnjhh.get(shengcx),usercenter,shengcx,shij,xiaox,leix); 
		} 
	}
	
	/**
	 * @description 计算考虑最小时间单位后的工时
	 * @author 王国首
	 * @date 2011-12-30
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return gongShi	考虑最小时间单位后的工时
	 */
	public double getMinTime(double mintime,double gongShi){
		double result = 0;
//		result = gongShi%mintime == 0.0 ? gongShi : Math.ceil(gongShi/mintime)*mintime;
		if(gongShi%mintime == 0.0){
			result = gongShi;
		}else{
			result = BigDecimal.valueOf(Math.ceil(gongShi/mintime)).multiply(BigDecimal.valueOf(mintime)).doubleValue();
			
		}
		return result;
	}
	
	/**
	 * @description 得到当前工业周期的开始时间
	 * @author 王国首
	 * @date 2012-3-29
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public void setGongyzqKaissj(Map<String, String> params) {
		Map<String, String> gongyzq = (Map<String, String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("monpc.queryGyzq", params);
		if(gongyzq.get("KAISSJ")!= null){
			params.put("upkaissj", gongyzq.get("KAISSJ"));
		}
	} 
	
	/**
	 * @description	输出到NUP接口
	 * @author 王国首
	 * @date 2012-3-31
	 * @param params			排产初始参数
	 */ 
	@SuppressWarnings("unchecked")
	public void calOutPut(final Map<String,String> params) {
		String chanxzbh = "";
		String [] cxzbh = params.get("chanxzbh").split(",");
		String flag = "";
		for( String temp : cxzbh){
			chanxzbh = chanxzbh + flag + "'" + temp + "'";
			flag = ",";
		}
		params.put("chanxzbh", chanxzbh);
		params.put("shengcx", getYueMNDingdh(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryShengcxh",params),"SHENGCXBH",true));
		//deleteZCPC(params); //xss-0012285 转用接口进行清理
		parseOutPutNup(params);//将模拟日零件产量，订单预告，外部订单预告插入NUP输出接口表
	}
	
	/**
	 * @description 将集合中的号码组合成数据库接受的查询条件
	 * @author 王国首
	 * @date 2012-1-16
	 * @param dingdh 集合
	 * @param name   名字
	 */
	public String getYueMNDingdh(List<Map<String, String>> dingdh,String name,boolean biaos){
		String result = "";
		String flag = "";
		String bs = biaos ? "'" : "";
		for( Map<String, String> dingdhMap : dingdh){
			result = result + flag +bs+ dingdhMap.get(name)+bs;
			flag = ",";
		}
		return result;
	}
	
	/**
	 * @description 将List<Map<String, String>>结构的数据中所有数据拼装成字符串输出到logs中
	 * @author 王国首
	 * @date 2012-4-26
	 * @param source 输入元素
	 * @return String	返回一个字符串
	 */
	public String getListMapKeyValue(List<Map<String, String>> source){
		final StringBuffer lStrQicku = new StringBuffer();
		String stemp = "";
		if(source != null && source.size()>0){
			for(Map<String,String> map : source){
				for(Map.Entry<String, String> m: map.entrySet()){
					stemp = String.valueOf(m.getValue());
					lStrQicku.append(m.getKey()+"="+stemp+",");
				}
			}
		}
		return lStrQicku.toString();
	}
	
	/**
	 * @description 将入库明细更新到排产明细表中
	 * @author 王国首
	 * @date 2012-5-14
	 * @param params 输入元素
	 * @return String	返回一个字符串
	 */
	public void calDailyProduce(Map<String, String> params){
		final List<Map<String, String>> shengxList = getChangxList(params);
		final StringBuffer lStrChanx = new StringBuffer();
		String flag = "";
		for (Map<String, String> changx : shengxList) {
			lStrChanx.append(flag).append('\'').append(changx.get("SHENGCXBH").toString()).append('\'');
			flag = ",";
		}
		params.put("shengcx", lStrChanx.toString());
		List<Map<String, String>> gongyzq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryDayInGongyzq",params);
		if(gongyzq.size()>0){
			params.put("gongyzqkaissj", gongyzq.get(0).get("KAISSJ"));
			params.put("gongyzqjiessj", gongyzq.get(0).get("JIESSJ"));
		}
		List<Map<String, String>> rukKaissj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRukKaissj",params);
		final Map<String,String> kaissjMap = new HashMap<String,String>();
		for(Map<String,String> temp : rukKaissj){
			String kaissj = params.get("kaissj");
			if(temp.get("TIAOZSJ") != null && "1".equals(temp.get("TIAOZSJ"))){
				kaissj = params.get("today");
			}
			kaissjMap.put( temp.get("SHENGCXBH"), kaissj + " " + temp.get("KAISSJ"));
		}
		Map<String, String> lingjJiep = convertListToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryLingjJiep",params),"IDH","SHENGCJP");
		for(Map<String, String> shengxMap : shengxList){
			String chanxh = shengxMap.get("SHENGCXBH").toString();
			params.put("chanxh", chanxh);
			if(kaissjMap.get(chanxh) != null && kaissjMap.get(chanxh).length() == 19){
				params.put("kaissjhms", kaissjMap.get(chanxh));
			}else{
				String kaissjhms = kaissjMap.get(chanxh) != null?kaissjMap.get(chanxh) : "null";
				String msg = chanxh + "," + params.get("kaissj")+"日,入库开始时间错误，入库开始时间为:"+kaissjhms+"请计划员调整参考系工作时间表";
				logger.info("排产入库明细开始时间错误：" + msg);
				BackgroundRunLog.addError("系统调度","UW",CommonUtil.MODULE_PC, "排产入库明细", "排产入库明细开始时间错误", CommonUtil.getClassMethod(), msg);
				continue;
			}
			parseRukmx(params,lingjJiep);
		}
	}
	
	/**
	 * @description 
	 * @author 王国首
	 * @date 2012-4-26
	 * @param params 输入元素
	 * @return void	
	 */
	@SuppressWarnings("unchecked")
	public void parseRukmx(Map<String, String> params,Map<String, String> lingjJiep){
		String timeNow = getTimeNow(TIMEFORMAT);
		params.put("EDIT_TIME", timeNow);
		List<Map<String, String>> rukmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryProduceRukmx",params);
		String shij = "";
		try{
			shij = DateUtil.StringFormatToString(params.get("kaissj"));
		}catch(ParseException e){
			logger.error(e.getMessage());
		}
		String gongzbh = params.get("USERCENTER") + params.get("chanxh") + shij;
		//工作编号 = 用户中心+产线编号+当天时间-1
		for(Map<String, String> rukmxMap : rukmx){
			String lingjIdh  = rukmxMap.get("USERCENTER")+ rukmxMap.get("SHENGCXBH") +rukmxMap.get("LINGJBH");
			rukmxMap.put("GONGZBH", gongzbh);
			rukmxMap.put("EDIT_TIME", timeNow);
			rukmxMap.put("CREATE_TIME", timeNow);
			rukmxMap.put("EDITOR", params.get("jihybh"));
			rukmxMap.put("CREATOR", params.get("jihybh"));
			rukmxMap.put("CHANXZBH", params.get("chanxzbh"));
			rukmxMap.put("ZHUANGT", "1");
			
			//xss-2015-12-03
			rukmxMap.put("ID", UUIDHexGenerator.getInstance().generate());
			logger.info("排产入库明细节拍 ID：" + rukmxMap.get("ID"));
			
			
			if(lingjJiep.get(lingjIdh) != null && Double.parseDouble(lingjJiep.get(lingjIdh))>0){
				double gongs = getMinTime(0.01,Double.parseDouble(String.valueOf(rukmxMap.get("LINGJSL")))/Double.parseDouble(lingjJiep.get(lingjIdh)));
				rukmxMap.put("shijgongs", String.valueOf(gongs));
			}else{
				String jiep = lingjJiep.get(lingjIdh) != null?lingjJiep.get(lingjIdh) : "null";
				String msg = params.get("chanxh") + "," + params.get("kaissj")+"日,"+rukmxMap.get("LINGJBH")+",生产节拍有误，无法计算此零件的工时,节拍为:"+jiep;
				logger.info("排产入库明细节拍错误：" + msg);
				BackgroundRunLog.addError("系统调度","UW",CommonUtil.MODULE_PC, "排产入库明细", "排产入库明细节拍错误", CommonUtil.getClassMethod(), msg);
			}
			updateYuedmnjhmx(rukmxMap);
			updateRigdpcjhmx(rukmxMap);
		}
		if(params.get("gongyzqkaissj") != null){
			params.put("GONGZBH", gongzbh);
			updateYuedmnjhmxLeijce(params);
		}
		
	}
	
	/**
	 * @description 模拟日零件产量表
	 * @author 王国首
	 * @date 2012-4-26
	 * @param params 输入元素
	 * @return void	
	 */
	@SuppressWarnings("unchecked")
	public void updateYuedmnjhmx(Map<String, String> rukmxMap){
		int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMonrgdljclbProduce",rukmxMap);
		if(num == 0){
			List<Map<String, String>> yuemnjhmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYuedmnjhmxProduce",rukmxMap);
			if(yuemnjhmx.size()==1){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMonrgdljclbProduce",rukmxMap);
			}
		}
	}
	
	/**
	 * @description 日滚动排产计划明细
	 * @author 王国首
	 * @date 2012-4-26
	 * @param params 输入元素
	 * @return void	
	 */
	@SuppressWarnings("unchecked")
	public void updateRigdpcjhmx(Map<String, String> rukmxMap){
		int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateRigdpcjhmxProduce",rukmxMap);
		if(num == 0){
			List<Map<String, String>> yuemnjhmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRigdpcjhmxProduce",rukmxMap);
			if(yuemnjhmx.size()==1){
				rukmxMap.put("RIGDJHH", yuemnjhmx.get(0).get("RIGDJHH"));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertRigdpcjhmxProduce",rukmxMap);
			}
		}
	}
	
	/**
	 * @description 计算每零件每个零件的累计差额
	 * @author 王国首
	 * @date 2012-4-26
	 * @param params 输入元素
	 * @return void	
	 */
	@SuppressWarnings("unchecked")
	public void updateYuedmnjhmxLeijce(Map<String, String> params){
		List<Map<String, String>> leijce = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryMonrgdljclbLeijce",params);
		for( Map<String, String> leijceMap : leijce){
			leijceMap.put("EDIT_TIME", params.get("EDIT_TIME"));
			//xss-2015-12-03
			//rukmxMap.put("ID", UUIDHexGenerator.getInstance().generate());
			//leijceMap.put("ID", getUUID()); //xss-2015-12-3
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMonrgdljclbProduce",leijceMap);
		}
	}
	
	/**
	 * @description 计算每零件每个零件的累计差额
	 * @author 王国首
	 * @date 2012-4-26
	 * @param params 输入元素
	 * @return void	
	 */
	@SuppressWarnings("unchecked")
	public void createBeicxh(final List<Map<String, String>> shengxList,Map<String, String> params,
			List<Map<String,String>> req,Map<String,List<Map<String, String>>>DailyXQ){
		if (DailyXQ.size() <= 0){return;} 
		List<Map<String, String>> temp;
		Map<String, String> beicjmxh = new HashMap<String, String>();
		Map<String, String> bckaissj = new HashMap<String, String>();
		for(Map<String, String> shengxListMap : shengxList){
			temp = DailyXQ.get(shengxListMap.get("SHENGCXBH"));
			if(temp != null){
				for(Map<String, String> tempMap : temp){
//					String id = tempMap.get("USERCENTER")+tempMap.get("LINGJBH");
					String id = tempMap.get("ID");
					tempMap.put("kaissj", params.get("kaissj"));
					if(beicjmxh.get(id)== null){
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteBEICRXH",tempMap);
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteBEICRZHHZ",tempMap);
						beicjmxh.put(id,id);
						bckaissj.put("BC"+id,tempMap.get("BCKAISSJ"));
					}
					if(Double.parseDouble(String.valueOf(tempMap.get("ljsl")))>0){
						insertLingjrxqb(tempMap);	
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertBEICRXH",tempMap);
					}
				}
			}
		}
		final Iterator<String> it = beicjmxh.keySet().iterator(); 
		Map<String, String> beichz = new HashMap<String, String>();
		while(it.hasNext()){
			beichz.put("id", beicjmxh.get(it.next()));
			beichz.put("BCKAISSJ", bckaissj.get("BC"+beichz.get("id")));
			beichz.put("biaos", params.get("biaos"));
			beichz.put("today", params.get("today"));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertBEICRZHHZ",beichz);
			calLeijBeic(params,beichz);
		}
	}
	
	/**
	 * @description 期初库存减去备储计划
	 * @author 王国首
	 * @date 2012-6-20
	 * @param qckc 		期初库存
	 * @param params	 包含产线和排产时间范围
	 */
	public void calBeicjh(Map<String, String> params,List<Map<String,String>> qckc){
		Map<String, String> paramsTemp = new HashMap<String, String>();
		paramsTemp.putAll(params);
		paramsTemp.put("biaos", "R");
		List<Map<String, String>> beicjhmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectBeicjhmxLast",paramsTemp);
		for(Map<String, String> Temp : beicjhmx){
			for(int i = 0;i<qckc.size();i++){
				Map<String, String> qckcTemp = qckc.get(i);
				if(qckcTemp.get("LINGJBH").equals(Temp.get("LINGJBH"))){
					double kuc = Double.parseDouble(String.valueOf(qckcTemp.get("QCKC")))-Double.parseDouble(String.valueOf(Temp.get("LINGJSL")));
					qckcTemp.put("QCKC", String.valueOf(kuc));
					checkQckc(params,qckcTemp.get("LINGJBH"),kuc,Temp.get("SHENGCXBH"),params.get("USERCENTER"),params.get("kaissj"),"2");
					qckc.set(i, qckcTemp);
					break;
				}
			}
		}
	}
	
	/**
	 * @description 计算零件期初库存
	 * @author 王国首
	 * @date 2012-06-20
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return gongShi	考虑最小时间单位后的工时
	 */
	public void calQckc(List<Map<String,String>> tempQckc,List<Map<String, String>> lingj){
		Map<String,String> qckcMap = new HashMap<String,String>();
		String lastLingjbh = "";
		for( Map<String, String> lingjMap : lingj){
			String lingbh = (lingjMap.get("LINGJBH") != null && lingjMap.get("LINGJBH").length()>0) 
							? lingjMap.get("LINGJBH") : lingjMap.get("LINGJBHMAOXQ");
			for(int i = 0;i<tempQckc.size();i++){
				qckcMap = tempQckc.get(i);
				if(lingbh.equals(qckcMap.get("LINGJBH"))){
					double maoxq = lingjMap.get("MAOXQ")==null?0 : Double.parseDouble(String.valueOf(lingjMap.get("MAOXQ")));
					double lingjsl = lingjMap.get("LINGJSL")==null?0 : Double.parseDouble(String.valueOf(lingjMap.get("LINGJSL")));
					//判断当毛需求减去一次以后，第二次相同零件不再减去毛需求，防止重复减毛需求
					if(lingbh.equals(lastLingjbh)){
						maoxq = 0;
					}
					double lingjqckc = Double.parseDouble(String.valueOf(qckcMap.get("QCKC")))+lingjsl-maoxq;
//					/*检查期初库存是否小于0，当期初库存小于0时，将此消息写入报警消息表，提示给计划员*/
//					checkQckc(params,lingjMap.get("LINGJBH"),lingjqckc,params.get("shengcxCX"),params.get("USERCENTER"),params.get("kaissjWeeK"),"2");
					qckcMap.put("QCKC", String.valueOf(lingjqckc));
					tempQckc.set(i, qckcMap);
					lastLingjbh = lingbh;
					break;
				}
			}
		}
	}
	
	/**
	 * @description 得到下一个工作日的日期
	 * @author 王国首
	 * @date 2012-06-20
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return gongShi	考虑最小时间单位后的工时
	 */
	public String getNextWorkDay(Map<String,String> params,String shengcxbh,String riq){
		String result = "";
		List<String> workCalCXZ = getWorkCalendar().get(shengcxbh);
		int fromIndex = findWorkCalIndex(workCalCXZ, riq);
		if(workCalCXZ.size()>=fromIndex+2){
			result = workCalCXZ.get(fromIndex+1);
		}
		return result;
	}
	
	/**
	 * @description 四舍五入
	 * @author 王国首
	 * @date 2011-12-30
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return Scale	保留小数位数
	 */
	public double getRound(double gongShi,int Scale){
		BigDecimal bd = new BigDecimal(gongShi);  
		return bd.setScale(Scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * @description 将查询出来需要拆分的零件备储数量减去已经排产完成的备储数量，然后进行备储拆分
	 * @author 王国首
	 * @date 2012-1-11
	 * @param params        包含产线和排产时间范围
	 * @param requirements  零件需求列表 
	 */
	public void parseBeicxh(final Map<String, String> params, final List<Map<String,String>> requirements,
			List<Map<String, String>> shengxList) { 
		if (requirements.size() <= 0){return;} 
		for (Map<String,String> req : requirements) {
			req.put("pckaissj", params.get("kaissj"));
			req.put("biaos", params.get("biaos"));
			List<Map<String, String>> beicjhmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectBeicjhmxOver",req);
			if(beicjhmx.size()>0){
				int lingjOrvr = Integer.parseInt(String.valueOf(req.get("LINGJSLCX"))) - Integer.parseInt(String.valueOf(beicjhmx.get(0).get("LINGJSL")));
				lingjOrvr = lingjOrvr>0?lingjOrvr:0;
				req.put("LINGJSLCX", String.valueOf(lingjOrvr));
				req.put("KAISSJ", params.get("kaissj"));
				req.put("BCKAISSJ", params.get("kaissj"));
			}
		}
	}
	
	/**
	 * @description 计算累计备储数量
	 * @author 王国首
	 * @date 2012-6-20
	 * @param qckc 		期初库存
	 * @param params	 包含产线和排产时间范围
	 */
	public void calLeijBeic(Map<String, String> params,Map<String,String> beichz){
		Map<String, String> paramsTemp = new HashMap<String, String>();
		paramsTemp.putAll(params);
		beichz.put("USERCENTER", paramsTemp.get("USERCENTER"));
		List<Map<String, String>> beicjhmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectBeicjhByJhh",beichz);
		if(beicjhmx.size()>0){
			double leijBeic = 0;
			for( Map<String, String> beicjhmxMap: beicjhmx){
				leijBeic = leijBeic + Double.parseDouble(String.valueOf(beicjhmxMap.get("LINGJSL")));
//				if(beicjhmxMap.get("SHIJ").compareTo(paramsTemp.get("today"))>=0){
					beicjhmxMap.put("LEIJBCSL", String.valueOf(leijBeic));
//					int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateBeicrzhhz",beicjhmxMap);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateBeicrzhhz",beicjhmxMap);
//				}
			}
		}
	}
	
	/**
	 * @description 将查询出来需要拆分的零件备储数量减去已经排产完成的备储数量，然后进行备储拆分
	 * @author 王国首
	 * @date 2012-1-11
	 * @param params        包含产线和排产时间范围
	 * @param requirements  零件需求列表 
	 */
	public void parseMaoxqxh(final Map<String, String> params, final List<Map<String,String>> requirements) { 
		if (requirements.size() <= 0){return;} 
		for (Map<String,String> req : requirements) {
			req.put("pckaissj", params.get("kaissj"));
			req.put("biaos", params.get("biaos"));
			List<Map<String, String>> beicjhmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectMaoxqxhOver",req);
			if(beicjhmx.size()>0){
				int lingjOrvr = Integer.parseInt(String.valueOf(req.get("LINGJSLCX"))) - Integer.parseInt(String.valueOf(beicjhmx.get(0).get("LINGJSL")));
				lingjOrvr = lingjOrvr>0?lingjOrvr:0;
				req.put("LINGJSLCX", String.valueOf(lingjOrvr));
				req.put("KAISSJ", params.get("kaissj"));
				if("BC".equals(req.get("PCFLAG"))){
					req.put("BCKAISSJ", params.get("kaissj"));
				}
			}
		}
	}
	
	/**
	 * @description	更新排产需求表时间
	 * @author 王国首
	 * @date 2012-8-21
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */ 
	@SuppressWarnings("unchecked")
	public void updatePcShij(final List<Map<String,String>> shengxList, final Map<String,String> params) {
		int num = 0;
		num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxShij",params);
		logger.info("更新排产需求表时间,更新订单明细记录:"+num);
		num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateGevpShij",params);
		logger.info("更新排产需求表时间,更新GEVP明细记录:"+num);
		num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateWbddygShij",params);
		logger.info("更新排产需求表时间,更新外部订单明细记录:"+num);
	}
	
	/**
	 * @description	批量更新需要排产的订单标识
	 * @author 王国首
	 * @date 2012-8-21
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */ 
	@SuppressWarnings("unchecked")
	public void updateDingd(final Map<String,String> params) {
		if(params.get("Dingdlx") != null && ("PP".equals(params.get("Dingdlx")) || "PJ".equals(params.get("Dingdlx")))){
			params.put("DingdlxN", "PP".equals(params.get("Dingdlx")) ? "NP":"NJ");
			this.updateDingdMx(params);
		}else{
			params.put("Dingdlx", "PJ");
			params.put("DingdlxN", "NJ");
			for(int i=0;i<2;i++){
				this.updateDingdMx(params);
				params.put("Dingdlx", "PP");
				params.put("DingdlxN", "NP");
			}
		}
	}
	
	/**
	 * @description	批量更新需要排产的订单标识
	 * @author 王国首
	 * @date 2012-10-20
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */ 
	@SuppressWarnings("unchecked")
	public void updateDingdMx(final Map<String,String> para) {
		Map<String, String> params = new HashMap<String, String>();
		params.putAll(para);
		params.put("ZHUANGT", "4");
		if("PP".equals(params.get("Dingdlx"))){
			List<Map<String, String>> dingDTwoList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectDingdTwo",params);
			for(int i = 0;i<dingDTwoList.size();i++){
				List<String> dingdSize = new ArrayList<String>();
				Map<String,String>  dingdMap = dingDTwoList.get(i);
				params.put("DDUSERCENTER", dingdMap.get("USERCENTER"));
				String cn = String.valueOf(dingdMap.get("CN"));
				//得到到排序的订单号
				if("1".equals(cn)){
					dingdSize.add(dingdMap.get("DINGDH"));
					if((i+1)<dingDTwoList.size() && "2".equals(String.valueOf(dingDTwoList.get(i+1).get("CN")))){
						dingdSize.add(dingDTwoList.get(i+1).get("DINGDH"));
						if((i+2)<dingDTwoList.size() && "3".equals(String.valueOf(dingDTwoList.get(i+2).get("CN")))){
							dingdSize.add(dingDTwoList.get(i+2).get("DINGDH"));
						}
					}
				}
				//更新pj订单的订单明细
				if(dingdSize.size()>0 && "PJ".equals(params.get("Dingdlx"))){
					params.put("DINGDHONE", dingdMap.get("DINGDH"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagOne",params);
					if(dingdSize.size()>=2){
						for(int j = 0;j<dingdSize.size()-1;j++){
							params.put("DINGDHONE", dingdSize.get(j));
							params.put("DINGDHTWO", dingdSize.get(j+1));
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagMin",params);
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagMax",params);
						}
					}
				}
				//更新PP订单的订单明细
				if(dingdSize.size()>0 && "PP".equals(params.get("Dingdlx"))){
					params.put("DINGDHONE", dingdMap.get("DINGDH"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagOne",params);
					String dingdmxshij = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("monpc.selectDingdmxShij",params);
					params.put("kaissj", dingdmxshij);
					List<Map<String, String>> gongyzqShij = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryDayInGongyzq",params);
					if(dingdSize.size()>=2 && gongyzqShij.size()>0){
						params.put("DINGDHTWO", dingdSize.get(1));
						params.put("MIN", gongyzqShij.get(0).get("KAISSJ"));
						params.put("PCFLAG", "0");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagPP",params);
						params.put("MAX", gongyzqShij.get(0).get("KAISSJ"));
						params.remove("MIN");
						params.put("PCFLAG", "1");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagPP",params);
						params.remove("MAX");
					}
				}
			}
		}else{
			List<Map<String, String>> dingDTwoList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectDingdTwoPj",params);
			for(int i = 0;i<dingDTwoList.size();i++){
				List<String> dingdSize = new ArrayList<String>();
				Map<String,String>  dingdMap = dingDTwoList.get(i);
				params.put("DDUSERCENTER", dingdMap.get("USERCENTER"));
				params.put("DDGONGYSDM", dingdMap.get("GONGYSDM"));
				params.put("DDDINGDH", dingdMap.get("DINGDH"));
				String cn = String.valueOf(dingdMap.get("CN"));
				//得到到排序的订单号
				params.put("ppcn", "1");
				List<Map<String, String>> dingDList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectDingdList",params);
				//更新pj订单的订单明细
				Map<String, String> dingDListMap = null;
				for(int d = 0;d<dingDList.size();d++){
					dingDListMap = dingDList.get(d);
					params.put("DINGDHONE", dingDListMap.get("DINGDH"));
					if(d==0){
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagOne",params);
					}
					if( dingDList.size()>d+1){
						Map<String, String> dingDListNextMap = dingDList.get(d+1);
						params.put("DINGDHONE", dingDListMap.get("DINGDH"));
						params.put("DINGDHTWO", dingDListNextMap.get("DINGDH"));
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagMin",params);
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagMax",params);
					}
				}
				if(dingDList.size()>1){
					params.put("DDUSERCENTER", dingDListMap.get("USERCENTER"));
					params.put("DDGONGYSDM", dingDListMap.get("GONGYSDM"));
					params.put("DDDINGDH", dingDListMap.get("DINGDH"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxPjFlagZ",params);
				}
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxFlagTemp",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateDingdmxcangkTemp",params);
	}
	
	/**
	 * @description 保存
	 * @author 王国首
	 * @date 2012-1-11
	 * @param params        包含产线和排产时间范围
	 * @param requirements  零件需求列表 
	 */
	public Map<String, String> saveMaxDingdh(final List<Map<String,String>> requirements) { 
		Map<String, String> dingdMap = new HashMap<String, String>();
		for (Map<String,String> req : requirements) {
			dingdMap.put(req.get("DINGDH"),req.get("USERCENTER")+req.get("GONGYSDM"));
		}
		return dingdMap;
	}
	
	public BigDecimal getBigDecimal(Object obj) {
		if (obj != null) {
			return new BigDecimal(this.strNull(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	public String strNull(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
}
