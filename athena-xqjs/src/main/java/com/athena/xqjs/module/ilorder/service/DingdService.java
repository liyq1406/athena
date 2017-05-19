package com.athena.xqjs.module.ilorder.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.Cache;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:订单类
 * </p>
 * <p>
 * Description:订单类
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
 * @date 2012-01-03
 */
@Component
public class DingdService extends BaseService {
	
	/**
	 * 插入操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * 
	 */
	@Transactional
	public boolean doInsert(Dingd bean) {
		int count = 0;
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", bean.getDingdh());
		CommonFun.mapPrint(map, "订单插入doInsert方法中查询已有订单参数map");
		CommonFun.logger.debug("订单插入doInsert方法中查询已有订单的sql语句为：ilorder.queryDingdByDingdh");
		Dingd object = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdByDingdh", map);
		map.clear();
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		if (object == null) {
			CommonFun.logger.debug("订单插入doInsert方法中插入订单的sql语句为：ilorder.insertDingd");
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingd", bean);
		}
		return count > 0;
	}

	/**
	 * 根据订单号查询实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2011-12-09
	 * @参数：String dingdh,String fahzq
	 */
	public Dingd queryDingdByDingdh(Map<String, String> map) {
		CommonFun.mapPrint(map, "根据订单号查询订单方法queryDingdByDingdh参数map");
		CommonFun.logger.debug("根据订单号查询订单方法queryDingdByDingdh使用的sql语句为：ilorder.queryDingdByDingdh");
		return (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdByDingdh", map);

	}
	
	/**
	 * 根据订单号查询实体和发运周期查最近一个订单
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @date: 2011-12-13
	 * @参数：String dingdh,String fahzq
	 */
	public List<Dingd> queryLastDingdByDingdh(Map<String, String> map) {
		CommonFun.mapPrint(map, "根据订单号查询订单方法queryLastDingdByDingdh参数map");
		CommonFun.logger.debug("根据订单号查询订单方法queryLastDingdByDingdh使用的sql语句为：ilorder.queryLastDingdByDingdh");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdByDingdh", map);

	}
///////////////wuyichao/////////////
	public Dingd queryLastDingdByDingdhNew(Map<String, String> map) {
		return (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdByDingdhNew", map);
	}
///////////////wuyichao/////////////
	
	public List<Dingd> queryDdListByDingdh(Map<String, String> map) {
		CommonFun.mapPrint(map, "根据订单号查询订单方法queryDingdByDingdh参数map");
		CommonFun.logger.debug("根据订单号查询订单方法queryDingdByDingdh使用的sql语句为：ilorder.queryDingdByDingdh");
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdByDingdh", map);

	}
	
	/**
	 * 更具订单号查询处理类型
	 * @param dingds 订单集合
	 * @return 处理类型
	 */
	public String queryChullxByDingdh(ArrayList<Dingd> dingdList) {
		//拼接订单号
		StringBuilder dingdh = new StringBuilder("");
		for (int i = 0; i < dingdList.size(); i++) {
			dingdh.append("'"+dingdList.get(i).getDingdh()+"',");
		}
		dingdh.deleteCharAt(dingdh.lastIndexOf(","));
		//根据订单号查询处理类型
		List<String> chullxs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryChullxByDingdh", dingdh.toString());
		//如果只有一条直接返回
		if(chullxs.size() == 1){
			return chullxs.get(0);
		}else{
			for (int i = 1; i < chullxs.size(); i++) {
				String chullx1 = chullxs.get(i - 1);
				String chullx2 = chullxs.get(i);
				if(!chullx1.equals(chullx2)){
					return "false";
				}
			}
			return chullxs.get(0);
		}
	}

	/**
	 * KD订单定义查询
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-12
	 * @参数：map
	 */
	public Map<String, Object> kdQueryList(Pageable page, Map parrams) {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.kdQueryDingd", parrams, page);
	}

	/**
	 * KD订单定义查询 ------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-12
	 * @参数：map
	 */
	public Map<String, Object> kdQueryListForShengx(Pageable page, Map parrams) {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.kdQueryDingdForShengx", parrams, page);
	}
	
	
	
	//KD订单修改即生效导出
	public List<Dingd> exportDingdXls(Map<String,String> parrams) {
		List<Dingd> dingdList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.kdQueryDingdForEXP", parrams);
		List resultList = new ArrayList();
		CacheManager cmDingdlx = CacheManager.getInstance();
		Cache cacheDingdlx = cmDingdlx.getCacheInfo("queryDingdlx");
		List<CacheValue> cacheDingdlxValue = (List<CacheValue>) cacheDingdlx.getCacheValue();
		Map<String,String> cacheDingdlxMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheDingdlxValue){
			cacheDingdlxMap.put(cvalue.getKey(),cvalue.getValue());
		}
		Cache cacheShifjsyhl = cmDingdlx.getCacheInfo("queryShifyjsyhl");
		List<CacheValue> cacheShifjsyhlValue = (List<CacheValue>) cacheShifjsyhl.getCacheValue();
		Map<String,String> cacheShifjsyhlMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheShifjsyhlValue){
			cacheShifjsyhlMap.put(cvalue.getKey(),cvalue.getValue());
		}
		Cache cacheDingdzt = cmDingdlx.getCacheInfo("queryDingdzt");
		List<CacheValue> cacheDingdztValue = (List<CacheValue>) cacheDingdzt.getCacheValue();
		Map<String,String> cacheDingdztMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheDingdztValue){
			cacheDingdztMap.put(cvalue.getKey(),cvalue.getValue());
		}
		
		for(Dingd dingd:dingdList){
			String dingdlxString = dingd.getDingdlx();
			String shifjsYhlString = dingd.getShifyjsyhl();
			String dingdztString = dingd.getDingdzt();
			if(cacheDingdlxMap.get(dingdlxString)!=null){
				dingd.setDingdlx(cacheDingdlxMap.get(dingdlxString));
			}
			if(cacheShifjsyhlMap.get(shifjsYhlString)!=null){
				dingd.setShifyjsyhl(cacheShifjsyhlMap.get(shifjsYhlString));
			}
			if(cacheDingdztMap.get(dingdztString)!=null){
				dingd.setDingdzt(cacheDingdztMap.get(dingdztString));
			}
			resultList.add(dingd);
		}
		return resultList;
	}

	/**
	 * KD订单查询,返回List ------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-12
	 * @参数：map
	 */
	public List<Dingd> queryListDingd() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingd");
	}
	
	public List<Dingd> queryDingNum(Map<String, String> map) {
		
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdNum",map);
	}

	/**
	 * KD订单删除操作 ------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-12
	 * @参数：实体
	 */
	@Transactional
	public boolean doRemove(Dingd bean) {
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingd", bean);
		return count > 0;
	}

	/**
	 * KD订单修改操作 ------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-12
	 * @参数：实体
	 */
	@Transactional
	public boolean doUpdate(Dingd bean) {
		CommonFun.logger.debug("KD订单修改操作doUpdate方法的sql语句为：ilorder.updateDingd");
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateDingd", bean);
		return count>0;
	}

	/**
	 * KD特殊订单特殊订单获取 ------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-12
	 * @参数：订单号，发运周期
	 */
	public String getDingdh(String dingdlx, String Jiszq) {
		String year = Jiszq.substring(0, 4);
		String dingdh = year.substring(year.length() - 2, year.length()) + "E"
				+ Jiszq.substring(Jiszq.length() - 2, Jiszq.length());
		Map<String,String> map = new HashMap<String,String>();
		map.put("dingdh", dingdh);
		List<Dingd> listDingd = this.queryDingNum(map);
		String dingdNum = null;
		if (!listDingd.isEmpty()) {
			dingdNum = listDingd.get(0).getDingdh();
		}
		DecimalFormat myFormat = new DecimalFormat("000");
		if (dingdNum == null) {
			dingdh += "001";
		}else{
			dingdh += myFormat.format(Integer.parseInt(dingdNum.substring(dingdNum.length() - 3, dingdNum.length())) + 1);
		}
		
		return dingdh;
	}

	/**
	 * 柔性检查中获取发运周期
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-3-8
	 * @参数：发运周期
	 */
	public static String getXuhao(String fahzq) {
		String xuhao = null;
		if (null != fahzq && !"".equals(fahzq)) {
			String front = fahzq.substring(0, 4);
			CommonFun.logger.debug("柔性检查中获取发运周期getXuhao方法front="+front);
			String back = fahzq.substring(4, fahzq.length());
			CommonFun.logger.debug("柔性检查中获取发运周期getXuhao方法back="+back);
			if (Integer.parseInt(back) - 1 == 0) {
				xuhao = Integer.parseInt(front) - 1 + "12";
				CommonFun.logger.debug("柔性检查中获取发运周期getXuhao方法在（Integer.parseInt(back) - 1 == 0）时xuhao="+xuhao);
			} else {
				xuhao = Integer.parseInt(fahzq) - 1 + "";
				CommonFun.logger.debug("柔性检查中获取发运周期getXuhao方法不在（Integer.parseInt(back) - 1 == 0）时xuhao="+xuhao);
			}
		}
		return xuhao;
	}

	/**
	 * 在订单表中查询状态为已定义的订单 ------未测试
	 * 
	 * @author 李智
	 * @date 2012-3-2
	 * @param param
	 *            查询条件
	 * @return List<Dingd> 检索结果
	 */
	public Map queryDingdhs(Pageable page,Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdByZt",param,page);
	}
	
	/**
	 * 在订单表中查询状态为已定义的订单 ------未测试
	 * 
	 * @author 李智
	 * @date 2012-3-2
	 * @param param
	 *            查询条件
	 * @return List<Dingd> 检索结果
	 */
	public List<Dingd> queryDingdh(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdByZt", param);
	}

	/**
	 * 按订单类型查询订单 ------未测试
	 * 
	 * @author 李智
	 * @date 2012-3-7
	 * @param page
	 *            分页显示
	 * @param param
	 *            查询条件
	 * @return Map 检索结果
	 */
	public Map<String, Object> queryDingdBylx(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdBylx", param, page);
	}
	
	
	public List<Dingd> queryOrderNumbers(String dingdh) {
		Map<String, String> map = new HashMap<String, String>();
		//map.put("usercenter", usercenter);
		map.put("dingdh", dingdh.substring(0, 5));
		CommonFun.mapPrint(map, "订单查找queryOrderNumbers方法参数map");
		CommonFun.logger.debug("订单查找queryOrderNumbers方法的sql语句为：ilorder.queryOrderNums");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryOrderNums", map);
	}
	
	
	/**
	 * 更新拒绝状态的订单为制作中
	 * */
	public void updateDingdzt(String dingdh) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("dingdh", dingdh);
		Dingd dd = this.queryDingdByDingdh(param);
		if (dd != null && dd.getDingdzt().equals(Const.DINGD_STATUS_JUJ)) {
			Dingd dingd = new Dingd() ;
			dingd.setDingdzt(Const.DINGD_STATUS_ZZZ) ;
			dingd.setDingdh(dingdh) ;
			this.doUpdate(dingd) ;
		}
	}

	/**
	 * @param bean
	 * @param map
	 * @return
	 */
	public Map<String, Object> querySumDingd(Dingd bean, Map<String, String> map) {
		return (Map<String, Object>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.querySumDingd",map,bean);
		
	}
	
	/**
	 * 查询订单状态状态
	 */
	public  int queryDingdzt(String dingdh){
		String zhuangt= (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdzt",dingdh);
		if(StringUtils.isBlank(zhuangt)|| !StringUtils.isNumeric(zhuangt)){
			return -99;
		}
		return Integer.valueOf(zhuangt);
	}
}