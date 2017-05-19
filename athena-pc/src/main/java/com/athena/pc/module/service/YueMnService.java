package com.athena.pc.module.service;
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:月模拟计划
 * </p>
 * <p>
 * Description:按照选择模拟工业周期进行月模拟排产，同时系统进行每日排产
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
public class YueMnService extends AbstractMonpcTemplate {
	

	
	@Inject
	private EquilibriaSCXService equilibriaSCXService; 
	/*特殊时间map*/
	private Map<String, String> teSuTime;
	
	public Map<String, String> getTeSuTime() {
		return teSuTime;
	}

	public void setTeSuTime(Map<String, String> teSuTime) {
		this.teSuTime = teSuTime;
	}
	
	@Override
	public void parseRiMargin(List<Map<String, String>> shengxList,
			Map<String, String> params) {
	}
	
	/**
	 * @description 解析毛需求,PP类型的订单明细
	 * @author 余飞
	 * @date 2012-1-10
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void parseMaoxq(List<Map<String, String>> shengxList,
			Map<String, String> params) {
		Map<String, String> map = new HashMap<String,String>();
		map.putAll(params);
		map.put("Dingdlx", "PP");
		map.put("DingdlxN", "NP");
		map.put("jiessj", params.get("nextjiessj"));
		/*查询出PP订单的客户提前期*/
		Map<String, String> maoParam = getOrderAheadOfTime(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQParam",map));
		maoParam.putAll(params);
		maoParam.put("Dingdlx", "PP");
		maoParam.put("DingdlxN", "NP");
		/*查询出PP订单的既定的数据*/
		List<Map<String,String>> requirements = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQ",map);
		Map<String,String> dingdhGys = this.saveMaxDingdh(requirements);
		parseRequirements(maoParam, requirements);
		updateXuQState(maoParam,requirements);
		/*查询出PP订单中，不同供应商最新的订单号*/
		List<Map<String, String>> dingdMax = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMaoXQNewDingdh",maoParam);
		String flag = "";
		/*循环用户中心，得到不同用户中心不用供应商最新的订单号*/
		for(Map<String, String> dingdMap : dingdMax){
			if(dingdhGys.get(dingdMap.get("DINGDH"))!=null && dingdhGys.get(dingdMap.get("DINGDH")).length()>0){
				params.put("DINGDH", params.get("DINGDH")+flag+"'"+dingdMap.get("DINGDH")+"'");
				flag = ",";
			}
		}
	} 

	/**
	 * @description 解析外部订单预告
	 * @author 王国首
	 * @date 2012-3-28
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void parseMaoxqWbddyg(List<Map<String, String>> shengxList,
			Map<String, String> params) {
		Map<String, String> map = new HashMap<String,String>();
		map.putAll(params);
		/*查询出外部订单预告的客户订单提前期*/
		Map<String, String> maoParam = getOrderAheadOfTime(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryWbddygMaoXQParam",map));
		maoParam.putAll(params);
		maoParam.put("Dingdlx", "WBDDYG");
		List<Map<String,String>> requirements = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryWbddygMaoXQ",map);
		parseRequirements(maoParam, requirements);
		updateXuQState(maoParam,requirements);
	} 
	
	/**
	 * @description 将零件每日需求汇总插入到零件日需求汇总表
	 * @author 王国首
	 * @date 2012-1-4
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@Override
	public void statDailyLingj(List<Map<String, String>> shengxList,
			Map<String, String> params) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertLingjrxqhzb",params);
	}

	/**
	 * @description 进行月度模拟计划排产，滚动月模拟
	 * @author 王国首
	 * @date 2011-12-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		期初库存
	 */
	public void calYueMoN(List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String,String>> qckc){
		List<Map<String,String>> qckcJXQ = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = new ArrayList<Map<String,String>>();
		List<Map<String,String>> qckcDaily = new ArrayList<Map<String,String>>();
//		List<Map<String,String>> beicjh = new ArrayList<Map<String,String>>();
		/*得到零件的安全库存数量或者安全库存天数*/
		Map<String, String> anqkcMap = calAnqkc(params);
		/*得到零件计算开始时间的期初库存*/
		logger.info("模拟排产计算期初库存");
		tempQckc = qiChuKuCun(params,qckc);
		calBeicjh(params,tempQckc);
		/*将没有拆分完的需求使用期初库存减去*/
		calQckcMinusXQ(params,tempQckc);
		/*将计算出的零件的安全库存设置到期初库存列表中*/
		tempQckc = setAnqkcToQckc(params,tempQckc,anqkcMap);
		List<Map<String, String>> chanxzrl = getChanxzWorkCalendar(params);
		/*查询出排产时间范围内的特殊时间日期，特殊时间*/
		setTeSuTime(getTeSuTime(params));
		/*计算排产时间范围内，每个工作日加上提前期后，对应的加上提前期的工作日历*/
		setAheadPeriod(calAddTiqqRil(params,chanxzrl));
		Map<String, List<String>> chanxzWC = getChanxzWeekWorkCalendar(params,chanxzrl);
		if("G".equals(params.get("biaos"))){
			/*解析滚动月模拟产线的封闭期*/
			setBlockWorkCalendar(parseBlockWorkCalendar(params,shengxList,chanxzWC));		
		}else{
			setBlockWorkCalendar(new HashMap<String, Map<String,String>>());
			/*插入月度模拟计划表*/
			insertYuedmnjhb(shengxList,params);
		}
		final Iterator<String> it = chanxzWC.keySet().iterator(); 
		while(it.hasNext()){
			setWeekCal(true); 
			List<String> weekRil =(ArrayList<String>)chanxzWC.get(it.next());
			params.put("kaissjWeeK", weekRil.get(0).toString());
			params.put("jiessjWeeK", weekRil.get(weekRil.size()-1).toString());
			qckcJXQ = listMapCopy(tempQckc);
			qckcDaily = listMapCopy(tempQckc);
			setAheadJXQ(new HashMap<String,List<Map<String,String>>>());
			/*计算每日净需求*/
			logger.info("模拟排产计算每日净需求");
			calDailyJXQ(shengxList, params,qckcJXQ);
			/*计算产线每日净需求*/
			logger.info("模拟排产计算产线净需求");
			calDailyCXJXQ(shengxList, params);
			/*计算每条生产线的最大平均工时*/
			Map<String, String> avgMap = calWeekAvgHours(shengxList,params);
			//test gswang begin
			List<Map<String,String>> avgList = new ArrayList<Map<String,String>>();
			avgList.add(avgMap);
			String logString = this.getListMapKeyValue(avgList);
			logger.info("最大平均工时："+params.get("kaissjWeeK")+" "+ logString);	
			//test end
			/*更具最大平均工时进行每日排产*/
			logger.info("模拟排产计算每日排产");
			qckcDaily = calDailyTime(shengxList,params,qckcDaily,avgMap,weekRil);
			Map<String, Object> paramsJH= new HashMap<String, Object>();
			paramsJH.put("params",params);
			paramsJH.put("shengx",shengxList);
			paramsJH.put("qckc",tempQckc);
			paramsJH.put("workCalendar",workCalendar);

			equilibriaSCXService.equilibData(paramsJH);
		}
		if(qckc != null){
			qckc.clear();
		}
		qckc.addAll(tempQckc);
		if("Y".equals(params.get("biaos"))){
			/*判断做月模拟时，计算包装消耗，滚动月模拟时，不计算包装消耗*/
			logger.info("模拟排产计算包装消耗");
			calBaozxh(shengxList, params);
		}	
	}

	/**
	 * @description 模拟排产前数据初始化，删除中间表数据和上次的排产计划
	 * @author 王国首
	 * @date 2012-1-7
	 * @param params	 包含产线和排产时间范围
	 */
	@Override
	public void cleanInitData(Map<String, String> params) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteLingjrxqb",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteLingjrxqhzb",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteChanxljhzb",params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteMessage",params);
//		if(params.get("GUND")!= null && "G".equals(params.get("GUND"))){
		if("G".equals(params.get("biaos"))){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteGundyMonrgdljclb",params);	
			deleteCloseCalendarMX(params);
		}else{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteMonrgdljclb",params);	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteYuedmnjhmx",params);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteYuedmnjhb",params);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteBaozxh",params);	
		}
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
		paramsTemp.put("kaissj", paramsTemp.get("kaissjWeeK"));
		paramsTemp.put("jiessj", paramsTemp.get("jiessjWeeK"));
		List<Map<String, String>> gongzrlChanxzList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrlChanxz",paramsTemp);
		for(Map<String, String> gongzrlChanxz : gongzrlChanxzList){
			paramsTemp.put("JSSHIJ", gongzrlChanxz.get("RIQ").toString());
			paramsTemp.put("TQSHIJ", getAheadPeriod().get(gongzrlChanxz.get("RIQ")).toString());
			Map<String,List<Map<String, String>>> temp = calOneDayJXQ( gongzrlChanxz, tempQckc ,paramsTemp);
			List<Map<String, String>> lingjJinxuqiuUpdate = temp.get("Jinxuqiu");
			tempQckc = temp.get("beginKucList");
			updateDailyJXQ(lingjJinxuqiuUpdate);
			//test
			if(isWeekCal()){
				String logString = this.getListMapKeyValue(lingjJinxuqiuUpdate);
				logger.info("净需求："+gongzrlChanxz.get("RIQ")+" "+ logString);				
			}

		}
		return tempQckc;
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
	 * @description 计算每个零件的期初库存
	 * @author 王国首
	 * @date 2011-12-20
	 * @param params	 包含产线和排产时间范围
	 * @param qckc	 	 期初库存
	 * @return List<Map<String, String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> qiChuKuCun(Map<String, String> params,List<Map<String,String>> qckc){
		/*查询出产线下所有零件的期初库存*/
		List<Map<String, String>> beginKucList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryFirstDayBeginKuc",params);
		/*如果从外部来的期初库存参数不等于NULL，则期初库存取外部送入的期初库存作为参数*/
		if(qckc != null && qckc.size()>0){
			for(int i = 0;i<beginKucList.size();i++){
				Map<String, String> temp = beginKucList.get(i);
				for(Map<String, String> qckcMap : qckc){
					if(temp.get("LINGJBH").equals(qckcMap.get("LINGJBH"))){
						temp.putAll(qckcMap);
						beginKucList.set(i, temp);
						break;
					}
				}
			}
		}
		return beginKucList;
	}
	
	/**
	 * @description 将每日净需求拆分到产线每日净需求，并比较经济批量
	 * @author 王国首
	 * @date 2011-12-22
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public void calDailyCXJXQ(List<Map<String, String>> shengxList,Map<String, String> params) {
		Map<String, String> paramsTemp = new HashMap<String, String>(); 
		paramsTemp.putAll(params);
		paramsTemp.put("kaissj", params.get("kaissjWeeK"));
		paramsTemp.put("jiessj", params.get("jiessjWeeK"));
		List<Map<String, String>> gongzrlChanxzList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongzrlChanxz",paramsTemp);
		paramsTemp.put("MINSJ", params.get("kaissj"));
		paramsTemp.put("MAXSJ", params.get("jiessj"));
		List<Map<String, String>> chanxLingjHz = new ArrayList<Map<String, String>>();
		Map<String, Map<String,String>> workCalendar = getWorkCalendarMap(paramsTemp);
		String lStrChanx = "";
		String gongzrl = "";
//		Map<String,List<Map<String,String>>> aheadJXQ = new HashMap<String,List<Map<String,String>>>();
		if(isWeekCal()){
			calDailyCXJXQAhead (shengxList,params,gongzrlChanxzList,workCalendar,getAheadJXQ());
		}
		for(Map<String, String> gongzrlChanxz : gongzrlChanxzList){
			gongzrl = gongzrlChanxz.get("RIQ");
			lStrChanx = getDailyWorkChanx(gongzrl,shengxList,workCalendar);
			paramsTemp.put("shengcx", lStrChanx);
			paramsTemp.put("RIQ", gongzrl);
			chanxLingjHz = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryChanxLingjHz",paramsTemp);
			getDailyChanxJxq(paramsTemp,chanxLingjHz);
		}
	}
	
	/**
	 * @description 判断一周内是否有零件在某一天没有生产线去生产，将此零件前提到这周前面工作日生产。
	 * @author 王国首
	 * @date 2011-12-22
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public void calDailyCXJXQAhead (List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String, String>> gongzrlChanxzList,
			Map<String, Map<String,String>> workCalendar,Map<String,List<Map<String,String>>> aheadJXQ) {
		List<Map<String, String>> gongzrllast = new ArrayList<Map<String, String>>();
		Map<String,List<Map<String, String>>> riShengcx = new HashMap<String,List<Map<String, String>>>();
		for(Map<String, String> gongzrlChanxz : gongzrlChanxzList){
			List<Map<String, String>> shengx = listMapCopy(shengxList);
			String gongzrl = gongzrlChanxz.get("RIQ");
			params.put("shij", gongzrl);
			boolean flag = true;
			for(Map<String, String> shengxListMap : shengx){
				if(workCalendar.get(shengxListMap.get("SHENGCXBH")).get(gongzrl) == null){
					flag = false;
					shengxListMap.put("flag", String.valueOf(false));
				}else{
					shengxListMap.put("flag", String.valueOf(true));
				}
			}
			riShengcx.put(gongzrl, shengx);
			gongzrlChanxz.put("flag", String.valueOf(flag));
			if(!flag){
				List<Map<String, String>> chanxLingjHz = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.selectLingjrxqhzbAhead",params);
				for(Map<String, String> chanxLingjHzMap : chanxLingjHz){
					List<Map<String, String>> lingjshengcx = getLingjOfLine().get(chanxLingjHzMap.get("LINGJBH"));
					if(lingjshengcx == null){
						continue;
					}
					boolean ljflag = false;
					for( Map<String, String> lingjshengcxMap : lingjshengcx){
						for(Map<String, String> shengxListMap : shengx){
							if(lingjshengcxMap.get("SHENGCXBH").equals(shengxListMap.get("SHENGCXBH")) && Boolean.valueOf(shengxListMap.get("flag"))){
								ljflag = true;
							}
						}
					}
					if(!ljflag){
						List<Map<String, String>> jxq = new ArrayList<Map<String, String>>();
						Map<String, String> jxqMap = new HashMap<String, String>();
						jxqMap.putAll(chanxLingjHzMap);
						jxqMap.put("date", gongzrl);
						jxqMap.put("ljsl", String.valueOf((-1)*Integer.parseInt(String.valueOf(jxqMap.get("JINXQ")))));
						jxq.add(jxqMap);
						aheadJXQ.put(gongzrl, jxq);
						calAheadCXJXQ(riShengcx,params,chanxLingjHzMap,gongzrllast,aheadJXQ);
					}
				}
			}
			gongzrllast.add(gongzrlChanxz);
		}
		
	}
	
	/**
	 * @description 将当前没有生产线生产的零件提前到前面生产。
	 * @author 王国首
	 * @date 2012-06-19
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public void calAheadCXJXQ(Map<String,List<Map<String, String>>> shengx,Map<String, String> params,
			Map<String, String> chanxLingjHzMap,List<Map<String, String>> gongzrllast,Map<String,List<Map<String,String>>> aheadJXQ) {
		List<String> rili = new ArrayList<String>();
		if(params.get("shij").compareTo(params.get("kaissjWeeK"))>0){
			for( Map<String, String> gongzrllastMap : gongzrllast){
				String gongzrl = gongzrllastMap.get("RIQ");
				List<Map<String, String>> riScx = shengx.get(gongzrl);
				List<Map<String, String>> lingjshengcx = getLingjOfLine().get(chanxLingjHzMap.get("LINGJBH"));
				for( Map<String, String> lingjshengcxMap : lingjshengcx){
					for(Map<String, String> shengxListMap : riScx){
						if(lingjshengcxMap.get("SHENGCXBH").equals(shengxListMap.get("SHENGCXBH")) && Boolean.valueOf(shengxListMap.get("flag"))){
							rili.add(gongzrl);
						}
					}
				}
			}
			if(rili.size()>0){
				int lingjsl = Integer.parseInt(String.valueOf(chanxLingjHzMap.get("JINXQ")));
				final int days = rili.size();
				final int lingjslAvg = lingjsl / days;
				int mod = lingjsl - (lingjslAvg * days);
				for (String aCal : rili) {
					Map<String, String> temp = new HashMap<String, String>();
					temp.putAll(chanxLingjHzMap);
					temp.put("date", aCal);
					if (mod > 0) {
						mod--;
						temp.put("ljsl", String.valueOf(lingjslAvg + 1));
					} else {
						temp.put("ljsl", String.valueOf(lingjslAvg));
					}
					List<Map<String, String>> jxq = null;
					if(aheadJXQ.get(aCal) != null){
						jxq = aheadJXQ.get(aCal);
					}else{
						jxq = new ArrayList<Map<String, String>>();
					}
					jxq.add(temp);
					aheadJXQ.put(aCal, jxq);
				}	
			}
		}
	}
	
	/**
	 * @description 计算这一天中每个零件的净需求，计算完并插入数据库
	 * @author 王国首
	 * @date 2011-12-24
	 * @param chanxLingjHz   	一天中开启产线零件汇总数据（包括生成比例，是否取经济批量，经济批量）
	 * @return  null	
	 */
	public void getDailyChanxJxq(Map<String, String> params,List<Map<String, String>> chanxLingjHz){
		for(int i = 0;i<chanxLingjHz.size();i++){
			Map<String, String> chanxLingj = chanxLingjHz.get(i);
			String shengcxbh = "";
			for( Map<String, String> shengcMap : getLingjOfLine().get(chanxLingj.get("LINGJBH"))){
				if(parseShengcxWork(params.get("RIQ"),shengcMap.get("SHENGCXBH"))){
					shengcxbh = shengcMap.get("SHENGCXBH");
					break;
				}
			}
			if("".equals(shengcxbh)){
				break;
			}
			chanxLingj.put("SHENGCXBH", shengcxbh);
			/*计算产线的净需求*/
			double jinxuqChanx =Double.valueOf(String.valueOf(chanxLingj.get("JINXQ"))) ;
			if(isWeekCal() && getAheadJXQ().get(params.get("RIQ")) != null && getAheadJXQ().get(params.get("RIQ")).size()>0){
				for( Map<String,String> aheadJXQMap : getAheadJXQ().get(params.get("RIQ"))){
					if(chanxLingj.get("LINGJBH").equals(aheadJXQMap.get("LINGJBH"))){
						jinxuqChanx = jinxuqChanx + Double.valueOf(String.valueOf(aheadJXQMap.get("ljsl")));
					}
				}
			}
			chanxLingj.put("chanxLingj", String.valueOf(jinxuqChanx));
			chanxLingj.put("biaos", params.get("biaos"));
			chanxLingjHz.set(i, chanxLingj);
		}
		
		//test 
		String test = "";
		if(chanxLingjHz.size()>0 && isWeekCal()){
			test = chanxLingjHz.get(0).get("SHENGCXBH")+" "+chanxLingjHz.get(0).get("SHIJ");
			String logString = this.getListMapKeyValue(chanxLingjHz);
			logger.info("产线净需求："+test + " "+ logString);
		}

		if(isWeekCal()){
			//使用批量插入数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertCangxljhzb",chanxLingjHz);
//			for( Map<String,String> temp: chanxLingjHz){
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertCangxljhzb",temp);
//			}
		}
		else{
			//使用批量更新数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.updateCangxljhzb",chanxLingjHz);
//			for( Map<String,String> temp: chanxLingjHz){
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateCangxljhzb",temp);
//			}	
		}

	}
	
	/**
	 * @description 计算一周的最大平均工时
	 * @author 王国首
	 * @date 2011-12-28
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param workCalendar	 最大时间范围的每条产线的工作日历
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> calWeekAvgHours(List<Map<String, String>> shengxList,Map<String, String> params) {
		Map<String,String> avgMap = new HashMap<String,String>();
//		double mintime =  Double.valueOf(params.get("MINTIME"));
		for(Map<String, String> shengcx : shengxList){
			String shengcxbh = shengcx.get("SHENGCXBH");
			params.put("shengcxWeek", shengcxbh);
			List<String> shengcxCal = workCalendar.get(shengcxbh);
			List<String> subCal = getWorkSubCal(shengcxCal,params.get("kaissjWeeK"),params.get("jiessjWeeK"));
			List<Map<String, String>> chanxlj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryChanxLjWeek",params);  
			Map<String, List<String>> result = convertListToMapList(chanxlj,"SHIJ","SHENGCSHIJ");
			
			String logString = this.getListMapKeyValue(chanxlj);
			logger.info("最大平均工时产线零件数量："+" "+ logString);
			
			double avg = 0;
			double totle = 0;
			for(int i = 0;i< subCal.size()&&result.size()>0;i++){
				String riq = subCal.get(i);
				/*如果当天属于特殊时间，计算当天工时时，需要加上特殊时间*/
				totle = getTeSuTime().get(riq) != null ? totle+ Double.valueOf(getTeSuTime().get(riq)) : totle;
				if(result.get(riq) != null){
					for(String  shengcsj: result.get(riq)){
						totle = totle + Double.valueOf(String.valueOf(shengcsj));
					}					
				}
				avg = totle/(i+1)>avg?totle/(i+1):avg;
			}
			avg = getMinTime(Double.valueOf(String.valueOf(params.get("MINROUND"))),avg);
//			/*判断最大平均工时是否大于22小时，如果大于22小时，则终止计算，并提示告诉计划员*/
//			if(avg>22){
//				String xiaox = "产线"+shengcxbh+", "+params.get("kaissjWeeK")+"本周最大平均工时大于22小时,实际工时为"+avg+"小时";
//				this.addMessage(params,yueMnjhh.get(shengcxbh),params.get("USERCENTER"),shengcxbh,params.get("kaissjWeeK"),xiaox,"9");
//				insertMessage();
//				throw new PCRunTimeException(new Message("bigAvgTimeGreat.error","i18n.pc.pc").getMessage());	//gswang-test
//			}
//			/*判断最大平均工时是否小于8小时，如果小于8小时，则将最大平均工时提高到8小时，并将此信息提示告诉计划员*/
//			if(avg<8){
//				String xiaox = "产线"+shengcxbh+", "+params.get("kaissjWeeK")+"本周最大平均工时小于8小时,实际工时为"+avg+"小时";
//				this.addMessage(params,yueMnjhh.get(shengcxbh),params.get("USERCENTER"),shengcxbh,params.get("kaissjWeeK"),xiaox,"3");
//				avg = 8;
//			}
			avgMap.put(shengcxbh, String.valueOf(avg));
		}
		return avgMap;
	}
	
	/**
	 * @description 将数据库查询出来的List<Map<String, String>>转换为Map<String, List<String>>
	 * @author 王国首
	 * @date 2011-12-29
	 * @param souce 	需要转换的list，
	 * @param keyName	keyName 所对应的值将作为Map的KEY
	 * @param valueName	valueName 所对应的值将作为Map的value
	 */
	public Map<String, List<String>> convertListToMapList(List<Map<String, String>> souce,String keyName,String valueName){
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for( Map<String, String> souceMap: souce){
			List<String> temp = null;
			if (result.containsKey(souceMap.get(keyName))) {
				temp = result.get(souceMap.get(keyName));
			} else {
				temp = new ArrayList<String>();
			}
			temp.add(String.valueOf(souceMap.get(valueName)));
			result.put(String.valueOf(souceMap.get(keyName)), temp);
		}
		return result;
	}
	
	/**
	 * @description 计算一周的最大平均工时
	 * @author 王国首
	 * @date 2011-12-30
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 * @param workCalendar	 最大时间范围的每条产线的工作日历
	 * @return Map	 计划时间内产线组开启的日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<String>> getChanxzWeekWorkCalendar(Map<String,String> params,List<Map<String, String>> cals){
		Map<String,List<String>> result = new TreeMap<String,List<String>>();
		for (Map<String, String> shengcxCal : cals){
			List<String> temp = null;
			String nianzx = shengcxCal.get("NIANZX");
			if(nianzx.length()==1){
				nianzx = shengcxCal.get("RIQ").substring(0,4) + "0" + nianzx;
			}else if(nianzx.length()==2){
				nianzx = shengcxCal.get("RIQ").substring(0,4) + nianzx;
			}
			if (result.containsKey(nianzx)) {
				temp = result.get(nianzx);
			} else {
				temp = new ArrayList<String>();
			}				
			temp.add(shengcxCal.get("RIQ"));
			result.put(nianzx, temp);
		} 
		return result;
	}

	/**
	 * @description 计算计划时间内的特殊时间
	 * @author 王国首
	 * @date 2011-12-31
	 * @param params	 包含产线和排产时间范围
	 * @return gongShi	产线组下每条产线考虑最小时间单位后的工时
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getTeSuTime(Map<String,String> params){
		Map<String,String> teSuTimeTemp = new HashMap<String,String>();
		List<Map<String, String>> cals = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryTeSuTime",params);  
		String xingq = "";
		for (Map<String, String> dataMap : cals){
			double time = 0;
			xingq = dataMap.get("XINGQ");
			/*将产线一天的特殊时间累加起来*/
			time = String.valueOf(dataMap.get("TESSJXQ1")).equals(xingq)?time + Double.valueOf(String.valueOf(dataMap.get("TESSJXS1"))):time;
			time = String.valueOf(dataMap.get("TESSJXQ2")).equals(xingq)?time + Double.valueOf(String.valueOf(dataMap.get("TESSJXS2"))):time;
			time = String.valueOf(dataMap.get("TESSJXQ3")).equals(xingq)?time + Double.valueOf(String.valueOf(dataMap.get("TESSJXS3"))):time;
			teSuTimeTemp.put(dataMap.get("RIQ"), String.valueOf(time));
		} 
		return teSuTimeTemp;
	}

	/**
	 * @description 计算每日工时
	 * @author 王国首
	 * @date 2012-1-4
	 * @param shengxList 	产线列表
	 * @param params	 	包含产线和排产时间范围
	 * @param workCalendar	 最大时间范围的每条产线的工作日历
	 * @param qckc			期初库存
	 * @param avgMap		每条产线一星期平均工时
	 * @param weekRil		一周内产线组工作日历
	 * @return qckc	 		最新的期初库存
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Map<String,String>> calDailyTime(List<Map<String, String>> shengxList,Map<String, String> params,List<Map<String,String>> qckc,Map<String,String> avgMap,List<String> weekRil) {
		Map<String, String> paramsTemp = new HashMap<String, String>();
		List<Map<String,String>> tempQckc =  new ArrayList<Map<String,String>>();
		tempQckc.addAll(qckc);
		paramsTemp.putAll(params);
		/*定义一个标识，标识第一天排产完后，一周后面每天需要重新计算净需求和产线净需求*/
		boolean firstDayFlag = false;
		for(String riq : weekRil){
			if(firstDayFlag){
				this.setWeekCal(false);
				Map<String, String> paramsJxqTemp = new HashMap<String, String>();
				paramsJxqTemp.putAll(params);
				paramsJxqTemp.put("kaissjWeeKAhead", params.get("kaissjWeeK"));
				paramsJxqTemp.put("kaissjWeeK", riq);
				paramsJxqTemp.put("jiessjWeeK", riq);
				List<Map<String,String>> qckcJXQ = new ArrayList<Map<String,String>>();
				qckcJXQ = listMapCopy(tempQckc);
				calDailyJXQ(shengxList, paramsJxqTemp,qckcJXQ);
				calDailyCXJXQ(shengxList, paramsJxqTemp);
			}
			firstDayFlag = true;
			paramsTemp.put("kaissjPC", riq);
			paramsTemp.put("shijNext", getNextWorkDay(paramsTemp,paramsTemp.get("chanxzbh"),riq));
			double gongs = 0;
			List<Map<String, String>> lingjri = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryDailyLingj",paramsTemp);
			calQckc(tempQckc,lingjri);
			for(Map<String, String> shengcx : shengxList){
				String shengcxbh = shengcx.get("SHENGCXBH");
				paramsTemp.put("EVERYTIME", shengcx.get("EVERYTIME"));
				//test 
				String logString = this.getListMapKeyValue(qckc);
				logger.info("期初库存："+riq+" " +shengcxbh +" "+ logString);
				
				double avgTime = Double.valueOf(avgMap.get(shengcxbh));
				paramsTemp.put("shengcxCX", shengcxbh);  
				/*查询出一天，一条产线下零件的产线净需求*/
				List<Map<String, String>> chanxlj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryDailyChanxLj",paramsTemp);  
				List<Map<String, String>> kuCunXiShu = new ArrayList<Map<String,String>>();
				double totle = 0;
				double testime = 0;
				/*如果当天有特殊时间需要计算，需要得到当天的特殊时间*/
				testime = getTeSuTime().get(riq) != null ? Double.valueOf(getTeSuTime().get(riq)) : 0;
				for(int i = 0;i<chanxlj.size();i++){
					Map<String, String> chanxljMap = chanxlj.get(i);
					/*计算零件每日工时时，需要和最小时间单位比较，零件的工时需要满足最小时间单位*/
					double lingjsj = Double.valueOf(String.valueOf(chanxljMap.get("SHENGCSHIJ")));
					double ljsl = Double.valueOf(String.valueOf(chanxljMap.get("LINGJSL")));
					double jjpl = Double.valueOf(String.valueOf(chanxljMap.get("JINGJPL")));
					double shengcjp = Double.valueOf(String.valueOf(chanxljMap.get("SHENGCJP")));
					jjpl = jjpl>0?jjpl:shengcjp;
					if(ljsl>0 && jjpl>ljsl){
						for(int ia = 0;ia<tempQckc.size();ia++){
							Map<String, String> ianq = tempQckc.get(ia);
							if(ianq.get("LINGJBH").equals(chanxljMap.get("LINGJBH"))){
								ianq.put("QCKC",this.getBigDecimal(ianq.get("QCKC")).add(this.getBigDecimal(jjpl-ljsl)).toString());
								break;
							}
							logger.info("小品种零件："+ianq.get("LINGJBH")+"   "+ianq.get("QCKC"));
						}
						chanxljMap.put("LINGJSL", String.valueOf(jjpl));
						lingjsj = jjpl/shengcjp;
						chanxljMap.put("SHENGCSHIJ", String.valueOf(lingjsj));
					}
					totle = totle + lingjsj;
					chanxljMap.put("SHENGCSHIJ", String.valueOf(lingjsj));
//					chanxljMap.put("LINGJSL", String.valueOf(Math.ceil(lingjsj*Double.valueOf(String.valueOf(chanxljMap.get("SHENGCJP"))))));	//gswang 向上取整
					chanxlj.set(i, chanxljMap);
				}
//				if(totle + testime > avgTime){
//					/*当排产时间大于平均工时时，增加一条警告信息，提示给用户*/
//					checkPaicTime(params,totle,testime,avgTime,shengcxbh,riq);
//				}
//				else{
					/*得到工作编号，工作编号由 用户中心+生产线编号+年+月+日组成*/
					String uid = params.get("USERCENTER") + paramsTemp.get("shengcxCX") + riq.substring(0,4) + riq.substring(5,7) + riq.substring(riq.length()-2);
					
					//test 
					logString = this.getListMapKeyValue(kuCunXiShu);
					logger.info("库存系数："+riq+" " +shengcxbh +" "+ logString);
					Map<String,Map<String,String>> zengc = new HashMap<String,Map<String,String>>();
					/*当零件的最大平均工时大于当日排产量加特殊时间时，需要增产*/
					if(avgTime - testime - totle>0){
						zengc = calZengchan(paramsTemp,tempQckc,chanxlj,shengcxbh,avgTime - testime - totle);
					}
					/*月模拟每日排产数量等于当日产线零件数量加上增产数量*/
					tempQckc = calYueMnPC(paramsTemp,tempQckc,chanxlj,zengc,uid);
//				}
			}
		}
		return tempQckc;
	}
	
	/**
	 * @description 当每日工时小于平均工时时进行增产
	 * @author 王国首
	 * @date 2012-1-7
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param params	 一条产线当天的库存系数，已经进行小到大排序
	 * @param shengcx	生产线编号
	 * @param zengcsj	需要增产的工时
	 * @return zengc	增产的零件数量和增产的工时
	 */
	public Map<String,Map<String,String>> calZengchan(Map<String,String> params,List<Map<String,String>> qckc,List<Map<String, String>> chanxlj,String shengcx,double zengcsj){
		Map<String,Map<String,String>> zengc = new HashMap<String,Map<String,String>>();
		double zengChanShiJ = zengcsj;
		double everytime = Double.parseDouble(String.valueOf(params.get("EVERYTIME")));
		List<Map<String,String>> kuCunXiShu = new ArrayList<Map<String,String>>();
		/*当增产时间大于0时，需要增产*/
		if(zengChanShiJ>0){
//			List<String> shengcxCal = getWorkCalendar().get(shengcx);
			int i = 0;
			while(zengChanShiJ>everytime && i<=1000){
				kuCunXiShu = calkuCunXiShu(qckc,chanxlj,params.get("shengcxCX"));
				i++;
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
							}else if(mintime<jingjpl/shengcjp ){
								zengcsl = Math.round(shengcjp*mintime);
							}else{
								zengcsl = jingjpl;
							}
						}else{
							zengcsl = anqkctop-qckcsl;
						}
						if(zengcsl/shengcjp>zengChanShiJ){
							zengcshij = zengChanShiJ;
							zengcsl = Math.round(zengChanShiJ*shengcjp);
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
				calQckc(qckc,addqckcList);
				if(zengChanShiJ<=0){break;}
			}
			
//			if(zengChanShiJ>0){
//				zengChanShiJ = calZengchanNext(params,zengc,chanxlj,shengcx,zengChanShiJ);
//			}
			/*当增产完所有零件，但是需要增产的时间任然大于0，则增产零件不足，需要将此消息提示给计划员*/
			if(zengChanShiJ>everytime){
				checkZengcNotEnough (params,shengcx,params.get("USERCENTER"),params.get("kaissjPC"),this.getRound(zengcsj, 2),this.getRound(zengcsj-zengChanShiJ, 2),"2");
			}
		}
		return zengc;
	}
	
	/**
	 * @description 当每日工时小于平均工时时进行增产
	 * @author 王国首
	 * @date 2012-1-7
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param params	 一条产线当天的库存系数，已经进行小到大排序
	 * @param shengcx	生产线编号
	 * @param zengcsj	需要增产的工时
	 * @return zengc	增产的零件数量和增产的工时
	 */
/*	public double calZengchanNext(Map<String,String> params,Map<String,Map<String,String>>zengc,List<Map<String, String>> chanxlj,String shengcx,double zengcsj){
		String lingjString = CollectionUtil.chanxListToString(chanxlj,"LINGJBH");
		if(lingjString.length()>0){
			params.put("lingjString", lingjString);
		}else if(params.get("lingjString") != null){
			params.remove("lingjString");
		}
		List<Map<String, String>> zengcLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryZengcljslNext",params);  
		double zengChanShiJ = zengcsj;
		//当增产时间大于0时，需要增产
		if(zengChanShiJ>0){
			for(Map<String,String> zengcLingjMap : zengcLingj ){
				String lingjbh = zengcLingjMap.get("LINGJBH");
				double zengcljsj = 0;
				double shengcjp = Double.parseDouble(String.valueOf(zengcLingjMap.get("SHENGCJP")));
				zengcljsj = getMinTime(Double.valueOf(params.get("MINTIME")),Double.parseDouble(String.valueOf(zengcLingjMap.get("SHENGCSHIJ")))); 
				zengcljsj = zengcljsj>=zengChanShiJ? zengChanShiJ : zengcljsj ;
				zengChanShiJ = zengcljsj>=zengChanShiJ ? 0 : zengChanShiJ - zengcljsj;
				if(zengc.get(lingjbh) != null){
					zengcLingjMap = zengc.get(lingjbh);
					zengcljsj = zengcljsj + Double.parseDouble(String.valueOf(zengcLingjMap.get("ZENGCLJSJ")));
				}
				zengcLingjMap.put("ZENGCLJSJ", String.valueOf(zengcljsj));
				zengcLingjMap.put("ZENGCSL", String.valueOf(Math.ceil(shengcjp*zengcljsj)));
				zengc.put(lingjbh, zengcLingjMap);
				boolean flag = false;
				for(Map<String, String> chanxljMap : chanxlj){
					if(chanxljMap.get("LINGJBH").equals(lingjbh)){
						flag = true;
						break;
					}
				}
				if(!flag){
					zengcLingjMap.put("SHIJ", params.get("kaissjPC"));
					zengcLingjMap.put("SHENGCSHIJ", "0");
					zengcLingjMap.put("LINGJSL", "0");	
					chanxlj.add(zengcLingjMap);
				}
				//当增产时间为0时，增产完成，跳出循环
				if(zengChanShiJ<=0){break;}
			}
		}
		return zengChanShiJ;
	}
*/	
	/**
	 * @description 得到一天的模拟排产计划
	 * @author 王国首
	 * @date 2012-1-10
	 * @param params	 包含产线和排产时间范围
	 * @param qckc		 期初库存
	 * @param chanxlj	 一条产线一天需要生产的零件
	 * @param zengc		 一条产线一天内进行增产的零件编号，零件数量，每个零件增产的时间
	 * @return qckc		返回最新的期初库存
	 */
	public List<Map<String,String>> calYueMnPC(Map<String,String> params,List<Map<String,String>> qckc,List<Map<String,String>> chanxlj,Map<String,Map<String,String>> zengc,String gongzbh){
		List<Map<String,String>> mnpc = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = new ArrayList<Map<String,String>>();
		tempQckc.addAll(qckc);
		String timeNow = getTimeNow(TIMEFORMAT);
		double gongs = 0;
		double meiRqbcn = 0;
		for(Map<String,String> chanxljMap :chanxlj){
			String lingjbh = chanxljMap.get("LINGJBH");
			double shengcshij = Double.parseDouble(String.valueOf(chanxljMap.get("SHENGCSHIJ")));
			double shengcsl = Double.parseDouble(String.valueOf(chanxljMap.get("LINGJSL")));
			/*当零件有增产时，当天此生产线零件的排产数等于零件的产线净需求+零件的增产数量*/
			if(zengc.size()>0 && zengc.get(lingjbh) != null){
				shengcshij = shengcshij + Double.parseDouble(zengc.get(lingjbh).get("ZENGCLJSJ"));
				shengcsl= shengcsl + Double.parseDouble(zengc.get(lingjbh).get("LINGJSL"));
			}
			/*计算零件下一天的期初库存,下一天的期初库存 = 当天的期初库存+计划排产量-当天的毛需求*/
//			String usbaoz = params.get("USERCENTER")+params.get("shengcxCX")+lingjbh;
//			/*将当天排产出的零件按包装进行取整*/
//			if(getUsPack().get(usbaoz) != null && !"".equals(usbaoz)){
//				shengcsl = getPackFull(Double.parseDouble(String.valueOf(getUsPack().get(usbaoz))),shengcsl);
//			}
//			calNextQckc(params,tempQckc,chanxljMap,lingjbh,shengcsl);
			/*汇总产线当天的工时*/
			gongs = this.getRound(gongs,2)+ this.getRound(shengcshij, 2);
			/*汇总产线当天的所有零件数量，得到每日确保产能*/
			meiRqbcn = meiRqbcn + shengcsl;
			chanxljMap.put("GONGZBH", gongzbh);
			chanxljMap.put("HOUR", String.valueOf(this.getRound(shengcshij, 2)));
			chanxljMap.put("LINGJSL", String.valueOf(shengcsl));
			chanxljMap.put("CHANXZBH", params.get("chanxzbh"));
			chanxljMap.put("EDITOR", params.get("jihybh"));
			chanxljMap.put("EDIT_TIME", timeNow);
			chanxljMap.put("CREATOR", params.get("jihybh"));
			chanxljMap.put("CREATE_TIME", timeNow);
			chanxljMap.put("ID", getUUID());
			mnpc.add(chanxljMap);
		}
//		if(gongs>0 && meiRqbcn>0 && mnpc.size()>0){
			/*将排产出的结果跟新到月度模拟计划明细表和模拟日零件产量表*/
			updateYuedmnjh(params,mnpc,gongzbh,this.getRound(gongs,2),meiRqbcn);
//		}
		return tempQckc;
	}

	/**
	 * @description 计算零件按照包装取整
	 * @author 王国首
	 * @date 2011-12-30
	 * @param params	 包含产线和排产时间范围
	 * @param gongShi	 计算工时
	 * @return gongShi	考虑最小时间单位后的工时
	 */
	public void calNextQckc(Map<String,String> params,List<Map<String,String>> tempQckc,Map<String,String> chanxljMap,String lingjbh,double shengcsl){
		Map<String,String> qckcMap = new HashMap<String,String>();
		for(int i = 0;i<tempQckc.size();i++){
			qckcMap = tempQckc.get(i);
			if(lingjbh.equals(qckcMap.get("LINGJBH"))){
				double lingjqckc = Double.parseDouble(String.valueOf(qckcMap.get("QCKC")))+shengcsl-Double.parseDouble(String.valueOf(chanxljMap.get("MAOXQ")));
				/*检查期初库存是否小于0，当期初库存小于0时，将此消息写入报警消息表，提示给计划员*/
				checkQckc(params,lingjbh,lingjqckc,params.get("shengcxCX"),params.get("USERCENTER"),params.get("kaissjWeeK"),"2");
				qckcMap.put("QCKC", String.valueOf(lingjqckc));
				tempQckc.set(i, qckcMap);
				break;
			}
		}
	}
	
	
	/**
	 * @description 更新月度模拟计划数据
	 * @author 王国首
	 * @date 2012-1-11
	 * @param params	 包含产线和排产时间范围
	 * @param mnpc	 	 模拟日零件产量
	 * @param gongzbh	工作编号
	 * @param gongs		工时
	 * @param meiRqbcn	每日确保产能
	 */
	@Transactional
	public void updateYuedmnjh(Map<String,String> params,List<Map<String,String>> mnpc,String gongzbh,double gongs,double meiRqbcn){
		String shiffb = "N";
		if(getBlockWorkCalendar().get(params.get("shengcxCX")) != null ){
			/*判断当天是否封闭期，如果当天为封闭期，则需将当天的是否封闭字段修改为Y，否者为N*/
			shiffb = getBlockWorkCalendar().get(params.get("shengcxCX")).get(params.get("kaissjPC")) != null ? "Y" : "N";
		}
		if("G".equals(params.get("biaos"))){
			/*滚动月模拟中，如果数据没有更新到数据库，则需要将此条数据插入到数据库*/
			int num = updateYuedmnjhmx(params,gongzbh,gongs,meiRqbcn,shiffb);
			if(num == 0){
				//使用批量操作插入数据
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertMonrgdljclb",mnpc);
//				for(Map<String,String> mnpcMap : mnpc){
//					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMonrgdljclb",mnpcMap);
//				}
			}
			//使用批量操作插入数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertGundyMonrgdljclb",mnpc);
//			for(Map<String,String> mnpcMap : mnpc){
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertGundyMonrgdljclb",mnpcMap);
//			}
		}else{
			insertYuedmnjhmx(params,gongzbh,gongs,meiRqbcn,shiffb);
			//使用批量操作插入数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertMonrgdljclb",mnpc);
//			for(Map<String,String> mnpcMap : mnpc){
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertMonrgdljclb",mnpcMap);
//			}
		}
	}
	
	/**
	 * @description 插入月度模拟计划明细表
	 * @author 王国首
	 * @date 2012-1-16
	 * @param params	 包含产线和排产时间范围
	 * @param uid 		 月度模拟计划号
	 * @param gongs 	 每日工时
	 */
	public void insertYuedmnjhmx(Map<String, String> params,String uid,double gongs,double meiRqbcn,String shiffb){
		String timeNow = getTimeNow(TIMEFORMAT);
		Map<String,String> tempMap = new HashMap<String,String>();
		tempMap.put("GONGZBH", uid);
		tempMap.put("YUEMNJHH", yueMnjhh.get(params.get("shengcxCX")));
		tempMap.put("MEIRQBCN", String.valueOf(meiRqbcn));
		tempMap.put("CHANXH", params.get("shengcxCX"));
		tempMap.put("HOUR", String.valueOf(gongs));
		tempMap.put("SHIJ", params.get("kaissjPC"));
		tempMap.put("USERCENTER", params.get("USERCENTER"));
		tempMap.put("BEIZ", "");	
		tempMap.put("SHIFFB", shiffb);
		tempMap.put("GUNDMNGS", String.valueOf("0"));
		tempMap.put("GUNDQBCN", String.valueOf("0"));
		tempMap.put("CHANXZBH", params.get("chanxzbh"));
		tempMap.put("EDITOR", params.get("jihybh"));
		tempMap.put("EDIT_TIME", timeNow);
		tempMap.put("CREATOR", params.get("jihybh"));
		tempMap.put("CREATE_TIME", timeNow);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertYuedmnjhmx",tempMap);

	}

	/**
	 * @description 插入月度模拟计划明细表
	 * @author 王国首
	 * @date 2012-1-16
	 * @param params	 包含产线和排产时间范围
	 * @param uid 		 月度模拟计划号
	 * @param gongs 	 每日工时
	 * @return num		更新的记录条数
	 */
	public int updateYuedmnjhmx(Map<String, String> params,String uid,double gongs,double meiRqbcn,String shiffb){
		String timeNow = getTimeNow(TIMEFORMAT);
		Map<String,String> tempMap = new HashMap<String,String>();
		tempMap.put("GONGZBH", uid);
		tempMap.put("YUEMNJHH", yueMnjhh.get(params.get("shengcxCX")));
		tempMap.put("CHANXH", params.get("shengcxCX"));
		tempMap.put("SHIJ", params.get("kaissjPC"));
		tempMap.put("USERCENTER", params.get("USERCENTER"));
		if("G".equals(params.get("biaos"))){
			tempMap.put("GUNDMNGS", String.valueOf(gongs));
			tempMap.put("GUNDQBCN", String.valueOf(meiRqbcn));		
		}else{
			tempMap.put("MEIRQBCN", String.valueOf(meiRqbcn));
			tempMap.put("HOUR", String.valueOf(gongs));
		}
		tempMap.put("SHIFFB", shiffb);
		tempMap.put("CHANXZBH", params.get("chanxzbh"));
		tempMap.put("EDITOR", params.get("jihybh"));
		tempMap.put("EDIT_TIME", timeNow);
		int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.updateYuedmnjhmx",tempMap);
		if(num==0 ){
			tempMap.put("MEIRQBCN", String.valueOf(meiRqbcn));
			tempMap.put("HOUR", String.valueOf(gongs));
			tempMap.put("BEIZ", "");	
			tempMap.put("CREATOR", params.get("jihybh"));
			tempMap.put("CREATE_TIME", timeNow);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertYuedmnjhmx",tempMap);
		}
		return num;
	}
	
	/**
	 * @description 插入月度模拟计划表
	 * @author 王国首
	 * @date 2012-2-1
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void insertYuedmnjhb(List<Map<String, String>> shengxList,Map<String, String> params){
		String timeNow = getTimeNow(TIMEFORMAT);
		for( Map<String, String> shengxListMap : shengxList){
			Map<String,String> tempMap = new HashMap<String,String>();
			tempMap.put("YUEMNJHH", yueMnjhh.get(shengxListMap.get("SHENGCXBH")));
			tempMap.put("CHANXH", shengxListMap.get("SHENGCXBH"));
			tempMap.put("KAISSJ", params.get("kaissj"));
			tempMap.put("JIESSJ", params.get("jiessj"));
			tempMap.put("USERCENTER", params.get("USERCENTER"));
			tempMap.put("SHIFQR", "N");
			tempMap.put("DINDDHS", params.get("DINGDH"));
			tempMap.put("CHANXZBH", params.get("chanxzbh"));
			tempMap.put("EDITOR", params.get("jihybh"));
			tempMap.put("EDIT_TIME", timeNow);
			tempMap.put("CREATOR", params.get("jihybh"));
			tempMap.put("CREATE_TIME", timeNow);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertYuedmnjhb",tempMap);
		}
	}
	
	/**
	 * @description 解析滚动月模拟产线的封闭期
	 * @author 王国首
	 * @date 2012-2-5
	 * @param params  包含产线和排产时间范围
	 * @param shengxList 产线列表
	 * @param chanxzWC 	 各个产线的工作日历
	 * @return ap<String, Map<String,String>>	封闭期
	 */
	public Map<String, Map<String,String>> parseBlockWorkCalendar(final Map<String, String> params,List<Map<String, String>> shengxList,Map<String, List<String>> chanxzWC){
		final Map<String, Map<String,String>> workCal = new HashMap<String, Map<String,String>>(); 
		for( Map<String, String> shengcx : shengxList){
			List<String> workCalCXZ = workCalendar.get(shengcx.get("SHENGCXBH"));
			List<String> workCalCX = new ArrayList<String>();
			if(params.get("today").equals(params.get("kaissj")) && ("G".equals(params.get("biaos")))){
				if(chanxzWC.size()>0){
					parseGUNDBlockWork( shengxList,params, chanxzWC,workCalCXZ,workCalCX);
				}
			}else{
				final Iterator<String> it = chanxzWC.keySet().iterator(); 
				if(it.hasNext()){
					List<String> weekRil =(ArrayList<String>)chanxzWC.get(it.next());
					workCalCX.addAll(getWorkSubCal(workCalCXZ,weekRil.get(0),weekRil.get(weekRil.size()-1)));
				}	
			}
			Map<String, String> workCalMap = new HashMap<String, String>(); 
			for( String temp : workCalCX){
				workCalMap.put(temp, temp);
			}
			workCal.put(shengcx.get("SHENGCXBH"), workCalMap);
		}
		return workCal;
	}
	
	/**
	 * @description 得到滚动月模拟第一个滚动周期的封闭期
	 * @author 王国首
	 * @date 2012-2-1
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void parseGUNDBlockWork(List<Map<String, String>> shengxList,Map<String, String> params,Map<String, List<String>> chanxzWC,List<String> workCalCXZ,List<String> workCalCX){
		final Iterator<String> it = chanxzWC.keySet().iterator(); 
		List<String> weekRil =(ArrayList<String>)chanxzWC.get(it.next());
		workCalCX.addAll(getWorkSubCal(workCalCXZ,weekRil.get(0),weekRil.get(weekRil.size()-1)));
		int tiqq = Integer.parseInt(params.get("TIQQ"));
		if(it.hasNext()){
			boolean flag = false;
			if(workCalCX.size()==0){ 
				flag = true;
			}else if(workCalCX.size()>0 && workCalCX.get(0).equals(params.get("today")) && workCalCX.size()<=(tiqq+1)){
				flag = true;
			}else if(workCalCX.size()>0 && !workCalCX.get(0).equals(params.get("today")) && workCalCX.size()<=tiqq){
				flag = true;
			}
			if(flag){
				List<String> weekRilNext =(ArrayList<String>)chanxzWC.get(it.next());
				workCalCX.addAll(getWorkSubCal(workCalCXZ,weekRilNext.get(0),weekRilNext.get(weekRilNext.size()-1)));	
			}
		}
	}
	
	/**
	 * @description 计算包装消耗
	 * @author 王国首
	 * @date 2012-2-10
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void calBaozxh(List<Map<String, String>> shengxList,Map<String, String> params){
		List<Map<String, String>> pack = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryBaozxh",params);
		Map<String,Map<String, String>> baozcx = new HashMap<String,Map<String, String>>();
		for(Map<String, String> packMap : pack){
			Map<String, String> temp = null;
			String shengcxbh = packMap.get("CHANXH");
			if(packMap.get("USBZLX") != null && packMap.get("USBZRL") != null && Double.parseDouble(String.valueOf(packMap.get("USBZRL")))>0){
				String hid = shengcxbh + packMap.get("USBZLX");
				double amount= Math.ceil(Double.parseDouble(String.valueOf(packMap.get("LINGJSL")))/Double.parseDouble(String.valueOf(packMap.get("USBZRL"))));
				if(baozcx.containsKey(hid)){
					temp = baozcx.get(hid);
					amount= Double.parseDouble(temp.get("AMOUNT"))+ amount;
				}else{
					temp = packMap;
					temp.put("EDITOR", params.get("jihybh"));
					temp.put("EDIT_TIME", getTimeNow(TIMEFORMAT));
					temp.put("CREATOR", params.get("jihybh"));
					temp.put("CREATE_TIME", getTimeNow(TIMEFORMAT));
				}
				temp.put("AMOUNT", String.valueOf(amount));
				baozcx.put(hid, temp);
			}	
		}
		final Iterator<String> it = baozcx.keySet().iterator(); 
		while(it.hasNext()){
			Map<String, String> tempMap =(Map<String, String>)baozcx.get(it.next());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertBaozxh",tempMap);
		}
	}
	
	/**
	 * @description 滚动月模拟计算时，删除月度模拟计划明细表中关闭了工作日历的数据
	 * @author 王国首
	 * @date 2012-2-12
	 * @param params	 包含产线和排产时间范围
	 */
	public void deleteCloseCalendarMX(Map<String,String> params){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteCloseCalendarYMN",params);	
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteCloseCalendarMX",params);	
	}
	
	/**
	 * @description 当排产时间大于平均工时时，增加一条警告信息，提示给用户
	 * @author 王国首
	 * @date 2012-2-12
	 * @param params	 包含产线和排产时间范围
	 */
	public void checkPaicTime(Map<String,String> params,double totle,double testime,double avgTime,String shengcxbh,String riq){
		String xiaox = "产线"+shengcxbh+","+riq+"实际工时大于最大平均工时,实际工时为"+this.getRound(totle + testime,2)+"小时,平均工时为"+this.getRound(avgTime,2);
		this.addMessage(params,yueMnjhh.get(shengcxbh),params.get("USERCENTER"),shengcxbh,riq,xiaox,"2");
	}
	
	/**
	 * @description 生成月度模拟计划号
	 * @author 王国首
	 * @date 2012-1-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public Map<String, String> getYueMoNijhh(List<Map<String, String>> shengxList,Map<String, String> params){
		Map<String, String> temp = new HashMap<String, String>();
		for( Map<String, String> shengcxMap : shengxList ){
			temp.put(shengcxMap.get("SHENGCXBH"), params.get("USERCENTER")+shengcxMap.get("SHENGCXBH")+params.get("period"));
		}
		return temp;
	}
	
	/**
	 * @description 将模拟日零件产量，订单预告，外部订单预告插入NUP输出接口表
	 * @author 王国首
	 * @date 2012-3-31
	 * @param params	 包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void parseOutPutNup(Map<String, String> params) {
		params.put("LYXT", "ZCP");//ZCP-月模拟 ZCJ-日滚动
		params.put("YXBH", "1");
		params.put("ZFBJ", "+");
		params.put("HGBM", "ZX");
		params.put("FILLER", "        ");
		List<Map<String, String>> gongyzqNext = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryNextGyzq",params);
		params.put("kaissjNext", gongyzqNext.get(0).get("KAISSJ"));
		params.put("jiessjNext", gongyzqNext.get(0).get("JIESSJ"));
		/*将月模拟结果，模拟日零件产量中数据插入到总成排产接口表中*/
		List<Map<String, String>> monthPC = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryMONRGDLJCLBNUP",params);
		//使用批量操作插入数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertNup",monthPC);
		//xss-0012285
//		for(Map<String,String> monthPCMap: monthPC){
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertNup",monthPCMap);
//		}
		
		
		/*从月模拟计划表中得到订单号*/
		/* xss-下个工业周期未排产的订单数据不发送给NUP */
//		List<Map<String, String>> dingdh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryYueMNDingdh",params);
//		String dingdhs = getYueMNDingdh(dingdh,"DINDDHS",false);
//		if(dingdhs.length()>0){
//			params.put("dingdhs", dingdhs);
//			/*查询订单明细表，得到下个工业周期的预告，将下个工业周期的预告插入到总成排产接口表中*/
//			List<Map<String, String>> dingdPC = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryDingdmxNUP",params);
//			//使用批量操作插入数据
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertNup",dingdPC);
//			for(Map<String,String> dingdPCMap: dingdPC){
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertNup",dingdPCMap);
//			}
//		}
//		List<Map<String, String>> gongyzq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGyzq",params);
//		params.put("jiessj", gongyzq.get(0).get("JIESSJ"));
//		/*得到外部订单预告接口表中下个工业周期的订单，插入到总成排产接口表中*/
//		List<Map<String, String>> wbddygPC = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryWbddygNUP",params);
//		//使用批量操作插入数据
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).executeBatch("monpc.insertNup",wbddygPC);
//		for(Map<String,String> wbddygPCPCMap: wbddygPC){
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.insertNup",wbddygPCPCMap);
//		}
		/* xss-下个工业周期未排产的订单数据不发送给NUP */
	} 
	
	/**
	 * @description 月模拟确认时先将Nup输出接口表中对应生产线的数据删除掉
	 * @author 王国首
	 * @date 2012-1-16
	 * @param shengxList 产线列表
	 * @param params	 包含产线和排产时间范围
	 */
	public void deleteZCPC(Map<String, String> params){
		params.put("monthlyxt", "ZCJ"); 
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("monpc.deleteZCPC",params);
	}
	
}
