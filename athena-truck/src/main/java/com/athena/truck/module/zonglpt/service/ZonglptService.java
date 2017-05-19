package com.athena.truck.module.zonglpt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chac;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Shijdzt;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class ZonglptService extends BaseService<Chac>{
	
	/**
	 * 获得命名空间
	 * @author liushuang
	 * @date 2015-1-20
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	/**
	 * 查询
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public List listMc(Map<String,String> map) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryQuymc",map);
	}
	
	@SuppressWarnings("rawtypes")
	public List listMcinner(Map map) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.querydaztbyquybhinner",map);
	}
	
	@SuppressWarnings("rawtypes")
	public List listDazt(Map map) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.querydaztbyquybh",map);
	}
	
	@SuppressWarnings("rawtypes")
	public String selectsbkcs(Map map) throws ServiceException {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.querysbkcbydazt",map);
	}
	
	@SuppressWarnings("rawtypes")
	public String selectpdkcs(Map map) throws ServiceException {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.querypdkcbydazt",map);
	}
	@SuppressWarnings("rawtypes")
	public String selectfpkcs(Map map) throws ServiceException {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.queryfpkcbydazt",map);
	}
	
	@SuppressWarnings("rawtypes")
	public List listyunda(Map map) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryunda",map);
	}
	
	@SuppressWarnings("rawtypes")
	public String selectfkwccs(Map map) throws ServiceException {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.queryfkwccbydazt",map);
	}
	
	@SuppressWarnings("rawtypes")
	public List listCwinner(Map map) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.querychewbyquybhinner",map);
	}
}
