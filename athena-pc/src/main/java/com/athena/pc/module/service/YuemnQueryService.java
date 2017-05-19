package com.athena.pc.module.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.EquilibLJ;
import com.athena.pc.entity.Yuemn;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * <p>
 * Title:月模拟逻辑处理类
 * </p>
 * <p>
 * Description:定义逻辑处理方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-2-13
 */
@Component
public class YuemnQueryService extends BaseService<Yuemn> {
	static Logger logger = Logger.getLogger(YuemnQueryService.class.getName());
	/**
	 * 月模拟查询
	 * @author 贺志国
	 * @date 2012-2-13
	 * @param param 参数
	 * @return List<Map<String,String>>
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Yuemn> selectYuemn(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryYuemn",param);
	}
	
	
	/**
	 * 产线查询
	 * @date 2012-2-13
	 * @param param 参数
	 * @return List<Map<String,String>>
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanx(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryShengcx",param);
	}
	
	
	/**
	 * 工业周期查询
	 * @param param 参数
	 * @return List<Map<String,String>> 工业周期集合
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGongyzq(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryGongyzq", param);
	}

	
	/**
	 * 工业周期查询
	 * @param param 参数
	 * @return List<Map<String,String>> 工业周期集合
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGongyzqstart(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryGongyzqstart", param);
	}
	
	/**
	 * 工业周期时间范围查询
	 * @param gongyzq 工业周期号
	 * @return List<Map<String,String>> 工业周期时间范围
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGyzqsjfw(String gongyzq)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryGyzqfw", gongyzq);
	}

	/**
	 * 根据工业周期查义上一个工业周期
	 * @param gongyzq 当前工业周期
	 * @return String 上一个工业周期
	 */
	public String selectUpGyzq(String gongyzq)throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("yuemn.queryUpGyzq", gongyzq);
	}
	
	/**
	 * 根据工业周期查义下一个工业周期
	 * @param gongyzq 当前工业周期
	 * @return String 下一个工业周期
	 */
	public String selectNextGyzq(String gongyzq)throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("yuemn.queryNextGyzq", gongyzq);
	}
	
	/**
	 * 查询排产参数设置
	 * @param param
	 * @return List<Map<String,String>> 排产参数
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPaiccs(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryPaiccs", param);
	} 
	
	
	/**
	 * 更新月模拟计划表中的是否确认状态为已确认'Y'
	 * @param param 月模拟计划号
	 * @return int 更新成功个数
	 */
	@Transactional
	public int updateYuedmnjhb(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("yuemn.updateYuedmnjhb", param);
		
	}
	
	/**
	 * 查询月模拟计划表中的确认状态是否已经确认
	 * @param param 月模拟计划号
	 * @return String 确认状态
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectShifqrOfYuedmnjh(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryShifqr",param);
		
	}
	
	
	/**
	 * 查询月模拟错误消息
	 * @param param 参数
	 * @return String 错误消息
	 */
	public String selectErrorMessage(Map<String,String> param)throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("yuemn.queryErrorMessage", param);
	}
	
	/**
	 * 查询某条产线下某天的月模拟错误消息
	 * @param param 参数
	 * @return String 错误消息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectAllErrorMessage(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryAllErrorMessage", param);
	}
	
	/**
	 * 查询某条产线所有的月模拟错误消息
	 * @param param 参数
	 * @return String 错误消息
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> selectMessage(Yuemn bean,Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("yuemn.queryMessage",param,bean);
	}
	
	/**
	 * 查询产线详细信息
	 * @param param 查询参数
	 * @return List<Map<String,String>>某条产线集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanxDetail(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryChanxDetail", param);
	}
	
	/**
	 * 月模拟计划日产线零件查询
	 * @param gongzbh 工作编号
	 * @return List<Map<String,String>> 产线零件集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanxLj(String gongzbh){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryChanxLj", gongzbh);
	}
	
	/**
	 * 月模拟导出报表数据
	 * @param param 参数
	 * @return dataSource 数据源集合
	 */
	public void getDownload(Map<String,String> param, Map<String, Object> dataSource){
		List<Map<String,String>> gyzqsjfw = selectGyzqsjfw(param.get("gongyzq"));
		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		dataSource.put("kaissjChina", CollectionUtil.getYearMonthDayChina(param.get("kaissj")));
		dataSource.put("jiessjChina", CollectionUtil.getYearMonthDayChina(param.get("jiessj")));
		dataSource.put("downloadDay", CollectionUtil.getTimeNow(CollectionUtil.YMDFORMAT));

		List<Map<String, String>> chanxzrl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryChanxzrl",param);
		List<String> riq = new ArrayList<String>();
		List<String> riqymd = new ArrayList<String>();
		List<String> xingq = new ArrayList<String>();
//		Map<String, String> daysql = new HashMap<String, String>();
		String [] xingqihao = {"周一","周二","周三","周四","周五","周六","周日"};
		StringBuffer sqlName = new StringBuffer();
		StringBuffer sqlNameOut = new StringBuffer();
		int i = 0;
		for(Map<String, String> chanxzrlMap : chanxzrl){
			riq.add(String.valueOf(chanxzrlMap.get("DAYNUM")));
			xingq.add(xingqihao[Integer.parseInt(String.valueOf(chanxzrlMap.get("XINGQ")))-1]);
			sqlName.append(",").append("sum(decode(ymn.shij,'").append(chanxzrlMap.get("RIQ")).append("',ymn.lingjsl,0)) as A").append(i);
			sqlNameOut.append(",").append("outlj.A").append(i);
			riqymd.add(chanxzrlMap.get("RIQ"));
			i++;
		}
		param.put("sqlName", sqlName.toString());
		param.put("sqlNameOut", sqlNameOut.toString());
		dataSource.put("tiannum", riq.size());
		dataSource.put("titlecol", 4+riq.size());
		dataSource.put("riq", riq);
		dataSource.put("xingq", xingq);
		dataSource.put("riqymd", riqymd);
		List<Map<String, String>> chanxLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryChanxLingjOut",param);
//		if(param.get("chanxFlag") != null &&  "Y".equals(param.get("chanxFlag"))){
//		}
		paraDownloadData( param,  dataSource,chanxzrl,chanxLingj);
		logger.info("生成带产线零件数据成功.");
		param.put("chanxFlag", "N");
		List<Map<String, String>> Lingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryLingjOut",param);
		paraDownloadData( param,  dataSource,chanxzrl,Lingj);
		dataSource.put("titlecol_gs", 1+riq.size());
		paraGongs(param,  dataSource,chanxzrl);
	}
	
	public void paraDownloadData(Map<String,String> param, Map<String, Object> dataSource,List<Map<String, String>> chanxzrl,List<Map<String, String>> chanxLingj){
		String Lie = String.valueOf(chanxzrl.size());
		Map<String, Object> chanxOne = new HashMap<String, Object>();
		for( Map<String, String> chanxLingjMap: chanxLingj){
			Map<String, Object> lingjOne = new HashMap<String, Object>();
			List<Map<String, Object>> LingJList = null;
			if(chanxOne.containsKey(chanxLingjMap.get("CHANXH"))){
				LingJList = (List<Map<String, Object>>)chanxOne.get(chanxLingjMap.get("CHANXH"));
			}else{
				LingJList = new ArrayList<Map<String, Object>>();
			}
			chanxLingjMap.put("lingjKS", "1");
			chanxLingjMap.put("lingjJS", Lie);
			lingjOne.put("nameMap", chanxLingjMap);
			List value = new ArrayList();
			for(int i = 0;i<chanxzrl.size();i++){
				value.add(chanxLingjMap.get("A"+i));
			}
			lingjOne.put("valueList", value);
			LingJList.add(lingjOne);
			chanxOne.put(chanxLingjMap.get("CHANXH"), LingJList);
		}
		Object[] a = chanxOne.keySet().toArray();
		Arrays.sort(a);   
		List<Map<String, Object>> chanx =new ArrayList<Map<String, Object>>();
		 for(Object key :a) {
			Map<String, Object> chanxOut = new HashMap<String, Object>();
			Map<String, String> chanxMap = new HashMap<String, String>();
			List<Map<String, Object>> LingJList = (List<Map<String, Object>>)chanxOne.get(key);
			chanxMap.put("chanxh", key.toString());
			chanxMap.put("chanxKS", String.valueOf(0-LingJList.size()));
			chanxMap.put("chanxJS", "-1");
			chanxMap.put("chanxMergeDown", String.valueOf(LingJList.size()-1));
			chanxOut.put("cxMap", chanxMap);
			List value = new ArrayList();
			for(int i = 0;i<chanxzrl.size();i++){
				value.add(chanxMap.get("chanxKS"));
			}
			chanxOut.put("cxList", value);
			chanxOut.put("ljList", LingJList);
			chanx.add(chanxOut);
		}
		if("Y".equals(param.get("chanxFlag"))){
			dataSource.put("lingjcx", chanx);
		}else{
			dataSource.put("lingjnotcx", chanx);
		}
		
	}
	
	public void paraGongs(Map<String,String> param, Map<String, Object> dataSource,List<Map<String, String>> chanxzrl){
		List<Map<String, String>> chanxLingjGs =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryLingjGongs",param);
		List<Map<String,String>> nullList = new ArrayList<Map<String,String>>();
		Map<String, String> riq_num = new HashMap<String, String>();
		for(int i = 0;i<chanxzrl.size();i++){
			Map<String, String> chanxzrlMap = chanxzrl.get(i);
			Map<String,String> temp = new HashMap<String,String>();
			temp.put("HOUR", "");
			nullList.add(temp);
			riq_num.put(chanxzrlMap.get("RIQ"), String.valueOf(i));
		}
		Map<String, String> baoz = this.getBaozxh(param);
		String Lie = String.valueOf(chanxzrl.size());
		Map<String, Object> chanxOne = new HashMap<String, Object>();
		Map<String, String> chanxGongs = new HashMap<String, String>();
		for( Map<String, String> chanxLingjGsMap: chanxLingjGs){
			List<Map<String,String>> LingJList = null;
			String chanxGongsAll = "0";
			if(chanxOne.containsKey(chanxLingjGsMap.get("CHANXH"))){
				LingJList = (List<Map<String,String>>)chanxOne.get(chanxLingjGsMap.get("CHANXH"));
			}else{
				LingJList = new ArrayList<Map<String,String>>();
				List<Map<String,String>> tmp = listMapCopy(nullList);
				LingJList.addAll(tmp);
			}
			String riq = chanxLingjGsMap.get("RIQ");
			if(riq_num.get(riq) != null){
				int num = Integer.parseInt(riq_num.get(riq));
				Map<String, String> gongsMap = LingJList.get(num);
				gongsMap.put("HOUR", chanxLingjGsMap.get("HOUR"));
				gongsMap.put("DANW", "小时");
				LingJList.set(num, gongsMap);
			}
			chanxOne.put(chanxLingjGsMap.get("CHANXH"), LingJList);
			if(chanxGongs.containsKey(chanxLingjGsMap.get("CHANXH"))){
				chanxGongsAll = chanxGongs.get(chanxLingjGsMap.get("CHANXH"));
			}
			chanxGongs.put(chanxLingjGsMap.get("CHANXH"), String.valueOf(Double.parseDouble(chanxGongsAll)+Double.parseDouble(String.valueOf(chanxLingjGsMap.get("HOUR")))));
		}
		Object[] a = chanxOne.keySet().toArray();
		Arrays.sort(a);   
		List<Map<String, Object>> chanx =new ArrayList<Map<String, Object>>();
		for(Object key :a) {
			Map<String, Object> chanxOut = new HashMap<String, Object>();
			Map<String, String> chanxMap = new HashMap<String, String>();
			List<Map<String, String>> LingJList = (List<Map<String, String>>)chanxOne.get(key);
			chanxMap.put("chanxh", key.toString());
			String baozxh = baoz.get(chanxMap.get("chanxh"))!=null ? baoz.get(chanxMap.get("chanxh")):"";
			chanxMap.put("chanxKS", String.valueOf(0-LingJList.size()));
			chanxMap.put("chanxJS", "-1");
			chanxMap.put("chanxGongs", chanxGongs.get(chanxMap.get("chanxh")));
			chanxMap.put("chanxbaoz", baozxh);
			chanxOut.put("cxMap", chanxMap);
			List value = new ArrayList();
			for(int i = 0;i<chanxzrl.size();i++){
				value.add(chanxMap.get("chanxKS"));
			}
			chanxOut.put("cxList", value);
			chanxOut.put("ljList", LingJList);
			chanx.add(chanxOut);
		}
		dataSource.put("lingjgongs", chanx);
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
	 * @description 得到包装消耗
	 * @author 王国首
	 * @date 2012-2-2
	 * @param param		参数
	 * @return result	拷贝完后的List
	 */
	public Map<String, String> getBaozxh(Map<String,String> param){
		Map<String, String> result = new HashMap<String, String>();
		List<Map<String, String>> baozhList =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryBaozxh",param);
		for( Map<String,String> baozhMap: baozhList){
			StringBuffer baozStr = new StringBuffer();
			if(result.containsKey(baozhMap.get("CHANXH"))){
				baozStr.append(result.get(baozhMap.get("CHANXH"))).append(",");
			}
			baozStr.append(baozhMap.get("BAOZDM")).append(":").append(String.valueOf(baozhMap.get("AMOUNT")));
			result.put(baozhMap.get("CHANXH"), baozStr.toString());
		}
		return result;
	}
	
	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "yuemn";
	}
	
	
}
