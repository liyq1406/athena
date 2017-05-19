package com.athena.xqjs.module.common.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:小火车运输时刻service
 * </p>
 * <p>
 * Description:小火车运输时刻service
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
 * @date 2012-3-23
 */

@Component
public class XiaohcysskService extends BaseService {

	/**
	 * 查询小火车运输时刻实体(针对备货时间和上线时间)
	 * @author 李明
	 * @version v1.0
	 * @date 2012-3-23 
	 * @参数说明：String usercenter, 用户中心，String riq 日期,小火车编号，产线编号
	 */
	public List<Xiaohcyssk> queryXiaohcysskObject(Map<String,String> map) {
		List<Xiaohcyssk> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryXiaohcyssk", map);
		return list ;
	}
	
	/**
	 * 查询小火车运输时刻实体(针对备货时间和上线时间)
	 * @author 李明
	 * @version v1.0
	 * @date 2012-3-23 
	 * @参数说明：String usercenter, 用户中心，String riq 日期,小火车编号，产线编号
	 */
	public List<Xiaohcyssk> queryXiaohcysskObjectByShangx(Map<String,String> map) {
		List<Xiaohcyssk> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryXiaohcyssk", map);
		return list ;
	}

}