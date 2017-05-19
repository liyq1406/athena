package com.athena.xqjs.module.ilorder.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

@Component
public class AnxlsOrderWebEeService extends BaseService {

	/**
	 * 查询工作时间模板向后
	 * @param map 参数
	 * @return 时间
	 */
	public String queryGongzsjmbH(Map<String,String> map){
		return CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbH",map));
	}
	
	/**
	 * 查询小火车运输时刻实体(针对备货时间和上线时间) 
	 * @参数说明：String usercenter, 用户中心，String riq 日期,小火车编号，产线编号
	 */
	public List<Xiaohcyssk> queryXiaohcysskObjectByShangxH(Map<String,String> map) {
		List<Xiaohcyssk> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryXiaohcysskH", map);
		return list ;
	}
	
}
