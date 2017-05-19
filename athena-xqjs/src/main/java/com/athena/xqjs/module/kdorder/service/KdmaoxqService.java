package com.athena.xqjs.module.kdorder.service;

import java.util.HashMap;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Component
public class KdmaoxqService extends BaseService {
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 查询指定用户中心下该版次的毛需求插入中间表
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-1-31
	 */
	public void selectXuq(String usercenter, String banc) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("xuqbc", banc);
		CommonFun.mapPrint(map, "查询指定用户中心下该版次的毛需求插入中间表selectXuq方法参数map");
		CommonFun.logger.debug("查询指定用户中心下该版次的毛需求插入中间表selectXuq方法的sql语句：kdorder.queryKDMaoxqmxObjectAll");
		userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "插入KD毛需求中间表", "保存开始");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKDMaoxqmxObjectAll", map);
		userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "插入KD毛需求中间表", "保存结束");
	
	}
	
	
	/**
	 * 查询指定用户中心下该版次的毛需求插入中间表
	 * @author 袁修瑞
	 * @date 2012-12-10
	 */
	public void selectNewXuq(String usercenter, String banc,String zhizlx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("xuqbc", banc);
		map.put("zhizlx", zhizlx);
		CommonFun.mapPrint(map, "查询指定用户中心下该版次的毛需求插入中间表selectNewXuq方法参数map");
		CommonFun.logger.debug("查询指定用户中心下该版次的毛需求插入中间表selectNewXuq方法的sql语句：kdorder.queryKDMxqmxAndGysObjectAll");
		userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "插入KD毛需求中间表", "保存开始");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKDMxqmxAndGysObjectAll", map);
		userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "插入KD毛需求中间表", "保存结束");
	
	}

	/**
	 * 无条件删除全部
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-1-31
	 */
	@Transactional
	public boolean doAllDelete() {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteAllOfKDMaoxq");
		return count > 0;
	}

	public boolean doDeleteOne(Map map) {
		CommonFun.mapPrint(map, "剔除kd件中间表KDMaoxq中无用参数方法doDeleteOne参数map");
		CommonFun.logger.debug("剔除kd件中间表KDMaoxq中无用参数方法doDeleteOne的sql语句为：kdorder.deleteOneOfKDMaoxq");
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteOneOfKDMaoxq", map);
		return count > 0;
	}

}
