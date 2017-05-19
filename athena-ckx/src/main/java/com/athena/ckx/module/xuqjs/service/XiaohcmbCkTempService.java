package com.athena.ckx.module.xuqjs.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.XiaohcmbCk;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * @description小火车运输时刻模板(执行层)
 * @author hj
 * @date 2013-12-6
 */
@Component
public class XiaohcmbCkTempService extends BaseService<XiaohcmbCk> {
	private  Logger logger =Logger.getLogger(XiaohcmbCkTempService.class);
	private String creator;
	/**
	 * @description获得命名空间
	 * @author denggq
	 * @date 2012-4-11
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 根据趟次和起始流水号，删除当前数据和当前数据以下的数据，并根据max(smonbhlsh) = 999999999保存小火车模板数据
	 * @param bean
	 */
	@Transactional
	public void removeMbByQislsh(XiaohcmbCk bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.addXiaohcmbMax",bean);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.removeXiaohcmbCk",bean);
	}
	/**
	 * 将计算完的小火车模板插入系统中，并将max表数据更新到小火车模板中
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void insertXiaohcmb(List<XiaohcmbCk> listXiaohcmbCk,Shengcx shengcx){
		Map param = new HashMap();
		param.put("usercenter", shengcx.getUsercenter());
		param.put("shengcxbh", shengcx.getShengcxbh());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.addXiaohcmcByChanx", listXiaohcmbCk);
		List<Map<String,Object>> xiaohcmbCkMap = (List<Map<String,Object>>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohcmbAndMax", param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.updateXiaohcmbMaxSmon",xiaohcmbCkMap);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiaohcmbMax",param);
		logger.info("4270批量"+shengcx.getShengcxbh()+"生产线查询出max条数为："+xiaohcmbCkMap.size());
	}
	
	/**
	 * 将计算完的小火车模板插入系统中，并将max表数据更新到小火车模板中
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void insertXXiaohcmb(List<XiaohcmbCk> listXiaohcmbCk,Shengcx shengcx){		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch("ts_ckx.addXXiaohcmcByChanx", listXiaohcmbCk);
	}
	
	/**
	 * 将计算完的小火车模板插入系统中，并将max表数据更新到小火车模板中
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void insertXXiaohcmb(XiaohcmbCk xiaohcmbck,Shengcx shengcx){		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.addXXiaohcmcByChanx", xiaohcmbck);
	}
}
