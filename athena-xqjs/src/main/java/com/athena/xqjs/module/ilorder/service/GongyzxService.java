package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongyzx;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:工业周序类
 * </p>
 * <p>
 * Description:工业周序类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 陈骏
 * @version v1.0
 * @date 2011-12-27
 */
@SuppressWarnings("rawtypes")
@Component
public class GongyzxService extends BaseService {
	/**
	 * 查询CalendarGroup返回实体
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String gongyzx, 工业周序
	 */
	public Gongyzx queryGongyzx(String gongyzx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gongyzx", gongyzx);
		Gongyzx gongYzx = new Gongyzx();
		gongYzx.setGongyzx(gongyzx);
		return (Gongyzx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTimeZx", map);
	}

	@SuppressWarnings("unchecked")
	public List<Gongyzx> queryGongyzx() {
		return (List<Gongyzx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTimeZx");
	}

	/**
	 * 查询CKX_CALENDAR_GONGYZX返回实体
	 * 
	 * @author NIESY
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String rq, 日期
	 */
	public Gongyzx queryGongyzxByRq(String rq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("rq", rq);
		return (Gongyzx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTimeZx", map);
	}

	/**
	 * 查询CKX_CALENDAR_GONGYZX返回指定工业周序（包括指定工业周序）的LIST集合
	 * 
	 * @author NIESY
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String gongyzx, 工业周序
	 */
	@SuppressWarnings("unchecked")
	public List<Gongyzx> queryGongyzxByApointZx(String gongyzx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("apointzx", gongyzx);
		return (List<Gongyzx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTimeZx", map);
	}
	@SuppressWarnings("unchecked")
	public List<Gongyzx> queryVGongyzx(Map<String,String> map) {
		return (List<Gongyzx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryVTimeZx",map);
	}

}
