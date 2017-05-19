package com.athena.xqjs.module.common.service;

import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class GongyxhdService extends BaseService{
	
	/**
	 * 根据消耗点查询流水号
	 * @author 李智
	 * @date 2012-3-23
	 * @param param 查询条件
	 * @return String 流水号
	 */
	public String queryLiushByXhd(Map<String, String> param) {
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryLiushByXhd", param);
	}
}