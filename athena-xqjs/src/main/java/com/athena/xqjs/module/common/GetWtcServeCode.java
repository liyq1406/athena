package com.athena.xqjs.module.common;

import java.util.HashMap;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.XqjsWtcDy;

@SuppressWarnings("rawtypes")
public class GetWtcServeCode extends BaseService {
	/**
	 * 获取WTC资源名称
	 * 
	 * @param usercenter
	 * @return
	 */
	public String getResourceName(String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		XqjsWtcDy wd = (XqjsWtcDy) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWtcdy", map).get(0);
		return wd.getZiybh();
	}

	/**
	 * 根据用户中心、功能名称得到WTC服务代码
	 * 
	 * @param usercenter
	 * @param gongndm
	 * @return
	 */
	public String getWtcServeCode(String usercenter, String gongndm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("gongndm", gongndm);
		XqjsWtcDy wd = (XqjsWtcDy) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWtcdy", map).get(0);
		return wd.getFuwdm();
	}

	/**
	 * 根据用户中心、功能名称得到WTC服务代码与资源名称
	 * 
	 * @param usercenter
	 * @param gongndm
	 * @return
	 */
	public Map<String, String> getZymAndFwdm(String usercenter, String gongndm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("gongndm", gongndm);
		XqjsWtcDy wd = (XqjsWtcDy) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWtcdy", map).get(0);
		map.put("ziymc", wd.getZiybh());
		map.put("fuwdm", wd.getFuwdm());
		return map;
	}
}
