package com.athena.ckx.util.Impl;


import java.util.List;

import javax.jws.WebService;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.util.PeislxWebservice;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;




@SuppressWarnings("rawtypes")
@WebService(endpointInterface="com.athena.ckx.util.PeislxWebservice", serviceName="/peislxWebservice")
@Component
public class PeislxWebserviceImpl extends BaseService implements PeislxWebservice
{
	@SuppressWarnings("unchecked")
	public List<Peislb> queryPeislx() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryPeislbWsl");
	}

	public Integer querypeislxTB(CkxLingjxhd bean) {
		return (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.querypeislxTBZX", bean);
	}
}
