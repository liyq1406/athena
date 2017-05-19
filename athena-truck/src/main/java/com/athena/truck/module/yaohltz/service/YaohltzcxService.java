package com.athena.truck.module.yaohltz.service;

import java.util.List;
import java.util.Map;


import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Yaohltzcx;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 要货令调整查询
 * @author chenpeng
 * @date 2015-1-29
 */
@Component
public class YaohltzcxService  extends BaseService<Yaohltzcx> {
	
	
	/**
	 * 获得命名空间
	 * @author chenpeng
	 * @date 2015-1-29
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	
	/**
	 * 查询卸货站台
	 * @return List
	 * @author chenpeng
	 * @date 2015-1-29
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> list(Map<String,String> params) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryXiehzt",params);
	}
	
	
	/**
	 * 获得要货令调整信息
	 * @param bean
	 * @return List
	 * @author chenpeng
	 * @date 2015-1-29
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List listYaohltz(Yaohltzcx bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryYaohltzcx",bean);
	}
	
	
	
}
