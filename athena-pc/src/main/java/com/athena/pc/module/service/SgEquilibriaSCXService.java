package com.athena.pc.module.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.LineTimeUpdate;
import com.athena.pc.entity.LingjDayVolume;
import com.athena.pc.entity.Yuemn;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @Author: 贺志国
 * @E-mail zghe@isoftstone.com
 * @Date: 2012-6-17
 * @version 1.0
 * @Description :产线工时调整
 */
@Component
public class SgEquilibriaSCXService extends BaseService<LineTimeUpdate> {
	private static Logger logger = Logger.getLogger(SgEquilibriaSCXService.class.getName());
	@Inject
	private UserOperLog userOperLog;
	private final static int JC=0;  //减产
	private final static int ZC=1;  //增产
	private final static int CS=2;  //重算期初库存
	private final static String MSG_LEIX="2";//报警消息类型
	
	/**
	 * 查询工业周期内需调整的产线工时
	 * @author 贺志国
	 * @date 2012-6-20
	 * @param param 查询参数
	 * @return Map<String, Object> 结果集
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,LineTimeUpdate>> selectChanxGongs(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryGongstz", param);
	}
	
	/**
	 * 产线查询
	 * @author 贺志国
	 * @date 2012-6-20
	 * @param param 用户中心、计划员组编号
	 * @return List<Map<String,String>> 产线结果集
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanx(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryShengcx",param);
	}
	
	/**
	 * 产线组编号查询
	 * @author 贺志国
	 * @date 2012-5-25
	 * @param param 用户中心、计划员组编号
	 * @return String 产线组编号
	 * @throws ServiceException
	 */
	public String selectChanxzbh(Map<String,String> param)throws ServiceException{
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("sgeq.queryChanxzbh", param);
	}
	
	
	/**
	 * 工业周期时间范围查询
	 * @author 贺志国
	 * @date 2012-6-20
	 * @param gongyzq 工业周期号
	 * @return List<Map<String,String>> 工业周期时间范围
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGyzqsjfw(String gongyzq)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryGyzqfw", gongyzq);
	}
	
	/**
	 * 确认手工调整
	 * @param lines
	 */
	public void sureUpdate(Map<String,String> param)  throws ServiceException{
		logger.info("进入确认手工调整方法");
		//checkInputData();
		logger.info("开始把调整表中的数据迁移到正式表中");
		clearMasterTable(param);
		insertMasterTable(param);
	}
	
	/**
	 * 确认调整后将辅助表中的数据迁移到主表中
	 * @param lines
	 */
	public void insertMasterTable(Map<String,String> param)  throws ServiceException{
		//根据条件将辅表记录插入到主表中
		//1、将辅表记录插入到月度模拟计划表（PC_YUEDMNJHB）
		insertPC_YUEDMNJHB(param);
		//2、将辅表记录插入到月度模拟计划明细表（PC_YUEDMNJHMX）
		insertPC_YUEDMNJHMX(param);
		if("Y".equals(param.get("biaos"))){
			//3、将辅表记录插入到模拟日零件产量表（PC_MONRGDLJCLB）
			insertPC_MONRGDLJCLB(param);
		}
		if("G".equals(param.get("biaos"))){
			//4、将辅表记录插入到滚动月模拟日零件产量表（PC_GUNDYMNRGDLJCLB）
			insertPC_GUNDYMNRGDLJCLB(param);
		}
		//5、将辅表记录插入到零件日需求汇总表（PC_LINGJRXQHZB）
		insertPC_LINGJRXQHZB(param);
		
	}
	
	/**
	 * 确认调整后将主表中的数据清除
	 * @param lines
	 */
	public void clearMasterTable(Map<String,String> param) throws ServiceException{
		//向param中加入主表表名参数
		param.put("PC_MONRGDLJCLB_B", "PC_MONRGDLJCLB");
		param.put("PC_GUNDYMNRGDLJCLB_B", "PC_GUNDYMNRGDLJCLB");
		param.put("PC_YUEDMNJHMX_B", "PC_YUEDMNJHMX");
		param.put("PC_YUEDMNJHB_B", "PC_YUEDMNJHB");
		param.put("PC_LINGJRXQHZB_B", "PC_LINGJRXQHZB");
		this.clearTableData(param);
	}
	
	/**
	 * 初始化数据将辅表或主表（根据参数）中的数据清除
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param 包含查询参数和表名参数
	 */
	@Transactional
	public void clearServantTable(Map<String,String> param) throws ServiceException{
		//向param中加入辅表表名参数
		param.put("PC_MONRGDLJCLB_B", "PC_MONRGDLJCLB_B");
		param.put("PC_GUNDYMNRGDLJCLB_B", "PC_GUNDYMNRGDLJCLB_B");
		param.put("PC_YUEDMNJHMX_B", "PC_YUEDMNJHMX_B");
		param.put("PC_YUEDMNJHB_B", "PC_YUEDMNJHB_B");
		param.put("PC_LINGJRXQHZB_B", "PC_LINGJRXQHZB_B");
		this.clearTableData(param);
	}
	
	/**
	 * 清除辅表或主表数据（根据参数）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param 包含查询参数和表名参数
	 */
	@Transactional
	public void clearTableData(Map<String,String> param) throws ServiceException {
		if("Y".equals(param.get("biaos"))){
			//根据工作编号删除模拟日零件产量表B中的记录
			deletePC_MONRGDLJCLB_B(param);
		}
		if("G".equals(param.get("biaos"))){
			//根据工作编号删除滚动月模拟日零件产量表B中的记录
			deletePC_GUNDYMNRGDLJCLB_B(param);
		}
		//删除月度模拟计划明细表B中的记录
		deletePC_YUEDMNJHMX_B(param);
		//根据计划号删除月度模拟计划表B中的记录
		deletePC_YUEDMNJHB_B(param);
		//根据零件编号、时间、用户中心删除零件日需求汇总表B中的记录
		deletePC_LINGJRXQHZB_B(param);
	}
	
	/**
	 * 1、删除模拟日零件产量表B记录
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param 工作编号 gongzbh
	 */
	@Transactional
	public void deletePC_MONRGDLJCLB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.deleteMONRGDLJCLB_B", param);
	}

	/**
	 * 2、删除滚动月模拟日零件产量表B
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param 工作编号 gongzbh
	 */
	@Transactional
	public void deletePC_GUNDYMNRGDLJCLB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.deleteGUNDYMNRGDLJCLB_B", param);
	}
	
	/**
	 * 3、删除月度模拟计划明细表B
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param '用户中心'、'产线号'、'时间'
	 */
	@Transactional
	public void deletePC_YUEDMNJHMX_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.deletePC_YUEDMNJHMX_B", param);
	}
	
	/**
	 * 4、删除月度模拟计划表B
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param 计划号 jihh
	 */
	@Transactional
	public void deletePC_YUEDMNJHB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.deletePC_YUEDMNJHB_B", param);
	}
	
	/**
	 * 4、删除月度模拟计划表B
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param 零件编号 、时间、用户中心、标识
	 */
	@Transactional
	public void deletePC_LINGJRXQHZB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.deletePC_LINGJRXQHZB_B", param);
	}
	
	
	
	/**
	 * 初始化数据将主表表中的数据迁移到辅助中
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param lines
	 * @param param
	 */
	@Transactional
	public void insertServantTable(List<LineTimeUpdate> lines,Map<String,String> param) throws ServiceException {
		//根据条件将主表记录插入到辅表中
		//1、将主表记录插入到月度模拟计划表B（PC_YUEDMNJHB_B）
		insertPC_YUEDMNJHB_B(param);
		//2、将主表记录插入到月度模拟计划明细表B（PC_YUEDMNJHMX_B）
		insertPC_YUEDMNJHMX_B(param);
		if("Y".equals(param.get("biaos"))){
			//3、将主表记录插入到模拟日零件产量表B（PC_MONRGDLJCLB_B）
			insertPC_MONRGDLJCLB_B(param);
		}
		if("G".equals(param.get("biaos"))){
			//4、将主表记录插入到滚动月模拟日零件产量表B（PC_GUNDYMNRGDLJCLB_B）
			insertPC_GUNDYMNRGDLJCLB_B(param);
		}
		//5、将主表记录插入到零件日需求汇总表B（PC_LINGJRXQHZB_B）
		insertPC_LINGJRXQHZB_B(param);
	}
	
	
//*********************** 主表记录插入辅表中 *********************************//	
	/**
	 *1、将主表记录插入到对应的辅表中（PC_YUEDMNJHB_B）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_YUEDMNJHB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_YUEDMNJHB_B", param);
	}
	/**
	 *2、将主表记录插入到对应的辅表中（PC_YUEDMNJHMX_B）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_YUEDMNJHMX_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_YUEDMNJHMX_B", param);
	}
	
	/**
	 *3、将主表记录插入到对应的辅表中（PC_MONRGDLJCLB_B）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_MONRGDLJCLB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_MONRGDLJCLB_B", param);
	}
	
	/**
	 *4、将主表记录插入到对应的辅表中（PC_LINGJRXQHZB_B）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_LINGJRXQHZB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_LINGJRXQHZB_B", param);
	}
	
	/**
	 *5、将主表记录插入到对应的辅表中（PC_GUNDYMNRGDLJCLB_B）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_GUNDYMNRGDLJCLB_B(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_GUNDYMNRGDLJCLB_B", param);
	}
	
	
//*********************** 辅表记录插入主表中 *********************************//
	/**
	 *1、将辅表记录插入到对应的主表中（PC_YUEDMNJHB）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_YUEDMNJHB(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_YUEDMNJHB", param);
	}
	/**
	 *2、将辅表记录插入到对应的主表中（PC_YUEDMNJHMX）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_YUEDMNJHMX(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_YUEDMNJHMX", param);
	}
	
	/**
	 *3、将辅表记录插入到对应的主表中（PC_MONRGDLJCLB）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_MONRGDLJCLB(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_MONRGDLJCLB", param);
	}
	
	/**
	 *4、将辅表记录插入到对应的主表中（PC_LINGJRXQHZB）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_LINGJRXQHZB(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_LINGJRXQHZB", param);
	}
	
	/**
	 *5、将辅表记录插入到对应的主表中（PC_GUNDYMNRGDLJCLB_B）
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 */
	@Transactional
	public void insertPC_GUNDYMNRGDLJCLB(Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertPC_GUNDYMNRGDLJCLB", param);
	}
	
	
	
	/**
	 *  手动均衡
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param params
	 * @param linesList
	 * @return String msg 消息提醒
	 */
	public String stickOfequilibria(Map<String,String> params, List<LineTimeUpdate> linesList) throws ServiceException{
		logger.info("进入手工均衡产线方法");
		String isTrue = checkInputData(linesList,params);
		if("success".equals(isTrue)){
			initData(linesList,params);
			//取产线结果集合中的第一个开始时间，作为调整工时的开始时间
			params.put("startTime", linesList.get(0).getStartTime()); //调整工时的开始时间
			//查询从开始时间到工业周期结束时间范围内的工作日集合
			List<String> pcGzrList = this.getWorkCalendarCX(params);
			//封装排产工作日工作编号
			Map<String,String> daysGongzbh = wrapGongzbh(pcGzrList,params.get("usercenter"));
			//封装每天零件消耗
			Map<String,Map<String,Integer>> ljXHL = this.wrapMaoxq(pcGzrList,params);
			//取第一天所有零件的期初库存
			Map<String,Integer> firstQCKC = this.getFirstQickcOfLingj(params);
			logger.info("工时调整开始");
			for(String riq : pcGzrList){
				List<LineTimeUpdate> linesChange = getlinesChange(linesList,riq);
				//增产或减产的零件及期初库存
				Map<String,List<Integer>> zjcLingj =  new HashMap<String,List<Integer>>();
				params.put("riq", riq);
				//查询得到下一天的日期
				String nextDay = this.getNextWorkDay(riq,pcGzrList);
				if("".equals(nextDay)){//已是最后一天
					//终止循环
					break;
				}
				//获得当天零件排产量
				Map<String,Integer> ljPCL = this.caculatePaicl(daysGongzbh.get(riq),params.get("biaos"));
				if(ljPCL.size()==0){
					logger.error("零件排产量为空");
					break;
				}
				//计算零件下一天期初库存（期初库存=当日库存+当日排产量-当日消耗量）
				this.caculateNextQickcOfLingj(ljPCL, ljXHL.get(riq), firstQCKC); 
				//计算某天零件排产量和期初库存
				//2013-10-15  linesList重复循环执行了多次，此处修改为只有当天修改的生产线作为参数。
//				this.caculateLingjPC(linesList,ljPCL,ljXHL.get(riq),firstQCKC,zjcLingj,params,nextDay);
				this.caculateLingjPC(linesChange,ljPCL,ljXHL.get(riq),firstQCKC,zjcLingj,params,nextDay);
				//更新当天零件的期初库存
				this.updateQickc(firstQCKC, params,nextDay);
			}
		}
		logger.info("工时调整结束");
		return isTrue;
	} 
	
	/**
	 * 计算零件排产量
	 * @author 贺志国
	 * @date 2012-7-18
	 * @param linesList 
	 * @param ljPCL
	 * @param firstQCKC
	 * @param params
	 */
	public void caculateLingjPC(List<LineTimeUpdate> linesList,Map<String,Integer> ljPCL,Map<String,Integer> ljXHL,
			Map<String,Integer> firstQCKC,Map<String,List<Integer>> zjcLingj,Map<String,String> params,String nextDay)throws ServiceException{
		for (LineTimeUpdate line : linesList) {
			logger.info("更新用户调整工时");
			zjcLingj =  new HashMap<String,List<Integer>>();
			line.setBiaos(params.get("biaos"));
			line.setLinegroup(params.get("chanxzbh"));
			updateLineWorkTime(line);
			logger.info("重新计算零件排产量，期初库存");
			recountLingjPC(line,ljPCL,ljXHL,firstQCKC,zjcLingj,params,nextDay); 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.updateYuedmnjhmxLingjsl",line);
		}
		
	}

	/**
	 * 重新计算更新工时后的零件排产量，期初库存
	 * @param line
	 */
	public void recountLingjPC(LineTimeUpdate line, Map<String,Integer> ljPCL,Map<String,Integer> ljXHL,
			Map<String,Integer> firstQCKC,Map<String,List<Integer>> zjcLingj,Map<String,String> params,String nextDay) throws ServiceException {
		
		params.put("lineNum", line.getLineNum()); //产线号
		List<Yuemn> ymnList = selectCurrLineTime(params);

		//当天零件排产量，如果是Y就查询模拟日零件表，如果是G就查询滚动月模拟零件表
		List<LingjDayVolume> days = getLingjDayVolume(ymnList,params); 
		//获得下一天的毛需求
		Map<String,Integer>  nextLingjMaoxq = this.nextLingjMaoxq(nextDay,params);
		Map<String,Integer> dtPCLJ = new HashMap<String,Integer>();
		//每一天零件总工时
		double countHour = 0;
		for(LingjDayVolume day : days) {
			countHour += Double.parseDouble(day.getHour()); 
			dtPCLJ.put(day.getLingjbh(), day.getLingjsl());
		}
		adjustLingjVolume(Double.parseDouble(ymnList.get(0).getHour())-countHour,days,ymnList,dtPCLJ,
				ljPCL,ljXHL,firstQCKC,nextLingjMaoxq,params,zjcLingj,nextDay); 
		//更新某条产线的零件数量
		this.updateLingj(ymnList,params,zjcLingj);
		
	}
	/**
	 * 调整零件数量
	 * @param hour 工时
	 * @param yue 月根据实体类
	 * @param days list
	 */
	private void adjustLingjVolume(double hour, List<LingjDayVolume> days,List<Yuemn> ymnList,Map<String,Integer> dtPCLJ,
			Map<String,Integer> ljPCL,Map<String,Integer> ljXHL	,Map<String,Integer> nextDayQickc,Map<String,Integer>  nextLingjMaoxq,
			Map<String,String> params,Map<String,List<Integer>> zjcLingj,String nextDay) throws ServiceException {
		//增产或减产的零件数量=工时*节拍，取绝对值
		double number = Math.abs(Math.ceil(hour * Double.parseDouble(String.valueOf(ymnList.get(0).getBeat()))));		
		if (hour > 0) {
			//根据库存系数小的增产
			increaseReduceProduction(number,days,dtPCLJ,ljPCL,nextDayQickc,nextLingjMaoxq,params,zjcLingj,ZC,new HashSet<String>(),nextDay);
		} else if (hour < 0) {
			//根据库存系数大的减产 
			increaseReduceProduction(number,days,dtPCLJ,ljPCL,nextDayQickc,nextLingjMaoxq,params,zjcLingj,JC,new HashSet<String>(),nextDay);
		} else if (hour == 0) {
			//重新计算期初库存
			increaseReduceProduction(number,days,dtPCLJ,ljPCL,nextDayQickc,nextLingjMaoxq,params,zjcLingj,CS,new HashSet<String>(),nextDay);
		}
	}
	
	/**
	 * 增减产
	 * @author 贺志国
	 * @date 2012-6-28
	 * @param days 某一天的零件
	 * @param yue 月模拟工作日
	 * @param params 基本参数
	 * @param number 增减产零件数量
	 * @param zjcLingj 增减产零件和期初库存
	 * @param zjc 增产或减产
	 */
	public void increaseReduceProduction(double number,List<LingjDayVolume> days,Map<String,Integer> dtPCLJ
			,Map<String,Integer> ljPCL,Map<String,Integer> nextDayQickc,Map<String,Integer> nextLingjMaoxq
			,Map<String,String> params,Map<String,List<Integer>> zjcLingj,int zjc,Set<String> ljBhs,String nextDay) throws ServiceException{
		
		//初始化零件的经济批量
		List<Map<String,String>> chanxljList =  this.initChanxJJPL();
		if(zjc==ZC){
			this.increaseProduction(number,days,dtPCLJ,ljPCL,nextDayQickc,nextLingjMaoxq,chanxljList,params,zjcLingj,zjc,ljBhs);
		}else if(zjc==JC){
			this.reduceProduction(number,days,dtPCLJ,ljPCL,nextDayQickc,nextLingjMaoxq,chanxljList,params,zjcLingj,zjc,ljBhs);
		}else if(zjc==CS){
			this.reCaculateQickc(nextDayQickc,params,nextDay);
		}
	}
	
	
	
	/**
	 * 增产零件计算
	 * @author 贺志国
	 * @date 2012-7-6
	 * @param number 增产零件数量
	 * @param days 当天零件集合
	 * @param nextQickc 下一天的期初库存
	 * @param nextLingjMaoxq 下一天的毛需求
	 * @param chanxljList 产线所有的零件集合
	 * @param params 基本参数
	 * @param zjcLingj 实际增减产零件
	 * @param zjc 增减产标识
	 * @param ljBhs 不需要计算库存系数的零件编号
	 * @throws ServiceException 异常
	 */
	public void increaseProduction(double number,List<LingjDayVolume> days,Map<String,Integer> dtPCLJ,Map<String,Integer> ljPCL,Map<String,Integer> nextDayQickc,
			Map<String,Integer> nextLingjMaoxq,List<Map<String,String>> chanxljList,Map<String,String> params,
			Map<String,List<Integer>> zjcLingj,int zjc,Set<String> ljBhs) throws ServiceException {
		logger.info("计算增产开始");
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "工时调整increaseProduction", "计算增产开始");
		//如果增产的零件小于等于0，则停止增产
		double numberTemp = number;
		if(numberTemp<=0){
			return;
		}
		//计算库存系数
		Map<String,Float> kcxs = getKucxsOfLingj(dtPCLJ,nextDayQickc,nextLingjMaoxq,ljBhs);
		// 得到库存系数最小的零件
		String ljbh = this.findMinKCXS(kcxs);
		String jjpl = this.getLingjJJPL(ljbh, chanxljList,params);
		if(jjpl==null){
			logger.info("所获取零件的经济批量为空");
		}
		//增产零件大于经济批量
		if(numberTemp>=Double.parseDouble(jjpl)){
			//增产一个经济批量
			for(LingjDayVolume day : days){
				if(ljbh.equalsIgnoreCase(day.getLingjbh())){
					//某条产线的零件数量
					int ljsl = day.getLingjsl()+Integer.parseInt(jjpl);
					//当天排产的零件数量
					int pcljs = ljPCL.get(ljbh)+Integer.parseInt(jjpl);
					day.setLingjsl(ljsl);
					ljPCL.put(ljbh, pcljs);
					//零件增产后更新零件的期初库存
					this.caculateQickcSingleLingj(ljbh, Integer.parseInt(jjpl), nextDayQickc,zjc);
					//day.setQickc(nextDayQickc.get(ljbh));
					//封装零件数量和期初库存
					List<Integer> slList = new ArrayList<Integer>();
					slList.add(ljsl);
					//slList.add(nextDayQickc.get(ljbh));
					zjcLingj.put(ljbh, slList);
				}
			}
			numberTemp = numberTemp-Double.parseDouble(jjpl);
			//递归按库存系数小的增产
			this.increaseProduction(numberTemp,days,dtPCLJ,ljPCL,nextDayQickc,nextLingjMaoxq,chanxljList,params,zjcLingj,zjc,ljBhs);
		}else{
			//增产不满足一个经济批量的零件
			for(LingjDayVolume day : days){
				if(ljbh.equalsIgnoreCase(day.getLingjbh())){
					//某条产线的零件数量
					int ljsl = day.getLingjsl()+(int)numberTemp;
					//当天排产的零件数量
					int pcljs = ljPCL.get(ljbh)+(int)numberTemp;
					day.setLingjsl(ljsl);
					ljPCL.put(ljbh, pcljs);
					this.caculateQickcSingleLingj(ljbh, (int)numberTemp, nextDayQickc, zjc);
					//day.setQickc(nextDayQickc.get(ljbh));
					//封装零件数量和期初库存
					List<Integer> slList = new ArrayList<Integer>();
					slList.add(ljsl);  //零件数量
					//slList.add(nextDayQickc.get(ljbh));  //期初库存
					zjcLingj.put(ljbh, slList);
				}
			}
			
			
			//终止递归
			return;
		}
		logger.info("计算增产结束");
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "工时调整increaseProduction", "计算增产结束");
	}
	
	/**
	 * 减产零件计算
	 * @author 贺志国
	 * @date 2012-7-6
	 * @param number 减产零件
	 * @param days 当天零件集合
	 * @param riq 日期
	 * @param nextQickc 下一天的期初库存
	 * @param nextLingjMaoxq 下一天的毛需求
	 * @param chanxljList 产线所有的零件集合
	 * @param params 基本参数
	 * @param zjcLingj 实际增减产零件
	 * @param zjc 增减产标识
	 * @param ljBhs 不需要计算库存系数的零件编号
	 * @throws ServiceException 异常
	 */
	public void reduceProduction(double number,List<LingjDayVolume> days,Map<String,Integer> dtPCLJ,Map<String,Integer> ljPCL,
			Map<String,Integer> nextQickc,Map<String,Integer> nextLingjMaoxq,List<Map<String,String>> chanxljList,
			Map<String,String> params,	Map<String,List<Integer>> zjcLingj,int zjc,Set<String> ljBhs) throws ServiceException{
		logger.info("计算减产开始");
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "工时调整reduceProduction", "计算减产开始");
		//如果减产的零件小于等于0，则停止减产
		double numberTemp = number;
		if(numberTemp<=0){
			return;
		}
		//计算库存系数
		Map<String,Float> kcxs = getKucxsOfLingj(dtPCLJ,nextQickc,nextLingjMaoxq,ljBhs);
		//如果没有要减产的零件,则报警
		if(kcxs.size()==0){
			//消息
			String message = "产线组("+params.get("chanxzbh")+")下的产线编号("+params.get("lineNum")+")的减产数量为"+numberTemp+",该产线下当天的所有零件均已为0,无法通过减产来达到当天零件的减产数量。请审查!";
			//报警
			this.alarmMessage(message,params) ;
			//终止递归
			return;
		}
		
		// 得到库存系数最大的零件
		String ljbh = this.findMaxKCXS(kcxs);
		String jjpl = this.getLingjJJPL(ljbh, chanxljList,params);
		//减产零件大于经济批量
		if(numberTemp>Double.parseDouble(jjpl)){
			//减产一个经济批量
			for(LingjDayVolume day : days){
				if(ljbh.equalsIgnoreCase(day.getLingjbh())){
					int ljsl = 0;
					if(day.getLingjsl()==0||day.getLingjsl()<=Integer.parseInt(jjpl)){ //零件数量小于经济批量，减产后为0,则不需要计算此零件的库存系数
						numberTemp = numberTemp-day.getLingjsl(); //直接减去剩余零件数量
						this.caculateQickcSingleLingj(ljbh, day.getLingjsl(), nextQickc,zjc);
						ljBhs.add(ljbh);
					}else{
						ljsl = day.getLingjsl()-Integer.parseInt(jjpl); //减经济批量
						numberTemp = numberTemp-Double.parseDouble(jjpl);
						this.caculateQickcSingleLingj(ljbh, Integer.parseInt(jjpl), nextQickc,zjc);
					}
					day.setLingjsl(ljsl);
					day.setQickc(nextQickc.get(ljbh));
					//封装零件数量和期初库存
					List<Integer> slList = new ArrayList<Integer>();
					slList.add(ljsl);
					slList.add(nextQickc.get(ljbh));
					zjcLingj.put(ljbh, slList);
				}
			}
			//递归按库存系数大的减产
			this.reduceProduction(numberTemp,days,dtPCLJ,ljPCL,nextQickc,nextLingjMaoxq,chanxljList,params,zjcLingj,zjc,ljBhs);
		}else{
			//减产零件不满足一个经济批量
			for(LingjDayVolume day : days){
				if(ljbh.equalsIgnoreCase(day.getLingjbh())){
					if(day.getLingjsl()<numberTemp){ //零件数量小于减产零件数量，则报警
						String message = "产线组("+params.get("chanxzbh")+")下的产线编号("+params.get("lineNum")+")的减产数量为"+numberTemp+",减产零件小于经济批量并且小于所要减产的零件数量。请审查!";
						this.alarmMessage( message, params);
						//终止递归
						return; 
					}
					int	ljsl = day.getLingjsl()-(int)numberTemp;
					day.setLingjsl(ljsl);
					this.caculateQickcSingleLingj(ljbh, (int)numberTemp, nextQickc, zjc);
					day.setQickc(nextQickc.get(ljbh));
					//封装零件数量和期初库存
					List<Integer> slList = new ArrayList<Integer>();
					slList.add(ljsl);  //零件数量
					slList.add(nextQickc.get(ljbh));  //期初库存
					zjcLingj.put(ljbh, slList);
				}
			}
			//终止递归
			return;
		}
		logger.info("计算减产结束");
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "工时调整reduceProduction", "计算减产结束");
	}
	
	
	/**
	 * 重新计算期初库存
	 * @author 贺志国
	 * @date 2012-6-29
	 * @param days
	 * @param yue
	 * @param params
	 * @param number
	 * @param zjcLingj
	 */
	public void reCaculateQickc(Map<String,Integer> nextDayQickc,Map<String,String> params,String nextDay) throws ServiceException{
		logger.info("重新计算期初库存开始");
		//更新零件下一天的期初库存
		updateQickc(nextDayQickc, params,nextDay);
		logger.info("重新计算期初库存结束");
	}
	
	
	
	/**
	 * 零件经济批量
	 * @author 贺志国
	 * @date 2012-6-27
	 * @param ljbh 零件编号
	 * @param chanxljList 产线零件集合
	 * @param params 参数
	 * @return 指定零件的经济批量
	 */
	public String getLingjJJPL(String ljbh,List<Map<String,String>> chanxljList,Map<String,String> params) throws ServiceException{
		logger.info("零件经济批量开始");
		String chanxlj = params.get("usercenter")+params.get("lineNum")+ljbh;
		for(Map<String,String> cxlj : chanxljList){
			if(chanxlj.equalsIgnoreCase(cxlj.get("CHANXLJ"))){
				return cxlj.get("JINGJPL");
			}
		}
		return null;
	}
	
	
	/**
	 * 得到库存系数最小的零件
	 * @author 贺志国
	 * @date 2012-6-27
	 * @param kcxs 库存系数
	 * @return String 库存系数最小的零件编号
	 */
	public String findMinKCXS(Map<String,Float> kcxs) throws ServiceException{
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
			}else{
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
	 *  得到库存系数最大的零件
	 * @author 贺志国
	 * @date 2012-6-27
	 * @param kcxs 库存系数
	 * @return String 库存系数最大的零件编号
	 */
	public String findMaxKCXS(Map<String,Float> kcxs) throws ServiceException{
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
			}else{
				//如果库存系数大于或者等于,则指向新的零件号
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
	 * 查询并封装零件第一天的期初库存
	 * @author 贺志国
	 * @date 2012-6-27
	 * @param parmas 用户中心、开始时间、结束时间、标识'Y'或'G'
	 * @return Map<String,Integer>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Integer> getFirstQickcOfLingj(Map<String,String> params) throws ServiceException{
		logger.info("获取并封装零件第一天的期初库存开始");
		//查询第一天所有零件期初库存 
		List<Map<String,String>> firstQickcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryQickcOfLingj", params);
		//封装期初库存
		Map<String,Integer> currDayQickc = wrapQickc(firstQickcList);
		logger.info("获取并封装零件第一天的期初库存结束");
		
		return currDayQickc;
	}
	
	
	
	/**
	 * 计算零件下一天期初库存（期初库存=当日库存+排产量-消耗量）
	 * @author 贺志国
	 * @date 2012-6-26
	 * @return Map<String,Integer>
	 */
	public void caculateNextQickcOfLingj(Map<String,Integer> ljPCL,Map<String,Integer> ljXHL, 
			Map<String,Integer> firstQCKC) throws ServiceException{
		logger.info("计算零件下一天期初库存开始");
		//计算当天的排产量
		//Map<String,Integer> ljPCL = this.caculatePaicl(days);
		//1、有排产无消耗；2、有消耗无排产（无排产有消耗）
		//迭代排产量(1)
		for(Map.Entry<String, Integer> ljpc :ljPCL.entrySet()){
			 //得到消耗量(如果这天没有消耗，则将其消耗置为0)
			 int xhSl = ljXHL.containsKey(ljpc.getKey())?ljXHL.get(ljpc.getKey()):0;
				//期初库存
				int kc = firstQCKC.get(ljpc.getKey())+ljpc.getValue()-xhSl;
				firstQCKC.put(ljpc.getKey(), kc);
		}
		//迭代消耗量(2)
		for(Map.Entry<String, Integer> ljxh :ljXHL.entrySet()){
			if(ljPCL.containsKey(ljxh.getKey())){
				continue;
			}else{
				int kc = firstQCKC.get(ljxh.getKey())+0-ljxh.getValue();
				firstQCKC.put(ljxh.getKey(), kc);
			}
			
		}
		logger.info("计算零件下一天期初库存结束");
		
		//return firstQCKC;
	}
	
	/**
	 * 增减产时计算期初库存
	 * @author 贺志国
	 * @date 2012-7-17
	 * @param lingjbh 零件编号
	 * @param lingjsl 零件数量
	 * @param firstQCKC 期初库存
	 * @param zjc 增减产标识
	 */
	public void caculateQickcSingleLingj(String lingjbh,int lingjsl,Map<String,Integer> firstQCKC,int zjc){
		if(ZC==zjc){
			firstQCKC.put(lingjbh,firstQCKC.get(lingjbh)+lingjsl);
		}else if(JC==zjc){
			firstQCKC.put(lingjbh,firstQCKC.get(lingjbh)-lingjsl);
		}
	}
	
	/**
	 * 查询下一天的零件毛需求
	 * @author 贺志国
	 * @date 2012-6-28
	 * @param yue 模拟排产明细
	 * @param params 参数集
	 * @return Map<String,Integer>
	 */
	public Map<String,Integer>  nextLingjMaoxq(String nextDay, Map<String,String> params) throws ServiceException{
		logger.info("获取下一天的零件毛需求开始");
		//下一天的时间与工作编号
		Map<String,String> nextDayParam = new HashMap<String,String>();
		nextDayParam.put("usercenter", params.get("usercenter"));
		nextDayParam.put("biaos", params.get("biaos"));
		nextDayParam.put("riq", nextDay);
		//获得下个工作日的毛需求
		Map<String,Integer> nextLjMaoxq = this.wrapNextMaoxq(this.selectLingjMaoxq(nextDayParam));
		logger.info("获取下一天的零件毛需求结束");
		return nextLjMaoxq;
	}
	
	/**
	 * 计算零件的库存系数（库存系数=下一个工作日的期初库存/下个工作日的毛需求）
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param nextQickc 下一工作日的期初库存
	 * @param nextLjMaoxq 下一工作日的零件毛需求
	 * @return Map<String,Float>
	 */
	public Map<String,Float> getKucxsOfLingj(Map<String,Integer> dtPCLJ,Map<String,Integer> nextQickc,
			Map<String,Integer> nextLjMaoxq,Set<String> ljBhs) throws ServiceException{
		logger.info("计算零件的库存系数getKucxsOfLingj开始");
		//库存系数
		Map<String,Float> kcxs = new HashMap<String, Float>();
		//期初库存
		for(Map.Entry<String, Integer> qckc:nextQickc.entrySet()){
			if(ljBhs.contains(qckc.getKey())){
				continue;
			}
			//包含排产零件
			if(dtPCLJ.containsKey(qckc.getKey())){
				if(nextLjMaoxq.containsKey(qckc.getKey())){
					kcxs.put(qckc.getKey(), Float.parseFloat(String.valueOf(qckc.getValue()))
							/Float.parseFloat(String.valueOf(nextLjMaoxq.get(qckc.getKey()))));
				}else{
					System.out.println("111111111:"+qckc.getKey());
					kcxs.put(qckc.getKey(),Float.parseFloat("10000000.0"));
				}

			}
		}
		logger.info("计算零件的库存系数getKucxsOfLingj结束");
		return kcxs;
	}
	
	
	/**
	 * 计算当天排产量
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param days 当天的零件数量
	 * @return Map<String,Integer>
	 */
	public Map<String,Integer> caculatePaicl(String gongzbhs,String biaos) throws ServiceException{
		logger.info("计算当天排产量开始");
		List<Map<String,String>> pclList = null;
		if("Y".equals(biaos)){
			pclList = this.getcurrYmnPCL(gongzbhs);
		}else if("G".equals(biaos)){
			pclList = this.getcurrRgdPCL(gongzbhs);
		}
		Map<String,Integer> ljPCL = new HashMap<String,Integer>();
		for(Map<String,String> pclmap : pclList){
			ljPCL.put(pclmap.get("LINGJBH"),Integer.parseInt(pclmap.get("LINGJSL")));
		}
		logger.info("计算当天排产量结束");
		return ljPCL;
	}
	
	/**
	 * 获取当天月模拟排产量
	 * @author 贺志国
	 * @date 2012-7-18
	 * @param riq
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getcurrYmnPCL(String gongzbhs){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.querycurrYmnPCL", gongzbhs);
	}
	
	/**
	 * 获取当天日滚动模拟排产量
	 * @author 贺志国
	 * @date 2012-7-18
	 * @param riq
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getcurrRgdPCL(String gongzbhs){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.querycurrRgdPCL", gongzbhs);
	}
	
	/**
	 * 计算当天期初库存
	 * @author 贺志国
	 * @date 2012-7-4
	 * @param days
	 * @return
	 */
	public Map<String,Integer> caculateCurrQickc(List<LingjDayVolume> days){
		Map<String,Integer> ljQCKC = new HashMap<String,Integer>();
		for(LingjDayVolume day : days){
			ljQCKC.put(day.getLingjbh(),day.getQickc());
		}
		return ljQCKC;
	}
	
	
	/**
	 * 返回某一天零件的消耗量
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param params 用户中心，时间，标识
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectLingjMaoxq(Map<String,String> dayParam) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryMaoxq", dayParam);
	}

	/**
	 * 返回某一天零件的期初库存
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param params 用户中心，时间，标识
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectQickc(Map<String,String> params) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryQickc", params);
	}
	
	/**
	 * 封装排产工作日零件毛需求（消耗量）
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param maoxqList 当天零件毛需求集合
	 * @return  Map<String,Integer>
	 */
	public Map<String,Map<String,Integer>> wrapMaoxq(List<String> pcGzrList,Map<String,String> params) throws ServiceException{
		Map<String,String> dayParam = new HashMap<String,String>();
		Map<String,Map<String,Integer>> dayLingjXHL = new HashMap<String,Map<String,Integer>>();
		dayParam.put("usercenter", params.get("usercenter"));
		dayParam.put("biaos", params.get("biaos"));
		for(String riq : pcGzrList){
			dayParam.put("riq", riq);
			//每天零件毛需求（消耗量）
			List<Map<String,String>> maoxqList = this.selectLingjMaoxq(dayParam);
			Map<String,Integer> lingjXHL = new HashMap<String,Integer>();
			//封装每天零件毛需求（消耗量）
			for(Map<String,String> ljmxq: maoxqList){
				lingjXHL.put(ljmxq.get("LINGJBH"), Integer.parseInt(ljmxq.get("LINGJSL")));
			}
			dayLingjXHL.put(riq, lingjXHL);
		}
		return dayLingjXHL;
	}
	
	
	/**
	 * 封装某一天零件毛需求（消耗量）
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param maoxqList 当天零件毛需求集合
	 * @return  Map<String,Integer>
	 */
	public Map<String,Integer> wrapNextMaoxq(List<Map<String,String>> maoxqList) throws ServiceException{
		Map<String,Integer> lingjXHL = new HashMap<String,Integer>();
		for(Iterator<Map<String,String>> mxqit =maoxqList.iterator();mxqit.hasNext(); ){
			Map<String,String> ljxql = mxqit.next();
			lingjXHL.put(ljxql.get("LINGJBH"), Integer.parseInt(ljxql.get("LINGJSL")));
		}
		return lingjXHL;
	}
	
	/**
	 * 封装某一天零件期初库存
	 * @author 贺志国
	 * @date 2012-6-26
	 * @param qickcList 当天零件期初库存集合
	 * @return Map<String,Integer>
	 */
	public Map<String,Integer> wrapQickc(List<Map<String,String>> qickcList) throws ServiceException{
		Map<String,Integer> lingjQCKC = new HashMap<String,Integer>();
		for(Iterator<Map<String,String>> qckcit =qickcList.iterator();qckcit.hasNext(); ){
			Map<String,String> ljxql = qckcit.next();
			lingjQCKC.put(ljxql.get("LINGJBH"), Integer.parseInt(ljxql.get("QICKC")));
		}
		return lingjQCKC;
	}
	
	/**
	 * 返回某一天的零件排产量
	 * @date 2012-6-25
	 * @param yue
	 * @param gongzbh 工作编号
	 * @param biaos 标识，Y或G
	 * @return List<LingjDayVolume> 某一天零件的工时和数量
	 */
	@SuppressWarnings("unchecked")
	public List<LingjDayVolume> getLingjDayVolume(List<Yuemn> ymnList,Map<String,String> params) throws ServiceException {
		logger.info("返回某一天的零件排产量");
		List<LingjDayVolume> lingjList = null;
		Map<String,String> ljclParams = new HashMap<String,String>();
		String biaos = params.get("biaos");
		ljclParams.put("usercenter",params.get("usercenter"));
		ljclParams.put("biaos",biaos);
		ljclParams.put("shij",ymnList.get(0).getRiq());
		ljclParams.put("gongzbh",ymnList.get(0).getGongzbh());
		if("Y".equals(biaos)){//查询模拟日零件产量表
			 lingjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryMnrljcl", ljclParams);
		}else if("G".equals(biaos)){//查询滚动月模拟日零件产量表
			 lingjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryGdmnrljcl", ljclParams);
		}
		return lingjList;
	}
	
	
	/**
	 * 更新零件排产数量,库存
	 */
	@Transactional
	public void updateLingj(List<Yuemn> yue,Map<String,String> params,Map<String,List<Integer>> zjcLingj) throws ServiceException {
		logger.info("更新零件排产数量,库存,updateLingj");
		Map<String,String> ljParams = new HashMap<String,String>();
		ljParams.put("usercenter", params.get("usercenter"));
		ljParams.put("lineNum", params.get("lineNum"));
		if("Y".equals(params.get("biaos"))){//更新模拟日零件产量表
			ljParams.put("pc_monrgdljclb_b", "pc_monrgdljclb_b");
			ljParams.put("biaos", "Y");
			updateMnrgdLingl(yue,ljParams,zjcLingj);
		}else if("G".equals(params.get("biaos"))){//更新滚动月模拟日零件产量表
			ljParams.put("pc_monrgdljclb_b", "pc_gundymnrgdljclb_b");
			ljParams.put("biaos", "G");
			updateMnrgdLingl(yue,ljParams,zjcLingj);
		}
	}
		
	/**
	 * 只更新零件期初库存
	 * @author 贺志国
	 * @date 2012-6-29
	 */
	@Transactional
	public void updateQickc(Map<String,Integer> nextQickc,Map<String,String> params,String nextDay) throws ServiceException{
		Map<String,String> ljParams = new HashMap<String,String>();
		logger.info("只更新零件期初库存,updateQickc");
		ljParams.put("usercenter", params.get("usercenter"));
		ljParams.put("biaos", params.get("biaos"));
		ljParams.put("shij", nextDay);
		for(String ljbh : nextQickc.keySet()){
			ljParams.put("lingjbh", ljbh);
			ljParams.put("qickc", String.valueOf(nextQickc.get(ljbh)));
			//更新下一天期初库存
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.updateLingjrxqzh", ljParams);
		}
	}
	
	
	/**
	 * 更新零件数量及期初库存
	 * @author 贺志国
	 * @date 2012-6-28
	 * @param days
	 * @param yue
	 * @param ljParams
	 */
	public void updateMnrgdLingl(List<Yuemn> yue,Map<String,String> ljParams,Map<String,List<Integer>> zjcLingj) throws ServiceException{
		//更新数据库存
		for(String ljbh : zjcLingj.keySet()){
			ljParams.put("lingjbh", ljbh);
			ljParams.put("lingjsl", String.valueOf(zjcLingj.get(ljbh).get(0)));
			BigDecimal hour = this.getBigDecimal(zjcLingj.get(ljbh).get(0)).divide(this.getBigDecimal(yue.get(0).getBeat()),2,BigDecimal.ROUND_HALF_UP);
			ljParams.put("hour", hour.toString());
			ljParams.put("gongzbh", yue.get(0).getGongzbh());
			//更新零件数量
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.updateMnrgdLingj", ljParams);
		}
	}
	
	
	/**
	 * 查询更新过工时的工作日
	 * @date 2012-6-25
	 * @param line 更新产线实体集
	 * @param params 用户中心、产线号、周所在的开始时间、工业周期的结束时间
	 */
	@SuppressWarnings("unchecked")
	public List<Yuemn> selectLineTime(LineTimeUpdate line, Map<String,String> params)  throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryLineTime", params);
	}
	
	
	/**
	 * 查询当天产线工时
	 * @author 贺志国
	 * @param params 用户中心、产线号、产线组号、日期
	 * @date 2012-7-18
	 * @return List<Yuemn>
	 */
	@SuppressWarnings("unchecked")
	public List<Yuemn> selectCurrLineTime(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryCurrLineTime", params);
	}
	/**
	 * 初始化数据库表
	 * @param lines
	 * @param param
	 */
	public void initData(List<LineTimeUpdate> lines,Map<String,String> param)  throws ServiceException{
		logger.info("确定调整并验证通过后初始化数据库表");
		clearServantTable(param);
		insertServantTable(lines,param);
	}
	/**
	 * 更新生产线工作时间
	 * @param line
	 */
	public void updateLineWorkTime(LineTimeUpdate line) throws ServiceException { 
		logger.info("更新"+line.getLineNum());
		Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", line.getUsercenter());
		param.put("lineNum", line.getLineNum());
		param.put("chanxzbh", line.getLinegroup());
		param.put("startTime", line.getStartTime());
		param.put("jiessj", line.getEndTime());
		List<Yuemn> workTime = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryLineTime", param); 
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.updateLineWorkTime",line);
		Map<String,String> tesuTime = getTeSuTime(param);
		for( Yuemn yuBean :workTime){
			if(tesuTime.get(yuBean.getRiq())!=null && tesuTime.get(yuBean.getRiq()).length()>0){
				yuBean.setBiaos(line.getBiaos());
				yuBean.setHour(this.getBigDecimal(line.getWorkTime()).subtract(this.getBigDecimal(tesuTime.get(yuBean.getRiq()))).toString());
				yuBean.setGundmngs(this.getBigDecimal(line.getWorkTime()).subtract(this.getBigDecimal(tesuTime.get(yuBean.getRiq()))).toString());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.updateLineWorkTimeTesu",yuBean);
			}
		}
		logger.info("更新"+count+"条记录");
	}

	public Map<String,String> getTeSuTime(Map<String,String> params){
		Map<String,String> teSuTimeTemp = new HashMap<String,String>();
		List<Map<String, String>> cals = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryTeSuTime",params);  
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
	 * 检查输入数据格式，1、检验调整的工时是否是最小时间单的倍数
	 * @author 贺志国
	 * @date 2012-7-6
	 * @param linesList 调整产线集合
	 * @throws ServiceException 异常
	 */
	public String checkInputData(List<LineTimeUpdate> linesList,Map<String,String> params) throws ServiceException {
		logger.info("检查输入数据格式");
		//数据未变化
		if(linesList.size()==0){
			return "null";
		}
		List<Map<String,String>> paiccList = this.queryPaiccs(params);
		double mintime = Double.valueOf(String.valueOf(paiccList.get(0).get("MINTIME")));
		for(LineTimeUpdate chanxTime : linesList){
			if(chanxTime.getWorkTime().doubleValue()<8||chanxTime.getWorkTime().doubleValue()>22){
				logger.info("输入工时有误，必须是大于等于8小时或小于等于22小时之间的值");
				return "gongsQJerror";
			}
			double beis =  chanxTime.getWorkTime().doubleValue()%mintime;
			if(beis!=0){ //不能整除
				logger.info("输入工时有误，必须是最小工时的倍数");
				return "gongsBSerror";
			}
		}
		return "success";
	}
	
	
	/**
	 * 获取最小时间单位
	 * @author 贺志国
	 * @date 2012-7-6
	 * @param params 用户中心、产线组编号、当前日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryPaiccs(Map<String,String> params){
		Map<String,String> paiccsParams = new HashMap<String,String>();
		paiccsParams.put("usercenter",params.get("usercenter"));
		paiccsParams.put("chanxzbh",params.get("chanxzbh"));
		paiccsParams.put("currDate",DateUtil.curDateTime().substring(0,10));
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.getPCArg", paiccsParams);
	}
	
	
	/**
	 * 查询PC_YUEDMNJHMX_B表，获得工作编号集合
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param '用户中心'、'产线号'、'时间'
	 * @return List<Map<String,String>> list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYuedmnjhmxOfGongzbh(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryYuedmnjhmx_BOfGongzbh", param);
	}
	
	/**
	 * 查询PC_YUEDMNJHMX_B表，获得月模拟计划号
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param '用户中心'、'产线号'、'时间'
	 * @return List<Map<String,String>> list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYuedmnjhmxOfJihh(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryYuedmnjhmx_BOJihh", param);
	}
	
	/**
	 * 查询ckx_shengcx_lingj表，获得计划员所有的零件号
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param '产线号'、'用户中心'
	 * @return List<Map<String,String>> list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectCKX_SHENGCX_LINGJOFLingjbh(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryCKX_SHENGCX_LINGJOFLingjbh", param);
	}
	
	
	/**
	 * 初始化所有的产线零件经济批量
	 * @author 贺志国
	 * @date 2012-6-27
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> initChanxJJPL() throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryChanxLJ");
	}
	
    
    
    /**
	 * 期初库存查询
	 * @author 贺志国
	 * @date 2012-6-18
	 * @param page  分页查询
	 * @param param 查询参数
	 * @return Map<String,Object> 期初库存结果集
	 */
	public Map<String,Object> selectSGQickcAll(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("sgeq.querySGQickcAll", param, page);
	}
    
	/**
	 * 消息报警
	 * @Author: 贺志国
	 * @Date: 2012-7-2
	 * @version 1.0
	 * @param riq 当天日期
	 * @param message 排产错误消息
	 * @param params 基本参数  
	 */
	public void alarmMessage(String message,Map<String,String> params) throws ServiceException{
		logger.info("消息报警");
		String rq = this.splitStringToArray(params.get("riq"), "-");
		String jihh = params.get("usercenter").concat(params.get("lineNum")).concat(rq);
		Map<String,String> msg = new HashMap<String,String>();
		msg.put("xiaox", message);
		msg.put("chanxh", params.get("lineNum"));
		msg.put("usercenter", params.get("usercenter"));
		msg.put("leix", MSG_LEIX);
		msg.put("shij", params.get("riq"));
		msg.put("chanxzbh", params.get("chanxzbh"));
		msg.put("biaos",params.get("biaos"));
		msg.put("jihh", jihh);
		//写数据库
		this.insertPCMessage(msg);
		
	}
	
	/**
	 * 将报警信息插入排产报警表
	 * @author 贺志国
	 * @date 2012-7-2
	 * @param msg 报警消息参数
	 */
	@Transactional
	public void insertPCMessage(Map<String,String> msg) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("sgeq.insertMessage", msg);
	}

	/**
	 * 分隔字符串，返回一个新的字符串
	 * @author 贺志国
	 * @date 2012-7-2
	 * @param str 要分隔的字符串
	 * @param symbol 分隔符
	 * @return 分隔后的字符串
	 */
	public String splitStringToArray(String str,String symbol) throws ServiceException{
		String [] arrRiq = str.split(symbol);
    	StringBuffer buf = new StringBuffer("");
    	for(int i=0;i<arrRiq.length;i++){
    		buf.append(arrRiq[i]);
    	}
    	return buf.toString();
	}
	
	
	/**
	 * 返回某条产线所有工作日
	 * @author 贺志国
	 * @date 2012-6-30
	 * @param params 包含用户中心,产线,产线组和排产时间范围
	 * @return List<String> 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getWorkCalendarCX(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryChanxGZR",params);
	}
	
	/**
	 * @description 得到下一个工作日的日期
	 * @author 贺志国
	 * @date 2012-06-30
	 * @param params	 包含用户中心,产线,产线组和排产时间范围
	 * @return String 返回下一天的工作日，如果返回为空字符说明是最后一天，无需再计划期初库存
	 */
	public String getNextWorkDay(String riq,List<String> workCalCXZ){
		logger.info("得到下一个工作日的日期");
		String result = "";
		//List<String> workCalCXZ = this.getWorkCalendarCX(params);
		int fromIndex = findWorkCalIndex(workCalCXZ, riq);
		if(workCalCXZ.size()>=fromIndex+2){
			result = workCalCXZ.get(fromIndex+1);
		}
		return result;
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
	 * 封装排产工作日的工作编号
	 * @author 贺志国
	 * @date 2012-7-18
	 * @param pcGzrList 排产工作日集合
	 * @return Map<String,String> ,key/value ，2012-07-30:'UXUX5L120120730','UXUX5L220120730'
	 */
	public Map<String,String> wrapGongzbh(List<String> pcGzrList,String usercenter){
		Map<String,String> daysGongzbhMap = new HashMap<String,String>();
		Map<String,String> gParam = new HashMap<String,String>();
		gParam.put("usercenter", usercenter);
		for(String riq : pcGzrList){
			gParam.put("riq",riq);
			//查询每一天的工作编号并封装到Map中
			List<String> daysGongzbh = this.daysGongzbh(gParam);
			daysGongzbhMap.put(riq, CollectionUtil.listToString(daysGongzbh));
		}
		return daysGongzbhMap;
	}
	
	
	/**
	 * 查询当天的工作编号
	 * @author 贺志国
	 * @date 2012-7-18
	 * @param riq 日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> daysGongzbh(Map<String,String> gParam){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryDayGongzbh",gParam);
	}
	
	
	/**
	 * 返回产线所在的排产明细所在周最大工时
	 * @author 贺志国
	 * @date 2012-8-1
	 * @param params 用户中心，时间范围，产线号
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPaicmx(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("sgeq.queryPaicmx",params);
	}
	
	/**
	 * 根据排产日期得到需要计算的生产线
	 * @author 王国首
	 * @date 2013-10-15
	 * @param params 产线号，日期
	 * @return List<LineTimeUpdate>
	 */	
	public List<LineTimeUpdate> getlinesChange(List<LineTimeUpdate> linesList,String riq){
		List<LineTimeUpdate> line = new ArrayList<LineTimeUpdate>();
		for(int i = 0;i<linesList.size();i++){
			LineTimeUpdate LineTime = linesList.get(i);
			if(riq.compareTo(LineTime.getStartTime())>=0 && LineTime.getEndTime().compareTo(riq)>=0){
				line.add(linesList.get(i));
			}
		}
		return line;
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
