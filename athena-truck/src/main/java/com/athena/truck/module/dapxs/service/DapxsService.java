package com.athena.truck.module.dapxs.service;


import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chelpd;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Shijdzt;
import com.athena.truck.entity.Yund;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
/**
 * 大屏显示
 */
@Component 
public class DapxsService extends BaseService{

//查询区域名称
@SuppressWarnings("rawtypes")
public Dengdqy queryQuymc(Map map){
	return 	(Dengdqy) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dapxs.queryQuymc", map);
}


//查询等待区域下的车辆排队信息
@SuppressWarnings({ "rawtypes", "unchecked" })
public List<Chelpd> queryShenbpdxx(Map map){
	return 	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dapxs.queryShenbpdxx", map);
}

//查询等待区域下入厂的车辆信息
@SuppressWarnings({ "rawtypes", "unchecked" })
public List<Yund> queryRucxx(Map map){
	return 	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dapxs.queryRucxx", map);
}
}
