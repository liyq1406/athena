package com.athena.pc.module.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.athena.db.ConstantDbCode;
import com.athena.pc.common.CollectionUtil;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;


/**
 * <p>
 * Title:日滚动排产
 * </p>
 * <p>
 * Description:系统每日定时日滚动排产
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
 * @date 2012-03-06
 **/
@Component
public class DailyRollService extends AbstractMonpcTemplate {

	@Inject
	private EquilibriaSCXService equilibriaSCXService; 


	@Override
	public void parseMaoxq(List<Map<String, String>> shengxList,
			Map<String, String> params) {
	}

	@Override
	public void parseMaoxqWbddyg(List<Map<String, String>> shengxList,
			Map<String, String> params) {
	}
	
	@Override
	public void parseRiMargin(List<Map<String, String>> shengxList,Map<String, String> params) {
		int fengBQ =Integer.parseInt(params.get("FENGBQ"));
		fengBQ = fengBQ>0?fengBQ-1:fengBQ;
		int tiqq =  Integer.parseInt(params.get("TIQQ"));
		int leijshij = fengBQ + tiqq;
		String leijriq = params.get("kaissj");
		if(leijshij>0){
			List<String> rili = getWorkSubCal(getWorkCalendar().get(params.get("chanxzbh")),DateUtil.dateAddDays(params.get("kaissj"), 1),params.get("nextjiessj"));
			if(rili.size()>=leijshij){
				leijriq = rili.get(leijshij-1);
			}else{
				return;
			}
		}
		List<Map<String, String>> leijjfce = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryLeijjfce",params);
		for( Map<String, String> leijjfceMap: leijjfce){
			leijjfceMap.put("date", leijriq);
			leijjfceMap.put("ljsl", String.valueOf(leijjfceMap.get("LINGJSL")));
			leijjfceMap.put("biaos", params.get("biaos"));
			insertLingjrxqb(leijjfceMap);
		}
	}
	
	/**
	 * @description 模拟排产前数据初始化，删除中间表数据和上次的排产计划
	 * @author 王国首
	 * @date 2012-2-24
	 * @param params	 包含产线和排产时间范围
	 */
	@Override
	public void cleanInitData(Map<String, String> params) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteLingjrxqb",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteLingjrxqhzb",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteMessage",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteBANCMX",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteRIGDPCJHMX",params);
		cleanRigdjh(params);
	} 

	/**
	 * @description 模拟排产前数据初始化，删除日滚动排产计划明细
	 * @author 王国首
	 * @date 2012-2-24
	 * @param params	 包含产线和排产时间范围
	 */
	public void cleanRigdjh(Map<String, String> params) {
		List<Map<String, String>> rigdjh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRigdjh",params);
		if(rigdjh != null && rigdjh.size()>0){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteBANCMXFB",params);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteRIGDPCJHMXFB",params);
		}
	} 
	
	/**
	 * @description 将零件每日需求汇总插入到零件日需求汇总表
	 * @author 王国首
	 * @date 2012-2-25
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@Override
	public void statDailyLingj(List<Map<String, String>> shengxList,
			Map<String, String> params) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertLingjrxqhzb",params);
	}

	/**
	 * @description 日滚动排产
	 * @author 王国首
	 * @date 2012-2-25
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param qckc	 	 期初库存
	 */
	@Override
	public void calYueMoN(List<Map<String, String>> shengxList,Map<String, String> params, List<Map<String, String>> qckc) {
		List<Map<String,String>> qckcJXQ = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = new ArrayList<Map<String,String>>();
		/*得到零件的安全库存数量或者安全库存天数*/
		Map<String, String> anqkcMap = calAnqkc(params);
		/*得到零件计算开始时间的期初库存*/
		tempQckc = qiChuKuCun(params,qckc);
		/*期初库存减去备储计划*/
		calBeicjh(params,tempQckc);
		/*将没有拆分完的需求使用期初库存减去*/
		calQckcMinusXQ(params,tempQckc);
		/*将计算出的零件的安全库存设置到期初库存列表中*/
		tempQckc = setAnqkcToQckc(params,tempQckc,anqkcMap);
		List<Map<String, String>> chanxzrl = getChanxzWorkCalendar(params);
		/*计算排产时间范围内，每个工作日加上提前期后，对应的加上提前期的工作日历*/
		setAheadPeriod(calAddTiqqRil(params,chanxzrl));
		setBlockWorkCalendar(parseBlockWorkCalendar(params,shengxList,chanxzrl));
		/*得到上一天封闭了的工作日*/
		Map<String,String> lastBlockWorkCalendar = parseLastBlockWorkCalendar(params);
		Map<String, String> paramsTemp = new HashMap<String, String>();
		paramsTemp.putAll(params);
		/*插入日滚动计划表*/
		insertRigdjh(shengxList,params);
		for( Map<String, String> rl : chanxzrl){
			String riq = rl.get("RIQ");
			paramsTemp.put("kaissj", riq);
			paramsTemp.put("jiessj", riq);
			paramsTemp.put("kaissjPC", riq);
			qckcJXQ = listMapCopy(tempQckc);
			calDailyParam(shengxList, paramsTemp);
			/*计算每日净需求*/
			logger.info("模拟排产计算每日净需求");
			calDailyJXQ(shengxList, paramsTemp,qckcJXQ);
			if(lastBlockWorkCalendar.get(riq) != null && riq.equals(lastBlockWorkCalendar.get(riq))){
				/*如果当天属于封闭期，计算下一天的期初库存P1，下一天的期初库存P1 = 当天的期初库存P0 + 封闭期当天的计划排产量 - 封闭期当天的需求*/
				logger.info("模拟排产计算封闭期每日净需求");
				calBlockDailyXQ(shengxList, paramsTemp,tempQckc);
				/*如果当天属于封闭期，计算出下一天的期初库存后，跳出当前循环，进行下一天的排产*/
				updateMeiRiMaoxq(paramsTemp);
				continue;
			}	
			/*计算产线每日净需求*/
			logger.info("模拟排产计算产线净需求");
			calDailyCXJXQ(shengxList, paramsTemp,tempQckc);
			/*产线均衡*/
			Map<String, Object> paramsJH= new HashMap<String, Object>();
			params.put("kaissjWeeK", riq);
			params.put("jiessjWeeK", riq);
			paramsJH.put("params",params);
			paramsJH.put("shengx",shengxList);
			paramsJH.put("qckc",tempQckc);
			paramsJH.put("workCalendar",workCalendar);
			equilibriaSCXService.equilibData(paramsJH);
			/*进行每日排产*/
			logger.info("模拟排产计算每日排产");
			calDailyProduce(shengxList,paramsTemp,tempQckc);
			/*将每日排产结果分配到当天生产的班*/
			logger.info("模拟排产安排生产班次");
			calBancProduce(shengxList,paramsTemp);
		}
	}

	/**
	 * @description 计算每个零件的期初库存
	 * @author 王国首
	 * @date 2011-12-20
	 * @param params	 包含产线和排产时间范围
	 * @param qckc	 	 期初库存
	 * @return List<Map<String, String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> qiChuKuCun(Map<String, String> params,List<Map<String,String>> qckc){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryFirstDayBeginKucRi",params);
	}
	
	/**
	 * @description 计算每日净需求
	 * @author 王国首
	 * @date 2011-12-17
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param anqkcMap	 安全库存
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> calDailyJXQ(List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String,String>> qckc) {
		Map<String, String> paramsTemp = new HashMap<String, String>(); 
		List<Map<String,String>> tempQckc =  new ArrayList<Map<String,String>>();
		tempQckc.addAll(qckc);
		paramsTemp.putAll(params);
		List<Map<String, String>> gongzrlChanxzList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrlChanxz",paramsTemp);
		for(Map<String, String> gongzrlChanxz : gongzrlChanxzList){
			paramsTemp.put("JSSHIJ", gongzrlChanxz.get("RIQ").toString());
			paramsTemp.put("TQSHIJ", getAheadPeriod().get(gongzrlChanxz.get("RIQ")).toString());
			Map<String,List<Map<String, String>>> temp = calOneDayJXQ( gongzrlChanxz, tempQckc ,paramsTemp);
			List<Map<String, String>> lingjJinxuqiuUpdate = temp.get("Jinxuqiu");
			tempQckc = temp.get("beginKucList");
			//test
			String logString = this.getListMapKeyValue(lingjJinxuqiuUpdate);
			logger.info("净需求："+gongzrlChanxz.get("RIQ")+" "+ logString);	
			
			updateDailyJXQ(lingjJinxuqiuUpdate);
		}
		return tempQckc;
	}
	
	/**
	 * @description 日滚动模拟产线的封闭期
	 * @author 王国首
	 * @date 2012-2-5
	 * @param params  包含产线和排产时间范围
	 * @param shengxList 产线列表
	 * @param chanxzWC 	 各个产线的工作日历
	 * @return workCal	封闭期
	 */
	public Map<String, Map<String,String>> parseBlockWorkCalendar(final Map<String, String> params,List<Map<String, String>> shengxList,List<Map<String, String>> chanxzWC){
		final Map<String, Map<String,String>> workCal = new HashMap<String, Map<String,String>>(); 
		int fengBQ =Integer.parseInt(params.get("FENGBQ"));
		for( Map<String, String> shengcx : shengxList){
			List<String> workCalCXZ = getWorkCalendar().get(shengcx.get("SHENGCXBH"));
			Map<String, String> workCalMap = new HashMap<String, String>(); 
			if(workCalCXZ.size()>0 && fengBQ>0){
				fengBQ = workCalCXZ.size()<fengBQ ? workCalCXZ.size() : fengBQ;
				List<String> workCalCX = new ArrayList<String>();
				workCalCX.addAll(getWorkSubCal(workCalCXZ,chanxzWC.get(0).get("RIQ"),chanxzWC.get(fengBQ-1).get("RIQ")));
				for( String temp : workCalCX){
					workCalMap.put(temp, temp);
				}
			}
			workCal.put(shengcx.get("SHENGCXBH"), workCalMap);
		}
		return workCal;
	}
	
	/**
	 * @description 将每日净需求拆分到产线每日净需求，并比较经济批量
	 * @author 王国首
	 * @date 2011-12-22
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param qckc   	产线组下零件的期初库存
	 */
	@SuppressWarnings("unchecked")
	public void calDailyCXJXQ(List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String,String>> qckc) {
		Map<String, String> paramsTemp = new HashMap<String, String>(); 
		paramsTemp.putAll(params);
		List<Map<String, String>> gongzrlChanxzList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrlChanxz",paramsTemp);
		paramsTemp.put("MINSJ", params.get("kaissj"));
		paramsTemp.put("MAXSJ", params.get("jiessj"));
		List<Map<String, String>> chanxLingjHz = new ArrayList<Map<String, String>>();
		Map<String, Map<String,String>> workCalendar = getWorkCalendarMap(paramsTemp);
		String lStrChanx = "";
		String gongzrl = "";
		for(Map<String, String> gongzrlChanxz : gongzrlChanxzList){
			gongzrl = gongzrlChanxz.get("RIQ");
			lStrChanx = getDailyWorkChanx(gongzrl,shengxList,workCalendar);
			paramsTemp.put("shengcx", lStrChanx);
			paramsTemp.put("RIQ", gongzrl);
			chanxLingjHz = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryChanxLingjHzRi",paramsTemp);
			getDailyChanxJxq(params,chanxLingjHz,qckc);
		}
	}
	
	/**
	 * @description 计算这一天中每个零件的净需求，然后和经济批量比较，取较大值为当天的日净需求，计算完并插入数据库
	 * @author 王国首
	 * @date 2011-12-24
	 * @param params			 包含产线和排产时间范围
	 * @param chanxLingjHz   	一天中开启产线零件汇总数据（包括生成比例，是否取经济批量，经济批量）
	 * @param qckc   			产线组下零件的期初库存
	 * @return  null	
	 */
	public void getDailyChanxJxq(Map<String, String> params,List<Map<String, String>> chanxLingjHz,List<Map<String,String>> qckc){
		String timeNow = getTimeNow(TIMEFORMAT);
		for(int i = 0;i<chanxLingjHz.size();i++){
			Map<String, String> chanxLingj = chanxLingjHz.get(i);
			double jinxuqChanx =Double.valueOf(String.valueOf(chanxLingj.get("JINXQ")));
			double hour =Double.valueOf(String.valueOf(chanxLingj.get("SHENGCJP")));
			String shengcxbh = "";
			for( Map<String, String> shengcMap : getLingjOfLine().get(chanxLingj.get("LINGJBH"))){
				if(parseShengcxWork(params.get("kaissj"),shengcMap.get("SHENGCXBH"))){
					shengcxbh = shengcMap.get("SHENGCXBH");
					break;
				}
			}
			if("".equals(shengcxbh)){
				break;
			}
			chanxLingj.put("SHENGCXBH", shengcxbh);
			chanxLingj.put("HOUR", String.valueOf(this.getMinTime(0.01, jinxuqChanx/hour)));
			chanxLingj.put("chanxLingj", String.valueOf(jinxuqChanx));
			chanxLingj.put("RIGDJHH", yueMnjhh.get(shengcxbh));
			chanxLingj.put("CHANXZBH", params.get("chanxzbh"));
			chanxLingj.put("EDITOR", params.get("jihybh"));
			chanxLingj.put("EDIT_TIME", timeNow);
			chanxLingj.put("CREATOR", params.get("jihybh"));
			chanxLingj.put("CREATE_TIME", timeNow);
			chanxLingj.put("JINJPL", String.valueOf(chanxLingj.get("JINGJPL")));
			chanxLingj.put("MAOXQ", "0");
			if(getBlockWorkCalendar().get(chanxLingj.get("CHANXH")).get(params.get("kaissj")) != null){
				chanxLingj.put("ZHUANGT", "2");
			}else{
				chanxLingj.put("ZHUANGT", "3");
			}
			addParams(chanxLingj,qckc);
			chanxLingjHz.set(i, chanxLingj);
		}
		//test 
		String test = "";
		if(chanxLingjHz.size()>0 ){
			test = chanxLingjHz.get(0).get("SHENGCXBH")+" "+chanxLingjHz.get(0).get("SHIJ");
			String logString = this.getListMapKeyValue(chanxLingjHz);
			logger.info("产线净需求："+test + " "+ logString);
		}
		for( Map<String,String> temp: chanxLingjHz){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertRIGDPCJHMX",temp);
		}
	}
	
	/**
	 * @description 插入日滚动计划表
	 * @author 王国首
	 * @date 2012-03-08
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void insertRigdjh(List<Map<String, String>> shengxList,Map<String, String> params){
		String timeNow = getTimeNow(TIMEFORMAT);
		for( Map<String, String> shengxListMap : shengxList){
			Map<String,String> tempMap = new HashMap<String,String>();
			tempMap.put("RIGDJHH", yueMnjhh.get(shengxListMap.get("SHENGCXBH")));
			tempMap.put("CHANXH", shengxListMap.get("SHENGCXBH"));
			tempMap.put("KAISSJ", params.get("kaissj"));
			tempMap.put("JIESSJ", params.get("jiessj"));
			tempMap.put("USERCENTER", params.get("USERCENTER"));
			tempMap.put("SHIFQR", "N");
			tempMap.put("CHANXZBH", params.get("chanxzbh"));
			tempMap.put("EDITOR", params.get("jihybh"));
			tempMap.put("EDIT_TIME", timeNow);
			tempMap.put("CREATOR", params.get("jihybh"));
			tempMap.put("CREATE_TIME", timeNow);
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateRigdjh",tempMap);
			if(num == 0){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertRigdjh",tempMap);
			}
		}
	}
	
	/**
	 * @description 从期初库存列表中得到零件的期初库存，安全库存
	 * @author 王国首
	 * @date 2012-03-08
	 * @param chanxLingj 产线零件MAP
	 * @param qckc		 期初库存集合
	 */
	public void addParams(Map<String, String> chanxLingj,List<Map<String,String>> qckc){
		for(Map<String, String> qckcMap : qckc){
			if(chanxLingj.get("LINGJBH").equals(qckcMap.get("LINGJBH"))){
				chanxLingj.put("ANQKC", String.valueOf(qckcMap.get("ANQKC")));
				chanxLingj.put("QICKC", String.valueOf(qckcMap.get("QCKC")));
				break;
			}
		}
	}
	
	/**
	 * @description 得到计算开始时间和计算开始时间之后的已经封闭的产线组工作日历
	 * @author 王国首
	 * @date 2012-03-19
	 * @param params  包含产线和排产时间范围
	 * @param shengxList 产线列表
	 * @return Map       封闭的工作日历
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> parseLastBlockWorkCalendar(Map<String, String> params) {
		Map<String,String> LastBlockWorkCalendar = new HashMap<String,String>();
		List<Map<String, String>> temp = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryLastBlockWorkCalendar",params);
		for( Map<String, String> dailyBlock : temp){
			LastBlockWorkCalendar.put(dailyBlock.get("SHIJ"), dailyBlock.get("SHIJ"));
		}
		return LastBlockWorkCalendar;
	}	
	
	/**
	 * @description 计算减去封闭期需求后的期初库存
	 * @author 王国首
	 * @date 2012-03-09
	 * @param shengxList 产线列表
	 * @param params  包含产线和排产时间范围
	 * @param qckc    期初库存
	 */
	@SuppressWarnings("unchecked")
	public void calBlockDailyXQ(List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String,String>> qckc){
		List<Map<String, String>> blockMxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryLastBlockMxq",params);
		for( Map<String, String> blockMxqMap : blockMxq){
			String lingjh = blockMxqMap.get("LINGJBH") != null ? blockMxqMap.get("LINGJBH") : blockMxqMap.get("LINGJMAOXQ");
			blockMxqMap.put("LINGJBH", lingjh);
			for( Map<String,String> qckcMap : qckc){
				if(lingjh.equals(qckcMap.get("LINGJBH"))){
					qckcMap.put("SHIJ", params.get("kaissj"));
					qckcMap.put("biaos", params.get("biaos"));
					upateBlockDailyXQQckc(params,qckcMap);
					double qckcsl = Double.parseDouble(String.valueOf(qckcMap.get("QCKC")));
					if(blockMxqMap.get("LINGJSL") != null && !"".equals(blockMxqMap.get("LINGJSL"))){
						qckcsl = qckcsl + Double.parseDouble(String.valueOf(blockMxqMap.get("LINGJSL")));
					}
					qckcsl = calBlockDailyXQQckc(shengxList,params,blockMxqMap,qckcsl,lingjh);
					qckcMap.put("QCKC", String.valueOf(qckcsl));
					break;
				}
			}
		}
	}
	
	/**
	 * @description 计算期初库存减去当天的毛需求
	 * @author 王国首
	 * @date 2012-03-09
	 * @param shengxList 产线列表
	 * @param params  包含产线和排产时间范围
	 * @param qckc    期初库存
	 * @param blockMxqMap    封闭期的零件
	 * @param qckcsl    期初库存数量
	 * @param lingjh    零件编号
	 */
	public double calBlockDailyXQQckc(List<Map<String, String>> shengxList,Map<String, String> params,Map<String,String> blockMxqMap,double qckcsl,String lingjh){
		double shul = qckcsl;
		if(blockMxqMap.get("MAOXQ") != null && !"".equals(blockMxqMap.get("MAOXQ"))){
			shul = shul - Double.parseDouble(String.valueOf(blockMxqMap.get("MAOXQ")));
			if(shul<=0){
				for(Map<String, String> shengxc : shengxList){
					String xiaox = "产线"+shengxc.get("SHENGCXBH")+",零件"+lingjh+","+params.get("kaissj")+"期初库存小于零, 当前期初库存为"+shul+",请计划员调整生产";
					this.addMessage(params,yueMnjhh.get(shengxc.get("SHENGCXBH")),params.get("USERCENTER"),shengxc.get("SHENGCXBH"),params.get("kaissj"),xiaox,"2");
				}
			}
		}
		return shul;
	}

	/**
	 * @description 更新封闭期的期初库存安全库存
	 * @author 王国首
	 * @date 2012-03-09
	 * @param shengxList 产线列表
	 * @param params  包含产线和排产时间范围
	 * @param qckc    期初库存
	 * @param blockMxqMap    封闭期的零件
	 * @param qckcsl    期初库存数量
	 * @param lingjh    零件编号
	 */
	public void upateBlockDailyXQQckc(Map<String, String> params,Map<String,String> qckc){
		int numqckc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiQckc",qckc);
		if(numqckc == 0 ){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMeiRiQckc",qckc);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateRiRollQckc",qckc);
	}
	
	/**
	 * @description 生成月度模拟计划号
	 * @author 王国首
	 * @date 2012-3-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public Map<String, String> getYueMoNijhh(List<Map<String, String>> shengxList,Map<String, String> params){
		Map<String, String> temp = new HashMap<String, String>();
		for( Map<String, String> shengcxMap : shengxList ){
			/*生成日滚动计划号*/
			String shij = "";
			try{
				shij = DateUtil.StringFormatToString(params.get("today"));
			}catch(ParseException e){
				logger.error(e.getMessage());
			}
			temp.put(shengcxMap.get("SHENGCXBH"), params.get("USERCENTER")+shengcxMap.get("SHENGCXBH")+shij);
		}
		return temp;
	}
	
	/**
	 * @description 对日滚动排产计划进行排产，得到每日零件生产数量
	 * @author 王国首
	 * @date 2012-3-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		期初库存
	 */
	public List<Map<String,String>> calDailyProduce(List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String,String>> qckc) {
		List<Map<String, String>> lingjri = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryrigdpcjhmxDailyLj",params);
		List<Map<String,String>> qckctemp= listMapCopy(qckc);
		calQckc(qckc,lingjri);
		params.put("shijNext", getNextWorkDay(params,params.get("chanxzbh"),params.get("kaissj")));
		for( Map<String, String> shengcx : shengxList){
			double shengcjp = Double.parseDouble(String.valueOf(shengcx.get("SHENGCJP")));
			params.put("EVERYTIME", shengcx.get("EVERYTIME"));
//			List<Map<String, String>> kuCunXiShu = new ArrayList<Map<String,String>>();
			String shengcxbh = shengcx.get("SHENGCXBH");
			params.put("shengcxCX", shengcxbh);
			//test
			String logString = this.getListMapKeyValue(qckc);
			logger.info("期初库存："+params.get("kaissj")+" " +shengcxbh +" "+ logString);
			/*查询出日滚动排产计划明细中的预计排产量和对应的月模拟的每日确保产能*/
			List<Map<String, String>> dailyProduce = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRIGDPCJHMX",params);
			/*预计排产量与每日确保产能比较,    true:每日确保产能>预计排产量             false:每日确保产能<=预计排产量 */
			Map<String, String> compareResult = compareYujpclAndMeirqbcl(dailyProduce,params);
			double meirqbtime = Double.parseDouble(compareResult.get("meirqbtime"));
			double yujpcl = Double.parseDouble(compareResult.get("yujpcl"));
			Map<String,Map<String,String>> zengc = new HashMap<String,Map<String,String>>();
			logger.info("00001:"+this.getQckcPc(qckc, "9803218980"));
			if("0".equals(compareResult.get("flag"))){
				calJianchan(params,qckc,dailyProduce,shengcxbh,yujpcl/shengcjp - meirqbtime,qckctemp);
			} else if("1".equals(compareResult.get("flag"))){
				/*当每日确保产能>预计排产量时，进行增产*/
				zengc = calZengchan(params,qckc,dailyProduce,shengcxbh,meirqbtime - yujpcl/shengcjp);
				calDailyRollPC(params,qckc,qckctemp,dailyProduce,zengc);
			}
			calDailyShengcxAfter(params);
			logger.info("30001:"+this.getQckcPc(qckc, "9803218980"));
		}
		updateMeiRiMaoxq(params);
		updateMeiRiQckc(params,qckctemp);
		return qckc;
	}
	
	/**
	 * @description 生产线每日的预计排产量与每日确保产能比较
	 * @author 王国首
	 * @date 2012-3-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @return       true:每日确保产能>预计排产量             false:每日确保产能<预计排产量
	 */
	public Map<String, String> compareYujpclAndMeirqbcl(List<Map<String, String>> dailyProduce,Map<String, String> params){
		Map<String, String> result = new HashMap<String, String>();
		String flag = "0";
		double yujpcl = 0;
//		double meirqbcl = 0;
		double meirqbtime = 0;
		double shengcjp = 0;
		for( Map<String, String> dailyProduceMap : dailyProduce ){
			meirqbtime = Double.parseDouble(String.valueOf(dailyProduceMap.get("MEIRQBTIME")));
//			meirqbcl = Double.parseDouble(String.valueOf(dailyProduceMap.get("MEIRQBCN")));
			yujpcl = yujpcl + Double.parseDouble(String.valueOf(dailyProduceMap.get("LINGJSL")));
			shengcjp = Double.parseDouble(String.valueOf(dailyProduceMap.get("SHENGCJP")));
		}
		if(shengcjp>0 && meirqbtime>yujpcl/shengcjp){
			flag = "1";
		}else if(dailyProduce.size()==0){
			flag = "1";
			List<Map<String, String>>  gs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYuemnjhMeirqb",params);
			meirqbtime = gs.size()>0? Double.parseDouble(String.valueOf(gs.get(0).get("MEIRQBTIME"))):0;
		}
		result.put("meirqbtime", String.valueOf(meirqbtime));
		result.put("yujpcl", String.valueOf(yujpcl));
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * @description 当预计排产量小于每日确保产能时进行增产
	 * @author 王国首
	 * @date 2012-3-7
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param params	 一条产线当天的库存系数，已经进行小到大排序
	 * @param shengcx	生产线编号
	 * @param zengcsj	需要增产的工时
	 * @return zengc	增产的零件数量和增产的工时
	 */
	public Map<String,Map<String,String>> calZengchan(Map<String,String> params,List<Map<String,String>> qckc,List<Map<String, String>> chanxlj,String shengcx,double zengcljsj){
		Map<String,Map<String,String>> zengc = new HashMap<String,Map<String,String>>();
		double zengChanShiJ = zengcljsj;
		double everytime = Double.parseDouble(String.valueOf(params.get("EVERYTIME")));
		List<Map<String,String>> kuCunXiShu =  new ArrayList<Map<String,String>>();
		if(zengChanShiJ>0){
			int i = 0;
			while(zengChanShiJ>everytime && i<=1000){
				i++;
				kuCunXiShu = calkuCunXiShuRi(qckc,chanxlj,params.get("shengcxCX"));
				int xisflag = 0;
				double zengcsl = 0;
				double zengcshij = 0;
				String lingjbh = "";
				Map<String,String> zengckuCunXiShu = new HashMap<String,String>();
				for(Map<String,String> kuCunXiShuMap : kuCunXiShu ){
					double qckcsl = Double.parseDouble(String.valueOf(kuCunXiShuMap.get("QCKC")));
					double anqkctop = Double.parseDouble(String.valueOf(kuCunXiShuMap.get("ANQKCTOP")));
					double shengcjp = Double.parseDouble(String.valueOf(kuCunXiShuMap.get("SHENGCJP")));
					double jingjpl = Double.parseDouble(String.valueOf(kuCunXiShuMap.get("JINGJPL")));
					double mintime = Double.parseDouble(String.valueOf(params.get("MINTIME")));
					anqkctop = anqkctop>0?anqkctop:99999999;
					if(anqkctop>=qckcsl+1){
						if(anqkctop-qckcsl>=jingjpl){
							if(jingjpl != 0 && jingjpl<shengcjp*zengChanShiJ){
								zengcsl = jingjpl;
							}else if((anqkctop-qckcsl)/shengcjp<=zengChanShiJ && (anqkctop-qckcsl)>0){
								zengcsl = anqkctop-qckcsl; 
								if(zengcsl < jingjpl){
									zengcsl = jingjpl;
								}
							//0012250 - xss -必须达到经济批量
							//}else if(mintime<jingjpl/shengcjp ){
							//	zengcsl = Math.round(shengcjp*mintime);
							}else{
								zengcsl = jingjpl;
							}
						}else{
							//0012250 - xss -必须达到经济批量
							zengcsl = anqkctop-qckcsl;
							if(zengcsl < jingjpl){
								zengcsl = jingjpl;
							} 
						}
						if(zengcsl/shengcjp>zengChanShiJ){
							zengcshij = zengChanShiJ;
							zengcsl = Math.round(zengChanShiJ*shengcjp);
							
							//xss-20160608 增产必须达到经济批量
							if(zengcsl < jingjpl){
								zengcsl = jingjpl;
							} 
						}
						zengcshij = getMinTime(0.01,zengcsl/shengcjp);
						zengckuCunXiShu.putAll(kuCunXiShuMap);
						lingjbh = kuCunXiShuMap.get("LINGJBH");
						break;
					}
					xisflag ++;
				}
				if(xisflag==kuCunXiShu.size()){
					break;
				}
				zengChanShiJ = zengChanShiJ - zengcshij;
				zengckuCunXiShu.put("ZENGCLJSJ", String.valueOf(zengcshij));
				zengckuCunXiShu.put("LINGJSL", String.valueOf(zengcsl));
				Map<String,String> addqckc = new HashMap<String,String>();
				addqckc.putAll(zengckuCunXiShu);
				if(zengc.get(lingjbh) != null){
					zengckuCunXiShu = zengc.get(lingjbh);
					zengckuCunXiShu.put("ZENGCLJSJ", String.valueOf(Double.parseDouble(String.valueOf(zengckuCunXiShu.get("ZENGCLJSJ"))) + zengcshij));
					zengckuCunXiShu.put("LINGJSL", String.valueOf(Double.parseDouble(String.valueOf(zengckuCunXiShu.get("LINGJSL"))) + zengcsl));
				}
				zengc.put(lingjbh, zengckuCunXiShu);
				List<Map<String,String>> addqckcList = new ArrayList<Map<String,String>>();
				addqckcList.add(addqckc);
				logger.info("01001:"+this.getQckcPc(qckc, "9803218980"));
				logger.info(addqckc);
				calQckc(qckc,addqckcList);
				if(zengChanShiJ<=0){break;}
			}
			if(zengChanShiJ>0 && zengChanShiJ>everytime){
				checkZengcNotEnoughRi (params,shengcx,params.get("USERCENTER"),params.get("kaissjPC"),zengcljsj,zengcljsj-zengChanShiJ,"2");
			}
		}
		return zengc;
	}

	/**
	 * @description 当预计排产量小于每日确保产能时进行增产
	 * @author 王国首
	 * @date 2012-3-7
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param params	 一条产线当天的库存系数，已经进行小到大排序
	 * @param shengcx	生产线编号
	 * @param zengcsj	需要增产的工时
	 * @return zengc	增产的零件数量和增产的工时
	 */
	public Map<String,Map<String,String>> calJianchan(Map<String,String> params,List<Map<String,String>> qckc,List<Map<String, String>> chanxlj,String shengcx,double zengcljsj,List<Map<String,String>> tempQckc){
		Map<String,Map<String,String>> zengc = new HashMap<String,Map<String,String>>();
		double zengChanShiJ = zengcljsj;
		double everytime = Double.parseDouble(String.valueOf(params.get("EVERYTIME")));
		List<Map<String,String>> kuCunXiShu =  new ArrayList<Map<String,String>>();
		if(zengChanShiJ>0){
			int i = 0;
			while(zengChanShiJ>everytime && i<=300){
				i++;
				kuCunXiShu = calkuCunXiShuRi(qckc,chanxlj,params.get("shengcxCX"));
				int xisflag = 0;
				double zengcsl = 0;
				double jiancshij = 0;
				String lingjbh = "";
				Map<String,String> zengckuCunXiShu = new HashMap<String,String>();
				Map<String,String> jiancMap = null;
				double jjpl = 0;
				double qckcsl = 0;
				double risl = 0;
				double shengcsl = 0;
				double jiancsl = 0;
				for(int k =kuCunXiShu.size()-1;k>=0;k--){
					Map<String,String> kuCunXiShuMap = kuCunXiShu.get(k);
					for(int c =0;c<chanxlj.size();c++){
						Map<String,String> chanxljMap = chanxlj.get(c);
						if(kuCunXiShuMap.get("LINGJBH").equals(chanxljMap.get("LINGJBH"))){
							Map<String,String> qcMap = this.getQckcPc(qckc, kuCunXiShuMap.get("LINGJBH"));
							 jjpl = Double.parseDouble(String.valueOf(chanxljMap.get("JINGJPL")));
							 double jiep = Double.parseDouble(String.valueOf(chanxljMap.get("SHENGCJP")));
							 jjpl = jjpl<1?jiep:jjpl;
							 qckcsl = Double.parseDouble(String.valueOf(qcMap.get("QCKC")));
							 risl = Double.parseDouble(String.valueOf(chanxljMap.get("LINGJSL")));
							 double biz = risl/jjpl;
							 //xss-0012250 排产量小于等于经济批量时，直接减掉。
							if(risl>jjpl && risl>=1){
								if(biz>=1){
									shengcsl = risl-jjpl;
								}else{
									shengcsl = 0;
								}
								
								//xss-20160608 减产后小于经济批量时 直接减掉
								if( shengcsl < jjpl ){
									shengcsl = 0;
								} 
									
								jiancsl = Math.ceil(risl-shengcsl);
								shengcsl = Math.ceil(shengcsl);
								double hour = Math.ceil(shengcsl/jiep);
								jiancshij = jiancsl/jiep;
								chanxljMap.put("LINGJSL", String.valueOf(shengcsl));
								chanxljMap.put("HOUR", String.valueOf(hour));
								chanxljMap.put("JIANCFLAG", "1");
								zengckuCunXiShu.put("LINGJBH", chanxljMap.get("LINGJBH"));
								zengckuCunXiShu.put("LINGJSL", String.valueOf(jiancsl*(-1)));
								jiancMap = chanxljMap;
								break;
							}else if(risl>=1){
								shengcsl = 0;
								jiancsl = Math.ceil(risl-shengcsl);
								shengcsl = Math.ceil(shengcsl);
								double hour = Math.ceil(shengcsl/jiep);
								jiancshij = jiancsl/jiep;
								chanxljMap.put("LINGJSL", String.valueOf(shengcsl));
								chanxljMap.put("HOUR", String.valueOf(hour));
								chanxljMap.put("JIANCFLAG", "1");
								zengckuCunXiShu.put("LINGJBH", chanxljMap.get("LINGJBH"));
								zengckuCunXiShu.put("LINGJSL", String.valueOf(jiancsl*(-1)));
								jiancMap = chanxljMap;
								break;
							}
						}
					}
					if(jiancMap!=null){
						break;
					}
				}
				if(jiancMap!=null){
					zengChanShiJ = zengChanShiJ - jiancshij;
					Map<String,String> addqckc = new HashMap<String,String>();
					addqckc.putAll(zengckuCunXiShu);
					List<Map<String,String>> addqckcList = new ArrayList<Map<String,String>>();
					addqckcList.add(addqckc);
					logger.info("01001:"+this.getQckcPc(qckc, "9803218980"));
					logger.info(addqckc);
					calQckc(qckc,addqckcList);	
				}
				if(zengChanShiJ<=0 || zengChanShiJ<everytime){break;}
			}
			for(Map<String,String> chanxljMap:chanxlj){
				double shengcsl = Double.parseDouble(String.valueOf(chanxljMap.get("LINGJSL")));
				double scsl = 0;
				String usbaoz = params.get("USERCENTER")+params.get("shengcxCX")+chanxljMap.get("LINGJBH");
				/*将当天排产出的零件按包装进行取整*/
				if(getUsPack().get(usbaoz) != null && !"".equals(usbaoz) && shengcsl>=1){
					scsl = getPackFull(Double.parseDouble(String.valueOf(getUsPack().get(usbaoz))),shengcsl);
				}
				if(shengcsl!=scsl && scsl>0){
					double jiep = Double.parseDouble(String.valueOf(chanxljMap.get("SHENGCJP")));
					double hour = Math.ceil(scsl/jiep);
					chanxljMap.put("LINGJSL", String.valueOf(scsl));
					chanxljMap.put("HOUR", String.valueOf(hour));
					
					Map<String,String> zengckuCunXiShu = new HashMap<String,String>();
					zengckuCunXiShu.put("LINGJBH", chanxljMap.get("LINGJBH"));
					zengckuCunXiShu.put("LINGJSL", String.valueOf(scsl-shengcsl));
					List<Map<String,String>> addqckcList = new ArrayList<Map<String,String>>();
					addqckcList.add(zengckuCunXiShu);
					calQckc(qckc,addqckcList);	
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateRIGDPCJHMXJianc",chanxljMap);
				logger.info("日滚动减产数量为:"+chanxljMap.get("LINGJBH")+"-"+Double.parseDouble(String.valueOf(chanxljMap.get("LINGJSL"))));
			}
			if(zengChanShiJ>0 && zengChanShiJ>everytime){
				checkZengcNotEnoughRi (params,shengcx,params.get("USERCENTER"),params.get("kaissjPC"),zengcljsj,zengcljsj-zengChanShiJ,"2");
			}
		}
		return zengc;
	}
	
	/**
	 * @description 当产线零件净需求之和小于每日确保产能时进行增产
	 * @author 王国首
	 * @date 2012-1-7
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param params	 一条产线当天的库存系数，已经进行小到大排序
	 * @param shengcx	生产线编号
	 * @param zengcsj	需要增产的工时
	 * @return zengc	增产的零件数量和增产的工时
	 */
	public double calZengchanNext(Map<String,String> params,Map<String,Map<String,String>>zengc,List<Map<String, String>> chanxlj,String shengcx,double zengcsj){
		String timeNow = getTimeNow(TIMEFORMAT);
		String lingjString = CollectionUtil.chanxListToString(chanxlj,"LINGJBH");
		if(lingjString.length()>0){
			params.put("lingjString", lingjString);
		}else if(params.get("lingjString") != null){
			params.remove("lingjString");
		}
		List<Map<String, String>> zengcLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryZengcljslNext",params);  
		double zengChanShiJ = zengcsj;
		/*当增产时间大于0时，需要增产*/
		if(zengChanShiJ>0){
			for(Map<String,String> zengcLingjMap : zengcLingj ){
				String lingjbh = zengcLingjMap.get("LINGJBH");
				double zengcljsl = 0;
				double shengcjp = Double.parseDouble(String.valueOf(zengcLingjMap.get("SHENGCJP")));
				zengcljsl = Double.parseDouble(String.valueOf(zengcLingjMap.get("LINGJSL"))); 
				zengcljsl = zengcljsl>=zengChanShiJ? zengChanShiJ : zengcljsl ;
				zengChanShiJ = zengcljsl>=zengChanShiJ ? 0 : zengChanShiJ - zengcljsl;
				if(zengc.get(lingjbh) != null){
					zengcLingjMap = zengc.get(lingjbh);
					zengcljsl = zengcljsl + Double.parseDouble(String.valueOf(zengcLingjMap.get("ZENGCSL")));
				}
				zengcLingjMap.put("ZENGCLJSJ", String.valueOf(Math.ceil(zengcljsl*100/shengcjp)/100));
				zengcLingjMap.put("ZENGCSL", String.valueOf(zengcljsl));
				zengc.put(lingjbh, zengcLingjMap);
				boolean flag = false;
				for(Map<String, String> chanxljMap : chanxlj){
					if(chanxljMap.get("LINGJBH").equals(lingjbh)){
						flag = true;
						break;
					}
				}
				if(!flag){
					zengcLingjMap.put("RIGDJHH", yueMnjhh.get(params.get("shengcxCX")));
					if(getBlockWorkCalendar().get(params.get("shengcxCX")).get(params.get("kaissjPC")) != null){
						zengcLingjMap.put("ZHUANGT", "2");
					}else{
						zengcLingjMap.put("ZHUANGT", "3");
					}
					zengcLingjMap.put("CHANXZBH", params.get("chanxzbh"));
					zengcLingjMap.put("EDITOR", params.get("jihybh"));
					zengcLingjMap.put("EDIT_TIME", timeNow);
					zengcLingjMap.put("CREATOR", params.get("jihybh"));
					zengcLingjMap.put("CREATE_TIME", timeNow);
					zengcLingjMap.put("SHIJ", params.get("kaissjPC"));
					zengcLingjMap.put("SHENGCSHIJ", "0");
					zengcLingjMap.put("LINGJSL", "0");	
					chanxlj.add(zengcLingjMap);
				}
				/*当增产时间为0时，增产完成，跳出循环*/
				if(zengChanShiJ<=0){break;}
			}
		}
		return zengChanShiJ;
	}
	
	/**
	 * @description 检测当实际增产时间小于计划增产时间时间，将信息写入提示信息
	 * @author 王国首
	 * @date 2012-2-12
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkZengcNotEnoughRi (Map<String,String> params,String shengcx,String usercenter,String shij,double zengcsj,double shiji,String leix){
		 if(shiji<zengcsj){ 
			 String xiaox = "产线"+shengcx+", 在"+shij+"计划增产零件"+zengcsj+"个,实际增产"+shiji+"个，请计划员调整生产"; 
			 this.addMessage(params,yueMnjhh.get(shengcx),usercenter,shengcx,shij,xiaox,leix); 
		} 
	}
	
	/**
	 * @description 得到日滚动排产计划明细
	 * @author 王国首
	 * @date 2012-1-10
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param chanxlj	 一条产线一天需要生产的零件
	 * @param zengc		 一条产线一天内进行增产的零件编号，零件数量，每个零件增产的时间
	 * @return tempQckc		返回最新的期初库存
	 */
	public List<Map<String,String>> calDailyRollPC(Map<String,String> params,List<Map<String,String>> Qckc,List<Map<String,String>> tempQckc,List<Map<String,String>> chanxlj,Map<String,Map<String,String>> zengc){
		List<Map<String,String>> mnpc = new ArrayList<Map<String,String>>();
		List<Map<String,String>> addqckc = new ArrayList<Map<String,String>>();
		String timeNow = getTimeNow(TIMEFORMAT);
		for(Map<String,String> chanxljMap :chanxlj){
			Map<String,String> addqckcMap = new HashMap<String,String>();
			String lingjbh = chanxljMap.get("LINGJBH");
			addqckcMap.put("LINGJBH", lingjbh);
			double shengcshij = 0;
			double addlingjsl = 0;
			double shengcsl = Double.parseDouble(String.valueOf(chanxljMap.get("LINGJSL")));
			if(zengc.size()>0 && zengc.get(lingjbh) != null){
				shengcsl= shengcsl + Double.parseDouble(String.valueOf(zengc.get(lingjbh).get("LINGJSL")));
				zengc.remove(lingjbh);
//				if(zengc.get(lingjbh).get("KUCXS") != null){
//					chanxljMap.put("KUCXS", String.valueOf(zengc.get(lingjbh).get("KUCXS")));					
//				}
			}
			addlingjsl= shengcsl;
			String shifjjpl = chanxljMap.get("SHIFQYJJPL");
			double jinJiPiLiang = Double.valueOf(String.valueOf(chanxljMap.get("JINGJPL")));
			if(shengcsl>0){
				shengcsl = "1".equals(shifjjpl) ? jinJiPiLiang>shengcsl ? jinJiPiLiang:shengcsl : shengcsl;
				String usbaoz = params.get("USERCENTER")+params.get("shengcxCX")+lingjbh;
				/*将当天排产出的零件按包装进行取整*/
				if(getUsPack().get(usbaoz) != null && !"".equals(usbaoz)){
					shengcsl = getPackFull(Double.parseDouble(String.valueOf(getUsPack().get(usbaoz))),shengcsl);
				}
			}
			for(Map<String,String> qckcMap : tempQckc){
				if(lingjbh.equals(qckcMap.get("LINGJBH"))){
					chanxljMap.put("QCKC", String.valueOf(qckcMap.get("QCKC")));
					chanxljMap.put("ANQKC", String.valueOf(qckcMap.get("ANQKC")));
					break;
				}
			}
			
			addqckcMap.put("LINGJSL", String.valueOf(shengcsl-addlingjsl));
			chanxljMap.put("RIGDJHH", getYueMnjhh().get(params.get("shengcxCX")));
			shengcshij = getMinTime(0.01,shengcsl / Double.parseDouble(String.valueOf(chanxljMap.get("SHENGCJP"))));
			chanxljMap.put("HOUR", String.valueOf(shengcshij));
			chanxljMap.put("LINGJSL", String.valueOf(shengcsl));
			chanxljMap.put("EDITOR", params.get("jihybh"));
			chanxljMap.put("EDIT_TIME", timeNow);
			chanxljMap.put("CREATOR", params.get("jihybh"));
			chanxljMap.put("CREATE_TIME", timeNow);
			chanxljMap.put("biaos", params.get("biaos"));
			chanxljMap.put("MAOXQ", "0");
			if(getBlockWorkCalendar().get(params.get("shengcxCX")).get(chanxljMap.get("SHIJ")) != null){
				chanxljMap.put("ZHUANGT", "2");
			}else{
				chanxljMap.put("ZHUANGT", "3");
			}
			mnpc.add(chanxljMap);
			addqckc.add(addqckcMap);
		}
		logger.info("10001:"+this.getQckcPc(Qckc, "9803218980"));
		logger.info(addqckc);
		calQckc(Qckc,addqckc);
		addqckc.clear();
		if(zengc.size()!=0){
			Iterator<String> it = zengc.keySet().iterator(); 
			while(it.hasNext()){
				double shengcshij = 0;
				double addlingjsl = 0;
				Map<String,String> zcMap = zengc.get(it.next());
				String lingjbh = zcMap.get("LINGJBH");
				Map<String,String> qMap =this.getQckcPc(tempQckc, lingjbh);
				Map<String,String> chanxljMapzc = new  HashMap<String,String>();
				chanxljMapzc.put("RIGDJHH", getYueMnjhh().get(params.get("shengcxCX")));
				double shengcsl= Double.parseDouble(String.valueOf(zcMap.get("LINGJSL")));

				addlingjsl= shengcsl;
				Map<String,String> addqckcMap = new HashMap<String,String>();
				String shifjjpl = zcMap.get("SHIFQYJJPL");
				double jinJiPiLiang = Double.valueOf(String.valueOf(zcMap.get("JINGJPL")));
				if(shengcsl>0){
					shengcsl = "1".equals(shifjjpl) ? jinJiPiLiang>shengcsl ? jinJiPiLiang:shengcsl : shengcsl;
					String usbaoz = params.get("USERCENTER")+params.get("shengcxCX")+lingjbh;
					/*将当天排产出的零件按包装进行取整*/
					if(getUsPack().get(usbaoz) != null && !"".equals(usbaoz)){
						shengcsl = getPackFull(Double.parseDouble(String.valueOf(getUsPack().get(usbaoz))),shengcsl);
					}
				}
				
				addqckcMap.put("LINGJBH", lingjbh);
				addqckcMap.put("LINGJSL", String.valueOf(shengcsl-addlingjsl));
				shengcshij = getMinTime(0.01,shengcsl / Double.parseDouble(String.valueOf(zcMap.get("SHENGCJP"))));
				chanxljMapzc.put("LINGJBH", lingjbh);
				chanxljMapzc.put("HOUR", String.valueOf(shengcshij));
				chanxljMapzc.put("LINGJSL", String.valueOf(shengcsl));
				chanxljMapzc.put("EDITOR", params.get("jihybh"));
				chanxljMapzc.put("EDIT_TIME", timeNow);
				chanxljMapzc.put("CREATOR", params.get("jihybh"));
				chanxljMapzc.put("CREATE_TIME", timeNow);
				chanxljMapzc.put("biaos", params.get("biaos"));
				chanxljMapzc.put("MAOXQ", "0");
				chanxljMapzc.put("USERCENTER", params.get("USERCENTER"));
				chanxljMapzc.put("CHANXH", params.get("shengcxCX"));
				chanxljMapzc.put("QCKC", qMap==null?"0":qMap.get("QCKC"));
				chanxljMapzc.put("SHIJ", params.get("kaissjPC"));
				chanxljMapzc.put("JINGJPL", zcMap.get("JINGJPL"));
				chanxljMapzc.put("ANQKC", qMap==null?"0":qMap.get("ANQKC"));
				chanxljMapzc.put("CHANXZBH", params.get("chanxzbh"));
				if(getBlockWorkCalendar().get(params.get("shengcxCX")).get(params.get("kaissjPC")) != null){
					chanxljMapzc.put("ZHUANGT", "2");
				}else{
					chanxljMapzc.put("ZHUANGT", "3");
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertRIGDPCJHMXZengc",chanxljMapzc);
				//xss-20170220
				int numqckc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiQckc",chanxljMapzc);
				if(numqckc == 0 && Double.parseDouble( String.valueOf(chanxljMapzc.get("LINGJSL"))) >0){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMeiRiQckc",chanxljMapzc);
				}
				addqckc.add(addqckcMap);
			}	
		}
		logger.info("20001:"+this.getQckcPc(Qckc, "9803218980"));
		logger.info(addqckc);
		calQckc(Qckc,addqckc);
		for( Map<String,String> temp: mnpc){
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateRIGDPCJHMX",temp);
			if(num == 0 && Double.parseDouble( String.valueOf(temp.get("LINGJSL"))) >0){
				if(getBlockWorkCalendar().get(params.get("shengcxCX")).get(params.get("kaissjPC")) != null){
					temp.put("ZHUANGT", "2");
				}else{
					temp.put("ZHUANGT", "3");
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertRIGDPCJHMXZengc",temp);
			}
			//xss-20170220
			int numqckc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiQckc",temp);
			if(numqckc == 0 && Double.parseDouble( String.valueOf(temp.get("LINGJSL"))) >0){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMeiRiQckc",temp);
			}
		}
		return Qckc;
	}
	
	/**
	 * @description 将每条产线上生产的零件分配到每个班上
	 * @author 王国首
	 * @date 2012-3-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @return  null
	 */
	public void calBancProduce(List<Map<String, String>> shengxList,Map<String, String> params){
		for( Map<String, String> shengcx : shengxList){
			String shengcxbh = shengcx.get("SHENGCXBH");
			params.put("shengcxCX", shengcxbh);
			List<Map<String, String>> chanxlj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRigdpcjhmxBanc",params);
			double alltime = calAllTime(chanxlj,"HOUR");
			List<String> banc = calBanc(params);
			if(banc.size()<=0){
				String xiaox = "产线"+shengcxbh+", 在"+params.get("kaissj")+"日,工作时间内班为空,请计划员设置产线对应的工作时间表中的班"; 
				this.addMessage(params,yueMnjhh.get(shengcxbh),params.get("USERCENTER"),shengcxbh,params.get("kaissj"),xiaox,"2"); 
				continue;
			}
			double avgTime = getMinTime(0.01,alltime/banc.size());
			Map<String , Map<String, List<Map<String, String>>>> bancLj = parseChanxLjBan(chanxlj,banc,avgTime);
			calProduce(params,banc,bancLj,avgTime);
		}
	}
	
	/**
	 * @description 计算总工时
	 * @author 王国首
	 * @date 2012-3-16
	 * @param source 产线零件
	 * @param name	 取数据的名称
	 * @return time
	 */
	public double calAllTime(List<Map<String, String>> source,String name){
		double time = 0;
		for( Map<String, String> temp : source){
			time = time + Double.parseDouble(String.valueOf(temp.get(name)));
		}
		return time;
	}
	
	/**
	 * @description 得到需要排产的班次
	 * @author 王国首
	 * @date 2012-3-16
	 * @param params	 包含产线和排产时间范围
	 * @return result  班次列表
	 */
	public List<String> calBanc(Map<String, String> params){
		List<String> result = new ArrayList<String>();
		Map<String, String> checkMap = new HashMap<String, String>();
		List<Map<String, String>> banc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryBanc",params);
		for( Map<String, String> temp : banc){
			if(temp.get("BAN") != null && !checkMap.containsKey(temp.get("BAN"))){
				checkMap.put(temp.get("BAN"), temp.get("BAN"));
				result.add(String.valueOf(temp.get("BAN")));	
			}
		}
		return result;
	}
	
	/**
	 * @description 解析产线零件，得到大于平均工时的班和班对应的零件，小于工时的班和班对应的零件和没有指定优先班次的零件
	 * @author 王国首
	 * @date 2012-3-16
	 * @param bancLj	需要排产的零件
	 * @param banc	 	需要排产的班
	 * @param avgTime	根据需要排产的班数，计算出的每个班需要生产的时间
	 * @return parseFinish  
	 */
	public Map<String , Map<String, List<Map<String, String>>>> parseChanxLjBan(List<Map<String, String>> chanxlj,List<String> banc,double avgTime){
		Map<String,List<Map<String, String>>> banLj =  new HashMap<String,List<Map<String, String>>>();
		Map<String,List<Map<String, String>>> bigBanLj =  new HashMap<String,List<Map<String, String>>>();
		Map<String,List<Map<String, String>>> smallBanLj =  new HashMap<String,List<Map<String, String>>>();
		Map<String,List<Map<String, String>>> nullBanLj =  new HashMap<String,List<Map<String, String>>>();
		for( Map<String, String> chanxljMap : chanxlj){
			List<Map<String, String>> temp= null;
			if (banLj.containsKey(chanxljMap.get("BANC"))) {
				temp = banLj.get(chanxljMap.get("BANC"));
			} else {
				temp = new ArrayList<Map<String, String>>();
			}
			temp.add(chanxljMap);
			banLj.put(chanxljMap.get("BANC"), temp);
		}
		for(int i = 0;i<banc.size();i++){
			String bancName = banc.get(i);
			if(banLj.get(bancName) != null){
				double banTime = calAllTime(banLj.get(bancName),"HOUR");
				if(banTime>avgTime){
					bigBanLj.put(bancName, listMapCopy(banLj.get(bancName)));
				}else{
					smallBanLj.put(bancName, listMapCopy(banLj.get(bancName)));
				}
				banLj.remove(bancName);
			}else{
				smallBanLj.put(bancName, new ArrayList<Map<String, String>>());
			}
		}
		List<Map<String, String>> nullBanc = new ArrayList<Map<String, String>>();
		if(banLj.size()>0){
			Iterator<String> it = banLj.keySet().iterator(); 
			while(it.hasNext()){
				nullBanc.addAll(listMapCopy(banLj.get(it.next())));
			}
		}
		nullBanLj.put("NB", nullBanc);
		Map<String , Map<String, List<Map<String, String>>>> parseFinish = new HashMap<String , Map<String, List<Map<String, String>>>>();
		parseFinish.put("BIG", bigBanLj);
		parseFinish.put("SMALL", smallBanLj);
		parseFinish.put("NULLB", nullBanLj);
		return parseFinish;
	}
	
	/**
	 * @description 将零件按照优先班次生产原则分配到各个班
	 * @author 王国首
	 * @date 2012-3-16
	 * @param banc	 需要排产的班
	 * @param bancLj	需要排产的零件
	 * @param avgTime	根据需要排产的班数，计算出的每个班需要生产的时间
	 * @return null  
	 */
	public void calProduce(Map<String, String> params,List<String> banc,Map<String , Map<String, List<Map<String, String>>>> bancLj,double avgTime){
		Map<String,List<Map<String, String>>> bigBanLj		=  bancLj.get("BIG");
		Map<String,List<Map<String, String>>> smallBanLj	=  bancLj.get("SMALL");
		Map<String,List<Map<String, String>>> nullBanLj		=  bancLj.get("NULLB");
		List<Map<String, String>> finishBanLJ = new ArrayList<Map<String, String>>();

		calProduceBig(params,bigBanLj,nullBanLj,finishBanLJ,avgTime);
		
		if(nullBanLj.get("NB") != null && nullBanLj.get("NB").size()>0){
			List<Map<String, String>> nullBanLjList = nullBanLj.get("NB");
			for(int i = (nullBanLjList.size()-1);i>=0;i--){
				Map<String, String> nullBanLjMap = nullBanLjList.get(i);
				while(nullBanLjMap.size()>0){
					nullBanLjMap = parseBanAgent(banc,smallBanLj,nullBanLjMap,finishBanLJ,avgTime);
				}
			}
		}
		
		Iterator<String> it = smallBanLj.keySet().iterator(); 
		while(it.hasNext()){
			List<Map<String, String>> smallBanLjList = smallBanLj.get(it.next());
			if(smallBanLjList.size()>0){
				finishBanLJ.addAll(smallBanLjList);
			}
		}
		String timeNow = getTimeNow(TIMEFORMAT);
		for(Map<String, String> finishBanLJMap : finishBanLJ){
			finishBanLJMap.put("CHANXZBH", params.get("chanxzbh"));
			finishBanLJMap.put("EDITOR", params.get("jihybh"));
			finishBanLJMap.put("EDIT_TIME", timeNow);
			finishBanLJMap.put("CREATOR", params.get("jihybh"));
			finishBanLJMap.put("CREATE_TIME", timeNow);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertBANCMX",finishBanLJMap);
		}
	}
	
	/**
	 * @description 当班的总工时大于平均工时时，将多出的零件分出，保证此班的总工时等于平均工时。
	 * @author 王国首
	 * @date 2012-3-16
	 * @param banc	 需要排产的班
	 * @param bancLj	需要排产的零件
	 * @param avgTime	根据需要排产的班数，计算出的每个班需要生产的时间
	 * @return null  
	 */
	public void calProduceBig(Map<String, String> params,Map<String,List<Map<String, String>>> bigBanLj,Map<String,List<Map<String, String>>> nullBanLj,List<Map<String, String>> finishBanLJ,double avgTime){
		Iterator<String> it = bigBanLj.keySet().iterator(); 
		while(it.hasNext()){
			List<Map<String, String>> bigList = bigBanLj.get(it.next());
			if(bigList.size()>0){
				for(int i = (bigList.size()-1);i>=0;i--){
					Map<String, String> firstBig = bigList.get(i);
					bigList.remove(i);
					double banTime = calAllTime(bigList,"HOUR");
					double firstBigTime = Double.parseDouble(String.valueOf(firstBig.get("HOUR")));
					double firstBigLingj = Double.parseDouble(String.valueOf(firstBig.get("LINGJSL")));
					if(banTime >= avgTime){
						nullBanLj.get("NB").add(firstBig);
					}else{
						Map<String, String> parseBig = new HashMap<String, String>();
						parseBig.putAll(firstBig);
						double shengcJP = Double.parseDouble(String.valueOf(firstBig.get("SHENGCJP")));
						double lingjsl = Math.ceil((avgTime - banTime)*shengcJP);
						lingjsl = lingjsl > firstBigLingj?firstBigLingj:lingjsl;
						double hour = getMinTime(0.01,lingjsl/shengcJP);
						firstBig.put("LINGJSL", String.valueOf(lingjsl));
						firstBig.put("HOUR", String.valueOf(hour));
						bigList.add(firstBig);
						if((firstBigLingj-lingjsl)>0){
							parseBig.put("LINGJSL", String.valueOf(firstBigLingj-lingjsl));
							parseBig.put("HOUR", String.valueOf(getMinTime(0.01,firstBigTime-hour)));
							nullBanLj.get("NB").add(parseBig);	
						}
						break;
					}
				}
				finishBanLJ.addAll(bigList);
			}
		}
	}
	
	/**
	 * @description 将没有设置优先班的零件分配到需要排产的班上
	 * @author 王国首
	 * @date 2012-3-16
	 * @param banc	 需要排产的班
	 * @param smallBanLj	小于平均生产时间的班和班下面的零件
	 * @param nullBanLjMap	没有设置优先班的班和班下面的零件
	 * @param finishBanLJ	已经完成零件分配的班和班下面的零件
	 * @param avgTime	根据需要排产的班数，计算出的每个班需要生产的时间
	 * @return null  
	 */
	public Map<String, String> parseBanAgent(List<String> banc,Map<String,List<Map<String, String>>> smallBanLj,Map<String, String> nullBanLjMap,List<Map<String, String>> finishBanLJ,double avgTime){
		Map<String, String> tempNullBanLjMap = new HashMap<String, String>();
		if(smallBanLj.size()>0){
			if(smallBanLj.size()>1){
				double nullTime = Double.parseDouble(String.valueOf(nullBanLjMap.get("HOUR")));
				double nullLingjsl = Double.parseDouble(String.valueOf(nullBanLjMap.get("LINGJSL")));
				Iterator<String> it = smallBanLj.keySet().iterator(); 
				double bigTimeBan = 0;
				String keyName = "";
				while(it.hasNext()){
					String key = it.next();
					double banTime = calAllTime(smallBanLj.get(key),"HOUR");
					if(keyName.length()==0 || banTime>bigTimeBan){
						bigTimeBan = banTime;
						keyName = key;
					}
				}
				if((avgTime - bigTimeBan) > nullTime){
					nullBanLjMap.put("BANC", keyName);
					tempNullBanLjMap.putAll(nullBanLjMap);
					smallBanLj.get(keyName).add(tempNullBanLjMap);
					nullBanLjMap.clear();
				}else{
					Map<String, String> parseBig = new HashMap<String, String>();
					parseBig.putAll(nullBanLjMap);
					double shengcJP = Double.parseDouble(String.valueOf(nullBanLjMap.get("SHENGCJP")));
					double lingjsl = Math.ceil((avgTime - bigTimeBan)*shengcJP);
					double hour = getMinTime(0.01,lingjsl/shengcJP);
					parseBig.put("LINGJSL", String.valueOf(lingjsl));
					parseBig.put("HOUR", String.valueOf(hour));
					parseBig.put("BANC", String.valueOf(keyName));
					smallBanLj.get(keyName).add(parseBig);
					finishBanLJ.addAll(smallBanLj.get(keyName));
					smallBanLj.remove(keyName);
					nullBanLjMap.put("LINGJSL", String.valueOf(nullLingjsl-lingjsl));
					nullBanLjMap.put("HOUR", String.valueOf(getMinTime(0.01,nullTime-hour)));
				}
			}else{
				for( String bc : banc){
					if(smallBanLj.get(bc) != null){
						nullBanLjMap.put("BANC", bc);
						tempNullBanLjMap = new HashMap<String, String>();
						tempNullBanLjMap.putAll(nullBanLjMap);
						smallBanLj.get(bc).add(tempNullBanLjMap);
						nullBanLjMap.clear();
					}
				}
			}
		}else{
			nullBanLjMap.clear();
		}
		return nullBanLjMap;
	}
	
	/**
	 * @description 将日滚动排产计划明细插入NUP输出接口表
	 * @author 王国首
	 * @date 2012-3-31
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void parseOutPutNup(Map<String, String> params) {
		params.put("LYXT", "ZCJ");
		params.put("YXBH", "1");
		params.put("ZFBJ", "+");
		params.put("HGBM", "ZX");
		params.put("FILLER", "        ");
		List<Map<String, String>> monthPC = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRIGDPCJHMXNUP",params);
		//使用批量操作插入数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertNup",monthPC);
//		for(Map<String,String> monthPCMap: monthPC){
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("monpc.insertNup",monthPCMap);
//		}
	} 
	
	/**
	 * @description 日滚动确认时先将Nup输出接口表中对应生产线的数据删除掉
	 * @author 王国首
	 * @date 2012-1-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void deleteZCPC(Map<String, String> params){
		params.put("dailylyxt", "ZCJ");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteZCPC",params);
	}
	
	/**
	 * @description 计算零件按照包装取整
	 * @author 王国首
	 * @date 2011-12-30
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return gongShi	考虑最小时间单位后的工时
	 */
	public double getPackFull(double packFull,double shengcsl){
		double result = 0;
		result = shengcsl%packFull == 0.0 ? shengcsl : Math.ceil(shengcsl/packFull)*packFull;
		return result;
	}
	
	/**
	 * @description 更新每日毛需求
	 * @author 王国首
	 * @date 2012-3-16
	 * @param params	 包含产线和排产时间范围
	 * @return result  班次列表
	 */
	public void updateMeiRiMaoxq(Map<String, String> params){
		List<Map<String, String>> maoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRimaoq",params);
		for( Map<String, String> maoxqMap: maoxq){
			List<Map<String, String>> lingjshengcx = getLingjOfLine().get(maoxqMap.get("LINGJBH"));
			boolean ljflag = false;
			String shengcxbh = "";
			Map<String, String> lingjshengcxMap = null;
			for(int i = 0;i<lingjshengcx.size();){
				lingjshengcxMap = lingjshengcx.get(i);
				maoxqMap.put("biaos", params.get("biaos"));
				maoxqMap.put("CHANXH", lingjshengcxMap.get("SHENGCXBH"));
				maoxqMap.put("RIGDJHH", getYueMnjhh().get(lingjshengcxMap.get("SHENGCXBH")));
				maoxqMap.put("CHANXZBH", params.get("chanxzbh"));
				maoxqMap.put("EDITOR", params.get("jihybh"));
				maoxqMap.put("EDIT_TIME", getTimeNow(TIMEFORMAT));
				maoxqMap.put("CREATOR", params.get("jihybh"));
				maoxqMap.put("CREATE_TIME", getTimeNow(TIMEFORMAT));
				if(lingjshengcxMap.get("ZHUXFX") != null && params.get("ZHUXFX").equals(lingjshengcxMap.get("ZHUXFX"))){
					shengcxbh = lingjshengcxMap.get("SHENGCXBH");
				}
				int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiMaoxq",maoxqMap);
				if(num == 0){
					if(getBlockWorkCalendar().get(lingjshengcxMap.get("SHENGCXBH")).get(params.get("kaissj")) != null){
						maoxqMap.put("ZHUANGT", "2");
					}else{
						maoxqMap.put("ZHUANGT", "3");
					}
					num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMeiRiMaoxq",maoxqMap);
				}
				if(num>0){
					ljflag = true;
					break;
				}else{
					ljflag = num>0;
				}
			}
			if(!ljflag && shengcxbh.length()>0){
				maoxqMap.put("CHANXH", shengcxbh);
				maoxqMap.put("RIGDJHH", getYueMnjhh().get(shengcxbh));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMeiRiMaoxq",maoxqMap);
			}
		}
	}
	
	/**
	 * @description 更新每日毛需求
	 * @author 王国首
	 * @date 2012-3-16
	 * @param params	 包含产线和排产时间范围
	 * @return result  班次列表
	 */
	public void calDailyParam(List<Map<String, String>> shengxList,Map<String, String> params){
		String jiessjMaoxq = params.get("nextjiessj");
		if(this.getWorkCalendar().get(params.get("chanxzbh"))!=null){
			List<String> rl = this.getWorkCalendar().get(params.get("chanxzbh"));
			for(int i  = 0;i<rl.size();i++){
				String r = rl.get(i);
				if(params.get("kaissj").equals(r)){
					if(i+3<rl.size()){
						jiessjMaoxq = rl.get(i+3);
						break;
					}
				}
			}
		}
		params.put("jiessjMaoxq", jiessjMaoxq);
		//查询排产日期之后四天内需要排产的零件列表，总的毛需求///
		Map<String, Map<String,String>> riLingjpzMap = new HashMap<String, Map<String,String>>();
		List<Map<String, String>> chanxzmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryChanxzLingjMaox",params);
		for(Map<String, String>chanxzmxqMap:chanxzmxq){
			String lingjbh = chanxzmxqMap.get("LINGJBH");
			riLingjpzMap.put(lingjbh, chanxzmxqMap);
		}
		this.setRiLingjpz(riLingjpzMap);
		
		//查询排产日期之后四天内需要排产的零件列表，总的毛需求///
		Map<String, Map<String,Map<String,String>>> riScxLingjpzMap = new HashMap<String, Map<String,Map<String,String>>>();
		List<Map<String, String>> shengcxLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryShengcxLingj",params);
		for( Map<String, String> shengxListMap: shengxList){
			riScxLingjpzMap.put(shengxListMap.get("SHENGCXBH"), new HashMap<String,Map<String,String>>());
		}
		String scx = "";
		String lingjbh = "";
		for(Map<String, String>shengcxLingjMap:shengcxLingj){
			scx = shengcxLingjMap.get("SHENGCXBH");
			lingjbh = shengcxLingjMap.get("LINGJBH");
			riScxLingjpzMap.get(scx).put(lingjbh, shengcxLingjMap);
		}
		this.setRiScxLingjpz(riScxLingjpzMap);
	}

	/**
	 * @description 计算零件按照包装取整
	 * @author 王国首
	 * @date 2011-12-30
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return gongShi	考虑最小时间单位后的工时
	 */
	public Map<String,String> getQckcPc(List<Map<String,String>> tempQckc,String lingjbh){
		Map<String,String> q =  null;
		for( Map<String,String> tempQckcMap :tempQckc){
			if(lingjbh.equals(tempQckcMap.get("LINGJBH"))){
				return tempQckcMap;
			}
		}
		return q;
	}
	
	/**
	 * @description 更新期初库存，安全库存
	 * @author 王国首
	 * @date 2012-3-16
	 * @param params	 包含产线和排产时间范围
	 * @return result  班次列表
	 */
	public void updateMeiRiQckc(Map<String, String> params,List<Map<String,String>> tempQckc){
		for(Map<String, String> tempQckcMap:tempQckc){
			tempQckcMap.put("SHIJ", params.get("kaissjPC"));
			tempQckcMap.put("biaos", params.get("biaos"));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiQckc",tempQckcMap);
		}
	}
	
	/**
	 * @description 更新每日毛需求
	 * @author 王国首
	 * @date 2012-3-16
	 * @param params	 包含产线和排产时间范围
	 * @return result  班次列表
	 */
	public void calDailyShengcxAfter(Map<String, String> params){
		List<Map<String, String>> daily = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryRIGDPCJHMXData",params);
		for(Map<String, String> dailyMap:daily){
			if(getBlockWorkCalendar().get(params.get("shengcxCX")).get(dailyMap.get("SHIJ")) != null){
				dailyMap.put("ZHUANGT", "2");
			}else{
				dailyMap.put("ZHUANGT", "3");
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateMeiRiZhuangt",dailyMap);
		}
	}
}
