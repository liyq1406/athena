package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;

import javax.jws.WebService;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * 按需初始化出库量、异常消耗点查询
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-7-15
 */
@SuppressWarnings("rawtypes")
@WebService(endpointInterface="com.athena.xqjs.module.anxorder.service.AnxCshWebservice", serviceName="/anxCshWebservice")
@Component
public class AnxCshWebserviceImpl extends BaseService implements AnxCshWebservice {

	@Override
	public  Chushzyb  queryAnxcshYcxhl(Chushzyb bean){
		BigDecimal chukl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryChuklOfLingjxhb",bean));
		BigDecimal yicxhl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryYicxhlOfYicsbcz",bean));
		bean.setChukl(chukl);
		bean.setYicxhl(yicxhl);
		return bean;
	}

}
